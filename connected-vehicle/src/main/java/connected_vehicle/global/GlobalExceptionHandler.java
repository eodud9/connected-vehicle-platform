package connected_vehicle.global;

import connected_vehicle.exception.*;
import connected_vehicle.global.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException e){
        FieldError error = e.getBindingResult().getFieldError();

        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "INVALID_INPUT", error.getField() + ": " + error.getDefaultMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentials(InvalidCredentialsException e){
        ErrorResponse response = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), "INVALID_CREDENTIALS", e.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(VehicleAuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleVehicleAuthenticationFailed(VehicleAuthenticationException e){

        ErrorResponse response = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), "INVALID_VEHICLE_CREDENTIALS", e.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);

    }

    @ExceptionHandler(VehicleAccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleVehicleAccessDenied(VehicleAccessDeniedException e){
        ErrorResponse response = new ErrorResponse(HttpStatus.FORBIDDEN.value(), "VEHICLE_ACCESS_DENIED", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException e){
        ErrorResponse response = new ErrorResponse(HttpStatus.NOT_FOUND.value(), "USER_NOT_FOUND", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(VehicleNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleVehicleNotFound(VehicleNotFoundException e){
        ErrorResponse response = new ErrorResponse(HttpStatus.NOT_FOUND.value(), "VEHICLE_NOT_FOUND", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(TelemetryNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTelemetryNotFound(TelemetryNotFoundException e){
        ErrorResponse response = new ErrorResponse(HttpStatus.NOT_FOUND.value(), "TELEMETRY_NOT_FOUND", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(DrivingSessionNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleDrivingSessionNotFound(DrivingSessionNotFoundException e){
        ErrorResponse response = new ErrorResponse(HttpStatus.NOT_FOUND.value(), "DRIVING_SESSION_NOT_FOUND", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(DuplicateVinException.class)
    public ResponseEntity<ErrorResponse> handleVinDuplicate(DuplicateVinException e){
        ErrorResponse response = new ErrorResponse(HttpStatus.CONFLICT.value(), "DUPLICATE_VIN", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(DuplicateLicensePlateException.class)
    public ResponseEntity<ErrorResponse> handleLicensePlateDuplicate(DuplicateLicensePlateException e){
        ErrorResponse response = new ErrorResponse(HttpStatus.CONFLICT.value(), "DUPLICATE_LICENSE_PLATE", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ErrorResponse> handleEmailDuplicate(DuplicateEmailException e){
        ErrorResponse response = new ErrorResponse(HttpStatus.CONFLICT.value(), "DUPLICATE_EMAIL", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(ActiveOwnershipAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleVehicleDuplicate(ActiveOwnershipAlreadyExistsException e){
        ErrorResponse response = new ErrorResponse(HttpStatus.CONFLICT.value(), "DUPLICATE_VEHICLE", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

}
