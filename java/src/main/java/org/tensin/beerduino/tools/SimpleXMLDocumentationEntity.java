package org.tensin.beerduino.tools;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.simpleframework.xml.ElementListUnion;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

/**
 * The Class SimpleXMLDocumentationEntity.
 *
 * @author j385649
 * @version $Revision: 1.4.2.3 $
 * @since 26 oct. 2011 14:40:52
 */
public class SimpleXMLDocumentationEntity {
	
	/** PATH_PREFIX. */
	public final static String PATH_PREFIX = "PATH_";
	
	/** Nom. */
	private String name;
	
	/** Balise XML. */
	private String baliseName;
	
	/** clazz. */
	private Class<?> clazz;
	
	/** inline. */
	private boolean inline;
	
	/** Description. */
	private String description;
	
	/** Required. */
	private boolean required;
	
	/** isText. */
	private boolean isText;
	
	/** isAttribute. */
	private boolean isAttribute;
	
	/** isElement. */
	private boolean isElement;
	
	/** isElementList. */
	private boolean isElementList;
	
	/** entités composantes. */
	private LinkedHashMap<String,SimpleXMLDocumentationEntity> entities;

	/**
	 * Constructor.
	 *
	 * @param method the method
	 */
	public SimpleXMLDocumentationEntity(Method method) {
		this.clazz = method.getReturnType();
		this.name = method.getName().toLowerCase().charAt(3) + method.getName().substring(4);
		this.baliseName = this.name;
		this.entities = new LinkedHashMap<String,SimpleXMLDocumentationEntity>();
	}
	
	/**
	 * Constructor.
	 *
	 * @param clazz the clazz
	 */
	public SimpleXMLDocumentationEntity(Class<?> clazz) {
		this.clazz = clazz;
		this.name = clazz.getSimpleName();
		this.baliseName = this.name;
		this.entities = new LinkedHashMap<String,SimpleXMLDocumentationEntity>();
	}
	
	/**
	 * Constructor.
	 *
	 * @param field the field
	 */
	public SimpleXMLDocumentationEntity(Field field) {
		this.clazz = field.getType();
		this.name = field.getName();
		this.baliseName = this.name;
		this.entities = new LinkedHashMap<String,SimpleXMLDocumentationEntity>();
	}
	
	/**
	 * Constructor.
	 *
	 * @param name the name
	 * @param baliseName the balise name
	 * @param entityList the entity list
	 */
	public SimpleXMLDocumentationEntity(String name, String baliseName, List<SimpleXMLDocumentationEntity> entityList) {
		this.clazz = null;
		this.name = name;
		this.baliseName = baliseName;
		this.entities = new LinkedHashMap<String,SimpleXMLDocumentationEntity>();
		for (SimpleXMLDocumentationEntity subEntity : entityList) {
			this.entities.put(subEntity.getName(), subEntity);
		}
	}

	/**
	 * Method.
	 *
	 * @return true, if is primitive
	 */
	public boolean isPrimitive() {
		return this.getType().isPrimitive();
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
	 * @return the clazz
	 */
	public Class<?> getClazz() {
		return this.clazz;
	}
	
	/**
	 * Method.
	 *
	 * @param clazz the new clazz
	 */
	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}
	
	/**
	 * Method.
	 *
	 * @return the type
	 */
	public SimpleXMLDocumentationType getType() {
		if (clazz != null) {
			return SimpleXMLDocumentation.getType(clazz);
		} else {
			return SimpleXMLDocumentation.getType(this.name);
		}
	}
	
	/**
	 * Method.
	 *
	 */
	public void parse() {
//		if (getClazz().getName().equals("com.inetpsa.pmm.config.ppfapi.ComportementPPFAPIConfig")) {
//			System.out.println("coucou");
//		} 
//		else {
//			System.out.println(this.getClazz().getName());
//		}
//		
		/* Description de la classe */
		SimpleXMLDocumentationType type = SimpleXMLDocumentation.getType(getClazz());
		for (Annotation annotation : getClazz().getAnnotations()) {
			// System.out.println(">> Annotation trouvée, class [" + annotation.getClass().getName() + "], type réel [" + annotation.annotationType().getName() + "]");
			if (isInstance(annotation, Description.class)) {
				type.setDescription((String) getFieldValue(annotation, "value"));
				// type.setDocumentation(descriptionAnnotation.documentation());
			}
			if (isInstance(annotation, Root.class)) {
				type.setBaliseName((String) getFieldValue(annotation, "name"));
			}
		}

		
		List<Class<?>> lstClass = new ArrayList<Class<?>>();
		Class<?> currentClass = getClazz();
		do {
			lstClass.add(0,currentClass);
			currentClass = currentClass.getSuperclass();
		} while (currentClass != null && currentClass != Object.class);
//		StringBuffer sbClass = new StringBuffer("Empilement ["+getClazz()+"] :");
//		for (Class<?> clazz : lstClass) {
//			sbClass.append("\n  - "+clazz.getName());
//		}
//		System.out.println(sbClass.toString());

		/* Entités de la classe */
		for (Class<?> usedClass : lstClass) {
			// System.out.println(">> Classe trouvée [" + usedClass.getName() + "]");
			for (Field field : usedClass.getDeclaredFields()) {
				// System.out.println("  >> Field trouvé [" + field.getName() + "]");
				String fieldDescription = null;
				String pathValue = null;
				List<SimpleXMLDocumentationEntity> entityList = new ArrayList<SimpleXMLDocumentationEntity>();
				for (Annotation annotation : field.getAnnotations()) {
					// System.out.println("    >> Annotation de champ trouvée, class [" + annotation.getClass().getName() + "], type réel [" + annotation.annotationType().getName() + "]");
					if (isInstance(annotation, org.simpleframework.xml.Text.class)) {
						SimpleXMLDocumentationEntity subEntity = new SimpleXMLDocumentationEntity(field);
						subEntity.setText(true);
						subEntity.setRequired(getBooleanValue(annotation, "required"));
						fieldDescription = type.getDescription();
						entityList.add(subEntity);
					}
					if (isInstance(annotation, org.simpleframework.xml.Attribute.class)) {
						SimpleXMLDocumentationEntity subEntity = new SimpleXMLDocumentationEntity(field);
						subEntity.setAttribute(true);
						subEntity.setRequired(getBooleanValue(annotation, "required"));
						subEntity.setBaliseName(StringUtils.defaultIfEmpty((String)getFieldValue(annotation, "name"), subEntity.getBaliseName()));
						entityList.add(subEntity);
					}
					if (isInstance(annotation, org.simpleframework.xml.Element.class)) {
						SimpleXMLDocumentationEntity subEntity = new SimpleXMLDocumentationEntity(field);
						subEntity.setElement(true);
						subEntity.setRequired(getBooleanValue(annotation, "required"));
						subEntity.setBaliseName(StringUtils.defaultIfEmpty((String) getFieldValue(annotation, "name"), subEntity.getBaliseName()));
						entityList.add(subEntity);
					}
					if (isInstance(annotation, org.simpleframework.xml.ElementList.class)) {
						SimpleXMLDocumentationEntity subEntity = new SimpleXMLDocumentationEntity(field);
						subEntity.setElementList(true);
						ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
						subEntity.setClazz((Class<?>) parameterizedType.getActualTypeArguments()[0]);
						subEntity.setInline(getBooleanValue(annotation, "inline"));
						subEntity.setRequired(getBooleanValue(annotation, "required"));
						subEntity.setBaliseName(StringUtils.defaultIfEmpty((String) getFieldValue(annotation, "name"), subEntity.getBaliseName()));
						entityList.add(subEntity);
					}
					if (isInstance(annotation, ElementListUnion.class)) {
						for(Annotation o : (Annotation[])getFieldValue(annotation, "value")) {
							Class<?> parameterizedClass = (Class<?>)getFieldValue(o, "type");
							SimpleXMLDocumentationEntity subEntity = new SimpleXMLDocumentationEntity(parameterizedClass);
							subEntity.setElementList(true);
							subEntity.setClazz(parameterizedClass);
							subEntity.setInline(getBooleanValue(o, "inline"));
							subEntity.setRequired(getBooleanValue(o, "required"));
							subEntity.setBaliseName(StringUtils.defaultIfEmpty((String)getFieldValue(o, "name"), subEntity.getBaliseName()));
							entityList.add(subEntity);
						}
					}
					if (isInstance(annotation, Description.class)) {
						fieldDescription = (String) getFieldValue(annotation, "value");
					}
					if (isInstance(annotation, Path.class)) {
						pathValue = (String) getFieldValue(annotation, "value");
					}
				}
				
				/* Cas où on trouve un @Path */
				if (pathValue != null) {
					fieldDescription = StringUtils.defaultIfBlank(fieldDescription, "(@TODO) Description de "+pathValue);
					SimpleXMLDocumentationEntity pathEntity = new SimpleXMLDocumentationEntity(PATH_PREFIX+pathValue,pathValue,entityList);
					pathEntity.setElement(true);
					pathEntity.setRequired(false);
					pathEntity.setDescription(fieldDescription);
					entityList = new ArrayList<SimpleXMLDocumentationEntity>();
					entityList.add(pathEntity);
				} else {
					/* Description par defaut */
					fieldDescription = StringUtils.defaultIfBlank(fieldDescription, "(@TODO) Description de "+field.getName());
				}
				
				/* On ajoute et on part en recursif */
				addAndParse(fieldDescription, entityList);
			}
			
			/* Methodes */
			for (Method method : usedClass.getDeclaredMethods()) {
				if (method.getName().startsWith("get")) {
					String methodDescription = null;
					String pathValue = null;
					List<SimpleXMLDocumentationEntity> entityList = new ArrayList<SimpleXMLDocumentationEntity>();
					for (Annotation annotation : method.getAnnotations()) {
						if (isInstance(annotation, org.simpleframework.xml.Text.class)) {
							System.err.println("Text pas implemente pour "+method.getName()+ " sur " + usedClass.getName());
						}
						if (isInstance(annotation, org.simpleframework.xml.Attribute.class)) {
							//System.err.println("Attribute OK pour "+method.getName()+ " sur " + usedClass.getName());
							SimpleXMLDocumentationEntity subEntity = new SimpleXMLDocumentationEntity(method);
							subEntity.setAttribute(true);
							subEntity.setRequired(getBooleanValue(annotation, "required"));
							subEntity.setBaliseName(StringUtils.defaultIfEmpty((String) getFieldValue(annotation, "name"), subEntity.getBaliseName()));
							entityList.add(subEntity);
						}
						if (isInstance(annotation, org.simpleframework.xml.Element.class)) {
							//System.err.println("Element OK pour "+method.getName()+ " sur " + usedClass.getName());
							SimpleXMLDocumentationEntity subEntity = new SimpleXMLDocumentationEntity(method);
							subEntity.setElement(true);
							subEntity.setRequired(getBooleanValue(annotation, "required"));
							subEntity.setBaliseName(StringUtils.defaultIfEmpty((String) getFieldValue(annotation, "name"), subEntity.getBaliseName()));
							entityList.add(subEntity);
						}
						if (isInstance(annotation, org.simpleframework.xml.ElementList.class)) {
							System.err.println("ElementList pas implemente pour "+method.getName()+ " sur " + usedClass.getName());
						}
						if (isInstance(annotation, ElementListUnion.class)) {
							System.err.println("ElementListUnion pas implemente pour "+method.getName()+ " sur " + usedClass.getName());
						}
						if (isInstance(annotation, Description.class)) {
							methodDescription = (String) getFieldValue(annotation, "value");
						}
						if (isInstance(annotation, Path.class)) {
							pathValue = (String) getFieldValue(annotation, "value");
						}
					}
					
					
					/* Cas où on truove un @Path */
					if (pathValue != null) {
						methodDescription = StringUtils.defaultIfBlank(methodDescription, "(@TODO) Description de "+pathValue);
						SimpleXMLDocumentationEntity pathEntity = new SimpleXMLDocumentationEntity(PATH_PREFIX+pathValue,pathValue,entityList);
						pathEntity.setElement(true);
						pathEntity.setRequired(false);
						pathEntity.setDescription(methodDescription);
						entityList = new ArrayList<SimpleXMLDocumentationEntity>();
						entityList.add(pathEntity);
					} else {
						methodDescription = StringUtils.defaultIfBlank(methodDescription, "(@TODO) Description de "+method.getName());
					}
					
					/* On ajoute et on part en recursif */
					addAndParse(methodDescription, entityList);
				}
			}
			
		}
	}
	
	/**
	 * Gets the boolean value.
	 *
	 * @param annotation the annotation
	 * @param fieldName the field name
	 * @return the boolean value
	 */
	private boolean getBooleanValue(Annotation annotation, String fieldName) {
		Object o = getFieldValue(annotation, fieldName);
		return ((Boolean) o).booleanValue();
	}

	/**
	 * Détermine si une annotation implémente ou non un type.
	 * Ne PAS utiliser instanceof car dans certains contextes la classe de l'annotatino retournée par introspection est (par ex.) $Proxy1 au lieu de Root
	 *
	 * @param annotation the annotation
	 * @param clazz the clazz
	 * @return true, if is instance
	 */
	private boolean isInstance(Annotation annotation, Class<?> clazz) {
		return annotation.annotationType().getSimpleName().equalsIgnoreCase(clazz.getSimpleName());
	}
	
	/**
	 * Gets the field value.
	 *
	 * @param annotation the annotation
	 * @param fieldName the field name
	 * @return the field value
	 */
	private Object getFieldValue(Annotation annotation, String fieldName) {
        try {
			return annotation.annotationType().getDeclaredMethod(fieldName, new Class[0]).invoke(annotation);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
        return null;
	}

	/**
	 * Adds the and parse.
	 *
	 * @param description the description
	 * @param entityList the entity list
	 */
	private void addAndParse(String description, List<SimpleXMLDocumentationEntity> entityList) {
		for (SimpleXMLDocumentationEntity subEntity : entityList) {
			subEntity.setDescription(description);
			this.entities.put(subEntity.getName(), subEntity);
			
			if (subEntity.getClazz() != null) {
				boolean isTypeAlreadyDiscovered = (SimpleXMLDocumentation.getType(subEntity.getClazz()) != null);
				if (!isTypeAlreadyDiscovered) {
					SimpleXMLDocumentation.registerType(subEntity.getClazz());
					subEntity.parse();
				}
			} else {
				boolean isPathAlreadyDiscovered = (SimpleXMLDocumentation.getType(subEntity.getName()) != null);
				if (!isPathAlreadyDiscovered) {
					SimpleXMLDocumentation.registerType(subEntity.getName());
					Collection<SimpleXMLDocumentationEntity> subEntities = subEntity.getEntities().values();
					for (SimpleXMLDocumentationEntity se : subEntities) {
						boolean isTypeAlreadyDiscovered = (SimpleXMLDocumentation.getType(se.getClazz()) != null);
						if (!isTypeAlreadyDiscovered) {
							SimpleXMLDocumentation.registerType(se.getClazz());
							se.parse();
						}
					}
				}
			}
		}
	}
	
	/**
	 * Checks if is attribute.
	 *
	 * @return true, if is attribute
	 */
	public boolean isAttribute() {
		return isAttribute;
	}

	/**
	 * Sets the attribute.
	 *
	 * @param isAttribute the new attribute
	 */
	public void setAttribute(boolean isAttribute) {
		this.isAttribute = isAttribute;
	}

	/**
	 * Checks if is element.
	 *
	 * @return true, if is element
	 */
	public boolean isElement() {
		return isElement;
	}

	/**
	 * Sets the element.
	 *
	 * @param isElement the new element
	 */
	public void setElement(boolean isElement) {
		this.isElement = isElement;
	}

	/**
	 * Checks if is element list.
	 *
	 * @return true, if is element list
	 */
	public boolean isElementList() {
		return isElementList;
	}

	/**
	 * Sets the element list.
	 *
	 * @param isElementList the new element list
	 */
	public void setElementList(boolean isElementList) {
		this.isElementList = isElementList;
	}
	
	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Checks if is required.
	 *
	 * @return true, if is required
	 */
	public boolean isRequired() {
		return required;
	}

	/**
	 * Sets the required.
	 *
	 * @param required the new required
	 */
	public void setRequired(boolean required) {
		this.required = required;
	}
	
	/**
	 * Gets the balise name.
	 *
	 * @return the balise name
	 */
	public String getBaliseName() {
		return baliseName;
	}

	/**
	 * Gets the type name.
	 *
	 * @return the type name
	 */
	public String getTypeName() {
		if (clazz==null) {
			//System.err.println("clazz null pour name="+name);
			return this.name;
		}
		SimpleXMLDocumentationType type = getType();
		if (type != null) {
			return type.getName();
		} else {
			System.err.println("type null pour name="+name);
			return "TYPENULL";
		}
	}

	/**
	 * Gets the primitive xsd.
	 *
	 * @return the primitive xsd
	 */
	public String getPrimitiveXSD() {
		String xsd = "xsd:string";
		if (this.isPrimitive()) {
			Class<?> primitiveClass = this.getClazz();
			if (primitiveClass == String.class) {
				return xsd;
			}
			if (primitiveClass == int.class || primitiveClass == Integer.class) {
				return "xsd:integer";
			}
			if (primitiveClass == long.class || primitiveClass == Long.class) {
				return "xsd:long";
			}
			if (primitiveClass == boolean.class || primitiveClass == Boolean.class) {
				return "xsd:boolean";
			}			
		}
		return xsd;
	}
	
	/**
	 * Sets the balise name.
	 *
	 * @param baliseName the new balise name
	 */
	public void setBaliseName(String baliseName) {
		this.baliseName = baliseName;
	}
	
	/**
	 * Checks if is text.
	 *
	 * @return true, if is text
	 */
	public boolean isText() {
		return isText;
	}

	/**
	 * Sets the text.
	 *
	 * @param isText the new text
	 */
	public void setText(boolean isText) {
		this.isText = isText;
	}

	/**
	 * Checks if is inline.
	 *
	 * @return true, if is inline
	 */
	public boolean isInline() {
		return inline;
	}

	/**
	 * Sets the inline.
	 *
	 * @param inline the new inline
	 */
	public void setInline(boolean inline) {
		this.inline = inline;
	}

	/**
	 * Gets the entities.
	 *
	 * @return the entities
	 */
	public Map<String,SimpleXMLDocumentationEntity> getEntities() {
		return this.entities;
	}
}

