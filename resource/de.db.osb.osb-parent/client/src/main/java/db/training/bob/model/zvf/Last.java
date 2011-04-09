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
@XmlType(name = "", propOrder = { "last" })
public class Last implements Serializable {

	@XmlValue
	@Column(name = "last")
	protected Integer last;

	@XmlAttribute(name = "gl")
	@XmlJavaTypeAdapter(value = EinsNullLeerAdapter.class, type = Boolean.class)
	@Column(name = "gl")
	protected Boolean gl;

	public Last() {
		last = Integer.valueOf(0);
		gl = false;
	}

	public Integer getLast() {
		return last;
	}

	public void setLast(Integer last) {
		this.last = last;
	}

	public Boolean getGl() {
		return gl;
	}

	public void setGl(Boolean gl) {
		this.gl = gl;
	}
}
