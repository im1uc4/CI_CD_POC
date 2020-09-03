package sg.com.ncs.common.exceptions;

public class CustomException extends RuntimeException {

	private static final long serialVersionUID = 764928470726412351L;

	public CustomException(String cause) {
		super(cause);
	}
}
