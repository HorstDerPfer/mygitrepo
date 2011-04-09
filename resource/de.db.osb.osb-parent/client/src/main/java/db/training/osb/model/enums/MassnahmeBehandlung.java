package db.training.osb.model.enums;

// TODO: Werte korrigieren 
public enum MassnahmeBehandlung {
	BLANCK {
		public String toString() {
			return "";
		}
	},
	GEBUENDELT {
		public String toString() {
			return "GEBUENDELT";
		}
	},
	B_EM_BETRACHTET {
		public String toString() {
			return "B_EM_BETRACHTET";
		}
	},
	NICHT_WEITER_BETRACHTET {
		public String toString() {
			return "NICHT_WEITER_BETRACHTET";
		}
	}
}
