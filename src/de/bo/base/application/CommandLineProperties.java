package de.bo.base.application;

import java.util.*;
import java.io.*;

/**
 * This class provides command line parsing.
 * <p>
 * A command line argument must match to one of the following formats:
 * <ul>
 * <li><tt>-key [value]</tt> (old style option)
 * <li><tt>--key[=value]</tt> (Property like)
 * <li><tt>value</tt> (not an option, only an argument)
 * </ul>
 * The argument <tt>--</tt> itself tells the parser that all
 * remaining options are treated as arguments.
 * The special string <tt>-</tt> is treated as an argument too.
 * <p>
 * Common arguments (those parameters that are not options) won't
 * be stored in this <tt>Properties</tt>.
 *
 * @author sml
 */
public class CommandLineProperties extends Properties
{
  private static final long serialVersionUID = 1L;
  private String[] args;
  private List<CommandLineArgument> validArgs;

  private boolean acceptAllPropertyTypes;

  /**
   * Creates <tt>CommandLineProperties</tt> with the given command
   * line arguments and an array of expected arguments.
   * <p>
   * <b>The constructor does not retrieve arguments.</b>
   * Use the method <tt>retrieve()</tt> for it.
   *
   * @param args command line arguments as in
   * <tt>void main(String[] args)</tt>
   * @param validArgs array of expected arguments.
   *
   * @see #retrieve()
   */
  public CommandLineProperties(String[] args,
			       CommandLineArgument[] validArgs) {
    this(args,validArgs,false);
  }

  public CommandLineProperties(String[] args,
			       CommandLineArgument[] validArgs,
			       boolean acceptAllPropertyTypes) {
    this.args = args;
    this.validArgs = Arrays.asList(validArgs);
    this.acceptAllPropertyTypes = acceptAllPropertyTypes;
  }

  /**
   * Creates <tt>CommandLineProperties</tt> with the given command
   * line arguments and a list of expected arguments.
   * <p>
   * <b>The constructor does not retrieve arguments.</b>
   * Use the method <tt>retrieve()</tt> for it.
   *
   * @param args command line arguments as in
   * <tt>void main(String[] args)</tt>
   * @param validArgs list of expected arguments.
   *
   * @see #retrieve()
   */
  public CommandLineProperties(String[] args,
			       List<CommandLineArgument> validArgs) {
    this(args,validArgs,false);
  }

  public CommandLineProperties(String[] args,
			       List<CommandLineArgument> validArgs,
			       boolean acceptAllPropertyTypes) {
    this.args = args;
    this.validArgs = validArgs;
    this.acceptAllPropertyTypes = acceptAllPropertyTypes;
  }

  public boolean isAcceptingAllPropertyTypes() {
    return acceptAllPropertyTypes;
  }

  public void setAcceptingAllPropertyTypes(boolean accepting) {
    acceptAllPropertyTypes = accepting;
  }

  /**
   * Adds an argument to the list of valid arguments.
   *
   * @param varg argument to add
   */
  public void addValidArgument(CommandLineArgument varg) {
    validArgs.add(varg);
  }

  /**
   * Removes an argument from the list of valid arguments.
   *
   * @param varg argument to remove
   */
  public void removeValidArgument(CommandLineArgument varg) {
    validArgs.remove(varg);
  }

  /**
   * Tests an argument with the list of valid arguments.
   *
   * @param varg argument to test
   * @return <tt>true</tt> if and only if the argument ist in the list
   *
   * @see #checkOption(CommandLineArgument)
   */
  public boolean isArgumentValid(CommandLineArgument varg) {
    return validArgs.contains(varg) ||
      (acceptAllPropertyTypes &&
       varg.getType() == CommandLineArgument.PROPERTY_TYPE);
  }

  /**
   * Tests an argument with the list of valid arguments.
   *
   * This method returns the matching argument from the list
   * of valid arguments if the parameter was found.
   *
   * @param option argument to test
   * @return the matching argument from the list of valid arguments
   * @exception IllegalArgumentException if the parameter is not
   * in the list of valid arguments
   *
   * @see #isArgumentValid(CommandLineArgument)
   */
  protected CommandLineArgument checkOption(CommandLineArgument option)
    throws IllegalArgumentException {

    Iterator<CommandLineArgument> it = validArgs.iterator();
    while ( it.hasNext() ) {
      CommandLineArgument arg = it.next();
      if ( arg.equals(option) )
	return arg;
    }
    throw new IllegalArgumentException("invalid option: "+option);
  }

  /**
   * This method retrieves and parses the command line arguments.
   * <p>
   * Parameters which do not start with <tt>"-"</tt> nor with <tt>"--"</tt>
   * are treated as common arguments, not as options. This function
   * returns those parameters as an array of strings.
   *
   * @return array of none option parameters
   * @exception IllegalArgumentException if at least one option is not
   * found in the list of valid arguments.
   */
  public String[] retrieve()
    throws IllegalArgumentException {

    // Leichter mit Iterator zu behandlen.
    Iterator<String> it = Arrays.asList(args).iterator();

    // R�ckgabe: Liste der Argumente, die keine Optionen sind.
    List<String> noOptions = new LinkedList<String>();

    // Ist true, wenn "--" passiert wurde.
    boolean noMoreOptions = false;

    while ( it.hasNext() ) {

      // Die erwartete Option:
      String s = it.next();

      // Leere Optionen (welche in Quotes auftauchen k�nnen)
      // werden ignoriert.
      if ( s.length() == 0 ) continue;

      // Behandle alles nach "--" als Argument, nicht als Option:
      if ( noMoreOptions )
	noOptions.add(s);

      // "--" wird passiert ...
      else if ( s.equals("--") )
	noMoreOptions = true;

      // "--" ist Prefix, d.h. Option ist Property (key=value)
      else if ( s.startsWith("--") )
	readProperty(s.substring(2));

      // Sezial-Argument "-"
      else if ( s.equals("-") )
	noOptions.add(s);

      // "-" ist Prefix, d.h. Option ist old-style (key [value])
      else if ( s.startsWith("-") )
	readOption(it,s.substring(1));

      // Kein Prefix, Option ist Argument.
      else
	noOptions.add(s);
    }

    // Argumente, die keine Optionen sind, zur�ckliefern:
    return (String[])noOptions.toArray(new String[0]);
  }

  private void readProperty(String s)
    throws IllegalArgumentException {

    try {
      Properties tmp = new Properties();
      ByteArrayInputStream bais = new
	ByteArrayInputStream((s+"\n").getBytes());
      tmp.load(bais);

      String key = tmp.propertyNames().nextElement().toString();
      CommandLineArgument arg = new
	CommandLineArgument(key,
			    CommandLineArgument.PROPERTY_TYPE);
      if ( !acceptAllPropertyTypes )
	arg = checkOption(arg);

      key = arg.getKey();
      setProperty(key,tmp.getProperty(key));
    }
    catch (NoSuchElementException nsex) {
      throw new IllegalArgumentException(s);
    }
    catch (IOException iox) { // gibst nicht
    }
  }

  private void readOption(Iterator<String> it,String key)
    throws IllegalArgumentException {

    CommandLineArgument arg =
      checkOption(new CommandLineArgument(key,
					  CommandLineArgument.OPTION_TYPE));
    try {
      String value = arg.isValueRequired() ? it.next() : "";
      setProperty(arg.getKey(),value);
    }
    catch (NoSuchElementException nsex) {
      throw new IllegalArgumentException("value required for "+key);
    }
  }

  public void combine(String srcKey,String destKey) {
    String srcValue = getProperty(srcKey);
    String destValue = getProperty(destKey);
    if ( destValue == null && srcValue != null )
      setProperty(destKey,srcValue);
    remove(srcKey);
  }
}
