package tests;

import com.codeborne.selenide.Configuration;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Configuration.baseUrl;
import static com.codeborne.selenide.Selenide.open;

@ExtendWith(ReportPortalExtension.class)
public class BaseTest {

    private static final Logger LOGGER = LogManager.getLogger(BaseTest.class);

    @BeforeEach
    public void initDriverLocal() {
        Configuration.startMaximized = true;
        Configuration.baseUrl = "https://yam.telekom.de/startpage";
        Configuration.headless = true;
        open(baseUrl);
        LOGGER.info("Opened YAM");
    }

}