package store.tteolione.tteolione.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import store.tteolione.tteolione.global.dto.BaseResponse;
import store.tteolione.tteolione.global.dto.Code;
import store.tteolione.tteolione.global.dto.ResponseDto;
import store.tteolione.tteolione.global.exception.GeneralException;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(GeneralException.class)
    public ResponseDto handleGeneralException(GeneralException ex) {
        return ResponseDto.of(false, ex.getErrorCode(), ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseDto handleValidException(MethodArgumentNotValidException ex) {
        return ResponseDto.of(false, Code.VALIDATION_ERROR, ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseDto handleHttpMethodException(HttpRequestMethodNotSupportedException ex) {
        return ResponseDto.of(false, Code.METHOD_NOT_ALLOWED);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseDto handleHttpMethodException(MissingServletRequestParameterException ex) {
        String parameterName = ex.getParameterName();
        return ResponseDto.of(false, Code.BAD_REQUEST, "필수 파라미터 " + parameterName + "가 누락되었습니다.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseDto handleMissingPathVariableException(MissingPathVariableException ex) {
        String variableName = ex.getVariableName();
        return ResponseDto.of(false, Code.BAD_REQUEST, "필수 경로 변수 '" + variableName + "'가 누락되었습니다.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseDto handleMissingServletRequestPartException(MissingServletRequestPartException ex) {
        String partName = ex.getRequestPartName();
        return ResponseDto.of(false, Code.BAD_REQUEST, "필수 요청 파트 '" + partName + "'가 누락되었습니다.");
    }
}
