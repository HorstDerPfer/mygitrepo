package db.training.bob.service;

import java.io.Serializable;
import java.util.List;

import db.training.bob.model.Grund;
import db.training.easy.common.BasicService;

public interface GrundService extends BasicService<Grund, Serializable> {

	List<Grund> findAllInUse();

}
