/**
 * 
 */
package db.training.hibernate;

import java.sql.Types;

import org.hibernate.dialect.MySQL5InnoDBDialect;

/**
 * @author michels
 * 
 * bildet den Hibernate Typ 'bit' auf dem MySQL Typ 'tinyint(1)' ab.
 * 
 */
public class BaudbMySQL5InnoDBDialect extends MySQL5InnoDBDialect {

	public BaudbMySQL5InnoDBDialect() {
		super();

		registerColumnType(Types.BIT, "tinyint(1)");
	}
}
