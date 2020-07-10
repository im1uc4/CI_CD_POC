package sg.com.ncs.brain.exceptions.handler;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import sg.com.ncs.common.exceptions.BadRequestException;
import sg.com.ncs.common.exceptions.CustomException;
import sg.com.ncs.common.exceptions.DataException;
import sg.com.ncs.common.exceptions.NoDataFoundException;

@RestControllerAdvice
public class CustomGlobalExceptionHandler {

	Logger logger = LoggerFactory.getLogger(CustomGlobalExceptionHandler.class);

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ApiError> handelError(BadRequestException e) {
		ApiError error = new ApiError();
		List<String> list = new ArrayList<String>();
		list.add(e.getMessage());
		error.setCode(ErrorCodes.BAD_REQUEST.getErrorCode());
		error.setMessage("bad request");
		error.setError(e.getMessage());
		error.setStatus("error");

		return new ResponseEntity<>(error, HttpStatus.OK);
	}

	@ExceptionHandler(DataException.class)
	public ResponseEntity<ApiError> handelError(DataException e) {
		ApiError error = new ApiError();
		error.setCode(ErrorCodes.UNSUCESSFULL.getErrorCode());
		error.setMessage("Improper Request");
		error.setError(e.getMessage());
		error.setStatus("error");

		return new ResponseEntity<>(error, HttpStatus.OK);
	}
	
	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ApiError> handelError(CustomException e) {
		ApiError error = new ApiError();
		error.setCode(ErrorCodes.UNSUCESSFULL.getErrorCode());
		error.setMessage("Improper Request");
		error.setError(e.getMessage());
		error.setStatus("error");

		return new ResponseEntity<>(error, HttpStatus.OK);
	}

	@ExceptionHandler(NoDataFoundException.class)
	public ResponseEntity<ApiError> handelError(NoDataFoundException e) {
		ApiError error = new ApiError();
		error.setCode(ErrorCodes.NOT_FOUND.getErrorCode());
		error.setMessage("No Data Found in Database");
		error.setError(e.getMessage());
		error.setStatus("error");

		return new ResponseEntity<>(error, HttpStatus.OK);
	}
}
