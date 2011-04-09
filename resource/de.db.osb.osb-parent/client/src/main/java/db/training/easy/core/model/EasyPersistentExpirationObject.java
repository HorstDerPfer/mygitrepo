package db.training.easy.core.model;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * erweitert die Klasse <code>EasyPersistentObject</code> um die Angabe eines Gültigkeitszeitraums.
 * 
 * @author mst
 * 
 */
@MappedSuperclass
public abstract class EasyPersistentExpirationObject extends EasyPersistentObject {

	private static final long serialVersionUID = 7149934482358232151L;

	@Column(name = "gueltig_von", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date gueltigVon;

	@Column(name = "gueltig_bis", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date gueltigBis;

	public Date getGueltigVon() {
		return gueltigVon;
	}

	public void setGueltigVon(Date gueltigVon) {
		this.gueltigVon = gueltigVon;
	}

	public Date getGueltigBis() {
		return gueltigBis;
	}

	public void setGueltigBis(Date gueltigBis) {
		this.gueltigBis = gueltigBis;
	}

	/**
	 * gibt den größtmöglichen Wert für das gültigBis-Datum zurück.
	 * 
	 * @return 31.12.2099
	 */
	public static Date getMaxGueltigBis() {
		Calendar calendar = new GregorianCalendar(2099, Calendar.DECEMBER, 31);
		return calendar.getTime();
	}
}
