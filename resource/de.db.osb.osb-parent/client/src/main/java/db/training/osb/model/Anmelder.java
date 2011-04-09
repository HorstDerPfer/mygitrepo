package db.training.osb.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import db.training.bob.model.Regionalbereich;
import db.training.easy.core.model.EasyPersistentObject;
import db.training.easy.core.model.User;

/**
 * Eigenschaften von Anmeldern und Verursachern von Änderungen von Baumaßnahmen.
 * 
 * @author michels
 * @see Feinkonzept (25.1.10), 7.1.2.1 Anmelder
 */
@Entity
@Table(name = "anmelder")
public class Anmelder extends EasyPersistentObject {

	@Transient
	private static final long serialVersionUID = -4643548085485047125L;

	@Column(length = 32, nullable = false)
	private String name;

	@Column(nullable = false)
	private boolean verursacher;

	@Column(nullable = false)
	private boolean technischerAnmelder;

	@ManyToOne(optional = true)
	private Regionalbereich regionalbereich;

	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date gueltigBis;

	@Column(nullable = false)
	private boolean bpl;

	@Column(nullable = false)
	private boolean fault;

	@ManyToMany()
	private Set<Anmeldergruppe> anmeldergruppe;

	@ManyToOne(optional = true)
	private User user;

	private boolean auftraggeber;

	/**
	 * @return Bezeichnung des Anmelders (ggf. mit Tätigkeitsbeschreibung)
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            Bezeichnung des Anmelders (ggf. mit Tätigkeitsbeschreibung)
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return sagt aus, ob der Anmelder auch Verursacher von nachträglichen Veränderungen an
	 *         Baumaßnahmen, Sperrungen und Betriebsweisen sein kann
	 */
	public boolean isVerursacher() {
		return verursacher;
	}

	/**
	 * @param verursacher
	 *            sagt aus, ob der Anmelder auch Verursacher von nachträglichen Veränderungen an
	 *            Baumaßnahmen, Sperrungen und Betriebsweisen sein kann
	 */
	public void setVerursacher(boolean verursacher) {
		this.verursacher = verursacher;
	}

	/**
	 * @return sagt aus, ob der Anmelder aus dem Bereich Technik (auch Anlagenmanagement und
	 *         Anlagenverantwortung) stammt oder nicht
	 */
	public boolean isTechnischerAnmelder() {
		return technischerAnmelder;
	}

	/**
	 * @param technischerAnmelder
	 *            sagt aus, ob der Anmelder aus dem Bereich Technik (auch Anlagenmanagement und
	 *            Anlagenverantwortung) stammt oder nicht
	 */
	public void setTechnischerAnmelder(boolean technischerAnmelder) {
		this.technischerAnmelder = technischerAnmelder;
	}

	/**
	 * @return Zuscheidung zu einer Region, für die angemeldet werden darf, Null = Anmelder für
	 *         mehrere Regionen
	 */
	public Regionalbereich getRegionalbereich() {
		return regionalbereich;
	}

	/**
	 * @param regionalbereich
	 *            Zuscheidung zu einer Region, für die angemeldet werden darf, Null = Anmelder für
	 *            mehrere Regionen
	 */
	public void setRegionalbereich(Regionalbereich regionalbereich) {
		this.regionalbereich = regionalbereich;
	}

	/**
	 * @return Falls ein Anmelder nicht mehr existiert, dann kann dieser über das Gültigkeitsdatum
	 *         gesperrt werden. "Gültig ab" ist nicht erforderlich, da Anmelder nur mit Gültigkeit
	 *         "ab jetzt" aufgenommen werden.
	 */
	public Date getGueltigBis() {
		return gueltigBis;
	}

	/**
	 * @param gueltigBis
	 *            Falls ein Anmelder nicht mehr existiert, dann kann dieser über das
	 *            Gültigkeitsdatum gesperrt werden. "Gültig ab" ist nicht erforderlich, da Anmelder
	 *            nur mit Gültigkeit "ab jetzt" aufgenommen werden.
	 */
	public void setGueltigBis(Date gueltigBis) {
		this.gueltigBis = gueltigBis;
	}

	/**
	 * @return sagt aus, ob der Anmelder aus dem Bereich "Koordination Betrieb – Bau" stammt. Das
	 *         Attribut dient der statistischen Auswertung bei "Verursacher".
	 */
	public boolean isBpl() {
		return bpl;
	}

	/**
	 * @param bpl
	 *            sagt aus, ob der Anmelder aus dem Bereich "Koordination Betrieb – Bau" stammt. Das
	 *            Attribut dient der statistischen Auswertung bei "Verursacher".
	 */
	public void setBpl(boolean bpl) {
		this.bpl = bpl;
	}

	/**
	 * @return dient für den "Anmelder/Verursacher" des Inhalts "Eingabefehler" und damit der
	 *         Verhinderung der statistischen Erfassung von Eingabefehlern als durch echte
	 *         Verursacher ausgelöste Änderung.
	 */
	public boolean isFault() {
		return fault;
	}

	/**
	 * @param fault
	 *            dient für den "Anmelder/Verursacher" des Inhalts "Eingabefehler" und damit der
	 *            Verhinderung der statistischen Erfassung von Eingabefehlern als durch echte
	 *            Verursacher ausgelöste Änderung.
	 */
	public void setFault(boolean fault) {
		this.fault = fault;
	}

	public Set<Anmeldergruppe> getAnmeldergruppe() {
		return anmeldergruppe;
	}

	public void setAnmeldergruppe(Set<Anmeldergruppe> anmeldergruppe) {
		this.anmeldergruppe = anmeldergruppe;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isAuftraggeber() {
		return auftraggeber;
	}

	public void setAuftraggeber(boolean auftraggeber) {
		this.auftraggeber = auftraggeber;
	}

	public String getCaption() {
		if (getUser() != null)
			return String.format("%s - %s", name, user.getCaption());

		return name;
	}
}