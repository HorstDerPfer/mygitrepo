package db.training.misc;

import java.util.Date;
import java.util.Set;

import db.training.easy.core.service.EasyServiceFactory;
import db.training.osb.model.VzgStrecke;
import db.training.osb.service.VzgStreckeService;

public class AllzweckApp {

	public static void main(String argv[]) {
		System.out.println("start");
		Date start = new Date();

		VzgStreckeService vzgService = EasyServiceFactory.getInstance().createVzgStreckeService();

		VzgStrecke vzgStrecke = vzgService.findById(1700);
		Set<VzgStrecke> vzgStrecken = vzgService.findAbzweigendeStrecken(vzgStrecke, 2013, null,
		    null);
		System.out.println("vzgStrecken: " + vzgStrecken.size());

		Date end = new Date();
		System.out.println("time: " + ((end.getTime() - start.getTime())) + "s");
		System.out.println("finish");
	}

}
