package com.infinitechnic.parser.csv;

import com.infinitechnic.parser.csv.exception.IllegalOperationException;
import com.infinitechnic.util.StringUtil;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

public class CsvReader {
	private final static String DEFAULT_DELIMITER = ",";

	private FileInputStream fis;
	private CsvDocument csvDocument;

	public CsvReader(final String filename, final FileInputStream fis, boolean hasHeader, String delimiter, Class<?> modelType) {
		super();
		this.fis = fis;
		this.csvDocument = new CsvDocument(filename, delimiter, hasHeader, modelType);
	}

	public CsvReader(final String filename, final FileInputStream fis, boolean hasHeader, String delimiter) {
		this(filename, fis, hasHeader, delimiter, Line.class);
	}

	public CsvReader(final String filename, final FileInputStream fis, boolean hasHeader, Class<?> modelType) {
		this(filename, fis, hasHeader, DEFAULT_DELIMITER, modelType);
	}

	public CsvReader(final String filename, final FileInputStream fis, boolean hasHeader) {
		this(filename, fis, hasHeader, Line.class);
	}

	public CsvReader(final String filename, final FileInputStream fis, Class<?> modelType) {
		this(filename, fis, true, modelType);
	}

	public CsvReader(final String filename, final FileInputStream fis) {
		this(filename, fis, Line.class);
	}

	public CsvReader(final File csvFile, boolean hasHeader, String delimiter, Class<?> modelType) {
		this(csvFile.getName(), null, hasHeader, delimiter, modelType);
		try {
			this.fis = new FileInputStream(csvFile);
		} catch (FileNotFoundException fnfe) {
			throw new RuntimeException("CSV file can't be found!", fnfe);
		}
	}

	public CsvReader(final File csvFile, boolean hasHeader, String delimiter) {
		this(csvFile, hasHeader, delimiter, Line.class);
	}

	public CsvReader(final File csvFile, boolean hasHeader, Class<?> modelType) throws FileNotFoundException {
		this(csvFile, hasHeader, DEFAULT_DELIMITER, modelType);
	}

	public CsvReader(final File csvFile, boolean hasHeader) throws FileNotFoundException {
		this(csvFile, hasHeader, Line.class);
	}

	public CsvReader(final File csvFile, Class<?> modelType) throws FileNotFoundException {
		this(csvFile, true, modelType);
	}

	public CsvReader(final File csvFile) throws FileNotFoundException {
		this(csvFile, Line.class);
	}

	public CsvDocument read() {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(fis))) {
			csvDocument.clear();
			String lineStr = "";
			while ((lineStr = br.readLine()) != null) {
				if (csvDocument.isHasHeader() && csvDocument.getHeaders() == null) {
					csvDocument.readHeader(lineStr);
				} else {
					csvDocument.readLine(lineStr);
				}
			}
		} catch (Exception e) {
			throw new IllegalOperationException("Fail to read input file", e);
		}
		return csvDocument;
	}
}
