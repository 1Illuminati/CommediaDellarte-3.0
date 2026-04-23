package org.red.minecraft.dellarte.web;

import com.sun.net.httpserver.HttpServer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.red.minecraft.dellarte.library.event.FirstLoadEvent;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class CommediaDellarteWebPlugin extends JavaPlugin {
    private HttpServer httpServer;
    private static final int PORT = 8080;

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    /**
     * FirstLoadEvent이후 모든 데이터가 로딩되었을대 웹사이트를 실행
     * @param event 필요없다
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void firstLoadEventListen(FirstLoadEvent event) {
        // .site 폴더 생성 (없으면)
        File siteDir = new File(getDataFolder().getParentFile().getParentFile(), ".site");
        if (!siteDir.exists()) {
            siteDir.mkdirs();
            getLogger().info(".site 폴더 생성됨: " + siteDir.getAbsolutePath());
        }

        try {
            httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);

            // /api/* → API 핸들러
            httpServer.createContext("/api/status",  new ApiHandler(this, "status"));
            httpServer.createContext("/api/players", new ApiHandler(this, "players"));

            // /* → 정적 파일 핸들러 (.site 폴더)
            httpServer.createContext("/", new StaticFileHandler(siteDir, getLogger()));

            httpServer.setExecutor(Executors.newFixedThreadPool(4));
            httpServer.start();

            getLogger().info("웹서버 시작 → http://localhost:" + PORT);
            getLogger().info("사이트 루트: " + siteDir.getAbsolutePath());

        } catch (IOException e) {
            getLogger().severe("웹서버 시작 실패: " + e.getMessage());
            getServer().getPluginManager().disablePlugin(this);
        }
    }
}
