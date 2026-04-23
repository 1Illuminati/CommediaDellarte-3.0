package org.red.minecraft.dellarte.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class ConsoleCapture extends AbstractAppender {

    private static final int MAX_LINES = 300;
    private final Deque<String> buffer = new ArrayDeque<>();

    public ConsoleCapture() {
        super("WebPluginConsoleCapture", null,
                PatternLayout.newBuilder()
                        .withPattern("[%d{HH:mm:ss}] [%level] %msg%n")
                        .build(),
                true, null);
    }

    public void install() {
        start();
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        Configuration config = ctx.getConfiguration();
        config.getRootLogger().addAppender(this, null, null);
        ctx.updateLoggers();
    }

    public void uninstall() {
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        Configuration config = ctx.getConfiguration();
        config.getRootLogger().removeAppender(getName());
        ctx.updateLoggers();
        stop();
    }

    @Override
    public void append(LogEvent event) {
        String line = new String(getLayout().toByteArray(event)).stripTrailing();
        synchronized (buffer) {
            buffer.addLast(line);
            if (buffer.size() > MAX_LINES) buffer.pollFirst();
        }
    }

    public List<String> getLines() {
        synchronized (buffer) {
            return new ArrayList<>(buffer);
        }
    }
}
