package db.training.osb.service;

import java.io.Serializable;
import java.util.List;

import db.training.easy.common.BasicService;
import db.training.osb.model.Verkehrstageregelung;

public interface VerkehrstageregelungService extends
    BasicService<Verkehrstageregelung, Serializable> {

	/**
	 * Liefert einen Verkehrstageschluessel anhand der VTS-Nummer zurueck. Werden mehrere Ergebnisse
	 * zu einer VTS-Nummer gefunden, wird der erste Datensatz zurueckgegeben. VTS-Nummern koennen
	 * doppelt vergeben werden, diese haben dann aber eine andere Bezeichnung.
	 * 
	 * @param vts
	 * @return verkehrstageregelung
	 */
	public Verkehrstageregelung findByVtsWithoutDuplicates(Integer vts);

	/**
	 * Liefert eine Liste Verkehrstageschluessel anhand der VTS-Nummer zurueck. VTS-Nummern koennen
	 * doppelt vergeben werden, diese haben dann aber eine andere Bezeichnung.
	 * 
	 * @param vts
	 * @return verkehrstageregelung
	 */
	public List<Verkehrstageregelung> findByVts(Integer vts);

}
