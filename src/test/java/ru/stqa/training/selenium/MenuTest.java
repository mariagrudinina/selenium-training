package ru.stqa.training.selenium;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MenuTest {
    private WebDriver driver;
    private WebDriverWait wait;


    @Before
    public void start() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 10);
    }

    public WebElement getCurrentItem(String menuItemName) {
        // нашли меню
        List<WebElement> menuItems = driver.findElements(By.cssSelector("ul#box-apps-menu"));
        assertEquals(1, menuItems.size());
        // добавила, иначе иногда клика по элементу не происходило
        wait.until(ExpectedConditions.visibilityOfAllElements(menuItems.get(0).findElements(By.tagName("li"))));
        // нашли текущий элемент списка
        return menuItems.get(0).findElement(By.xpath("//li[@id='app-']/a/span[text() = '" + menuItemName + "']"));
    }

    public WebElement getCurrentInnerItem(String innerItemName) {
        // нашли меню
        List<WebElement> menuItems = driver.findElements(By.cssSelector("ul#box-apps-menu"));
        assertEquals(1, menuItems.size());
        wait.until(ExpectedConditions.visibilityOfAllElements(menuItems.get(0).findElements(By.tagName("li"))));
        // нашли текущий элемент списка
        return menuItems.get(0).findElement(By.xpath("//ul[@class='docs']//span[text() = '" + innerItemName + "']"));
    }

    public void login() {
        driver.get("http://localhost/litecart/admin/");
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("admin");
        driver.findElement(By.name("login")).click();
    }

    public List<String> getListOfTextElements(List<WebElement> items) {
        List<String> itemNames = new ArrayList<>();
        for (WebElement item : items) {
            itemNames.add(item.getText());
        }
        return itemNames;
    }

    @Test
    public void verifyMenuItems() {
        login();
        List<WebElement> menuItems = driver.findElements(By.cssSelector("ul#box-apps-menu > li"));
        assertTrue(menuItems.size() > 0);
        // сохраним имена всех пунктов меню, буду искать по тексту ссылки, иначе stale element reference
        List<String> menuItemNames = getListOfTextElements(menuItems);
        for (String menuItemName : menuItemNames) {
            WebElement currentItem = getCurrentItem(menuItemName);
            currentItem.click();
            currentItem = getCurrentItem(menuItemName);
            // проверяем, есть ли вложенные пункты
            List<WebElement> innerItems = currentItem.findElements(By.xpath(".//../../ul/li"));
            if (!innerItems.isEmpty()) {
                List<String> innerItemsNames = getListOfTextElements(innerItems);
                for (String innerItemName : innerItemsNames) {
                    WebElement currentInnerItem = getCurrentInnerItem(innerItemName);
                    currentInnerItem.click();
                    String headerText = driver.findElement(By.tagName("h1")).getText();
                    assertNotEquals("", headerText);
                }
            } else {
                String headerText = driver.findElement(By.tagName("h1")).getText();
                assertNotEquals("", headerText);
            }
        }
    }

    @After
    public void stop() {
        driver.quit();
        driver = null;
    }
}
