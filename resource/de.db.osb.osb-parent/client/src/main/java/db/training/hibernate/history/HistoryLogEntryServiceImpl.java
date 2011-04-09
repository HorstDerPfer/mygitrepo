package db.training.hibernate.history;

import java.io.Serializable;

import org.springframework.transaction.annotation.Transactional;

import db.training.easy.common.EasyServiceImpl;

public class HistoryLogEntryServiceImpl extends EasyServiceImpl<HistoryLogEntry, Serializable>
    implements HistoryLogEntryService {

	public HistoryLogEntryServiceImpl() {
		super(HistoryLogEntry.class);
	}

	public HistoryLogEntryDao getDao() {
		return (HistoryLogEntryDao) getBasicDao();
	}

	@Transactional(readOnly = false)
	public void create(HistoryLogEntry historyLogEntry) {
		getDao().save(historyLogEntry);
	}
}