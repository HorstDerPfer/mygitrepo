package db.training.bob.model;

import org.apache.struts.util.MessageResources;

public enum Meilensteinbezeichnungen {
	ANF_BBZR { // 0
		public String toString() {
			return MessageResources.getMessageResources("MessageResources")
					.getMessage("baumassnahme.admin.meilenstein.anf_bbzr");
		}
	},
	BIUE_ENTW { // 1
		public String toString() {// ehemals BiUe-Entwurf
			return MessageResources.getMessageResources("MessageResources")
					.getMessage("baumassnahme.admin.meilenstein.biue_entw");
		}
	},
	ZVF_ENTW { // 2
		public String toString() {
			return MessageResources.getMessageResources("MessageResources")
					.getMessage("baumassnahme.admin.meilenstein.zvf_entw");
		}
	},
	KOORD_ERG { // 3
		public String toString() {
			return MessageResources.getMessageResources("MessageResources")
			.getMessage("baumassnahme.admin.meilenstein.koord_erg");
		}
	},
	GESAMTKONZ_BBZR { // 4
		public String toString() {
			return MessageResources.getMessageResources("MessageResources")
			.getMessage("baumassnahme.admin.meilenstein.gesamtkonz_bbzr");
		}
	},
	BIUE { // 5
		public String toString() {
			return MessageResources.getMessageResources("MessageResources")
			.getMessage("baumassnahme.admin.meilenstein.biue");
		}
	},
	ZVF { // 6
		public String toString() {
			return MessageResources.getMessageResources("MessageResources")
			.getMessage("baumassnahme.admin.meilenstein.zvf");
		}
	},
	STELLUNGN_EVU { // 7
		public String toString() {
			return MessageResources.getMessageResources("MessageResources")
			.getMessage("baumassnahme.admin.meilenstein.stellungn_evu");
		}
	},
	M_UEB_PV { // 8
		public String toString() {
			return MessageResources.getMessageResources("MessageResources")
			.getMessage("baumassnahme.admin.meilenstein.m_ueb_pv");
		}
	},
	UEB_PV { // 9
		public String toString() {
			return MessageResources.getMessageResources("MessageResources")
			.getMessage("baumassnahme.admin.meilenstein.ueb_pv");
		}
	},
	FPLO { // 10
		public String toString() {
			return MessageResources.getMessageResources("MessageResources")
			.getMessage("baumassnahme.admin.meilenstein.fplo");
		}
	},
	EING_GFDZ { // 11
		public String toString() {
			return MessageResources.getMessageResources("MessageResources")
			.getMessage("baumassnahme.admin.meilenstein.eing_gfdz");
		}
	},
	BKONZ_EVU { // 12
		public String toString() {
			return MessageResources.getMessageResources("MessageResources")
			.getMessage("baumassnahme.admin.meilenstein.bkonz_evu");
		}
	},
	M_UEB_GV { // 13
		public String toString() {
			return MessageResources.getMessageResources("MessageResources")
			.getMessage("baumassnahme.admin.meilenstein.m_ueb_gv");
		}
	},
	UEB_GV { // 14
		public String toString() {
			return MessageResources.getMessageResources("MessageResources")
			.getMessage("baumassnahme.admin.meilenstein.ueb_gv");
		}
	};
}
