package db.training.bob.service.report;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.SortedSet;
import java.util.TreeSet;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import db.training.bob.model.Art;
import db.training.bob.model.SearchBean;
import db.training.bob.util.DateUtils;
import db.training.easy.util.EasyDateFormat;
import db.training.easy.util.FrontendHelper;
import db.training.logwrapper.Logger;

/**
 * A bunch of util-Methods to help to formulate
 * searches (Crterias) from SearchBeans 
 * 
 * @author EckhardJost
 *
 */
public class SearchBeanHelper {
	private static Logger log = Logger.getLogger(SearchBeanHelper.class);
	
	/**
	 * Erstellt das Suchkriterium für die Zeitraumsuche und gibt dieses zurück. Die Intervallgrenzen
	 * sind im Suchergebnis eingeschlossen.
	 * 
	 * @param propertyVon
	 *            Name des Properties, dass das Enddatum beschreibt
	 * @param von
	 *            untere Grenze des Suchintervalls
	 * @param propertyBis
	 *            Name des Properties, dass das Ende beschreibt
	 * @param bis
	 *            obere Grenze des Suchintervalls
	 * @return Hibernate Suchkriterium. Kann <c>null</c> zurückgeben.
	 */
	public static Criterion getZeitraumCriteria(String propertyVon, Date von, String propertyBis, Date bis) {
		assert (propertyVon != null);
		assert (propertyBis != null);

		Criterion criterion = null;
		Calendar cal = Calendar.getInstance(Locale.GERMANY);

		if (von != null) {
			cal.setTime(von);
			EasyDateFormat.setToStartOfDay(cal);
			von = cal.getTime();
		}
		if (bis != null) {
			cal.setTime(bis);
			EasyDateFormat.setToEndOfDay(cal);
			bis = cal.getTime();
		}
		if (von != null && bis != null) {
			// beide Grenzen des Suchintervalls sind gesetzt
			if (log.isDebugEnabled())
				log.debug("Suche in Aktiven Zeitraeumen. (von UND bis)");

			Junction junction = Restrictions.conjunction();
			junction.add(Restrictions.le(propertyVon, bis));
			junction.add(Restrictions.ge(propertyBis, von));
			criterion = junction;

		} else {
			if (log.isDebugEnabled())
				log.debug("Suche in Aktiven Zeitraeumen. (von XODER bis)");
			log.debug("von: " + von);
			log.debug("bis: " + bis);
			if (von != null) {
				criterion = Restrictions.ge(propertyBis, von);
			}
			if (bis != null) {
				criterion = Restrictions.le(propertyVon, bis);
			}
		}

		return criterion;
	}
	
	/**
	 * Erstellt die Ordnungskriterien für ein Suche nach Baumassnahmen aus der übergebenen
	 * SearchBean.
	 * 
	 * @param criteria Die Suche nach den Baumassnahmen.
	 * @param sortOrders Die zu ergänzenden Suchkriterien für eine geordnete Rückgabe der Ergebnisse.
	 * @param searchBean Die gesamten Suchkriterien der zu erstellenden Suche. 
	 * @return
	 */
	public static Criteria applyBaumassnahmeOrders(Criteria criteria, Collection<Order> sortOrders,
			SearchBean searchBean) {
		if (criteria != null && sortOrders != null) {
			for (Order order : sortOrders) {
				if (order.toString().indexOf("regionalBereichFpl") != -1) {
					criteria.createAlias("regionalBereichFpl", "regionalBereichFpl",
							CriteriaSpecification.LEFT_JOIN);
				}
				if (order.toString().indexOf("baubetriebsplanung") != -1 && searchBean == null) {
					criteria.createAlias("baubetriebsplanung", "baubetriebsplanung",
							CriteriaSpecification.INNER_JOIN);
				}
				criteria.addOrder(order);
			}
		}
		return criteria;
	}
	
	/**
	 * Erstellt eine Suchabfrage aus Daten einer Baumassnahme, die in der SearchBean uebergeben werden.
	 * 
	 */
	public static Criteria fillCriteriaFromBaumassnahmeSearchBean(Criteria criteria, SearchBean searchBean) {
		assert (criteria != null);
		assert (searchBean != null);

		SortedSet<String> joinedTables = new TreeSet<String>();

		// Eine Baumassnahme gilt als ausgefallen, wenn das Feld "Ausfalldatum"
		// gefuellt ist
		if (searchBean.getSearchAusfaelle() != null) {
			if (searchBean.getSearchAusfaelle()) {
				criteria.add(Restrictions.isNotNull("ausfallDatum"));
			} else {
				criteria.add(Restrictions.isNull("ausfallDatum"));
			}
		}
		// Eine Baumassnahme wurde "veraendert", wenn im Reiter
		// "Aenderungsdokumentation" mindestens
		// ein Eintrag vorhanden ist
		if (searchBean.getSearchAenderungen() != null) {
			if (searchBean.getSearchAenderungen()) {
				criteria.add(Restrictions.isNotEmpty("aenderungen"));
			} else {
				criteria.add(Restrictions.isEmpty("aenderungen"));
			}
		}
		// Korridorzeitfenster
		String value = searchBean.getSearchKorridorZeitfenster();
		if ((value != null) && !(value.trim().equals(""))) {
			criteria
			.add(Restrictions
					.sqlRestriction("this_.id IN (SELECT bm_id FROM bm_korridorzeitfenster WHERE UPPER(korridorzeitfenster) like UPPER('"
							+ value + "'))"));
		}
		// masId
		value = searchBean.getSearchMasId();
		if ((value != null) && !(value.trim().equals(""))) {
			Junction masIdDisjunction = Restrictions.disjunction();
			criteria.createAlias("hauptBbpMassnahme", "hauptBbpMassnahme",
					CriteriaSpecification.LEFT_JOIN);
			joinedTables.add("hauptBbpMassnahme");
			masIdDisjunction.add(Restrictions.ilike("hauptBbpMassnahme.masId", value,
					MatchMode.ANYWHERE));

			criteria.createAlias("bbpMassnahmen", "bbpMassnahmen", CriteriaSpecification.LEFT_JOIN);
			joinedTables.add("bbpMassnahmen");
			masIdDisjunction.add(Restrictions.ilike("bbpMassnahmen.masId", value,
					MatchMode.ANYWHERE));
			criteria.add(masIdDisjunction);
		}

		value = searchBean.getSearchStreckeBBP();
		if ((value != null) && !(value.equals(""))) {
			criteria.add(Restrictions.ilike("streckeBBP", value, MatchMode.ANYWHERE));
		}

		value = searchBean.getSearchStreckeVZG();
		if ((value != null) && !(value.equals(""))) {
			criteria.add(Restrictions.ilike("streckeVZG", value, MatchMode.ANYWHERE));
		}

		value = searchBean.getSearchStreckenAbschnitt();
		if ((value != null) && !(value.equals(""))) {
			criteria.add(Restrictions.ilike("streckenAbschnitt", value, MatchMode.ANYWHERE));
		}

		if (FrontendHelper.stringNotNullOrEmpty(searchBean.getVorgangsNummer())) {
			// da die Vorgangsnummer in der Datenbank als Integer gespeichert
			// wird, muss bei der
			// Abfrage ein type-cast durchgeführt werden, um eine like-Abfrage
			// darauf ausführen zu
			// können.
			String param = searchBean.getVorgangsNummer().concat("%");
			criteria.add(Restrictions.sqlRestriction("cast(this_.vorgangsNr as char(6)) like ?",
					param, Hibernate.STRING));
		}

		value = searchBean.getSearchKigBauNr();
		if ((value != null) && !(value.equals(""))) {
			// Abfrage der Werte mit SQLQuery führt zu Ergebnisliste mit
			// BigDecimals, welche nicht
			// in SQL-Integer konvertiert werden können, mit sqlRestriction
			// funktioniert das
			criteria
			.add(Restrictions
					.sqlRestriction("this_.id IN (SELECT bm_id FROM bm_kigbaunr WHERE UPPER(kigBauNr) like UPPER('%"
							+ value + "%'))"));
		}

		value = searchBean.getSearchKorridorNr();
		if ((value != null) && !(value.trim().equals(""))) {
			try {
				criteria.add(Restrictions.eq("korridorNr", Integer.parseInt(value)));
			} catch (NumberFormatException e) {
				//
			}
		}

		value = searchBean.getSearchQsNr();
		if ((value != null) && !(value.trim().equals(""))) {
			criteria
			.add(Restrictions
					.sqlRestriction("this_.id IN (SELECT bm_ID FROM bm_qsNr WHERE UPPER(qsnr) like UPPER('%"
							+ value + "%'))"));
		}

		value = searchBean.getSearchRegionalbereichFpl();
		if ((value != null) && !(value.trim().equals(""))) {
			int regionalbereichId = Integer.parseInt(value);
			criteria.add(Restrictions.eq("regionalBereichFpl.id", regionalbereichId));
		}

		value = searchBean.getSearchRegionalbereichBM();
		if ((value != null) && !(value.trim().equals(""))) {
			criteria.add(Restrictions.eq("regionalbereichBM", value));
		}

		// Suche nach Art. Allgemeine SearchArt und detailierte SearchArtX darf
		// nicht
		// gemeinsam verwendet werden
		value = searchBean.getSearchArt();
		if ((value != null) && !(value.trim().equals(""))) {
			criteria.add(Restrictions.eq("art", Art.valueOf(value)));
		} else {
			if ((searchBean.getSearchArtA() != null && searchBean.getSearchArtA().equals(true))
					|| (searchBean.getSearchArtB() != null && searchBean.getSearchArtB().equals(true))
					|| (searchBean.getSearchArtKs() != null && searchBean.getSearchArtKs().equals(true))
					|| (searchBean.getSearchArtQs() != null && searchBean.getSearchArtQs().equals(true))) {
				Junction artDisjunction = Restrictions.disjunction();
				if (searchBean.getSearchArtA() != null && searchBean.getSearchArtA().equals(true)) {
					artDisjunction.add(Restrictions.eq("art", Art.A));
				}
				if (searchBean.getSearchArtB() != null && searchBean.getSearchArtB().equals(true)) {
					artDisjunction.add(Restrictions.eq("art", Art.B));
				}
				if (searchBean.getSearchArtKs() != null && searchBean.getSearchArtKs().equals(true)) {
					artDisjunction.add(Restrictions.eq("art", Art.KS));
				}
				if (searchBean.getSearchArtQs() != null && searchBean.getSearchArtQs().equals(true)) {
					artDisjunction.add(Restrictions.eq("art", Art.QS));
				}
				criteria.add(artDisjunction);
			}
		}

		// Arbeitssteuerung / Meilensteinsuche
		if (searchBean.isViewModeMilestones()) {
			// Aliasnamen erstellen, wenn noch nicht vorhanden
			if (!joinedTables.contains("baubetriebsplanung")) {
				criteria.createAlias("baubetriebsplanung", "baubetriebsplanung",
						CriteriaSpecification.INNER_JOIN);
				joinedTables.add("baubetriebsplanung");
			}
			if (!joinedTables.contains("gevus")) {
				criteria.createAlias("gevus", "gevuTermin", CriteriaSpecification.LEFT_JOIN);
				joinedTables.add("gevus");
			}
			if (!joinedTables.contains("pevus")) {
				criteria.createAlias("pevus", "pevuTermin", CriteriaSpecification.LEFT_JOIN);
				joinedTables.add("pevus");
			}
		}

		if ((searchBean.getSearchMilestones() != null)
				&& (searchBean.getSearchMilestones().length > 0)) {
			String[] milestones = searchBean.getSearchMilestones();

			Date controllingBeginnDatum = FrontendHelper.castStringToDate(searchBean
					.getSearchControllingBeginnDatum());

			Date controllingEndDatum = FrontendHelper.castStringToDate(searchBean
					.getSearchControllingEndDatum());

			if (searchBean.isOptionZeitraum()) {
				// Anzahl Wochen vorher/nachher wird an Stelle des
				// Controllingzeitraumes genutzt,
				Integer letzteXWochen = FrontendHelper.castStringToInteger(searchBean
						.getLetzteXWochen());
				Integer naechsteXWochen = FrontendHelper.castStringToInteger(searchBean
						.getNaechsteXWochen());

				if (letzteXWochen == null) {
					letzteXWochen = 0;
				}
				GregorianCalendar cal = new GregorianCalendar(Locale.GERMANY);
				controllingBeginnDatum = DateUtils.subtractWeeks(cal, letzteXWochen).getTime();
				if (naechsteXWochen == null) {
					naechsteXWochen = 0;
				}
				GregorianCalendar cal2 = new GregorianCalendar(Locale.GERMANY);
				controllingEndDatum = DateUtils.addWeeks(cal2, naechsteXWochen).getTime();
			}

			// Aliasnamen erstellen, wenn noch nicht vorhanden
			if (!joinedTables.contains("baubetriebsplanung")) {
				criteria.createAlias("baubetriebsplanung", "baubetriebsplanung",
						CriteriaSpecification.INNER_JOIN);
				joinedTables.add("baubetriebsplanung");
			}
			if (!joinedTables.contains("gevus")) {
				criteria.createAlias("gevus", "gevuTermin", CriteriaSpecification.LEFT_JOIN);
				joinedTables.add("gevus");
			}
			if (!joinedTables.contains("pevus")) {
				criteria.createAlias("pevus", "pevuTermin", CriteriaSpecification.LEFT_JOIN);
				joinedTables.add("pevus");
			}

			// alle Meilensteine (SOLL Termine) werden mit ODER verknüpft,
			// mind. ein Meilenstein muss im Suchzeitraum liegen
			// anhand des IST Termins wird der Bearbeitungsstand eines
			// Meilensteins abgefragt
			Junction milestonesDisjunction = Restrictions.disjunction();

			for (String milestone : milestones) {
				// alle Kriterien bzgl. eines Meilensteins werden mit UND
				// verknüpft
				Junction conjunction = Restrictions.conjunction();

				// ////
				// Anforderung BBZR
				if (milestone.equalsIgnoreCase("anforderungbbzr")) {
					if (searchBean.getOnlyOpenMilestones() == Boolean.TRUE) {
						conjunction.add(Restrictions.isNull("baubetriebsplanung.anforderungBBZR"));
						conjunction.add(Restrictions.isNotNull("baubetriebsplanung.id"));
					}

					Criterion c = getZeitraumCriteria("baubetriebsplanung.anforderungBBZRSoll",
							controllingBeginnDatum, "baubetriebsplanung.anforderungBBZRSoll",
							controllingEndDatum);
					if (c != null) {
						conjunction.add(c);
					}

					milestonesDisjunction.add(conjunction);
				}

				// ////
				// BiÜ Entwurf
				else if (milestone.equalsIgnoreCase("biueentwurf")) {
					if (searchBean.getOnlyOpenMilestones() == Boolean.TRUE) {
						conjunction.add(Restrictions.isNull("baubetriebsplanung.biUeEntwurf"));
						conjunction.add(Restrictions.isNotNull("baubetriebsplanung.id"));
					}

					Criterion c = getZeitraumCriteria("baubetriebsplanung.biUeEntwurfSoll",
							controllingBeginnDatum, "baubetriebsplanung.biUeEntwurfSoll",
							controllingEndDatum);
					if (c != null) {
						conjunction.add(c);
					}

					conjunction.add(Restrictions.eq("baubetriebsplanung.biUeEntwurfErforderlich",
							true));
					conjunction.add(Restrictions.ne("art", Art.B));// Meilenstein
					// bei Art=B
					// nicht
					// möglich
					milestonesDisjunction.add(conjunction);
				}

				// ////
				// ZvF Entwurf
				else if (milestone.equalsIgnoreCase("zvfentwurf")) {
					if (searchBean.getOnlyOpenMilestones() == Boolean.TRUE) {
						Disjunction d = Restrictions.disjunction();

						Conjunction c = Restrictions.conjunction();
						c.add(Restrictions.isNull("baubetriebsplanung.zvfEntwurf"));
						c.add(Restrictions.isNotNull("baubetriebsplanung.id"));
						c.add(Restrictions.eq("baubetriebsplanung.zvfEntwurfErforderlich", true));
						d.add(c);

						c = Restrictions.conjunction();
						c.add(Restrictions.isNull("gevuTermin.zvfEntwurf"));
						c.add(Restrictions.isNotNull("gevuTermin.id"));
						c.add(Restrictions.eq("gevuTermin.zvfEntwurfErforderlich", true));
						d.add(c);

						c = Restrictions.conjunction();
						c.add(Restrictions.isNull("pevuTermin.zvfEntwurf"));
						c.add(Restrictions.isNotNull("pevuTermin.id"));
						c.add(Restrictions.eq("pevuTermin.zvfEntwurfErforderlich", true));
						d.add(c);

						conjunction.add(d);
					}

					Criterion c = getZeitraumCriteria("baubetriebsplanung.zvfEntwurfSoll",
							controllingBeginnDatum, "baubetriebsplanung.zvfEntwurfSoll",
							controllingEndDatum);
					if (c != null) {
						conjunction.add(c);
					}

					Disjunction d = Restrictions.disjunction();
					d.add(Restrictions.eq("baubetriebsplanung.zvfEntwurfErforderlich", true));
					d.add(Restrictions.eq("pevuTermin.zvfEntwurfErforderlich", true));
					d.add(Restrictions.eq("gevuTermin.zvfEntwurfErforderlich", true));
					conjunction.add(d);

					milestonesDisjunction.add(conjunction);
				}

				// ////
				// Koordinationsergebnis
				else if (milestone.equalsIgnoreCase("koordinationsergebnis")) {
					if (searchBean.getOnlyOpenMilestones() == Boolean.TRUE) {
						conjunction.add(Restrictions
								.isNull("baubetriebsplanung.koordinationsErgebnis"));
						conjunction.add(Restrictions.isNotNull("baubetriebsplanung.id"));
					}

					Criterion c = getZeitraumCriteria(
							"baubetriebsplanung.koordinationsErgebnisSoll", controllingBeginnDatum,
							"baubetriebsplanung.koordinationsErgebnisSoll", controllingEndDatum);
					if (c != null) {
						conjunction.add(c);
					}

					conjunction.add(Restrictions.eq(
							"baubetriebsplanung.koordinationsergebnisErforderlich", true));
					milestonesDisjunction.add(conjunction);
				}

				// ////
				// Gesamtkonzept BBZR
				else if (milestone.equalsIgnoreCase("gesamtkonzeptbbzr")) {
					if (searchBean.getOnlyOpenMilestones() == Boolean.TRUE) {
						conjunction
						.add(Restrictions.isNull("baubetriebsplanung.gesamtKonzeptBBZR"));
						conjunction.add(Restrictions.isNotNull("baubetriebsplanung.id"));
					}

					Criterion c = getZeitraumCriteria("baubetriebsplanung.gesamtKonzeptBBZRSoll",
							controllingBeginnDatum, "baubetriebsplanung.gesamtKonzeptBBZRSoll",
							controllingEndDatum);
					if (c != null) {
						conjunction.add(c);
					}

					conjunction.add(Restrictions.eq(
							"baubetriebsplanung.gesamtkonzeptBBZRErforderlich", true));
					milestonesDisjunction.add(conjunction);
				}

				// ////
				// BiÜ
				else if (milestone.equalsIgnoreCase("biue")) {
					if (searchBean.getOnlyOpenMilestones() == Boolean.TRUE) {
						conjunction.add(Restrictions.isNull("baubetriebsplanung.biUe"));
						conjunction.add(Restrictions.isNotNull("baubetriebsplanung.id"));
					}

					Criterion c = getZeitraumCriteria("baubetriebsplanung.biUeSoll",
							controllingBeginnDatum, "baubetriebsplanung.biUeSoll", controllingEndDatum);
					if (c != null) {
						conjunction.add(c);
					}

					conjunction.add(Restrictions.eq("baubetriebsplanung.biUeErforderlich", true));
					milestonesDisjunction.add(conjunction);
				}

				// ////
				// ZvF
				else if (milestone.equalsIgnoreCase("zvf")) {
					if (searchBean.getOnlyOpenMilestones() == Boolean.TRUE) {
						Disjunction d = Restrictions.disjunction();

						Conjunction c = Restrictions.conjunction();
						c.add(Restrictions.isNull("baubetriebsplanung.zvf"));
						c.add(Restrictions.isNotNull("baubetriebsplanung.id"));
						c.add(Restrictions.eq("baubetriebsplanung.zvFErforderlich", true));
						d.add(c);

						c = Restrictions.conjunction();
						c.add(Restrictions.isNull("gevuTermin.zvF"));
						c.add(Restrictions.isNotNull("gevuTermin.id"));
						c.add(Restrictions.eq("gevuTermin.zvfErforderlich", true));
						d.add(c);

						c = Restrictions.conjunction();
						c.add(Restrictions.isNull("pevuTermin.zvF"));
						c.add(Restrictions.isNotNull("pevuTermin.id"));
						c.add(Restrictions.eq("pevuTermin.zvfErforderlich", true));
						d.add(c);

						conjunction.add(d);
					}

					Criterion c = getZeitraumCriteria("baubetriebsplanung.zvfSoll",
							controllingBeginnDatum, "baubetriebsplanung.zvfSoll", controllingEndDatum);
					if (c != null) {
						conjunction.add(c);
					}

					Disjunction d = Restrictions.disjunction();
					d.add(Restrictions.eq("baubetriebsplanung.zvFErforderlich", true));
					d.add(Restrictions.eq("pevuTermin.zvfErforderlich", true));
					d.add(Restrictions.eq("gevuTermin.zvfErforderlich", true));
					conjunction.add(d);

					milestonesDisjunction.add(conjunction);
				}

				// ////
				// Master ÜB-GV
				else if (milestone.equalsIgnoreCase("masterUebergabeblattGV")) {
					if (searchBean.getOnlyOpenMilestones() == Boolean.TRUE) {
						conjunction.add(Restrictions.isNull("gevuTermin.masterUebergabeblattGV"));
						conjunction.add(Restrictions.isNotNull("gevuTermin.id"));
					}

					Criterion c = getZeitraumCriteria("gevuTermin.masterUebergabeblattGVSoll",
							controllingBeginnDatum, "gevuTermin.masterUebergabeblattGVSoll",
							controllingEndDatum);
					if (c != null) {
						conjunction.add(c);
					}

					conjunction.add(Restrictions.eq(
							"gevuTermin.masterUebergabeblattGVErforderlich", true));
					milestonesDisjunction.add(conjunction);
				}

				// ////
				// ÜB-GV
				else if (milestone.equalsIgnoreCase("uebergabeblattGV")) {
					if (searchBean.getOnlyOpenMilestones() == Boolean.TRUE) {
						conjunction.add(Restrictions.isNull("gevuTermin.uebergabeblattGV"));
						conjunction.add(Restrictions.isNotNull("gevuTermin.id"));
					}

					Criterion c = getZeitraumCriteria("gevuTermin.uebergabeblattGVSoll",
							controllingBeginnDatum, "gevuTermin.uebergabeblattGVSoll",
							controllingEndDatum);
					if (c != null) {
						conjunction.add(c);
					}

					conjunction.add(Restrictions
							.eq("gevuTermin.uebergabeblattGVErforderlich", true));
					milestonesDisjunction.add(conjunction);
				}

				// ////
				// Master ÜB-PV
				else if (milestone.equalsIgnoreCase("masterUebergabeblattPV")) {
					if (searchBean.getOnlyOpenMilestones() == Boolean.TRUE) {
						conjunction.add(Restrictions.isNull("pevuTermin.masterUebergabeblattPV"));
						conjunction.add(Restrictions.isNotNull("pevuTermin.id"));
					}

					Criterion c = getZeitraumCriteria("pevuTermin.masterUebergabeblattPVSoll",
							controllingBeginnDatum, "pevuTermin.masterUebergabeblattPVSoll",
							controllingEndDatum);
					if (c != null) {
						conjunction.add(c);
					}

					conjunction.add(Restrictions.eq(
							"pevuTermin.masterUebergabeblattPVErforderlich", true));
					milestonesDisjunction.add(conjunction);
				}

				// ////
				// ÜB PV
				else if (milestone.equalsIgnoreCase("uebergabeblattPV")) {
					if (searchBean.getOnlyOpenMilestones() == Boolean.TRUE) {
						conjunction.add(Restrictions.isNull("pevuTermin.uebergabeblattPV"));
						conjunction.add(Restrictions.isNotNull("pevuTermin.id"));
					}

					Criterion c = getZeitraumCriteria("pevuTermin.uebergabeblattPVSoll",
							controllingBeginnDatum, "pevuTermin.uebergabeblattPVSoll",
							controllingEndDatum);
					if (c != null) {
						conjunction.add(c);
					}

					conjunction.add(Restrictions
							.eq("pevuTermin.uebergabeblattPVErforderlich", true));
					milestonesDisjunction.add(conjunction);
				}

				// ////
				// Stellungnahme EVU
				else if (milestone.equalsIgnoreCase("stellungnahmeEVU")) {
					if (searchBean.getOnlyOpenMilestones() == Boolean.TRUE) {
						Disjunction d = Restrictions.disjunction();

						Conjunction c = Restrictions.conjunction();
						c.add(Restrictions.isNull("gevuTermin.stellungnahmeEVU"));
						c.add(Restrictions.isNotNull("gevuTermin.id"));
						c.add(Restrictions.eq("gevuTermin.stellungnahmeEVUErforderlich", true));
						d.add(c);

						c = Restrictions.conjunction();
						c.add(Restrictions.isNull("pevuTermin.stellungnahmeEVU"));
						c.add(Restrictions.isNotNull("pevuTermin.id"));
						c.add(Restrictions.eq("pevuTermin.stellungnahmeEVUErforderlich", true));
						d.add(c);

						conjunction.add(d);
					}

					Disjunction dZeitraum = Restrictions.disjunction();
					Criterion c = getZeitraumCriteria("gevuTermin.stellungnahmeEVUSoll",
							controllingBeginnDatum, "gevuTermin.stellungnahmeEVUSoll",
							controllingEndDatum);
					if (c != null) {
						dZeitraum.add(c);
					}
					c = getZeitraumCriteria("pevuTermin.stellungnahmeEVUSoll",
							controllingBeginnDatum, "pevuTermin.stellungnahmeEVUSoll",
							controllingEndDatum);
					if (c != null) {
						dZeitraum.add(c);
					}
					conjunction.add(dZeitraum);

					milestonesDisjunction.add(conjunction);
				}

				// ////
				// B-Konzept EVU
				else if (milestone.equalsIgnoreCase("bKonzeptEVU")) {
					if (searchBean.getOnlyOpenMilestones() == Boolean.TRUE) {
						conjunction.add(Restrictions.isNull("pevuTermin.bKonzeptEVU"));
						conjunction.add(Restrictions.isNotNull("pevuTermin.id"));
					}

					Criterion c = getZeitraumCriteria("pevuTermin.bKonzeptEVUSoll",
							controllingBeginnDatum, "pevuTermin.bKonzeptEVUSoll", controllingEndDatum);
					if (c != null) {
						conjunction.add(c);
					}

					conjunction.add(Restrictions.eq("pevuTermin.bKonzeptEVUErforderlich", true));
					milestonesDisjunction.add(conjunction);
				}

				// ////
				// Fplo
				else if (milestone.equalsIgnoreCase("fplo")) {
					if (searchBean.getOnlyOpenMilestones() == Boolean.TRUE) {
						Disjunction d = Restrictions.disjunction();

						Conjunction c = Restrictions.conjunction();
						c.add(Restrictions.isNull("gevuTermin.fplo"));
						c.add(Restrictions.isNotNull("gevuTermin.id"));
						c.add(Restrictions.eq("gevuTermin.fploErforderlich", true));
						d.add(c);

						c = Restrictions.conjunction();
						c.add(Restrictions.isNull("pevuTermin.fplo"));
						c.add(Restrictions.isNotNull("pevuTermin.id"));
						c.add(Restrictions.eq("pevuTermin.fploErforderlich", true));
						d.add(c);

						conjunction.add(d);
					}

					Disjunction dZeitraum = Restrictions.disjunction();
					Criterion c = getZeitraumCriteria("gevuTermin.fploSoll",
							controllingBeginnDatum, "gevuTermin.fploSoll", controllingEndDatum);
					if (c != null) {
						dZeitraum.add(c);
					}
					c = getZeitraumCriteria("pevuTermin.fploSoll", controllingBeginnDatum,
							"pevuTermin.fploSoll", controllingEndDatum);
					if (c != null) {
						dZeitraum.add(c);
					}
					conjunction.add(dZeitraum);

					Disjunction d = Restrictions.disjunction();
					d.add(Restrictions.eq("pevuTermin.fploErforderlich", true));
					d.add(Restrictions.eq("gevuTermin.fploErforderlich", true));
					conjunction.add(d);

					milestonesDisjunction.add(conjunction);
				}
				// ////
				// Eingabe GFDZ
				else if (milestone.equalsIgnoreCase("eingabegfd_z")) {
					if (searchBean.getOnlyOpenMilestones() == Boolean.TRUE) {
						Disjunction d = Restrictions.disjunction();

						Conjunction c = Restrictions.conjunction();
						c.add(Restrictions.isNull("gevuTermin.eingabeGFD_Z"));
						c.add(Restrictions.isNotNull("gevuTermin.id"));
						c.add(Restrictions.eq("gevuTermin.eingabeGFD_ZErforderlich", true));
						d.add(c);

						c = Restrictions.conjunction();
						c.add(Restrictions.isNull("pevuTermin.eingabeGFD_Z"));
						c.add(Restrictions.isNotNull("pevuTermin.id"));
						c.add(Restrictions.eq("pevuTermin.eingabeGFD_ZErforderlich", true));
						d.add(c);

						conjunction.add(d);
					}

					Disjunction dZeitraum = Restrictions.disjunction();
					Criterion c = getZeitraumCriteria("gevuTermin.eingabeGFD_ZSoll",
							controllingBeginnDatum, "gevuTermin.eingabeGFD_ZSoll", controllingEndDatum);
					if (c != null) {
						dZeitraum.add(c);
					}
					c = getZeitraumCriteria("pevuTermin.eingabeGFD_ZSoll", controllingBeginnDatum,
							"pevuTermin.eingabeGFD_ZSoll", controllingEndDatum);
					if (c != null) {
						dZeitraum.add(c);
					}
					conjunction.add(dZeitraum);

					Disjunction d = Restrictions.disjunction();
					d.add(Restrictions.eq("pevuTermin.eingabeGFD_ZErforderlich", true));
					d.add(Restrictions.eq("gevuTermin.eingabeGFD_ZErforderlich", true));
					conjunction.add(d);

					milestonesDisjunction.add(conjunction);
				}
			}
			criteria.add(milestonesDisjunction);

		}

		if (searchBean.getNurAktiv() == Boolean.TRUE) {
			// Terminsuche bezgl. der der Regelungen einer Baumaßnahme
			//
			// Es werden nur Baumaßnahmen gesucht, bei denen im Suchintervall
			// eine Regelung
			// existiert. Pausen zwischen Regelungen werden nicht beachtet.
			criteria.createAlias("bbpMassnahmen", "bbp", CriteriaSpecification.INNER_JOIN);
			criteria.createAlias("bbp.regelungen", "rgl", CriteriaSpecification.INNER_JOIN);

			Criterion c = getZeitraumCriteria("rgl.beginn",
					FrontendHelper.castStringToDate(searchBean.getBauZeitraumVon()), "rgl.ende",
					FrontendHelper.castStringToDate(searchBean.getBauZeitraumBis()));
			if (c != null) {
				criteria.add(c);
			}
		} else {
			// Terminsuche bezgl. des Bauzeitraums
			/* nurAktiv == NULL || nurAktiv == Boolean.FALSE */
			Criterion c = getZeitraumCriteria("beginnDatum",
					FrontendHelper.castStringToDate(searchBean.getBauZeitraumVon()), "endDatum",
					FrontendHelper.castStringToDate(searchBean.getBauZeitraumBis()));
			if (c != null) {
				criteria.add(c);
			}
		}

		if (searchBean.getSearchFahrplanjahr() != null && searchBean.getSearchFahrplanjahr() != 0) {
			criteria.add(Restrictions.eq("fahrplanjahr", searchBean.getSearchFahrplanjahr()));
		}

		// Suche nach Baubeginn: Von
		value = searchBean.getSearchBeginnDatum();
		if ((value != null) && !(value.equals(""))) {
			Date beginnDatum = FrontendHelper.castStringToDate(value);
			if (beginnDatum != null) {
				criteria.add(Restrictions.ge("beginnDatum", beginnDatum));
			}
		}

		// Suche nach Baubeginn: Bis
		value = searchBean.getSearchEndDatum();
		if ((value != null) && !(value.trim().equals(""))) {
			Date endDatum = FrontendHelper.castStringToDate(value);
			if (endDatum != null) {
				criteria.add(Restrictions.le("beginnDatum", endDatum));
			}
		}

		value = searchBean.getSearchBearbeitungsbereich();
		if ((value != null) && !(value.trim().equals(""))) {
			int bearbeitungsbereichId = Integer.parseInt(value);
			criteria.add(Restrictions.eq("bearbeitungsbereich.id", bearbeitungsbereichId));
		}

		value = searchBean.getSearchFploNr();
		if ((value != null) && !(value.equals(""))) {
			criteria.add(Restrictions.ilike("fploNr", value, MatchMode.ANYWHERE));
		}

		Integer intValue = searchBean.getSearchBearbeiter();
		if (intValue != null) {
			// Conjunction c = Restrictions.conjunction();
			// c.add(Restrictions.eq("bearbeiter.aktiv", true));
			if (intValue != 0) {
				criteria.createAlias("bearbeiter", "bearbeiterAlias",
						CriteriaSpecification.INNER_JOIN);
				criteria.add(Restrictions.eq("bearbeiterAlias.aktiv", true));
				criteria.add(Restrictions.eq("bearbeiterAlias.user.id", intValue));
			}
			// SQLQuery qry =getCurrentSession().createSQLQuery("SELECT
			// baumassnahme_id FROM
			// baumassnahme_kigbaunr WHERE kigBauNr like '%"+ value +"%'");
			// criteria.add(Restrictions.in("id", qry.list()));
			// criteria.createCriteria("bearbeiter").add(Restrictions.ilike("name",
			// value));
			// }

			// Arbeitssteuerung:
			// String[] milestones = searchBean.getSearchMilestones();
			// if ((milestones != null) && (milestones.length > 0)) {
			// @SuppressWarnings("unused")
			// String firstMilestone = milestones[0];
			// @SuppressWarnings("unused")
			// String lastMilestone = milestones[milestones.length - 1];
			//
		}

		return criteria;
	}
}
