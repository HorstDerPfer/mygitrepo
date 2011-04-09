package db.training.bob.service;

import java.io.Serializable;
import java.util.List;

import db.training.bob.model.BBPMassnahme;
import db.training.easy.common.BasicService;

public interface BBPMassnahmeService extends BasicService<BBPMassnahme, Serializable> {

	public List<BBPMassnahme> findByMasId(String masId, FetchPlan[] fetchPlans);

}
