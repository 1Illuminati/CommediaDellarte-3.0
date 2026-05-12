package org.red.minecraft.dellarte.web;

import com.sun.net.httpserver.HttpServer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.red.minecraft.dellarte.library.event.FirstLoadEvent;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;

public class CommediaDellarteWebPlugin extends JavaPlugin implements Listener {
    private HttpServer httpServer;
    private ConsoleCapture consoleCapture;

    /** 플레이어별 현재 세션 접속 시각 (ms) */
    private final Map<UUID, Long> joinTimes = new HashMap<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(this, this);

        consoleCapture = new ConsoleCapture();
        consoleCapture.install();

        saveDefaultSiteResource("site/index.html");
        saveDefaultSiteResource("site/css/style.css");
        saveDefaultSiteResource("site/js/main.js");

        File siteDir = new File(getDataFolder(), "site");
        int port = getConfig().getInt("web.port", 8080);

        try {
            httpServer = HttpServer.create(new InetSocketAddress(port), 0);

            ApiHandler api = new ApiHandler(this, consoleCapture);
            httpServer.createContext("/api/stats",       api);
            httpServer.createContext("/api/console",     api);
            httpServer.createContext("/api/command",     api);
            httpServer.createContext("/api/files",       api);
            httpServer.createContext("/api/shutdown",    api);
            httpServer.createContext("/api/restart",     api);
            httpServer.createContext("/api/player/info", api);
            httpServer.createContext("/api/player/kick", api);
            httpServer.createContext("/api/player/ban",  api);
            httpServer.createContext("/api/plugins",      api);
            httpServer.createContext("/api/auth/login",   api);
            httpServer.createContext("/api/auth/status",  api);
            httpServer.createContext("/api/auth/logout",  api);
            httpServer.createContext("/api/storage/load",   api);
            httpServer.createContext("/api/storage/set",    api);
            httpServer.createContext("/api/storage/save",   api);
            httpServer.createContext("/api/storage/add",    api);
            httpServer.createContext("/api/storage/delete", api);

            httpServer.createContext("/", new StaticFileHandler(siteDir, getLogger()));
            httpServer.setExecutor(Executors.newFixedThreadPool(4));
            httpServer.start();

            getLogger().info("웹서버 시작 → http://localhost:" + port);

        } catch (IOException e) {
            getLogger().severe("웹서버 시작 실패: " + e.getMessage());
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        if (httpServer != null) httpServer.stop(1);
        if (consoleCapture != null) consoleCapture.uninstall();
        getLogger().info("웹서버 종료됨");
    }

    // ── 이벤트 ──────────────────────────────────────────────────────────

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        joinTimes.put(event.getPlayer().getUniqueId(), System.currentTimeMillis());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        joinTimes.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void firstLoadEventListen(FirstLoadEvent event) {
    }

    // ── Getter ──────────────────────────────────────────────────────────

    public Map<UUID, Long> getJoinTimes() {
        return joinTimes;
    }

    // ── 유틸 ────────────────────────────────────────────────────────────

    /** 항상 최신 리소스를 덮어씁니다 (플러그인 업데이트 시 정적 파일도 갱신) */
    private void saveDefaultSiteResource(String resourcePath) {
        File outFile = new File(getDataFolder(), resourcePath);
        outFile.getParentFile().mkdirs();
        saveResource(resourcePath, true);
    }
}