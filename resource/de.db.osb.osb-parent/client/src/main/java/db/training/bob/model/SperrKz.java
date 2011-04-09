package db.training.bob.model;

public enum SperrKz {
	DURCHGEHEND, UNTERBROCHEN, DURCHGEHENDE_SCHICHTEN, UNTERBROCHENE_SCHICHTEN;
	
	public static SperrKz getConstant(Integer index) throws IllegalArgumentException {
		if (index == 1) return DURCHGEHEND;
		if (index == 2) return UNTERBROCHEN;
		if (index == 3) return DURCHGEHENDE_SCHICHTEN;
		if (index == 4) return UNTERBROCHENE_SCHICHTEN;
		throw new IllegalArgumentException();
	}
}
