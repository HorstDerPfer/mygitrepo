package db.training.osb.service;

import java.io.Serializable;
import java.util.List;

import org.hibernate.criterion.Order;

import db.training.bob.model.Regionalbereich;
import db.training.bob.service.FetchPlan;
import db.training.easy.common.BasicService;
import db.training.easy.util.displaytag.pagination.PaginatedList;
import db.training.osb.model.SAPMassnahme;
import db.training.osb.model.VzgStrecke;
import db.training.osb.web.sperrpausenbedarf.SperrpausenbedarfListReport;

public interface SAPMassnahmeService extends BasicService<SAPMassnahme, Serializable> {

	public void create(SAPMassnahme massnahme) throws RuntimeException;

	public List<SAPMassnahme> findByVzgStrecke(VzgStrecke streckeVzg, FetchPlan[] fetchPlans);

	public List<SAPMassnahme> findByRegionalbereich(Regionalbereich regionalbereich,
	    FetchPlan[] fetchPlans);

	public List<SAPMassnahme> findByIds(Integer[] ids, FetchPlan[] fetchPlans);

	public List<SAPMassnahme> findByFahrplanjahr(int fahrplanjahr, FetchPlan[] fetchPlans);

	public List<SAPMassnahme> findByKeywords(Integer regionalbereichId, String anmelder,
	    String gewerk, String untergewerk, Integer hauptStreckeNummer, Integer paketId,
	    String sapProjektnummer, FetchPlan[] fetchPlans, int fahrplanjahr);

	public PaginatedList<SAPMassnahme> findPaginatedByKeywords(Integer regionalbereichId,
	    String anmelder, String gewerk, String untergewerk, Integer hauptStreckeNummer,
	    Integer paketId, String sapProjektnummer, FetchPlan[] fetchPlans, Integer start,
	    Integer count, int fahrplanjahr);

	public PaginatedList<SAPMassnahme> findPaginatedByKeywordsAndSort(List<Order> sortOrders,
	    Integer regionalbereichId, String anmelder, String gewerk, String untergewerk,
	    Integer hauptStreckeNummer, Integer paketId, String sapProjektnummer,
	    FetchPlan[] fetchPlans, Integer start, Integer count, int fahrplanjahr);

	PaginatedList<SperrpausenbedarfListReport> findReportPaginatedByKeywordsAndSort(
	    List<Order> sortOrders, Integer regionalbereichId, String anmelder, String gewerk,
	    String untergewerk, Integer hauptstreckenNummer, Integer paketId, String sapProjektnummer,
	    FetchPlan[] fetchPlans, Integer start, Integer count, Integer sessionFahrplanjahr);
}