package ru.stqa.training.selenium.tests;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CartTest extends TestBase {


    @Test
    public void verifyAddNewProduct() {
        app.addFirstProductToCart();
        app.addFirstProductToCart();
        app.addFirstProductToCart();
        // перейдем на главную проверить значение счетчика
        assertEquals(3, app.getCounter());
        app.openCart();
        app.removeAllProductsFromCart();
    }
}
