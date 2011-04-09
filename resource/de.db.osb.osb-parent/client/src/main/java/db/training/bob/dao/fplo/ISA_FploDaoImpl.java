package db.training.bob.dao.fplo;

import java.io.Serializable;

import org.hibernate.SessionFactory;

import db.training.bob.model.fplo.ISA_Fplo;
import db.training.easy.common.BasicDaoImp;

public class ISA_FploDaoImpl extends BasicDaoImp<ISA_Fplo, Serializable> implements ISA_FploDao {

	public ISA_FploDaoImpl(SessionFactory instance) {
		super(ISA_Fplo.class, instance);
	}
}