package com.infinitechnic.parser.csv.exception;

public class InvalidDataFormatException extends RuntimeException {
	public InvalidDataFormatException(String message) {
		super(message);
	}

	public InvalidDataFormatException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidDataFormatException(Throwable cause) {
		super(cause);
	}
}
