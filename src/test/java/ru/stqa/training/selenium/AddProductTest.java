package ru.stqa.training.selenium;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import java.sql.Timestamp;

import static java.lang.Thread.sleep;
import static org.junit.Assert.*;

public class AddProductTest {
    private WebDriver driver;


    @Before
    public void start() {
        driver = new ChromeDriver();
        driver.get("http://localhost/litecart/admin/");
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("admin");
        driver.findElement(By.name("login")).click();
    }

    @Test
    public void verifyAddingNewProduct() throws InterruptedException {
        String productName = "New product " + new Timestamp(System.currentTimeMillis()).getTime();
        driver.get("http://localhost/litecart/admin/?app=catalog&doc=catalog");
        driver.findElement(By.xpath("//a[contains(text(),'Add New Product')]")).click();
        sleep(3000);
        driver.findElement(By.xpath("//label[contains(text(),'Enabled')]")).click();
        driver.findElement(By.name("name[en]")).sendKeys(productName);
        driver.findElement(By.name("code")).sendKeys("Test code");
        driver.findElement(By.xpath("//input[@data-name='Rubber Ducks']")).click();
        new Select(driver.findElement(By.name("default_category_id"))).selectByVisibleText("Rubber Ducks");
        driver.findElement(By.xpath("//input[@value='1-3']")).click();
        WebElement quantity = driver.findElement(By.name("quantity"));
        quantity.clear();
        quantity.sendKeys("100");
        new Select(driver.findElement(By.name("sold_out_status_id"))).selectByVisibleText("-- Select --");
        driver.findElement(By.name("new_images[]")).sendKeys(System.getProperty("user.dir") + "\\heisenbug.jpeg");
        driver.findElement(By.name("date_valid_from")).sendKeys("25.05.2020");
        driver.findElement(By.xpath("//a[contains(text(),'Information')]")).click();
        sleep(3000);
        new Select(driver.findElement(By.name("manufacturer_id"))).selectByVisibleText("ACME Corp.");
        driver.findElement(By.name("keywords")).sendKeys("duck");
        driver.findElement(By.name("short_description[en]")).sendKeys("A new duck");
        driver.findElement(By.className("trumbowyg-editor")).sendKeys("Duck\nSome description");
        driver.findElement(By.name("head_title[en]")).sendKeys("Title");
        driver.findElement(By.name("meta_description[en]")).sendKeys("Meta description");
        driver.findElement(By.xpath("//a[contains(text(),'Prices')]")).click();
        sleep(3000);
        WebElement purchasePrice = driver.findElement(By.name("purchase_price"));
        purchasePrice.clear();
        purchasePrice.sendKeys("9.99");
        new Select(driver.findElement(By.name("purchase_price_currency_code"))).selectByValue("EUR");
        driver.findElement(By.name("prices[USD]")).sendKeys("10.89");
        driver.findElement(By.name("prices[EUR]")).sendKeys("9.99");
        driver.findElement(By.name("save")).click();
        assertTrue(driver.findElement(By.xpath("//table[@class='dataTable']//a[contains(text(),'" + productName
                + "')]")).isDisplayed());
    }

    @After
    public void stop() {
        driver.quit();
        driver = null;
    }
}
