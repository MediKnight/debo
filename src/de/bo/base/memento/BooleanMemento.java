package de.bo.base.memento;

public abstract	class BooleanMemento {
				public	abstract	void		setBooleanValue( Boolean v );
	final	public						void		setBooleanValue( boolean v ) { setBooleanValue( new Boolean( v ) ); }
				public	abstract	Boolean	getBooleanValue();
}

