package db.training.bob.service.fplo;

import java.io.Serializable;
import java.util.List;

import db.training.bob.model.fplo.ISA_Zug;
import db.training.easy.common.BasicService;

public interface ISA_ZugService extends BasicService<ISA_Zug, Serializable> {

	public List<ISA_Zug> findByVorgangsnr(Integer vorgangsnr, Integer regionalbereichId);
}
