package db.training.web.administration;

import db.training.bob.model.EVUGruppe;

public class EditableEvuDisplaytagItem extends EditableDisplaytagItem<Integer> {

	private String kundenNr;
	
	private String kurzbezeichnung;
	
	private EVUGruppe evugruppe;

	public EditableEvuDisplaytagItem() {
	super();
	setKundenNr(null);
	setEvugruppe(null);
	setKurzbezeichnung(null);
    }

	public void setKundenNr(String kundenNr) {
		this.kundenNr = kundenNr;
	}

	public String getKundenNr() {
		return kundenNr;
	}

	public void setEvugruppe(EVUGruppe evugruppe) {
		this.evugruppe = evugruppe;
	}

	public EVUGruppe getEvugruppe() {
		return evugruppe;
	}

	public void setName(String name) {
		super.setText(name);
	}

	public String getName() {
		return super.getText();
	}
	
	public void setKurzbezeichnung(String kurzbezeichnung) {
		this.kurzbezeichnung = kurzbezeichnung;
	}

	public String getKurzbezeichnung() {
		return kurzbezeichnung;
	}
}
