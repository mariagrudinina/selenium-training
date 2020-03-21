package ru.stqa.training.selenium;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.WebDriver;

public class MyFirstTest {
    private WebDriver driver;

    @Before
    public void start() {
        driver = new ChromeDriver();
    }

    @Test
    public void myFirstTest() {
        driver.get("http://google.com");
    }

    @After
    public void stop() {
        driver.quit();
        driver = null;
    }
}
