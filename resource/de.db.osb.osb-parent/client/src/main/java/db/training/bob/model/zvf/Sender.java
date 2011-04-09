package db.training.bob.model.zvf;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import db.training.easy.core.model.EasyPersistentObject;

@SuppressWarnings("serial")
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "", propOrder = { "name", "vorname", "kuerzel", "abteilung", "strasse", "plz",
        "ort", "email", "telefon", "telefonIntern" })
@Entity
@Table(name = "sender")
public class Sender extends EasyPersistentObject {

	@XmlElement(required = true)
	protected String name;

	@XmlElement(required = true)
	protected String vorname;

	@XmlElement(required = true)
	protected String kuerzel;

	@XmlElement(required = true)
	protected String abteilung;

	@XmlElement(required = true)
	protected String strasse;

	@XmlElement(required = true)
	@Column(length = 5)
	protected String plz;

	@XmlElement(required = true)
	protected String ort;

	@XmlElement(required = true)
	protected String email;

	@XmlElement(required = true)
	protected String telefon;

	@XmlElement(name = "telefon_intern", required = true)
	protected String telefonIntern;

	/**
	 * Gets the value of the name property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the value of the name property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setName(String value) {
		this.name = value;
	}

	/**
	 * Gets the value of the vorname property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getVorname() {
		return vorname;
	}

	/**
	 * Sets the value of the vorname property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setVorname(String value) {
		this.vorname = value;
	}

	/**
	 * Gets the value of the kuerzel property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getKuerzel() {
		return kuerzel;
	}

	/**
	 * Sets the value of the kuerzel property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setKuerzel(String value) {
		this.kuerzel = value;
	}

	/**
	 * Gets the value of the abteilung property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getAbteilung() {
		return abteilung;
	}

	/**
	 * Sets the value of the abteilung property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setAbteilung(String value) {
		this.abteilung = value;
	}

	/**
	 * Gets the value of the strasse property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getStrasse() {
		return strasse;
	}

	/**
	 * Sets the value of the strasse property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setStrasse(String value) {
		this.strasse = value;
	}

	/**
	 * Gets the value of the plz property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getPlz() {
		return plz;
	}

	/**
	 * Sets the value of the plz property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setPlz(String value) {
		this.plz = value;
	}

	/**
	 * Gets the value of the ort property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getOrt() {
		return ort;
	}

	/**
	 * Sets the value of the ort property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setOrt(String value) {
		this.ort = value;
	}

	/**
	 * Gets the value of the email property.
	 * 
	 * @return possible object is {@link Uebergabeblatt.Header.Sender.Email }
	 * 
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the value of the email property.
	 * 
	 * @param value
	 *            allowed object is {@link Uebergabeblatt.Header.Sender.Email }
	 * 
	 */
	public void setEmail(String value) {
		this.email = value;
	}

	/**
	 * Gets the value of the telefon property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getTelefon() {
		return telefon;
	}

	/**
	 * Sets the value of the telefon property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setTelefon(String value) {
		this.telefon = value;
	}

	/**
	 * Gets the value of the telefonIntern property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getTelefonIntern() {
		return telefonIntern;
	}

	/**
	 * Sets the value of the telefonIntern property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setTelefonIntern(String value) {
		this.telefonIntern = value;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString());

		sb.append("\nAbteilung=");
		sb.append(getAbteilung());

		sb.append("\nEMail=");
		sb.append(getEmail());

		sb.append("\nName=");
		sb.append(getName());

		sb.append("\nVorname=");
		sb.append(getVorname());

		return sb.toString();
	}
}
