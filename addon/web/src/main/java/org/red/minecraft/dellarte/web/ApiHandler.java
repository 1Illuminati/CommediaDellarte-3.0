package org.red.minecraft.dellarte.web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import me.lucko.spark.api.Spark;
import me.lucko.spark.api.SparkProvider;
import me.lucko.spark.api.gc.GarbageCollector;
import me.lucko.spark.api.statistic.StatisticWindow;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.red.minecraft.dellarte.library.CommediaDellarte;
import org.red.minecraft.dellarte.library.data.IDataStorage;
import org.red.minecraft.dellarte.library.util.A_DataMap;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ApiHandler implements HttpHandler {

    private final CommediaDellarteWebPlugin plugin;
    private final ConsoleCapture consoleCapture;

    private static final long START_TIME  = ManagementFactory.getRuntimeMXBean().getStartTime();
    private static final long SESSION_TTL = 24 * 60 * 60 * 1000L;

    // ── 인증 ─────────────────────────────────────────────────────────────
    private final boolean authEnabled;
    private final String  authUsername;
    private final String  authPassword;
    private final Map<String, Long> sessions = new ConcurrentHashMap<>();

    public ApiHandler(CommediaDellarteWebPlugin plugin, ConsoleCapture consoleCapture) {
        this.plugin = plugin;
        this.consoleCapture = consoleCapture;
        this.authEnabled  = plugin.getConfig().getBoolean("web.auth.enabled",  false);
        this.authUsername = plugin.getConfig().getString ("web.auth.username", "admin");
        this.authPassword = plugin.getConfig().getString ("web.auth.password", "changeme");
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        addCorsHeaders(exchange);

        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        boolean isAuthPath = path.startsWith("/api/auth/");
        if (authEnabled && !isAuthPath && !isAuthenticated(exchange)) {
            sendJson(exchange, 401, "{\"error\":\"unauthorized\"}");
            return;
        }

        switch (path) {
            case "/api/stats":          handleStats(exchange);        break;
            case "/api/console":        handleConsole(exchange);      break;
            case "/api/command":        handleCommand(exchange);      break;
            case "/api/files":          handleFiles(exchange);        break;
            case "/api/shutdown":       handleShutdown(exchange);     break;
            case "/api/restart":        handleRestart(exchange);      break;
            case "/api/player/info":    handlePlayerInfo(exchange);   break;
            case "/api/player/kick":    handlePlayerKick(exchange);   break;
            case "/api/player/ban":     handlePlayerBan(exchange);    break;
            case "/api/plugins":        handlePlugins(exchange);      break;
            case "/api/auth/login":     handleAuthLogin(exchange);    break;
            case "/api/auth/status":    handleAuthStatus(exchange);   break;
            case "/api/auth/logout":    handleAuthLogout(exchange);   break;
            case "/api/storage/load":   handleStorageLoad(exchange);   break;
            case "/api/storage/set":    handleStorageSet(exchange);    break;
            case "/api/storage/save":   handleStorageSave(exchange);   break;
            case "/api/storage/add":    handleStorageAdd(exchange);    break;
            case "/api/storage/delete": handleStorageDelete(exchange); break;
            default:
                sendJson(exchange, 404, "{\"error\":\"not found\"}");
        }
    }

    // ── /api/auth/* ───────────────────────────────────────────────────────

    private void handleAuthLogin(HttpExchange exchange) throws IOException {
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            sendJson(exchange, 405, "{\"error\":\"POST required\"}"); return;
        }
        if (!authEnabled) {
            sendJson(exchange, 200, "{\"ok\":true,\"token\":\"\"}"); return;
        }
        String body     = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        String username = extractJsonString(body, "username");
        String password = extractJsonString(body, "password");

        if (authUsername.equals(username) && authPassword.equals(password)) {
            String token = UUID.randomUUID().toString().replace("-", "");
            sessions.put(token, System.currentTimeMillis() + SESSION_TTL);
            sessions.entrySet().removeIf(e -> System.currentTimeMillis() > e.getValue());
            sendJson(exchange, 200, "{\"ok\":true,\"token\":\"" + token + "\"}");
        } else {
            sendJson(exchange, 401, "{\"ok\":false,\"error\":\"아이디 또는 비밀번호가 올바르지 않습니다.\"}");
        }
    }

    private void handleAuthStatus(HttpExchange exchange) throws IOException {
        if (!authEnabled) {
            sendJson(exchange, 200, "{\"enabled\":false,\"authenticated\":true}"); return;
        }
        sendJson(exchange, 200,
                "{\"enabled\":true,\"authenticated\":" + isAuthenticated(exchange) + "}");
    }

    private void handleAuthLogout(HttpExchange exchange) throws IOException {
        String header = exchange.getRequestHeaders().getFirst("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            sessions.remove(header.substring(7));
        }
        sendJson(exchange, 200, "{\"ok\":true}");
    }

    // ── /api/stats ────────────────────────────────────────────────────────

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
                    sb.append(String.format("{\"name\":\"%s\",\"avgFrequency\":%d,\"avgTime\":%.2f}",
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
            playerSb.append(String.format("{\"name\":\"%s\",\"world\":\"%s\",\"health\":%.1f,\"ping\":%d}",
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
                tps1s, tps5m, tps15m, msptMean, mspt95,
                cpuProc, cpuSys, gcJson,
                memUsed, memTotal, memMax,
                uptimeSec, playerSb, players.size(), Bukkit.getMaxPlayers(),
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
            sendJson(exchange, 405, "{\"error\":\"POST required\"}"); return;
        }
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        String cmd = extractJsonString(body, "command");
        if (cmd == null || cmd.isBlank()) {
            sendJson(exchange, 400, "{\"error\":\"command required\"}"); return;
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
            sendJson(exchange, 403, "{\"error\":\"forbidden\"}"); return;
        }
        if (!target.exists()) {
            sendJson(exchange, 404, "{\"error\":\"not found\"}"); return;
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
                    sb.append(String.format("{\"name\":\"%s\",\"isDir\":%b,\"size\":%d,\"modified\":%d}",
                            esc(f.getName()), f.isDirectory(),
                            f.isDirectory() ? dirSize(f) : f.length(), f.lastModified()));
                    first = false;
                }
            }
            sendJson(exchange, 200, sb.append("]}").toString());
        } else {
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

    // ── /api/shutdown / /api/restart ──────────────────────────────────────

    private void handleShutdown(HttpExchange exchange) throws IOException {
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            sendJson(exchange, 405, "{\"error\":\"POST required\"}"); return;
        }
        sendJson(exchange, 200, "{\"ok\":true}");
        Bukkit.getScheduler().runTaskLater(plugin, Bukkit::shutdown, 20L);
    }

    private void handleRestart(HttpExchange exchange) throws IOException {
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            sendJson(exchange, 405, "{\"error\":\"POST required\"}"); return;
        }
        sendJson(exchange, 200, "{\"ok\":true}");
        Bukkit.getScheduler().runTaskLater(plugin, () -> Bukkit.spigot().restart(), 20L);
    }

    // ── /api/player/* ─────────────────────────────────────────────────────

    private void handlePlayerInfo(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        String name = "";
        if (query != null && query.startsWith("name=")) {
            name = URLDecoder.decode(query.substring(5), StandardCharsets.UTF_8);
        }
        Player player = Bukkit.getPlayerExact(name);
        if (player == null) {
            sendJson(exchange, 404, "{\"error\":\"플레이어를 찾을 수 없습니다.\"}"); return;
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
                esc(player.getName()), player.getUniqueId(), esc(ip),
                esc(player.getWorld().getName()),
                player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(),
                player.getHealth(), maxHealth, player.getFoodLevel(),
                esc(player.getGameMode().name()), player.getLevel(),
                player.getFirstPlayed(), sessionSec, player.getPing());
        sendJson(exchange, 200, json);
    }

    private void handlePlayerKick(HttpExchange exchange) throws IOException {
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            sendJson(exchange, 405, "{\"error\":\"POST required\"}"); return;
        }
        String body   = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        String name   = extractJsonString(body, "name");
        String reason = extractJsonString(body, "reason");
        if (reason == null || reason.isBlank()) reason = "웹 대시보드에서 추방되었습니다.";
        if (name == null || name.isBlank()) {
            sendJson(exchange, 400, "{\"error\":\"name required\"}"); return;
        }
        final String fName = name, fReason = reason;
        Bukkit.getScheduler().runTask(plugin, () -> {
            Player p = Bukkit.getPlayerExact(fName);
            if (p != null) p.kickPlayer(fReason);
        });
        sendJson(exchange, 200, "{\"ok\":true}");
    }

    private void handlePlayerBan(HttpExchange exchange) throws IOException {
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            sendJson(exchange, 405, "{\"error\":\"POST required\"}"); return;
        }
        String body   = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        String name   = extractJsonString(body, "name");
        String reason = extractJsonString(body, "reason");
        if (reason == null || reason.isBlank()) reason = "웹 대시보드에서 밴되었습니다.";
        if (name == null || name.isBlank()) {
            sendJson(exchange, 400, "{\"error\":\"name required\"}"); return;
        }
        final String fName = name, fReason = reason;
        Bukkit.getScheduler().runTask(plugin, () -> {
            Bukkit.getBanList(org.bukkit.BanList.Type.NAME).addBan(fName, fReason, null, null);
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
            sb.append(String.format("{\"name\":\"%s\",\"version\":\"%s\",\"enabled\":%b}",
                    esc(pl.getName()), esc(pl.getDescription().getVersion()), pl.isEnabled()));
            first = false;
        }
        sendJson(exchange, 200, sb.append("]").toString());
    }

    // ── /api/storage/load ─────────────────────────────────────────────────
    // GET ?storage=namespace:name
    // 해당 스토리지가 존재하는지 확인 후 메모리에 로드된 데이터를 반환

    private void handleStorageLoad(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        String storageParam = "";
        if (query != null) {
            for (String param : query.split("&")) {
                if (param.startsWith("storage=")) {
                    storageParam = URLDecoder.decode(param.substring(8), StandardCharsets.UTF_8);
                    break;
                }
            }
        }
        if (storageParam.isBlank()) {
            sendJson(exchange, 400, "{\"error\":\"storage 파라미터가 필요합니다\"}"); return;
        }

        String[] parts = storageParam.split(":", 2);
        if (parts.length != 2 || parts[0].isBlank() || parts[1].isBlank()) {
            sendJson(exchange, 400, "{\"error\":\"형식 오류: namespace:name\"}"); return;
        }

        NamespacedKey nsKey;
        try {
            nsKey = new NamespacedKey(parts[0].toLowerCase().trim(), parts[1].toLowerCase().trim());
        } catch (IllegalArgumentException e) {
            sendJson(exchange, 400, "{\"error\":\"잘못된 키 형식: " + esc(e.getMessage()) + "\"}");
            return;
        }

        if (!CommediaDellarte.containStorage(nsKey)) {
            sendJson(exchange, 404, "{\"error\":\"스토리지가 존재하지 않습니다: " +
                    esc(nsKey.getNamespace() + ":" + nsKey.getKey()) + "\"}");
            return;
        }

        IDataStorage storage = CommediaDellarte.getStorage(nsKey);
        if (storage == null) {
            sendJson(exchange, 404, "{\"error\":\"스토리지를 가져올 수 없습니다\"}"); return;
        }

        Map<String, A_DataMap> cache = getDataMapCache(storage);
        StringBuilder sb = new StringBuilder("{\"storage\":\"")
                .append(esc(nsKey.getNamespace() + ":" + nsKey.getKey()))
                .append("\",\"entries\":[");
        boolean first = true;
        for (Map.Entry<String, A_DataMap> e : cache.entrySet()) {
            if (!first) sb.append(",");
            sb.append("{\"dataKey\":\"").append(esc(e.getKey()))
              .append("\",\"data\":").append(dataMapToJson(e.getValue())).append("}");
            first = false;
        }
        sendJson(exchange, 200, sb.append("]}").toString());
    }

    // ── /api/storage/set ──────────────────────────────────────────────────
    // POST { storage, dataKey, field, type, value }

    private void handleStorageSet(HttpExchange exchange) throws IOException {
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            sendJson(exchange, 405, "{\"error\":\"POST required\"}"); return;
        }
        String body       = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        String storageKey = extractJsonString(body, "storage");
        String dataKey    = extractJsonString(body, "dataKey");
        String field      = extractJsonString(body, "field");
        String type       = extractJsonString(body, "type");
        String value      = extractJsonString(body, "value");

        if (storageKey == null || dataKey == null || field == null || type == null || value == null) {
            sendJson(exchange, 400, "{\"error\":\"필수 파라미터 누락 (storage, dataKey, field, type, value)\"}");
            return;
        }

        String[] parts = storageKey.split(":", 2);
        if (parts.length != 2) {
            sendJson(exchange, 400, "{\"error\":\"잘못된 스토리지 키\"}"); return;
        }

        NamespacedKey nsKey;
        try {
            nsKey = new NamespacedKey(parts[0].toLowerCase(), parts[1].toLowerCase());
        } catch (IllegalArgumentException e) {
            sendJson(exchange, 400, "{\"error\":\"잘못된 키 형식\"}"); return;
        }

        if (!CommediaDellarte.containStorage(nsKey)) {
            sendJson(exchange, 404, "{\"error\":\"스토리지가 존재하지 않습니다\"}"); return;
        }
        IDataStorage storage = CommediaDellarte.getStorage(nsKey);
        if (storage == null) {
            sendJson(exchange, 404, "{\"error\":\"스토리지를 가져올 수 없습니다\"}"); return;
        }

        A_DataMap dataMap = storage.getDataMap(dataKey);
        if (dataMap == null) {
            sendJson(exchange, 404, "{\"error\":\"데이터 엔트리를 찾을 수 없습니다\"}"); return;
        }

        String[] pathParts = field.split("/");
        try {
            Object newValue = parseStorageValue(type, value);
            setDeepValue(dataMap, pathParts, 0, newValue);
            sendJson(exchange, 200, "{\"ok\":true}");
        } catch (Exception e) {
            sendJson(exchange, 400, "{\"error\":\"" + esc(e.getMessage()) + "\"}");
        }
    }

    /** A_DataMap 안에서 재귀적으로 경로를 탐색해 값을 설정 */
    @SuppressWarnings("unchecked")
    private void setDeepValue(A_DataMap map, String[] path, int idx, Object newValue) throws Exception {
        String key = path[idx];
        if (idx == path.length - 1) {
            map.put(key, newValue);
            return;
        }
        Object nested = map.getMap().get(key);
        if (nested instanceof A_DataMap adm) {
            setDeepValue(adm, path, idx + 1, newValue);
        } else if (nested instanceof ConfigurationSerializable cs) {
            Object restored = setDeepValueInCS(cs, path, idx + 1, newValue);
            map.put(key, restored);
        } else if (nested instanceof Map<?,?> m) {
            Map<String, Object> mutable = new HashMap<>((Map<String, Object>) m);
            setDeepValueInMap(mutable, path, idx + 1, newValue);
            map.put(key, mutable);
        } else if (nested instanceof List<?> l) {
            List<Object> mutable = new ArrayList<>((List<Object>) l);
            setDeepValueInList(mutable, path, idx + 1, newValue);
            map.put(key, mutable);
        } else {
            throw new IllegalArgumentException("탐색 불가 경로: " + key);
        }
    }

    /** CS 객체 내부 경로에서 값 변경 후 복구 */
    @SuppressWarnings("unchecked")
    private Object setDeepValueInCS(ConfigurationSerializable cs, String[] path, int idx, Object newValue) throws Exception {
        Map<String, Object> serialized = new HashMap<>(cs.serialize());
        String key = path[idx];
        if (key.equals("__class__")) throw new IllegalArgumentException("__class__ 는 수정 불가");
        if (idx == path.length - 1) {
            serialized.put(key, newValue);
        } else {
            Object nested = serialized.get(key);
            if (nested instanceof ConfigurationSerializable ncs) {
                serialized.put(key, setDeepValueInCS(ncs, path, idx + 1, newValue));
            } else if (nested instanceof Map<?,?> m) {
                Map<String, Object> mutable = new HashMap<>((Map<String, Object>) m);
                setDeepValueInMap(mutable, path, idx + 1, newValue);
                serialized.put(key, mutable);
            } else if (nested instanceof List<?> l) {
                List<Object> mutable = new ArrayList<>((List<Object>) l);
                setDeepValueInList(mutable, path, idx + 1, newValue);
                serialized.put(key, mutable);
            } else {
                throw new IllegalArgumentException("탐색 불가 CS 경로: " + key);
            }
        }
        return restoreConfigSerializable(serialized, cs.getClass().getName());
    }

    @SuppressWarnings("unchecked")
    private void setDeepValueInMap(Map<String, Object> map, String[] path, int idx, Object newValue) throws Exception {
        String key = path[idx];
        if (idx == path.length - 1) {
            map.put(key, newValue);
            return;
        }
        Object nested = map.get(key);
        if (nested instanceof Map<?,?> m) {
            Map<String, Object> mutable = new HashMap<>((Map<String, Object>) m);
            setDeepValueInMap(mutable, path, idx + 1, newValue);
            map.put(key, mutable);
        } else if (nested instanceof List<?> l) {
            List<Object> mutable = new ArrayList<>((List<Object>) l);
            setDeepValueInList(mutable, path, idx + 1, newValue);
            map.put(key, mutable);
        } else {
            throw new IllegalArgumentException("탐색 불가 Map 경로: " + key);
        }
    }

    @SuppressWarnings("unchecked")
    private void setDeepValueInList(List<Object> list, String[] path, int idx, Object newValue) throws Exception {
        int i;
        try { i = Integer.parseInt(path[idx]); } catch (NumberFormatException e) {
            throw new IllegalArgumentException("List 인덱스가 숫자가 아님: " + path[idx]);
        }
        if (i < 0 || i >= list.size()) throw new IndexOutOfBoundsException("List 인덱스 범위 초과: " + i);
        if (idx == path.length - 1) {
            list.set(i, newValue);
            return;
        }
        Object nested = list.get(i);
        if (nested instanceof Map<?,?> m) {
            Map<String, Object> mutable = new HashMap<>((Map<String, Object>) m);
            setDeepValueInMap(mutable, path, idx + 1, newValue);
            list.set(i, mutable);
        } else if (nested instanceof List<?> l) {
            List<Object> mutable = new ArrayList<>((List<Object>) l);
            setDeepValueInList(mutable, path, idx + 1, newValue);
            list.set(i, mutable);
        } else if (nested instanceof ConfigurationSerializable cs) {
            list.set(i, setDeepValueInCS(cs, path, idx + 1, newValue));
        } else {
            throw new IllegalArgumentException("탐색 불가 List 요소 경로");
        }
    }

    // ── /api/storage/add ─────────────────────────────────────────────────────
    // POST { storage, dataKey, container, newKey }
    // container = "" 이면 루트 DataMap에 추가, 그렇지 않으면 경로를 탐색
    // newKey = 빈 문자열이면 List에 append (키 불필요)

    private void handleStorageAdd(HttpExchange exchange) throws IOException {
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            sendJson(exchange, 405, "{\"error\":\"POST required\"}"); return;
        }
        String body          = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        String storageKey    = extractJsonString(body, "storage");
        String dataKey       = extractJsonString(body, "dataKey");
        String containerPath = extractJsonString(body, "container");
        String newKey        = extractJsonString(body, "newKey");

        if (storageKey == null || dataKey == null) {
            sendJson(exchange, 400, "{\"error\":\"storage, dataKey 필수\"}"); return;
        }
        if (containerPath == null) containerPath = "";

        String[] parts = storageKey.split(":", 2);
        if (parts.length != 2) { sendJson(exchange, 400, "{\"error\":\"잘못된 스토리지 키\"}"); return; }
        NamespacedKey nsKey;
        try { nsKey = new NamespacedKey(parts[0].toLowerCase(), parts[1].toLowerCase()); }
        catch (IllegalArgumentException e) { sendJson(exchange, 400, "{\"error\":\"잘못된 키 형식\"}"); return; }

        if (!CommediaDellarte.containStorage(nsKey)) {
            sendJson(exchange, 404, "{\"error\":\"스토리지가 존재하지 않습니다\"}"); return;
        }
        IDataStorage storage = CommediaDellarte.getStorage(nsKey);
        if (storage == null) { sendJson(exchange, 404, "{\"error\":\"스토리지를 가져올 수 없습니다\"}"); return; }

        A_DataMap rootMap = storage.getDataMap(dataKey);
        if (rootMap == null) { sendJson(exchange, 404, "{\"error\":\"데이터 엔트리를 찾을 수 없습니다\"}"); return; }

        try {
            addToContainer(rootMap, containerPath, newKey);
            sendJson(exchange, 200, "{\"ok\":true}");
        } catch (Exception e) {
            sendJson(exchange, 400, "{\"error\":\"" + esc(e.getMessage()) + "\"}");
        }
    }

    // ── /api/storage/delete ───────────────────────────────────────────────────
    // POST { storage, dataKey, field }

    private void handleStorageDelete(HttpExchange exchange) throws IOException {
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            sendJson(exchange, 405, "{\"error\":\"POST required\"}"); return;
        }
        String body       = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        String storageKey = extractJsonString(body, "storage");
        String dataKey    = extractJsonString(body, "dataKey");
        String field      = extractJsonString(body, "field");

        if (storageKey == null || dataKey == null || field == null || field.isBlank()) {
            sendJson(exchange, 400, "{\"error\":\"storage, dataKey, field 필수\"}"); return;
        }

        String[] parts = storageKey.split(":", 2);
        if (parts.length != 2) { sendJson(exchange, 400, "{\"error\":\"잘못된 스토리지 키\"}"); return; }
        NamespacedKey nsKey;
        try { nsKey = new NamespacedKey(parts[0].toLowerCase(), parts[1].toLowerCase()); }
        catch (IllegalArgumentException e) { sendJson(exchange, 400, "{\"error\":\"잘못된 키 형식\"}"); return; }

        if (!CommediaDellarte.containStorage(nsKey)) {
            sendJson(exchange, 404, "{\"error\":\"스토리지가 존재하지 않습니다\"}"); return;
        }
        IDataStorage storage = CommediaDellarte.getStorage(nsKey);
        if (storage == null) { sendJson(exchange, 404, "{\"error\":\"스토리지를 가져올 수 없습니다\"}"); return; }

        A_DataMap rootMap = storage.getDataMap(dataKey);
        if (rootMap == null) { sendJson(exchange, 404, "{\"error\":\"데이터 엔트리를 찾을 수 없습니다\"}"); return; }

        String[] pathParts = field.split("/");
        try {
            deleteFromDataMap(rootMap, pathParts, 0);
            sendJson(exchange, 200, "{\"ok\":true}");
        } catch (Exception e) {
            sendJson(exchange, 400, "{\"error\":\"" + esc(e.getMessage()) + "\"}");
        }
    }

    // ── 추가 네비게이션 ───────────────────────────────────────────────────────

    @SuppressWarnings("unchecked")
    private void addToContainer(A_DataMap rootMap, String containerPath, String newKey) throws Exception {
        if (containerPath == null || containerPath.isBlank()) {
            if (newKey == null || newKey.isBlank()) throw new IllegalArgumentException("키 이름이 필요합니다");
            rootMap.put(newKey, "");
            return;
        }
        addToContainerInDataMap(rootMap, containerPath.split("/"), 0, newKey);
    }

    @SuppressWarnings("unchecked")
    private void addToContainerInDataMap(A_DataMap map, String[] path, int idx, String newKey) throws Exception {
        String key = path[idx];
        Object obj = map.getMap().get(key);
        if (idx == path.length - 1) {
            if (obj instanceof A_DataMap adm)           { if (newKey == null || newKey.isBlank()) throw new IllegalArgumentException("키 이름이 필요합니다"); adm.put(newKey, ""); }
            else if (obj instanceof List<?>)            { ((List<Object>) obj).add(""); }
            else if (obj instanceof Map<?,?>)           { if (newKey == null || newKey.isBlank()) throw new IllegalArgumentException("키 이름이 필요합니다"); ((Map<String, Object>) obj).put(newKey, ""); }
            else if (obj instanceof ConfigurationSerializable cs) {
                if (newKey == null || newKey.isBlank()) throw new IllegalArgumentException("키 이름이 필요합니다");
                Map<String, Object> ser = new HashMap<>(cs.serialize()); ser.put(newKey, "");
                ConfigurationSerializable restored = restoreConfigSerializable(ser, cs.getClass().getName());
                if (!restored.serialize().containsKey(newKey))
                    throw new IllegalArgumentException("CS 클래스가 해당 필드를 지원하지 않습니다: " + newKey);
                map.put(key, restored);
            } else throw new IllegalArgumentException("해당 경로는 컨테이너가 아닙니다: " + key);
        } else {
            if (obj instanceof A_DataMap adm)           addToContainerInDataMap(adm, path, idx + 1, newKey);
            else if (obj instanceof ConfigurationSerializable cs) map.put(key, addToContainerInCS(cs, path, idx + 1, newKey));
            else if (obj instanceof Map<?,?> m)         { Map<String, Object> mut = new HashMap<>((Map<String, Object>) m); addToContainerInMap(mut, path, idx + 1, newKey); map.put(key, mut); }
            else if (obj instanceof List<?> l)          { List<Object> mut = new ArrayList<>((List<Object>) l); addToContainerInList(mut, path, idx + 1, newKey); map.put(key, mut); }
            else throw new IllegalArgumentException("컨테이너 경로 오류: " + key);
        }
    }

    @SuppressWarnings("unchecked")
    private ConfigurationSerializable addToContainerInCS(ConfigurationSerializable cs, String[] path, int idx, String newKey) throws Exception {
        Map<String, Object> ser = new HashMap<>(cs.serialize());
        String key = path[idx]; Object obj = ser.get(key);
        if (idx == path.length - 1) {
            if (obj instanceof A_DataMap adm)           { if (newKey == null || newKey.isBlank()) throw new IllegalArgumentException("키 이름이 필요합니다"); adm.put(newKey, ""); }
            else if (obj instanceof List<?>)            { ((List<Object>) obj).add(""); }
            else if (obj instanceof Map<?,?>)           { if (newKey == null || newKey.isBlank()) throw new IllegalArgumentException("키 이름이 필요합니다"); ((Map<String, Object>) obj).put(newKey, ""); }
            else if (obj instanceof ConfigurationSerializable cs2) {
                if (newKey == null || newKey.isBlank()) throw new IllegalArgumentException("키 이름이 필요합니다");
                Map<String, Object> ser2 = new HashMap<>(cs2.serialize()); ser2.put(newKey, "");
                ConfigurationSerializable restored2 = restoreConfigSerializable(ser2, cs2.getClass().getName());
                if (!restored2.serialize().containsKey(newKey))
                    throw new IllegalArgumentException("CS 클래스가 해당 필드를 지원하지 않습니다: " + newKey);
                ser.put(key, restored2);
            } else throw new IllegalArgumentException("해당 경로는 컨테이너가 아닙니다: " + key);
        } else {
            if (obj instanceof A_DataMap adm)           addToContainerInDataMap(adm, path, idx + 1, newKey);
            else if (obj instanceof ConfigurationSerializable cs2) ser.put(key, addToContainerInCS(cs2, path, idx + 1, newKey));
            else if (obj instanceof Map<?,?> m)         { Map<String, Object> mut = new HashMap<>((Map<String, Object>) m); addToContainerInMap(mut, path, idx + 1, newKey); ser.put(key, mut); }
            else if (obj instanceof List<?> l)          { List<Object> mut = new ArrayList<>((List<Object>) l); addToContainerInList(mut, path, idx + 1, newKey); ser.put(key, mut); }
            else throw new IllegalArgumentException("컨테이너 경로 오류: " + key);
        }
        return restoreConfigSerializable(ser, cs.getClass().getName());
    }

    @SuppressWarnings("unchecked")
    private void addToContainerInMap(Map<String, Object> map, String[] path, int idx, String newKey) throws Exception {
        String key = path[idx]; Object obj = map.get(key);
        if (idx == path.length - 1) {
            if (obj instanceof A_DataMap adm)  { if (newKey == null || newKey.isBlank()) throw new IllegalArgumentException("키 이름이 필요합니다"); adm.put(newKey, ""); }
            else if (obj instanceof List<?>)   { ((List<Object>) obj).add(""); }
            else if (obj instanceof Map<?,?>)  { if (newKey == null || newKey.isBlank()) throw new IllegalArgumentException("키 이름이 필요합니다"); ((Map<String, Object>) obj).put(newKey, ""); }
            else throw new IllegalArgumentException("해당 경로는 컨테이너가 아닙니다: " + key);
        } else {
            if (obj instanceof A_DataMap adm)  addToContainerInDataMap(adm, path, idx + 1, newKey);
            else if (obj instanceof Map<?,?> m) { Map<String, Object> mut = new HashMap<>((Map<String, Object>) m); addToContainerInMap(mut, path, idx + 1, newKey); map.put(key, mut); }
            else if (obj instanceof List<?> l)  { List<Object> mut = new ArrayList<>((List<Object>) l); addToContainerInList(mut, path, idx + 1, newKey); map.put(key, mut); }
            else throw new IllegalArgumentException("컨테이너 경로 오류: " + key);
        }
    }

    @SuppressWarnings("unchecked")
    private void addToContainerInList(List<Object> list, String[] path, int idx, String newKey) throws Exception {
        int i; try { i = Integer.parseInt(path[idx]); } catch (NumberFormatException e) { throw new IllegalArgumentException("List 인덱스가 숫자가 아님: " + path[idx]); }
        if (i < 0 || i >= list.size()) throw new IndexOutOfBoundsException("List 인덱스 범위 초과: " + i);
        Object obj = list.get(i);
        if (idx == path.length - 1) {
            if (obj instanceof A_DataMap adm)  { if (newKey == null || newKey.isBlank()) throw new IllegalArgumentException("키 이름이 필요합니다"); adm.put(newKey, ""); }
            else if (obj instanceof List<?>)   { ((List<Object>) obj).add(""); }
            else if (obj instanceof Map<?,?>)  { if (newKey == null || newKey.isBlank()) throw new IllegalArgumentException("키 이름이 필요합니다"); ((Map<String, Object>) obj).put(newKey, ""); }
            else throw new IllegalArgumentException("해당 인덱스는 컨테이너가 아닙니다: " + i);
        } else {
            if (obj instanceof A_DataMap adm)  addToContainerInDataMap(adm, path, idx + 1, newKey);
            else if (obj instanceof Map<?,?> m) { Map<String, Object> mut = new HashMap<>((Map<String, Object>) m); addToContainerInMap(mut, path, idx + 1, newKey); list.set(i, mut); }
            else if (obj instanceof List<?> l)  { List<Object> mut = new ArrayList<>((List<Object>) l); addToContainerInList(mut, path, idx + 1, newKey); list.set(i, mut); }
            else throw new IllegalArgumentException("경로에 컨테이너 없음: " + i);
        }
    }

    // ── 삭제 네비게이션 ───────────────────────────────────────────────────────

    private void deleteFromDataMap(A_DataMap map, String[] path, int idx) throws Exception {
        String key = path[idx];
        if (idx == path.length - 1) { map.remove(key); return; }
        Object obj = map.getMap().get(key);
        if (obj instanceof A_DataMap adm)           deleteFromDataMap(adm, path, idx + 1);
        else if (obj instanceof ConfigurationSerializable cs) map.put(key, deleteFromCS(cs, path, idx + 1));
        else if (obj instanceof Map<?,?> m)         { @SuppressWarnings("unchecked") Map<String, Object> mut = new HashMap<>((Map<String, Object>) m); deleteFromMap(mut, path, idx + 1); map.put(key, mut); }
        else if (obj instanceof List<?> l)          { @SuppressWarnings("unchecked") List<Object> mut = new ArrayList<>((List<Object>) l); deleteFromList(mut, path, idx + 1); map.put(key, mut); }
        else throw new IllegalArgumentException("삭제 경로 오류: " + key);
    }

    @SuppressWarnings("unchecked")
    private Object deleteFromCS(ConfigurationSerializable cs, String[] path, int idx) throws Exception {
        Map<String, Object> ser = new HashMap<>(cs.serialize());
        String key = path[idx];
        if (idx == path.length - 1) { ser.remove(key); }
        else {
            Object obj = ser.get(key);
            if (obj instanceof A_DataMap adm)           deleteFromDataMap(adm, path, idx + 1);
            else if (obj instanceof ConfigurationSerializable cs2) ser.put(key, deleteFromCS(cs2, path, idx + 1));
            else if (obj instanceof Map<?,?> m)         { Map<String, Object> mut = new HashMap<>((Map<String, Object>) m); deleteFromMap(mut, path, idx + 1); ser.put(key, mut); }
            else if (obj instanceof List<?> l)          { List<Object> mut = new ArrayList<>((List<Object>) l); deleteFromList(mut, path, idx + 1); ser.put(key, mut); }
            else throw new IllegalArgumentException("삭제 경로 오류: " + key);
        }
        return restoreConfigSerializable(ser, cs.getClass().getName());
    }

    @SuppressWarnings("unchecked")
    private void deleteFromMap(Map<String, Object> map, String[] path, int idx) throws Exception {
        String key = path[idx];
        if (idx == path.length - 1) { map.remove(key); return; }
        Object obj = map.get(key);
        if (obj instanceof A_DataMap adm)  deleteFromDataMap(adm, path, idx + 1);
        else if (obj instanceof Map<?,?> m) { Map<String, Object> mut = new HashMap<>((Map<String, Object>) m); deleteFromMap(mut, path, idx + 1); map.put(key, mut); }
        else if (obj instanceof List<?> l)  { List<Object> mut = new ArrayList<>((List<Object>) l); deleteFromList(mut, path, idx + 1); map.put(key, mut); }
        else throw new IllegalArgumentException("삭제 경로 오류: " + key);
    }

    @SuppressWarnings("unchecked")
    private void deleteFromList(List<Object> list, String[] path, int idx) throws Exception {
        int i; try { i = Integer.parseInt(path[idx]); } catch (NumberFormatException e) { throw new IllegalArgumentException("List 인덱스가 숫자가 아님: " + path[idx]); }
        if (i < 0 || i >= list.size()) throw new IndexOutOfBoundsException("List 인덱스 범위 초과: " + i);
        if (idx == path.length - 1) { list.remove(i); return; }
        Object obj = list.get(i);
        if (obj instanceof A_DataMap adm)  deleteFromDataMap(adm, path, idx + 1);
        else if (obj instanceof Map<?,?> m) { Map<String, Object> mut = new HashMap<>((Map<String, Object>) m); deleteFromMap(mut, path, idx + 1); list.set(i, mut); }
        else if (obj instanceof List<?> l)  { List<Object> mut = new ArrayList<>((List<Object>) l); deleteFromList(mut, path, idx + 1); list.set(i, mut); }
        else throw new IllegalArgumentException("삭제 경로 오류: " + i);
    }

    /** 직렬화된 Map으로부터 ConfigurationSerializable 객체를 복원 */
    @SuppressWarnings({"unchecked","deprecation"})
    private ConfigurationSerializable restoreConfigSerializable(Map<String, Object> serialized, String className) throws Exception {
        Map<String, Object> copy = new HashMap<>(serialized);
        copy.remove("__class__");
        Class<? extends ConfigurationSerializable> clazz;
        try {
            clazz = (Class<? extends ConfigurationSerializable>) Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("클래스를 찾을 수 없음: " + className);
        }
        ConfigurationSerializable result = ConfigurationSerialization.deserializeObject(copy, clazz);
        if (result == null) throw new IllegalStateException("역직렬화 실패: " + className);
        return result;
    }

    // ── /api/storage/save ─────────────────────────────────────────────────
    // POST { storage }

    private void handleStorageSave(HttpExchange exchange) throws IOException {
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            sendJson(exchange, 405, "{\"error\":\"POST required\"}"); return;
        }
        String body       = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        String storageKey = extractJsonString(body, "storage");

        if (storageKey == null || storageKey.isBlank()) {
            sendJson(exchange, 400, "{\"error\":\"storage 파라미터가 필요합니다\"}"); return;
        }

        String[] parts = storageKey.split(":", 2);
        if (parts.length != 2) {
            sendJson(exchange, 400, "{\"error\":\"잘못된 스토리지 키\"}"); return;
        }

        NamespacedKey nsKey;
        try {
            nsKey = new NamespacedKey(parts[0].toLowerCase(), parts[1].toLowerCase());
        } catch (IllegalArgumentException e) {
            sendJson(exchange, 400, "{\"error\":\"잘못된 키 형식\"}"); return;
        }

        if (!CommediaDellarte.containStorage(nsKey)) {
            sendJson(exchange, 404, "{\"error\":\"스토리지가 존재하지 않습니다\"}"); return;
        }
        IDataStorage storage = CommediaDellarte.getStorage(nsKey);
        if (storage == null) {
            sendJson(exchange, 404, "{\"error\":\"스토리지를 가져올 수 없습니다\"}"); return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(plugin, storage::saveAll);
        sendJson(exchange, 200, "{\"ok\":true}");
    }

    // ── 스토리지 헬퍼 ─────────────────────────────────────────────────────

    /** 리플렉션으로 DataStorage.dataMap(private) 접근 */
    @SuppressWarnings("unchecked")
    private Map<String, A_DataMap> getDataMapCache(IDataStorage storage) {
        try {
            Field f = storage.getClass().getDeclaredField("dataMap");
            f.setAccessible(true);
            return (Map<String, A_DataMap>) f.get(storage);
        } catch (Exception e) {
            plugin.getLogger().warning("dataMap 접근 실패: " + e.getMessage());
            return new HashMap<>();
        }
    }

    private String dataMapToJson(A_DataMap dataMap) {
        if (dataMap == null) return "{}";
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<String, Object> entry : dataMap.getMap().entrySet()) {
            if (!first) sb.append(",");
            sb.append("\"").append(esc(entry.getKey())).append("\":");
            sb.append(valueToJson(entry.getValue()));
            first = false;
        }
        return sb.append("}").toString();
    }

    private String valueToJson(Object value) {
        if (value == null) {
            return "{\"type\":\"null\",\"value\":null}";
        } else if (value instanceof Integer i) {
            return "{\"type\":\"int\",\"value\":" + i + "}";
        } else if (value instanceof Long l) {
            return "{\"type\":\"long\",\"value\":" + l + "}";
        } else if (value instanceof Float f) {
            if (f.isNaN() || f.isInfinite())
                return "{\"type\":\"float\",\"value\":\"" + f + "\",\"readonly\":true}";
            return "{\"type\":\"float\",\"value\":" + f + "}";
        } else if (value instanceof Double d) {
            if (d.isNaN() || d.isInfinite())
                return "{\"type\":\"double\",\"value\":\"" + d + "\",\"readonly\":true}";
            return "{\"type\":\"double\",\"value\":" + d + "}";
        } else if (value instanceof Boolean b) {
            return "{\"type\":\"boolean\",\"value\":" + b + "}";
        } else if (value instanceof String s) {
            return "{\"type\":\"String\",\"value\":\"" + esc(s) + "\"}";
        } else if (value instanceof UUID uuid) {
            return "{\"type\":\"UUID\",\"value\":\"" + uuid + "\"}";
        } else if (value instanceof A_DataMap adm) {
            return "{\"type\":\"A_DataMap\",\"value\":" + dataMapToJson(adm) + "}";
        } else if (value instanceof List<?> list) {
            StringBuilder sb = new StringBuilder("{\"type\":\"List\",\"value\":[");
            boolean first = true;
            for (Object item : list) {
                if (!first) sb.append(",");
                sb.append(valueToJson(item));
                first = false;
            }
            return sb.append("]}").toString();
        } else if (value instanceof ConfigurationSerializable cs) {
            return csToJson(cs);
        } else if (value instanceof Map<?,?> map) {
            return plainMapToJson(map);
        } else {
            String cn = value.getClass().getSimpleName();
            String sv;
            try { sv = esc(value.toString()); } catch (Exception ex) { sv = "[error]"; }
            return "{\"type\":\"" + esc(cn) + "\",\"value\":\"" + sv + "\",\"readonly\":true}";
        }
    }

    /** ConfigurationSerializable → JSON 객체 (serialize() 결과 + __class__ 필드) */
    private String csToJson(ConfigurationSerializable cs) {
        Map<String, Object> serialized;
        try { serialized = cs.serialize(); } catch (Exception e) {
            return "{\"type\":\"" + esc(cs.getClass().getSimpleName()) + "\",\"value\":\"[serialize error]\",\"readonly\":true}";
        }
        String className = cs.getClass().getCanonicalName();
        if (className == null) className = cs.getClass().getName();

        StringBuilder sb = new StringBuilder("{\"type\":\"CS\",\"__class__\":\"")
                .append(esc(className)).append("\",\"value\":{");
        // __class__ 필드 (readonly)
        sb.append("\"__class__\":{\"type\":\"String\",\"value\":\"").append(esc(className)).append("\",\"readonly\":true}");
        for (Map.Entry<String, Object> e : serialized.entrySet()) {
            sb.append(",\"").append(esc(e.getKey())).append("\":").append(valueToJson(e.getValue()));
        }
        return sb.append("}}").toString();
    }

    /** 일반 Map<?,?> → JSON 객체 */
    private String plainMapToJson(Map<?,?> map) {
        StringBuilder sb = new StringBuilder("{\"type\":\"Map\",\"value\":{");
        boolean first = true;
        for (Map.Entry<?,?> e : map.entrySet()) {
            if (!first) sb.append(",");
            sb.append("\"").append(esc(String.valueOf(e.getKey()))).append("\":").append(valueToJson(e.getValue()));
            first = false;
        }
        return sb.append("}}").toString();
    }

    private Object parseStorageValue(String type, String value) {
        return switch (type) {
            case "int"      -> Integer.parseInt(value);
            case "long"     -> Long.parseLong(value);
            case "float"    -> Float.parseFloat(value);
            case "double"   -> Double.parseDouble(value);
            case "boolean"  -> Boolean.parseBoolean(value);
            case "String"   -> value;
            case "UUID"     -> UUID.fromString(value);
            case "A_DataMap"-> new A_DataMap();
            case "List"     -> new ArrayList<>();
            default -> throw new IllegalArgumentException("편집 불가 타입: " + type);
        };
    }

    // ── 인증 유틸 ─────────────────────────────────────────────────────────

    private boolean isAuthenticated(HttpExchange exchange) {
        String header = exchange.getRequestHeaders().getFirst("Authorization");
        if (header == null || !header.startsWith("Bearer ")) return false;
        String token = header.substring(7);
        Long expiry = sessions.get(token);
        if (expiry == null) return false;
        if (System.currentTimeMillis() > expiry) { sessions.remove(token); return false; }
        return true;
    }

    // ── 공통 유틸 ──────────────────────────────────────────────────────────

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
        ex.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type, Authorization");
    }

    private void sendJson(HttpExchange ex, int code, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        ex.sendResponseHeaders(code, bytes.length);
        try (OutputStream os = ex.getResponseBody()) { os.write(bytes); }
    }
}