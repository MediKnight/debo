package de.baltic_online.base.swing.text;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

/**
 * Diese Klasse stellt einen <CODE>DocumentDecorator</CODE> für
 * <CODE>JTextComponents</CODE> dar,
 * der bei Eingabe eines Prefixes aus einer Liste eine mögliche Ergänzung
 * auswählt und diese in der JTextComponent selektiert.
 * (Netscape macht das bei der URL genauso :-)
 *
 * Bei einem Datum würde man Tag, Monat und Jahr mit getrennten
 * Vervollständigungen bearbeiten. Dazu würde <CODE>getCompletableSuffix</CODE>
 * jeweils den String nach dem letzten Punkt zurückliefern und
 * <CODE>getCompletions</CODE> anhand der Anzahl der Punkte die richtige
 * Vervollständigung ermitteln.
 *
 * @version 0.01 1.8.1999
 * @author Dagobert Michelsen
 */
public abstract class AutoCompleteDocumentDecorator extends DocumentDecorator {

    public static final String CLASSNAME = "AutoCompleteDocumentDecorator";

    /**
     *
     */
    protected JTextComponent textComponent;

    /**
     *
     */
    protected String separators = " ";

    /**
     *
     */
    protected boolean wordCompletion = false;

    /**
     *
     */
    protected boolean caseInsensitive = true;

    /**
     *
     */
    protected boolean disabled = false;


    /**
     * Konstruktor fuer eine selbstvervollständigende <CODE>JTextComponent</CODE>.
     *
     * @param textComponent Die <CODE>JTextComponent</CODE> für die
     *  Auswahl des Vervollständigungsvorschlages
     */
    public AutoCompleteDocumentDecorator( JTextComponent textComponent ) {
	this( textComponent.getDocument(), textComponent, null );
    }

    /**
     * Konstruktor fuer eine selbstvervollständigende <CODE>JTextComponent</CODE>.
     *
     * @param textComponent Die <CODE>JTextComponent</CODE> für die
     *  Auswahl des Vervollständigungsvorschlages
     * @param id Die neue Id dieses Decorators
     */
    public AutoCompleteDocumentDecorator( JTextComponent textComponent, Object id ) {
	this( textComponent.getDocument(), textComponent, id );
    }

    /**
     * Konstruktor für eine selbstvervollständigende <CODE>JTextComponent</CODE>.
     *
     * @param document Das darunterliegende <CODE>Document</CODE>
     * @param textComponent Die <CODE>JTextComponent</CODE> für die
     *  Auswahl des Vervollständigungsvorschlages
     */
    public AutoCompleteDocumentDecorator( Document document,
					  JTextComponent textComponent ) {
	this( document, textComponent, null );
    }

    /**
     * Konstruktor für eine selbstvervollständigende <CODE>JTextComponent</CODE>.
     * @param document Das darunterliegende <CODE>Document</CODE>
     * @param textComponent Die <CODE>JTextComponent</CODE> für die
     *  Auswahl des Vervollständigungsvorschlages
     * @param id Die neue Id dieses Decorators
     */
    public AutoCompleteDocumentDecorator( Document document,
					  JTextComponent textComponent, Object id ) {
	super( document, id );
	this.textComponent = textComponent;
    }


    /**
     *
     */
    public void setWordCompletion( boolean status ) {
	wordCompletion = status;
    }

    /**
     *
     */
    public boolean isWordCompletion() {
	return wordCompletion;
    }


    /**
     *
     */
    public void setCaseInsensitive( boolean status ) {
	caseInsensitive = status;
    }

    /**
     *
     */
    public boolean isCaseInsensitive() {
	return caseInsensitive;
    }


    /**
     *
     */
    public void setDisabled( boolean status ) {
	disabled = status;
    }

    /**
     *
     */
    public boolean isDisabled() {
	return disabled;
    }


    /**
     * Diese Methode liefert eine <CODE>Enumeration</CODE> mit den möglichen
     * Vervollständigungen und muß von der abgeleiteten Klasse
     * überschrieben werden. Durch die Parameter können hier unterschiedliche
     * Vervollständigungen für verschiedene Prefixe zurückgeliefert werden.
     *
     * @param beforeText Der Text vor der einzufügenden Stelle.
     * @param str Der einzufügende Text.
     * @return Eine <CODE>Enumeration</CODE> von <CODE>String</CODE> die
     *  die möglichen Vervollständigungen angibt.
     */
    abstract public String getCompletion( String s );


    /**
     * Diese Methode wird aufgerufen, wenn eine Vervollständigung gefunden worden
     * ist. Damit kann etwa eine der Vervollständigung entsprechende Information
     * in einer Statuszeile angezeigt werden.
     *
     * @param completion Die Vervollständigung, die bei dem Abgleich mit der
     *    Eingabe vorgeschlagen wird.
     */
    public void completionFound( String completion ) {}


    /**
     * Diese Methode bestimmt das Suffix der Eingabe, für das die
     * Vervollständigung durchgeführt werden soll. Als Resultat wird
     * im Normalfall die Konkatenation von dem bereits vorhandenen und dem
     * neuen Text zurückgeliefert. Es kann aber auch nur ein bestimmtes
     * Endstueck extrahiert werden (z.B. der Text nach dem letzten Komma).
     * Abgeleitete Klassen sollten diese Methode überschreiben.
     *
     * @param beforeText Der Text vor der einzufügenden Stelle.
     * @param str Der einzufügende Text.
     *
     * @return Der Text, der vervollständigt werden soll.
     */
    public String getCompletableSuffix( String s ) {
	String result = null;

	if( wordCompletion ) {
	    result = s.substring( findRightMostSeparator( s ) + 1 );
	} else {
	    result = s;
	}

	if( result != null && result.length() == 0 ) {
	    return null;
	} else {
	    return result;
	}
    }


    /**
     *
     */
    protected int findRightMostSeparator( String s ) {
	int index = -1;
	int tempIndex = 0;

	for( int i = 0; i < separators.length(); i++ ) {
	    tempIndex = s.lastIndexOf( separators.charAt( i ) );

	    if( tempIndex > index ) {
		index = tempIndex;
	    }
	}

	return index;
    }


    /**
     *
     */
    protected boolean mayComplete( int offset, String s ) {
	if( offset == s.length() ) {
	    //
	    // Am Ende der Eingabe: Komplettierung moeglich.
	    //

	    return true;
	} else {
	    if( ! wordCompletion ) {
		//
		// Nicht am Ende der Eingabe und keine wortbasierte Vervollstaendigung:
		// Keine Komplettierung moeglich.
		//

		return false;
	    } else {
		if( s.charAt( offset ) == ' ' ) {
		    //
		    // Zwischen zwei Worten bei wordCompletion == true: Komplettierung
		    // moeglich.
		    //

		    return true;
		} else {
		    //
		    // Keine Komplettierung moeglich.
		    //

		    return false;
		}
	    }
	}
    }


    /**
     * Diese Methode führt eine Enfügung durch.
     *
     * @param offs ist der Startoffset f&uuml;r die Einf&uuml;gung
     * @param str enthält den einzufügenden String
     * @param a enthält die Attribute des einzufügenden Strings
     */
    public void insertString( int offset, String s, AttributeSet attributeSet )
	throws BadLocationException {

	if( s == null ) {
	    return;
	}

	if( disabled ) {
	    super.insertString( offset, s, attributeSet );
	} else {
	    String completion = null;
	    final String currentContents = getText( 0, getLength() );
	    final String leftSide = currentContents.substring( 0, offset ) + s;
	    
	    if( mayComplete( offset, currentContents ) ) {
		//
		// Komplettierung moeglich, d.h. entweder ist dieses eine Einfuegung am
		// Ende der Eingabe oder eine Einfuegung am Ende eines 
		// Wortes (wordCompletion == true).
		//
		
		String completableSuffix = getCompletableSuffix( leftSide );
		
		if( completableSuffix != null ) {
		    completion = getCompletion( completableSuffix );
		    
		    if( completion != null ) {
			
			//
			// Benachrichtigung ueber die Vervollstaendigung.
			//
			completionFound( completion );
			
			s = s + completion;
		    }
		}
	    }
	    
	    //
	    // Einfuegen in das Dokument.
	    //
	    super.insertString( offset, s, attributeSet );
	    
	    if( completion != null ) {
		//
		// Markierung des komplettierten Anteils.
		//
		
		textComponent.select( leftSide.length(), 
				      leftSide.length() + completion.length() );
	    }
	}
    }
}
