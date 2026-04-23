package org.red.minecraft.dellarte.web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.logging.Logger;

public class StaticFileHandler implements HttpHandler {

    private final File siteRoot;
    private final Logger logger;

    public StaticFileHandler(File siteRoot, Logger logger) {
        this.siteRoot = siteRoot;
        this.logger = logger;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String uriPath = exchange.getRequestURI().getPath();

        // "/" 요청은 index.html로
        if (uriPath.equals("/")) uriPath = "/index.html";

        // .site 폴더 기준으로 파일 경로 결정
        File target = new File(siteRoot, uriPath).getCanonicalFile();

        // 경로 탈출 방지 (directory traversal)
        if (!target.getPath().startsWith(siteRoot.getCanonicalPath())) {
            sendError(exchange, 403, "403 Forbidden");
            return;
        }

        if (!target.exists() || target.isDirectory()) {
            sendError(exchange, 404, "404 Not Found: " + uriPath);
            return;
        }

        // MIME 타입 결정
        String mime = resolveMime(target.getName());
        byte[] bytes = Files.readAllBytes(target.toPath());

        exchange.getResponseHeaders().set("Content-Type", mime);
        exchange.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private String resolveMime(String filename) {
        if (filename.endsWith(".html")) return "text/html; charset=UTF-8";
        if (filename.endsWith(".css"))  return "text/css; charset=UTF-8";
        if (filename.endsWith(".js"))   return "application/javascript; charset=UTF-8";
        if (filename.endsWith(".json")) return "application/json; charset=UTF-8";
        if (filename.endsWith(".png"))  return "image/png";
        if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) return "image/jpeg";
        if (filename.endsWith(".svg"))  return "image/svg+xml";
        if (filename.endsWith(".ico"))  return "image/x-icon";
        return "application/octet-stream";
    }

    private void sendError(HttpExchange exchange, int code, String message) throws IOException {
        byte[] bytes = message.getBytes();
        exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=UTF-8");
        exchange.sendResponseHeaders(code, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}

