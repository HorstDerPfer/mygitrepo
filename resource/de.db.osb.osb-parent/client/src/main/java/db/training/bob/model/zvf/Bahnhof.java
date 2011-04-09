package db.training.bob.model.zvf;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@SuppressWarnings("serial")
@Entity
@Table(name = "bahnhof")
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "", propOrder = { "langName", "ds100" })
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Bahnhof implements Serializable {

	@XmlTransient
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", nullable = false, insertable = true, updatable = true)
	private Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@XmlValue
	@Column(name = "langname")
	protected String langName;

	@XmlAttribute(name = "ds100")
	@Column(name = "ds100", length = 5, unique = true)
	protected String ds100;

	public Bahnhof() {

	}

	public Bahnhof(String langName, String ds100) {
		this.langName = langName;
		this.ds100 = ds100;
	}

	public String getLangName() {
		return langName;
	}

	public void setLangName(String langName) {
		this.langName = langName;
	}

	public String getDs100() {
		return ds100;
	}

	public void setDs100(String ds100) {
		this.ds100 = ds100;
	}
}
