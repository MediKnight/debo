package de.bo.base.swing.text;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

/**
 * Diese Klasse implementiert ein Decorator-Pattern für <CODE>Document</CODE>.
 * Einem DocumentDecorator kann eine Id zugewiesen bekommen um ihn eindeutig
 * zu kennzeichnen. Falls in einer Kette von Decorators einer ausgetauscht
 * werden soll wird dazu diese Id verwendet.
 *
 * @version 0.01 1.8.1999
 * @author Dagobert Michelsen
 **/

public class DocumentDecorator implements Document {

	protected	Document	document;   // das darunterliegende Document
	protected	Object    id;         // eine beliebige Id für diesen Decorator

  /**
   * Standard-Konstruktor
   **/
	public DocumentDecorator() {
  	this( null, null );
	}

  /**
   * Konstruktor, der als Parameter das Document üebergeben bekommt, das durch
   * den Decorator eingeschalt werden soll.
   * @parm document Das Document, das von diesem DocumentDecorator eingeschalt
   *		werden soll.
   **/
	public DocumentDecorator( Document document ) {
		this( document, null );
	}

  /**
   * Konstruktor, der als Parameter das Document üebergeben bekommt, das durch
   * den Decorator eingeschalt werden soll.
   * @parm document Das Document, das von diesem DocumentDecorator eingeschalt
   *		werden soll.
   * @param id Die neue Id dieses Decorators
   **/
	public DocumentDecorator( Document document, Object id ) {
  	setEnclosedDocument( document );
    setId( id );
	}


	/**
   * Diese Methode setzt die Id dieses Decorators auf den übergebenen Parameter
   * @param id Die neue Id dieses Decorators
   **/
	public	void		setId( Object id ) {
  	this.id = id;
  }

  /**
   * Diese Methode liefert als Resultat die Id dieses Decorators
   * @return Die Id dieses Decorators
   **/
	public	Object	getId(){
  	return id;
  }

	/**
   * Mit dieser Methode wird das Document, an das die Aufrufe
	 * delegiert werden, auf den übergebenen Paramater gesetzt.
   * @param document Das neue Document, das durch diesen DocumentDecorator
   *		eingeschalt wird.
   **/
	public	void	setEnclosedDocument( Document document ) {
  	this.document = document;
  }

  /**
   * Diese Methode liefert als Ergebnis das Document, das durch diesen
   * DocumentDecorator eingeschalt ist.
   * @return Das Document, das durch diesen DocumentDecorator eingeschalt ist.
   **/
	public	Document 	getEnclosedDocument() {
  	return document;
  }

	/**
   * Mit dieser Methode wird der DocumentDecorator des JTextFields ersetzt
   * durch den als Parameter übergebenen DocumentDecorator der gleichen Id.
   * Ist in der Kette kein DocumentDecorator dieser Id enthalten oder ist
   * die Id des als Parameter übergebenen DocumentDecorator <CODE>null</CODE>,
   * so wird dieser an dem Ende der Decoratorkette hinzugefuegt.
   * @param tf Das JTextField, in dessen Decoratorkette der DocumentDecorator
   *		eingefügt werden soll.
   * @param d2 Der neue DocumentDecorator, der in die Decoratorkette eingesetzt
   *		werden soll.
   **/
	public	static	void	updateDocumentDecorator( JTextField tf, DocumentDecorator d2 ) {
		Document					d		= tf.getDocument();
		DocumentDecorator	d1	= null;
    Object						id	= d2.getId();

		// DocumentDecorator mit der richtigen Id suchen. Falls er gefunden wird
		// ist er in d und der Vorgaenger in d1.
		while( d instanceof DocumentDecorator ) {
			if( (id != null) && id.equals( ((DocumentDecorator) d).getId() ) ) {
				break;
			}
			d1 = (DocumentDecorator) d;
			d = d1.getEnclosedDocument();
		}

		if( d instanceof DocumentDecorator ) {
			// Ersetzen
			d2.setEnclosedDocument( ((DocumentDecorator) d).getEnclosedDocument() );
		} else {
			// Hinzufuegen
			d2.setEnclosedDocument( d );
		}

		if( d1 == null ) {
			// Kein Vorgaenger
			tf.setDocument( d2 );
		} else {
			// Vorgaenger auf den neuen DocumentDecorator zeigen lassen
			d1.setEnclosedDocument( d2 );
		}
	}

	// Die folgenden Methoden implementieren das Document-Interface als
	// Decorator-Pattern durch Delegation der Methodenaufrufe.

	public int				getLength()																									{ return document.getLength(); }
	public void				addDocumentListener( DocumentListener listener )						{ document.addDocumentListener( listener ); }
	public void				removeDocumentListener( DocumentListener listener )					{ document.removeDocumentListener( listener ); }
	public void				addUndoableEditListener( UndoableEditListener listener )		{ document.addUndoableEditListener( listener ); }
	public void				removeUndoableEditListener( UndoableEditListener listener )	{ document.removeUndoableEditListener( listener ); }
	public Object			getProperty( Object key )																		{ return document.getProperty( key ); }
	public void				putProperty( Object key, Object value )											{ document.putProperty( key, value ); }
	public void				remove( int offs, int len )
										throws BadLocationException                                 { document.remove( offs, len ); }
	public void				insertString( int offset, String str, AttributeSet a )
										throws BadLocationException																	{ document.insertString( offset, str, a ); }
	public String			getText( int offset, int length )
										throws BadLocationException																	{ return document.getText( offset, length ); }
	public void				getText( int offset, int length, Segment txt )
										throws BadLocationException																	{ document.getText( offset, length, txt ); }
	public Position		getStartPosition()																					{ return document.getStartPosition(); }
	public Position		getEndPosition()																						{ return document.getEndPosition(); }
	public Position		createPosition( int offs )
										throws BadLocationException																	{ return document.createPosition( offs ); }
	public Element[]	getRootElements()																						{ return document.getRootElements(); }
	public Element		getDefaultRootElement()																			{ return document.getDefaultRootElement(); }
	public void				render( Runnable r )																				{ document.render( r ); }
}
