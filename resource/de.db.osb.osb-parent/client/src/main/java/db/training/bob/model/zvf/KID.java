package db.training.bob.model.zvf;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import db.training.bob.model.zvf.helper.DateAdapter;

@SuppressWarnings("serial")
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "", propOrder = { "timestamp" })
@Entity
@Table(name = "kid")
public class KID implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", nullable = false, insertable = true, updatable = true)
	@XmlTransient
	protected Integer id;

	@XmlValue
	@Column(name = "anzahlKID")
	protected Integer anzahl;

	@XmlAttribute(name = "timestamp")
	@Temporal(TemporalType.DATE)
	@XmlJavaTypeAdapter(value = DateAdapter.class, type = Date.class)
	@Column(name = "timestampKID")
	protected Date timestamp;

	public KID() {
		anzahl = Integer.valueOf(0);
	}

	public Integer getAnzahl() {
		return anzahl;
	}

	public void setAnzahl(final Integer anzahl) {
		this.anzahl = anzahl;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(final Date timestamp) {
		this.timestamp = timestamp;
	}
}
