package ru.stqa.training.selenium;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Set;


public class LinkTest {
    private WebDriver driver;
    private WebDriverWait wait;


    @Before
    public void start() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 10);
        login();
    }

    public void login() {
        driver.get("http://localhost/litecart/admin/");
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("admin");
        driver.findElement(By.name("login")).click();
    }

    public ExpectedCondition<String> anyWindowOtherThan(Set<String> oldWindows) {
        return new ExpectedCondition<String>() {
            public String apply(WebDriver driver) {
                Set<String> handles = driver.getWindowHandles();
                handles.removeAll(oldWindows);
                return handles.size() > 0 ? handles.iterator().next() : null;
            }
        };
    }

    public void verifyLinkOpensInNewWindow(By linkLocator, Set<String> oldWindows, String title, String mainWindow) {
        driver.findElement(linkLocator).click();
        String newWindow = wait.until(anyWindowOtherThan(oldWindows));
        driver.switchTo().window(newWindow);
        wait.until(ExpectedConditions.titleIs(title));
        driver.close();
        driver.switchTo().window(mainWindow);
    }

    @Test
    public void verifyLinks() {
        driver.get("http://localhost/litecart/admin/?app=countries&doc=countries");
        driver.findElement(By.className("button")).click();
        String mainWindow = driver.getWindowHandle();
        Set<String> oldWindows = driver.getWindowHandles();
        verifyLinkOpensInNewWindow(By.xpath("//td[text()=' (ISO 3166-1 alpha-2)']/a"),
                oldWindows, "ISO 3166-1 alpha-2 - Wikipedia", mainWindow);
        verifyLinkOpensInNewWindow(By.xpath("//td[text()=' (ISO 3166-1 alpha-3) ']/a"),
                oldWindows, "ISO 3166-1 alpha-3 - Wikipedia", mainWindow);
        verifyLinkOpensInNewWindow(By.xpath("//strong[text()='Tax ID Format']/../a"),
                oldWindows, "Regular expression - Wikipedia", mainWindow);
        verifyLinkOpensInNewWindow(By.xpath("//strong[text()='Address Format']/../a[.//i]"),
                oldWindows, "International Address Format Validator: Verify Mailing Formats | Informatica",
                mainWindow);
        verifyLinkOpensInNewWindow(By.xpath("//strong[text()='Postcode Format']/../a"),
                oldWindows, "Regular expression - Wikipedia", mainWindow);
        verifyLinkOpensInNewWindow(By.xpath("//strong[text()='Currency Code']/../a"),
                oldWindows, "List of countries and capitals with currency and language - Wikipedia", mainWindow);
        verifyLinkOpensInNewWindow(By.xpath("//strong[text()='Phone Country Code']/../a"),
                oldWindows, "List of country calling codes - Wikipedia", mainWindow);
    }

    @After
    public void stop() {
        driver.quit();
        driver = null;
    }
}
