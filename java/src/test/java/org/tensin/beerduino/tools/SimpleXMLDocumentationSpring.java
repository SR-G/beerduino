package org.tensin.beerduino.tools;

import java.util.prefs.Preferences;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Persister;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.tensin.beerduino.CoreException;

public class SimpleXMLDocumentationSpring {

	public static void main(final String[] args) throws CoreException {
		SimpleXMLDocumentationSpring doc = new SimpleXMLDocumentationSpring();
		doc.generate(Preferences.class, new SimpleXMLDocumentationOutputWiki(),
				"beerduino.wiki");
	}

	private void generate(final Class<?> className, final ISimpleXMLDocumentationOutput converter, final String destination) {
		Persister p = new Persister();
	
		ClassPathScanningCandidateComponentProvider scanner =
				new ClassPathScanningCandidateComponentProvider(true);

		scanner.addIncludeFilter(new AnnotationTypeFilter(Root.class));
		scanner.addIncludeFilter(new AnnotationTypeFilter(Element.class));
		scanner.addIncludeFilter(new AnnotationTypeFilter(Attribute.class));
		scanner.addIncludeFilter(new AssignableTypeFilter(className));

		for (BeanDefinition bd : scanner.findCandidateComponents("org/tensin/")) {
		    System.out.println(bd.getBeanClassName());
		}
		
	}
}
