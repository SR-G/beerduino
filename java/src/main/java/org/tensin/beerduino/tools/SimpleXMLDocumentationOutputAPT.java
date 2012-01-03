package org.tensin.beerduino.tools;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;

/**
 * The Class SimpleXMLDocumentationOutputAPT.
 */
public class SimpleXMLDocumentationOutputAPT implements ISimpleXMLDocumentationOutput {

	/* (non-Javadoc)
	 * @see org.tensin.beerduino.tools.ISimpleXMLDocumentation#generate(org.tensin.beerduino.tools.SimpleXMLDocumentationEntity)
	 */
	@Override
	public String generate(final Class<?> racine, final SimpleXMLDocumentationEntity entity) throws SimpleXMLDocumentationException {
		StringBuilder sb = new StringBuilder("{{Documentation}} ");
		sb.append("(" + getCurrentDateWithDateFormat("yyyy-MM-dd HH:mm:ss") + ")\n\n");
		sb.append(summaryInAPT(1,entity,1));
		sb.append(toAPT(entity, new TreeSet<String>()));
		return sb.toString();
	}

	/**
	 * Méthode getDateWithDateFormat.
	 *
	 * @param pattern the pattern
	 * @param date the date
	 * @return the date with date format
	 * String
	 */
	public static String getDateWithDateFormat(String pattern, Date date) {
		final SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		return dateFormat.format(date);
	}
	
	/**
	 * Méthode getCurrentDateWithDateFormat.
	 *
	 * @param pattern the pattern
	 * @return current date with date format
	 * String
	 */
	public static String getCurrentDateWithDateFormat(String pattern) {
		return getDateWithDateFormat(pattern, new Date());
	}
	
	/**
	 * Method.
	 *
	 * @param profondeur the profondeur
	 * @param root the root
	 * @param indice the indice
	 * @return the string
	 */
	private String summaryInAPT(int profondeur, SimpleXMLDocumentationEntity root, int indice) {
		StringBuffer sb = new StringBuffer();
		sb.append("    " + StringUtils.repeat(" ",profondeur*2)+ "* {{{" + root.getType().getAnchor() + "}");
		sb.append(root.getType().getBaliseName()+"}}\n\n");
		Collection<SimpleXMLDocumentationEntity> subEntities = root.getEntities().values();
		for (SimpleXMLDocumentationEntity subEntity : subEntities) {
			if (subEntity.isElement() || subEntity.isElementList() || subEntity.isText()) {
				if (!subEntity.isPrimitive()) {
					sb.append(summaryInAPT(profondeur+1,subEntity,indice));
				}
			}
		}
		return sb.toString();
	}
	

	/**
	 * Method.
	 *
	 * @param entity the entity
	 * @return the link
	 */
	private String getLink(final SimpleXMLDocumentationEntity entity) {
		if (entity.isPrimitive()) {
			return entity.getType().getSimpleName();
		} else {
			return "{{{" + entity.getType().getAnchor() + "}"+entity.getType().getBaliseName()+"}}";
		}
	}

	/**
	 * Method.
	 *
	 * @param entity the entity
	 * @return the required
	 */
	private String getRequired(final SimpleXMLDocumentationEntity entity) {
		if (entity.isRequired()) {
			return " X ";
		} else {
			return "   ";
		}
	}
	
	/**
	 * Method.
	 *
	 * @param currentEntity the current entity
	 * @param alreadyDisplayed the already displayed
	 * @return the string
	 */
	public String toAPT(final SimpleXMLDocumentationEntity currentEntity, final Set<String> alreadyDisplayed) {
		/* Doublons */
		// currentEntity.checkDoublons(currentEntity.getType().getBaliseName(), currentEntity.getType().getName());
		
		/* Titre */
		StringBuffer sb = new StringBuffer();
		sb.append("{"+currentEntity.getType().getAnchor()+"}\n\n");
		sb.append("    "+currentEntity.getType().getDescription()+"\n\n");
		
		/* Exemple */
		StringBuffer sbExemple = new StringBuffer("* Exemple\n\n");
		sbExemple.append("+--------------------------------------------------------------------------------+\n");
		sbExemple.append("<"+currentEntity.getType().getBaliseName());
		
		/* Enum */
		if (currentEntity.getType().isEnum()) {
			StringBuffer sbEnum = new StringBuffer("* Enumeration\n\n");
			sbEnum.append("*-----------------+\n");
			sbEnum.append("| <<Valeur>>      |\n");
			sbEnum.append("*-----------------+\n");
			List<String> enumerations = currentEntity.getType().getEnumeration();
			for (String enumeration : enumerations) {
				sbEnum.append("| " + enumeration + "|\n");
				sbEnum.append("*-----------------+\n");
			}
			sb.append(sbEnum+"\n\n");
		}
		
		/* Attributs */
		int initialAttributeLineSize = ("<"+currentEntity.getType().getBaliseName()).length();
		int currentAttributeLineSize = initialAttributeLineSize;
		String currentAttributeLine = "";
		int nbAttributs = 0;
		StringBuffer sbAttributs = new StringBuffer("* Attributs\n\n");
		sbAttributs.append("*-----------------+-------------------+-------*-------------------+\n");
		sbAttributs.append("| <<Attributs>>   | <<Type>>          |<<Req>>| <<Description>>   |\n");
		sbAttributs.append("*-----------------+-------------------+-------*-------------------+\n");
		Iterator<String> itEntities = currentEntity.getEntities().keySet().iterator();
		while(itEntities.hasNext()) {
			String name = itEntities.next();
			SimpleXMLDocumentationEntity entity = currentEntity.getEntities().get(name);
			if (entity.isAttribute()) {
				nbAttributs++;
				sbAttributs.append("| " + entity.getBaliseName() + " | " + getLink(entity) + " | " + getRequired(entity) + " | " + entity.getDescription() + " |\n");
				sbAttributs.append("*-----------------+-------------------+-------*-------------------+\n");
				
				String attributeExemple = " "+entity.getBaliseName()+"='...'";
				if (currentAttributeLineSize + attributeExemple.length() > 80) {
					sbExemple.append(currentAttributeLine+"\n");
					currentAttributeLine = StringUtils.repeat(" ",initialAttributeLineSize);
					currentAttributeLineSize = initialAttributeLineSize;
				}
				currentAttributeLine += attributeExemple;
				currentAttributeLineSize += attributeExemple.length();
				
			}
		}
		sbExemple.append(currentAttributeLine+">");
		if (nbAttributs > 0) {
			sb.append(sbAttributs + "\n\n");
		}
		
		/* Element */
		int nbElements = 0;
		StringBuffer sbElements = new StringBuffer("* Elements\n\n");
		sbElements.append("*-----------------+-------------------+-------*-------------------+\n");
		sbElements.append("| <<Elements>>    | <<Type>>          |<<Req>>| <<Description>>   |\n");
		sbElements.append("*-----------------+-------------------+-------*-------------------+\n");
		itEntities = currentEntity.getEntities().keySet().iterator();
		while(itEntities.hasNext()) {
			String name = itEntities.next();
			SimpleXMLDocumentationEntity entity = currentEntity.getEntities().get(name);
			if (entity.isElement()) {
				nbElements++;
				sbElements.append("| " + entity.getBaliseName() + " | " + getLink(entity) + " | " + getRequired(entity) + " | " + entity.getDescription() + " |\n");
				sbElements.append("*-----------------+-------------------+-------*-------------------+\n");
				sbExemple.append("\n   <!-- Description de '"+entity.getBaliseName()+"' -->");
				sbExemple.append("\n   <"+entity.getBaliseName()+">");
				if (entity.isPrimitive()) {
					sbExemple.append(". . .");
				} else { 
					sbExemple.append("\n   . . .\n   ");
				}
				sbExemple.append("</"+entity.getBaliseName()+">\n");
			} else if (entity.isElementList()) {
				nbElements++;
				if (entity.isInline()) {
					sbElements.append("| Liste de " + getLink(entity) + " | " + getLink(entity) + " | " + getRequired(entity) + " | " + entity.getDescription() + " |\n");
					sbExemple.append("\n   <!-- Liste de '"+entity.getType().getBaliseName()+"' -->");
					sbExemple.append("\n   <"+entity.getType().getBaliseName()+">");
					sbExemple.append("\n   . . .");
					sbExemple.append("\n   </"+entity.getType().getBaliseName()+">");
					sbExemple.append("\n   .");
					sbExemple.append("\n   .");
					sbExemple.append("\n   .");
					sbExemple.append("\n   <"+entity.getType().getBaliseName()+">");
					sbExemple.append("\n   . . .");
					sbExemple.append("\n   </"+entity.getType().getBaliseName()+">\n");
				} else {
					sbElements.append("| " + entity.getBaliseName() + " | Liste de " + getLink(entity) + " | " + getRequired(entity) + " | " + entity.getDescription() + " |\n");
					sbExemple.append("\n   <!-- Description de '"+entity.getBaliseName()+"' -->");
					sbExemple.append("\n   <"+entity.getBaliseName()+">");
					sbExemple.append("\n   . . .");
					sbExemple.append("\n   </"+entity.getBaliseName()+">\n");
				}
				sbElements.append("*-----------------+-------------------+---+-------------------+\n");
			}
		}
		if (nbElements > 0) {
			sb.append(sbElements + "\n\n");
		}
		
		/* Text */
		int nbTexts = 0;
		StringBuffer sbTexts = new StringBuffer("* Contenu\n\n");
		sbTexts.append("*-------------------+-------*-------------------+\n");
		sbTexts.append("| <<Type>>          |<<Req>>| <<Description>>   |\n");
		sbTexts.append("*-------------------+-------*-------------------+\n");
		itEntities = currentEntity.getEntities().keySet().iterator();
		while(itEntities.hasNext()) {
			String name = itEntities.next();
			SimpleXMLDocumentationEntity entity = currentEntity.getEntities().get(name);
			if (entity.isText()) {
				nbTexts++;
				sbTexts.append("| " + getLink(entity) + " | " + getRequired(entity) + " | " + entity.getDescription() + " |\n");
				sbTexts.append("*-------------------+-------*-------------------+\n");
				sbExemple.append("...");
			}
		}
		if (nbTexts > 0) {
			sb.append(sbTexts + "\n\n");
		}
		
		/* Fin de l'exemple */
		if (nbElements == 0 && nbTexts == 0) {
			sbExemple.deleteCharAt(sbExemple.length()-1).append(" />\n");
		} else {
			sbExemple.append("</"+currentEntity.getType().getBaliseName()+">\n");
		}
		sbExemple.append("+--------------------------------------------------------------------------------+\n\n");
		if (!currentEntity.getType().isEnum()) {
			sb.append(sbExemple);
		}
		
		/* Classe d'implementation */
		if (currentEntity.getType().getName().indexOf(".") > -1) {
			String fullName = currentEntity.getType().getName();
			String packageName = fullName.substring(0,fullName.lastIndexOf("."));
			String className = fullName.substring(fullName.lastIndexOf(".")+1);
			sb.append("* Implémentation\n\n");
			sb.append("*----------+---------------------------------------------------------------+\n");
			sb.append("| Package  | "+packageName+" |\n");
			sb.append("*----------+---------------------------------------------------------------+\n");
			sb.append("| Classe   | "+className+" |\n");
			sb.append("*----------+---------------------------------------------------------------+\n\n");
		}
		
		/* Documentation */
		String documentation = currentEntity.getType().getDocumentation();
		if (StringUtils.isNotBlank(documentation)) {
			StringBuffer sbDocumentation = new StringBuffer("* Documentation\n\n");
			sbDocumentation.append("    Une documentation complète est disponible {{{"+documentation+"}ici}}\n");
			sb.append(sbDocumentation);
		}
		
		/* Entités suivantes.... */
		itEntities = currentEntity.getEntities().keySet().iterator();
		while(itEntities.hasNext()) {
			String name = itEntities.next();
			SimpleXMLDocumentationEntity subEntity = currentEntity.getEntities().get(name);
			if (!subEntity.isPrimitive() && !alreadyDisplayed.contains(subEntity.getTypeName())) {
				alreadyDisplayed.add(subEntity.getTypeName());
				sb.append("\n\n\n"+toAPT(subEntity, alreadyDisplayed));
				
			}
		}
		
		return sb.toString();
	}	


}
