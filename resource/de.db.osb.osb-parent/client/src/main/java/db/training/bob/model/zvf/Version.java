package db.training.bob.model.zvf;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import db.training.bob.model.zvf.helper.FormularKennung;
import db.training.bob.model.zvf.helper.FormularKennungAdapter;
import db.training.easy.core.model.EasyPersistentObject;

@SuppressWarnings("serial")
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "", propOrder = { "titel", "formular", "major", "minor", "sub" })
@Entity
@Table(name = "zvfversion")
public class Version extends EasyPersistentObject implements Comparable<Version> {

	@XmlElement(required = true)
	@Column(name = "titel")
	protected String titel;

	@XmlElement(required = true)
	@XmlJavaTypeAdapter(value = FormularKennungAdapter.class, type = FormularKennung.class)
	@Column(name = "formular")
	protected FormularKennung formular;

	@XmlElement(required = true)
	@Column(name = "major")
	protected Integer major;

	@XmlElement(required = true)
	@Column(name = "minor")
	protected Integer minor;

	@XmlElement(required = true)
	@Column(name = "sub", length = 2)
	protected String sub;

	public Version() {
		// major = 0;
		// minor = 0;
		// sub = "00";
	}

	public Version(Integer major, Integer minor, String sub) {
		this.major = major;
		this.minor = minor;
		this.sub = sub;
	}

	/**
	 * Gets the value of the titel property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getTitel() {
		return titel;
	}

	/**
	 * Sets the value of the titel property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setTitel(String value) {
		this.titel = value;
	}

	/**
	 * Gets the value of the formular property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public FormularKennung getFormular() {
		return formular;
	}

	/**
	 * Sets the value of the formular property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setFormular(FormularKennung value) {
		this.formular = value;
	}

	/**
	 * Gets the value of the major property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public Integer getMajor() {
		return major;
	}

	/**
	 * Sets the value of the major property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setMajor(Integer value) {
		this.major = value;
	}

	/**
	 * Gets the value of the minor property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public Integer getMinor() {
		return minor;
	}

	/**
	 * Sets the value of the minor property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setMinor(Integer value) {
		this.minor = value;
	}

	/**
	 * Gets the value of the sub property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getSub() {
		return sub;
	}

	/**
	 * Sets the value of the sub property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setSub(String value) {
		this.sub = value;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if ((obj == null) || (obj.getClass() != this.getClass()))
			return false;
		Version version = (Version) obj;
		return (major != null && major == version.major)
		    && (minor != null && minor == version.minor)
		    && (sub != null && sub.equals(version.sub));
	}

	public int hashCode() {
		int hash = 7;
		hash = 31 * hash + (null == titel ? 0 : titel.hashCode());
		hash = 17 * hash + (null == formular ? 0 : formular.hashCode());
		hash = 13 * hash + (null == major ? 0 : major.hashCode());
		return hash;
	}

	public int compareTo(Version o) throws NumberFormatException, NullPointerException {
		final int BEFORE = -1;
		final int EQUAL = 0;
		final int AFTER = 1;

		if (this == o)
			return EQUAL;

		if (major > o.major)
			return AFTER;
		if (major < o.major)
			return BEFORE;
		if (minor > o.minor)
			return AFTER;
		if (minor < o.minor)
			return BEFORE;

		Integer sub1 = Integer.valueOf(sub);
		Integer sub2 = Integer.valueOf(o.sub);

		if (sub1 > sub2)
			return AFTER;
		if (sub1 < sub2)
			return BEFORE;
		return EQUAL;
	}
}
