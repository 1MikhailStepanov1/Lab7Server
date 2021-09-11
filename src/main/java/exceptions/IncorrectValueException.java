package exceptions;

/**
 * Is threw when field has incorrect value
 */
public class IncorrectValueException extends Exception {
    public IncorrectValueException(String FieldName, String Message) {
        super(FieldName + "`s value is incorrect. " + Message);
    }
}
