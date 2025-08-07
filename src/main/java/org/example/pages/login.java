package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class login {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By phoneField      = By.name("phoneNumber");
    private final By passwordField   = By.id("password");
    private final By continueButton  = By.xpath("//button[normalize-space()='Continue']");
    private final By dashboardLogo   = By.cssSelector("img[alt='Logo']");

    public login(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void enterPhoneNumber(String number) {
        driver.findElement(phoneField).sendKeys(number);
    }

    public void enterPassword(String password) {
        driver.findElement(passwordField).sendKeys(password);
    }

    public void clickContinue() {
        driver.findElement(continueButton).click();
    }

    /**
     * Waits for the dashboard logo to be visible and returns its display status.
     */
    public boolean isDashboardLogoVisible() {
        WebElement logo = wait.until(
                ExpectedConditions.visibilityOfElementLocated(dashboardLogo)
        );
        return logo.isDisplayed();
    }
}