package global;

import java.util.HashMap;
import java.util.Map;

/**
 * Country map properties utility
 * 
 * @author Lev Rado, edited by Yehuda Ginsburg
 */
public class CountriesMap {

	/**
	 * ReUse from AutomationCloud Util class, 
	 * 
	 * @return Map of String and String
	 * array with the country alpha numeric code as the key
	 */
	public static final Map<String, String[]> getMap() {
		 Map<String, String[]> getMap = new HashMap<String, String[]>();
         getMap.put("AR", new String[] { "54",	"1123456789", "722", "Argentina (722)" });
         getMap.put("AU", new String[] { "61",	"212345678", "505", "Australia (505)" });
         getMap.put("AT", new String[] { "43",	"1234567890", "232", "Austria (232)" });
         getMap.put("BE", new String[] { "32", 	"12345678", "206", "Belgium (206)" });
         getMap.put("CA", new String[] { "1", 	"2042345678", "302", "Canada (302)" });
         getMap.put("CN", new String[] { "86", 	"1012345678", "460", "China (460)" });
         getMap.put("CY", new String[] { "357", "22345678", "280", "Cyprus (280)" });
         getMap.put("DK", new String[] { "45", 	"32123456", "238", "Denmark (238)" });
         getMap.put("FR", new String[] { "33", 	"123456789", "208", "France (208)" });
         getMap.put("DE", new String[] { "49", 	"30123456", "262", "Germany (262)" });
         getMap.put("GR", new String[] { "30", 	"2123456789", "202", "Greece (202)" });
         getMap.put("HK", new String[] { "852", "21234567", "454", "Hong Kong (454)" });
         getMap.put("HU", new String[] { "36", 	"12345678", "216", "Hungary (216)" });
         getMap.put("IN", new String[] { "91", 	"1123456789", "404", "India (404)" });
         getMap.put("IL", new String[] { "972", "21234567", "425", "Israel (425)" });
         getMap.put("IT", new String[] { "39", 	"3471234567", "222", "Italy (222)" });
         getMap.put("MX", new String[] { "52",	"2221234567", "334", "Mexico (334)" });
         getMap.put("NL", new String[] { "31", 	"101234567", "204", "Netherlands (204)" });
         getMap.put("PL", new String[] { "48",	"123456789", "260", "Poland (260)" });
         getMap.put("PT", new String[] { "351", "212345678", "268", "Portugal (268)" });
         getMap.put("RO", new String[] { "40", "211234567", "226", "Romania (226)" });
         getMap.put("RU", new String[] { "7", "3011234567", "250", "Russia (250)" });
         getMap.put("SG", new String[] { "65", "61234567", "525", "Singapore (525)" });
         getMap.put("ZA", new String[] { "27", "101234567", "655", "South Africa (655)" });
         getMap.put("ES", new String[] { "34", "810123456", "214", "Spain (214)" });
         getMap.put("SE", new String[] { "46", "8123456", "240", "Sweden (240)" });
         getMap.put("CH", new String[] { "41", "212345678", "228", "Switzerland (228)" });
         getMap.put("TH", new String[] { "66", "21234567", "520", "Thailand (520)" });
         getMap.put("TR", new String[] { "90", "2123456789", "286", "Turkey (286)" });
         getMap.put("GB", new String[] { "44", "1212345678", "234", "UK (234)" });
         getMap.put("UA", new String[] { "380", "311234567", "255", "Ukraine (255)" });
         getMap.put("US", new String[] { "1", "2015555555", "310", "USA (310)" });
		return getMap;
	}
	
	/**
	 * Enter the Country code and return the full country name
	 * 
	 * @param countryCode
	 * @return full country name
	 */
	public static String getFullCountryName(String countryCode) {
		return CountriesMap.getMap().get(countryCode)[3];
	}
	
}
