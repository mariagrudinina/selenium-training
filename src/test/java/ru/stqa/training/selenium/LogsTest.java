package ru.stqa.training.selenium;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.*;

import static org.junit.Assert.assertEquals;


public class LogsTest {
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

    public List<LogEntry> verifyLinkOpensInNewWindow(String url, Set<String> oldWindows, String mainWindow) {
        ((JavascriptExecutor) driver).executeScript("window.open()");
        String newWindow = wait.until(anyWindowOtherThan(oldWindows));
        driver.switchTo().window(newWindow);
        driver.get(url);
        wait.until(ExpectedConditions.titleContains("Edit Product:"));
        List<LogEntry> logs = driver.manage().logs().get("browser").getAll();
        driver.close();
        driver.switchTo().window(mainWindow);
        return logs;
    }

    @Test
    public void verifyLogs() {
        driver.get("http://localhost/litecart/admin/?app=catalog&doc=catalog&category_id=2");
        // получим все ссылки продуктов
        List<WebElement> links = driver.findElements(By.xpath("//td[3]/a[contains(@href,'edit_product')]"));
        List<String> urls = new ArrayList<>();
        links.forEach(l -> urls.add(l.getAttribute("href")));
        String mainWindow = driver.getWindowHandle();
        Set<String> oldWindows = driver.getWindowHandles();
        Map<String, List<LogEntry>> allLogs = new HashMap<>();
        for (String url : urls) {
            // пройдем по всем url и сохраним логи, чтобы тест не падал в начале списка
            List<LogEntry> logs = verifyLinkOpensInNewWindow(url, oldWindows, mainWindow);
            if (logs.size() != 0) {
                allLogs.put(url, logs);
                System.out.println(url + " " + Arrays.toString(logs.toArray()));
            }
        }
        assertEquals(0, allLogs.size());
    }

    @After
    public void stop() {
        driver.quit();
        driver = null;
    }
}
