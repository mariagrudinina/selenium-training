package ru.stqa.training.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class ProductPage extends Page {
    public ProductPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(css = ".product>a.link")
    public List<WebElement> products;
    private By addCartBtn = By.name("add_cart_product");
    private By sizeSelect = By.name("options[Size]");

    public void selectFirstSizeOptionIfNeeded() {
        List<WebElement> size = driver.findElements(sizeSelect);
        if (size.size() > 0) {
            new Select(size.get(0)).selectByIndex(1);
        }
    }

    public void addToCart() {
        driver.findElement(addCartBtn).click();
        // после добавления товара в корзину появляется алерт с текстом "Error"
        // счетчик товаров обновляется только после обновления страницы
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().dismiss();
    }

    public void waitUntilItIsLoaded() {
        wait.until(ExpectedConditions.elementToBeClickable(addCartBtn));
    }

}
