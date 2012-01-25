package org.tensin.common.tools.documentation.updater;

/**
 * The Interface ISyntaxer.
 */
public interface ISyntaxer {

	/**
	 * Builds the comment end.
	 * 
	 * @param comment
	 *            the comment
	 * @return the string
	 */
	String buildCommentEnd(final String comment);

	/**
	 * Builds the comment start.
	 * 
	 * @param comment
	 *            the comment
	 * @return the string
	 */
	String buildCommentStart(final String comment);

	/**
	 * Builds the file header.
	 * 
	 * @param title
	 *            the title
	 * @param author
	 *            the author
	 * @return the string
	 */
	String buildFileHeader(final String title, final String author);

	String buildHeader(final int level, final String value);

	String buildHTMLLink(String baliseName, String anchor);

	/**
	 * Builds the link.
	 * 
	 * @param anchor
	 *            the anchor
	 * @return the string
	 */
	String buildLink(final String anchor);

	/**
	 * Builds the link.
	 * 
	 * @param text
	 *            the text
	 * @param anchor
	 *            the anchor
	 * @return the string
	 */
	String buildLink(final String text, final String anchor);

	/**
	 * Builds the quote end.
	 * 
	 * @return the string
	 */
	String buildQuoteEnd();

	/**
	 * Builds the quote start.
	 * 
	 * @return the string
	 */
	String buildQuoteStart();

	/**
	 * Builds the table footer.
	 * 
	 * @return the string
	 */
	String buildTableFooter();

	/**
	 * Builds the table header.
	 * 
	 * @param columnCount
	 *            the column count
	 * @return the string
	 */
	String buildTableHeader(final int columnCount);

	/**
	 * Builds the table header.
	 * 
	 * @param values
	 *            the values
	 * @return the string
	 */
	String buildTableHeader(final String[] values);

	/**
	 * Builds the table row.
	 * 
	 * @param values
	 *            the values
	 * @return the string
	 */
	String buildTableRow(final String[] values);

	/**
	 * Builds the table row apt.
	 * 
	 * @param columnCount
	 *            the column count
	 * @return the string
	 */
	String buildTableRowApt(final int columnCount);

	/**
	 * Builds the unordered item.
	 * 
	 * @param value
	 *            the value
	 * @return the string
	 */
	String buildUnorderedItem(final String value);

	/**
	 * Font bold.
	 * 
	 * @param value
	 *            the value
	 * @return the string
	 */
	String fontBold(final String value);

	/**
	 * Font italic.
	 * 
	 * @param value
	 *            the value
	 * @return the string
	 */
	String fontItalic(final String value);

}
