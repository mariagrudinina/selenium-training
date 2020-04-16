package ru.stqa.training.selenium;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.WebDriver;

import static org.junit.Assert.assertEquals;

public class MyFirstTest {
    private WebDriver driver;

    @Before
    public void start() {
        driver = new ChromeDriver();
    }

    @Test
    public void login() {
        driver.get("http://localhost/litecart/admin/");
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("admin");
        driver.findElement(By.name("login")).click();
        assertEquals("You are now logged in as admin",
                driver.findElement(By.xpath("//div[@class='notice success']")).getText());
    }

    @After
    public void stop() {
        driver.quit();
        driver = null;
    }
}
