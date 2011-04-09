package db.training.hibernate;

import java.util.List;

/**
 * Fuehrt ein Hibernate Command aus.
 * 
 * @author Sebastian Hennebrueder
 * 
 */
public interface TxTemplate {

	/**
	 * Fuert ein Hibernate Befehl innerhalb einer Transaktion aus und rollt bei einer Exception, die
	 * Transkation zurueck.
	 * 
	 * @param command
	 */
	void execute(HibernateCommand command, Object... param);

	/**
	 * Fuert ein Hibernate Befehl innerhalb einer Transaktion aus und rollt bei einer Exception, die
	 * Transkation zurueck. Gibt den Rückgabewert des Kommando zurueck.
	 * 
	 * @param command
	 * @return
	 */
	Object executeUnique(HibernateCommand command, Object... param);

	/**
	 * Fuert ein Hibernate Befehl innerhalb einer Transaktion aus und rollt bei einer Exception, die
	 * Transkation zurueck. Gibt den Rueckgabewert des Kommando zurueck. Der Rueckgabewert sollte
	 * eine Liste sein.
	 * 
	 * @param command
	 * @return
	 */
	List<?> executeList(HibernateCommand command, Object... param);
}
