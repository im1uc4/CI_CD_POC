package sg.com.ncs.common.exceptions;

public class NoDataFoundException extends RuntimeException {

	private static final long serialVersionUID = 764928470726412351L;

	public NoDataFoundException(String cause) {
		super(cause);
	}
}
