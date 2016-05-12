package com.infinitechnic.parser.csv;

import com.infinitechnic.parser.csv.event.TransformCompletedHandler;
import com.infinitechnic.parser.csv.exception.IllegalOperationException;

import java.io.*;

public class CsvReader<T> {
	private final static String DEFAULT_DELIMITER = ",";

	private InputStream is;
	private int lineStartFrom;
	private CsvDocument csvDocument;

	public CsvReader(final String filename, final InputStream is, boolean hasHeader, String delimiter, Class<T> modelType) {
		super();
		this.is = is;
		this.lineStartFrom = 1;	// Start from first line of CSV file
		this.csvDocument = new CsvDocument(filename, delimiter, hasHeader, modelType);
	}

	public CsvReader(final String filename, final InputStream is, boolean hasHeader, String delimiter) {
		this(filename, is, hasHeader, delimiter, (Class<T>)Line.class);
	}

	public CsvReader(final String filename, final InputStream is, boolean hasHeader, Class<T> modelType) {
		this(filename, is, hasHeader, DEFAULT_DELIMITER, modelType);
	}

	public CsvReader(final String filename, final InputStream is, boolean hasHeader) {
		this(filename, is, hasHeader, (Class<T>)Line.class);
	}

	public CsvReader(final String filename, final InputStream is, Class<T> modelType) {
		this(filename, is, true, modelType);
	}

	public CsvReader(final String filename, final InputStream is) {
		this(filename, is, (Class<T>)Line.class);
	}

	public CsvReader(final File csvFile, boolean hasHeader, String delimiter, Class<T> modelType) {
		this(csvFile.getName(), null, hasHeader, delimiter, modelType);
		try {
			this.is = new FileInputStream(csvFile);
		} catch (FileNotFoundException fnfe) {
			throw new RuntimeException("CSV file can't be found!", fnfe);
		}
	}

	public CsvReader(final File csvFile, boolean hasHeader, String delimiter) {
		this(csvFile, hasHeader, delimiter, (Class<T>)Line.class);
	}

	public CsvReader(final File csvFile, boolean hasHeader, Class<T> modelType) throws FileNotFoundException {
		this(csvFile, hasHeader, DEFAULT_DELIMITER, modelType);
	}

	public CsvReader(final File csvFile, boolean hasHeader) throws FileNotFoundException {
		this(csvFile, hasHeader, (Class<T>)Line.class);
	}

	public CsvReader(final File csvFile, Class<T> modelType) throws FileNotFoundException {
		this(csvFile, true, modelType);
	}

	public CsvReader(final File csvFile) throws FileNotFoundException {
		this(csvFile, (Class<T>)Line.class);
	}

	public int getLineStartFrom() {
		return lineStartFrom;
	}

	public void setLineStartFrom(int lineStartFrom) {
		this.lineStartFrom = lineStartFrom;
	}

	public void add(TransformCompletedHandler<T> handler) {
		csvDocument.add(handler);
	}

	public CsvDocument read() {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
			csvDocument.clear();
			String lineStr = "";
			int lineNo = 1;
			while ((lineStr = br.readLine()) != null) {
				if (lineNo >= lineStartFrom) {
					if (csvDocument.isHasHeader() && csvDocument.getHeaders() == null) {
						csvDocument.readHeader(lineStr);
					} else {
						csvDocument.readLine(lineStr);
					}
				}
				lineNo++;
			}
		} catch (Exception e) {
			throw new IllegalOperationException("Fail to read input file", e);
		}
		return csvDocument;
	}
}
