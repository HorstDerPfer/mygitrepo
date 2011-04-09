package db.training.hibernate.history;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import db.training.easy.core.model.EasyPersistentObject;

/**
 * User: hennebrueder Date: 14.05.2009
 */
@Entity
@Table(name="changes_log")
public class HistoryLogEntry extends EasyPersistentObject {

	private static final long serialVersionUID = 8515715024562281830L;

	public enum Command {
		I, U, D;

		public static Command INSERT() {
			return I;
		}

		public static Command UPDATE() {
			return U;
		}

		public static Command DELETE() {
			return D;
		}

	}

	private String entityId;

	private String entityName;

	private String fieldName;

	@javax.persistence.Enumerated(EnumType.STRING)
	private Command command;

	private String userName;

	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	@Lob
	private String oldValue;

	@Lob
	private String newValue;

	protected HistoryLogEntry() {
	}

	public HistoryLogEntry(String entityId, String entityName, Command command, String userName,
	    Date date) {
		this.entityId = entityId;
		this.entityName = entityName;
		this.command = command;
		this.userName = userName;
		this.date = date;
	}

	public HistoryLogEntry(String entityId, String entityName, String fieldName, Command command,
	    String userName, Date date, String oldValue, String newValue) {
		this.entityId = entityId;
		this.entityName = entityName;
		this.fieldName = fieldName;
		this.command = command;
		this.userName = userName;
		this.date = date;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public Command getCommand() {
		return command;
	}

	public void setCommand(Command command) {
		this.command = command;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getOldValue() {
		return oldValue;
	}

	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("HistoryLogEntry");
		sb.append("{entityId=").append(entityId);
		sb.append(", entityName='").append(entityName).append('\'');
		sb.append(", fieldName='").append(fieldName).append('\'');
		sb.append(", command=").append(command);
		sb.append(", userName='").append(userName).append('\'');
		sb.append(", date=").append(date);
		sb.append(", oldValue='").append(oldValue).append('\'');
		sb.append(", newValue='").append(newValue).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
