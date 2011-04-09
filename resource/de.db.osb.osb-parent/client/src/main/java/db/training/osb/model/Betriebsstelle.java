package db.training.osb.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import db.training.easy.core.model.EasyPersistentExpirationObject;
import db.training.hibernate.history.Historizable;
import db.training.osb.model.babett.GaussKruegerKoordinate;

/**
 * 
 * @author michels
 * @see Feinkonzept (25.1.10), 7.1.3.2 Betriebsstelle
 */
@Entity
@Table(name = "bst")
public class Betriebsstelle extends EasyPersistentExpirationObject implements Historizable {

	private static final long serialVersionUID = -6496484236176439255L;

	/** DS100 */
	@Column(name = "kuerzel", length = 5)
	private String kuerzel;

	@ManyToOne(optional = true)
	private Betriebsstelle mutterBst;

	/** bezeichnung */
	@Column(name = "name", length = 32)
	private String name;

	@Column(name = "Netz_KEY", length = 6)
	private String netzKey;

	@OneToMany(mappedBy = "betriebsstelle")
	@Cascade(CascadeType.SAVE_UPDATE)
	@Fetch(FetchMode.SUBSELECT)
	private Set<BetriebsstelleVzgStreckeLink> strecken;

	@OneToMany(mappedBy = "bst")
	@Cascade(CascadeType.SAVE_UPDATE)
	@Fetch(FetchMode.SUBSELECT)
	private Set<BetriebsstelleRegionalbereichLink> regionalbereiche;

	// TODO: löschen
	@Column(name = "km_lage_rechts")
	private Float kmLageRechts;

	// TODO: löschen
	@Column(name = "km_ueb")
	private Long kmUeb;

	// TODO: löschen
	@Column(name = "gleis_hpgleis_dkz")
	private Boolean gleisHpgleisdKz;

	// TODO: löschen
	@Column(name = "sw_text")
	private String swText;

	@Embedded()
	private GaussKruegerKoordinate gkk;

	@Column(nullable = false)
	private boolean zugmeldestelle;

	@OneToMany(mappedBy = "bst")
	@Cascade(CascadeType.SAVE_UPDATE)
	@Fetch(FetchMode.SUBSELECT)
	private Set<BetriebsstelleBetriebsstellentypLink> betriebsstellentypen;

	public Betriebsstelle() {
	}

	public Betriebsstelle(String kuerzel, String name, String netzKey) {
		this.kuerzel = kuerzel;
		this.name = name;
		this.netzKey = netzKey;
	}

	public String getKuerzel() {
		return kuerzel;
	}

	public void setKuerzel(String kuerzel) {
		this.kuerzel = kuerzel;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Float getKmLageRechts() {
		return kmLageRechts;
	}

	public void setKmLageRechts(Float kmLageRechts) {
		this.kmLageRechts = kmLageRechts;
	}

	public Long getKmUeb() {
		return kmUeb;
	}

	public void setKmUeb(Long kmUeb) {
		this.kmUeb = kmUeb;
	}

	public Boolean getGleisHpgleisdKz() {
		return gleisHpgleisdKz;
	}

	public void setGleisHpgleisdKz(Boolean gleisHpgleisdKz) {
		this.gleisHpgleisdKz = gleisHpgleisdKz;
	}

	public String getSwText() {
		return swText;
	}

	public void setSwText(String swText) {
		this.swText = swText;
	}

	public Set<BetriebsstelleVzgStreckeLink> getStrecken() {
		return strecken;
	}

	public void setStrecke(Set<BetriebsstelleVzgStreckeLink> strecken) {
		this.strecken = strecken;
	}

	@Transient
	public String getCaption() {
		StringBuilder caption = new StringBuilder();
		if (name != null)
			caption.append("[").append(kuerzel).append("] ");
		caption.append(name);
		return caption.toString();
	}

	@Transient
	public String getCaptionWithID() {
		StringBuilder caption = new StringBuilder();
		if (name != null)
			caption.append("[").append(kuerzel).append("/").append(id).append("] ");
		caption.append(name);
		return caption.toString();
	}

	public Betriebsstelle getMutterBst() {
		return mutterBst;
	}

	public void setMutterBst(Betriebsstelle mutterBst) {
		this.mutterBst = mutterBst;
	}

	public Set<BetriebsstelleRegionalbereichLink> getRegionalbereiche() {
		return regionalbereiche;
	}

	public void setRegionalbereiche(Set<BetriebsstelleRegionalbereichLink> regionalbereiche) {
		this.regionalbereiche = regionalbereiche;
	}

	public GaussKruegerKoordinate getGkk() {
		return gkk;
	}

	public void setGkk(GaussKruegerKoordinate gkk) {
		this.gkk = gkk;
	}

	public void setZugmeldestelle(boolean zugmeldestelle) {
		this.zugmeldestelle = zugmeldestelle;
	}

	public boolean isZugmeldestelle() {
		return zugmeldestelle;
	}

	public void setNetzKey(String netzKey) {
		this.netzKey = netzKey;
	}

	public String getNetzKey() {
		return netzKey;
	}

	public Set<BetriebsstelleBetriebsstellentypLink> getBetriebsstellentypen() {
		return betriebsstellentypen;
	}

	public void setBetriebsstellentypen(
	    Set<BetriebsstelleBetriebsstellentypLink> betriebsstellentypen) {
		this.betriebsstellentypen = betriebsstellentypen;
	}

	/**
	 * gibt aus der Menge der zugeordneten Betriebsstellentypen das erste Element zurück, das
	 * vollständig innerhalb des angegebenen Suchintervalls liegt.
	 * 
	 * @param von
	 *            Beginn des Suchintervalls
	 * @param bis
	 *            Ende des Suchintervalls
	 * @return
	 */
	@Transient
	public Betriebsstellentyp getBetriebsstellentyp(Date von, Date bis) {

		for (BetriebsstelleBetriebsstellentypLink link : getBetriebsstellentypen()) {
			if (von.before(link.getGueltigVon()) && bis.after(link.getGueltigBis())) {
				return link.getTyp();
			}
		}

		return null;
	}
}