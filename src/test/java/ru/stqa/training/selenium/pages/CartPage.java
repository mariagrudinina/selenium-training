package ru.stqa.training.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class CartPage extends Page {
    public CartPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(css = ".dataTable td.item")
    public List<WebElement> products;

    @FindBy(xpath = "//ul[@class='items']/li[1]//button[@value='Remove']")
    private WebElement removeBtn;

    public void waitUntilItIsLoaded() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//ul[@class='items']")));
        wait.until(ExpectedConditions.visibilityOfAllElements(products));
    }

    public void removeAllProducts() {
        int productsBefore = products.size();
        if (productsBefore > 1) {
            for (int i = 0; i < productsBefore - 1; i++) {
                // кликнули по первому продукту в карусели
                WebElement shortcut = driver.findElement(By.xpath("//ul[@class='shortcuts']/li[1]/a"));
                shortcut.click();
                wait.until(ExpectedConditions.attributeContains(shortcut, "class", "inact"));
                // нашли кнопку Remove
                wait.until(ExpectedConditions.visibilityOf(removeBtn)).click();
                wait.until(ExpectedConditions.stalenessOf(products.get(0)));
                wait.until(ExpectedConditions.stalenessOf(shortcut));
            }
        }
        // удаляем последний товар
        wait.until(ExpectedConditions.visibilityOf(removeBtn)).click();
        wait.until(ExpectedConditions.stalenessOf(products.get(0)));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//em[contains(text(),'There are no items in your cart.')]")));
    }
}
