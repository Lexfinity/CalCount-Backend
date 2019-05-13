package ca.development.calcount.exception;

/*
 * Class used to handle unexisting objects (null)
 *
 * */

public class NullObjectException extends Exception {

    /**
    * Constructor
    * @param errorMessage
    */
    public NullObjectException(String errorMessage) {
        super(errorMessage);
    }

}
