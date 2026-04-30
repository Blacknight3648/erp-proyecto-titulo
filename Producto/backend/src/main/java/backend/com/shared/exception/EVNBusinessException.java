package backend.com.shared.exception;

import org.springframework.http.HttpStatus;

public class EVNBusinessException extends BaseException {
    public EVNBusinessException(String message) {
        super(message, "EVN_BUSINESS_ERROR", HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
