package db.training.web.administration;

public class EditableMeilensteinDisplaytagItem extends EditableDisplaytagItem<Integer> {

	private String art;

	private String schnittstelle;

	private String meilensteinBezeichnung;

	private Integer wochenVorBaubeginn;

	private String wochentag;

	private Boolean mindestfrist;

	private String gueltigAb;

	public EditableMeilensteinDisplaytagItem() {
		super();
		setArt(null);
		setSchnittstelle(null);
		setMeilensteinBezeichnung(null);
		setWochenVorBaubeginn(null);
		setWochentag(null);
		setMindestfrist(null);
		setGueltigAb(null);
	}

	public String getArt() {
		return art;
	}

	public void setArt(String art) {
		this.art = art;
	}

	public String getSchnittstelle() {
		return schnittstelle;
	}

	public void setSchnittstelle(String schnittstelle) {
		this.schnittstelle = schnittstelle;
	}

	public String getMeilensteinBezeichnung() {
		return meilensteinBezeichnung;
	}

	public void setMeilensteinBezeichnung(String meilensteinBezeichnung) {
		this.meilensteinBezeichnung = meilensteinBezeichnung;
	}

	public Integer getWochenVorBaubeginn() {
		return wochenVorBaubeginn;
	}

	public void setWochenVorBaubeginn(Integer wochenVorBaubeginn) {
		this.wochenVorBaubeginn = wochenVorBaubeginn;
	}

	public String getWochentag() {
		return wochentag;
	}

	public void setWochentag(String wochentag) {
		this.wochentag = wochentag;
	}

	public Boolean getMindestfrist() {
		return mindestfrist;
	}

	public void setMindestfrist(Boolean mindestfrist) {
		this.mindestfrist = mindestfrist;
	}

	public String getGueltigAb() {
		return gueltigAb;
	}

	public void setGueltigAb(String gueltigAb) {
		this.gueltigAb = gueltigAb;
	}

}
