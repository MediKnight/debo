package de.bo.base.swing.text;

import java.util.Enumeration;

import javax.swing.SwingUtilities;
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
 **/

public abstract	class DagoAutoCompleteDocumentDecorator extends DocumentDecorator {
	protected	JTextComponent	textComponent;

	/**
	 * Konstruktor fuer eine selbstvervollständigende <CODE>JTextComponent</CODE>.
	 * @param textComponent Die <CODE>JTextComponent</CODE> für die
	 *		Auswahl des Vervollständigungsvorschlages
	 **/
	public DagoAutoCompleteDocumentDecorator( JTextComponent textComponent ) {
  	this( textComponent.getDocument(), textComponent, null );
	}

	/**
	 * Konstruktor fuer eine selbstvervollständigende <CODE>JTextComponent</CODE>.
	 * @param textComponent Die <CODE>JTextComponent</CODE> für die
	 *		Auswahl des Vervollständigungsvorschlages
   * @param id Die neue Id dieses Decorators
	 **/
	public DagoAutoCompleteDocumentDecorator( JTextComponent textComponent, Object id ) {
  	this( textComponent.getDocument(), textComponent, id );
	}

	/**
	 * Konstruktor für eine selbstvervollständigende <CODE>JTextComponent</CODE>.
	 * @param document Das darunterliegende <CODE>Document</CODE>
	 * @param textComponent Die <CODE>JTextComponent</CODE> für die
	 *		Auswahl des Vervollständigungsvorschlages
	 **/
	public DagoAutoCompleteDocumentDecorator( Document document,
			JTextComponent textComponent ) {
  	this( document, textComponent, null );
	}

	/**
	 * Konstruktor für eine selbstvervollständigende <CODE>JTextComponent</CODE>.
	 * @param document Das darunterliegende <CODE>Document</CODE>
	 * @param textComponent Die <CODE>JTextComponent</CODE> für die
	 *		Auswahl des Vervollständigungsvorschlages
   * @param id Die neue Id dieses Decorators
	 **/
	public DagoAutoCompleteDocumentDecorator( Document document,
			JTextComponent textComponent, Object id ) {
		super( document, id );
		this.textComponent = textComponent;
	}

	/**
	 * Diese Methode liefert eine <CODE>Enumeration</CODE> mit den möglichen
	 * Vervollständigungen und muß von der abgeleiteten Klasse
	 * überschrieben werden. Durch die Parameter können hier unterschiedliche
   * Vervollständigungen für verschiedene Prefixe zurückgeliefert werden.
   * @param beforeText Der Text vor der einzufügenden Stelle.
   * @param str Der einzufügende Text.
	 * @return Eine <CODE>Enumeration</CODE> von <CODE>String</CODE> die
	 *		die möglichen Vervollständigungen angibt.
	 **/
	abstract public	Enumeration	getCompletions( String beforeText, String str );

  /**
   * Diese Methode wird aufgerufen, wenn eine Vervollständigung gefunden worden
   * ist. Damit kann etwa eine der Vervollständigung entsprechende Information
   * in einer Statuszeile angezeigt werden.
   * @param completion Die Vervollständigung, die bei dem Abgleich mit der
   *    Eingabe vorgeschlagen wird.
   **/
	public	void	completionFound( String completion ) {
	}

  /**
   * Diese Methode bestimmt das Suffix der Eingabe, für das die
   * Vervollständigung durchgeführt werden soll. Als Resultat wird
   * im Normalfall die Konkatenation von dem bereits vorhandenen und dem
   * neuen Text zurückgeliefert. Es kann aber auch nur ein bestimmtes
   * Endstueck extrahiert werden (z.B. der Text nach dem letzten Komma).
   * Abgeleitete Klassen sollten diese Methode überschreiben.
   * @param beforeText Der Text vor der einzufügenden Stelle.
   * @param str Der einzufügende Text.
   * @return Der Text, der vervollständigt werden soll.
   **/
	public	String	getCompletableSuffix( String beforeText, String str ) {
		return beforeText + str;
	}

	/**
	 * Diese Methode führt eine Enfügung durch.
	 * @param offs ist der Startoffset f&uuml;r die Einf&uuml;gung
	 * @param str enthält den einzufügenden String
	 * @param a enthält die Attribute des einzufügenden Strings
	 **/
	public void insertString( int offs, String str, AttributeSet a )
	throws BadLocationException {
		if( str == null ) return;

		final	String	currentText	= getText( 0, getLength() );
		final	String	beforeText	= currentText.substring( 0, offs );
		final	String	afterText		= currentText.substring( offs, currentText.length() );

		if( afterText.equals( "" ) ) {
			String completableSuffix	= getCompletableSuffix( beforeText, str );
			if( !completableSuffix.equals( "" ) ) {
  			Enumeration completions = getCompletions( beforeText, str );
				while( completions.hasMoreElements() ) {
					String completion = (String) completions.nextElement();
					if( completion.regionMatches( 0, completableSuffix, 0, completableSuffix.length() ) ) {
						final	int	from	= beforeText.length() + str.length();
						final	int	to		= beforeText.length() + completion.length();

						// Benachrichtigung, dass eine Vervollständigung gefunden wurde
						completionFound( completion );

            // Wenn eine Vervollständigung existiert, diese Auswählen
						if( from != to ) {
							SwingUtilities.invokeLater( new Runnable() {
								public void run() {
									textComponent.select( from, to );
								}
							} );
						}
						str = str + completion.substring( completableSuffix.length() );
						break;
					}
				}
			}
		}
		super.insertString( offs, str, a );
	}
}
