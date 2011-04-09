package db.training.easy.core.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.Hibernate;

import db.training.easy.util.ClassUtils;

@MappedSuperclass
@XmlAccessorType(XmlAccessType.NONE)
public class EasyPersistentObject implements Serializable {

	private static final long serialVersionUID = 5403034810636153302L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", nullable = false, insertable = true, updatable = true)
	@XmlTransient
	protected Integer id;

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return ClassUtils.printBean(this);
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		// Weil Hibernate für Lazy-Initialization Proxy Klassen generiert, reicht
		// if (getClass() != obj.getClass())
		// nicht aus. Hibernate.getClass() liefert "Proxy-freie" Klassen
		if (Hibernate.getClass(this) != Hibernate.getClass(obj))
			return false;
		final EasyPersistentObject other = (EasyPersistentObject) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
