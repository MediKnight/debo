package de.baltic_online.base.application;

import java.util.*;

public class OptionProperties extends Properties
{
  private static final long serialVersionUID = 1L;
  protected Vector<OptionType> knownOption;
  protected String args[];

  public OptionProperties(String args[]) {
    this.args = args;

    knownOption = new Vector<OptionType>( 30, 10 );

    // trace level
    defineOption( 't', true );

    // help page and exit
    defineOption( 'h', false );

    // language code
    defineOption( 'L', true );

    // working directory
    defineOption( 'w', true );
  }

  public void defineOption(char option,boolean hasParam) {
    knownOption.addElement( new OptionType(option,hasParam) );
  }
  public void defineOption(OptionType ot) {
    knownOption.addElement( ot );
  }

  protected void traceMessage(String msgResource,String os) {
    StdApplication app = StdApplication.getApplication();
    String msg = app.getResource( msgResource ) +
      ": \"" + os +"\"";
    app.getTracer().trace( "*", this, msg );
  }

  public void getOptions() {
    int argc = args.length;

    for ( int i=0; i<argc; i++ ) {
      // read tokens

      // expected option and length
      String s = args[i].trim();
      int sl = s.length();

      // ignore empty option
      // (they can arise by applying trim() in quotes)
      if ( sl == 0 ) continue;

      // expect '-'
      char oc = s.charAt( 0 );

      // no '-' char or standalone '-' char:
      if ( oc != '-' || sl == 1 ) {
	traceMessage( "InvalidOption", s );
	continue;
      }

      // oc becomes option char
      oc = s.charAt( 1 );

      // validation check
      boolean valid = false;
      boolean param = false;

      for ( int j=0; j<knownOption.size(); j++ ) {
	OptionType ot = knownOption.elementAt(j);
	if ( oc == ot.getOption() ) {
	  valid = true;
	  param = ot.isParamRequired();
	  break;
	}
      }

      // symbol is not valid
      if ( !valid ) {
	traceMessage( "InvalidOption", s );
	continue;
      }

      // symbol is valid (ocs is appropiate string)
      Character occ = new Character( oc );
      String ocs = occ.toString();

      // param expected ?
      if ( param ) {

	// if true, starts param immediate after symbol or exists
	// whitespaces between symbol and param ?
	if ( sl == 2 ) {

	  // is there any next param ?
	  if ( i < argc - 1 )
	    put( ocs, args[++i].trim() );
	  else
	    traceMessage( "MissingOptionParam", s );
	}
	else
	  put( ocs, s.substring( 2 ) );
      }
      else
	// without param assignment is easy:
	put( ocs, "" );
    } // endfor i
  }
}
