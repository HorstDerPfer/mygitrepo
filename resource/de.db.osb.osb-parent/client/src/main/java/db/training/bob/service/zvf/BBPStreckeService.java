package db.training.bob.service.zvf;

import java.io.Serializable;

import db.training.bob.model.zvf.BBPStrecke;
import db.training.easy.common.BasicService;

public interface BBPStreckeService extends BasicService<BBPStrecke, Serializable> {

	public BBPStrecke findByNummer(int nummer);
}
