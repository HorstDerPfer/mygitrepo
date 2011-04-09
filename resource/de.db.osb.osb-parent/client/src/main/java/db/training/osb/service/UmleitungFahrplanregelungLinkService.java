package db.training.osb.service;

import java.io.Serializable;
import java.util.List;

import db.training.bob.service.FetchPlan;
import db.training.easy.common.BasicService;
import db.training.osb.model.Fahrplanregelung;
import db.training.osb.model.Umleitung;
import db.training.osb.model.UmleitungFahrplanregelungLink;

public interface UmleitungFahrplanregelungLinkService extends
    BasicService<UmleitungFahrplanregelungLink, Serializable> {

	public UmleitungFahrplanregelungLink findByUmleitungAndFahrplanregelung(Umleitung umleitung,
	    Fahrplanregelung fahrplanregelung);

	public List<UmleitungFahrplanregelungLink> findByUmleitung(Umleitung umleitung);

	public List<UmleitungFahrplanregelungLink> findByFahrplanregelung(
	    Fahrplanregelung fahrplanregelung, FetchPlan[] fetchPlans);

}