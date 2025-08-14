// src/main/java/org/example/pages/SearchPage.java
package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SearchPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locator for the search input
    private final By searchField = By.id("search");
    // Locator for the results grid container
    private final By resultsContainer = By.cssSelector(
            "div.grid.grid-cols-1.md:grid-cols-2.lg:grid-cols-3.gap-x-10.gap-y-10"
    );

    //Locator for add to cart button
    By addToCart = By.xpath("//div[contains(@class,'flex') and contains(@class,'justify-center')]//button[normalize-space()='Add to Cart']");

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

    public void clickAddToCart(){
        WebElement addToCartElement = wait.until(ExpectedConditions.elementToBeClickable(addToCart));
       addToCartElement.click();
    }



}
