package sg.com.ncs.common.exceptions;

public class BadRequestException extends RuntimeException {

	private static final long serialVersionUID = 7649285780726412351L;

	public BadRequestException(String cause) {
		super(cause);
	}
}
