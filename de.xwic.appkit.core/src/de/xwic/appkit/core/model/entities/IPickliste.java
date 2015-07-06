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
/*
 * de.xwic.appkit.core.model.entities.IPicklistenTyp
 * Created on 07.07.2005
 *
 */
package de.xwic.appkit.core.model.entities;

import de.xwic.appkit.core.dao.IEntity;

/**
 * PT - Picklistentyp.
 * 
 * @author Florian Lippisch
 */
public interface IPickliste extends IEntity {
	// global boolean pickliste will be "hidded", but still created
	// public final static String PL_GLOBAL_BOOLEAN = "global.bool";
	// public final static String PL_GLOBAL_BOOLEAN_PARTIALLY_YES =
	// "global.bool.partially";

	// particular boolean pickliste will be defined for each context
	/**
	 * zusatz pickliste for Kontakt objects
	 */
	public final static String PL_CO_ZUSATZ = "co.zusatz";

	/**
	 * isMaGefahr pickliste for Mandate objects
	 */
	public final static String PL_MA_IS_MA_GEFAHR = "ma.isMaGefahr";

	/**
	 * kreditAufnahme pickliste for Mandate objects
	 */
	public final static String PL_MA_KREDIT_AUFNAHME = "ma.kreditAufnahme";

	/**
	 * wpAnleihe pickliste for Mandate objects
	 */
	public final static String PL_MA_WP_ANLEIHE = "ma.wpAnleihe";

	/**
	 * Option pickliste for Mandate objects
	 */
	public final static String PL_MA_OPTION = "ma.option";

	/**
	 * isSteuerPflicht pickliste for Mandate objects
	 */
	public final static String PL_MA_IS_STEUER_PFLICHT = "ma.isSteuerPflicht";

	/**
	 * isZwischenGewinn pickliste for Mandate objects
	 */
	public final static String PL_MA_IS_ZWISCHEN_GEWINN = "ma.isZwischenGewinn";

	/**
	 * isErtragsAusgleich pickliste for Mandate objects
	 */
	public final static String PL_MA_IS_ERTRAGS_AUSGLEICH = "ma.isErtragsAusgleich";

	/**
	 * isProvVerteilung pickliste for Mandate objects
	 */
	public final static String PL_MA_IS_PROV_VERTEILUNG = "ma.isProvVerteilung";

	/**
	 * isPerformMessung pickliste for Mandate objects
	 */
	public final static String PL_MA_IS_PERFORM_MESSUNG = "ma.isPerformMessung";
	
	/**
	 * vertriebsart pickliste for Mandate objects
	 */
	public final static String PL_MA_VERTIEBSART = "ma.vertriebsart";
	
	/**
	 * klassifizierung pickliste for Mandate objects
	 */
	public final static String PL_MA_KLASSIFIZIERUNG = "ma.klassifizierung";

	/**
	 * abstimmtag pickliste for Mandate objects
	 */
	public final static String PL_MA_ABSTIMMTAG = "ma.abstimmtag";
	
	/**
	 * geschaeftsfeld pickliste for Unternehmen objects
	 */
	public final static String PL_UN_GESCHAEFTSFELD = "un.geschaeftsfeld";

	/**
	 * rechenschaftBericht pickliste 
	 */
	public final static String PL_MK_RECHENSCHAFT_BERICHT = "mk.rechenschaftBericht";

	/**
	 * pruefBericht pickliste
	 */
	public final static String PL_MK_PRUEF_BERICHT = "mk.pruefBericht";

	/**
	 * iwbEmpfaenger pickliste
	 */
	public final static String PL_MK_IWB_EMPFAENGER = "mk.iwbEmpfaenger";

	/**
	 * stati pickliste for Unternehmen objects
	 */
	public final static String PL_UN_STATUS = "un.stati";

	/**
	 *  prio pickliste for Unternehmen objects
	 */
	public final static String PL_UN_PRIO = "un.prio";

	/**
	 * kundenGrp pickliste for Unternehmen objects
	 */
	public final static String PL_UN_KUNDENGRUPPE = "un.kundenGrp";

	/**
	 * segment pickliste for Unternehmen objects
	 */
	public final static String PL_UN_SEGMENT = "un.segment";

	/**
	 * mitbewerber pickliste for Unternehmen objects
	 */
	public final static String PL_UN_MITBEWERBER = "un.mitbewerber";

	/**
	 * potenzialProd pickliste for Unternehmen objects
	 */
	public final static String PL_UN_POTENZIALPRODUKT = "un.potenzialProd";

	/**
	 * produkt pickliste for Unternehmen objects
	 */
	public final static String PL_UN_PRODUKT = "un.produkt";

	/**
	 * iclmRegion pickliste for Unternehmen objects
	 */
	public final static String PL_UN_ICLMREGION = "un.iclmRegion";

	/**
	 * gkc pickliste for Unternehmen objects
	 */
	public final static String PL_UN_GKC = "un.gkc";

	/**
	 * publicumsfonds pickliste for Unternehmen objects
	 */
	public final static String PL_UN_PUBLIKUMSFONDS = "un.publikumsfonds";

	/**
	 * einheit artistTitle pickliste for Kontakt objects
	 */
	public final static String PL_CM_EINHEIT = "cm.einheit";

	// public final static String PL_CM_ROLLE = "cm.rolle";
	/**
	 * rolle pickliste for Unternehmen objects
	 */
	public final static String PL_UN_CM_ROLLE = "un.cm.rolle";

	/**
	 * rolle pickliste for Mandate objects
	 */
	public final static String PL_MA_CM_ROLLE = "ma.cm.rolle";

	/**
	 * rolle pickliste for Kontakt objects
	 */
	public final static String PL_MA_CO_ROLLE = "ma.co.rolle";

	/**
	 * land pickliste for Adresse objects
	 */
	public final static String PL_AD_LAND = "ad.land";

	/**
	 *  investFokus pickliste for Mandate objects
	 */
	public final static String PL_MA_INVEST_FOKUS = "ma.investFokus";

	/**
	 *  kundenAuftrag pickliste for Mandate objects
	 */
	public final static String PL_MA_KUNDEN_AUFTRAG = "ma.kundenAuftrag";

	/**
	 *  rechteHuelle pickliste for Mandate objects
	 */
	public final static String PL_MA_RECHTE_HUELLE = "ma.rechteHuelle";

	/**
	 * depotBank pickliste for Mandate objects
	 */
	public final static String PL_MA_DEPOT_BANK = "ma.depotBank";

	/**
	 * kag pickliste for Mandate objects
	 */
	public final static String PL_MA_KAG = "ma.kag";

	/**
	 * basisWaehrung pickliste for Mandate objects
	 */
	public final static String PL_MA_BASIS_WAEHRUNG = "ma.basisWaehrung";

	/**
	 * waehrungSicher pickliste for Mandate objects
	 */
	public final static String PL_MA_WAEHRUNG_SICHER = "ma.waehrungSicher";

	/**
	 * kundenZufriedenheit pickliste for Mandate objects
	 */
	public final static String PL_MA_KUNDEN_ZUFRIEDENHEIT = "ma.kundenZufriedenheit";

	/**
	 * performMessungVon pickliste for Mandate objects
	 */
	public final static String PL_MA_PERFORM_MESSUNG_VON = "ma.performMessungVon";

	/**
	 * ma.segment pickliste for Mandate objects
	 */
	public final static String PL_MA_SEGMENT = "ma.segment";

	/**
	 * kundenGrp pickliste for Mandate objects
	 */
	public final static String PL_MA_KUNDENGRUPPE = "ma.kundenGrp";

	/**
	 * iclmRegion pickliste for Mandate objects
	 */
	public final static String PL_MA_ICLMREGION = "ma.iclmRegion";

	/**
	 * iwb_versandart pickliste for Mandate-Kontakte objects
	 */
	public final static String PL_MK_IWB_VERSANDART = "mk.iwb_versandart";

	/**
	 * closeGrund pickliste for Mandate objects
	 */
	public final static String PL_MA_CLOSE_GRUND = "ma.closeGrund";

	/**
	 * vertriebswege pickliste for Mandate objects
	 */
	public final static String PL_MA_VERTIEBSBEWEGE = "ma.vertriebswege";
	
	/**
	 * maRestriktion pickliste for Mandate objects
	 */
	public final static String PL_MA_MANDAT_RESTRIKTION = "ma.maRestriktion";

	/**
	 * konditionGebuehr pickliste for Mandate objects
	 */
	public final static String PL_MA_KONDITION_GEBUEHR = "ma.konditionGebuehr";

	/**
	 * gefahrGrund pickliste for Mandate objects
	 */
	public final static String PL_MA_GEFAHR_GRUND = "ma.gefahrGrund";

	/**
	 * bereich pickliste for Kampagne objects
	 */
	public final static String PL_KA_BEREICH = "ka.bereich";

	/**
	 * art pickliste for Kampagne objects
	 */
	public final static String PL_KA_VERSANDART = "ka.art";

	/**
	 * Returns the Beschreibung of the picklist.
	 * 
	 * @return
	 */
	public String getBeschreibung();

	/**
	 * Set the Beschreibung of the picklist.
	 * 
	 * @param beschreibung
	 */
	public void setBeschreibung(String beschreibung);

	/**
	 * Returns the key of the picklist.
	 * 
	 * @return
	 */
	public String getKey();

	/**
	 * Set the key of the picklist.
	 * 
	 * @param key
	 */
	public void setKey(String key);

	/**
	 * Returns the Title of the Pickliste.
	 * 
	 * @return
	 */
	public String getTitle();

	/**
	 * Set the Title of the Pickliste.
	 * 
	 * @param title
	 */
	public void setTitle(String title);

}
