package de.baltic_online.base.swing.text;

import java.util.Enumeration;

import javax.swing.SwingUtilities;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

/**
 * Diese Klasse stellt einen <CODE>DocumentDecorator</CODE> f�r
 * <CODE>JTextComponents</CODE> dar,
 * der bei Eingabe eines Prefixes aus einer Liste eine m�gliche Erg�nzung
 * ausw�hlt und diese in der JTextComponent selektiert.
 * (Netscape macht das bei der URL genauso :-)
 *
 * Bei einem Datum w�rde man Tag, Monat und Jahr mit getrennten
 * Vervollst�ndigungen bearbeiten. Dazu w�rde <CODE>getCompletableSuffix</CODE>
 * jeweils den String nach dem letzten Punkt zur�ckliefern und
 * <CODE>getCompletions</CODE> anhand der Anzahl der Punkte die richtige
 * Vervollst�ndigung ermitteln.
 *
 * @version 0.01 1.8.1999
 * @author Dagobert Michelsen
 **/

public abstract	class DagoAutoCompleteDocumentDecorator extends DocumentDecorator {
	protected	JTextComponent	textComponent;

	/**
	 * Konstruktor fuer eine selbstvervollst�ndigende <CODE>JTextComponent</CODE>.
	 * @param textComponent Die <CODE>JTextComponent</CODE> f�r die
	 *		Auswahl des Vervollst�ndigungsvorschlages
	 **/
	public DagoAutoCompleteDocumentDecorator( JTextComponent textComponent ) {
  	this( textComponent.getDocument(), textComponent, null );
	}

	/**
	 * Konstruktor fuer eine selbstvervollst�ndigende <CODE>JTextComponent</CODE>.
	 * @param textComponent Die <CODE>JTextComponent</CODE> f�r die
	 *		Auswahl des Vervollst�ndigungsvorschlages
   * @param id Die neue Id dieses Decorators
	 **/
	public DagoAutoCompleteDocumentDecorator( JTextComponent textComponent, Object id ) {
  	this( textComponent.getDocument(), textComponent, id );
	}

	/**
	 * Konstruktor f�r eine selbstvervollst�ndigende <CODE>JTextComponent</CODE>.
	 * @param document Das darunterliegende <CODE>Document</CODE>
	 * @param textComponent Die <CODE>JTextComponent</CODE> f�r die
	 *		Auswahl des Vervollst�ndigungsvorschlages
	 **/
	public DagoAutoCompleteDocumentDecorator( Document document,
			JTextComponent textComponent ) {
  	this( document, textComponent, null );
	}

	/**
	 * Konstruktor f�r eine selbstvervollst�ndigende <CODE>JTextComponent</CODE>.
	 * @param document Das darunterliegende <CODE>Document</CODE>
	 * @param textComponent Die <CODE>JTextComponent</CODE> f�r die
	 *		Auswahl des Vervollst�ndigungsvorschlages
   * @param id Die neue Id dieses Decorators
	 **/
	public DagoAutoCompleteDocumentDecorator( Document document,
			JTextComponent textComponent, Object id ) {
		super( document, id );
		this.textComponent = textComponent;
	}

	/**
	 * Diese Methode liefert eine <CODE>Enumeration</CODE> mit den m�glichen
	 * Vervollst�ndigungen und mu� von der abgeleiteten Klasse
	 * �berschrieben werden. Durch die Parameter k�nnen hier unterschiedliche
   * Vervollst�ndigungen f�r verschiedene Prefixe zur�ckgeliefert werden.
   * @param beforeText Der Text vor der einzuf�genden Stelle.
   * @param str Der einzuf�gende Text.
	 * @return Eine <CODE>Enumeration</CODE> von <CODE>String</CODE> die
	 *		die m�glichen Vervollst�ndigungen angibt.
	 **/
	abstract public	Enumeration<String>	getCompletions( String beforeText, String str );

  /**
   * Diese Methode wird aufgerufen, wenn eine Vervollst�ndigung gefunden worden
   * ist. Damit kann etwa eine der Vervollst�ndigung entsprechende Information
   * in einer Statuszeile angezeigt werden.
   * @param completion Die Vervollst�ndigung, die bei dem Abgleich mit der
   *    Eingabe vorgeschlagen wird.
   **/
	public	void	completionFound( String completion ) {
	}

  /**
   * Diese Methode bestimmt das Suffix der Eingabe, f�r das die
   * Vervollst�ndigung durchgef�hrt werden soll. Als Resultat wird
   * im Normalfall die Konkatenation von dem bereits vorhandenen und dem
   * neuen Text zur�ckgeliefert. Es kann aber auch nur ein bestimmtes
   * Endstueck extrahiert werden (z.B. der Text nach dem letzten Komma).
   * Abgeleitete Klassen sollten diese Methode �berschreiben.
   * @param beforeText Der Text vor der einzuf�genden Stelle.
   * @param str Der einzuf�gende Text.
   * @return Der Text, der vervollst�ndigt werden soll.
   **/
	public	String	getCompletableSuffix( String beforeText, String str ) {
		return beforeText + str;
	}

	/**
	 * Diese Methode f�hrt eine Enf�gung durch.
	 * @param offs ist der Startoffset f&uuml;r die Einf&uuml;gung
	 * @param str enth�lt den einzuf�genden String
	 * @param a enth�lt die Attribute des einzuf�genden Strings
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
  			Enumeration<String> completions = getCompletions( beforeText, str );
				while( completions.hasMoreElements() ) {
					String completion = completions.nextElement();
					if( completion.regionMatches( 0, completableSuffix, 0, completableSuffix.length() ) ) {
						final	int	from	= beforeText.length() + str.length();
						final	int	to		= beforeText.length() + completion.length();

						// Benachrichtigung, dass eine Vervollst�ndigung gefunden wurde
						completionFound( completion );

            // Wenn eine Vervollst�ndigung existiert, diese Ausw�hlen
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
