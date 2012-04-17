/**
 * 
 */
package de.xwic.appkit.core.model.util;

import java.util.Locale;

/**
 * Datatype to store currency values. <p>
 * 
 * The value is stored as long value. Internally, 4 digits<br>
 * after the delimiter is calculated. That means, the long<br>
 * value is "multiplied with 1000".<br><br>
 * 
 * Example: <br><br>
 * 
 * "123,33" is stored as "1233300" long.<br>
 * The displayed String will be rounded up after the third
 * digit.<br>
 * "65545400" will be displayed as "65,55" and means fully "65,5454". <br><br>
 * The display String will also be devided with thousand delimiter.<br>
 * That means "1234560000" will be displayed as "123.456,00".
 * 
 * @author Ronny Pfretzschner
 *
 */
public class Currency {

	/**
	 * The delimiter -> "," in Germany, "." in any other country
	 */
	private static String delimiter = Locale.GERMAN.getLanguage().equals(Locale.getDefault().getLanguage()) ? "," : ".";
	
	/**
	 * The thousand delimiter -> "." in Germany, "," in any other country
	 */
	private static String thousandDelimiter = Locale.GERMAN.getLanguage().equals(Locale.getDefault().getLanguage()) ? "." : ",";
	
	private long longValue = 0;
	private String displayString = "0" + delimiter + "00";
	
	/**
	 * Creates a Currency Object "0,00" or "0.00". <p>
	 *
	 * Depending on VM properties. 
	 */
	public Currency() {
	}
	
	/**
	 * Creates a Currency Object with the given long value. <p>
	 * 
	 * @param val
	 */
	public Currency(long val) {
		this.longValue = val;
		this.displayString = createDisplayString();
	}
	
	/**
	 * Creates a Currency Object with the given String value. <p>
	 * 
	 * If the String is not parseable, 0.00 or 0,00 is set. <br>
	 * Depending on VM properties. 
	 * 
	 * @param value
	 */
	public Currency(String value) {
		this.longValue = createValueFlat(value);
		displayString = createDisplayString();
	}
	
	/**
	 * Create a constructor from a double value.
	 * @param price
	 */
	public Currency(double price) {
		this.longValue = (long)(price * 10000.0);
		displayString = createDisplayString();
	}

	/**
	 * Creates a proper displayable String of the existing long value. <p>
	 * 
	 * @return a String like 123,33
	 */
	private String createDisplayString() {
		String raw = Long.toString(longValue);
		String erg = null;

		//set negative flag, work with string without "-"
		boolean isNegative = false;
		if (raw.startsWith("-")) {
			isNegative = true;
			raw = raw.substring(1, raw.length());
		}
		
		//small values, check directly and set erg to 0.00
		long tempLongVal = isNegative ? (longValue * (-1)) : longValue;
		
		if (tempLongVal >= 0 && tempLongVal <= 49) {
			erg = "0" + delimiter + "00";
		}
		
		//bigger values, round could be possible
		else if (raw.length() >= 2) {
			//get digit for round criteria
			char roundChar = raw.charAt(raw.length() - 2);
			
			//get int valCounter for char (maybe unnecessary)
			int roundInt = Character.getNumericValue(roundChar);
			
			//get the addition counter
			String addition = raw.substring(0, raw.length() - 2);

			//the rule, bigger than 4 round up
			if (roundInt >= 5) {
				//small number like 99 -> must be 0,01
				if (addition.length() == 0) {
					addition = "1";
				}
				else {
					//count up and set addition value as String
					long tempVal = Long.parseLong(addition);
					tempVal++;
					addition = Long.toString(tempVal);
				}
			}
			//devide into big val and after delimiter value
			if (addition.length() == 1) {
				erg = "0" + delimiter + "0" + addition;
			}
			else if (addition.length() == 2) {
				erg = "0" + delimiter + addition;
			}
			//addition is big enough do display before delimiter numbers
			else {
				String inFrontNumbers = addition.substring(0, addition.length() - 2);
				inFrontNumbers = formatFrontNumbers(inFrontNumbers);
				
				erg = inFrontNumbers + delimiter + 
						addition.substring(addition.length() - 2, addition.length());
			}
			//set negative flag
			erg = (isNegative ? "-" : "") + erg; 
		}
		return erg;
	}

	/**
	 * Formats the front numbers with specific thousand delimiter character. <p>
	 * 
	 * @param inFrontNumbers
	 * @return the formated number
	 */
	private String formatFrontNumbers(String inFrontNumbers){
		StringBuffer sb = new StringBuffer();
		
		//nothing -> return empty string
		if (inFrontNumbers == null) {
			return "";
		}
		
		//get character array
		char[] chars = inFrontNumbers.toCharArray();
		int elCounter = 1;
		
		//loop through backwards -> to get 3number step sequences 
		for (int i = chars.length - 1; i >= 0; i--) {
			sb.append(chars[i]);
			
			//not at beginning and if number is a block of 3 AND not at and to avoid .123,33 stuff
			if (i != chars.length - 1 && elCounter % 3 == 0 && i > 0 ) {
				sb.append(thousandDelimiter);
			}
			//helper
			elCounter++;
		}
		//reverse string to get proper sequence back
		sb = sb.reverse();
		return sb.toString();
	}
	
	/**
	 * Tries to get a long out of the given String. <p>
	 * 
	 * If any nonsense is given, 0 is returned.
	 * 
	 * @param currencyValue
	 * @return a long value, 0 if any error occoured
	 */
	private long createValueFlat(String currencyValue) {
		long temp = 0;
		boolean isNegative = false;
		
		try {
			//nothing, 0 is default
			if (currencyValue == null || currencyValue.length() == 0 || currencyValue.equals(",") || currencyValue.equals(".")) {
				return 0;
			}
			//forgot leading stuff -> set "0" in front
			if (currencyValue.startsWith(".") || currencyValue.startsWith(",")) {
				currencyValue = "0" + currencyValue;
			}
			
			//negative check, set flag and cut off "-"
			if (currencyValue.startsWith("-")) {
				isNegative = true;
				currencyValue = currencyValue.substring(1, currencyValue.length());
			}
			
			//get the index value of the delimiter
			int delimiterIndex = getDelimiterIndex(currencyValue);
			
			//default, no delimiter found
			String afterDelimiter = ""; 
			int afterDelimiterLength = 0;
				
			//delimiter found!
			if (delimiterIndex != -1) {
				//the numbers after delimiter
				afterDelimiter = currencyValue.substring(delimiterIndex + 1, currencyValue.length());

				//get the length -> with it, the value can be calculated
				afterDelimiterLength = afterDelimiter.length();
			}
				
			//delimiter position -> fill up with missing 0;
			if (afterDelimiterLength == 0) {
				temp = Long.parseLong((currencyValue.replaceAll("[\\,,\\.]", "")) + "0000");
			}
			else if (afterDelimiterLength == 1) {
				temp = Long.parseLong((currencyValue.replaceAll("[\\,,\\.]", "")) + "000");
			}
			else if (afterDelimiterLength == 2) {
				temp = Long.parseLong((currencyValue.replaceAll("[\\,,\\.]", "")) + "00");
			}
			else if (afterDelimiterLength == 3) {
				temp = Long.parseLong((currencyValue.replaceAll("[\\,,\\.]", "")) + "0");
			}
			//delimiter was set on 4th position -> ###,1234
			else if (afterDelimiterLength == 4) {
				temp = Long.parseLong(currencyValue.replaceAll("[\\,,\\.]", ""));
			}
			
			//check, if delimiter is set somewhere far in front of number -> cut off last numbers!
			else if (afterDelimiterLength > 4) {
				String cutOff = currencyValue.substring(0, delimiterIndex + 5);
				temp = Long.parseLong(cutOff.replaceAll("[\\,,\\.]", ""));
			}
			//try to do anything, no delimiter found for example, just parse
			else {
				temp = Long.parseLong(currencyValue);
			}
		} catch (NumberFormatException nfe) {
			//error -> set 0
			temp = 0;
		}
		//calculate negative
		return isNegative ? ((-1) * temp) : temp;
	}
	
	/**
	 * Get the delimiter index, could be -1 if nothing is found. <p>
	 * 
	 * @param val
	 * @return
	 */
	private int getDelimiterIndex(String val) {
		int index = val.indexOf(delimiter);
		
		if (index == -1) {
			index = val.indexOf(".");
		}
		
		return index;
	}
	
	/**
	 * @return The String for display on screen.
	 */
	public String getDisplayString() {
		return displayString;
	}

	/**
	 * Returns the long value of the Currency. <p>
	 * 
	 * The long value is stored "*1000" of the displayed value to have exact values.
	 * 
	 * @return the longValue
	 */
	public long getLongValue() {
		return longValue;
	}
	
	/**
	 * JUST FOR HIBERNATE, AXIS and so on!. <p>
	 * 
	 * DO NOT USE THIS METHOD (It will work, but you shouldn't do it). <br>
	 * As long as this datatype acts like immutable.
	 * 
	 * @param longVal
	 */
	public void setLongValue(long longVal) {
		this.longValue = longVal;
		this.displayString = createDisplayString();
	}
	

	/**
	 * JUST FOR HIBERNATE, AXIS and so on!. <p>
	 * 
	 * DO NOT USE THIS METHOD (It will work, but you shouldn't do it). <br>
	 * As long as this datatype acts like immutable.
	 * 
	 * @param dispString
	 */
	public void setDisplayString(String dispString) {
		this.displayString = dispString;
		this.longValue = createValueFlat(dispString);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + (int) (longValue ^ (longValue >>> 32));
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Currency other = (Currency) obj;
		if (longValue != other.longValue)
			return false;
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return displayString;
	}

	/**
	 * @return
	 */
	public double doubleValue() {
		return (double)longValue / (double)10000;
	}

	/**
	 * Subtract the given value from this value, returning a new
	 * Currency instance.
	 * @param previous
	 * @return
	 */
	public Currency sub(Currency previous) {
		return new Currency(longValue - previous.longValue);
	}
	/**
	 * Subtract the given value from this value, returning a new
	 * Currency instance.
	 * @param previous
	 * @return
	 */
	public Currency add(Currency previous) {
		return new Currency(longValue + previous.longValue);
	}

	/**
	 * Multiply this currency with the given amount and return a new currency instance.
	 * @param amount
	 * @return
	 */
	public Currency multiply(int amount) {
		return new Currency(longValue * amount);
	}

	/**
	 * Divide by the amount.
	 * @param amount
	 * @return
	 */
	public Currency divide(int amount) {
		return new Currency(longValue / amount);
	}
}
