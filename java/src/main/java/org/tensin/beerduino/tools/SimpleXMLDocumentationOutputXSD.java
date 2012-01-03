package org.tensin.beerduino.tools;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.simpleframework.xml.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class SimpleXMLDocumentationOutputXSD.
 */
public class SimpleXMLDocumentationOutputXSD implements ISimpleXMLDocumentationOutput {
	
	/** Logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleXMLDocumentationOutputXSD.class);
    
	/** The additionnal xsd definitions. */
	private Collection<String> additionnalXsdDefinitions = new ArrayList<String>();
	
	/** The main element name. */
	private String mainElementName;
	
	/** The main element class. */
	private String mainElementClass;
	
	/**
	 * Adds the additionnal xsd definitions.
	 *
	 * @param s the s
	 */
	public void addAdditionnalXsdDefinitions(final String s) {
		additionnalXsdDefinitions.add(s);
	}
	
	/* (non-Javadoc)
	 * @see org.tensin.beerduino.tools.ISimpleXMLDocumentation#generate(org.tensin.beerduino.tools.SimpleXMLDocumentationEntity)
	 */
	/**
	  * {@inheritDoc}
	  * 
	  * @see com.inetpsa.ltp.util.simplexmldoc.ISimpleXMLDocumentationOutput#generate(java.lang.Class, com.inetpsa.ltp.util.simplexmldoc.SimpleXMLDocumentationEntity)
	  */
	@Override
	public String generate(final Class<?> racine, final SimpleXMLDocumentationEntity entity) throws SimpleXMLDocumentationException {
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		sb.append("<xsd:schema xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\n"); 
		for (String definition : additionnalXsdDefinitions ) {
			sb.append("            ").append(definition).append("\n");
		}
		sb.append("            elementFormDefault=\"qualified\">\n\n");
		mainElementClass = getMainElementClass(racine);
		mainElementName = getMainElementName(racine);
		if ( StringUtils.isNotEmpty(mainElementClass) && StringUtils.isNotEmpty(mainElementName)) {
			sb.append("   <xsd:element name=\"").append(mainElementName).append("\" type=\"").append(mainElementClass).append("\" />\n\n");
		}
		sb.append(toXSD(entity, new TreeSet<String>()));
		sb.append("</xsd:schema>\n");
		return sb.toString();
	}

	/**
	 * Gets the additionnal xsd definitions.
	 *
	 * @return the additionnal xsd definitions
	 */
	public Collection<String> getAdditionnalXsdDefinitions() {
		return additionnalXsdDefinitions;
	}

	/**
	 * Gets the main element class.
	 *
	 * @return the main element class
	 */
	public String getMainElementClass() {
		return mainElementClass;
	}

	/**
	 * Gets the main element class.
	 *
	 * @param racine the racine
	 * @return the main element class
	 */
	private String getMainElementClass(final Class<?> racine) {
		return racine.getName().replaceAll(".class", "");
	}

	/**
	 * Gets the main element name.
	 *
	 * @return the main element name
	 */
	public String getMainElementName() {
		return mainElementName;
	}

	/**
	 * Gets the main element name.
	 *
	 * @param racine the racine
	 * @return the main element name
	 */
	private String getMainElementName(Class<?> racine) {
		for (Annotation annotation : racine.getAnnotations()) {
			if (AnnotationHelper.isInstance(annotation, Root.class)) {
				return (String)AnnotationHelper.getFieldValue(annotation, "name");
			}
		}
		return null;
	}

	/**
	 * Method.
	 *
	 * @param entity the entity
	 * @return the xSD required
	 */
	private String getXSDRequired(SimpleXMLDocumentationEntity entity) {
		if (entity.isRequired()) {
			return "use=\"required\" ";
		} else {
			return "";
		}
	}

	/**
	 * Sets the additionnal xsd definitions.
	 *
	 * @param additionnalXsdDefinitions the new additionnal xsd definitions
	 */
	public void setAdditionnalXsdDefinitions(
			Collection<String> additionnalXsdDefinitions) {
		this.additionnalXsdDefinitions = additionnalXsdDefinitions;
	}

	/**
	 * Sets the main element class.
	 *
	 * @param mainElementClass the new main element class
	 */
	public void setMainElementClass(String mainElementClass) {
		this.mainElementClass = mainElementClass;
	}
	
	/**
	 * Sets the main element name.
	 *
	 * @param mainElementName the new main element name
	 */
	public void setMainElementName(String mainElementName) {
		this.mainElementName = mainElementName;
	}

	/**
	 * Method.
	 *
	 * @param currentEntity the current entity
	 * @param alreadyDisplayed the already displayed
	 * @return the string
	 */
	public String toXSD(final SimpleXMLDocumentationEntity currentEntity, final Set<String> alreadyDisplayed) {
		/* Titre */
//		if (StringUtils.equals("ComportementPPFEntreeMagasinConfig",this.name)) {
//			System.out.println("toto:");
//		} else {
//			System.out.println(this.name);
//		}

		StringBuffer sb = new StringBuffer();
		sb.append("   <xsd:complexType name=\""+currentEntity.getType().getName()+"\">\n");
		
		/* Text */
		int nbTexts = 0;
		Iterator<String> itEntities = currentEntity.getEntities().keySet().iterator();
		while(itEntities.hasNext()) {
			String name = itEntities.next();
			SimpleXMLDocumentationEntity entity = currentEntity.getEntities().get(name);
			if (entity.isText()) {
				nbTexts++;
			}
		}
		if (nbTexts > 1) {
			LOGGER.error("Etrange: "+nbTexts+" 'text' sur "+currentEntity.getBaliseName());
		}

		/* Enum */
		int nbEnums = 0;
		StringBuffer sbEnum = new StringBuffer();
		if (currentEntity.getType().isEnum()) {
			nbEnums++;
			sbEnum.append("   <xsd:simpleType name=\""+currentEntity.getTypeName()+"\">\n"); 
			sbEnum.append("      <xsd:restriction base=\"xsd:string\">\n");
			List<String> enumerations = currentEntity.getType().getEnumeration();
			for (String enumeration : enumerations) {
				sbEnum.append("         <xsd:enumeration value=\""+enumeration+"\"/>\n");
			}
			sbEnum.append("      </xsd:restriction>\n");
			sbEnum.append("   </xsd:simpleType>\n");
		}
		
		/* Element */
		int nbElements = 0;
		StringBuffer sbElements = new StringBuffer();
		itEntities = currentEntity.getEntities().keySet().iterator();
		while(itEntities.hasNext()) {
			String name = itEntities.next();
			SimpleXMLDocumentationEntity entity = currentEntity.getEntities().get(name);
			if (entity.isElement()) {
				if (entity.isPrimitive()) {
					sbElements.append("            <xsd:element name=\""+entity.getBaliseName()+"\" type=\""+entity.getPrimitiveXSD()+"\" />\n");
				} else {
					sbElements.append("            <xsd:element name=\""+entity.getBaliseName()+"\" type=\""+entity.getTypeName()+"\" minOccurs=\"0\" />\n");
				}
				nbElements++;
			} 
			else if (entity.isElementList()) {
				nbElements++;
				if (entity.isInline()) {
					if (entity.isPrimitive()) {
						sbElements.append("            <xsd:element name=\""+entity.getType().getBaliseName()+"\" minOccurs=\"0\" maxOccurs=\"unbounded\" />\n");
					} else {
						sbElements.append("            <xsd:element name=\""+entity.getType().getBaliseName()+"\" type=\""+entity.getTypeName()+"\" minOccurs=\"0\" maxOccurs=\"unbounded\" />\n");
					}
				} else {
					sbElements.append("            <xsd:element name=\""+entity.getBaliseName()+"\" minOccurs=\"0\" >\n");
					sbElements.append("               <xsd:complexType>\n");
					sbElements.append("                  <xsd:sequence>\n");
					sbElements.append("            			<xsd:element name=\""+entity.getType().getBaliseName()+"\" type=\""+entity.getTypeName()+"\" minOccurs=\"0\" maxOccurs=\"unbounded\" />\n");
					sbElements.append("                  </xsd:sequence>\n");
					sbElements.append("               </xsd:complexType>\n");
					sbElements.append("            </xsd:element>\n");
				}
			}
		}

		/* Attributs */
		int nbAttributs = 0;
		StringBuffer sbAttributes = new StringBuffer();
		itEntities = currentEntity.getEntities().keySet().iterator();
		while(itEntities.hasNext()) {
			String name = itEntities.next();
			SimpleXMLDocumentationEntity entity = currentEntity.getEntities().get(name);
			if (entity.isAttribute()) {
				if (entity.getClazz().isEnum()) {
					sbAttributes.append("         <xsd:attribute name=\""+entity.getBaliseName()+"\" type=\""+entity.getTypeName()+"\" "+getXSDRequired(entity)+"/>\n");					
				} else {
					sbAttributes.append("         <xsd:attribute name=\""+entity.getBaliseName()+"\" type=\""+entity.getPrimitiveXSD()+"\" "+getXSDRequired(entity)+"/>\n");
				}
				nbAttributs++;
			}
		}

		/* Fin de l'element */
		if (nbTexts > 0) {
			sb.append("      <xsd:simpleContent>\n");
			sb.append("         <xsd:extension base=\"xsd:string\">\n");
			if (nbElements > 0) {
				sb.append("         <xsd:choice maxOccurs=\"unbounded\">\n");
				sb.append(sbElements);
				sb.append("         </xsd:choice>\n");
			}
			if (nbAttributs > 0) {
				sb.append(sbAttributes);
			}
			sb.append("         </xsd:extension>\n");
			sb.append("      </xsd:simpleContent>\n");
			sb.append("   </xsd:complexType>\n");
		} else if (currentEntity.getType().isEnum()) {
			sb = sbEnum;
		} else {
			if (nbElements > 0) {
				sb.append("         <xsd:choice maxOccurs=\"unbounded\">\n");
				sb.append(sbElements + "         </xsd:choice>\n");
			}
			if (nbAttributs > 0) {
				sb.append(sbAttributes);
			}
			sb.append("   </xsd:complexType>\n");
		} 
		
		if (nbTexts == 0 && nbElements == 0 && nbAttributs == 0 && nbEnums ==0) {
			LOGGER.info("Balise vide: <"+currentEntity.getBaliseName()+">");
		}
		
		
		/* Entités suivantes.... */
		itEntities = currentEntity.getEntities().keySet().iterator();
		while(itEntities.hasNext()) {
			String name = itEntities.next();
			SimpleXMLDocumentationEntity subEntity = currentEntity.getEntities().get(name);
			if (!subEntity.isPrimitive() && !alreadyDisplayed.contains(subEntity.getTypeName())) {
				alreadyDisplayed.add(subEntity.getTypeName());
				sb.append("\n"+toXSD(subEntity, alreadyDisplayed));
				
			}
		}
		
		return sb.toString();
	}
}
