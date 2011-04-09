package db.training.osb.service;

import java.io.InputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import db.training.osb.web.imports.ImportMessage;
import db.training.osb.web.imports.ImportTable;
import db.training.security.hibernate.TqmUser;

public interface ImportService {

	public List<ImportMessage> importData(InputStream inputStream, TqmUser secUser);

	public HSSFWorkbook createWorkbookTemplate(ImportTable importTable);

}
