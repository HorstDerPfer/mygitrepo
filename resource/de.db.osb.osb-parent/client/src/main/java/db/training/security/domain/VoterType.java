package db.training.security.domain;

public enum VoterType {
	ALL, // TRUE if all Authorizations do apply
	ANY, // True if one of the specified authorizations applies
	NONE
	// True if none of the specified authorizations applies
}
