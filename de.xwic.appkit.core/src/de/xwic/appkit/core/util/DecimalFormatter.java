package de.xwic.appkit.core.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * @author Adrian Ionescu
 */
public class DecimalFormatter {

	private static DecimalFormat dfDoubleValue = new DecimalFormat("#,##0.00");
	private static DecimalFormat dfDoubleValueOptionalDecimals = new DecimalFormat("#,##0.##");
	private static DecimalFormat dfDoubleValueNoSeparator = new DecimalFormat("#0.00");
	private static DecimalFormat dfDoubleValueNoSeparatorOptionalDecimal = new DecimalFormat("#0.##");
	private static DecimalFormat dfStringValue = new DecimalFormat("#");

	private static NumberFormat numberFormatForPsaUpdates;

	static {
		numberFormatForPsaUpdates = NumberFormat.getInstance(Locale.US);
		numberFormatForPsaUpdates.setMinimumFractionDigits(2);
		numberFormatForPsaUpdates.setMaximumFractionDigits(2);
		numberFormatForPsaUpdates.setGroupingUsed(false);
	}

	/**
	 * @return
	 */
	public static String formatForPsaUpdates(Double value) {
		return numberFormatForPsaUpdates.format(value);
	}

	/**
	 * @return
	 */
	public static DecimalFormat getDoubleFormatter() {
		return dfDoubleValue;
	}

	/**
	 * @param value
	 * @return
	 */
	public static String formatDoubleValueOptionalDecimals(double value) {
		return dfDoubleValueOptionalDecimals.format(value);
	}

	/**
	 * @return
	 */
	public static String formatDoubleValue(double value) {
		return dfDoubleValue.format(value);
	}

	/**
	 * @return
	 */
	public static String formatDoubleValueNoSeparator(double value) {
		return dfDoubleValueNoSeparator.format(value);
	}

	/**
	 * @param value
	 * @return
	 */
	public static String formatDoubleValueNoSeparatorOptionalDecimal(double value) {
		return dfDoubleValueNoSeparatorOptionalDecimal.format(value);
	}

	/**
	 * 
	 * @return
	 */
	public static String formatStringValue(double value) {
		return dfStringValue.format(value);
	}
}
