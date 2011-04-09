package db.training.bob.model.zvf;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import db.training.easy.core.model.EasyPersistentExpirationObject;

@SuppressWarnings("serial")
@Entity
@Table(name = "bbpstrecke")
public class BBPStrecke extends EasyPersistentExpirationObject {

	@Column(name = "nummer", unique = true)
	private Integer nummer;

	@Column(name = "bezeichnung")
	private String bezeichnung;

	public Integer getNummer() {
		return nummer;
	}

	public void setNummer(Integer nummer) {
		this.nummer = nummer;
	}

	public String getBezeichnung() {
		return bezeichnung;
	}

	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

	@Override
	@Transient
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("BBP Strecke ");
		sb.append(getNummer());
		sb.append("[");
		sb.append(getId());
		sb.append("]");
		return sb.toString();
	}
}
