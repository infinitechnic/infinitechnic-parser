package com.infinitechnic.parser.csv;

import com.infinitechnic.util.StringUtil;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;


public class Line {
	private List<String> headers;
	private List<Cell> cells;
	private int noOfCells;

	public Line(List<Cell> cells) {
		super();
		this.headers = null;
		this.cells = cells;
		this.noOfCells = cells == null ? 0 : cells.size();
	}

	protected void setHeaders(List<String> headers) {
		this.headers = headers;
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

	protected <E> E transform(E mappingObject, Map<String, Field> fieldMap) {
		if (mappingObject != null && fieldMap != null) {
			fieldMap.entrySet().stream().forEach(e -> {
				setValue(getCell(e.getKey()), e.getValue());
			});
		}
		return mappingObject;
	}

	//TODO: define strategy under mapping package
	private void setValue(Cell cell, Field field) {

	}
}