package db.training.osb.service;

import java.io.Serializable;
import java.util.List;

import org.hibernate.criterion.Order;

import db.training.bob.model.Regionalbereich;
import db.training.bob.service.FetchPlan;
import db.training.easy.common.BasicService;
import db.training.easy.util.displaytag.pagination.PaginatedList;
import db.training.osb.model.Paket;

public interface PaketService extends BasicService<Paket, Serializable> {
	Paket findByRbFahrplanjahrAndLfdNr(Regionalbereich regionalbereich, Integer fahrplanjahr,
	    Integer lfdNr);

	List<Paket> findByKeywords(Regionalbereich regionalbereich, Integer fahrplanjahrId,
	    Integer lfdNrId);

	PaginatedList<Paket> findPaginatedAll(Integer start, Integer count, int fahrplanjahr);

	PaginatedList<Paket> findPaginatedAllAndSort(List<Order> sortOrders, Integer start,
	    Integer count, int fahrplanjahr, FetchPlan[] fetchPlans);
}