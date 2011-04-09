package db.training.bob.service;

import java.io.Serializable;

import db.training.bob.model.SearchConfig;
import db.training.easy.common.BasicService;
import db.training.easy.core.model.User;

public interface SearchConfigService extends BasicService<SearchConfig, Serializable> {

	public SearchConfig findByUser(User user);
}
