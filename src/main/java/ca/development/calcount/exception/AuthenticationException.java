package ca.development.calcount.exception;

/*
* Class used to handle login/authentication exceptions
*
* */

public class AuthenticationException extends Exception {

	/**
	 * Constructor
	 * @param errorMessage
	 */
	public AuthenticationException(String errorMessage) {
		super(errorMessage);
	}

}