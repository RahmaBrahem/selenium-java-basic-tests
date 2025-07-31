/**
 * Copyright (c) 2025 RahmaBrahem
 * 
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for license information.
 */
package com.lineairecode.extent;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import atu.testrecorder.ATUTestRecorder;
import atu.testrecorder.exceptions.ATUTestRecorderException;
import io.github.bonigarcia.wdm.WebDriverManager;

public class STSauceLabs {

	private static WebDriver driver;
	private static WebDriverWait wait;
	private static Properties prop;
	private static ExtentReports extent;
	private static ATUTestRecorder recorder;

	public String methodName;
	private static final String VIDEO_LOCATION = "C:\\Users\\Hp\\eclipse-workspace\\SeleniumWebDriverAvance\\target\\Videos\\";
	private static com.aventstack.extentreports.ExtentTest test;

	public String getActualDateTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yy_MM_dd_HH_mm_ss");
		return dateFormat.format(new Date());
	}

	public String getVideoFileName(String methodName) {
		return "Video_" + methodName + "_" + getActualDateTime();
	}

	private void autoRecorderStart() throws ATUTestRecorderException {
		new File(VIDEO_LOCATION).mkdirs();
		recorder = new ATUTestRecorder(VIDEO_LOCATION, getVideoFileName(methodName), false);
		recorder.start();
	}

	private void autoRecorderFin() throws ATUTestRecorderException {
		if (recorder != null) {
			recorder.stop();
		}
	}

	private String getScreenshotPath(String imageName) throws IOException {
		String imageLocation = "C:\\Users\\Hp\\eclipse-workspace\\SeleniumWebDriverAvance\\target\\Screenshots\\";
		new File(imageLocation).mkdirs();
		String actualImageName = imageLocation + imageName + "_" + getActualDateTime() + ".png";
		File sourceFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		File destFileImg = new File(actualImageName);
		FileUtils.copyFile(sourceFile, destFileImg);
		return actualImageName;
	}

	@BeforeSuite
	public void setupBeforeSuite() throws IOException {
		prop = new Properties();
		FileInputStream fis = new FileInputStream("src/test/resources/Data/data.properties");
		prop.load(fis);
		new File("target/Spark").mkdirs();
		String reportName = "target/Spark/SparkReport_" + getActualDateTime() + ".html";
		ExtentSparkReporter spark = new ExtentSparkReporter(reportName);
		spark.config().setTheme(Theme.DARK);
		spark.config().setDocumentTitle("Formation Auto Test Report");
		spark.config().setReportName("Automated Test Rahma's Report");
		spark.config().setTimeStampFormat("dd/MM/yyyy HH:mm:ss");

		extent = new ExtentReports();
		extent.attachReporter(spark);
	}

	@BeforeClass
	public void setupBeforeClass() {
		String browser = prop.getProperty("browser", "chrome");
		if (browser.equalsIgnoreCase("chrome")) {
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
		} else if (browser.equalsIgnoreCase("firefox")) {
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
		} else {
			throw new IllegalArgumentException("Browser not supported: " + browser);
		}

		driver.get(prop.getProperty("baseUrl"));
		driver.manage().window().maximize();
		wait = new WebDriverWait(driver, Duration.ofSeconds(30));
	}

	@BeforeMethod
	public void setupBeforeMethod(ITestResult result) {
		this.methodName = result.getMethod().getMethodName();
		try {
			autoRecorderStart();
		} catch (ATUTestRecorderException e) {
			e.printStackTrace();
		}
	}

	@Test(priority = 1)
	public void failedLoginTest() throws IOException {
		test = extent.createTest("Failed Login Test");
		try {
			WebElement username = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name")));
			username.sendKeys(prop.getProperty("fake_username"));

			WebElement password = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
			password.sendKeys(prop.getProperty("wrong_password"));

			WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("login-button")));
			loginButton.click();

			WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
					By.xpath("//*[contains(text(),'Epic sadface: Username and password')]")));

			Assert.assertTrue(errorMessage.isDisplayed(), "Error message not displayed!");

			test.pass("Login failed as expected with an incorrect username and password.");

		} catch (AssertionError e) {
			test.fail("Error message not displayed: " + e.getMessage());
			throw e;
		}
	}

	@Test(priority = 2)
	public void lockedOutUserTest() throws IOException {
		test = extent.createTest("Locked Out User Test");
		try {
			WebElement username = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name")));
			username.clear();
			username.sendKeys(prop.getProperty("locked_out_username"));

			WebElement password = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
			password.clear();
			password.sendKeys(prop.getProperty("password"));

			WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("login-button")));
			loginButton.click();

			WebElement errorMessage = wait.until(
					ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(),'locked out')]")));

			Assert.assertTrue(errorMessage.isDisplayed(), "Error message not displayed!");

			test.pass("Login failed as expected with locked out username.");
		} catch (AssertionError e) {
			test.fail("Error message not displayed: " + e.getMessage());
			throw e;
		}
	}

	@Test(priority = 3)
	public void validLoginTest() throws IOException {
		test = extent.createTest("Valid Login Test");
		try {
			WebElement username = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name")));
			username.clear();
			username.sendKeys(prop.getProperty("valid_username"));

			WebElement password = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
			password.clear();
			password.sendKeys(prop.getProperty("password"));

			WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("login-button")));
			loginButton.click();

			WebElement productsPage = wait
					.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[text()='Products']")));

			Assert.assertTrue(productsPage.isDisplayed(), "Products page not displayed!");

			test.pass("Logged in successfully as a standard user.");
		} catch (AssertionError e) {
			test.fail("Login failed: " + e.getMessage());
			throw e;
		}
	}

	@Test(priority = 4)
	public void addToCartTest() throws IOException {
		test = extent.createTest("Add To Cart Test");
		try {
			WebElement firstProduct = wait.until(ExpectedConditions.visibilityOfElementLocated(
					By.xpath("/html/body/div/div/div/div[2]/div/div/div/div[1]/div[2]/div[2]/button")));
			firstProduct.click();

			WebElement cartBadge = wait
					.until(ExpectedConditions.visibilityOfElementLocated(By.className("shopping_cart_badge")));

			Assert.assertEquals(cartBadge.getText(), "1", "Product not added to cart!");

			test.pass("Product successfully added to cart.");
		} catch (AssertionError e) {
			test.fail("Failed to add product to cart: " + e.getMessage());
			throw e;

		}
	}

	@Test(priority = 5)
	public void viewCartTest() throws IOException {
		test = extent.createTest("View Cart Test");
		try {
			WebElement cartButton = wait
					.until(ExpectedConditions.elementToBeClickable(By.className("shopping_cart_link")));
			cartButton.click();

			WebElement cartTitle = wait
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("cart_contents_container")));

			Assert.assertTrue(cartTitle.isDisplayed(), "Cart title not displayed!");

			test.pass("Successfully viewed the cart.");
		} catch (AssertionError e) {
			test.fail("Failed to view cart: " + e.getMessage());
			throw e;
		}
	}

	@Test(priority = 6)
	public void removeFromCartTest() throws IOException {
		test = extent.createTest("Remove From Cart Test");
		try {
			WebElement removeButton = wait
					.until(ExpectedConditions.elementToBeClickable(By.id("remove-sauce-labs-backpack")));
			removeButton.click();

			wait.until(ExpectedConditions.invisibilityOf(removeButton));

			test.pass("Product successfully removed from cart.");
		} catch (AssertionError e) {
			test.fail("Failed to remove product from cart: " + e.getMessage());
			throw e;
		}
	}

	@Test(priority = 7)
	public void checkOutTest() throws IOException {
		test = extent.createTest("Check Out Test");
		try {
			WebElement checkOutButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("checkout")));
			checkOutButton.click();

			Assert.assertEquals((driver.findElement(By.xpath("//*[contains(text(),'Checkout:')]")).getText()),
					"Checkout: Your Information");

			WebElement firstNameChamp = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("first-name")));
			firstNameChamp.sendKeys(prop.getProperty("First_Name"));
			WebElement lastNameChamp = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("last-name")));
			lastNameChamp.sendKeys(prop.getProperty("Last_Name"));
			WebElement codeChamp = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("postal-code")));
			codeChamp.sendKeys(prop.getProperty("Zip_Postal_Code"));
			WebElement continueButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("continue")));
			continueButton.click();
			WebElement finishButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("finish")));
			finishButton.click();
			WebElement checkoutComplete = wait
					.until(ExpectedConditions.visibilityOfElementLocated(By.className("complete-header")));

			Assert.assertTrue(checkoutComplete.isDisplayed(), "Checkout was not successful!");
			Assert.assertEquals(checkoutComplete.getText(), "Thank you for your order!");

			test.pass("Checkout completed successfully.");
		} catch (AssertionError e) {
			test.fail("Checkout failed: " + e.getMessage());
			throw e;
		}
	}

	@Test(priority = 8)
	public void filterProductsTest() throws IOException {
		test = extent.createTest("Filter Products Test");
		try {
			WebElement backToHomeButton = driver.findElement(By.id("back-to-products"));
			backToHomeButton.click();

			WebElement filterDropdown = wait
					.until(ExpectedConditions.elementToBeClickable(By.className("product_sort_container")));
			filterDropdown.click();

			WebElement lowToHighOption = wait.until(ExpectedConditions
					.elementToBeClickable(By.xpath("//option[contains(text(), 'Price (low to high)')]")));
			lowToHighOption.click();

			List<WebElement> productPrices = driver.findElements(By.className("inventory_item_price"));
			boolean isSorted = true;
			double previousPrice = 0.0;

			for (WebElement priceElement : productPrices) {
				double currentPrice = Double.parseDouble(priceElement.getText().replace("$", ""));
				if (currentPrice < previousPrice) {
					isSorted = false;
					break;
				}
				previousPrice = currentPrice;
			}

			Assert.assertTrue(isSorted, "Les produits ne sont pas triés par prix!");

			test.pass("Filtre des produits appliqué avec succès.");
		} catch (AssertionError e) {
			test.fail("Echec de l'application du filtre des produits: " + e.getMessage());
			throw e;
		}
	}

	@Test(priority = 9)
	public void logOutTest() throws IOException {
		test = extent.createTest("Log Out Test");
		try {
			WebElement menuButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("react-burger-menu-btn")));
			menuButton.click();
			WebElement logoutButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("logout_sidebar_link")));
			logoutButton.click();

			WebElement loginButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));

			Assert.assertTrue(loginButton.isDisplayed(), "Login button not displayed after logout!");

			test.pass("Logged out successfully.");
		} catch (AssertionError e) {
			test.fail("Logout failed: " + e.getMessage());
			throw e;
		}
	}

	@AfterMethod
	public void tearDownMethod(ITestResult result) throws IOException, ATUTestRecorderException {
		test.addScreenCaptureFromPath(getScreenshotPath("Image_" + methodName));

		test.log(Status.INFO,
				"Vidéo ajoutée au rapport: <a href='" + VIDEO_LOCATION + "'>Cliquez ici pour voir la vidéo</a>");

		if (result.getStatus() == ITestResult.SUCCESS) {
			test.log(Status.PASS, "Test passed.");
		} else if (result.getStatus() == ITestResult.FAILURE) {
			test.log(Status.FAIL, "Test failed: " + result.getThrowable().getMessage());
		} else {
			test.log(Status.SKIP, "Test skipped.");
		}

		autoRecorderFin();
	}

	@AfterClass
	public void tearDownClass() {
		if (driver != null)
			driver.quit();

	}

	@AfterSuite
	public void tearDownSuite() {
		extent.flush();
	}

}
