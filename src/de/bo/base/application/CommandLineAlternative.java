package de.bo.base.application;

public class CommandLineAlternative extends CommandLineArgument
{
  private String shortKey;

  public CommandLineAlternative(String shortKey,String longKey) {
    super(longKey,PROPERTY_TYPE);
    this.shortKey = shortKey;
  }

  public CommandLineAlternative(String shortKey,
				String longKey,
				boolean valueRequired) {
    super(longKey,valueRequired,PROPERTY_TYPE);
    this.shortKey = shortKey;
  }

  public boolean equals(Object o) {

    if ( o instanceof CommandLineAlternative )
      return super.equals(o) &&
	shortKey.equals(((CommandLineAlternative)o).shortKey);

    CommandLineArgument cla = (CommandLineArgument)o;
    CommandLineArgument test = (cla.getType()==PROPERTY_TYPE) ?
      new CommandLineArgument(getKey(),PROPERTY_TYPE) :
      new CommandLineArgument(shortKey);

    return test.equals(cla);
  }
}
