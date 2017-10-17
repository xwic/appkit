/*
 * Copyright (c) 2009 Network Appliance, Inc.
 * All rights reserved.
 */

package de.xwic.appkit.webbase.entityselection.people;

import org.json.JSONException;
import org.json.JSONWriter;

import de.jwic.json.IObjectToJsonSerializer;
import de.xwic.appkit.core.model.entities.IMitarbeiter;

/**
 * @author Lippisch
 */
public class EmployeeJsonSerializer implements IObjectToJsonSerializer {

	/* (non-Javadoc)
	 * @see de.jwic.json.IObjectToJsonSerializer#serialize(java.lang.Object, org.json.JSONWriter)
	 */
	@Override
	public void serialize(Object object, JSONWriter out)	throws JSONException {
		
		IMitarbeiter ef = (IMitarbeiter) object;
		
		out.object();
		
		out.key("zusatz");
		out.value(ef.getZusatz() != null ? ef.getZusatz() : "");
		
		out.key("phone");
		out.value(ef.getTelefon());

		out.key("mobile");
		out.value(ef.getHandyNr());

		out.key("email");
		out.value(ef.getEmail());
		
		out.endObject();
		
	}

}
