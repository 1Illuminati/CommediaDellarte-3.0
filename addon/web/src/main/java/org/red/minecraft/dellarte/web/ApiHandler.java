package org.red.minecraft.dellarte.web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import me.lucko.spark.api.Spark;
import me.lucko.spark.api.SparkProvider;
import me.lucko.spark.api.gc.GarbageCollector;
import me.lucko.spark.api.statistic.StatisticWindow;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.management.ManagementFactory;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ApiHandler implements HttpHandler {

    private final CommediaDellarteWebPlugin plugin;
    private final ConsoleCapture consoleCapture;

    private static final long START_TIME = ManagementFactory.getRuntimeMXBean().getStartTime();

    public ApiHandler(CommediaDellarteWebPlugin plugin, ConsoleCapture consoleCapture) {
        this.plugin = plugin;
        this.consoleCapture = consoleCapture;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        addCorsHeaders(exchange);

        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        switch (path) {
            case "/api/stats":        handleStats(exchange);       break;
            case "/api/console":      handleConsole(exchange);     break;
            case "/api/command":      handleCommand(exchange);     break;
            case "/api/files":        handleFiles(exchange);       break;
            case "/api/shutdown":     handleShutdown(exchange);    break;
            case "/api/restart":      handleRestart(exchange);     break;
            case "/api/player/info":  handlePlayerInfo(exchange);  break;
            case "/api/player/kick":  handlePlayerKick(exchange);  break;
            case "/api/player/ban":   handlePlayerBan(exchange);   break;
            case "/api/plugins":      handlePlugins(exchange);     break;
            default:
                sendJson(exchange, 404, "{\"error\":\"not found\"}");
        }
    }

    // ── /api/stats ─────────────────────────────────────────────────────────

    private void handleStats(HttpExchange exchange) throws IOException {
        double tps1s = -1, tps5m = -1, tps15m = -1;
        double msptMean = -1, mspt95 = -1;
        double cpuProc = -1, cpuSys = -1;
        String gcJson = "[]";

        try {
            Spark spark = SparkProvider.get();

            var tps = spark.tps();
            if (tps != null) {
                tps1s  = tps.poll(StatisticWindow.TicksPerSecond.SECONDS_10);
                tps5m  = tps.poll(StatisticWindow.TicksPerSecond.MINUTES_5);
                tps15m = tps.poll(StatisticWindow.TicksPerSecond.MINUTES_15);
            }

            var mspt = spark.mspt();
            if (mspt != null) {
                var m = mspt.poll(StatisticWindow.MillisPerTick.MINUTES_1);
                if (m != null) { msptMean = m.mean(); mspt95 = m.percentile95th(); }
            }

            var cpu = spark.cpuProcess();
            if (cpu != null) cpuProc = cpu.poll(StatisticWindow.CpuUsage.MINUTES_1) * 100;

            var cpuS = spark.cpuSystem();
            if (cpuS != null) cpuSys = cpuS.poll(StatisticWindow.CpuUsage.MINUTES_1) * 100;

            Map<String, GarbageCollector> gcMap = spark.gc();
            if (gcMap != null && !gcMap.isEmpty()) {
                StringBuilder sb = new StringBuilder("[");
                boolean first = true;
                for (GarbageCollector gc : gcMap.values()) {
                    if (!first) sb.append(",");
                    sb.append(String.format(
                            "{\"name\":\"%s\",\"avgFrequency\":%d,\"avgTime\":%.2f}",
                            esc(gc.name()), gc.avgFrequency(), gc.avgTime()));
                    first = false;
                }
                gcJson = sb.append("]").toString();
            }
        } catch (IllegalStateException ignored) {}

        Runtime rt = Runtime.getRuntime();
        long memUsed  = (rt.totalMemory() - rt.freeMemory()) / 1024 / 1024;
        long memTotal = rt.totalMemory() / 1024 / 1024;
        long memMax   = rt.maxMemory()   / 1024 / 1024;
        long uptimeSec = (System.currentTimeMillis() - START_TIME) / 1000;

        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        StringBuilder playerSb = new StringBuilder("[");
        boolean pFirst = true;
        for (Player p : players) {
            if (!pFirst) playerSb.append(",");
            playerSb.append(String.format(
                    "{\"name\":\"%s\",\"world\":\"%s\",\"health\":%.1f,\"ping\":%d}",
                    esc(p.getName()), esc(p.getWorld().getName()), p.getHealth(), p.getPing()));
            pFirst = false;
        }
        playerSb.append("]");

        String json = String.format(
                "{\"tps\":{\"s10\":%.2f,\"m5\":%.2f,\"m15\":%.2f}," +
                "\"mspt\":{\"mean\":%.2f,\"p95\":%.2f}," +
                "\"cpu\":{\"process\":%.2f,\"system\":%.2f}," +
                "\"gc\":%s," +
                "\"memory\":{\"used\":%d,\"total\":%d,\"max\":%d}," +
                "\"uptime\":%d," +
                "\"players\":%s," +
                "\"onlineCount\":%d," +
                "\"maxPlayers\":%d," +
                "\"version\":\"%s\"}",
                tps1s, tps5m, tps15m,
                msptMean, mspt95,
                cpuProc, cpuSys,
                gcJson,
                memUsed, memTotal, memMax,
                uptimeSec,
                playerSb, players.size(), Bukkit.getMaxPlayers(),
                esc(Bukkit.getVersion()));

        sendJson(exchange, 200, json);
    }

    // ── /api/console ──────────────────────────────────────────────────────

    private void handleConsole(HttpExchange exchange) throws IOException {
        List<String> lines = consoleCapture.getLines();
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < lines.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append("\"").append(esc(lines.get(i))).append("\"");
        }
        sendJson(exchange, 200, sb.append("]").toString());
    }

    // ── /api/command ──────────────────────────────────────────────────────

    private void handleCommand(HttpExchange exchange) throws IOException {
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            sendJson(exchange, 405, "{\"error\":\"POST required\"}");
            return;
        }
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        String cmd = extractJsonString(body, "command");
        if (cmd == null || cmd.isBlank()) {
            sendJson(exchange, 400, "{\"error\":\"command required\"}");
            return;
        }
        final String finalCmd = cmd.trim();
        Bukkit.getScheduler().runTask(plugin, () ->
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), finalCmd));
        sendJson(exchange, 200, "{\"ok\":true,\"command\":\"" + esc(finalCmd) + "\"}");
    }

    // ── /api/files ────────────────────────────────────────────────────────

    private void handleFiles(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        String relPath = "";
        if (query != null && query.startsWith("path=")) {
            relPath = URLDecoder.decode(query.substring(5), StandardCharsets.UTF_8);
        }

        File serverRoot = plugin.getDataFolder().getParentFile().getParentFile();
        File target = new File(serverRoot, relPath).getCanonicalFile();

        if (!target.getPath().startsWith(serverRoot.getCanonicalPath())) {
            sendJson(exchange, 403, "{\"error\":\"forbidden\"}");
            return;
        }
        if (!target.exists()) {
            sendJson(exchange, 404, "{\"error\":\"not found\"}");
            return;
        }

        if (target.isDirectory()) {
            File[] children = target.listFiles();
            StringBuilder sb = new StringBuilder("{\"type\":\"dir\",\"path\":\"")
                    .append(esc(relPath)).append("\",\"items\":[");
            if (children != null) {
                Arrays.sort(children, Comparator.comparing(File::isFile).thenComparing(File::getName));
                boolean first = true;
                for (File f : children) {
                    if (!first) sb.append(",");
                    sb.append(String.format(
                            "{\"name\":\"%s\",\"isDir\":%b,\"size\":%d,\"modified\":%d}",
                            esc(f.getName()), f.isDirectory(),
                            f.isDirectory() ? dirSize(f) : f.length(),
                            f.lastModified()));
                    first = false;
                }
            }
            sendJson(exchange, 200, sb.append("]}").toString());
        } else {
            // 스트리밍 다운로드 (메모리에 전부 올리지 않음)
            long fileSize = target.length();
            exchange.getResponseHeaders().set("Content-Type", "application/octet-stream");
            exchange.getResponseHeaders().set("Content-Disposition",
                    "attachment; filename=\"" + target.getName() + "\"");
            exchange.sendResponseHeaders(200, fileSize);
            try (OutputStream os = exchange.getResponseBody();
                 FileInputStream fis = new FileInputStream(target)) {
                byte[] buf = new byte[65536];
                int n;
                while ((n = fis.read(buf)) != -1) os.write(buf, 0, n);
            }
        }
    }

    // ── /api/shutdown ─────────────────────────────────────────────────────

    private void handleShutdown(HttpExchange exchange) throws IOException {
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            sendJson(exchange, 405, "{\"error\":\"POST required\"}"); return;
        }
        sendJson(exchange, 200, "{\"ok\":true}");
        Bukkit.getScheduler().runTaskLater(plugin, Bukkit::shutdown, 20L);
    }

    // ── /api/restart ──────────────────────────────────────────────────────

    private void handleRestart(HttpExchange exchange) throws IOException {
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            sendJson(exchange, 405, "{\"error\":\"POST required\"}"); return;
        }
        sendJson(exchange, 200, "{\"ok\":true}");
        Bukkit.getScheduler().runTaskLater(plugin, () -> Bukkit.spigot().restart(), 20L);
    }

    // ── /api/player/info ──────────────────────────────────────────────────

    private void handlePlayerInfo(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        String name = "";
        if (query != null && query.startsWith("name=")) {
            name = URLDecoder.decode(query.substring(5), StandardCharsets.UTF_8);
        }

        Player player = Bukkit.getPlayerExact(name);
        if (player == null) {
            sendJson(exchange, 404, "{\"error\":\"플레이어를 찾을 수 없습니다.\"}");
            return;
        }

        String ip = (player.getAddress() != null) ? player.getAddress().getHostString() : "unknown";
        Long joinTime = plugin.getJoinTimes().get(player.getUniqueId());
        long sessionSec = (joinTime != null) ? (System.currentTimeMillis() - joinTime) / 1000 : -1;

        double maxHealth = -1;
        try {
            var attr = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            if (attr != null) maxHealth = attr.getValue();
        } catch (Exception ignored) {}

        String json = String.format(
                "{\"name\":\"%s\",\"uuid\":\"%s\",\"ip\":\"%s\"," +
                "\"world\":\"%s\",\"x\":%.1f,\"y\":%.1f,\"z\":%.1f," +
                "\"health\":%.1f,\"maxHealth\":%.1f,\"foodLevel\":%d," +
                "\"gameMode\":\"%s\",\"level\":%d," +
                "\"firstPlayed\":%d,\"sessionSeconds\":%d,\"ping\":%d}",
                esc(player.getName()),
                player.getUniqueId().toString(),
                esc(ip),
                esc(player.getWorld().getName()),
                player.getLocation().getX(),
                player.getLocation().getY(),
                player.getLocation().getZ(),
                player.getHealth(),
                maxHealth,
                player.getFoodLevel(),
                esc(player.getGameMode().name()),
                player.getLevel(),
                player.getFirstPlayed(),
                sessionSec,
                player.getPing());

        sendJson(exchange, 200, json);
    }

    // ── /api/player/kick ──────────────────────────────────────────────────

    private void handlePlayerKick(HttpExchange exchange) throws IOException {
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            sendJson(exchange, 405, "{\"error\":\"POST required\"}"); return;
        }
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        String name   = extractJsonString(body, "name");
        String reason = extractJsonString(body, "reason");
        if (reason == null || reason.isBlank()) reason = "웹 대시보드에서 추방되었습니다.";
        if (name == null || name.isBlank()) {
            sendJson(exchange, 400, "{\"error\":\"name required\"}"); return;
        }

        final String fName = name;
        final String fReason = reason;
        Bukkit.getScheduler().runTask(plugin, () -> {
            Player p = Bukkit.getPlayerExact(fName);
            if (p != null) p.kickPlayer(fReason);
        });
        sendJson(exchange, 200, "{\"ok\":true}");
    }

    // ── /api/player/ban ───────────────────────────────────────────────────

    private void handlePlayerBan(HttpExchange exchange) throws IOException {
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            sendJson(exchange, 405, "{\"error\":\"POST required\"}"); return;
        }
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        String name   = extractJsonString(body, "name");
        String reason = extractJsonString(body, "reason");
        if (reason == null || reason.isBlank()) reason = "웹 대시보드에서 밴되었습니다.";
        if (name == null || name.isBlank()) {
            sendJson(exchange, 400, "{\"error\":\"name required\"}"); return;
        }

        final String fName = name;
        final String fReason = reason;
        Bukkit.getScheduler().runTask(plugin, () -> {
            Bukkit.getBanList(org.bukkit.BanList.Type.NAME)
                    .addBan(fName, fReason, null, null);
            Player p = Bukkit.getPlayerExact(fName);
            if (p != null) p.kickPlayer(fReason);
        });
        sendJson(exchange, 200, "{\"ok\":true}");
    }

    // ── /api/plugins ──────────────────────────────────────────────────────

    private void handlePlugins(HttpExchange exchange) throws IOException {
        Plugin[] plugins = Bukkit.getPluginManager().getPlugins();
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (Plugin pl : plugins) {
            if (!first) sb.append(",");
            sb.append(String.format(
                    "{\"name\":\"%s\",\"version\":\"%s\",\"enabled\":%b}",
                    esc(pl.getName()),
                    esc(pl.getDescription().getVersion()),
                    pl.isEnabled()));
            first = false;
        }
        sendJson(exchange, 200, sb.append("]").toString());
    }

    // ── 유틸 ──────────────────────────────────────────────────────────────

    private long dirSize(File dir) {
        long size = 0;
        File[] files = dir.listFiles();
        if (files == null) return 0;
        for (File f : files) size += f.isDirectory() ? dirSize(f) : f.length();
        return size;
    }

    private String esc(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "");
    }

    private String extractJsonString(String json, String key) {
        String search = "\"" + key + "\"";
        int idx = json.indexOf(search);
        if (idx < 0) return null;
        int colon = json.indexOf(':', idx + search.length());
        if (colon < 0) return null;
        int open = json.indexOf('"', colon + 1);
        if (open < 0) return null;
        StringBuilder sb = new StringBuilder();
        for (int i = open + 1; i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == '\\' && i + 1 < json.length()) { sb.append(json.charAt(++i)); continue; }
            if (c == '"') break;
            sb.append(c);
        }
        return sb.toString();
    }

    private void addCorsHeaders(HttpExchange ex) {
        ex.getResponseHeaders().set("Access-Control-Allow-Origin",  "*");
        ex.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        ex.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
    }

    private void sendJson(HttpExchange ex, int code, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        ex.sendResponseHeaders(code, bytes.length);
        try (OutputStream os = ex.getResponseBody()) { os.write(bytes); }
    }
}