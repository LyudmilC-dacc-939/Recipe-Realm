package Project.Recipe_Realm.advice;

import Project.Recipe_Realm.advice.exception.RecordAlreadyExistsException;
import Project.Recipe_Realm.advice.exception.RecordNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<?> handleOwnerNotFoundException(RecordNotFoundException ex){
        return  new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RecordAlreadyExistsException.class)
    public ResponseEntity<?> handleRecordAlreadyExistsException(RecordAlreadyExistsException recordAlreadyExistsException){
        return new ResponseEntity<>(recordAlreadyExistsException.getMessage(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String,String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach( (error)->{
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName,message);
        });
        return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
    }
}
