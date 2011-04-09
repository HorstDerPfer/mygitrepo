package db.training.bob.model.zvf.helper;

public enum FormularKennung {
	/*
	 * ZVF_070708 { public String toString() { return "ZVF-070708"; } }, UeB_070708 { public String
	 * toString() { return "�B-070708"; } },
	 */
	ZVF_091008 {
		public String toString() {
			return "ZVF-091008";
		}
	},
	UeB_091008 {
		public String toString() {
			return "ÜB-091008";
		}
	};

	public static FormularKennung castFromString(String value) {
		if (value.startsWith(UeB_091008.toString().substring(0, 1)))
			return UeB_091008;
		return ZVF_091008;
		// if (value.equals(UeB_091008.toString()))
		// return UeB_091008;
		// return ZVF_091008;
	}

}
