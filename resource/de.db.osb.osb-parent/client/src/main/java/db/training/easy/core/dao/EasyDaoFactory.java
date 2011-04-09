package db.training.easy.core.dao;

import org.springframework.web.context.WebApplicationContext;

import db.training.bob.dao.AenderungDaoImpl;
import db.training.bob.dao.ArbeitsleistungRegionalbereichDaoImpl;
import db.training.bob.dao.BBPMassnahmeDaoImpl;
import db.training.bob.dao.BaumassnahmeDaoImpl;
import db.training.bob.dao.BearbeiterDaoImpl;
import db.training.bob.dao.BearbeitungsbereichDaoImpl;
import db.training.bob.dao.EVUDaoImpl;
import db.training.bob.dao.EVUGruppeDaoImpl;
import db.training.bob.dao.GrundDaoImpl;
import db.training.bob.dao.MeilensteinDaoImpl;
import db.training.bob.dao.NachbarbahnDaoImpl;
import db.training.bob.dao.RegelungDaoImpl;
import db.training.bob.dao.RegionalbereichDaoImpl;
import db.training.bob.dao.SearchConfigDaoImpl;
import db.training.bob.dao.TerminUebersichtBaubetriebsplanungDaoImpl;
import db.training.bob.dao.TerminUebersichtDaoImpl;
import db.training.bob.dao.TerminUebersichtGueterverkehrsEVUDaoImpl;
import db.training.bob.dao.TerminUebersichtPersonenverkehrsEVUDaoImpl;
import db.training.bob.dao.fplo.FahrplanDaoImpl;
import db.training.bob.dao.fplo.ZugcharakteristikDaoImpl;
import db.training.bob.dao.zvf.BBPStreckeDaoImpl;
import db.training.bob.dao.zvf.BahnhofDaoImpl;
import db.training.bob.dao.zvf.KnotenzeitDaoImpl;
import db.training.bob.dao.zvf.StreckeDaoImpl;
import db.training.bob.dao.zvf.UebergabeblattDaoImpl;
import db.training.bob.dao.zvf.ZugDaoImpl;
import db.training.hibernate.history.HistoryLogEntryDao;
import db.training.osb.dao.AnmelderDaoImpl;
import db.training.osb.dao.ArbeitstypDaoImpl;
import db.training.osb.dao.BaustelleDaoImpl;
import db.training.osb.dao.BetriebsstelleDaoImpl;
import db.training.osb.dao.BetriebsweiseDaoImpl;
import db.training.osb.dao.BuendelDaoImpl;
import db.training.osb.dao.FahrplanregelungDaoImpl;
import db.training.osb.dao.FinanztypDaoImpl;
import db.training.osb.dao.FolgeNichtausfuehrungDaoImpl;
import db.training.osb.dao.GleissperrungDaoImpl;
import db.training.osb.dao.GrossmaschineDaoImpl;
import db.training.osb.dao.KorridorDaoImpl;
import db.training.osb.dao.LangsamfahrstelleDaoImpl;
import db.training.osb.dao.MasterBuendelDaoImpl;
import db.training.osb.dao.OberleitungDaoImpl;
import db.training.osb.dao.PaketDaoImpl;
import db.training.osb.dao.PhaseDaoImpl;
import db.training.osb.dao.SAPMassnahmeDaoImpl;
import db.training.osb.dao.SqlQueryDaoImpl;
import db.training.osb.dao.StreckenbandZeileDaoImpl;
import db.training.osb.dao.TopProjektDaoImpl;
import db.training.osb.dao.UmleitungDaoImpl;
import db.training.osb.dao.UmleitungFahrplanregelungLinkDaoImpl;
import db.training.osb.dao.UmleitungswegDaoImpl;
import db.training.osb.dao.VerkehrstageregelungDaoImpl;
import db.training.osb.dao.VzgStreckeDaoImpl;

/**
 * Factory die Dao Objects erzeugt
 * 
 * @author hennebrueder
 * 
 */
public class EasyDaoFactory {

	private WebApplicationContext wac;

	private static EasyDaoFactory easyDaoFactory = new EasyDaoFactory();

	protected EasyDaoFactory() {

	}

	public static EasyDaoFactory getInstance() {
		return easyDaoFactory;
	}

	/**
	 * sollte von einem StartUpListener der Anwendung aufgerufen werden.
	 * 
	 * @param webApplicationContext
	 */
	public void setWebApplicationContext(WebApplicationContext webApplicationContext) {
		this.wac = webApplicationContext;
	}

	public BaumassnahmeDaoImpl getBaumassnahmeDao() {
		return (BaumassnahmeDaoImpl) wac.getBean("baumassnahmeDao");
	}

	public RegionalbereichDaoImpl getRegionalbereichDao() {
		return (RegionalbereichDaoImpl) wac.getBean("regionalbereichDao");
	}

	public UserDaoImpl getUserDao() {
		return (UserDaoImpl) wac.getBean("userDao");
	}

	public BearbeitungsbereichDaoImpl getBearbeitungsbereichDao() {
		return (BearbeitungsbereichDaoImpl) wac.getBean("bearbeitungsbereichDao");
	}

	public AenderungDaoImpl getAenderungDao() {
		return (AenderungDaoImpl) wac.getBean("aenderungDao");
	}

	public ArbeitsleistungRegionalbereichDaoImpl getArbeitsleistungRegionalbereichDao() {
		return (ArbeitsleistungRegionalbereichDaoImpl) wac.getBean("benchmarkDao");
	}

	public TerminUebersichtPersonenverkehrsEVUDaoImpl getTerminUebersichtPersonenverkehrsEVUDao() {
		return (TerminUebersichtPersonenverkehrsEVUDaoImpl) wac
		    .getBean("terminUebersichtPersonenverkehrsEVUDao");
	}

	public TerminUebersichtBaubetriebsplanungDaoImpl getTerminUebersichtBaubetriebsplanungDao() {
		return (TerminUebersichtBaubetriebsplanungDaoImpl) wac
		    .getBean("terminUebersichtBaubetriebsplanungDao");
	}

	public TerminUebersichtDaoImpl getTerminUebersichtDao() {
		return (TerminUebersichtDaoImpl) wac.getBean("terminUebersichtDao");
	}

	public TerminUebersichtGueterverkehrsEVUDaoImpl getTerminUebersichtGueterverkehrsEVUDao() {
		return (TerminUebersichtGueterverkehrsEVUDaoImpl) wac
		    .getBean("terminUebersichtGueterverkehrsEVUDao");
	}

	public NachbarbahnDaoImpl getNachbarbahnDao() {
		return (NachbarbahnDaoImpl) wac.getBean("nachbarbahnDao");
	}

	public EVUDaoImpl getEVUDao() {
		return (EVUDaoImpl) wac.getBean("evuDao");
	}
	
	public EVUGruppeDaoImpl getEVUGruppeDao() {
		return (EVUGruppeDaoImpl) wac.getBean("evuGruppeDao");
	}

	public RegelungDaoImpl getRegelungDao() {
		return (RegelungDaoImpl) wac.getBean("regelungDao");
	}

	public BBPMassnahmeDaoImpl getBBPMassnahmeDao() {
		return (BBPMassnahmeDaoImpl) wac.getBean("bbpMassnahmeDao");
	}

	public GrundDaoImpl getGrundDao() {
		return (GrundDaoImpl) wac.getBean("grundDao");
	}

	public UebergabeblattDaoImpl getUebergabeblattDao() {
		return (UebergabeblattDaoImpl) wac.getBean("uebergabeblattDao");
	}

	public ZugDaoImpl getZugDao() {
		return (ZugDaoImpl) wac.getBean("zugDao");
	}

	public BahnhofDaoImpl getBahnhofDao() {
		return (BahnhofDaoImpl) wac.getBean("bahnhofDao");
	}

	public KnotenzeitDaoImpl getKnotenzeitDao() {
		return (KnotenzeitDaoImpl) wac.getBean("knotenzeitDao");
	}

	public StreckeDaoImpl getStreckeDao() {
		return (StreckeDaoImpl) wac.getBean("streckeDao");
	}

	public SAPMassnahmeDaoImpl getSAPMassnahmeDao() {
		return (SAPMassnahmeDaoImpl) wac.getBean("sapMassnahmeDao");
	}

	public PaketDaoImpl getPaketDao() {
		return (PaketDaoImpl) wac.getBean("paketDao");
	}

	public BuendelDaoImpl getBuendelDao() {
		return (BuendelDaoImpl) wac.getBean("buendelDao");
	}

	public VzgStreckeDaoImpl getVzgStreckeDao() {
		return (VzgStreckeDaoImpl) wac.getBean("vzgStreckeDao");
	}

	public SqlQueryDaoImpl getSqlQueryDao() {
		return (SqlQueryDaoImpl) wac.getBean("sqlQueryDao");
	}

	public BetriebsstelleDaoImpl getBetriebsstelleDao() {
		return (BetriebsstelleDaoImpl) wac.getBean("betriebsstelleDao");
	}

	public BetriebsweiseDaoImpl getBetriebsweiseDao() {
		return (BetriebsweiseDaoImpl) wac.getBean("betriebsweiseDao");
	}

	public FahrplanregelungDaoImpl getFahrplanregelungDao() {
		return (FahrplanregelungDaoImpl) wac.getBean("fahrplanregelungDao");
	}

	public UmleitungDaoImpl getUmleitungDao() {
		return (UmleitungDaoImpl) wac.getBean("umleitungDao");
	}

	public UmleitungswegDaoImpl getUmleitungswegDao() {
		return (UmleitungswegDaoImpl) wac.getBean("umleitungswegDao");
	}

	public StreckenbandZeileDaoImpl getStreckenbandZeileDao() {
		return (StreckenbandZeileDaoImpl) wac.getBean("streckenbandZeileDao");
	}

	public UmleitungFahrplanregelungLinkDaoImpl getUmleitungFahrplanregelungLinkDao() {
		return (UmleitungFahrplanregelungLinkDaoImpl) wac.getBean("umleitungFahrplanregelungLinkDao");
	}

	public KorridorDaoImpl getKorridorDao() {
		return (KorridorDaoImpl) wac.getBean("korridorDao");
	}

	public MasterBuendelDaoImpl getMasterBuendelDao() {
		return (MasterBuendelDaoImpl) wac.getBean("masterBuendelDao");
	}

	public TopProjektDaoImpl getTopProjektDao() {
		return (TopProjektDaoImpl) wac.getBean("topProjektDao");
	}

	public BaustelleDaoImpl getBaustelleDao() {
		return (BaustelleDaoImpl) wac.getBean("baustelleDao");
	}

	public GleissperrungDaoImpl getGleissperrungDao() {
		return (GleissperrungDaoImpl) wac.getBean("gleissperrungDao");
	}

	public VerkehrstageregelungDaoImpl getVerkehrstageregelungDao() {
		return (VerkehrstageregelungDaoImpl) wac.getBean("verkehrstageregelungDao");
	}

	public ZugcharakteristikDaoImpl getZugcharakteristikDao() {
		return (ZugcharakteristikDaoImpl) wac.getBean("zugcharakteristikDao");
	}

	public FahrplanDaoImpl getFahrplanDao() {
		return (FahrplanDaoImpl) wac.getBean("fahrplanDao");
	}

	public BBPStreckeDaoImpl getBBPStreckeDao() {
		return (BBPStreckeDaoImpl) wac.getBean("bbpStreckeDao");
	}

	public AnmelderDaoImpl getAnmelderDao() {
		return (AnmelderDaoImpl) wac.getBean("anmelderDao");
	}

	public FinanztypDaoImpl getFinanztypDao() {
		return (FinanztypDaoImpl) wac.getBean("finanztypDao");
	}

	public ArbeitstypDaoImpl getArbeitstypDao() {
		return (ArbeitstypDaoImpl) wac.getBean("arbeitstypDao");
	}

	public PhaseDaoImpl getPhaseDao() {
		return (PhaseDaoImpl) wac.getBean("phaseDao");
	}

	public FolgeNichtausfuehrungDaoImpl getFolgeNichtausfuehrungDao() {
		return (FolgeNichtausfuehrungDaoImpl) wac.getBean("folgeNichtausfuehrungDao");
	}

	public GrossmaschineDaoImpl getGrossmaschineDao() {
		return (GrossmaschineDaoImpl) wac.getBean("grossmachineDao");
	}

	public OberleitungDaoImpl getOberleitungDao() {
		return (OberleitungDaoImpl) wac.getBean("oberleitungDao");
	}

	public LangsamfahrstelleDaoImpl getLangsamfahrstelleDao() {
		return (LangsamfahrstelleDaoImpl) wac.getBean("langsamfahrstelleDao");
	}

	public MeilensteinDaoImpl getMeilensteinDao() {
		return (MeilensteinDaoImpl) wac.getBean("meilensteinDao");
	}

	public BearbeiterDaoImpl getBearbeiterDao() {
		return (BearbeiterDaoImpl) wac.getBean("bearbeiterDao");
	}

	public SearchConfigDaoImpl getSearchConfigDao() {
		return (SearchConfigDaoImpl) wac.getBean("searchConfigDao");
	}

	public HistoryLogEntryDao getHistoryLogEntryDao() {
		return (HistoryLogEntryDao) wac.getBean("historyLogEntryDao");
	}
}