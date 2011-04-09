package db.training.bob.service;

import java.io.Serializable;
import java.util.Date;

import db.training.bob.model.Art;
import db.training.bob.model.Meilenstein;
import db.training.bob.model.Meilensteinbezeichnungen;
import db.training.bob.model.Schnittstelle;
import db.training.easy.common.BasicService;

public interface MeilensteinService extends BasicService<Meilenstein, Serializable> {

	public Date getSollTermin(Date baubeginn, Art art, Schnittstelle schnittstelle,
	    Meilensteinbezeichnungen meilensteinbezeichnungen);
}
