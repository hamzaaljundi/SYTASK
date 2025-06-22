package utils;

import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.xml.XmlSuite;

import java.util.Properties;

public class TestNGConfigListener implements ISuiteListener {

    @Override
    public void onStart(ISuite suite) {
        setParallelMode(suite);
        setExtentReportProperties();
    }

    private void setParallelMode(ISuite suite) {
        String parallelMode = ConfigManager.getConfig("testng.parallel");
        String threadCountStr = ConfigManager.getConfig("testng.thread.count");

        if (parallelMode != null && !parallelMode.trim().isEmpty() && threadCountStr != null && !threadCountStr.trim().isEmpty()) {
            int threadCount = Integer.parseInt(threadCountStr);
            XmlSuite.ParallelMode mode = XmlSuite.ParallelMode.valueOf(parallelMode.toUpperCase());
            
            suite.getXmlSuite().setParallel(mode);

            suite.getXmlSuite().setDataProviderThreadCount(threadCount);
            
            System.out.println(">> TestNGConfigListener: Set parallel mode to '" + mode + "' with thread count of " + threadCount + ". <<");
        }
    }

    private void setExtentReportProperties() {
        Properties properties = ConfigManager.getProperties();
        
        properties.forEach((key, value) -> {
            String keyStr = (String) key;
            if (keyStr.startsWith("extent.")) {
                System.setProperty(keyStr, (String) value);
            }
        });

        System.out.println(">> TestNGConfigListener: Extent Report properties have been set. <<");
    }

    @Override
    public void onFinish(ISuite suite) {
        System.out.println(">> TestNGConfigListener: Test suite completed. <<");
    }
} 