package com.infinitechnic.parser.csv;

import com.infinitechnic.parser.csv.exception.InvalidDataFormatException;
import com.infinitechnic.parser.csv.mapping.CsvCell;
import com.infinitechnic.util.DateUtil;
import com.infinitechnic.util.ReflectionUtil;
import com.infinitechnic.util.StringUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class Line {
	private List<String> headers;
	private List<Cell> cells;
	private int noOfCells;

	public Line(List<String> headers, List<Cell> cells) {
		super();
		this.headers = headers;
		this.cells = cells;
		this.noOfCells = cells == null ? 0 : cells.size();
	}

	public List<Cell> getCells() {
		return cells;
	}

	public Cell getCell(String header) {
		if (headers != null && !StringUtil.isEmpty(header)) {
			int idx = headers.indexOf(header);
			if (idx > -1 && idx < noOfCells) {
				return cells.get(idx);
			}
		}
		return null;
	}

	public int getNoOfCells() {
		return noOfCells;
	}

	protected <E> E transform(E object, Map<String, Field> fieldMap) throws IllegalAccessException, InstantiationException {
		if (object != null && fieldMap != null) {
			fieldMap.entrySet().stream().forEach(e -> {
				Cell cell = getCell(e.getKey());
				if (cell != null) {
					setValue(cell, object, e.getValue());
				}
			});
			return object;
		}
		return null;
	}

	//TODO: any special types?
	private void setValue(Cell cell, Object instance, Field field) {
		try {
			if (Date.class.isAssignableFrom(field.getType())) {
				CsvCell csvCell = field.getAnnotation(CsvCell.class);
				if (csvCell != null) {
					ReflectionUtil.setValue(instance, field, DateUtil.parseDate(cell.toString(), csvCell.format()));
				}
			} else {
				ReflectionUtil.setValue(instance, field, cell.toString());
			}
		} catch (Exception e) {
			throw new InvalidDataFormatException(StringUtil.concat("Cannot set on field[", field.getName(), "], value[", cell.toString(), "]"), e);
		}
	}

	@Override
	public String toString() {
		String delimiter = "|";
		StringBuilder sb = new StringBuilder();
		if (cells != null) {
			cells.stream().forEach(c -> {
				if (sb.length() > 0) {
					sb.append(delimiter);
				}
				sb.append(c);
			});
		}
		return sb.toString();
	}
}