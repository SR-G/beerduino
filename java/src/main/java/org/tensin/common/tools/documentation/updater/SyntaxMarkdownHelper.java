package org.tensin.common.tools.documentation.updater;

/**
 * The Class SyntaxMarkdownHelper.
 * Outputs something useable on GitHub.
 * 
 * @author sergio
 */
public class SyntaxMarkdownHelper {

    /**
     * [pookie](#pookie).
     * 
     * @param anchor
     *            the anchor
     * @return the string
     */
    public static String buildLink(final String anchor) {
        return buildLink(anchor, anchor);
    }

    /**
     * Builds the link.
     * 
     * @param text
     *            the text
     * @param anchor
     *            the anchor
     * @return the string
     */
    public static String buildLink(final String text, final String anchor) {
        return "[" + text + "](#" + anchor + ")";
    }

}
