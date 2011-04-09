package db.training.bob.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import db.training.hibernate.history.Historizable;

@SuppressWarnings("serial")
@Entity
@Table(name = "zvfsperrebedarfsplan")
public class ZvFSperreBedarfsplan extends ZvF implements Historizable {

}
