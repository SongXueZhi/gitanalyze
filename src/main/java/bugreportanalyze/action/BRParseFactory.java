package bugreportanalyze.action;
/**
 * 
 * @author knightsong
 *
 */
public class BRParseFactory {

	public BRParse getBRParse(String parseType) {
	    if (parseType.equalsIgnoreCase("bz")) {
			return new BugzillaParse();
		}else if (parseType.equalsIgnoreCase("jr")) {
			return new JiraParse();
		}else {
			return new  BRParse();
		}
	}
}
