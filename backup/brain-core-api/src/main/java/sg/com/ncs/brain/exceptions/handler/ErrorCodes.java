package sg.com.ncs.brain.exceptions.handler;

public enum ErrorCodes {
	BUSINESS_ERROR(100), INTERNAL_SERVER_ERROR(502), NETWORK_ERROR(1000), NOT_FOUND(500), BAD_REQUEST(400),
	UNAUTHORIZED(401), IMPROPER_REQUEST(707), UNSUCESSFULL(404);
	private int errorCode;

	private ErrorCodes(int errorCode) {
		this.errorCode = errorCode;
	}

	public int getErrorCode() {
		return errorCode;
	}
}