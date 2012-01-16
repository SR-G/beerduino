/*
 * 
 */
package org.tensin.beerduino;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.tensin.common.tools.documentation.updater.Description;

/**
 * The Class Recipient.
 */
@Root(name = "destinataire")
@Description("Mail recipient whom the mail will be sent")
public class Recipient {

    /** The email. */
    @Attribute
    @Description("Mail that will be used for the to: field")
    private String email;

    /** The name. */
    @Attribute(required = false)
    @Description("Name of the recipient")
    private String name;

    /**
     * Instantiates a new recipient.
     */
    public Recipient() {
        super();
    }

    /**
     * Instantiates a new recipient.
     * 
     * @param email
     *            the email
     * @param name
     *            the name
     */
    public Recipient(final String email, final String name) {
        super();
        this.email = email;
        this.name = name;
    }

    /**
     * Gets the email.
     * 
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets the name.
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the email.
     * 
     * @param email
     *            the new email
     */
    public void setEmail(final String email) {
        this.email = email;
    }

    /**
     * Sets the name.
     * 
     * @param name
     *            the new name
     */
    public void setName(final String name) {
        this.name = name;
    }
}