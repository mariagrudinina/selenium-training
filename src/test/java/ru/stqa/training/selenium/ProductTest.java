package ru.stqa.training.selenium;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;

public class ProductTest {
    private WebDriver driver;


    @Before
    public void start() {
        driver = new ChromeDriver();
    }

    //а) на главной странице и на странице товара совпадает текст названия товара
    @Test
    public void verifyProductName() {
        driver.get("http://localhost/litecart/en/");
        WebElement firstProduct = driver.findElement(By.className("product"));
        String firstProductName = firstProduct.findElement(By.className("name")).getText();
        firstProduct.findElement(By.className("link")).click();
        String productNameOnDetailedPage = driver.findElement(By.cssSelector("#box-product .title")).getText();
        assertEquals(firstProductName, productNameOnDetailedPage);
    }

    //б) на главной странице и на странице товара совпадают цены (обычная)
    @Test
    public void verifyRegularPriceValue() {
        driver.get("http://localhost/litecart/en/");
        WebElement firstProductOnSale = driver.findElement(By.xpath("//li[.//strong[@class='campaign-price']]"));
        String regularPrice = firstProductOnSale.findElement(By.className("regular-price")).getText();
        firstProductOnSale.findElement(By.className("link")).click();
        String regularPriceOnDetailedPage = driver.findElement(By.cssSelector("#box-product .regular-price")).getText();
        assertEquals(regularPrice, regularPriceOnDetailedPage);
    }

    //б) на главной странице и на странице товара совпадают цены (акционная)
    @Test
    public void verifyCampaignPriceValue() {
        driver.get("http://localhost/litecart/en/");
        WebElement firstProductOnSale = driver.findElement(By.xpath("//li[.//strong[@class='campaign-price']]"));
        String campaignPrice = firstProductOnSale.findElement(By.className("campaign-price")).getText();
        firstProductOnSale.findElement(By.className("link")).click();
        String campaignPriceOnDetailedPage = driver.findElement(By.cssSelector("#box-product .campaign-price")).getText();
        assertEquals(campaignPrice, campaignPriceOnDetailedPage);
    }

    public String[] extractColors(WebElement priceObject) {
        String regularPriceColor = priceObject.getCssValue("color");
        String extracted = regularPriceColor.substring(regularPriceColor.indexOf('(') + 1, regularPriceColor.lastIndexOf(')'));
        return extracted.split(", ");
    }

    //в) обычная цена зачёркнутая и серая (главная страница)
    @Test
    public void verifyRegularPriceStyleOnListPage() {
        driver.get("http://localhost/litecart/en/");
        WebElement firstProductOnSale = driver.findElement(By.xpath("//li[.//strong[@class='campaign-price']]"));
        WebElement regularPrice = firstProductOnSale.findElement(By.className("regular-price"));
        String[] colors = extractColors(regularPrice);
        for (int i = 1; i < 3; i++) {
            assertEquals(colors[0], colors[i]);
        }
        String textDecoration = regularPrice.getCssValue("text-decoration");
        assertThat(textDecoration, containsString("line-through"));
    }

    //в) обычная цена зачёркнутая и серая (страница товара)
    @Test
    public void verifyRegularPriceStyleOnDetailedPage() {
        driver.get("http://localhost/litecart/en/");
        WebElement firstProductOnSale = driver.findElement(By.xpath("//li[.//strong[@class='campaign-price']]"));
        firstProductOnSale.findElement(By.className("link")).click();
        WebElement regularPrice = driver.findElement(By.cssSelector("#box-product .regular-price"));
        String[] colors = extractColors(regularPrice);
        for (int i = 1; i < 3; i++) {
            assertEquals(colors[0], colors[i]);
        }
        String textDecoration = regularPrice.getCssValue("text-decoration");
        assertThat(textDecoration, containsString("line-through"));
    }

    //г) акционная жирная и красная (главная страница)
    @Test
    public void verifyCampaignPriceStyleOnListPage() {
        driver.get("http://localhost/litecart/en/");
        WebElement firstProductOnSale = driver.findElement(By.xpath("//li[.//strong[@class='campaign-price']]"));
        WebElement campaignPrice = firstProductOnSale.findElement(By.className("campaign-price"));
        String[] colors = extractColors(campaignPrice);
        for (int i = 1; i < 3; i++) {
            assertEquals("0", colors[i]);
        }
        String fontWeight = campaignPrice.getCssValue("font-weight");
        assertTrue(Integer.parseInt(fontWeight) >= 700);
    }

    //г) акционная жирная и красная (страница товара)
    @Test
    public void verifyCampaignPriceStyleOnDetailedPage() {
        driver.get("http://localhost/litecart/en/");
        WebElement firstProductOnSale = driver.findElement(By.xpath("//li[.//strong[@class='campaign-price']]"));
        firstProductOnSale.findElement(By.className("link")).click();
        WebElement campaignPrice = driver.findElement(By.cssSelector("#box-product .campaign-price"));
        String[] colors = extractColors(campaignPrice);
        for (int i = 1; i < 3; i++) {
            assertEquals("0", colors[i]);
        }
        String fontWeight = campaignPrice.getCssValue("font-weight");
        assertTrue(Integer.parseInt(fontWeight) >= 700);
    }

    public float getFloatFontSize(WebElement priceObject) {
        return Float.parseFloat(priceObject.getCssValue("font-size").replace("px", ""));
    }

    //д) акционная цена крупнее, чем обычная (главная страница)
    @Test
    public void verifyPriceSizesOnListPage() {
        driver.get("http://localhost/litecart/en/");
        WebElement firstProductOnSale = driver.findElement(By.xpath("//li[.//strong[@class='campaign-price']]"));
        WebElement regularPrice = firstProductOnSale.findElement(By.className("regular-price"));
        WebElement campaignPrice = firstProductOnSale.findElement(By.className("campaign-price"));
        float fontSizeRegularPrice = getFloatFontSize(regularPrice);
        float fontSizeCampaignPrice = getFloatFontSize(campaignPrice);
        assertTrue(fontSizeRegularPrice < fontSizeCampaignPrice);
    }

    //д) акционная цена крупнее, чем обычная (страница товара)
    @Test
    public void verifyPriceSizesOnDetailedPage() {
        driver.get("http://localhost/litecart/en/");
        WebElement firstProductOnSale = driver.findElement(By.xpath("//li[.//strong[@class='campaign-price']]"));
        firstProductOnSale.findElement(By.className("link")).click();
        WebElement regularPrice = driver.findElement(By.cssSelector("#box-product .regular-price"));
        WebElement campaignPrice = driver.findElement(By.cssSelector("#box-product .campaign-price"));
        float fontSizeRegularPrice = getFloatFontSize(regularPrice);
        float fontSizeCampaignPrice = getFloatFontSize(campaignPrice);
        assertTrue(fontSizeRegularPrice < fontSizeCampaignPrice);
    }

    @After
    public void stop() {
        driver.quit();
        driver = null;
    }
}
