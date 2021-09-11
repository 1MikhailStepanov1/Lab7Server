package exceptions;

/**
 * Is threw when field has null value
 */
public class NullFieldException extends Exception {
    public NullFieldException(String FieldName) {
        super(FieldName + "can`t be null.");
    }
}
