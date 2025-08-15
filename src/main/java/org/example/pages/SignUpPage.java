package org.example.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;


public class SignUpPage {

    WebDriver driver;
    WebDriverWait wait;

    String PAGE_URL = "https://staging.uhuru.market/en/signup?tab=phone";


    By phoneTab = By.cssSelector("button[type='button'].rounded-r-xl");


    By firstName = By.id("fName");
    By lastName = By.id("lName");


    By genderControl = By.xpath("//label[contains(normalize-space(),'Gender')]/following::*[contains(@class,'control')][1]");


    By phoneInput = By.xpath("//input[@name='phoneNumber']");


    By password = By.id("password");
    By confirmPassword = By.id("confirmPassword");


    By termsRoleCheckbox = By.cssSelector("button[role='checkbox']");
    By agreeBtn = By.xpath("//button[normalize-space()='Agree & Continue']");

    By SubmitButton = By.xpath("//button[normalize-space()='Agree & Continue']");


    public SignUpPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // ---------- Navigation ----------
    public SignUpPage open() {
        driver.get(PAGE_URL);
        wait.until(ExpectedConditions.presenceOfElementLocated(phoneTab));
        return this;
    }


    public SignUpPage switchToPhoneTab() {
        click(phoneTab);
        return this;
    }


    public SignUpPage setFirstName(String value) {
        clearAndType(firstName, value);
        return this;
    }

    public SignUpPage setLastName(String value) {
        clearAndType(lastName, value);
        return this;
    }

    public SignUpPage selectGender(String genderText) {
        WebElement control = wait.until(ExpectedConditions.elementToBeClickable(genderControl));
        control.click();
        WebElement focused = driver.switchTo().activeElement();
        focused.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        focused.sendKeys(genderText);
        focused.sendKeys(Keys.ENTER);
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                    By.cssSelector("div[id^='react-select-'][id$='-listbox']")));
        } catch (TimeoutException ignored) {
        }
        return this;
    }

    public SignUpPage setPhone(String number) {
        clearAndType(phoneInput, number);
        return this;
    }

    public SignUpPage setPassword(String pwd) {
        clearAndType(password, pwd);
        return this;
    }

    public SignUpPage setConfirmPassword(String pwd) {
        clearAndType(confirmPassword, pwd);
        return this;
    }

    public SignUpPage acceptTerms() {
        WebElement cb = wait.until(ExpectedConditions.elementToBeClickable(termsRoleCheckbox));
        if (!"true".equalsIgnoreCase(cb.getAttribute("aria-checked"))) {
            try {
                cb.click();
            } catch (ElementClickInterceptedException e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", cb);
            }
            wait.until(d -> "true".equalsIgnoreCase(cb.getAttribute("aria-checked")));
        }
        return this;
    }

    public void submit() {
        WebElement btn = wait.until(ExpectedConditions.visibilityOfElementLocated(SubmitButton));
        wait.until(d -> btn.getAttribute("disabled") == null);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(btn)).click();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        }
    }

    private void click(By locator) {
        WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator));
        try {
            el.click();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
        }
    }

    private void clearAndType(By locator, String text) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        try {
            el.click();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
        }
        el.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        el.sendKeys(Keys.DELETE);
        el.sendKeys(text);
    }
}
