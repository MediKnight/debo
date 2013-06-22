/*
 *
 */

package de.baltic_online.base.swing;

import javax.swing.event.*;

import de.baltic_online.base.util.*;

/**
 *
 */
public interface BillAmountModel {

    public static final int NONE = 1;
    public static final int MORE = 2;
    public static final int LESS = 3;

    public static final int BRUTTO = 4;
    public static final int NETTO  = 5;


    void setVAT( double vat );
    void setVAT( double vat, boolean override );
    double getVAT();
    CurrencyNumber getAmountVAT();

    void setComputationTarget( int target );
    int getComputationTarget();

    void setAmountBrutto( CurrencyNumber bruttoAmount, boolean override );
    void setAmountBrutto( CurrencyNumber bruttoAmount );
    CurrencyNumber getAmountBrutto();

    void setAmountNetto( CurrencyNumber nettoAmount, boolean override );
    void setAmountNetto( CurrencyNumber nettoAmount );
    CurrencyNumber getAmountNetto();

    void setCorrection( int correction );
    int getCorrection();

    void addChangeListener( ChangeListener listener );
    void removeChangeListener( ChangeListener listener );
}
