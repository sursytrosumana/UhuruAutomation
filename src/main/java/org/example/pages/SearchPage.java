// src/main/java/org/example/pages/SearchPage.java
package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class SearchPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locator for the search input
    private final By searchField = By.id("search");
    // Locator for the results grid container
    private final By resultsContainer = By.cssSelector(
            "div.grid.grid-cols-1.md\\:grid-cols-2.lg\\:grid-cols-3.gap-x-10.gap-y-10"
    );
    // Locator for each result item link
    private final By resultItems = By.cssSelector(
            "div.grid.grid-cols-1.md\\:grid-cols-2.lg\\:grid-cols-3.gap-x-10.gap-y-10 > a"
    );
    // Locator for the product title within each result card
    private final By productTitle = By.cssSelector("div.my-3 span.font-medium");

    public SearchPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /**
     * Types the query into the search field (does not trigger the search yet).
     */
    public void searchItem(String query) {
        WebElement field = wait.until(
                ExpectedConditions.elementToBeClickable(searchField)
        );
        field.clear();
        field.sendKeys(query);
    }

    /**
     * Sends ENTER on the search field and waits for the results grid to appear.
     */
    public void pressEnter() throws InterruptedException {
        WebElement field = wait.until(
                ExpectedConditions.elementToBeClickable(searchField)
        );
        Thread.sleep(3000);
        field.sendKeys(Keys.ENTER);

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(resultsContainer)
        );
    }

    /**
     * Returns true if any result card’s title exactly matches the given text.
     */
    /**
     * Returns true if a product link whose href ends with the slug of the given text is visible.
     */
    public boolean isSearchResultVisible(String text) {
        // Convert "Funky Denim Jeans" → "funky-denim-jeans"
        String slug = text.toLowerCase().replaceAll("\\s+", "-");
        // Look for an <a> whose href ends with "/funky-denim-jeans"
        By linkLocator = By.cssSelector("a[href$='/" + slug + "']");
        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(linkLocator)
        ).isDisplayed();
    }

}
