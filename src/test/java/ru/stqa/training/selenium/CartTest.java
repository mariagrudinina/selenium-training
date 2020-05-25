package ru.stqa.training.selenium;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class CartTest {
    private WebDriver driver;
    private WebDriverWait wait;


    @Before
    public void start() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 10);
    }

    public void addFirstProductToCart() {
        driver.get("http://localhost/litecart/en/");
        driver.findElement(By.cssSelector(".product>a.link")).click();
        WebElement addCart = wait.until(ExpectedConditions.elementToBeClickable(By.name("add_cart_product")));
        List<WebElement> size = driver.findElements(By.name("options[Size]"));
        if (size.size() > 0) {
            new Select(size.get(0)).selectByIndex(1); // у некоторых товаров есть обязательное поле Size
        }
        addCart.click();
        // после добавления товара в корзину появляется алерт с текстом "Error"
        // счетчик товаров обновляется только после обновления страницы
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().dismiss();
    }

    @Test
    public void verifyAddNewProduct() {
        addFirstProductToCart();
        addFirstProductToCart();
        addFirstProductToCart();
        // перейдем на главную проверить значение счетчика
        driver.get("http://localhost/litecart/en/");
        int cartQuantityAfter = Integer.parseInt(driver.findElement(By.className("quantity")).getText());
        assertEquals(3, cartQuantityAfter);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(),'Checkout')]"))).click();
        // ждем загрузки товаров
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//ul[@class='items']")));
        List<WebElement> productsInTable = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                By.cssSelector(".dataTable td.item")));
        int productsBefore = productsInTable.size();
        if (productsBefore > 1) {
            for (int i = 0; i < productsBefore - 1; i++) {
                // кликнули по первому продукту в карусели
                WebElement shortcut = driver.findElement(By.xpath("//ul[@class='shortcuts']/li[1]/a"));
                shortcut.click();
                wait.until(ExpectedConditions.attributeContains(shortcut, "class", "inact"));
                // нашли кнопку Remove
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//ul[@class='items']/li[1]//button[@value='Remove']"))).click();
                wait.until(ExpectedConditions.stalenessOf(productsInTable.get(0)));
                wait.until(ExpectedConditions.stalenessOf(shortcut));
                productsInTable = driver.findElements(By.cssSelector(".dataTable td.item"));
            }
        }
        // удаляем последний товар
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//ul[@class='items']/li[1]//button[@value='Remove']"))).click();
        wait.until(ExpectedConditions.stalenessOf(productsInTable.get(0)));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//em[contains(text(),'There are no items in your cart.')]")));
    }

    @After
    public void stop() {
        driver.quit();
        driver = null;
    }
}
