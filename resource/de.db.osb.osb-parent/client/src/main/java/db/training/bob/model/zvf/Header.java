package db.training.bob.model.zvf;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CollectionOfElements;

import db.training.bob.model.zvf.helper.IsoDateAdapter;
import db.training.easy.core.model.EasyPersistentObject;

@SuppressWarnings("serial")
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "", propOrder = { "timestamp", "filename", "versionPrgZvf", "sender", "empfaenger" })
@Entity
@Table(name = "header")
public class Header extends EasyPersistentObject {

	@XmlElement(required = true)
	@XmlJavaTypeAdapter(value = IsoDateAdapter.class, type = Date.class)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "timestamp")
	protected Date timestamp;

	@XmlElement(required = true)
	@Column(name = "filename")
	protected String filename;

	@XmlElement(name = "version_prg_zvf", required = true)
	@Column(name = "version_prg_zvf", length = 10)
	protected String versionPrgZvf;

	@XmlElement(required = true)
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	protected Sender sender;

	@XmlElementWrapper(name = "empfaengerlist")
	@CollectionOfElements(fetch = FetchType.EAGER)
	@Cascade(org.hibernate.annotations.CascadeType.DELETE)
	protected Set<String> empfaenger;

	public Header() {
		// empfaenger = new HashSet<String>();
		sender = new Sender();
		timestamp = new GregorianCalendar().getTime();
	}

	/**
	 * Gets the value of the timestamp property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public Date getTimestamp() {
		return timestamp;
	}

	/**
	 * Sets the value of the timestamp property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setTimestamp(Date value) {
		this.timestamp = value;
	}

	/**
	 * Gets the value of the filename property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * Sets the value of the filename property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setFilename(String value) {
		this.filename = value;
	}

	/**
	 * Gets the value of the versionPrgZvf property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getVersionPrgZvf() {
		return versionPrgZvf;
	}

	/**
	 * Sets the value of the versionPrgZvf property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setVersionPrgZvf(String value) {
		this.versionPrgZvf = value;
	}

	/**
	 * Gets the value of the sender property.
	 * 
	 * @return possible object is {@link Uebergabeblatt.Header.Sender }
	 * 
	 */
	public Sender getSender() {
		return sender;
	}

	/**
	 * Sets the value of the sender property.
	 * 
	 * @param value
	 *            allowed object is {@link Uebergabeblatt.Header.Sender }
	 * 
	 */
	public void setSender(Sender value) {
		this.sender = value;
	}

	/**
	 * Gets the value of the empfaengerlist property.
	 * 
	 * @return possible object is {@link Uebergabeblatt.Header.Empfaengerlist }
	 * 
	 */
	public Set<String> getEmpfaenger() {
		if (empfaenger == null)
			return new HashSet<String>();
		return empfaenger;
	}

	/**
	 * Sets the value of the empfaengerlist property.
	 * 
	 * @param value
	 *            allowed object is {@link Uebergabeblatt.Header.Empfaengerlist }
	 * 
	 */
	public void setEmpfaenger(Set<String> value) {
		this.empfaenger = value;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		sb.append("\nfilename=");
		sb.append(getFilename());
		sb.append("\nID=");
		sb.append(getId());
		sb.append("\ntimestamp=");
		sb.append(getTimestamp());
		sb.append("\nVersionPrgZvf=");
		sb.append(getVersionPrgZvf());

		return sb.toString();
	}

	public void importHeader(Header header) {
		this.empfaenger.addAll(header.empfaenger);
		this.sender = header.sender;
		this.filename = header.filename;
		this.sender = header.sender;
		this.timestamp = header.timestamp;
	}
}