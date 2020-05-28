package ru.stqa.training.selenium.app;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import ru.stqa.training.selenium.pages.CartPage;
import ru.stqa.training.selenium.pages.Homepage;
import ru.stqa.training.selenium.pages.ProductPage;

public class Application {

    private WebDriver driver;
    private Homepage homepage;
    private ProductPage productPage;
    private CartPage cartPage;

    public Application() {
        driver = new ChromeDriver();
        homepage = new Homepage(driver);
        productPage = new ProductPage(driver);
        cartPage = new CartPage(driver);
    }

    public void quit() {
        driver.quit();
    }


    public void addFirstProductToCart() {
        homepage.open();
        homepage.products.get(0).click();
        productPage.waitUntilItIsLoaded();
        // у некоторых товаров есть обязательное поле Size
        productPage.selectFirstSizeOptionIfNeeded();
        productPage.addToCart();
    }

    public int getCounter() {
        homepage.open();
        return Integer.parseInt(homepage.quantity.getText());
    }

    public void openCart() {
        homepage.openCart();
        // ждем загрузки товаров
        cartPage.waitUntilItIsLoaded();
    }

    public void removeAllProductsFromCart() {
        cartPage.removeAllProducts();
    }

}