package de.bo.base.application;

/**
 * Instances of this class are used by the class
 * <tt>CommandLineProperties</tt>
 *
 * @see CommandLineProperties
 *
 * @author sml
 */
public class CommandLineArgument
{
  /**
   * Constant for the argument type (not used).
   */
  public final static int ARGUMENT_TYPE = 0;

  /**
   * Constant for the argument type (default).
   */
  public final static int OPTION_TYPE = 1;

  /**
   * Constant for the argument type.
   */
  public final static int PROPERTY_TYPE = 2;

  private String key;
  private boolean valueRequired;
  private int type;

  /**
   * Creates an argument with the given key, with no value
   * required and with type <tt>OPTION_TYPE</tt>.
   *
   * @param key the key of this option
   */
  public CommandLineArgument(String key) {
    this(key,false,OPTION_TYPE);
  }

  /**
   * Creates an argument with the given key, with eventually value
   * required and with type <tt>OPTION_TYPE</tt>.
   *
   * @param key the key of this option
   * @param valueRequired determines if the key needs a value
   */
  public CommandLineArgument(String key,boolean valueRequired) {
    this(key,valueRequired,OPTION_TYPE);
  }

  /**
   * Creates an argument with the given key, with no value
   * required and with the given type.
   *
   * @param key the key of this option
   * @param type the type of this option
   */
  public CommandLineArgument(String key,int type) {
    this(key,false,type);
  }

  /**
   * Creates an argument with the given key, with eventually value
   * required and with the given type.
   *
   * @param key the key of this option
   * @param valueRequired determines if the key needs a value
   * @param type the type of this option
   */
  public CommandLineArgument(String key,boolean valueRequired,int type) {
    setKey(key);
    setValueRequired(valueRequired);
    setType(type);
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getKey() {
    return key;
  }

  public void setValueRequired(boolean valueRequired) {
    this.valueRequired = valueRequired;
  }

  public boolean isValueRequired() {
    return valueRequired;
  }

  public void setType(int type) {
    if ( type < ARGUMENT_TYPE || type > PROPERTY_TYPE )
      throw new IllegalArgumentException();
    this.type = type;
  }

  public int getType() {
    return type;
  }

  public boolean equals(Object o) {
    CommandLineArgument arg = (CommandLineArgument)o;
    if ( !getKey().equals(arg.getKey()) )
      return false;
    return getType()==arg.getType();
  }

  public String toString() {
    return getKey();
  }
}
