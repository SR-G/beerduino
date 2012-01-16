package org.tensin.common.tools.documentation.updater;

public interface ISyntaxer {

    String buildCommentEnd(final String comment);

    String buildCommentStart(final String comment);

    String buildFileHeader(final String title, final String author);

    String buildLink(final String anchor);

    String buildLink(final String text, final String anchor);

    String buildQuoteEnd();

    String buildQuoteStart();

    String buildTableFooter();

    String buildTableHeader(final int columnCount);

    String buildTableHeader(final String[] values);

    String buildTableRow(final String[] values);

    String buildTableRowApt(final int columnCount);

    String buildUnorderedItem(final String value);

    String fontBold(final String value);

    String fontItalic(final String value);

}
