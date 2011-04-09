package db.training.hibernate;

import java.util.List;

import org.hibernate.Session;

import db.training.easy.core.dao.EasySessionFactory;
import db.training.logwrapper.Logger;

public class TxTemplateImpl implements TxTemplate {

	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger(TxTemplateImpl.class);

	/**
	 * Fuert ein Hibernate Befehl innerhalb einer Transaktion aus und rollt bei einer Exception, die
	 * Transkation zurueck.
	 * 
	 * @param command
	 */
	public void execute(HibernateCommand command, Object... param) {
		executeUnique(command, param);
	}

	/**
	 * Fuert ein Hibernate Befehl innerhalb einer Transaktion aus und rollt bei einer Exception, die
	 * Transkation zurueck. Gibt den Rückgabewert des Kommando zurueck.
	 * 
	 * @param command
	 * @return
	 */
	public Object executeUnique(HibernateCommand command, Object... param) {

		Object result = null;
		Session session = EasySessionFactory.getInstance().getCurrentSession();
		result = command.execute(session, param);
		return result;
	}

	/**
	 * Fuert ein Hibernate Befehl innerhalb einer Transaktion aus und rollt bei einer Exception, die
	 * Transkation zurueck. Gibt den Rueckgabewert des Kommando zurueck. Der Rueckgabewert sollte
	 * eine Liste sein.
	 * 
	 * @param command
	 * @return
	 */
	public List<?> executeList(HibernateCommand command, Object... param) {
		return (List<?>) executeUnique(command, param);
	}
}
