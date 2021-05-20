package com.extentreports.tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.service.ExtentTestManager;
import com.aventstack.extentreports.testng.listener.ExtentITestListenerClassAdapter;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;

import static com.codeborne.selenide.Configuration.baseUrl;
import static com.codeborne.selenide.Configuration.reportsFolder;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

/**
 * Used docs
 * https://www.extentreports.com/docs/versions/5/java/spark-reporter.html
 * https://www.extentreports.com/docs/versions/4/java/testng.html
 * https://www.extentreports.com/docs/versions/4/java/extentservice.html
 */

@Listeners(ExtentITestListenerClassAdapter.class)
public class ExtentReportTestNG {
    ExtentReports extent = new ExtentReports();

    @BeforeMethod
    public void initDriverLocal() {
        Configuration.startMaximized = true;
        Configuration.baseUrl = "https://yam.telekom.de/startpage";
        Configuration.headless = true;
        Configuration.reportsFolder = "build/test-results/selenide-screenshot";
        Configuration.savePageSource = false;
        open(baseUrl);
    }

    @Test
    public void firstTest() {

        // create first test
        ExtentTest firstTest = extent.createTest("Open Feedback box")

        // assign labels
                .assignAuthor("Nastja")
                .assignCategory("Smoke")
                .assignDevice("Chrome90"); // create a test node in the report
        // logger
        firstTest.info("Opened " + WebDriverRunner.url());

        $("span.open").click();
        firstTest.info("Clicked Open Feedback Dialogue");
        $("div.feedback-box").shouldHave(Condition.text("Feedback appreciated"));

    }

    /**
     * tagName: will be assigned as a category (assignCategory())
     * t:another-tagName, tag:another-tagName: will be assigned as a category
     * a:authorName, author:authorName: will be assigned as an author
     * d:deviceName, device:deviceName: will be assigned as a device
     *
     * @Test(groups = { "tagName", "tag:another-tagName", "author:authorName", "device:deviceName" })
     */

    @Test(groups = {"Regression", "t:YamReg", "a:NotNastja", "d:Chrome90"})
    public void secondTest() {
        // create second test
        ExtentTest secondTest = extent.createTest("Open Feedback box Regression");
        secondTest.info("Opened " + WebDriverRunner.url());
        $("span.open").click();
        secondTest.info("Clicked Open Feedback Dialogue");
        $("div.feedback-box").shouldHave(Condition.text("Feedback appreciated"));
    }

    @Test(groups = {"DOOM", "t:Eternal", "a:DOOM", "d:DOOM"})
    public void doomedToFail() {
        ExtentTest doomedToFail = extent.createTest("Doomed to fail");
        doomedToFail.info("Opened " + WebDriverRunner.url());
        $("span.open").click();
        doomedToFail.info("Clicked Open Feedback Dialogue");
        $("div.feedback-box").shouldHave(Condition.text("Geben Sie uns Feedback!"));
    }

    @AfterMethod
    public void afterMethod(ITestResult result) throws IOException {
        switch (result.getStatus()) {
            case ITestResult.FAILURE:

                File inputFile = findTheMostRecentScreenshot(reportsFolder);
                String encodedString = encodeFileToBase64(inputFile);

                ExtentTestManager.getTest(result).fail("Test failed, event afterMethod. Here is a screenshot: ",
                        MediaEntityBuilder.createScreenCaptureFromBase64String(encodedString).build());
                break;
            case ITestResult.SKIP:
                ExtentTestManager.getTest(result).skip("ITestResult.SKIP, event afterMethod");
                break;
            default:
                break;
        }
    }


    @AfterSuite
    public void tearDown() {
        extent.flush();
    }


    public static File findTheMostRecentScreenshot(String sdir) {
        File dir = new File(sdir);
        if (dir.isDirectory()) {
            File[] dirFiles = dir.listFiles((FileFilter) FileFilterUtils.fileFileFilter());
            if (dirFiles != null && dirFiles.length > 0) {
                Arrays.sort(dirFiles, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
                return dirFiles[0];
            }
        }

        return null;
    }

    public String encodeFileToBase64(File inputFile) throws IOException {
        byte[] fileContent = FileUtils.readFileToByteArray(inputFile);
        return Base64
                .getEncoder()
                .encodeToString(fileContent);
    }
}