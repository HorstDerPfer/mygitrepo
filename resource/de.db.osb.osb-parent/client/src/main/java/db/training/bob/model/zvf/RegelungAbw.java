package db.training.bob.model.zvf;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import db.training.easy.core.model.EasyPersistentObject;

@SuppressWarnings("serial")
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "", propOrder = { "art", "giltIn", "text" })
@Entity
@Table(name = "regelungabw")
public class RegelungAbw extends EasyPersistentObject {

	@XmlElement(required = true)
	@Column(name = "art")
	protected String art;
	
	@XmlElement(name="gilt_in")
	@OneToOne(fetch=FetchType.LAZY)
	protected Bahnhof giltIn;
	
	@XmlElement
	@Column(name = "text")
	protected String text;

	public String getArt() {
		return art;
	}

	public void setArt(String art) {
		this.art = art;
	}

	public Bahnhof getGiltIn() {
		return giltIn;
	}

	public void setGiltIn(Bahnhof giltIn) {
		this.giltIn = giltIn;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
}
