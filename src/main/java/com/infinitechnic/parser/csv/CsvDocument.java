package com.infinitechnic.parser.csv;


import com.infinitechnic.parser.csv.mapping.validation.CsvMappingRule;
import com.infinitechnic.parser.csv.mapping.validation.FieldMappingRule;
import com.infinitechnic.parser.csv.mapping.validation.HasHeaderRule;

import java.lang.reflect.Field;
import java.util.*;

public class CsvDocument<T> {
	private String filename;
	private boolean hasHeader;
	private Class<T> modelType;

	private List<String> headers;
//	private List<Line> lines;
	private List<T> models;

	public CsvDocument(String filename, boolean hasHeader, Class<T> modelType) {
		super();
		this.filename = filename;
		this.hasHeader = hasHeader;
		this.modelType = modelType;
		this.headers = null;
//		this.lines = new ArrayList<Line>();
		this.models = new ArrayList<>();

		if (!Line.class.equals(modelType)) {
			validate();
		}
	}
	
	public CsvDocument(String filename, Class<T> mappingModel) {
		this(filename, true, mappingModel);
	}

	public String getFilename() {
		return filename;
	}
	
	public String getFilenameWithoutExt() {
		return filename.substring(0, filename.lastIndexOf("."));
	}

	public boolean isHasHeader() {
		return hasHeader;
	}

	protected void setHasHeader(boolean hasHeader) {
		this.hasHeader = hasHeader;
	}

	public Class<T> getModelType() {
		return modelType;
	}

	public List<String> getHeaders() {
		return headers == null ? null : Collections.unmodifiableList(headers);
	}

	protected void setHeaders(List<String> headers) {
		this.headers = headers;
	}

	private void validate() {
		CsvMappingRule rule = new CsvMappingRule(this, modelType);
		rule.addChain(new HasHeaderRule(this, modelType))
				.addChain(new FieldMappingRule(this, modelType));
		rule.validate();
	}

	protected Map<String, Field> getFieldMap() {
		Map<String, Field> fieldMap = new HashMap<>();
		//TODO: get field map logic
		return fieldMap;
	}
/*
	protected void addLine(Line line) {
		line.setHeaders(headers);
		lines.add(line);
	}

	protected void clear() {
		headers = null;
		lines.clear();
	}

	public int getNoOfLines() {
		return lines.size();
	}

	public Iterator<Line> iterator() {
		return new LineIterator(lines);
	}

	private static final class LineIterator implements Iterator<Line> {
		private int cursor;
		private final List<Line> lines;
		private final int end;

		public LineIterator(List<Line> lines) {
			this.cursor = 0;
			this.lines = lines == null ? new ArrayList<Line>() : lines;
			this.end = this.lines.size() - 1;
		}

		@Override
		public boolean hasNext() {
			return cursor <= end;
		}

		@Override
		public Line next() {
			if (this.hasNext()) {
				int current = cursor;
				cursor++;
				return lines.get(current);
			}
			throw new NoSuchElementException();
		}
	}
*/

	protected void addModel(T model) {
		models.add(model);
		if (Line.class.equals(model.getClass())) {
			((Line)model).setHeaders(headers);
		}
	}

	protected void clear() {
		headers = null;
		models.clear();
	}

	public int getNoOfLines() {
		return models.size();
	}

	public Iterator<T> iterator() {
		return new ModelIterator<T>(models);
	}

	private static final class ModelIterator<T> implements Iterator<T> {
		private int cursor;
		private final List<T> models;
		private final int end;

		public ModelIterator(List<T> models) {
			this.cursor = 0;
			this.models = models == null ? new ArrayList<T>() : models;
			this.end = this.models.size() - 1;
		}

		@Override
		public boolean hasNext() {
			return cursor <= end;
		}

		@Override
		public T next() {
			if (this.hasNext()) {
				int current = cursor;
				cursor++;
				return models.get(current);
			}
			throw new NoSuchElementException();
		}
	}
}
