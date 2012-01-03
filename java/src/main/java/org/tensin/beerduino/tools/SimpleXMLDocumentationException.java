package org.tensin.beerduino.tools;

import org.tensin.beerduino.CoreException;

/**
 * The Class SimpleXMLDocumentationException.
 */
public class SimpleXMLDocumentationException extends CoreException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new simple xml documentation exception.
	 *
	 * @param e the e
	 */
	public SimpleXMLDocumentationException(Exception e) {
		super(e);
	}

	/**
	 * Instantiates a new simple xml documentation exception.
	 *
	 * @param msg the msg
	 * @param e the e
	 */
	public SimpleXMLDocumentationException(String msg, Exception e) {
		super(msg, e);
	}

	/**
	 * Instantiates a new simple xml documentation exception.
	 *
	 * @param msg the msg
	 * @param t the t
	 */
	public SimpleXMLDocumentationException(String msg, Throwable t) {
		super(msg, t);
	}

	/**
	 * Instantiates a new simple xml documentation exception.
	 *
	 * @param msg the msg
	 */
	public SimpleXMLDocumentationException(String msg) {
		super(msg);
	}

	/**
	 * Instantiates a new simple xml documentation exception.
	 *
	 * @param t the t
	 */
	public SimpleXMLDocumentationException(Throwable t) {
		super(t);
	}

}
