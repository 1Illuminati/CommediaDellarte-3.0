package org.red.test;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.appender.FileAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.filter.ThresholdFilter;
import org.apache.logging.log4j.core.layout.PatternLayout;

public class Test {
    private static final Logger logger = createCustomLogger();

    private static Logger createCustomLogger() {
        final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        final Configuration config = ctx.getConfiguration();

        // 패턴 레이아웃 설정
        PatternLayout layout = PatternLayout.newBuilder()
                .withConfiguration(config)
                .withPattern("%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n")
                .build();

        // 파일 어펜더 설정
        Appender fileAppender = FileAppender.newBuilder()
                .withFileName("C:/logs/app.log")
                .withAppend(true)
                .withName("FileLogger")
                .withLayout(layout)
                .withFilter(ThresholdFilter.createFilter(Level.INFO, Filter.Result.ACCEPT, Filter.Result.DENY))
                .setConfiguration(config)
                .build();

        fileAppender.start();

        // 콘솔 어펜더 설정
        Appender consoleAppender = ConsoleAppender.newBuilder()
                .withLayout(layout)
                .setConfiguration(config)
                .withName("ConsoleLogger")
                .build();

        consoleAppender.start();

        // 로거 설정
        config.addAppender(fileAppender);
        config.addAppender(consoleAppender);
        config.getRootLogger().addAppender(config.getAppender("FileLogger"), Level.INFO, null);
        config.getRootLogger().addAppender(config.getAppender("ConsoleLogger"), null, null);
        config.getRootLogger().setLevel(Level.DEBUG);

        ctx.updateLoggers();

        return LogManager.getLogger(Test.class);
    }

    public static void main(String[] args) {
        logger.debug("This is a debug message");
        logger.info("This is an info message");
        logger.error("This is an error message");
    }

}
