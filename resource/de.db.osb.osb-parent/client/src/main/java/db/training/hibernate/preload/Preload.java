/**
 * 
 */
package db.training.hibernate.preload;

import java.lang.reflect.Method;

import db.training.easy.core.model.EasyPersistentObject;

/**
 * (nach Java-Magazin 4.08)
 * 
 * @author michels
 * 
 */
public class Preload {

	private Class<? extends EasyPersistentObject> modelClass;

	private String property;

	public Preload(Class<? extends EasyPersistentObject> modelClass, String property) {
		this.modelClass = modelClass;
		this.property = property;
	}

	/**
	 * @return gibt die Klasse zurück, für die das Preload gilt.
	 */
	public Class<? extends EasyPersistentObject> getModelClass() {
		return modelClass;
	}

	/**
	 * @param modelClass
	 *            gibt die Klasse an, für die das Preload gilt.
	 */
	public void setModelClass(Class<EasyPersistentObject> modelClass) {
		this.modelClass = modelClass;
	}

	/**
	 * @return gibt den Property-Namen zurück, welches durch dieses Preload geladen werden soll.
	 */
	public String getProperty() {
		return property;
	}

	/**
	 * @param property
	 *            gibt den Property-Namen an, welches durch dieses Preload geladen werden soll.
	 */
	public void setProperty(String property) {
		this.property = property;
	}

	private String getPropertyGetterName(String property) {
		return String.format("get%1$s%2$s", property.toUpperCase().substring(0, 1), property
		    .substring(1));
	}

	Object invokeGetter(Object entity) {
		String getterName = getPropertyGetterName(getProperty());
		try {
			Method method = getModelClass().getMethod(getterName, (Class[]) null);
			return method.invoke(entity, (Object[]) null);

		} catch (Exception ex) {
			throw new RuntimeException("Cant't invoke getter for property: " + getProperty(), ex);
		}
	}

	@Override
	public String toString() {
		return String.format("[Preload] %s.%s", getModelClass().getSimpleName(), getProperty());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if ((obj == null) || (obj.getClass() != this.getClass()))
			return false;
		Preload otherPreload = (Preload) obj;
		if ((modelClass != null && modelClass.equals(otherPreload.modelClass))
		    && (property != null && property.equals(otherPreload.property)))
			return true;
		return false;
	}

	@Override
	public int hashCode() {
		int result = 1;
		result = 31 * result + ((modelClass == null) ? 0 : modelClass.hashCode())
		    + ((property == null) ? 0 : property.hashCode());
		return result;
	}
}
