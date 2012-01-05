package org.tensin.common;

/**
 * The Class CoreException.
 */
public class CoreException extends Exception {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new core exception.
     * 
     * @param e
     *            the e
     */
    public CoreException(final Exception e) {
        super(e);
    }

    /**
     * Instantiates a new core exception.
     * 
     * @param msg
     *            the msg
     */
    public CoreException(final String msg) {
        super(msg);
    }

    /**
     * Instantiates a new core exception.
     * 
     * @param msg
     *            the msg
     * @param e
     *            the e
     */
    public CoreException(final String msg, final Exception e) {
        super(msg, e);
    }

    /**
     * Instantiates a new core exception.
     * 
     * @param msg
     *            the msg
     * @param t
     *            the t
     */
    public CoreException(final String msg, final Throwable t) {
        super(msg, t);
    }

    /**
     * Instantiates a new core exception.
     * 
     * @param t
     *            the t
     */
    public CoreException(final Throwable t) {
        super(t);
    }
}
