package org.tensin.common.helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.Vector;

/**
 * Dump Helper.
 * 
 * @author u248663
 * @version $Revision: 1.4 $
 * @since 2 avr. 2009 09:55:19
 */
public final class DumpHelper {

    /** The Constant CARRIAGE_RETURN. */
    private static final Object CARRIAGE_RETURN = "\n";

    /**
     * Dump a collection.
     * 
     * @param l
     *            Collection do dump
     * @return String
     */
    public static String dump(final Collection<?> l) {
        StringBuilder sb = new StringBuilder();
        if (l == null) {
            sb.append("Variable non initialisée (null)");
        } else if (l.isEmpty()) {
            sb.append("Liste vide");
        } else {
            Iterator<?> iterator = l.iterator();
            while (iterator.hasNext()) {
                sb.append(" - ").append(iterator.next().toString()).append(CARRIAGE_RETURN);
            }
        }
        return (sb.toString());
    }

    /**
     * Dump du contenu d'une énumération.
     * 
     * @param e
     *            L'énumération à dumper
     * @return La représentation textuelle
     */
    public static String dump(final Enumeration<?> e) {
        StringBuilder sb = new StringBuilder("");
        if (e == null) {
            sb.append("Variable non initialisée (null)");
        } else {
            while (e.hasMoreElements()) {
                sb.append(" - ").append(e.nextElement().toString()).append(CARRIAGE_RETURN);
            }
        }

        return sb.toString();
    }

    /**
     * Dump du contenu d'une map.
     * 
     * @param h
     *            La map a dumper
     * @return La représentation textuelle
     */
    public static String dump(final HashMap<?, ?> h) {
        return dump((Map<?, ?>) h);
    }

    /**
     * Dump a map.
     * 
     * @param h
     *            Map to dump
     * @param sort
     *            Map must be sorted by keys ?
     * @return String
     */
    public static String dump(final HashMap<?, ?> h, final boolean sort) {
        if (sort) {
            Map<Object, Object> sorted = new TreeMap<Object, Object>();
            sorted.putAll(h);
            return dump(sorted);
        } else {
            return dump(h);
        }
    }

    /**
     * Dump du contenu d'une liste.
     * 
     * @param l
     *            La liste à dumper
     * @return La représentation textuelle
     */
    public static String dump(final List<?> l) {
        return dump((Collection<?>) l);
    }

    /**
     * Dump a list.
     * 
     * @param l
     *            List do dump
     * @param sort
     *            List must be sorted ?
     * @return String
     */
    public static String dump(final List<Comparable<? super Comparable<?>>> l, final boolean sort) {
        if (sort) {
            Collections.sort(l);
        }
        return dump((Collection<?>) l);
    }

    /**
     * Dump du contenu d'une Hashmap.
     * 
     * @param h
     *            La map a dumper
     * @return La représentation textuelle
     */
    public static String dump(final Map<?, ?> h) {
        StringBuilder sb = new StringBuilder("");
        Object item = null;
        if (h == null) {
            sb.append("Variable non initialisée (null)");
        } else {
            if (h.isEmpty()) {
                sb.append("Tableau vide");
            } else {
                Vector v = new Vector(h.keySet());
                Collections.sort(v);
                Iterator<?> iterator = v.iterator();
                while (iterator.hasNext()) {
                    item = iterator.next();
                    sb.append(" - ");
                    sb.append(item.toString());
                    sb.append(" = ");
                    sb.append(h.get(item).toString()).append(CARRIAGE_RETURN);
                }
            }
        }
        return (sb.toString());
    }

    /**
     * Dump d'un objet.
     * 
     * @param o
     *            L'objet a dumper
     * @return La représentation textuelle
     */
    public static String dump(final Object o) {
        return o.toString();
    }

    /**
     * Affichage du contenu d'un tableau.
     * 
     * @param objects
     *            Objects[]
     * @return String.
     */
    public static String dump(final Object[] objects) {
        ArrayList<?> a = new ArrayList<Object>(Arrays.asList(objects));
        return (dump(a));
    }

    /**
     * Dump d'un ensemble de properties.
     * 
     * @param p
     *            L'objet Properties à dumper
     * @return La représentation textuelle
     */
    public static String dump(final Properties p) {
        Enumeration<?> enumProperties = p.propertyNames();
        StringBuilder sb = new StringBuilder();

        int cnt = 0;
        String propName = null;
        String propValue = null;
        for (; enumProperties.hasMoreElements(); cnt++) {
            // Get property name
            propName = (String) enumProperties.nextElement();

            // Get property value
            propValue = (String) p.get(propName);

            sb.append(" - ").append(propName).append(" = ").append(propValue).append(CARRIAGE_RETURN);
        }

        if (cnt == 0) {
            return "Ensemble Properties vide.";
        } else {
            return sb.toString();
        }
    }

    /**
     * Dump du contenu d'une liste.
     * 
     * @param l
     *            La liste à dumper
     * @return La représentation textuelle
     */
    public static String singleDump(final Collection<?> l) {
        StringBuilder sb = new StringBuilder();
        if (l == null) {
            sb.append("[]");
        } else if (l.isEmpty()) {
            sb.append("[]");
        } else {
            Iterator<?> iterator = l.iterator();
            int cnt = 0;
            sb.append("[");
            while (iterator.hasNext()) {
                if (cnt++ > 0) {
                    sb.append(", ");
                }
                sb.append(iterator.next().toString());
            }
            sb.append("]");
        }
        return (sb.toString());
    }

    /**
     * Dump du contenu d'une liste.
     * 
     * @param l
     *            La liste à dumper
     * @return La représentation textuelle
     */
    public static String singleDump(final List<?> l) {
        return singleDump((Collection<?>) l);
    }

    /**
     * Method.
     * 
     * @param objects
     *            Object[]
     * @return String
     */
    public static String singleDump(final Object[] objects) {
        ArrayList<?> a = new ArrayList<Object>(Arrays.asList(objects));
        return (singleDump(a));
    }

    /**
     * Constructor. Cannot instanciate.
     */
    private DumpHelper() {
    }
}