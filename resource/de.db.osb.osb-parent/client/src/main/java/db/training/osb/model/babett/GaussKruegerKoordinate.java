/**
 * 
 */
package db.training.osb.model.babett;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Das Gauß-Krüger-Koordinatensystem ist ein kartesisches Koordinatensystem, das es ermöglicht,
 * hinreichend kleine Gebiete der Erde mit metrischen Koordinaten (Rechtswert und Hochwert) konform
 * zu verorten. Quelle: <a href="http://de.wikipedia.org/wiki/Gau%C3%9F-Kr%C3%BCger-
 * Koordinatensystem">Wikipedia</a>
 * 
 * @author michels
 * 
 */
@Embeddable
public class GaussKruegerKoordinate {
	@Column(name = "gkk_nord", nullable = true)
	private int nord;

	@Column(name = "gkk_ost", nullable = true)
	private int ost;

	public int getNord() {
		return nord;
	}

	public void setNord(int nord) {
		this.nord = nord;
	}

	public int getOst() {
		return ost;
	}

	public void setOst(int ost) {
		this.ost = ost;
	}
}
