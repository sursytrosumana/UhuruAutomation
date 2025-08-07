package org.example.test;

import org.example.pages.Setup;
import org.example.pages.LoginPage;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class loginTest {

    private WebDriver driver;
    private LoginPage loginPage;

    @BeforeEach
    void setUp() {
        // initialize browser, maximize, navigate to sign-in
        driver    = Setup.initialize();
        loginPage = new LoginPage(driver);
    }

    @AfterEach
    void tearDown() {
        Setup.quitDriver();
    }

    @Test
    @DisplayName("Login with valid credentials shows dashboard logo")
    void loginShowsDashboardLogo() {
        loginPage.login("68 012 3793","Nepal@123455" );
        assertTrue(
                loginPage.isDashboardLogoVisible(),
                "Dashboard logo should be visible after login"
        );
    }
}
