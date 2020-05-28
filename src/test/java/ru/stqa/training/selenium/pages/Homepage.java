package ru.stqa.training.selenium.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class Homepage extends Page {
    public Homepage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(css = ".product>a.link")
    public List<WebElement> products;

    @FindBy(className = "quantity")
    public WebElement quantity;

    @FindBy(xpath = "//a[contains(text(),'Checkout')]")
    public WebElement checkoutLink;

    public void open() {
        driver.get("http://localhost/litecart/en/");
    }

    public void openCart() {
        wait.until(ExpectedConditions.elementToBeClickable(checkoutLink)).click();
    }
}
