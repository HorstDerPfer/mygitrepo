package db.training.bob.dao;

import java.util.List;

public interface MassnahmeDao {
	public List<Object[]> findMassnahmenByBaumassnahmen(Integer bm_id);
}
