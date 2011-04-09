package db.training.osb.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import db.training.bob.model.Regionalbereich;
import db.training.easy.core.model.EasyPersistentObjectVc;
import db.training.easy.util.FrontendHelper;
import db.training.hibernate.history.Historizable;

@SuppressWarnings("serial")
@Entity
@Table(name = "paket")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Paket extends EasyPersistentObjectVc implements Historizable {

	@Column(name = "name")
	private String kurzname;

	@Column(name = "tsp")
	private Long tsp;

	@Column(name = "esp")
	private Long esp;

	@Column(name = "kommentar")
	private String kommentar;

	@ManyToOne(optional = false)
	private Regionalbereich regionalbereich;

	@Column(name = "fahrplanjahr", nullable = false, length = 4)
	private Integer fahrplanjahr;

	@Column(name = "lfdNr", nullable = false)
	private Integer lfdNr;

	@OneToMany(mappedBy = "paket")
	private Set<SAPMassnahme> massnahmen = new HashSet<SAPMassnahme>();

	public String getKurzname() {
		return kurzname;
	}

	public void setKurzname(String kurzname) {
		this.kurzname = kurzname;
	}

	public Long getTsp() {
		return tsp;
	}

	public void setTsp(Long tsp) {
		this.tsp = tsp;
	}

	public Long getEsp() {
		return esp;
	}

	public void setEsp(Long esp) {
		this.esp = esp;
	}

	public String getKommentar() {
		return kommentar;
	}

	public void setKommentar(String kommentar) {
		this.kommentar = kommentar;
	}

	public Regionalbereich getRegionalbereich() {
		return regionalbereich;
	}

	public void setRegionalbereich(Regionalbereich regionalbereich) {
		this.regionalbereich = regionalbereich;
	}

	public Integer getFahrplanjahr() {
		return fahrplanjahr;
	}

	public void setFahrplanjahr(Integer fahrplanjahr) {
		this.fahrplanjahr = fahrplanjahr;
	}

	public Integer getLfdNr() {
		return lfdNr;
	}

	public void setLfdNr(Integer lfdNr) {
		this.lfdNr = lfdNr;
	}

	public String getPaketId() {
		// ID setzt sich zusammen aus:
		// - 1./2. Stelle ID Regionalbereich
		// - 3./4. Stelle Fahrplanjahr
		// - 5./8. Stelle LfdNr. eindeutig unter Regionalbereich und FplJahr

		StringBuffer sb = new StringBuffer("00.00.0000");

		if (getRegionalbereich() != null) {
			String rbId = Integer.toString(getRegionalbereich().getId());
			sb.replace(2 - rbId.length(), rbId.length() + 1, rbId);
		}

		if (getFahrplanjahr() != null) {
			String fj = Integer.toString(getFahrplanjahr());
			fj = fj.substring(2);
			sb.replace(3, 5, fj);
		}

		if (getLfdNr() != null) {
			String lfd = Integer.toString(getLfdNr());
			sb.replace(10 - lfd.length(), 10, lfd);
		}

		return sb.toString();
	}

	public static Integer getRegionalbereichFromPaketId(String paketId) {
		// ID setzt sich zusammen aus:
		// - 1./2. Stelle ID Regionalbereich
		Integer regionalbereich = null;

		if (paketId != null && (!"".equals(paketId.trim())) && paketId.trim().length() >= 2) {

			regionalbereich = FrontendHelper.castStringToInteger(paketId.trim().substring(0, 2));
		}
		return regionalbereich;
	}

	public static Integer getJahrFromPaketId(String paketId) {
		// ID setzt sich zusammen aus:
		// - 3./4. Stelle Fahrplanjahr
		Integer jahr = null;

		if (paketId != null && (!"".equals(paketId.trim())) && paketId.trim().length() >= 5) {

			jahr = FrontendHelper.castStringToInteger("20" + paketId.trim().substring(3, 5));
		}
		return jahr;
	}

	public static Integer getLfdNrFromPaketId(String paketId) {
		// ID setzt sich zusammen aus:
		// - 5./8. Stelle LfdNr. eindeutig unter Regionalbereich und FplJahr
		Integer lfdNr = null;

		if (paketId != null && (!"".equals(paketId.trim())) && paketId.trim().length() >= 10) {

			lfdNr = FrontendHelper.castStringToInteger(paketId.trim().substring(6, 10));
		}
		return lfdNr;
	}

	public Set<SAPMassnahme> getMassnahmen() {
		return massnahmen;
	}

	public void setMassnahmen(Set<SAPMassnahme> massnahmen) {
		this.massnahmen = massnahmen;
	}

	@Transient
	public int getMassnahmenCount() {
		if (getMassnahmen() != null) {
			return getMassnahmen().size();
		}

		return 0;
	}
}
