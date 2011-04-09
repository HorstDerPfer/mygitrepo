package db.training.easy.core.service;

import org.springframework.web.context.WebApplicationContext;

import db.training.bob.service.AenderungService;
import db.training.bob.service.ArbeitsleistungRegionalbereichService;
import db.training.bob.service.BBPMassnahmeService;
import db.training.bob.service.BaumassnahmeService;
import db.training.bob.service.BearbeiterService;
import db.training.bob.service.BearbeitungsbereichService;
import db.training.bob.service.EVUGruppeService;
import db.training.bob.service.EVUService;
import db.training.bob.service.GrundService;
import db.training.bob.service.MeilensteinService;
import db.training.bob.service.NachbarbahnService;
import db.training.bob.service.RegelungService;
import db.training.bob.service.RegionalbereichService;
import db.training.bob.service.SearchConfigService;
import db.training.bob.service.TerminUebersichtBaubetriebsplanungService;
import db.training.bob.service.TerminUebersichtGueterverkehrsEVUService;
import db.training.bob.service.TerminUebersichtPersonenverkehrsEVUService;
import db.training.bob.service.TerminUebersichtService;
import db.training.bob.service.fplo.FahrplanService;
import db.training.bob.service.fplo.ISA_FploService;
import db.training.bob.service.fplo.ISA_ZugService;
import db.training.bob.service.fplo.ZugcharakteristikService;
import db.training.bob.service.report.BbzrVerspaetungsminutenReportService;
import db.training.bob.service.zvf.BBPStreckeService;
import db.training.bob.service.zvf.BahnhofService;
import db.training.bob.service.zvf.BbzrService;
import db.training.bob.service.zvf.KnotenzeitService;
import db.training.bob.service.zvf.StreckeService;
import db.training.bob.service.zvf.UebergabeblattService;
import db.training.bob.service.zvf.ZugService;
import db.training.hibernate.history.HistoryLogEntryService;
import db.training.osb.service.AnkermassnahmeArtService;
import db.training.osb.service.AnmelderService;
import db.training.osb.service.ArbeitstypService;
import db.training.osb.service.BaubetriebsplanService;
import db.training.osb.service.BaustelleService;
import db.training.osb.service.BetriebsstelleService;
import db.training.osb.service.BetriebsweiseService;
import db.training.osb.service.BuendelService;
import db.training.osb.service.FahrplanregelungService;
import db.training.osb.service.FinanztypService;
import db.training.osb.service.FolgeNichtausfuehrungService;
import db.training.osb.service.GleissperrungService;
import db.training.osb.service.GrossmaschineService;
import db.training.osb.service.ImportService;
import db.training.osb.service.KorridorService;
import db.training.osb.service.LangsamfahrstelleService;
import db.training.osb.service.MasterBuendelService;
import db.training.osb.service.OberleitungService;
import db.training.osb.service.PaketService;
import db.training.osb.service.PhaseService;
import db.training.osb.service.SAPMassnahmeService;
import db.training.osb.service.SqlQueryService;
import db.training.osb.service.StreckenbandService;
import db.training.osb.service.TopProjektService;
import db.training.osb.service.UmleitungFahrplanregelungLinkService;
import db.training.osb.service.UmleitungService;
import db.training.osb.service.UmleitungswegService;
import db.training.osb.service.VerkehrstageregelungService;
import db.training.osb.service.VzgStreckeService;
import db.training.security.SecurityAdministrationService;
import db.training.security.SecurityService;
import db.training.security.domain.BaumassnahmeAnyVoter;
import db.training.security.domain.BaumassnahmeNoneVoter;
import db.training.security.domain.BaustelleAnyVoter;
import db.training.security.domain.BuendelAnyVoter;
import db.training.security.domain.FahrplanregelungAnyVoter;
import db.training.security.domain.PaketAnyVoter;
import db.training.security.domain.RegelungAnyVoter;
import db.training.security.domain.SAPMassnahmeAnyVoter;
import db.training.security.domain.StreckenbandAnyVoter;
import db.training.security.domain.TopProjektAnyVoter;
import db.training.security.domain.UebergabeblattAnyVoter;
import db.training.security.domain.UebergabeblattNoneVoter;
import db.training.security.domain.UmleitungAnyVoter;
import db.training.security.domain.UmleitungswegAnyVoter;
import db.training.security.domain.UserAnyVoter;
import db.training.security.domain.UserNoneVoter;
import db.training.security.hibernate.SecurityServiceImpl;

/**
 * erzeugt Service Objekte und verwendet, falls erforderlich, den Spring Context.
 * 
 * @author Sebastian Hennebrueder
 * 
 */
public class EasyServiceFactory {

	private WebApplicationContext wac;

	private static EasyServiceFactory easyServiceFactory = new EasyServiceFactory();

	protected EasyServiceFactory() {

	}

	public static EasyServiceFactory getInstance() {
		return easyServiceFactory;
	}

	public SecurityService createSecurityService() {
		try {
			return (SecurityService) wac.getBean("securityService");
		} catch (NullPointerException e) {
			return new SecurityServiceImpl();
		}
	}

	public SecurityAdministrationService createSecurityAdministrationService() {
		return (SecurityAdministrationService) wac.getBean("securityAdministrationService");
	}

	public UserService createUserService() {
		return (UserService) wac.getBean("userService");
	}

	public UserAnyVoter createUserAnyVoter() {
		return (UserAnyVoter) wac.getBean("userAnyVoter");
	}

	public UserNoneVoter createUserNoneVoter() {
		return (UserNoneVoter) wac.getBean("userNoneVoter");
	}

	public static BaumassnahmeAnyVoter createBaumassnahmeAnyVoter() {
		return new BaumassnahmeAnyVoter();
	}

	public static BaumassnahmeNoneVoter createBaumassnahmeNoneVoter() {
		return new BaumassnahmeNoneVoter();
	}

	public UebergabeblattAnyVoter createUebergabeblattAnyVoter() {
		return (UebergabeblattAnyVoter) wac.getBean("uebergabeblattAnyVoter");
	}

	public UebergabeblattNoneVoter createUebergabeblattNoneVoter() {
		return (UebergabeblattNoneVoter) wac.getBean("uebergabeblattNoneVoter");
	}

	/**
	 * sollte von einem StartUpListener der Anwendung aufgerufen werden.
	 * 
	 * @param webApplicationContext
	 */
	public void setWebApplicationContext(WebApplicationContext webApplicationContext) {
		this.wac = webApplicationContext;
	}

	public BbzrVerspaetungsminutenReportService createBbzrVerspaetungsminutenReportService() {
		return (BbzrVerspaetungsminutenReportService) wac.getBean("bbzrVerspaetungsminutenReportService");
	}
	
	public BaumassnahmeService createBaumassnahmeService() {
		return (BaumassnahmeService) wac.getBean("baumassnahmeService");
	}

	public BearbeiterService createBearbeiterService() {
		return (BearbeiterService) wac.getBean("bearbeiterService");
	}

	public BearbeitungsbereichService createBearbeitungsbereichService() {
		return (BearbeitungsbereichService) wac.getBean("bearbeitungsbereichService");
	}

	public RegionalbereichService createRegionalbereichService() {
		return (RegionalbereichService) wac.getBean("regionalbereichService");
	}

	public AenderungService createAenderungService() {
		return (AenderungService) wac.getBean("aenderungService");
	}

	public ArbeitsleistungRegionalbereichService createArbeitsleistungRegionalbereichService() {
		return (ArbeitsleistungRegionalbereichService) wac.getBean("benchmarkService");
	}

	public TerminUebersichtService createTerminUebersichtService() {
		return (TerminUebersichtService) wac.getBean("terminUebersichtService");
	}

	public TerminUebersichtPersonenverkehrsEVUService createTerminUebersichtPersonenverkehrsEVUService() {
		return (TerminUebersichtPersonenverkehrsEVUService) wac
		    .getBean("terminUebersichtPersonenverkehrsEVUService");
	}

	public TerminUebersichtGueterverkehrsEVUService createTerminUebersichtGueterverkehrsEVUService() {
		return (TerminUebersichtGueterverkehrsEVUService) wac
		    .getBean("terminUebersichtGueterverkehrsEVUService");
	}

	public TerminUebersichtBaubetriebsplanungService createTerminUebersichtBaubetriebsplanungService() {
		return (TerminUebersichtBaubetriebsplanungService) wac
		    .getBean("terminUebersichtBaubetriebsplanungService");
	}

	public NachbarbahnService createNachbarbahnService() {
		return (NachbarbahnService) wac.getBean("nachbarbahnService");
	}

	public EVUService createEVUService() {
		return (EVUService) wac.getBean("evuService");
	}
	
	public EVUGruppeService createEVUGruppeService() {
		return (EVUGruppeService) wac.getBean("evuGruppeService");
	}

	public RegelungService createRegelungService() {
		return (RegelungService) wac.getBean("regelungService");
	}

	public BBPMassnahmeService createBBPMassnahmeService() {
		return (BBPMassnahmeService) wac.getBean("bbpMassnahmeService");
	}

	public GrundService createGrundService() {
		return (GrundService) wac.getBean("grundService");
	}

	public ZugService createZugService() {
		return (ZugService) wac.getBean("zugService");
	}

	public BahnhofService createBahnhofService() {
		return (BahnhofService) wac.getBean("bahnhofService");
	}

	public KnotenzeitService createKnotenzeitService() {
		return (KnotenzeitService) wac.getBean("knotenzeitService");
	}

	public StreckeService createStreckeService() {
		return (StreckeService) wac.getBean("streckeService");
	}

	public SAPMassnahmeService createSAPMassnahmeService() {
		return (SAPMassnahmeService) wac.getBean("sapMassnahmeService");
	}

	public SAPMassnahmeAnyVoter createSAPMassnahmeAnyVoter() {
		return (SAPMassnahmeAnyVoter) wac.getBean("sapMassnahmeAnyVoter");
	}

	public VzgStreckeService createVzgStreckeService() {
		return (VzgStreckeService) wac.getBean("vzgStreckeService");
	}

	public BetriebsstelleService createBetriebsstelleService() {
		return (BetriebsstelleService) wac.getBean("betriebsstelleService");
	}

	public BetriebsweiseService createBetriebsweiseService() {
		return (BetriebsweiseService) wac.getBean("betriebsweiseService");
	}

	public PaketService createPaketService() {
		return (PaketService) wac.getBean("paketService");
	}

	public PaketAnyVoter createPaketAnyVoter() {
		return (PaketAnyVoter) wac.getBean("paketAnyVoter");
	}

	public BuendelService createBuendelService() {
		return (BuendelService) wac.getBean("buendelService");
	}

	public BuendelAnyVoter createBuendelAnyVoter() {
		return (BuendelAnyVoter) wac.getBean("buendelAnyVoter");
	}

	/**
	 * @return
	 */
	public KorridorService createKorridorService() {
		return (KorridorService) wac.getBean("korridorService");
	}

	/**
	 * @return
	 */
	public MasterBuendelService createMasterBuendelService() {
		return (MasterBuendelService) wac.getBean("masterBuendelService");
	}

	public StreckenbandService createStreckenbandService() {
		return (StreckenbandService) wac.getBean("streckenbandService");
	}

	public StreckenbandAnyVoter createStreckenbandAnyVoter() {
		return (StreckenbandAnyVoter) wac.getBean("streckenbandAnyVoter");
	}

	public FahrplanregelungService createFahrplanregelungService() {
		return (FahrplanregelungService) wac.getBean("fahrplanregelungService");
	}

	public FahrplanregelungAnyVoter createFahrplanregelungAnyVoter() {
		return (FahrplanregelungAnyVoter) wac.getBean("fahrplanregelungAnyVoter");
	}

	public UmleitungService createUmleitungService() {
		return (UmleitungService) wac.getBean("umleitungService");
	}

	public UmleitungAnyVoter createUmleitungAnyVoter() {
		return (UmleitungAnyVoter) wac.getBean("umleitungAnyVoter");
	}

	public UmleitungswegService createUmleitungswegService() {
		return (UmleitungswegService) wac.getBean("umleitungswegService");
	}

	public UmleitungswegAnyVoter createUmleitungswegAnyVoter() {
		return (UmleitungswegAnyVoter) wac.getBean("umleitungswegAnyVoter");
	}

	public UmleitungFahrplanregelungLinkService createUmleitungFahrplanregelungLinkService() {
		return (UmleitungFahrplanregelungLinkService) wac
		    .getBean("umleitungFahrplanregelungLinkService");
	}

	public BbzrService createBbzrService() {
		return (BbzrService) wac.getBean("bbzrService");
	}

	public UebergabeblattService createUebergabeblattService() {
		return (UebergabeblattService) wac.getBean("uebergabeblattService");
	}

	public SqlQueryService createSqlQueryService() {
		return (SqlQueryService) wac.getBean("sqlQueryService");
	}

	public TopProjektService createTopProjektService() {
		return (TopProjektService) wac.getBean("topProjektService");
	}

	public TopProjektAnyVoter createTopProjektAnyVoter() {
		return (TopProjektAnyVoter) wac.getBean("topProjektAnyVoter");
	}

	public ImportService createImportService() {
		return (ImportService) wac.getBean("importService");
	}

	public BaustelleService createBaustelleService() {
		return (BaustelleService) wac.getBean("baustelleService");
	}

	public BaustelleAnyVoter createBaustelleAnyVoter() {
		return (BaustelleAnyVoter) wac.getBean("baustelleAnyVoter");
	}

	public GleissperrungService createGleissperrungService() {
		return (GleissperrungService) wac.getBean("gleissperrungService");
	}

	public LangsamfahrstelleService createLangsamfahrstelleService() {
		return (LangsamfahrstelleService) wac.getBean("langsamfahrstelleService");
	}

	public OberleitungService createOberleitungService() {
		return (OberleitungService) wac.getBean("oberleitungService");
	}

	public RegelungAnyVoter createRegelungAnyVoter() {
		return (RegelungAnyVoter) wac.getBean("regelungAnyVoter");
	}

	public VerkehrstageregelungService createVerkehrstageregelungService() {
		return (VerkehrstageregelungService) wac.getBean("verkehrstageregelungService");
	}

	public ZugcharakteristikService createZugcharakteristikService() {
		return (ZugcharakteristikService) wac.getBean("zugcharakteristikService");
	}

	public FahrplanService createFahrplanService() {
		return (FahrplanService) wac.getBean("fahrplanService");
	}

	public ISA_ZugService createISA_ZugService() {
		return (ISA_ZugService) wac.getBean("isaZugService");
	}

	public ISA_FploService createISA_FploService() {
		return (ISA_FploService) wac.getBean("isaFploService");
	}

	public BBPStreckeService createBBPStreckeService() {
		return (BBPStreckeService) wac.getBean("bbpStreckeService");
	}

	public BaubetriebsplanService createBaubetriebsplanService() {
		return (BaubetriebsplanService) wac.getBean("baubetriebsplanService");
	}

	public AnmelderService createAnmelderService() {
		return (AnmelderService) wac.getBean("anmelderService");
	}

	public FinanztypService createFinanztypService() {
		return (FinanztypService) wac.getBean("finanztypService");
	}

	public ArbeitstypService createArbeitstypService() {
		return (ArbeitstypService) wac.getBean("arbeitstypService");
	}

	public FolgeNichtausfuehrungService createFolgeNichtausfuehrungService() {
		return (FolgeNichtausfuehrungService) wac.getBean("folgeNichtausfuehrungService");
	}

	public GrossmaschineService createGrossmaschineService() {
		return (GrossmaschineService) wac.getBean("grossmaschineService");
	}

	public PhaseService createPhaseService() {
		return (PhaseService) wac.getBean("phaseService");
	}

	public MeilensteinService createMeilensteinService() {
		return (MeilensteinService) wac.getBean("meilensteinService");
	}

	public SearchConfigService createSearchConfigService() {
		return (SearchConfigService) wac.getBean("searchConfigService");
	}

	public HistoryLogEntryService createHistoryLogEntryService() {
		return (HistoryLogEntryService) wac.getBean("historyLogEntryService");
	}

	public AnkermassnahmeArtService createAnkermassnahmeArtService() {
		return (AnkermassnahmeArtService) wac.getBean("ankermassnahmeArtService");
	}

}
