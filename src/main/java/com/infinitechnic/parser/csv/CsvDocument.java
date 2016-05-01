package com.infinitechnic.parser.csv;


import com.infinitechnic.parser.csv.mapping.validation.CsvMappingRule;
import com.infinitechnic.parser.csv.mapping.validation.FieldMappingRule;
import com.infinitechnic.parser.csv.mapping.validation.HasHeaderRule;

import java.util.*;

public class CsvDocument {
	private String filename;
	private boolean hasHeader;
	private List<String> headers;
	private List<Line> lines;

	public CsvDocument(String filename, boolean hasHeader) {
		super();
		this.filename = filename;
		this.hasHeader = hasHeader;
		this.headers = null;
		this.lines = new ArrayList<Line>();
	}
	
	public CsvDocument(String filename) {
		this(filename, true);
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

	public <C> List<C> transform(Class<C> clazz) {
		CsvMappingRule rule = new CsvMappingRule(this, clazz);
		rule.addChain(new HasHeaderRule(this, clazz))
				.addChain(new FieldMappingRule(this, clazz));
		rule.validate();

		//TODO: perform transformation
		List<C> objects = new ArrayList<>(lines.size());
		for (Line line : lines) {	//TODO: see how to use stream
//			objects.add(line.transform())
		}
		return null;
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
