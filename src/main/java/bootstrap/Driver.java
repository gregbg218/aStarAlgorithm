package bootstrap;

import domain.PathFinder;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.EnhancedPatternLayout;
import org.apache.log4j.Level;
import org.apache.log4j.Priority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class Driver {
    public static Properties projectProperties = new Properties();
    public static Logger logger = LoggerFactory.getLogger(Driver.class);

    /**
     --log.file.path
     executionLogs
     --log.level
     info
     --source
     3
     --destination
     60
     --rows
     10
     --columns
     10
     --random.walls
     false
     --no.of.walls
     12
     --wall.ids
     4,15,14,27,31,46,50,51,76,91,96,98
     */

    public static void main(String[] args) {
        configureProperties(args);
        configureLogging(projectProperties.getProperty("log.file.path"),
                projectProperties.getProperty("log.level"));
        logger.info("Starting A Star");
        logger.debug("Starting A Star");
        PathFinder finder = new PathFinder(projectProperties);
        finder.selectWallGenerationMethod();
        finder.findPath();
        finder.printPath();
    }

    public static String configureLogging(String logFile, String logLevel) {
        DailyRollingFileAppender dailyRollingFileAppender = new DailyRollingFileAppender();

        String logFilename = logFile + "/Astar.log";
        switch (logLevel) {
            case "DEBUG": {
                dailyRollingFileAppender.setThreshold(Level.toLevel(Priority.DEBUG_INT));
            }
            case "WARN": {
                dailyRollingFileAppender.setThreshold(Level.toLevel(Priority.WARN_INT));
            }
            case "ERROR": {
                dailyRollingFileAppender.setThreshold(Level.toLevel(Priority.ERROR_INT));
            }
            default: {
                dailyRollingFileAppender.setThreshold(Level.toLevel(Priority.INFO_INT));
            }
            break;
        }

        System.out.println("Log files written out at " + logFilename);
        dailyRollingFileAppender.setFile(logFilename);
        dailyRollingFileAppender.setLayout(new EnhancedPatternLayout("%d [%t] %-5p %c - %m%n"));

        dailyRollingFileAppender.activateOptions();
        org.apache.log4j.Logger.getRootLogger().addAppender(dailyRollingFileAppender);
        return dailyRollingFileAppender.getFile();
    }

    public static void configureProperties(String[] args) {
        projectProperties = new Properties();
        for (int i = 0; i < args.length; i++) {
            if (args[i].contains("--")) {
                String key = args[i].replaceAll("--", "");
                projectProperties.put(key, args[i + 1]);
            }
        }
    }

}
