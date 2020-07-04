package sg.com.ncs.brain.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class SuccessHandler {

	public ResponseEntity<Map<String, String>> success(String message) {
		Map<String, String> response = new HashMap<String, String>();
		response.put("ok", message);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	public ResponseEntity<Map<String, String>> success() {
		Map<String, String> response = new HashMap<String, String>();
		response.put("ok", "Action SuccessFull");
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

}
