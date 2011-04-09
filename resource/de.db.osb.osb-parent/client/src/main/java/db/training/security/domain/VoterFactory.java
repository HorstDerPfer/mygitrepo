package db.training.security.domain;

import java.util.HashMap;
import java.util.Map;

import db.training.bob.model.Baumassnahme;
import db.training.bob.model.Regionalbereich;
import db.training.bob.model.zvf.Uebergabeblatt;
import db.training.easy.core.model.User;
import db.training.osb.model.Baustelle;
import db.training.osb.model.Buendel;
import db.training.osb.model.Fahrplanregelung;
import db.training.osb.model.Paket;
import db.training.osb.model.Regelung;
import db.training.osb.model.SAPMassnahme;
import db.training.osb.model.SqlQuery;
import db.training.osb.model.Streckenband;
import db.training.osb.model.TopProjekt;
import db.training.osb.model.Umleitung;
import db.training.osb.model.Umleitungsweg;
import db.training.osb.web.sperrpausenbedarf.SperrpausenbedarfListReport;
import db.training.security.hibernate.TqmRole;

/**
 * Factory erzeugt AccessDecisionVoter fuer unterschiedliche model/domain Objekte
 * 
 * @author Sebastian Hennebrueder
 * 
 */
public class VoterFactory {
	private static final String separator = "##";

	private static Map<String, EasyAccessDecisionVoter> voterMap = new HashMap<String, EasyAccessDecisionVoter>();

	static {
		voterMap.put(Baumassnahme.class.toString() + separator + VoterType.ANY,
		    new BaumassnahmeAnyVoter());
		voterMap.put(Baumassnahme.class.toString() + separator + VoterType.NONE,
		    new BaumassnahmeNoneVoter());
		voterMap.put(User.class.toString() + separator + VoterType.NONE, new UserNoneVoter());
		voterMap.put(User.class.toString() + separator + VoterType.ANY, new UserAnyVoter());
		voterMap.put(Uebergabeblatt.class.toString() + separator + VoterType.NONE,
		    new UebergabeblattNoneVoter());
		voterMap.put(Uebergabeblatt.class.toString() + separator + VoterType.ANY,
		    new UebergabeblattAnyVoter());
		voterMap.put(Streckenband.class.toString() + separator + VoterType.ANY,
		    new StreckenbandAnyVoter());
		voterMap.put(Streckenband.class.toString() + separator + VoterType.NONE,
		    new StreckenbandNoneVoter());
		voterMap.put(SAPMassnahme.class.toString() + separator + VoterType.ANY,
		    new SAPMassnahmeAnyVoter());
		voterMap.put(SAPMassnahme.class.toString() + separator + VoterType.NONE,
		    new SAPMassnahmeNoneVoter());
		voterMap.put(SperrpausenbedarfListReport.class.toString() + separator + VoterType.ANY,
		    new SAPMassnahmeAnyVoter());
		voterMap.put(SperrpausenbedarfListReport.class.toString() + separator + VoterType.NONE,
		    new SAPMassnahmeNoneVoter());

		voterMap.put(Paket.class.toString() + separator + VoterType.ANY, new PaketAnyVoter());
		voterMap.put(Buendel.class.toString() + separator + VoterType.ANY, new BuendelAnyVoter());
		voterMap.put(Fahrplanregelung.class.toString() + separator + VoterType.ANY,
		    new FahrplanregelungAnyVoter());
		voterMap.put(Umleitung.class.toString() + separator + VoterType.ANY,
		    new UmleitungAnyVoter());
		voterMap.put(Umleitungsweg.class.toString() + separator + VoterType.ANY,
		    new UmleitungswegAnyVoter());
		voterMap.put(TopProjekt.class.toString() + separator + VoterType.ANY,
		    new TopProjektAnyVoter());
		voterMap.put(Baustelle.class.toString() + separator + VoterType.ANY,
		    new BaustelleAnyVoter());
		voterMap.put(Regelung.class.toString() + separator + VoterType.ANY, new RegelungAnyVoter());
		voterMap.put(Regelung.class.toString() + separator + VoterType.NONE,
		    new RegelungNoneVoter());
		voterMap.put(TqmRole.class.toString() + separator + VoterType.ANY, new TqmRoleAnyVoter());
		voterMap.put(TqmRole.class.toString() + separator + VoterType.NONE, new TqmRoleNoneVoter());
		voterMap.put(Regionalbereich.class.toString() + separator + VoterType.ANY,
		    new RegionalbereichAnyVoter());
		voterMap.put(Regionalbereich.class.toString() + separator + VoterType.NONE,
		    new RegionalbereichNoneVoter());
		voterMap.put(SqlQuery.class.toString() + separator + VoterType.ANY, new SqlQueryAnyVoter());
		voterMap.put(SqlQuery.class.toString() + separator + VoterType.NONE,
		    new SqlQueryNoneVoter());
	}

	public static EasyAccessDecisionVoter getDecisionVoter(Class<?> modelClass, VoterType voterType) {
		String[] clazz = modelClass.toString().split("\\$\\$EnhancerByCGLIB\\$\\$");
		String key = String.format("%s%s%s", clazz[0], separator, voterType);

		if (!voterMap.containsKey(key)) {
			// prüfen, ob für ein Voter für eine Oberklasse existiert
			key = String.format("%s%s%s", modelClass.getSuperclass().toString(), separator,
			    voterType);
		}

		return voterMap.get(key);
	}
}
