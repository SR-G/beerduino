package org.tensin.common.tools.documentation.updater;

/**
 * The Class SyntaxGitHubHelper.
 * 
 * @author sergio
 */
public class SyntaxGitHubHelper {

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
    public static String buildLink(final String text, String anchor) {
        return "[" + text + "](#" + anchor + ")";
    }

}
