package db.training.bob.model.zvf;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import db.training.bob.model.zvf.helper.EinsNullLeerAdapter;

@SuppressWarnings("serial")
@Embeddable
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "", propOrder = { "tfz" })
public class Tfz implements Serializable {
	@XmlValue
	@Column(name = "tfz", length = 5)
	protected String tfz;

	@XmlAttribute(name = "lzb")
	@Column(name = "lzb")
	@XmlJavaTypeAdapter(value = EinsNullLeerAdapter.class, type = Boolean.class)
	protected Boolean lzb;

	public Tfz() {
		tfz = "";
		lzb = false;
	}

	public String getTfz() {
		return tfz;
	}

	public void setTfz(String tfz) {
		this.tfz = tfz;
	}

	public Boolean getLzb() {
		return lzb;
	}

	public void setLzb(Boolean lzb) {
		this.lzb = lzb;
	}
}
