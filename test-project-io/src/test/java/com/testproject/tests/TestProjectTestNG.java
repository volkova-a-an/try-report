package com.testproject.tests;

import io.testproject.sdk.DriverBuilder;
import io.testproject.sdk.drivers.ReportType;
import io.testproject.sdk.drivers.web.ChromeDriver;
import io.testproject.sdk.internal.exceptions.AgentConnectException;
import io.testproject.sdk.internal.exceptions.InvalidTokenException;
import io.testproject.sdk.internal.exceptions.ObsoleteVersionException;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

public class TestProjectTestNG {
    private static ChromeDriver driver;


    public TestProjectTestNG() throws MalformedURLException {
    }

    @BeforeMethod
    public void initDriverLocal() throws InvalidTokenException, ObsoleteVersionException, AgentConnectException, IOException {
         driver = new DriverBuilder<ChromeDriver>(new ChromeOptions())
                .withToken("xMkApZ0SAehw8BYvleJKqHpnxtWpSn7gmokjxflNw1o1")
                .withProjectName("HelloFromIntellij")
                .withJobName("TestProjectTestJob")
                .build(ChromeDriver.class);

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.report().disableTestAutoReports(true);
        driver.report().test("firstTest").submit();
    }

    @Test
    public void firstTest() {
        driver.get("https://www.google.com/");
        driver.report().step("Google homepage opened");
    }


    @AfterSuite
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }

    }

}