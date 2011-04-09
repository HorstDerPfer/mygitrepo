package db.training.bob.model.fplo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "TBL_BESTELLUNG")
public class ISA_Bestellung implements Serializable {

	@Id
	@Column(name = "ID_BESTELLUNG", nullable = false)
	private Integer idBestellung;

	@Column(name = "KUNDENNR")
	private String kundennr;

	@Column(name = "NL")
	private int nl;

	@Column(name = "TEAMNR")
	private int teamnr;

	// @OneToMany
	// @JoinColumn(name = "ID_BESTELLUNG")
	// private List<ISA_Zug> zuege;

	public Integer getIdBestellung() {
		return idBestellung;
	}

	public void setIdBestellung(Integer idBestellung) {
		this.idBestellung = idBestellung;
	}

	public String getKundennr() {
		return kundennr;
	}

	public void setKundennr(String kundennr) {
		this.kundennr = kundennr;
	}

	public int getNl() {
		return nl;
	}

	public void setNl(int nl) {
		this.nl = nl;
	}

	public void setTeamnr(int teamnr) {
		this.teamnr = teamnr;
	}

	public int getTeamnr() {
		return teamnr;
	}

	// public List<ISA_Zug> getZuege() {
	// return zuege;
	// }
	//
	// public void setZuege(List<ISA_Zug> zuege) {
	// this.zuege = zuege;
	// }
}
