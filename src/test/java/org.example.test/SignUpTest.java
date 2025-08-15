package org.example.test;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.example.pages.SignUpPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Random;

public class SignUpTest {

    private WebDriver driver;
    private SignUpPage signUp;

    @BeforeClass
    public void setup() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions opts = new ChromeOptions();
        opts.addArguments("--disable-gpu", "--no-sandbox", "--window-size=1440,900");

        driver = new ChromeDriver(opts);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0)); // we use explicit waits in the page
        driver.manage().window().maximize();

        signUp = new SignUpPage(driver);
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (driver != null) driver.quit();
    }

    private String randomLocalPhone() {
        // 9-digit local number starting with 7 → 7XXXXXXXX
        Random r = new Random();
        StringBuilder sb = new StringBuilder("7");
        for (int i = 0; i < 8; i++) sb.append(r.nextInt(10));
        return sb.toString();
    }

    @Test
    public void agreeButtonEnablesAfterValidInput() {
        String phoneLocal = randomLocalPhone();

        signUp.open();
        signUp.switchToPhoneTab();
        signUp.setFirstName("Test");
        signUp.setLastName("User");
        signUp.selectGender("Male");
        signUp.setPhone(phoneLocal);
        signUp.setPassword("Str0ng!Passw0rd");
        signUp.setConfirmPassword("Str0ng!Passw0rd");
        signUp.acceptTerms();
        signUp.submit();
    }
}
