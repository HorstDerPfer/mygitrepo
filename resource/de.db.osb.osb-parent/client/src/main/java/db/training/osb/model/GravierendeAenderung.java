/**
 * 
 */
package db.training.osb.model;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import db.training.easy.core.model.User;

/**
 * @author michels
 * 
 */
@Embeddable
public class GravierendeAenderung {
	@Column(name = "grav_datum", nullable = true)
	@Temporal(TemporalType.DATE)
	private Date date;

	@Column(name = "grav_ort", nullable = true)
	private boolean ort;

	@Column(name = "grav_zeit", nullable = true)
	private boolean zeit;

	@Column(name = "grav_betriebsweise", nullable = true)
	private boolean betriebsweise;

	@ManyToOne(optional = true)
	@JoinColumn(name = "grav_user_ID")
	private User user;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public boolean isOrt() {
		return ort;
	}

	public void setOrt(boolean ort) {
		this.ort = ort;
	}

	public boolean isZeit() {
		return zeit;
	}

	public void setZeit(boolean zeit) {
		this.zeit = zeit;
	}

	public boolean isBetriebsweise() {
		return betriebsweise;
	}

	public void setBetriebsweise(boolean betriebsweise) {
		this.betriebsweise = betriebsweise;
	}

	public GravierendeAenderung() {
		date = GregorianCalendar.getInstance().getTime();
		ort = false;
		zeit = false;
		betriebsweise = false;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
