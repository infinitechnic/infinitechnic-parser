package com.infinitechnic.parser.csv;

import com.infinitechnic.util.StringUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CsvReader {
	private final static String DEFAULT_DELIMITER = ",";
	private final static String REGEXP = "(?=([^\"]*\"[^\"]*\")*[^\"]*$)";
	private final static String DOUBLE_QUOTE = "\"";

	private File csvFile;
	private CsvDocument csvDocument;
	private String delimiter;
	private String regExpWithDelimiter;

	public CsvReader(final File csvFile, boolean hasHeader, String delimiter) {
		super();
		this.csvFile = csvFile;
		this.csvDocument = new CsvDocument(csvFile.getName(), hasHeader);

		setDelimiter(delimiter);
	}
	
	public CsvReader(final File csvFile, boolean hasHeader) {
		this(csvFile, hasHeader, DEFAULT_DELIMITER);
	}
	
	public CsvReader(final File csvFile) {
		this(csvFile, true);
	}

	public boolean isHasHeader() {
		return csvDocument.isHasHeader();
	}

	public void setHasHeader(boolean hasHeader) {
		csvDocument.setHasHeader(hasHeader);
	}

	public String getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(String delimiter) {
		if (StringUtil.isEmpty(delimiter)) {
			throw new IllegalArgumentException("Empty delimiter");
		}
		this.delimiter = delimiter;
		regExpWithDelimiter = delimiter + REGEXP;
	}

	public CsvDocument read() {
		try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
			csvDocument.clear();
			String lineStr = "";
			while ((lineStr = br.readLine()) != null) {
				if (csvDocument.isHasHeader() && csvDocument.getHeaders() == null) {
					csvDocument.setHeaders(toHeaders(lineStr));
				} else {
					csvDocument.addLine(new Line(toCells(lineStr)));
					//TODO: Warning message can be logged if no. of cells is diff from no. of headers
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Fail to read input file", e);
		}
		return csvDocument;
	}

	private List<String> toHeaders(String lineStr) {
		String[] headers = lineStr.split(regExpWithDelimiter);
		for (int i=0; i<headers.length; i++) {
			headers[i] = stripQuote(headers[i]);
		}
		return Arrays.asList(headers);
	}

	private List<Cell> toCells(String line) {
		String[] values = line.split(regExpWithDelimiter);
		int noOfHeaders = csvDocument.isHasHeader() ? csvDocument.getHeaders().size() : 0;
		int noOfValues = values.length;
		int noOfCells = noOfHeaders > noOfValues ? noOfHeaders : noOfValues;

		List<Cell> cells = new ArrayList<Cell>(noOfCells);
		for (int i=0; i<noOfCells; i++) {
			// If no. of values are less than the no. of headers, empty string will be set to cell
			cells.add(new Cell(i<noOfValues?stripQuote(values[i]):""));
		}
		return cells;
	}

	private String stripQuote(String value) {
		//TODO: maybe quote string can be specified in constructor
		if (value != null && value.startsWith(DOUBLE_QUOTE) && value.endsWith(DOUBLE_QUOTE)) {
			value = value.substring(1, value.length() - 1);
		}
		return value;
	}
}
