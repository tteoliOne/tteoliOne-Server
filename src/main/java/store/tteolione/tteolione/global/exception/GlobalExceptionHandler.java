package store.tteolione.tteolione.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseDto handleValidException(MethodArgumentNotValidException ex) {
        return ResponseDto.of(false, Code.VALIDATION_ERROR, ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }
}
