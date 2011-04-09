/**
 * (c) copyright 2006 www.laliluna.de
 * Sebastian Hennebrueder
 */
package db.training.easy.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.Transient;

import org.hibernate.Hibernate;
import org.springframework.beans.MethodInvocationException;

import db.training.logwrapper.Logger;
import db.training.osb.model.Buendel;

public class ClassUtils {

	private static Logger log = Logger.getLogger(ClassUtils.class);

	@SuppressWarnings("unchecked")
	private static List<Class> printableClasses = Arrays.asList(new Class[] { Short.class,
	        Integer.class, Long.class, String.class, Date.class, Float.class, Double.class,
	        Boolean.class, Character.class });

	public static String printBean(Object object) {
		if (object == null)
			return "";

		StringBuffer result = new StringBuffer();
		result.append(object.getClass().getSimpleName());
		result.append(": ");

		for (Method method : object.getClass().getDeclaredMethods()) {

			// ausgewertet werden alle Getter, die nicht einer @Tansient-Annotation markiert sind
			if (isGetter(method)
			    && (!method.isAnnotationPresent(Transient.class))
			    && (printableClasses.contains(method.getReturnType()) || method.getReturnType()
			        .isPrimitive()) && (!method.getReturnType().isArray()))

			{
				result.append(method.getName().substring(3));
				result.append(":");
				try {
					result.append(method.invoke(object, (Object[]) null));

				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} finally {
					result.append(" ");
				}

			}

		}
		return result.toString();
	}

	public static String printProperties(Object object, String[] propertyNames) {
		if (object == null)
			return "";

		StringBuffer result = new StringBuffer();
		Class<?> clazz = Hibernate.getClass(object);
		
		List<String> fields = getPrintableFields(clazz);

		result.append(clazz.getSimpleName());
		
		if (propertyNames == null)
			propertyNames = fields.toArray(new String[] {});

		if (propertyNames.length > 0) {
			result.append(": ");

			for (int i = 0; i < propertyNames.length; i++) {
				try {
					String propertyName = propertyNames[i];
					String methodName = String.format("get%S%s", propertyName.substring(0, 1),
					    propertyName.substring(1));

					if (!fields.contains(propertyName)) {
						if (log.isDebugEnabled())
							log.debug("ignoriere nicht-persistentes Property: " + propertyName);
						continue;
					}

					Method method = clazz.getMethod(methodName, (Class<?>[]) null);

					if (isGetter(method)) {
						Object propertyValue = method.invoke(object, (Object[]) null);

						if (Hibernate.isInitialized(propertyValue)) {
							result.append(propertyName);
							result.append(":");
							result.append(String.format("'%s'",
							    ((propertyValue != null) ? propertyValue.toString() : "null")));
						} else {
							result.append(String.format("%s:(Proxy)", propertyName));
						}
						if (i < (propertyNames.length - 1)) {
							result.append(", ");
						}
					}
				} catch (NoSuchMethodException ex) {
					if (log.isDebugEnabled()) {
						log.debug("Getter nicht gefunden für Property: " + propertyNames[i]);
						log.debug(ex);
					}
				} catch (MethodInvocationException ex) {
					if (log.isDebugEnabled())
						log.debug("Fehler beim Zugriff auf Getter: ", ex);
				} catch (InvocationTargetException ex) {
					if (log.isDebugEnabled())
						log.debug("Fehler beim Zugriff auf Getter: ", ex);
				} catch (IllegalAccessException ex) {
					if (log.isDebugEnabled())
						log.debug("Fehler beim Zugriff auf Getter: ", ex);
				} catch (SecurityException e) {
					e.printStackTrace();
				}
			}
		}

		return result.toString();
	}

	public static boolean isGetter(Method method) {
		if (!method.getName().startsWith("get"))
			return false;
		if (method.getParameterTypes().length != 0)
			return false;
		if (!Modifier.isPublic(method.getModifiers()))
			return false;
		if (method.isAnnotationPresent(Transient.class))
			return false;

		return true;
	}

	public static void main(String[] args) {
		Buendel b = new Buendel();
		List<String> propertyNames = new ArrayList<String>();

		for (Method method : b.getClass().getDeclaredMethods()) {

			// ausgewertet werden alle Getter, die nicht einer @Tansient-Annotation markiert sind
			if (isGetter(method)
			// && (!method.isAnnotationPresent(Transient.class))
			    && (printableClasses.contains(method.getReturnType()) || method.getReturnType()
			        .isPrimitive()) && (!method.getReturnType().isArray()))

			{
				propertyNames.add(String.format("%s%s", method.getName().substring(3, 4)
				    .toLowerCase(), method.getName().substring(4)));
			}
		}

		System.out.println(ClassUtils.printProperties(b, null));
		// System.out.println(ClassUtils.printProperties(s, new String[] { "caption" }));
	}

	/**
	 * erlaubt sind nur @Column Mappings
	 * 
	 * @param field
	 * @return
	 */
	public static boolean isPersistentPrintableField(Field field) {
		if (field.isAnnotationPresent(Transient.class))
			return false;

		return true;
	}

	public static List<String> getPrintableFields(Class<?> clazz) {
		List<String> result = new ArrayList<String>();

		for (Class<?> c = clazz; c != null; c = c.getSuperclass()) {
			for (Field f : c.getDeclaredFields()) {
				if (isPersistentPrintableField(f))
					result.add(f.getName());
			}
		}

		return result;
	}
}
