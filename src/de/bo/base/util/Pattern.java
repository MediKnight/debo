package de.bo.base.util;

public class Pattern
{
  /***************************************************
   *
   * glob string matching -- just use regular expressions?
   *    * = any sequence including the null string
   *    ? = any single character
   *  [chars] = any character in range
   *   \x = match x (for escaping above metacharacters)
   *
   ***************************************************/

  public static boolean match(String pattern,String txt) {
    boolean match=true;   // matched so far?

    for (int i=0,j=0; match && i<pattern.length(); i++,j++) {
      if (i==pattern.length() && j==txt.length()) return true;

      char p = pattern.charAt(i);
      char t = txt.charAt(j);

      switch (p) {
      case '?':
	// matches anything
	break;

      case '[':
	p = pattern.charAt(++i);
	// special case if first character after `[' is `]'
	match = false;
	while (!match && p!=']') {
	  if (p==t) match=true;
	  else {
	    char p2 = pattern.charAt(++i);
	    if (p2=='-') {  // range
	      p2 = pattern.charAt(++i);
	      if (p<=t && t<=p2) match=true;
	    } else p=p2;
	  }
	}
	while (pattern.charAt(i)!=']') i++;
	break;

      case '*':
	// more efficient if take into account number of non-*
	// tokens left in pattern
	char c;
	int minpost=0;
	for (int ci=i+1; ci<pattern.length(); ci++) {
	  c = pattern.charAt(ci);
	  switch (c) {
	  case '?': minpost++; break;
	  case '[':
	    // no matter what it is, ignore following
	    // character (if `]', it's the special case)
	    ci+=2;

	    while ((pattern.charAt(ci)) != ']') ci++;
	    minpost++;      // range matches only single character
	    break;
	  case '\\':
	    ci++; minpost++; break;
	  case '*':
	    minpost+=0; break;      // may be null match
	  default:
	    minpost++;
	  }
	}
	// System.out.println("\tfor \""+pattern.substring(i+1)+"\", minpost="+minpost);

	// biased toward long matches
	match = false;
	for (int skip=txt.length()-minpost-j; !match && skip>=0; skip--) {
	  //System.out.println("\tmatching \""+pattern.substring(i+1)+"\" with \""+txt.substring(j+skip)+"\"");
	  match = Pattern.match(pattern.substring(i+1), txt.substring(j+skip));
	}
	return match;

      case '\\':
	p = pattern.charAt(++i);
	// fall through to default

      default:      // plain characters
	match = (p==t);
	// else keep on truckin'
      }
    }

    return match;
  }
}
