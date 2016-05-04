package com.infinitechnic.parser.csv;

import com.infinitechnic.parser.csv.event.TransformCompletedHandler;
import com.infinitechnic.parser.csv.exception.IllegalOperationException;
import com.infinitechnic.util.StringUtil;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

public class CsvReader<T> {
	private final static String DEFAULT_DELIMITER = ",";

	private FileInputStream fis;
	private CsvDocument csvDocument;

	public CsvReader(final String filename, final FileInputStream fis, boolean hasHeader, String delimiter, Class<T> modelType) {
		super();
		this.fis = fis;
		this.csvDocument = new CsvDocument(filename, delimiter, hasHeader, modelType);
	}

	public CsvReader(final String filename, final FileInputStream fis, boolean hasHeader, String delimiter) {
		this(filename, fis, hasHeader, delimiter, (Class<T>)Line.class);
	}

	public CsvReader(final String filename, final FileInputStream fis, boolean hasHeader, Class<T> modelType) {
		this(filename, fis, hasHeader, DEFAULT_DELIMITER, modelType);
	}

	public CsvReader(final String filename, final FileInputStream fis, boolean hasHeader) {
		this(filename, fis, hasHeader, (Class<T>)Line.class);
	}

	public CsvReader(final String filename, final FileInputStream fis, Class<T> modelType) {
		this(filename, fis, true, modelType);
	}

	public CsvReader(final String filename, final FileInputStream fis) {
		this(filename, fis, (Class<T>)Line.class);
	}

	public CsvReader(final File csvFile, boolean hasHeader, String delimiter, Class<T> modelType) {
		this(csvFile.getName(), null, hasHeader, delimiter, modelType);
		try {
			this.fis = new FileInputStream(csvFile);
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

	public void add(TransformCompletedHandler<T> handler) {
		csvDocument.add(handler);
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
