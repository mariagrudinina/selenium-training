package ru.stqa.training.selenium;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class CountriesTest {
    private WebDriver driver;


    @Before
    public void start() {
        driver = new ChromeDriver();
        login();
    }

    public void login() {
        driver.get("http://localhost/litecart/admin/");
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("admin");
        driver.findElement(By.name("login")).click();
    }

    void verifyAscendingSorting(List<WebElement> columnObjects) {
        List<String> column = new ArrayList<>();
        for (WebElement cell : columnObjects) {
            column.add(cell.getText());
        }
        List<String> sortedColumn = new ArrayList<>(column);
        Collections.sort(sortedColumn);
        assertEquals(sortedColumn, column);
    }

    @Test
    public void verifyCountriesSorting() {
        driver.get("http://localhost/litecart/admin/?app=countries&doc=countries");
        List<WebElement> countryObjects = driver.findElements(By.xpath("//form[@name='countries_form']//td[5]"));
        verifyAscendingSorting(countryObjects);
    }

    @Test
    public void verifyZonesSorting() {
        driver.get("http://localhost/litecart/admin/?app=countries&doc=countries");
        List<WebElement> rows = driver.findElements(By.xpath("//form[@name='countries_form']//tr[@class='row']"));
        List<String> links = new ArrayList<>();
        for (WebElement row : rows) {
            int zone = Integer.parseInt(row.findElement(By.xpath(".//td[6]")).getText());
            if (zone > 0) {
                links.add(row.findElement(By.xpath(".//td[5]/a")).getAttribute("href"));
            }
        }
        for (String link : links) {
            driver.get(link);
            List<WebElement> zonesObjects = driver.findElements(By.xpath("//table[@id='table-zones']//td[3]"));
            zonesObjects.remove(zonesObjects.size() - 1); // удалим последнюю строку, так как там поля ввода
            verifyAscendingSorting(zonesObjects);
        }
    }

    @Test
    public void verifyGeoZonesSorting() {
        driver.get("http://localhost/litecart/admin/?app=geo_zones&doc=geo_zones");
        List<WebElement> rows = driver.findElements(By.xpath("//form[@name='geo_zones_form']//tr[@class='row']/td[3]/a"));
        List<String> links = new ArrayList<>();
        for (WebElement row : rows) {
            links.add(row.getAttribute("href"));
        }
        for (String link : links) {
            driver.get(link);
            List<WebElement> zonesObjects = driver.findElements(By.xpath("//table[@id='table-zones']//td[3]/select/option[@selected]"));
            verifyAscendingSorting(zonesObjects);
        }
    }

    @After
    public void stop() {
        driver.quit();
        driver = null;
    }
}
