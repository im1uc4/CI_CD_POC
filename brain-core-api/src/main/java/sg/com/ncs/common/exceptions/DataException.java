package sg.com.ncs.common.exceptions;

public class DataException extends RuntimeException {

	private static final long serialVersionUID = 764928470726412351L;

	public DataException(String cause) {
		super(cause);
	}

	public DataException(Exception e) {
		super(e.getCause().getCause().getMessage());
	}
}
