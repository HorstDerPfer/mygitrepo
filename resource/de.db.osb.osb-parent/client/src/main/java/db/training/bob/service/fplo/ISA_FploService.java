package db.training.bob.service.fplo;

import java.io.Serializable;
import java.util.List;

import db.training.bob.model.fplo.ISA_Fplo;
import db.training.easy.common.BasicService;

public interface ISA_FploService extends BasicService<ISA_Fplo, Serializable> {

	public List<Integer> findByFplo(String bob_vorgangsnr);

}
