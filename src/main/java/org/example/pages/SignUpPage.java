package org.example.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SignUpPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // --- URL ---
    private static final String PAGE_URL = "https://staging.uhuru.market/en/signup?tab=phone";

    // --- Tabs ---
    private final By emailTab = By.cssSelector("button[type='button'].rounded-l-xl");
    private final By phoneTab = By.cssSelector("button[type='button'].rounded-r-xl");

    // --- Text inputs ---
    private final By firstName = By.id("fName");
    private final By lastName  = By.id("lName");

    // --- Gender (react-select) ---
    private final By genderControl = By.xpath("//label[contains(normalize-space(),'Gender')]/following::*[contains(@class,'control')][1]");

    // --- Date of Birth ---
    private final By dobInput = By.xpath(
            "//input[(@type='text' or @type='date') and " +
                    "(@name='dob' or @placeholder='Date of Birth' or @aria-label='Date of Birth' or contains(@id,'date'))]"
    );

    // --- Phone ---
    private final By countryCodeDropdown = By.xpath(
            "//div[contains(@class,'iti__selected-flag') or contains(@class,'country')] | //select[contains(@name,'country')]"
    );
    private final By phoneInput = By.xpath("//input[@name='phoneNumber']");

    // --- Passwords ---
    private final By password = By.id("password");
    private final By confirmPassword = By.id("confirmPassword");

    // --- Terms + Submit ---
    private final By termsRoleCheckbox = By.cssSelector("button[role='checkbox']");
    private final By agreeBtn = By.xpath("//button[normalize-space()='Agree & Continue']");

    By SubmitButton = By.xpath("//button[normalize-space()='Agree & Continue']");

    // --- Errors ---
    private final By anyError = By.xpath("//*[contains(@class,'error') or contains(@class,'invalid') or @role='alert']");

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

    public SignUpPage clickEmailTab() {
        click(emailTab);
        return this;
    }

    public SignUpPage switchToPhoneTab() {
        click(phoneTab);
        return this;
    }

    // ---------- Form fillers ----------
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
        } catch (TimeoutException ignored) {}
        return this;
    }

    public SignUpPage setDOB(LocalDate date, String displayFormat) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(dobInput));
        String iso = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String shown = date.format(DateTimeFormatter.ofPattern(displayFormat));

        String js = """
            const el = arguments[0], val = arguments[1];
            el.value = val;
            el.dispatchEvent(new Event('input', {bubbles:true}));
            el.dispatchEvent(new Event('change', {bubbles:true}));
        """;
        try {
            ((JavascriptExecutor) driver).executeScript(js, input, iso);
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript(js, input, shown);
        }
        return this;
    }

    public SignUpPage setCountryCode(String codeLikePlus255) {
        WebElement cc = wait.until(ExpectedConditions.elementToBeClickable(countryCodeDropdown));
        cc.click();
        By option = By.xpath("//*[self::li or self::div or self::span]" +
                "[contains(@class,'iti__country') or @role='option' or contains(@class,'country')]" +
                "[contains(normalize-space(),'" + codeLikePlus255 + "')]");
        WebElement opt = wait.until(ExpectedConditions.elementToBeClickable(option));
        opt.click();
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                    By.cssSelector(".iti__country-list, .iti__dropdown-content, [role='listbox']")));
        } catch (TimeoutException ignored) {}
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
        // Wait for the button to be enabled
        wait.until(d -> btn.getAttribute("disabled") == null);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(btn)).click();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        }
    }


    // ---------- Assertions/helpers ----------
    public boolean isAgreeEnabled() {
        WebElement btn = wait.until(ExpectedConditions.visibilityOfElementLocated(agreeBtn));
        String disabled = btn.getAttribute("disabled");
        return disabled == null || disabled.isEmpty();
    }

    public boolean hasInlineErrors() {
        return !driver.findElements(anyError).isEmpty();
    }

    // ---------- private utils ----------
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
