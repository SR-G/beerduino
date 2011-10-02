package org.tensin.beerduino;

/**
 * @author u248663
 * @version $Revision: 1.1 $
 * @since 20 juil. 2011 23:13:25
 * 
 */
public class TemplatedGraph {

    private String url;
    private String cid;
    private String width;
    private String height;
    private String label;

    /**
     * Constructor.
     */
    public TemplatedGraph() {
        super();
    }

    /**
     * Getter for the attribute cid.
     * @return Returns the attribute cid.
     */
    public String getCid() {
        return cid;
    }

    /**
     * Getter for the attribute height.
     * @return Returns the attribute height.
     */
    public String getHeight() {
        return height;
    }

    /**
     * Getter for the attribute label.
     * @return Returns the attribute label.
     */
    public String getLabel() {
        return label;
    }

    /**
     * Getter for the attribute url.
     * @return Returns the attribute url.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Getter for the attribute width.
     * @return Returns the attribute width.
     */
    public String getWidth() {
        return width;
    }

    /**
     * Setter for the attribute cid.
     * @param cid The attribute cid.
     */
    public void setCid(final String cid) {
        this.cid = cid;
    }

    /**
     * Setter for the attribute height.
     * @param height The attribute height.
     */
    public void setHeight(final String height) {
        this.height = height;
    }

    /**
     * Setter for the attribute label.
     * @param label The attribute label.
     */
    public void setLabel(final String label) {
        this.label = label;
    }

    /**
     * Setter for the attribute url.
     * @param url The attribute url.
     */
    public void setUrl(final String url) {
        this.url = url;
    }

    /**
     * Setter for the attribute width.
     * @param width The attribute width.
     */
    public void setWidth(final String width) {
        this.width = width;
    }

}