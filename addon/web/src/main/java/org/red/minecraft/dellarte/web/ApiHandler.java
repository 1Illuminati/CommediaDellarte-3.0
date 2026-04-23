package org.red.minecraft.dellarte.web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ApiHandler implements HttpHandler {

    private final CommediaDellarteWebPlugin plugin;
    private final String endpoint;

    public ApiHandler(CommediaDellarteWebPlugin plugin, String endpoint) {
        this.plugin = plugin;
        this.endpoint = endpoint;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String body;
        switch (endpoint) {
            case "status":
                body = String.format(
                        "{\"version\":\"%s\",\"online\":%d,\"max\":%d,\"motd\":\"%s\"}",
                        Bukkit.getVersion(),
                        Bukkit.getOnlinePlayers().size(),
                        Bukkit.getMaxPlayers(),
                        Bukkit.getMotd().replace("\"", "\\\"")
                );
                break;

            case "players":
                StringBuilder sb = new StringBuilder("[");
                boolean first = true;
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (!first) sb.append(",");
                    sb.append(String.format(
                            "{\"name\":\"%s\",\"uuid\":\"%s\",\"world\":\"%s\",\"health\":%.1f}",
                            p.getName(), p.getUniqueId(), p.getWorld().getName(), p.getHealth()
                    ));
                    first = false;
                }
                sb.append("]");
                body = sb.toString();
                break;

            default:
                body = "{\"error\":\"unknown endpoint\"}";
        }

        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}
