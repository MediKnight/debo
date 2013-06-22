/*
 * @(#)$Id:
 */

package de.baltic_online.base.swing;

/**
 * This Swing component is an alternative for a TabControl.
 * It resembles the navigation control as seen in StarOffice.
 *
 * @example
 * <code>
 * import de.baltic_online.base.swing.*;
 * import java.awt.*;
 * import java.awt.event.*;
 * import javax.swing.*;
 * import java.util.*;
 *
 * public class NavControlDemo extends JFrame {
 *
 *   public NavControlDemo() {
 *     setTitle("NavControl Demo");
 *     addWindowListener (new WindowAdapter () {
 *       public void windowClosing (WindowEvent evt) {
 *         System.exit (0);
 *       }
 *     });
 *     NavControl n = new NavControl();
 *     Object[] v1 = {"Your", "are", "the", "first"};
 *     n.addCard(new JList(v1), "First");
 *     Object[] v2 = {"red", "green", "blue", "yellow"};
 *     n.addCard(new JList(v2), "Colors");
 *     Object[] v3 = {"crash", "boom", "bang"};
 *     n.addCard(new JList(v3), "Sounds");
 *     Object[] v4 = {"You", "are", "the", "last", "my", "everything"};
 *     n.addCard(new JList(v4), "Last");
 *     getContentPane().add(n, BorderLayout.WEST);
 *     final JTextField textField = new JTextField();
 *     getContentPane ().add (textField, java.awt.BorderLayout.CENTER);
 *     n.addActionListener(new ActionListener() {
 *         public void actionPerformed(ActionEvent e) {
 *             textField.setText(e.getActionCommand());
 *         }
 *     });
 *     pack ();
 *   }
 *
 *   public static void main (String args[]) {
 *     new NavControlDemo().show ();
 *   }
 * }
 * </code>
 *
 * @version 1.0
 * @author Frank Tonn (ft)
 * @company Baltic Online Computer GmbH
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.*;

public class NavControl extends JPanel implements ActionListener {

    private static final long serialVersionUID = 1L;
    private static final Insets defaultInsets = new Insets(-2, 1, -2, 1);
    private static final Font plainFont = new Font("dialog", Font.PLAIN, 12);
    private static final Font boldFont = new Font("dialog", Font.BOLD, 12);

    private JPanel upperButtons;
    private JPanel cards;
    private JPanel lowerButtons;
    private ButtonGroup buttonGroup = new ButtonGroup();
    private Vector<ActionListener> listeners = new Vector<ActionListener>();

    /* Constructors
     * ---------------------------------------------------------------------- */

    /** Creates new form NavControl */
    public NavControl() {
        initComponents();
    }

    /* Building
     * ---------------------------------------------------------------------- */

    /** This method is used to add a card and create a corresponding button.*/
    public void addCard(JComponent card, String label /* JLabel label */
    ) {
        cards.add(card, label /*.getText()*/
        );
        //Create corresponding Button
        NavButton b = new NavButton(label);
        buttonGroup.add(b);
        b.setActionCommand(label /*.getText()*/
        );
        b.addActionListener(this);
        upperButtons.add(b);
        b.setSelected(true);
        ((CardLayout) cards.getLayout()).show(cards, label);
    }

    /** The constructor calls this method to initialize the component. */
    private void initComponents() {
        setLayout(new BorderLayout());

        upperButtons = new JPanel();
        upperButtons.setLayout(new GridLayout(0, 1));
        add(upperButtons, java.awt.BorderLayout.NORTH);

        cards = new JPanel();
        cards.setLayout(new CardLayout());
        add(cards, BorderLayout.CENTER);

        lowerButtons = new JPanel();
        lowerButtons.setLayout(new GridLayout(0, 1));
        add(lowerButtons, java.awt.BorderLayout.SOUTH);
    }

    /* Event handling
     * ---------------------------------------------------------------------- */

    /** Adds an ActionListener to the control */
    public void addActionListener(ActionListener l) {
        listeners.add(l);
    }

    /** removes an ActionListener from the control */
    public void removeActionListener(ActionListener l) {
        listeners.remove(l);
    }

    /** Notify all listeners. */
    protected void fireActionEvent(String label) {
        ActionEvent event =
            new ActionEvent(this, ActionEvent.ACTION_PERFORMED, label);
        Enumeration<ActionListener> listenerEnum = listeners.elements();
        while (listenerEnum.hasMoreElements())
             listenerEnum.nextElement().actionPerformed(event);
    }

    /**  This is the action handler called by the NavButtons. */
    public void actionPerformed(final ActionEvent e) {
        Component source = (Component) e.getSource();
        if (source.getParent().equals(upperButtons))
            shuffleDown(source);
        else
            shuffleUp(source);
        validate();
    }

    /* This method is called if one of the upper buttons has been clicked. */
    private void shuffleDown(Component c) {
        boolean found = false;
        Vector<Component> todo = new Vector<Component>();
        //Find the clicked button and put everything below it into the Todo list
        for (int i = 0; i < upperButtons.getComponentCount(); i++) {
            if (found)
                todo.add(upperButtons.getComponent(i));
            else
                found = (upperButtons.getComponent(i).equals(c));
        }
        if (todo.isEmpty() && lowerButtons.getComponentCount() > 0) {
            //The lowermost of the upper buttons has been clicked,
            //take the uppermost of the lower buttons and shuffle it up.
            shuffleUp(lowerButtons.getComponent(0));
            return;
        }
        //Remove the Todo-buttons from the upper buttons
        Enumeration<Component> components;
        for (components = todo.elements(); components.hasMoreElements();)
            upperButtons.remove(components.nextElement());
        //Remove all lower buttons and put them on the Todo list
        while (lowerButtons.getComponentCount() > 0) {
            Component next = lowerButtons.getComponent(0);
            todo.add(next);
            lowerButtons.remove(next);
        }
        //Add all Todo-buttons to the lower buttons
        for (components = todo.elements(); components.hasMoreElements();)
            lowerButtons.add(components.nextElement());
        ((CardLayout) cards.getLayout()).show(cards, ((NavButton) c).getText());
        fireActionEvent(((NavButton) c).getText());
    }

    /** This method is called if one of the lower buttons has been clicked. */
    private void shuffleUp(Component c) {
        boolean found = false;
        while (!found && lowerButtons.getComponentCount() > 0) {
            NavButton next = (NavButton) lowerButtons.getComponent(0);
            found = (next.equals(c));
            lowerButtons.remove(next);
            upperButtons.add(next);
            next.setSelected(true);
        }
        ((CardLayout) cards.getLayout()).show(cards, ((NavButton) c).getText());
        fireActionEvent(((NavButton) c).getText());
    }

    /* Member classes
     * ---------------------------------------------------------------------- */

    /** This inner class is used for the buttons. We need a subclass
      * instead of JButton itself, because we want to suppress focus
      * traversal. */
    private class NavButton extends JButton implements ItemListener {

      private static final long serialVersionUID = 1L;

        /** Creates new NavButton */
        @SuppressWarnings("unused")
        public NavButton(JLabel label) {
            this(label.getText(), label.getIcon());
        }

        public NavButton(String label) {
            this(label, null);
        }

        public NavButton(String label, Icon icon) {
            super(label, icon);
            setMargin(defaultInsets);
            setFont(plainFont);
            setRequestFocusEnabled(false);
            setModel(new JToggleButton.ToggleButtonModel());
            getModel().addItemListener(this);
        }

        public void itemStateChanged(ItemEvent i) {
            if (i.getStateChange() == ItemEvent.SELECTED)
                setFont(boldFont);
            else
                setFont(plainFont);
        }

        /** This method is needed to suppress focus traversal additionally
          * to the call to #setRequestFocusEnabled in constructor. */
        public boolean isFocusable() {
            return false;
        }
    }
}
