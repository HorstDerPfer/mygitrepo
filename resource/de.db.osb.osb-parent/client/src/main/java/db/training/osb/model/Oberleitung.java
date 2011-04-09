/**
 * 
 */
package db.training.osb.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author michels
 * 
 */
@Entity
@Table(name = "oberleitung")
@SuppressWarnings("serial")
public class Oberleitung extends Regelung {

	@Column(length = 255)
	private String schaltgruppen;

	@Column(name = "sig_weiche_von")
	private String sigWeicheVon;

	@Column(name = "sig_weiche_bis")
	private String sigWeicheBis;

    public Oberleitung() {
    }

    public Oberleitung(Date zeitVon, Date zeitBis) {
        super(zeitVon, zeitBis);
    }

    public String getSchaltgruppen() {
		return schaltgruppen;
	}

	public void setSchaltgruppen(String schaltgruppen) {
		this.schaltgruppen = schaltgruppen;
	}

	public String getSigWeicheVon() {
		return sigWeicheVon;
	}

	public void setSigWeicheVon(String sigWeicheVon) {
		this.sigWeicheVon = sigWeicheVon;
	}

	public String getSigWeicheBis() {
		return sigWeicheBis;
	}

	public void setSigWeicheBis(String sigWeicheBis) {
		this.sigWeicheBis = sigWeicheBis;
	}
}
