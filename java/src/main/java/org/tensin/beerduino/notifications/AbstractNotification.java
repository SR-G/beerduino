package org.tensin.beerduino.notifications;

import org.simpleframework.xml.Attribute;

/**
 * The Class AbstractNotification.
 */
public abstract class AbstractNotification {

	/** The id. */
	@Attribute(required = false)
	private String id;

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(final String id) {
		this.id = id;
	}

}
