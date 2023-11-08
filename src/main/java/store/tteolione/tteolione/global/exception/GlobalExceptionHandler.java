package store.tteolione.tteolione.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import store.tteolione.tteolione.global.dto.BaseResponse;
import store.tteolione.tteolione.global.dto.Code;
import store.tteolione.tteolione.global.dto.ResponseDto;
import store.tteolione.tteolione.global.exception.GeneralException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GeneralException.class)
    public ResponseDto handleGeneralException(GeneralException ex) {
        return ResponseDto.of(false, ex.getErrorCode(), ex.getMessage());
    }
}
