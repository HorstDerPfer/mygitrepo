package db.training.bob.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import db.training.hibernate.history.Historizable;

@SuppressWarnings("serial")
@Entity
@Table(name = "zvfvorplanfahrt")
public class ZvFVorplanfahrt extends ZvF implements Historizable {

	@Column(name = "zeitVorPlan")
	private Integer zeitVorPlan;

	@Column(name = "vorPlanAbBetriebsstelle")
	private String vorPlanAbBetriebsstelle;

	/**
	 * @return the zeitVorPlan
	 */
	public Integer getZeitVorPlan() {
		return zeitVorPlan;
	}

	/**
	 * @param zeitVorPlan
	 *            the zeitVorPlan to set
	 */
	public void setZeitVorPlan(Integer zeitVorPlan) {
		this.zeitVorPlan = zeitVorPlan;
	}

	/**
	 * @return the vorPlanAbBetriebsstelle
	 */
	public String getVorPlanAbBetriebsstelle() {
		return vorPlanAbBetriebsstelle;
	}

	/**
	 * @param vorPlanAbBetriebsstelle
	 *            the vorPlanAbBetriebsstelle to set
	 */
	public void setVorPlanAbBetriebsstelle(String vorPlanAbBetriebsstelle) {
		this.vorPlanAbBetriebsstelle = vorPlanAbBetriebsstelle;
	}
}
