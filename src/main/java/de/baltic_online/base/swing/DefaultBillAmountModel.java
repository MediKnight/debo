/*
 *
 */

package de.baltic_online.base.swing;


import de.baltic_online.base.util.*;


/**
 *
 */
public class DefaultBillAmountModel extends AbstractBillAmountModel {

    private static final long serialVersionUID = 1L;

    public static final String CLASSNAME = "DefaultBillAmountModel";

    /**
     *
     */
    CurrencyNumber brutto;

    /**
     *
     */
    CurrencyNumber netto;

    /**
     *
     */
    double vat;

    /**
     *
     */
    int correction;

    /**
     *
     */
    int computationTarget;


    /**
     *
     */
    public DefaultBillAmountModel() {
	brutto = null;
	netto = null;
	vat = -1.0;
	correction = NONE;
	computationTarget = NETTO;
    }


    /**
     *
     */
    public void setComputationTarget( int target ) {
	computationTarget = target;
    }

    /**
     *
     */
    public int getComputationTarget() {
	return computationTarget;
    }


    /**
     *
     */
    public void setVAT( double vat, boolean override ) {
	if( this.vat != vat ) {
	    this.vat = vat;

	    correction = NONE;

	    if( ! override ) {
		calculate( NONE );
	    }

	    fireStateChanged();
	}
    }

    /**
     *
     */
    public void setVAT( double vat ) {
	setVAT( vat, false );
    }

    /**
     *
     */
    public double getVAT() {
	return vat;
    }

    /**
     *
     */
    public CurrencyNumber getAmountVAT() {
	if( brutto != null && netto != null ) {
	    CurrencyNumber vatPart = (CurrencyNumber) brutto.clone();
	    vatPart.sub( netto );

	    return vatPart;
	} else {
	    return null;
	}
    }



    /**
     *
     */
    public void setAmountBrutto( CurrencyNumber bruttoAmount, boolean override ) {
	boolean recalculate = false;

	if( brutto == bruttoAmount ) {
	    return;
	}

	if( bruttoAmount != null ) {
	    if( brutto == null ) {
		recalculate = true;
	    } else {
		if( brutto.compareTo( bruttoAmount ) != 0 ) {
		    //
		    // 
		    //

		    recalculate = true;
		}
	    }
	} else {
	    //
	    // Der zusetzende Wert ist null und der alte Wert ist ungleich null.
	    //

	    recalculate = true;
	}

	if( recalculate ) {
	    brutto = bruttoAmount;

	    if( ! override ) {
		correction = NONE;
		calculate( BRUTTO );
	    }

	    fireStateChanged();
	}
    }

    /**
     *
     */
    public void setAmountBrutto( CurrencyNumber bruttoAmount ) {
	setAmountBrutto( bruttoAmount, false );
    }

    /**
     *
     */
    public CurrencyNumber getAmountBrutto() {
	return brutto;
    }


    /**
     *
     */
    public void setAmountNetto( CurrencyNumber nettoAmount, boolean override ) {
	boolean recalculate = false;

	if( netto == nettoAmount ) {
	    return;
	}

	if( nettoAmount != null ) {
	    if( netto == null ) {
		recalculate = true;
	    } else {
		if( netto.compareTo( nettoAmount ) != 0 ) {
		    //
		    // 
		    //

		    recalculate = true;
		}
	    }
	} else {
	    //
	    // Der zusetzende Wert ist null und der alte Wert ist ungleich null.
	    //

	    recalculate = true;
	}

	if( recalculate ) {
	    netto = nettoAmount;

	    if( ! override ) {
		correction = NONE;
		calculate( NETTO );
	    }

	    fireStateChanged();
	}
    }

    /**
     *
     */
    public void setAmountNetto( CurrencyNumber nettoAmount ) {
	setAmountNetto( nettoAmount, false );
    }


    /**
     *
     */
    public CurrencyNumber getAmountNetto() {
	return netto;
    }


    /**
     *
     */
    public void setCorrection( int correction ) {
	if( this.correction != correction ) {
	    this.correction = correction;

	    calculate( NONE );
	    fireStateChanged();
	}
    }


    /**
     *
     */
    public int getCorrection() {
	return correction;
    }


    /**
     *
     */
    synchronized protected void calculate( int valueChanged ) {
	if( valueChanged == BRUTTO && brutto == null ) {
	    netto = null;
	    return;
	}

	if( valueChanged == NETTO && netto == null ) {
	    brutto = null;
	    return;
	}

	if( vat >= 0.0 &&
	    ((computationTarget == BRUTTO && netto != null) ||
	     (computationTarget == NETTO && brutto != null)) ) {

	    //
	    // Wir haben ueberhaupt einen validen Fall - im nicht initialisierten Zustand
	    // ist vat < 0.
	    //

	    switch( valueChanged ) {
	    case BRUTTO:
		if( brutto != null ) {
		    netto = (CurrencyNumber) brutto.clone();
		    netto.div( 1.0 + vat );
		} 

		break;

	    case NETTO:
		if( netto != null ) {
		    brutto = (CurrencyNumber) netto.clone();
		    brutto.mul( 1.0 + vat );
		}

		break;

	    case NONE:
		if( computationTarget == NETTO && brutto != null ) {
		    netto = (CurrencyNumber) brutto.clone();
		    netto.div( 1.0 + vat );
		} else if( computationTarget == BRUTTO && netto != null ) {
		    brutto = (CurrencyNumber) netto.clone();
		    brutto.mul( 1.0 + vat );
		}

		break;

	    }

	    //
	    // Jetzt die Hunderstel-Korrektur durchfuehren.
	    //
	    
	    if( correction != NONE ) {
		CurrencyNumber valueToCorrect = null;
		
		if( computationTarget == NETTO ) {
		    valueToCorrect = netto;
		} else {
		    valueToCorrect = brutto;
		}
		
		CurrencyNumber correctionValue =
		    new CurrencyNumber( 0.01, valueToCorrect.getCurrency() );
		
		if( correction == LESS ) {
		    correctionValue.negate();
		}
		
		valueToCorrect.add( correctionValue );
	    }
	}
    }


    /**
     *
     */
    public String toString() {
	String result = new String();

	result += "Brutto: ";

	if( brutto != null ) {
	    result += brutto.toString();
	} else {
	    result += "unset";
	}

	result += "\nNetto: ";

	if( netto != null ) {
	    result += netto.toString();
	} else {
	    result += "unset";
	}

	result += "\nVAT: " + vat;

	result += "\nCorrection: " + correction;

	return result;
    }
}

