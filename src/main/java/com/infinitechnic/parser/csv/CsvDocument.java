package com.infinitechnic.parser.csv;


import com.infinitechnic.parser.csv.mapping.validation.CsvMappingRule;
import com.infinitechnic.parser.csv.mapping.validation.FieldMappingRule;
import com.infinitechnic.parser.csv.mapping.validation.HasHeaderRule;

import java.util.*;

public class CsvDocument<T> {
	private String filename;
	private boolean hasHeader;
	private Class<T> mappingModel;

	private List<String> headers;
	private List<Line> lines;
	private List<T> models;

	public CsvDocument(String filename, boolean hasHeader, Class<T> mappingModel) {
		super();
		this.filename = filename;
		this.hasHeader = hasHeader;
		this.mappingModel = mappingModel;
		this.headers = null;
		this.lines = new ArrayList<Line>();
		this.models = new ArrayList<>();

		if (!Line.class.equals(mappingModel)) {
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

	public void setHasHeader(boolean hasHeader) {
		this.hasHeader = hasHeader;
	}

	public List<String> getHeaders() {
		return headers == null ? null : Collections.unmodifiableList(headers);
	}

	protected void setHeaders(List<String> headers) {
		this.headers = headers;
	}

	protected void addLine(Line line) {
		line.setHeaders(headers);
		lines.add(line);
	}

	protected void clear() {
		headers = null;
		lines.clear();
	}

	private void validate() {
		CsvMappingRule rule = new CsvMappingRule(this, mappingModel);
		rule.addChain(new HasHeaderRule(this, mappingModel))
				.addChain(new FieldMappingRule(this, mappingModel));
		rule.validate();
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

}
