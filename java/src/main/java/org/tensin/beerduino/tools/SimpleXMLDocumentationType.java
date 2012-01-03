/*
 * 
 */
package org.tensin.beerduino.tools;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * The Class SimpleXMLDocumentationType.
 *
 * @author j385649
 * @version $Revision: 1.3 $
 * @since 26 oct. 2011 14:40:52
 */
public class SimpleXMLDocumentationType {
	
	/** Name. */
	private String name;
	
	/** BaliseName. */
	private String baliseName;

	/** indice. */
	private int indice;
	
	/** description. */
	private String description;
	
	/** Documentation. */
	private String documentation;
	
	/** primitive. */
	private boolean primitive;
	
	/** enumeration. */
	private List<String> enumeration;
	
	/**
	 * Constructor.
	 *
	 * @param clazz the clazz
	 */
	public SimpleXMLDocumentationType(Class<?> clazz) {
		this.name = clazz.getName();
		this.baliseName = clazz.getSimpleName();
		this.indice = 0;
		this.description = null;
		this.documentation = null;
		
		/* enumeration */
		if (clazz.isEnum()) {
			this.enumeration = new ArrayList<String>();
			Object[] enumConstants = clazz.getEnumConstants();
			for (Object o : enumConstants) {
				this.enumeration.add(o.toString());
			}
		}

		/* Primitive */
		this.primitive = (clazz.getSuperclass() == null || clazz.getName().startsWith("java.lang")
				|| clazz.getName().startsWith("java.util.Date"));
	}
	
	/**
	 * Constructor.
	 *
	 * @param path the path
	 */
	public SimpleXMLDocumentationType(String path) {
		this.name = path;
		this.baliseName = getDisplayName();
		this.indice = 0;
		this.description = null;
		this.documentation = null;
		this.primitive = false;
		this.enumeration = null;
	}
	
	/**
	 * Method.
	 *
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Method.
	 *
	 * @return the simple name
	 */
	public String getSimpleName() {
		int lastIndex = this.name.lastIndexOf(".");
		if (lastIndex > -1) {
			return this.name.substring(lastIndex+1);
		}
		return this.name;
	}
	
	/**
	 * Method.
	 *
	 * @return the display name
	 */
	private String getDisplayName() {
		return StringUtils.removeStart(this.name,SimpleXMLDocumentationEntity.PATH_PREFIX);
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
	 * @param baliseName the new balise name
	 */
	public void setBaliseName(String baliseName) {
		this.baliseName = baliseName;
	}

	/**
	 * Method.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return StringUtils.defaultIfBlank(description,"(@TODO) Description de "+this.baliseName);
	}
	
	/**
	 * Method.
	 *
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
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
	 * @param documentation the new documentation
	 */
	public void setDocumentation(String documentation) {
		this.documentation = documentation;
	}

	/**
	 * Method.
	 *
	 * @return true, if is primitive
	 */
	public boolean isPrimitive() {
		return this.primitive;
	}
	
	/**
	 * Method.
	 *
	 * @return true, if is enum
	 */
	public boolean isEnum() {
		return (this.enumeration != null);
	}
	
	/**
	 * Method.
	 *
	 * @return the enumeration
	 */
	public List<String> getEnumeration() {
		return this.enumeration;
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
	 * @param indice the new indice
	 */
	public void setIndice(int indice) {
		this.indice = indice;
	}	
	
	/**
	 * Method.
	 *
	 * @return the anchor
	 */
	public String getAnchor() {
		return this.indice + "." + this.baliseName;
	}
}
