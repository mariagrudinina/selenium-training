package ru.stqa.training.selenium;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import java.util.List;

import static org.junit.Assert.*;

public class StickerTest {
    private WebDriver driver;


    @Before
    public void start() {
        driver = new ChromeDriver();
    }


    @Test
    public void verifyStickers() {
        driver.get("http://localhost/litecart/");
        List<WebElement> products = driver.findElements(By.cssSelector("li.product"));
        assertTrue(products.size() > 0);
        for (WebElement product : products){
            List<WebElement> stickers = product.findElements(By.cssSelector("div.sticker"));
            assertEquals(1, stickers.size());
        }
    }

    @After
    public void stop() {
        driver.quit();
        driver = null;
    }
}
