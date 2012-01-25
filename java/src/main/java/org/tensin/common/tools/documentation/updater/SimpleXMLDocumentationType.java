package org.tensin.common.tools.documentation.updater;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * The Class SimpleXMLDocumentationType.
 * 
 * @author j385649
 * @version $Revision: 1.3.2.1 $
 * @since 26 oct. 2011 14:40:52
 */
public class SimpleXMLDocumentationType {

	/** Name. */
	private final String name;

	/** BaliseName. */
	private String baliseName;

	/** indice. */
	private int indice;

	/** description. */
	private String description;

	/** Documentation. */
	private String documentation;

	/** primitive. */
	private final boolean primitive;

	/** enumeration. */
	private List<String> enumeration;

	/**
	 * Constructor.
	 * 
	 * @param clazz
	 *            the clazz
	 */
	public SimpleXMLDocumentationType(final Class<?> clazz) {
		name = clazz.getName();
		baliseName = clazz.getSimpleName();
		indice = 0;
		description = null;
		documentation = null;

		/* enumeration */
		if (clazz.isEnum()) {
			enumeration = new ArrayList<String>();
			Object[] enumConstants = clazz.getEnumConstants();
			for (Object o : enumConstants) {
				enumeration.add(o.toString());
			}
		}

		/* Primitive */
		primitive = (clazz.getSuperclass() == null
				|| clazz.getName().startsWith("java.lang") || clazz.getName()
				.startsWith("java.util.Date"));
	}

	/**
	 * Constructor.
	 * 
	 * @param path
	 *            the path
	 */
	public SimpleXMLDocumentationType(final String path) {
		name = path;
		baliseName = getDisplayName();
		indice = 0;
		description = null;
		documentation = null;
		primitive = false;
		enumeration = null;
	}

	/**
	 * Method.
	 * 
	 * @return the anchor
	 */
	public String getAnchor() {
		return indice + ". " + StringUtils.capitalize(baliseName);
	}

	/**
	 * Method.
	 * 
	 * @return the balise name
	 */
	public String getBaliseName() {
		return baliseName;
	}

	/**
	 * Method.
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return StringUtils.defaultIfBlank(description,
				"(@TODO) Description de " + baliseName);
	}

	/**
	 * Method.
	 * 
	 * @return the display name
	 */
	private String getDisplayName() {
		return StringUtils.removeStart(name,
				SimpleXMLDocumentationEntity.PATH_PREFIX);
	}

	/**
	 * Method.
	 * 
	 * @return the documentation
	 */
	public String getDocumentation() {
		return documentation;
	}

	/**
	 * Method.
	 * 
	 * @return the enumeration
	 */
	public List<String> getEnumeration() {
		return enumeration;
	}

	/**
	 * Method.
	 * 
	 * @return the indice
	 */
	public int getIndice() {
		return indice;
	}

	/**
	 * Method.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Method.
	 * 
	 * @return the simple name
	 */
	public String getSimpleName() {
		int lastIndex = name.lastIndexOf(".");
		if (lastIndex > -1) {
			return name.substring(lastIndex + 1);
		}
		return name;
	}

	/**
	 * Method.
	 * 
	 * @return true, if is enum
	 */
	public boolean isEnum() {
		return (enumeration != null);
	}

	/**
	 * Method.
	 * 
	 * @return true, if is primitive
	 */
	public boolean isPrimitive() {
		return primitive;
	}

	/**
	 * Method.
	 * 
	 * @param baliseName
	 *            the new balise name
	 */
	public void setBaliseName(final String baliseName) {
		this.baliseName = baliseName;
	}

	/**
	 * Method.
	 * 
	 * @param description
	 *            the new description
	 */
	public void setDescription(final String description) {
		this.description = description;
	}

	/**
	 * Method.
	 * 
	 * @param documentation
	 *            the new documentation
	 */
	public void setDocumentation(final String documentation) {
		this.documentation = documentation;
	}

	/**
	 * Method.
	 * 
	 * @param indice
	 *            the new indice
	 */
	public void setIndice(final int indice) {
		this.indice = indice;
	}
}
