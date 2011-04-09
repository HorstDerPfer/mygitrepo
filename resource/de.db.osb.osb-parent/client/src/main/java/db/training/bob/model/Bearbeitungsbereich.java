package db.training.bob.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import db.training.easy.core.model.EasyPersistentObject;
import db.training.hibernate.history.Historizable;

@SuppressWarnings("serial")
@Entity
@Table(name = "bearbeitungsbereich")
public class Bearbeitungsbereich extends EasyPersistentObject implements Historizable {

	@Column(name = "name")
	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
	private Regionalbereich regionalbereich;

	@Column(name = "vorgangsnr_min")
	private Integer vorgangsnrMin;

	@Column(name = "vorgangsnr_max")
	private Integer vorgangsnrMax;

	protected Bearbeitungsbereich() {
		super();
	};

	public Bearbeitungsbereich(String name) {
		super();
		this.name = name;
		this.vorgangsnrMin = 0;
		this.vorgangsnrMax = 0;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Regionalbereich getRegionalbereich() {
		return regionalbereich;
	}

	public void setRegionalbereich(Regionalbereich regionalbereich) {
		this.regionalbereich = regionalbereich;
	}

	public Integer getVorgangsnrMin() {
		return vorgangsnrMin;
	}

	public void setVorgangsnrMin(Integer vorgangsnrMin) {
		this.vorgangsnrMin = vorgangsnrMin;
	}

	public Integer getVorgangsnrMax() {
		return vorgangsnrMax;
	}

	public void setVorgangsnrMax(Integer vorgangsnrMax) {
		this.vorgangsnrMax = vorgangsnrMax;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((id == null) ? 0 : id.hashCode()) + name.hashCode();
		return result;
	}
}