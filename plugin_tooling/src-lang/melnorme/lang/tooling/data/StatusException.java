package melnorme.lang.tooling.data;

import melnorme.utilbox.core.CommonException;

@SuppressWarnings("serial")
public class StatusException extends CommonException {
	
	protected final StatusLevel statusLevel;
	
	public StatusException(StatusLevel statusLevel, String message) {
		this(statusLevel, message, null);
	}
	
	public StatusException(StatusLevel statusLevel, String message, Throwable cause) {
		super(message, cause);
		this.statusLevel = statusLevel;
	}
	
	public StatusLevel getStatusLevel() {
		return statusLevel;
	}
	
	public int getStatusLevelOrdinal() {
		return statusLevel.ordinal();
	}
	
	public boolean isOkStatus() {
		return getStatusLevel() == StatusLevel.OK;
	}
	
}