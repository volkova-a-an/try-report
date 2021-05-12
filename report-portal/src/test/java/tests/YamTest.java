package tests;

import com.codeborne.selenide.Condition;
import com.epam.reportportal.annotations.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.codeborne.selenide.Selenide.$;

public class YamTest extends BaseTest {

    private static final Logger LOGGER = LogManager.getLogger(YamTest.class);

    @Test
    public void openFeedbackBoxTest() throws IOException {
        navigateToMainPage();
        clickFeedbackButton();
        $("div.feedback-box").shouldHave(Condition.text("Feedback appreciated"));
    }

    @Test
    public void doomedToFailTest() {
        navigateToMainPage();
        clickFeedbackButton();
        $("div.feedback-box").shouldHave(Condition.text("Geben Sie uns Feedback!"));

    }

    //add aop-ajc.xml to META-INF directory in resources and a gradle variable to the test task  jvmArgs += "-javaagent:$weaver"

    @Step
    public void navigateToMainPage() {
        LOGGER.info("Main page displayed");
    }

    @Step("Click Feedback button")
    public void clickFeedbackButton() {
        $("span.open").click();
        LOGGER.info("Clicked Open Feedback Dialogue");
    }

}
