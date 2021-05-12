package com.extentreports.tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.ExtentSparkReporterConfig;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import org.testng.ITestResult;
import org.testng.annotations.*;

import static com.codeborne.selenide.Configuration.baseUrl;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

// https://www.extentreports.com/docs/versions/5/java/spark-reporter.html

public class ExtentReport {
    ExtentReports extent = new ExtentReports();

    @BeforeTest
    public void setUp() {
        // ExtentReports extent = new ExtentReports();
        ExtentSparkReporter spark = new ExtentSparkReporter("build/spark/spark.html"); // a place to put the generated html file
        extent.attachReporter(spark);

        spark.config(
                ExtentSparkReporterConfig.builder()
                        .theme(Theme.DARK)
                        .documentTitle("ReportTitle")
                        .build()
        );
    }

    @BeforeMethod
    public void initDriverLocal() {
        Configuration.startMaximized = true;
        Configuration.baseUrl = "https://yam.telekom.de/startpage";
        // Configuration.headless = true;
        open(baseUrl);
    }

    @Test
    public void extentTest() {

        try {
            ExtentTest firstTest = extent.createTest("Open Feedback box")
                    .assignAuthor("Nastja")
                    .assignCategory("Smoke")
                    .assignDevice("Chrome90"); // create a test node in the report

            firstTest.info("Opened " + WebDriverRunner.url());
            $("span.open").click();
            firstTest.info("Clicked Open Feedback Dialogue");
            $("div.feedback-box").shouldHave(Condition.text("Feedback appreciated"));


            ExtentTest secondTest = extent.createTest("Open Feedback box Regression")
                    .assignAuthor("NotNastja")
                    .assignCategory("Regression")
                    .assignDevice("Chrome90"); // create a test node in the report
            secondTest.info("Opened " + WebDriverRunner.url());
            $("span.open").click();
            secondTest.info("Clicked Open Feedback Dialogue");
            $("div.feedback-box").shouldHave(Condition.text("Feedback appreciated"));

            ExtentTest doomedToFail = extent.createTest("Doomed to fail");
            doomedToFail.info("Opened " + WebDriverRunner.url());
            $("span.open").click();
            doomedToFail.info("Clicked Open Feedback Dialogue");
            $("div.feedback-box").shouldHave(Condition.text("Geben Sie uns Feedback!"));
        }
        catch (Exception e){

        }

    }

    @AfterMethod
    public synchronized void afterMethod(ITestResult result, ExtentTest test) {
        if (result.getStatus() == ITestResult.FAILURE)
            test.fail(result.getThrowable());
        else if (result.getStatus() == ITestResult.SKIP)
            test.skip(result.getThrowable());
        else
            test.pass("Test passed");

      //  extent.flush();
    }

    @AfterSuite
    public void tearDown() {
        extent.flush(); // important
    }
}
