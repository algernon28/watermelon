package com.watermelon.tests.webelements;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.watermelon.core.webelements.WebTable;

import io.github.bonigarcia.wdm.WebDriverManager;

public class WebTableTest {
	private WebDriver driver;
	private WebTable table;

	@BeforeClass
	public void beforeTest() {
		this.driver = WebDriverManager.chromedriver().create();
		Path htmlTableFile = Paths.get("src/test/resources/webtable.html");
		driver.navigate().to(htmlTableFile.toUri().toString());
		WebElement tableElement = driver.findElement(By.id("table1"));
		table = new WebTable(tableElement);

	}

	@AfterClass
	public void cleanUp() {
		driver.quit();
	}

	@Test
	public void getCellByColumnNameRowNumberTest() {
		assertThat(table).returns("http://www.frank.com", t -> t.getCell("Web Site", 1).getText());
	}

	@Test
	public void getCellByColumnNumberRowNumberTest() {
		assertThat(table).returns("fbach@yahoo.com", t -> t.getCell(2, 1).getText());
	}

	@Test
	public void getColumnMapTest() {
		List<String> actual = table.getColumnMap().entrySet().stream().map(Map.Entry::getKey).toList();
		List<String>  expected = List.of("Last Name", "First Name", "Email", "Due", "Web Site", "Action");
		assertThat(actual).containsAll(expected);
	}

	@Test
	public void getColumnNamesTest() {
		List<String>  expected = List.of("Last Name", "First Name", "Email", "Due", "Web Site", "Action");
		assertThat(table).returns(expected, WebTable::getColumnNames);
	}

	@Test
	public void getHeaderTest() {
		List<String>  expected = List.of("Last Name", "First Name", "Email", "Due", "Web Site", "Action");
		List<String> actual = table.getHeader().stream().map(WebElement::getText).toList();
		assertThat(actual).containsAll(expected);
	}

	@Test
	public void getNumColumnsTest() {
		assertThat(table).returns(6, WebTable::getNumColumns);
	}

	@Test
	public void getNumRowsTest() {
		assertThat(table).returns(4, WebTable::getNumRows);
	}



	@Test
	public void shoudHaveHeaderTest() {
		assertThat(table).returns(true, WebTable::hasHeader);
	}

	@Test
	public void shoudNotHaveHeaderTest() {
		WebTable tableWithoutHeader = new WebTable(table.getBody());
		assertThat(tableWithoutHeader).returns(false, WebTable::hasHeader);
	}

}
