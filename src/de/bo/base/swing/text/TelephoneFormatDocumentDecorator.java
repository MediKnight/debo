package de.bo.base.swing.text;

import java.util.StringTokenizer;
import javax.swing.*;
import javax.swing.text.*;

/**
 * Diese Klasse stellt einen DocumentDecorator dar, der den Text des Documents
 * bei Einfügungen so verändert, daß Zahlen von rechts an in Zweiergruppen
 * formatiert werden (d.h. zwei Zahlen, ein Space; bei ungerader Anzahl steht
 * die erste Ziffer allein).
 * Gibt es weitere Begrenzungen der LimitedDocumentDecorator weiter unten in
 * der Decoratorkette, so wird die Gruboerung nur dann durchgeführt, wenn
 * alle Decorator in der Decoraterkette dem zustimmen. Ansonsten wird die
 * Einfügung ohne Formatierung durchgeführt.
 *
 * @version 0.01 1.8.1999
 * @author Dagobert Michelsen
 **/

public class TelephoneFormatDocumentDecorator extends DocumentDecorator {
	private static final boolean debug = false;

  /**
   * Standard-Konstruktor
   **/
	public TelephoneFormatDocumentDecorator() {
	}

  /**
   * Konstruktor, der als Parameter das ainzuschalende Document bekommt.
   * @param document Das einzuschalende Document
   **/
	public TelephoneFormatDocumentDecorator( Document document ) {
		super( document );
	}

	/**
	 * Diese Methode überprueft, ob in der Kette von LimitedDocumentDecorator
	 * das Einfügen erfolgreich währe.
	 * Achtung: Das geht nur dann, wenn dazwischen keine DocumentDecorator liegen,
	 * die den Einfügeprozess modifizieren (z.B. AutoCompleteDocumentDecorator)
   * @param offs Der Startoffset, ab dem in dem Document die Einfügung
   *		beginnt.
   * @param str Der einzufügende String
   * @param a Die Attribute für den einzufügenden String
   * @return <CODE>null</CODE> wenn die Einfügung gültig ist, ansonsten ein
   *		<CODE>String</CODE> mit einer Fehlerbeschreibung
	 **/
	public	String	checkInsertRecursive( int offs, String str, AttributeSet a ) {
		Document d = this;
		while( d instanceof DocumentDecorator ) {
			if( d instanceof LimitedDocumentDecorator ) {
				try {
					String err = ((LimitedDocumentDecorator) d).checkInsert( offs, str, a );
					if( err != null ) {
						return err;
					}
				} catch( BadLocationException e ) {
					e.printStackTrace();
				}
			}
			d = ((DocumentDecorator) d).getEnclosedDocument();
		}
		return null;
	}

  /**
   * Diese Methode formatiert den als Parameter übergebenen String so um,
   * daß er von rechts an in Zweiergruppen sortiert ist. Dies geschieht jedoch
   * nur dann, wenn der String ausschließlich aus Ziffern besteht. Ansonsten
   * wird der String unverändert zurückgegeben.
   * @param phone Der zu gruboerende String
   * @return Der von rechts an in Zweiergruppen gruboerte String oder die
   *		unveränderte Eingabe, falls sie nicht nur aus Ziffern bestand.
   **/
	protected	String	formatPairs( String phone ) {
		char[] phoneChars = phone.toCharArray();

		// Nur anwendbar wenn phone ausschliesslich Zahlen enthaelt
		for( int i = 0; i < phoneChars.length; i++ ) {
			if( !Character.isDigit( phoneChars[ i ] ) ) {
				return phone;
			}
		}

		StringBuffer phoneFormatted = new StringBuffer( phoneChars.length * 3 / 2 );
		boolean	evenLength = ( phoneChars.length % 2 == 0 );
		int i = 0;
		if( !evenLength ) {
			// Bei ungerade Länge ein Zeichen vorneweg
      phoneFormatted.append( phoneChars[ i++ ] );
			if( i < phoneChars.length ) {
				phoneFormatted.append( ' ' );
			}
		}
		while( i < phoneChars.length ) {
			phoneFormatted.append( phoneChars[ i++ ] );
			phoneFormatted.append( phoneChars[ i++ ] );
			if( i < phoneChars.length ) {
				phoneFormatted.append( ' ' );
			}
		}
		return phoneFormatted.toString();
	}

  /**
   * Diese Methode führt eine Einfügung durch. Dabei werden alle Zahlen
   * des gesamten Textes in Zweiergruppen von rechts formatiert.
   * @param offs Der Startoffset, ab dem in dem Document die Einfügung
   *		beginnt.
   * @param str Der einzufügende String
   * @param a Die Attribute für den einzufügenden String
   **/
	public void insertString( int offs, String str, AttributeSet a )
	throws BadLocationException {
		if( str == null ) return;

		final	String	currentText	= getText( 0, getLength() );
		final	String	beforeText	= currentText.substring( 0, offs );
		final	String	afterText		= currentText.substring( offs, currentText.length() );

    // Formatierung nur bei dem Anfügen von Text
		if( afterText.equals( "" ) ) {
			String				proposedResult	= beforeText + str;
			char[]				proposedChars		= proposedResult.toCharArray();
			StringBuffer	textPlain				= new StringBuffer( proposedChars.length );
			boolean				lastWasDigit		= false,
										butLastWasDigit	= false;
			String				spaces					= "";

			int end = proposedChars.length - 1;
			while( end >= 0 && Character.isWhitespace( proposedChars[ end ] ) ) {
				--end;
			}

			for( int i = 0; i <= end; i++ ) {
				if( Character.isWhitespace( proposedChars[ i ] ) ) {
					spaces = spaces + proposedChars[ i ];
					continue;
				}
				butLastWasDigit = lastWasDigit;
				lastWasDigit		= Character.isDigit( proposedChars[ i ] );
				if( debug ) System.out.println( "ButLast: " + butLastWasDigit );
				if( debug ) System.out.println( "Last:    " + lastWasDigit );
				if( !butLastWasDigit || !lastWasDigit ) {
					textPlain.append( spaces );
					if( debug) System.out.println( "Spaces appended: '" + spaces + "'" );
				}
				spaces = "";
				textPlain.append( proposedChars[ i ] );
				if( debug ) System.out.println( "Text appended: " + proposedChars[ i ] );
			}

			for( int i = end + 1; i < proposedChars.length; i++ ) {
				textPlain.append( proposedChars[ i ] );
				if( debug ) System.out.println( "Rest appended: " + proposedChars[ i ] );
			}

			StringTokenizer st	= new StringTokenizer( textPlain.toString(), " /-", true );
			StringBuffer		tpf	= new StringBuffer();
			while( st.hasMoreTokens() ) {
				String token = st.nextToken();
				if( debug ) System.out.println( "Token: " + token );
				tpf.append( formatPairs( token ) );
			}

			String formattedPhone	= tpf.toString();
			String backupText			= getText( 0, getLength() );
			super.remove( 0, getLength() );
			if( checkInsertRecursive( 0, formattedPhone, a ) == null ) {
				// Einfügen des formatierten Textes von anderen Limitern erlaubt
				str = formattedPhone;
				offs = 0;
			} else {
				// Einfügen von anderen Limitern nicht erlaubt. Geht unformatiertes
        // einfügen?
				if( checkInsertRecursive( 0, textPlain.toString(), a ) == null ) {
					// Ja, dingfest machen
					str = textPlain.toString();
					offs = 0;
				} else {
        	// Nein, alten Text wiederherstellen
					super.insertString( 0, backupText, a );
				}
			}
		}
		super.insertString( offs, str, a );
	}
}
