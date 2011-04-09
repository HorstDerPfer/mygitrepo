package db.training.easy.core.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@SuppressWarnings("serial")
@MappedSuperclass
public class EasyPersistentObjectVc extends EasyPersistentObject {

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "lastchangedate")
	protected Date lastChangeDate;

	public Date getLastChangeDate() {
		return lastChangeDate;
	}

	public void setLastChangeDate(Date lastChangeDate) {
		this.lastChangeDate = lastChangeDate;
	}

	@Transient
	public void setLastChange(User user) {
		setLastChangeDate(new Date());
	}
}
