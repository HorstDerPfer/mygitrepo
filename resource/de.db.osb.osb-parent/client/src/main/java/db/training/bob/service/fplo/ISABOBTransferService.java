package db.training.bob.service.fplo;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import db.training.bob.model.Baumassnahme;
import db.training.bob.model.fplo.ISA_Zug;
import db.training.bob.model.fplo.Zugcharakteristik;
import db.training.bob.service.BaumassnahmeService;
import db.training.bob.service.FetchPlan;
import db.training.easy.core.service.EasyServiceFactory;

public class ISABOBTransferService {

	public static void getZugAndFahrplanFromISA(Integer id, Integer bobVorgangsnr,
	    Integer regionalbereichFplId) {
		ISA_ZugService isaZugService = EasyServiceFactory.getInstance().createISA_ZugService();
		List<ISA_Zug> isaZuege = isaZugService
		    .findByVorgangsnr(bobVorgangsnr, regionalbereichFplId);

		ZugcharakteristikService zugcharakteristikService = EasyServiceFactory.getInstance()
		    .createZugcharakteristikService();
		List<Zugcharakteristik> zugcharaktistikImported = zugcharakteristikService
		    .createFromISAObjects(isaZuege);

		Iterator<Zugcharakteristik> it = zugcharaktistikImported.iterator();
		Zugcharakteristik z = null;
		Zugcharakteristik zPersistent = null;
		Set<Zugcharakteristik> set = new HashSet<Zugcharakteristik>();
		while (it.hasNext()) {
			z = it.next();
			zPersistent = zugcharakteristikService.findByIdZug(z.getIdZug());
			if (zPersistent == null) {
				zugcharakteristikService.create(z);
				set.add(z);
			} else {
				z = zugcharakteristikService.merge(zPersistent, z);
				zugcharakteristikService.update(z);
				set.add(z);
			}
		}

		// an Baumassnahme anhängen
		BaumassnahmeService baumassnahmeService = EasyServiceFactory.getInstance()
		    .createBaumassnahmeService();
		Baumassnahme b = baumassnahmeService.findById(id, new FetchPlan[] {});
		b.setZugcharakteristik(set);
		baumassnahmeService.update(b);
	}

}
