// src/test/java/org/example/test/SearchTest.java
package org.example.test;

import org.example.pages.Setup;
import org.example.pages.LoginPage;
import org.example.pages.SearchPage;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class SearchTest {
    private WebDriver driver;
    private SearchPage searchPage;

    @BeforeEach
    void setUp() {
        driver = Setup.initialize();

        // perform login once
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("68 012 3793", "Nepal@123455");
        assertTrue(
                loginPage.isDashboardLogoVisible(),
                "Precondition failed: login must succeed"
        );

        // now on dashboard, prepare search
        searchPage = new SearchPage(driver);
    }

    @AfterEach
    void tearDown() {
        Setup.quitDriver();
    }

    @Test
    @DisplayName("Search for an item and verify the first result exactly matches")
    void searchResultVerify() throws InterruptedException {
        String item = "Funky Denim Jeans";

        // Type the query
        searchPage.searchItem(item);

        // Press Enter to trigger the search
        searchPage.pressEnter();

        // ─── Replace everything from here ───

        // wait for the <a> whose href ends with "/funky-denim-jeans"
        String slug = item.toLowerCase().replaceAll("\\s+", "-");
        By productLink = By.cssSelector("a[href$='/" + slug + "']");
        WebElement firstResult = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(productLink));

        // grab its full text ("Funky Denim Jeans\n🏷️ $43.79")
        String fullText = firstResult.getText().trim();

        // split on newline and take the first line as the title
        String actualTitle = fullText.split("\\R")[0];

        // final assertion
        assertEquals(
                item,
                actualTitle,
                "Expected first result title to be exactly '" + item + "', but was '" + actualTitle + "'"
        );
        // ─── to here ───
    }


}
