package com.watermelon.core.webelements;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Wrap a table Webelement
 *
 * @author AM
 *
 */
public class WebTable {

	private final WebElement table;
	private WebElement tbody;
	private final List<WebElement> header;
	List<WebElement> rows;
	int numRows;
	int numColumns;
	boolean hasHeader;

	private List<String> columnNames;
	private Map<String, Integer> columnMap;
	public static final By DEFAULT_ROWLOCATOR = By.tagName("tr");

	/**
	 *
	 * @param webtable    the html table element
	 * @param hasHeader   {@literal true} if the table has a {@code th} header,
	 *                    {@literal false} otherwise
	 * @param rowsLocator the web locator used to identify the table rows, since
	 *                    {@code tr} is not always sufficient
	 */
	public WebTable(final WebElement webtable, By rowsLocator) {
		this.table = webtable;
		this.header = table.findElements(By.tagName("th"));
		hasHeader = !header.isEmpty();
		if (hasHeader) {
			this.tbody = table.findElement(By.tagName("tbody"));
		} else {
			this.tbody = table;
		}
		parse();
		rows = tbody.findElements(rowsLocator);
		numRows = rows.size();
		numColumns = header.size();
	}

	/**
	 *
	 * @param table the html table
	 */
	public WebTable(WebElement table) {
		this(table, DEFAULT_ROWLOCATOR);
	}

	/**
	 *
	 * @return the cells in the first row, if {@link #hasHeader} = true, otherwise
	 *         an {@link Optional#empty}
	 */
	public List<WebElement> getHeader() {
		return header;
	}

	/**
	 * 
	 * @param row the row element
	 * @return a {@link List} 
	 */
	private List<WebElement> getColumnsByRow(WebElement row) {
		return row.findElements(By.tagName("td"));
	}

	public WebElement getCell(String columnName, int rowNum) {
		return getColumnsByRow(rows.get(rowNum)).get(columnMap.get(columnName));

	}

	/**
	 *
	 * @param column the column number (zero based)
	 * @param rowNum the row number (zero based, included the row of eventual
	 *               header)
	 * @return
	 */
	public WebElement getCell(int column, int rowNum) {
		return getColumnsByRow(rows.get(rowNum)).get(column);
	}

	private void parse() {
		this.columnNames = table.findElements(By.tagName("th")) // get table headers
				.stream().map(WebElement::getText).map(String::trim).toList();
		this.columnMap = IntStream.range(0, columnNames.size()).boxed()
				.collect(Collectors.toMap(columnNames::get, Function.identity()));

	}

	public WebElement getTable() {
		return table;
	}

	public WebElement getBody() {
		return tbody;
	}

	public List<WebElement> getRows() {
		return rows;
	}

	/**
	 * 
	 * @return the number of rows of the table
	 */
	public int getNumRows() {
		return numRows;
	}

	/**
	 * 
	 * @return the number of columns of the table
	 */
	public int getNumColumns() {
		return numColumns;
	}

	/**
	 * 
	 * @return if the table has a header row or not
	 */
	public boolean hasHeader() {
		return hasHeader;
	}

	/**
	 * 
	 * @return the text content of the header's cells of the table
	 */
	public List<String> getColumnNames() {
		return columnNames;
	}

	/**
	 * 
	 * @return a {@link Map} with the column name as key and the column position as value
	 */
	public Map<String, Integer> getColumnMap() {
		return columnMap;
	}

}
