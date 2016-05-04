package com.infinitechnic.parser.csv;


import com.infinitechnic.parser.csv.event.TransformCompletedHandler;
import com.infinitechnic.parser.csv.exception.IllegalOperationException;
import com.infinitechnic.parser.csv.mapping.CsvCell;
import com.infinitechnic.parser.csv.mapping.CsvIgnore;
import com.infinitechnic.parser.csv.mapping.CsvMapping;
import com.infinitechnic.util.ReflectionUtil;
import com.infinitechnic.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.*;

public class CsvDocument<T> {
	private final static String REGEXP = "(?=([^\"]*\"[^\"]*\")*[^\"]*$)";
	private final static String DOUBLE_QUOTE = "\"";

	private Logger logger = LoggerFactory.getLogger(CsvDocument.class);

	private String filename;
	private boolean hasHeader;
	private Class<T> modelType;
	private boolean lineModel;

	private String regExpWithDelimiter;
	private List<String> headers;
	private List<T> models;
	private Map<String, Field> fieldMap;
	private List<TransformCompletedHandler> tcHandlers;

	public CsvDocument(String filename, String delimiter, boolean hasHeader, Class<T> modelType) {
		super();
		this.filename = filename;
		this.hasHeader = hasHeader;
		this.modelType = modelType;
		this.lineModel = Line.class.equals(modelType);

		this.headers = null;
		this.models = new ArrayList<>();
		this.tcHandlers = new ArrayList<>();

		setDelimiter(delimiter);

		if (!Line.class.equals(modelType) && !hasHeader) {
			throw new IllegalOperationException("HasHeader should be set to true!");
		}
	}
	
	public CsvDocument(String filename, String delimiter, Class<T> mappingModel) {
		this(filename, delimiter, true, mappingModel);
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

	public List<String> getHeaders() {
		return headers == null ? null : Collections.unmodifiableList(headers);
	}

	public int getNoOfLines() {
		return models.size();
	}

	public List<T> getLines() {
		return models;
	}

	public Iterator<T> iterator() {
		return new ModelIterator<T>(models);
	}

	protected void add(TransformCompletedHandler<T> handler) {
		if (handler != null) {
			tcHandlers.add(handler);
		}
	}

	protected void readHeader(String headerStr) {
		headers = toHeaders(headerStr);
		// If store Line model, doesn't need to get model field mapping
		if (fieldMap == null && !lineModel) {
			fieldMap = getFieldMap();
		}
	}

	protected Map<String, Field> getFieldMap() {
		Map<String, Field> classFieldMap;
		Set<String> csvFieldSet;

		CsvMapping csvMapping = modelType.getAnnotation(CsvMapping.class);
		if (csvMapping != null) {    // CsvMapping annotated class
			classFieldMap = getClassFieldMap(true, csvMapping.caseSensitive());
			csvFieldSet = getCsvFieldSet(csvMapping.caseSensitive());
		} else {	// Handle any object by convention
			classFieldMap = getClassFieldMap(false, true);
			csvFieldSet = getCsvFieldSet(true);
		}

		// Detect missing mapping fields
		Set<String> missingFieldSet = new HashSet<>();
		classFieldMap.keySet().stream().forEach(k -> {
			if (csvFieldSet.contains(k)) {
				csvFieldSet.remove(k);
			} else {
				missingFieldSet.add(k);
			}
		});
		log("CSV", csvFieldSet);
		log("Class", missingFieldSet);
		if (csvMapping != null && csvMapping.identical() && (csvFieldSet.size() > 0 || missingFieldSet.size() > 0)) {
			throw new IllegalOperationException("CSV & Class field mappings are not identical");
		}

		return classFieldMap;
	}

	protected void readLine(String lineStr) throws InstantiationException, IllegalAccessException {
		Line line = new Line(headers, toCells(lineStr));
		//TODO: Warning message can be logged if no. of cells is diff from no. of headers
		if (lineModel) {
			models.add((T)line);
		} else {
			T object = line.transform(modelType.newInstance(), fieldMap);
			transformCompleted(object);
			models.add(object);
		}
	}

	protected void clear() {
		headers = null;
		models.clear();
	}

	/*
	 * Support " " as delimiter
	 */
	private void setDelimiter(String delimiter) {
		if (delimiter == null || delimiter.length() == 0) {
			throw new IllegalArgumentException(StringUtil.concat("Invalid delimiter [\"", delimiter, "\"]"));
		}
		regExpWithDelimiter = delimiter + REGEXP;
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
		int noOfHeaders = (hasHeader && headers!= null) ? headers.size() : 0;
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

	private Set<String> getCsvFieldSet(boolean caseSensitive) {
		Set<String> fieldSet = new HashSet<>();
		if (headers != null) {
			headers.stream().forEach(h -> {
				//TODO: throw exception if exist already
				fieldSet.add(caseSensitive ? h : h.toLowerCase());
			});
		}
		return fieldSet;
	}

	private Map<String, Field> getClassFieldMap(boolean csvMapping, boolean caseSensitive) {
		Map<String, Field> fieldMap = new HashMap<>();
		ReflectionUtil.getAllFields(modelType).stream().forEach(f -> {
			CsvCell csvCell;
			String fieldName;
			if (csvMapping && (csvCell = f.getAnnotation(CsvCell.class)) != null) {
				fieldName = StringUtil.isEmpty(csvCell.name()) ? f.getName() : csvCell.name();
				if (!caseSensitive) {
					fieldName = fieldName.toLowerCase();
				}
				fieldMap.put(fieldName, f);
			} else if (f.getAnnotation(CsvIgnore.class) == null) {
				fieldName = caseSensitive ? f.getName() : f.getName().toLowerCase();
				fieldMap.put(fieldName, f);
			}
		});
		return fieldMap;
	}

	private void transformCompleted(T object) {
		tcHandlers.stream().forEach(h -> {
			h.handle(object);
		});
	}

	private void log(String type, Set<String> fields) {
		if (fields != null && fields.size() > 0) {
			StringBuilder sb = new StringBuilder();
			sb.append("Field mappings for ").append(type).append(" are missing. [");
			sb.append(StringUtil.concatWithDelimiter(fields, ", "));
			sb.append("]");
			logger.info(sb.toString());
		}
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
