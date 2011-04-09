package db.training.bob.model;

public enum Prioritaet {
	EINS, ZWEI, DREI;
	
	public int getOrdinal() {
		return ordinal();
	}
	
	public String getName() {
		return this.name();
	}
	
	public String getValue() {
		return "" + (ordinal()+1);
	}
}
