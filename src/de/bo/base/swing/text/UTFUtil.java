package de.bo.base.swing.text;

import java.io.*;

/**
 * Diese Klasse enthält diverse static-Methode zur Konvertierung von
 * UTF-Strings und Unicode-Strings, sowie zur Längenbestimmung selbiger.
 * Zur Umsetzung zwischen einzelnen CodePages siehe
 * <A HREF="http://java.sun.com/products/jdk/1.1/docs/guide/intl/encoding.doc.html">
 * Supported Encodings</A>.
 *
 * @version 0.01 1.8.1999
 * @author Dagobert Michelsen
 **/

public class UTFUtil {

	/**
	 * Diese Funktion wandelt eine UTF-Zeichenfolge in einen Unicode-String
	 * um.
	 * Die Kodierung entspricht derejnigen in der
	 * <A HREF="http://java.sun.com/docs/books/jls/html/javaio.doc1.html#28916">
	 * UTF8-Spezifikation</A>, bis auf das die ersten beiden Byte zur Angabe der
	 * Länge hier nicht enthalten sind (die Länge wird ja bereits durch den
	 * String spezifiziert).
	 * @param s die UTF-Zeichenfolge
	 * @return den umgewandelten Unicode-String
	 **/
	public static final String UTFToString( String s )
	throws UTFDataFormatException {
		if( s == null ) {
			return "";
		}
		byte	b[]			= s.getBytes();
		int		l				= s.length();
		char	str[]		= new char[ l ];
		int		pos			= 0;
		int		strlen	= 0;
		while( pos < l ) {
			int c = (b[ pos++ ] & 0xff);
			int char2, char3;
			switch( c >> 4 ) {
				case 0: case 1: case 2: case 3: case 4: case 5: case 6: case 7:
					// 0xxxxxxx
					str[ strlen++ ] = (char) c;
					break;
				case 12: case 13:
					// 110x xxxx   10xx xxxx
					if( pos >= l )
						throw new UTFDataFormatException();
					char2 = (b[ pos++ ] & 0xff);
					if( (char2 & 0xC0) != 0x80 )
						throw new UTFDataFormatException();
					str[ strlen++ ] = (char) (((c & 0x1F) << 6) | (char2 & 0x3F));
					break;
				case 14:
					// 1110 xxxx  10xx xxxx  10xx xxxx
					if( pos + 1 >= l )
						throw new UTFDataFormatException();
					char2 = (b[ pos++ ] & 0xff);
					char3 = (b[ pos++ ] & 0xff);
					if (((char2 & 0xC0) != 0x80) || ((char3 & 0xC0) != 0x80))
						throw new UTFDataFormatException();
					str[ strlen++ ] = (char)(((c & 0x0F) << 12) |
						 ((char2 & 0x3F) << 6) |
						 ((char3 & 0x3F) << 0));
					break;
				default:
					// 10xx xxxx,  1111 xxxx
					throw new UTFDataFormatException();
			}
		}
		return new String( str, 0, strlen );
	}

	/**
	 * Diese Funktion wandelt einen Unicode-String in eine UTF8-Zeichenfolge um.
	 * @param s der Unicode-String
	 * @return die UTF8-Zeichenfolge dieses Strings
	 **/
	public static final String StringToUTF( String s ) {
		if( s == null ) {
			return "";
		}
		char	str[]		= new char[ s.length() * 3 ];
		int		strlen	= 0;
		int		l				= s.length();
		for( int i = 0 ; i < l; i++ ) {
			int c = s.charAt(i);
			if( (c >= 0x0001) && (c <= 0x007F) ) {
				str[ strlen++ ] = (char) c;
			} else if( c > 0x07FF ) {
				str[ strlen++ ] = (char) (0xE0 | ((c >> 12) & 0x0F));
				str[ strlen++ ] = (char) (0x80 | ((c >>  6) & 0x3F));
				str[ strlen++ ] = (char) (0x80 | ((c >>  0) & 0x3F));
			} else {
				str[ strlen++ ] = (char) (0xC0 | ((c >>  6) & 0x1F));
				str[ strlen++ ] = (char) (0x80 | ((c >>  0) & 0x3F));
			}
		}
		return new String( str, 0, strlen );
	}

	/**
	 * Diese Funktion liefert zu einem Unicode-String die Länge des
	 * korrespondierenden UTF8-Strings in Byte.
	 * @param s der Unicode-String, von dem die Länge bestimmt werden soll
	 * @return die Länge des entsprechenden UTF8-Strings in Byte
	 **/
	public static final int UTFLength( String s ) {
		if( s == null ) {
			return 0;
		}
		int	l				= s.length();
		int length	= 0;
		for( int i = 0 ; i < l; i++ ) {
			int c = s.charAt(i);
			if( (c >= 0x0001) && (c <= 0x007F) )	{	length += 1;	}
			else if( c > 0x07FF )									{	length += 3;	}
			else																	{	length += 2;	}
		}
		return length;
	}

	/*
	public static void main(String[] args) {
		String s = StringToUTF( "ABC\u0000\u1234" );
		try {
			String t = UTFToString( s );
			System.out.println( t );
		} catch( UTFDataFormatException e ) {
			e.printStackTrace();
		}
	}
*/
}
