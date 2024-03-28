package matchingGoal.matchingGoal.common.exception;

import lombok.extern.slf4j.Slf4j;
import matchingGoal.matchingGoal.common.type.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler  {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        return ErrorResponse.toResponseEntity(e.getErrorCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException e) {
        Map<String, String> error = new HashMap<>();
        e.getBindingResult().getAllErrors()
                .forEach(c -> error.put( ((FieldError) c).getField(), c.getDefaultMessage()));
        ErrorCode code = ErrorCode.INVALID_ARGUMENT;
        return ResponseEntity.badRequest().body(ErrorResponse.builder()
                .status(code.getStatus().value())
                .error(code.getStatus().name())
                .code(code.toString())
                .errors(error)
                .build());
    }

    // 파일 용량 초과시
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    protected ResponseEntity<ErrorResponse> handleMaxUploadSizeExceededException(
            MaxUploadSizeExceededException e) {
        log.info("handleMaxUploadSizeExceededException", e);

        ErrorCode code = ErrorCode.FILE_SIZE_EXCEED;
        return ResponseEntity.badRequest().body(ErrorResponse.builder()
                .status(code.getStatus().value())
                .error(code.getStatus().name())
                .code(code.toString())
                .message(code.getDescription())
                .build());
    }
}
