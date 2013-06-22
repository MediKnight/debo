package de.baltic_online.base.application;

public class OptionType
{
  protected char option;
  protected boolean hasParam;

  public OptionType(char option,boolean hasParam)
  {
    this.option = option;
    this.hasParam = hasParam;
  }
  public char getOption()
  {
    return option;
  }
  public boolean isParamRequired()
  {
    return hasParam;
  }
  public String toString()
  {
    return "Option "+option+((hasParam)?" parameter required":"");
  }
}
