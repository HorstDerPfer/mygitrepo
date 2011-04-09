package db.training.osb.service;

import java.io.Serializable;

import db.training.easy.common.BasicService;
import db.training.osb.model.Betriebsstelle;
import db.training.osb.model.Umleitungsweg;
import db.training.osb.model.VzgStrecke;

public interface UmleitungswegService extends BasicService<Umleitungsweg, Serializable> {

	public Umleitungsweg findByBetriebsstellenAndStrecke(Betriebsstelle von, Betriebsstelle bis,
	    VzgStrecke strecke);
}