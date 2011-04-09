package db.training.web.administration;

public class EditableFplBearbeitungsbereichDisplaytagItem extends EditableDisplaytagItem<Integer> {

	private String regionalbereichFpl;

	private Integer regionalbereichFplId;

	private String vorgangsnrMin;

	private String vorgangsnrMax;

	public EditableFplBearbeitungsbereichDisplaytagItem() {
		super();
		setRegionalbereichFpl(null);
	}

	public void setRegionalbereichFpl(String regionalbereichFpl) {
		this.regionalbereichFpl = regionalbereichFpl;
	}

	public String getRegionalbereichFpl() {
		return regionalbereichFpl;
	}

	public void setRegionalbereichFplId(Integer regionalbereichFplId) {
		this.regionalbereichFplId = regionalbereichFplId;
	}

	public Integer getRegionalbereichFplId() {
		return regionalbereichFplId;
	}

	public String getVorgangsnrMin() {
		return vorgangsnrMin;
	}

	public void setVorgangsnrMin(String vorgangsnrMin) {
		this.vorgangsnrMin = vorgangsnrMin;
	}

	public String getVorgangsnrMax() {
		return vorgangsnrMax;
	}

	public void setVorgangsnrMax(String vorgangsnrMax) {
		this.vorgangsnrMax = vorgangsnrMax;
	}

	public void setName(String name) {
		super.setText(name);
	}

	public String getName() {
		return super.getText();
	}
}
