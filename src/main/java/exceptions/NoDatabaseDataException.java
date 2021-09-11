package exceptions;

public class NoDatabaseDataException extends Exception{
    public NoDatabaseDataException() {
        super("Variables DB_LOGIN or DB_PASSWORD are not matched.");
    }
}
