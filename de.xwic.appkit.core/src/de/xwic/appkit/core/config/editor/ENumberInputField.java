/*******************************************************************************
 * Copyright 2015 xWic group (http://www.xwic.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *  
 *******************************************************************************/
package de.xwic.appkit.core.config.editor;

/**
 * A field used for parsing numbers
 * 
 * @author Andra Iacovici
 * @editortag number
 */
public class ENumberInputField extends EField {

    private static final String DOUBLE_FORMAT = "double";
    private static final String LONG_FORMAT = "long";
    /**
     * Number formatting, double by default.
     */
	private String format = DOUBLE_FORMAT;
    /**
     * Currency symbol if applicable.
     */
	private String currencySymbol;
    /**
     * Currency placement symbol relative to amount.
     * Values supported: left side to "p", right side to "s".
     */
	private String currencySymbolPlacement = "RIGHT";
    /**
     * Decimal points, will override values defined by format.
     */
	private Integer decimalPoints;

	/**
	 * @return Returns the number format.
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * The format of the number. default is "double"
	 * @param format
	 *            The number format to set.
	 */
	public void setFormat(String format) {
		this.format = format;
	}


	public String getCurrencySymbol() {
		return currencySymbol;
	}

	public void setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}

	public String getCurrencySymbolPlacement() {
		return currencySymbolPlacement;
	}

	public void setCurrencySymbolPlacement(String currencySymbolPlacement) {
		this.currencySymbolPlacement = currencySymbolPlacement;
	}

    public Integer getDecimalPoints() {
        return decimalPoints;
    }

    public void setDecimalPoints(Integer decimalPoints) {
        this.decimalPoints = decimalPoints;
    }

    @Override
    public String toString() {
        return "ENumberInputField{" +
                "format='" + format + '\'' +
                ", currencySymbol='" + currencySymbol + '\'' +
                ", currencySymbolPlacement='" + currencySymbolPlacement + '\'' +
                ", decimalPoints=" + decimalPoints +
                '}';
    }
}
