package db.training.hibernate.history;

import java.io.Serializable;

import org.hibernate.SessionFactory;

import db.training.easy.common.BasicDaoImp;

public class HistoryLogEntryDaoImpl extends BasicDaoImp<HistoryLogEntry, Serializable> implements
    HistoryLogEntryDao {

	public HistoryLogEntryDaoImpl(SessionFactory instance) {
		super(HistoryLogEntry.class, instance);
	}
}