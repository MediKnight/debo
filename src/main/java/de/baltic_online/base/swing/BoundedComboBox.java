package de.baltic_online.base.swing;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.plaf.basic.*;

import java.util.*;

public class BoundedComboBox<E> extends JComboBox<E>
{
  private static final long serialVersionUID = 1L;

  public BoundedComboBox(int cols,int bound) {
    super();
    initEditor(cols,bound);
  }

  public BoundedComboBox(ComboBoxModel<E> aModel,int cols,int bound) {
    super(aModel);
    initEditor(cols,bound);
  }

  public BoundedComboBox(E[] items,int cols,int bound) {
    super(items);
    initEditor(cols,bound);
  }

  public BoundedComboBox(Vector<E> items,int cols,int bound) {
    super(items);
    initEditor(cols,bound);
  }

  protected void initEditor(int cols,int bound) {
    setEditor(new Editor(cols,bound));
    setEditable(true);
  }

  public Document getDocument() {
    Editor ed = (Editor)getEditor();
    return ed.getDocument();
  }

  public void setText(String text) {
    Editor ed = (Editor)getEditor();
    ed.setText(text);
  }

  public String getText() {
    Editor ed = (Editor)getEditor();
    return ed.getText();
  }

  public Dimension getPreferredSize() {
    Editor ed = (Editor)getEditor();
    return ed.getEditor().getPreferredSize();
  }

  public Dimension getMinimumSize() {
    Editor ed = (Editor)getEditor();
    return ed.getEditor().getMinimumSize();
  }

  public Dimension getMaximumSize() {
    Editor ed = (Editor)getEditor();
    return ed.getEditor().getMaximumSize();
  }

  public boolean hasFocus() {
    if ( super.hasFocus() ) return true;

    Editor ed = (Editor)getEditor();
    return ed.getEditor().hasFocus();
  }

  public void addActionListener(ActionListener l) {
    super.addActionListener(l);
    getEditor().addActionListener(l);
  }

  public void removeActionListener(ActionListener l) {
    getEditor().removeActionListener(l);
    super.removeActionListener(l);
  }

  public static class Editor extends BasicComboBoxEditor
  {
    Editor(int cols,int bound) {
      editor = new BoundedTextField(cols,bound);
    }

    Document getDocument() {
      return editor.getDocument();
    }

    void setText(String text) {
      editor.setText(text);
    }

    String getText() {
      return editor.getText();
    }

    BoundedTextField getEditor() {
      return (BoundedTextField)editor;
    }
  }
}
