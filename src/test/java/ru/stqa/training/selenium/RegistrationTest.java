package ru.stqa.training.selenium;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import java.sql.Timestamp;

import static java.lang.Thread.sleep;
import static org.junit.Assert.assertTrue;

public class RegistrationTest {
    private WebDriver driver;


    @Before
    public void start() {
        driver = new ChromeDriver();
    }

    @Test
    public void verifyRegistration() throws InterruptedException {
        driver.get("http://localhost/litecart/en/create_account");
        String testEmail = "test_" + new Timestamp(System.currentTimeMillis()).getTime() + "@example.com";
        String testPassword = "1";
        driver.findElement(By.name("tax_id")).sendKeys("test_tax_id");
        driver.findElement(By.name("company")).sendKeys("test company");
        driver.findElement(By.name("firstname")).sendKeys("Test");
        driver.findElement(By.name("lastname")).sendKeys("User");
        driver.findElement(By.name("address1")).sendKeys("address 1");
        driver.findElement(By.name("address2")).sendKeys("address 2");
        driver.findElement(By.name("postcode")).sendKeys("10001");
        driver.findElement(By.name("city")).sendKeys("New York");
        new Select(driver.findElement(By.name("country_code"))).selectByVisibleText("United States");
        driver.findElement(By.name("email")).sendKeys(testEmail);
        driver.findElement(By.name("phone")).sendKeys("+1234567890");
        driver.findElement(By.name("password")).sendKeys(testPassword);
        driver.findElement(By.name("confirmed_password")).sendKeys(testPassword);
        driver.findElement(By.name("create_account")).click();
        // Почему-то значения "Zone/State/Province" не подгружаются, пока не отправить форму. Поэтому отправляю повторно
        new Select(driver.findElement(By.name("zone_code"))).selectByValue("NY");
        driver.findElement(By.name("password")).sendKeys(testPassword);
        driver.findElement(By.name("confirmed_password")).sendKeys(testPassword);
        driver.findElement(By.name("create_account")).click();
        // пыталась дождаться "elementToBeClickable" для ссылки, но почему-то ссылка только подчеркивалась,
        // как будто выполнен hover, но клика не было
        sleep(3000);
        driver.findElement(By.xpath("//div[@id='box-account']//a[contains(text(),'Logout')]")).click();
        sleep(3000);
        driver.findElement(By.name("email")).sendKeys(testEmail);
        driver.findElement(By.name("password")).sendKeys(testPassword);
        driver.findElement(By.name("login")).click();
        sleep(3000);
        assertTrue(driver.findElement(By.xpath("//div[@id='box-account']//a[contains(text(),'Logout')]")).isDisplayed());
    }

    @After
    public void stop() {
        driver.quit();
        driver = null;
    }
}
