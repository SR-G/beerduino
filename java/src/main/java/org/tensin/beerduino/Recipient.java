package org.tensin.beerduino;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "destinataire")
public class Recipient {

    @Attribute
    private String email;

    @Attribute
    private String name;

    public Recipient() {
        super();
    }

    public Recipient(final String email, final String name) {
        super();
        this.email = email;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public void setName(final String name) {
        this.name = name;
    }
}