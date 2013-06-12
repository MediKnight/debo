package de.bo.base.application;

import java.util.*;
import java.io.*;

import de.bo.base.util.*;
import de.bo.base.util.log.*;

public class StdApplication
implements TraceConstants
{
  /**
   * The name of this package.
   * <p><b>
   * Important: Update this member if you plan to move this source
   * to an other package!</b>
   */
  protected final static String packageName = "de.bo.base.application";

  /**
   * Current instance of the application.
   * <p>
   * This member must be created by the function <code>main()</code>
   * and passed through the function <code>stdMain()</code>.
   * <p>
   * Typical use:
   * <pre>
   * public class MyApplication extends StdApplication
   * {
   *   public static void main(String[] args) {
   *     stdMain( new MyApplication(), args );
   *   }
   *   ...
   * }
   * </pre>
   */
  protected static StdApplication application;

  /**
   * Locale used by this application.
   *
   * The constructor creates this member by using the
   * default locale. It can be changed by the function
   * <code>setResources</code>.
   *
   * @see #setResources(Locale)
   */
  protected Locale locale;

  /**
   * This is the instance of all resources installed in this package.
   */
  protected ResourceBundle defaultResources;

  /**
   * This is the instance of all resources defined by the developer.
   */
  protected ResourceBundle userResources;

  /**
   * Commandline options.
   *
   * @see OptionProperties
   */
  protected OptionProperties optionProperties;

  /**
   * Options supplied by a configuration file.
   */
  protected Properties configProperties;

  /**
   * Working options.
   * <p>
   * This is a mixture of the config and the option properties.
   * If there exists common entries, option properties have
   * higher priority.
   */
  protected Properties workingProperties;

  /**
   * Working directory of the application.
   * <p>
   * This directory name is builded appending the home directory
   * with the value returned by the method
   * <code>getRelativeWorkingDirectory()</code>.
   *
   * @see #getRelativeWorkingDirectory
   */
  protected String workingDirectory;

  /**
   * Directory of the current application.
   */
  protected String applicationDirectory;

  /**
   * Complete configuration path.
   */
  protected String propertyPath;

  protected String helpText;
  /**
   * Tracing and debugging.
   *
   * @see Tracer
   */
  protected Tracer tracer;

  static {
    Tracer.addTraceClass( INFO );
    Tracer.addTraceClass( WARNING );
    Tracer.addTraceClass( ERROR );
    Tracer.addTraceClass( DEBUG );
    Tracer.addTraceClass( JDBC );
    Tracer.addTraceClass( RMI );
  }

  /**
   * Creates application by using the default locale.
   *
   * @see #locale
   */
  protected StdApplication() {
    try {
      configProperties = new Properties();

      helpText = "";

      // and a tracer
      tracer = new Tracer( System.err );

      // set locale and resources
      setResources( Locale.getDefault() );
    }
    catch ( MissingResourceException mre ) {
      warn( getResourceName()+".properties not found" );
    }
  }

  /**
   * Main entry for application instances.
   *
   * This function interprets options and configuration files
   * and should be called direct from the <code>main</code>
   * function.
   * <p>
   * Typical use:
   * <pre>
   * public class MyApplication extends StdApplication
   * {
   *   public static void main(String[] args) {
   *     stdMain( new MyApplication(), args );
   *   }
   *   ...
   * }
   * </pre>
   * <p>
   * Implementation:
   * <pre>
   * protected static void stdMain(StdApplication app,String args[]) {
   *   application = app;
   *   application.getOptions( args );
   *   application.getConfig();
   *   application.init();
   * }
   * </pre>
   * <p>
   * The functions <code>getOptions()</code> and
   * <code>getConfig</code> are private but they call the functions
   * <code>addKnownOptions()</code> and <code>setDefaultConfig()</code>
   * which can be overwrite to define valid options and default
   * configurations.
   *
   * @param app instance of the application
   * @param args command line parameters
   *
   * @see #application
   * @see #addKnownOptions
   * @see #setDefaultConfig
   * @see #init
   */
  protected static void stdMain(StdApplication app,String args[]) {
    application = app;
    application.getOptions( args );
    application.getConfig();
    application.init();
  }

  /**
   * Defines valid commandline options.
   * <p>
   * Defines valid commandline options by overwriting this method.
   * <p>
   * This function is called by <code>stdMain</code>.
   * <p>
   * Typical implementation:
   * <pre>
   * protected void addKnownOptions(OptionProperties options) {
   *   // option 'R' without parameter:
   *   options.addKnownOption( new OptionType( 'R', false ) );
   *
   *   // option 't' with parameter:
   *   options.addKnownOption( new OptionType( 't', true ) );
   * }
   * </pre>
   *
   * @see #stdMain
   * @see #optionProperties
   * @see OptionProperties
   * @see OptionType
   */
  protected void addKnownOptions(OptionProperties options) {
  }

  /**
   * Defines a default configuration.
   * <p>
   * Defines a default configuration by overwriting this method.
   * <p>
   * Typical implementation:
   * <pre>
   * protected void setDefaultConfig(Properties conf) {
   *   conf.put( "WindowSize", "600x400" );
   *   conf.put( "SaveOnExit", "true" );
   * }
   * </pre>
   *
   * @param conf properties to change
   *
   * @see #propertyPath
   * @see #stdMain
   * @see #saveConfig
   * @see #loadConfig
   */
  protected void setDefaultConfig(Properties conf) {
  }

  /**
   * Dummy for initialisation tasks.
   * <p>
   * This function does nothing but derived classes should
   * overwrite this function for initialisation tasks.
   * <p>
   * This function is called by <code>stdMain</code> when all
   * options, resources and configuration settings applied.
   *
   * @see #application
   * @see #stdMain
   */
  protected void init() {
  }

  /**
   * Returns instance of the application.
   *
   * @return instance of the application
   *
   * @see #application
   */
  public final static StdApplication getApplication() {
    return application;
  }

  /**
   * Creates <code>Locale</code> instance from a given string.
   * <p>
   * The string param must match following format:
   * <p>
   * <code>LL</code>
   * <p> or
   * <p>
   * <code>LL_CC</code>
   * <p> or
   * <p>
   * <code>LL_CC_VV</code>
   * <p>
   * <code>LL</code> means language <code>CC</code> country
   * and <code>VV</code> variation (not often used).
   * <p>
   * Examples:
   * <p><code>de</code> or <code>de_CH</code> (german, switzerland).
   *
   * @param ls locale string
   */
  public static Locale createLocale(String ls) {
    StringTokenizer st = new StringTokenizer( ls, "_" );
    String lang = "";
    String country = "";
    String var = null;
    if ( st.hasMoreTokens() )
      lang = st.nextToken();
    if ( st.hasMoreTokens() )
      country = st.nextToken();
    if ( st.hasMoreTokens() )
      var = st.nextToken();

    if ( var == null )
      return new Locale( lang, country );

    return new Locale( lang, country, var );
  }

  /**
   * Set resources an locale.
   *
   * This function changes the current locale and perhaps the
   * resource members <code>defaultResources</code> and
   * <code>userResources</code> (depends on locale).
   * If this function succeeds all message output (i.e. tracing)
   * will change.
   *
   * @param locale new Locale
   * @exception MissingResourceException if appropiate resources will
   * not be found.
   *
   * @see #locale
   */
  public void setResources(Locale locale)
    throws MissingResourceException {

      this.locale = locale;

      String defName = packageName+".resources.default";
      defaultResources = ResourceBundle.getBundle( defName, locale );
      userResources = ResourceBundle.getBundle( getResourceName(), locale );
  }

  /**
   * Path of the user resource file (relativ to the application
   * directory).
   * <p>
   * Implementation:
   * <pre>
   * public String getResourceName() {
   *   return "resources."+getClass().getName();
   * }
   * </pre>
   * <p>
   *
   * @see #applicationDirectory
   */
  public String getResourceName() {
    return "resources."+getClass().getName();
  }

  /**
   * @deprecated Use <tt>getUserResources</tt> instead.
   * @see #getUserResources()
   */
  public ResourceBundle getResources() {
    return userResources;
  }

  public ResourceBundle getUserResources() {
    return userResources;
  }

  public ResourceBundle getDefaultResources() {
    return defaultResources;
  }

  public Locale getLocale() {
    return locale;
  }

  /**
   * Returns matching string resource of the given key.
   *
   * This function looks in the user resources first and if no
   * matching resource is found it searches the default resources.
   *
   * @param key resource key
   * @return matching resource or <code>null</code> if no resource
   * is found
   */
  public String getResource(String key) {
    String s = null;
    try {
      if ( userResources != null )
	s = userResources.getString( key );
    }
    catch ( Exception x ) {
      try {
	s = defaultResources.getString( key );
      }
      catch ( Exception xx ) {
	tracer.trace( WARNING, this, xx );
      }
    }
    return s;
  }

  /**
   * Returns the name of the configuration directory
   * (relative to the user directory).
   * <p>
   * Implementation:
   * <pre>
   * public String getRelativeWorkingDirectory() {
   *   return "."+getClass().getName();
   * }
   * </pre>
   *
   * @return name of the configuration directory
   *
   * @see #workingDirectory
   */
  public String getRelativeWorkingDirectory() {
    return "."+getClass().getName();
  }

  public String getConfigDirectory() {
    return workingDirectory;
  }

  /**
   * Returns the name of the configuration file
   * (relative to the configuration directory).
   * <p>
   * Implementation:
   * <pre>
   * public String getPropertyName() {
   *   return "config";
   * }
   * </pre>
   *
   * @return name of the configuration file
   *
   * @see #getRelativeWorkingDirectory
   */
  public String getPropertyName() {
    return "config";
  }

  /**
   * Get function for <code>applicationDirectory</code>.
   *
   * @see #applicationDirectory
   */
  public String getApplicationDirectory() {
    return applicationDirectory;
  }

  /**
   * Set function for <code>tracer</code>.
   *
   * @see #tracer
   */
  public void setTracer(Tracer tracer) {
    this.tracer = tracer;
  }

  public void setHelpText(String text) {
    helpText = text;
  }

  public String getHelpText() {
    return helpText;
  }

  /**
   * Get function for <code>tracer</code>.
   *
   * @see #tracer
   */
  public Tracer getTracer() {
    return tracer;
  }

  private void getOptions(String args[]) {
    String s;

    optionProperties = new OptionProperties( args );
    addKnownOptions( optionProperties );
    optionProperties.getOptions();

    s = optionProperties.getProperty( "L" );
    if ( s != null ) {
      try {
	setResources( createLocale( s ) );
      }
      catch ( MissingResourceException mre ) {
	warn( getResourceName()+".properties not found" );
      }
    }

    s = optionProperties.getProperty( "t" );
    if ( s != null ) {
      String[] sa = StringTools.getTokens( s, "," );
      for ( int i=0; i<sa.length; i++ ) {
	tracer.enable( sa[i] );
      }
      tracer.trace( INFO, this, optionProperties );
    }

    s = optionProperties.getProperty( "h" );
    if ( s != null ) {
      System.out.println( getHelpText() );

      String[] sa = Tracer.getTraceClasses();
      int n = sa.length;

      System.out.print( "Known trace classes: " );
      for ( int i=0; i<n; i++ ) {
	System.out.print( sa[i] );
	if ( i < n-1 )
	  System.out.print( "," );
      }
      System.out.println( "" );

      System.exit( 0 );
    }    
  }

  private void buildFileNames() {
    Properties sp = System.getProperties();
    String homeDir = sp.getProperty( "user.home" );
    String userDir = sp.getProperty( "user.dir" );
    if ( userDir == null )
      fatal( getResource( "NoUserDir" ) );

    String s = optionProperties.getProperty( "w" );
    if ( s != null )
      workingDirectory = s;
    else {
      // Determine home directory
      // If no home directory exists then we use the current
      // working directory

      if ( homeDir == null ) {
	warn( getResource( "NoHomeDir" ) );
	workingDirectory = userDir;
      }
      else
	workingDirectory = homeDir;

      workingDirectory += File.separator + getRelativeWorkingDirectory();
    }
    applicationDirectory = userDir;
    propertyPath = workingDirectory + File.separator + getPropertyName();
  }

  /**
   * This method checks if the given file exists and tries to create
   * the file if not.
   * <p>
   * The application stops if the file cannot be created.
   *
   * @param fname filename
   *
   * @see #fatal
   * @see #createDirectoryIfNecessary
   */
  protected void createFileIfNecessary(String fname) {
    try {
      File f = new File( fname );
      if ( !f.exists() ) {
	tracer.trace( INFO, this, "creating "+fname );
	new FileOutputStream( f );
      }
      else
	if ( !f.isFile() ) {
	  String s = getResource( "NotAFile1" ) + fname;
	  s += getResource( "NotAFile2" );
	  fatal( s );
	}
    }
    catch ( IOException ioe ) {
      fatal( fileCreateError( fname ) );
    }
  }

  /**
   * This method checks if the given directory exists and tries to create
   * the directory if not.
   * <p>
   * The application stops if the directory cannot be created.
   *
   * @param dname directory
   *
   * @see #fatal
   * @see #createFileIfNecessary
   */
  protected void createDirectoryIfNecessary(String dname) {
    File f = new File( dname );
    if ( !f.exists() ) {
      tracer.trace( INFO, this, "creating "+dname );
      if ( !f.mkdirs() ) {
	String s = getResource( "CantCreateDir1" ) + dname;
	s += getResource( "CantCreateDir2" );
	fatal( s );
      }
    }
    else {
      if ( !f.isDirectory() ) {
	String s = getResource( "NotADir1" ) + dname;
	s += getResource( "NotADir2" );
	fatal( s );
      }
    }
  }

  private void createWorkingFilesIfNecessary() {
    createDirectoryIfNecessary( workingDirectory );
    createFileIfNecessary( propertyPath );
  }

  private void getConfig() {
    buildFileNames();
    createWorkingFilesIfNecessary();
    loadConfig( configProperties );

    // mix config and options
    workingProperties = new Properties();
    Enumeration<?> e = configProperties.propertyNames();
    while ( e.hasMoreElements() ) {
      String key = e.nextElement().toString();
      String s = optionProperties.getProperty( key );
      if ( s != null )
	workingProperties.put( key, s );
      else
	workingProperties.put( key, configProperties.getProperty(key) );
    }
    tracer.trace( INFO, this, workingProperties );
  }

  /**
   * Save configuration.
   * <p>
   * This method saves given configuration to the file
   * given by <code>propertyPath</code>.
   * 
   * @param conf configuration
   *
   * @see #propertyPath
   * @see #loadConfig
   */
  public void saveConfig(Properties conf) {
    try {
      File f = new File( propertyPath );
      FileOutputStream fos = new FileOutputStream( f );
      conf.store( fos, getResource( "ConfigHeader" ) );
      tracer.trace( INFO, this, "saving config file ..." );
      fos.close();
    }
    catch ( IOException ioe ) {
      warn( fileWriteError( propertyPath ) );
    }
  }

  /**
   * Save configuration.
   * <p>
   * This method saves <code>configProperties</code> to the file
   * given by <code>propertyPath</code>.
   *
   * @see #propertyPath
   * @see #loadConfig
   */
  public void saveConfig() {
    saveConfig( configProperties );
  }

  /**
   * Load configuration.
   * <p>
   * This method loads configuration from the file given by
   * <code>propertyPath</code>.
   * 
   * @param conf configuration
   *
   * @see #propertyPath
   * @see #setDefaultConfig
   * @see #saveConfig
   */
  public void loadConfig(Properties conf) {
    try {
      File f = new File( propertyPath );
      FileInputStream fis = new FileInputStream( f );
      conf.clear();

      Properties tmp = new Properties();
      setDefaultConfig( tmp );

      conf.load( fis );
      fis.close();
      int confCount = conf.size();

      Enumeration<?> e = tmp.propertyNames();
      while ( e.hasMoreElements() ) {
	String key = e.nextElement().toString();
	String s = conf.getProperty( key );
	if ( s == null )
	  conf.put( key, tmp.getProperty(key) );
      }

      if ( confCount == 0 ) {
	tracer.trace( INFO, this, "empty config: writing defaults ..." );
	saveConfig( conf );
      }

      tracer.trace( INFO, this, conf );
    }
    catch ( IOException ioe ) {
      warn( fileReadError( propertyPath ) );
    }
  }

  public String getProperty(String key) {
    return workingProperties.getProperty( key );
  }

  public void setProperty(String key,String value) {
    configProperties.setProperty( key, value );
  }

  /**
   * Creates a warning message using the tracer.
   *
   * @param msg warning message
   *
   * @see #tracer
   */
  public void warn(String msg) {
    tracer.trace( WARNING, this, getResource( "Warning" )+": "+msg+"!" );
  }

  /**
   * Creates an error message using the tracer and
   * stops the application.
   *
   * @param msg error message
   *
   * @see #tracer
   */
  public void fatal(String msg) {
    tracer.trace( ERROR, this, getResource( "Fatal" )+": "+msg+"!" );
    tracer.trace( ERROR, this, getResource( "FatalEnd" ) );
    System.exit( 1 );
  }

  /**
   * Returns locale depended error message for
   * file read errors.
   */
  public String fileReadError(String fname) {
    String s = getResource( "CantReadFile1" ) + fname;
    s += getResource( "CantReadFile2" );
    return s;
  }

  /**
   * Returns locale depended error message for
   * file open errors.
   */
  public String fileOpenError(String fname) {
    String s = getResource( "CantOpenFile1" ) + fname;
    s += getResource( "CantOpenFile2" );
    return s;
  }

  /**
   * Returns locale depended error message for
   * file write errors.
   */
  public String fileWriteError(String fname) {
    String s = getResource( "CantWriteFile1" ) + fname;
    s += getResource( "CantWriteFile2" );
    return s;
  }

  /**
   * Returns locale depended error message for
   * file creation errors.
   */
  public String fileCreateError(String fname) {
    String s = getResource( "CantCreateFile1" ) + fname;
    s += getResource( "CantCreateFile2" );
    return s;
  }

  /**
   * @deprecated use <tt>getTracer()</tt> instead
   * @see #getTracer
   * @see Tracer
   */
  public static void debugMsg(String s) {
  }
}
