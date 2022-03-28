package domain;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Properties;

public class PathFinderTest {
    public static Properties projectProperties = new Properties();

    @Test
    public void basicTestCase() {
        String[] args = {"--log.file.path", "executionLogs", "--log.level", "INFO", "--source", "40", "--destination", "60", "--rows", "10", "--columns", "10", "--random.walls", "false", "--no.of.walls", "3", "--wall.ids", "50,51,52"};
        configureProperties(args);
        PathFinder pathFinder = new PathFinder(projectProperties);
        pathFinder.selectWallGenerationMethod();
        pathFinder.calculatePath();
        List<Double> path = pathFinder.getPath();
        Double[] predictedPath = path.toArray(new Double[path.size()]);
        Double[] expectedPath = {40.0, 41.0, 42.0, 53.0, 62.0, 61.0, 60.0};
        Assert.assertArrayEquals(expectedPath, predictedPath);

    }

    @Test
    public void oneOpeningEndOfTheRowTestCase() {
        String[] args = {"--log.file.path", "executionLogs", "--log.level", "INFO", "--source", "40", "--destination", "60", "--rows", "10", "--columns", "10", "--random.walls", "false", "--no.of.walls", "9", "--wall.ids", "50,51,52,53,54,55,56,57,58"};
        configureProperties(args);
        PathFinder pathFinder = new PathFinder(projectProperties);
        pathFinder.selectWallGenerationMethod();
        pathFinder.calculatePath();
        List<Double> path = pathFinder.getPath();
        Double[] predictedPath = path.toArray(new Double[path.size()]);
        Double[] expectedPath = {40.0, 41.0, 42.0, 43.0, 44.0, 45.0, 46.0, 47.0, 48.0, 59.0, 68.0, 67.0, 66.0, 65.0, 64.0, 63.0, 62.0, 61.0, 60.0};
        Assert.assertArrayEquals(expectedPath, predictedPath);

    }

    @Test
    public void oneOpeningMiddleOfTheRowTestCase() {
        String[] args = {"--log.file.path", "executionLogs", "--log.level", "INFO", "--source", "9", "--destination", "99", "--rows", "10", "--columns", "10", "--random.walls", "false", "--no.of.walls", "9", "--wall.ids", "50,51,52,53,55,56,57,58,59"};
        configureProperties(args);
        PathFinder pathFinder = new PathFinder(projectProperties);
        pathFinder.selectWallGenerationMethod();
        pathFinder.calculatePath();
        List<Double> path = pathFinder.getPath();
        Double[] predictedPath = path.toArray(new Double[path.size()]);
        Double[] expectedPath = {9.0, 18.0, 27.0, 36.0, 45.0, 54.0, 65.0, 76.0, 87.0, 98.0, 99.0};
        Assert.assertArrayEquals(expectedPath, predictedPath);

    }

    @Test
    public void InvalidPathTestCase() {
        String[] args = {"--log.file.path", "executionLogs", "--log.level", "INFO", "--source", "54", "--destination", "56", "--rows", "10", "--columns", "10", "--random.walls", "false", "--no.of.walls", "8", "--wall.ids", "43,53,63,44,64,45,55,65"};
        configureProperties(args);
        PathFinder pathFinder = new PathFinder(projectProperties);
        pathFinder.selectWallGenerationMethod();
        pathFinder.calculatePath();
        boolean expectedInvalidFlag = pathFinder.isInvalidFlag();
        boolean predictedInvalidFlag = true;
        Assert.assertEquals(predictedInvalidFlag, expectedInvalidFlag);

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