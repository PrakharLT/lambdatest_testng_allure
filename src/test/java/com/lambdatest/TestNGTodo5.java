package com.lambdatest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;

public class TestNGTodo5 {

    private RemoteWebDriver driver;
    private String Status = "failed";

    @BeforeMethod
    public void setup(Method m, ITestContext ctx) throws MalformedURLException {
        String username = System.getenv("LT_USERNAME") == null ? "Your LT Username" : System.getenv("LT_USERNAME");
        String authkey = System.getenv("LT_ACCESS_KEY") == null ? "Your LT AccessKey" : System.getenv("LT_ACCESS_KEY");
        ;
        String hub = "@hub.lambdatest.com/wd/hub";

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platform", "MacOS Catalina");
        caps.setCapability("browserName", "Opera");
        caps.setCapability("version", "latest");
        caps.setCapability("build", "TestNG With Java");
        caps.setCapability("name", m.getName() + this.getClass().getName());
        caps.setCapability("plugin", "git-testng");

        String[] Tags = new String[] { "Feature", "Tag", "Moderate" };
        caps.setCapability("tags", Tags);

        driver = new RemoteWebDriver(new URL("https://" + username + ":" + authkey + hub), caps);
    }
    @Test
    public void testAddToCart() {
        // Navigate to Amazon
        driver.get("https://www.amazon.com");

        // Find the search input field
        WebElement searchInput = driver.findElement(By.id("twotabsearchtextbox"));

        // Enter the search query
        searchInput.sendKeys("Headphones");

        // Submit the search form
        searchInput.submit();

        // Click on the first search result (a product)
        WebElement firstProduct = driver.findElement(By.cssSelector("div.s-search-results div[data-index='0']"));
        firstProduct.click();

        // Add the product to the cart
        WebElement addToCartButton = driver.findElement(By.id("add-to-cart-button"));
        addToCartButton.click();

        // Wait for the cart to update (you may need to implement proper waits)
        // For simplicity, we'll just use a sleep here, but it's not recommended in real tests.
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Verify the cart contains the added product
        WebElement cartCount = driver.findElement(By.id("nav-cart-count"));
        int itemCount = Integer.parseInt(cartCount.getText().trim());
        Assert.assertEquals(itemCount, 1, "Cart does not contain the expected number of items");
        Status = "passed";
    }

    @AfterMethod
    public void tearDown() {
        driver.executeScript("lambda-status=" + Status);
        driver.quit();
    }

}