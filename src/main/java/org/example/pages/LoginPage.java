// src/main/java/org/example/pages/LoginPage.java
package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class LoginPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By phoneField     = By.name("phoneNumber");
    private final By passwordField  = By.id("password");
    private final By continueButton = By.xpath("//button[normalize-space()='Continue']");
    private final By dashboardLogo  = By.cssSelector("img[alt='Logo']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /** Type phone + password + click Continue in one call. */
    public void login(String phone, String pass) {
        driver.findElement(phoneField).sendKeys(phone);
        driver.findElement(passwordField).sendKeys(pass);
        driver.findElement(continueButton).click();
    }

    /** Returns true once the dashboard logo is visible (or times out). */
    public boolean isDashboardLogoVisible() {
        return wait
                .until(ExpectedConditions
                        .visibilityOfElementLocated(dashboardLogo))
                .isDisplayed();
    }
}
