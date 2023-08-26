package iob.logic.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.GONE)
public class DeprecatedMethodException extends RuntimeException {
	private static final long serialVersionUID = -8175961059528243329L;

	public DeprecatedMethodException() {
		
	}

	public DeprecatedMethodException(String message) {
		super(message);
	}

	public DeprecatedMethodException(Throwable cause) {
		super(cause);
	}

	public DeprecatedMethodException(String message, Throwable cause) {
		super(message, cause);
	}

}
