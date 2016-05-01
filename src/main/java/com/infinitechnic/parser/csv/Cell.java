package com.infinitechnic.parser.csv;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.infinitechnic.parser.csv.exception.IllegalOperationException;
import com.infinitechnic.util.NumberUtil;

public class Cell {
	private final static String DELIMITER_ARRAY = ",";
	private final static String PATTERN_UUID = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}|\\{[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}\\}";
	private final static String PATTERN_ARRAY = "^\\{(.*)\\}$";

	private String content;
	
	public Cell(String content) {
		super();
		this.content = content;
	}
	
	public boolean isNumber() {
		return NumberUtil.isNumber(content);
	}
	
	public boolean isInteger() {
		return NumberUtil.isInteger(content);
	}
	
	public boolean isFloat() {
		return NumberUtil.isFloat(content);
	}

	public boolean isUuid() {
		return content.matches(PATTERN_UUID);
	}
	
	public boolean isList() {
		return content.matches(PATTERN_ARRAY);
	}

	public <T> List<T> toList(Class<T> clazz) {
		if (!isList()) {
			throw new IllegalOperationException("The value is not an array type.");
		}

		List<T> list = new ArrayList<T>();
		try {
			Matcher matcher = Pattern.compile(PATTERN_ARRAY).matcher(content);
			if (matcher.matches()) {
				String[] values = matcher.group(1).split(DELIMITER_ARRAY);
				for (String value : values) { 
					list.add(clazz.getConstructor(String.class).newInstance(value.trim()));
				}
			}
		} catch (Exception e) {
			throw new IllegalOperationException("Invalid array type.");
		}
		return list;
	}

	@Override
	public String toString() {
		return content;
	}
}
