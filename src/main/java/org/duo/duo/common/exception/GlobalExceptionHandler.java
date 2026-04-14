package org.duo.duo.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestNotUsableException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(UserException e) {
        log.error("CustomException: {}", e.getMessage());
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(new ErrorResponse(errorCode.name(), errorCode.getMessage()));
    }

    @ExceptionHandler(AsyncRequestNotUsableException.class)
    public void handleDisconnected(AsyncRequestNotUsableException e) {
        log.debug("SSE 클라이언트 연결 끊김 (정상): {}", e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Unexpected error: ", e);
        return ResponseEntity
                .status(500)
                .body(new ErrorResponse("INTERNAL_SERVER_ERROR", "서버 오류가 발생했습니다."));
    }
}