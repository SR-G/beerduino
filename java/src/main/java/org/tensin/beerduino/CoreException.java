package org.tensin.beerduino;

public class CoreException extends Exception {

	public CoreException(final Exception e) {
		super(e);
	}

    public CoreException(final Throwable t) {
        super(t);
    }

	public CoreException(final String msg) {
	    super(msg);
    }

    public CoreException(final String msg, final Exception e) {
        super(msg, e);
    }

    public CoreException(final String msg, final Throwable t) {
        super(msg, t);
    }

    private static final long serialVersionUID = 1L;
}
