package db.training.osb.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.Table;

import db.training.easy.core.model.EasyPersistentExpirationObject;
import db.training.hibernate.history.Historizable;

@SuppressWarnings("serial")
@Entity
@Table(name = "sqlquery")
public class SqlQuery extends EasyPersistentExpirationObject implements Historizable {

	@Column(unique = true, nullable = false)
	private String name;

	@Lob
	private String beschreibung;

	@Lob
	@Column(nullable = false)
	private String query;

	public enum Modul {
		OSB, BOB
	}

	@Column(length = 3, nullable = false)
	@Enumerated(value = EnumType.STRING)
	private Modul modul;

	public enum Cluster {
		MENGENGERUEST, FORTSCHRITTSKENNZAHL, DATENAUSZUG
	}

	@Column(length = 24, nullable = false, name = "cluster_")
	@Enumerated(value = EnumType.STRING)
	private Cluster cluster;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBeschreibung() {
		return beschreibung;
	}

	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public Modul getModul() {
		return modul;
	}

	public void setModul(Modul modul) {
		this.modul = modul;
	}

	public Cluster getCluster() {
		return cluster;
	}

	public void setCluster(Cluster cluster) {
		this.cluster = cluster;
	}

}