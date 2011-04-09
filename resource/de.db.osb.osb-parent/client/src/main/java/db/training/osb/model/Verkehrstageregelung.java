package db.training.osb.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import db.training.easy.core.model.EasyPersistentObject;
import db.training.hibernate.history.Historizable;

/**
 * @see Feinkonzept (25.1.10), 7.1.1.4 Verkehrstageregelung
 * @author michels
 * 
 */
@Table(name = "verkehrstageregelung")
@SuppressWarnings("serial")
@Entity
public class Verkehrstageregelung extends EasyPersistentObject implements Historizable {

	@Column(name = "vts", nullable = false, scale = 5)
	private Integer vts;

	@Column(name = "vtr")
	private String vtr;

	public Integer getVts() {
		return vts;
	}

	public void setVts(Integer vts) {
		this.vts = vts;
	}

	public String getVtr() {
		return vtr;
	}

	public void setVtr(String vtr) {
		this.vtr = vtr;
	}

	public String getCaption() {
		StringBuilder sb = new StringBuilder();
		sb.append(getVts());
		sb.append(" - ");
		sb.append(getVtr());
		return sb.toString();
	}
}
