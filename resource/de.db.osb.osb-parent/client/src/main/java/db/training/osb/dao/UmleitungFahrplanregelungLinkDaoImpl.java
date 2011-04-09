package db.training.osb.dao;

import java.io.Serializable;

import org.hibernate.SessionFactory;

import db.training.easy.common.BasicDaoImp;
import db.training.osb.model.UmleitungFahrplanregelungLink;

public class UmleitungFahrplanregelungLinkDaoImpl extends
    BasicDaoImp<UmleitungFahrplanregelungLink, Serializable> implements
    UmleitungFahrplanregelungLinkDao {

	public UmleitungFahrplanregelungLinkDaoImpl(SessionFactory instance) {
		super(UmleitungFahrplanregelungLink.class, instance);
	}
}