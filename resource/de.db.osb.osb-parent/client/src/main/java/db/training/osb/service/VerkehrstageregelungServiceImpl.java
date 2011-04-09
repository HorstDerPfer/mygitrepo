package db.training.osb.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import db.training.easy.common.EasyServiceImpl;
import db.training.osb.dao.VerkehrstageregelungDao;
import db.training.osb.model.Verkehrstageregelung;

public class VerkehrstageregelungServiceImpl extends
    EasyServiceImpl<Verkehrstageregelung, Serializable> implements VerkehrstageregelungService {

	public VerkehrstageregelungServiceImpl() {
		super(Verkehrstageregelung.class);
	}

	public VerkehrstageregelungDao getDao() {
		return (VerkehrstageregelungDao) getBasicDao();
	}

	@Transactional
	public Verkehrstageregelung findByVtsWithoutDuplicates(Integer vts) {
		List<Verkehrstageregelung> vtrs = null;
		vtrs = getDao().findByVts(vts);
		if (vtrs != null && vtrs.size() > 0)
			return vtrs.get(0);
		return null;
	}

	@Transactional
	public List<Verkehrstageregelung> findByVts(Integer vts) {
		List<Verkehrstageregelung> vtrs = null;
		vtrs = getDao().findByVts(vts);

		return vtrs;
	}
}