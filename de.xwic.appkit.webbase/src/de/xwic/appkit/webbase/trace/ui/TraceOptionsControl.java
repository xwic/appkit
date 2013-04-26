/**
 * 
 */
package de.xwic.appkit.webbase.trace.ui;

import de.jwic.base.ControlContainer;
import de.jwic.base.IControlContainer;
import de.jwic.controls.Button;
import de.jwic.controls.CheckBox;
import de.jwic.controls.ErrorWarning;
import de.jwic.controls.InputBox;
import de.jwic.controls.Label;
import de.jwic.controls.combo.DropDown;
import de.jwic.events.SelectionEvent;
import de.jwic.events.SelectionListener;
import de.xwic.appkit.core.trace.ITraceDataManager;
import de.xwic.appkit.core.trace.ITraceDataManager.TraceLevel;
import de.xwic.appkit.core.trace.Trace;

/**
 * Control the Trace facility and view trace statistics.
 * 
 * @author Florian Lippisch
 */
public class TraceOptionsControl extends ControlContainer {

	private ErrorWarning errorWarning;
	
	private Label lbTraceStatus;
	private Button btOnOff;
	private DropDown ddTraceLevel;
	private InputBox inpLogFile;
	private InputBox inpLogThreshold;
	private InputBox inpMaxHistory;
	private CheckBox chkLogEnabled;
	private CheckBox chkHistoryEnabled;
	private Button btUpdate;

	/**
	 * @param container
	 * @param name
	 */
	public TraceOptionsControl(IControlContainer container, String name) {
		super(container, name);
		
		createControls();
		updateStatus();
		
	}

	/**
	 * 
	 */
	private void updateStatus() {
		
		if (Trace.isEnabled()) {
			lbTraceStatus.setText("Enabled");
			btOnOff.setTitle("Turn Off");
		} else {
			lbTraceStatus.setText("Disabled");
			btOnOff.setTitle("Turn On");
		}
		
		ITraceDataManager dataManager = Trace.getDataManager();
		if (dataManager != null) {
			inpLogThreshold.setText(Long.toString(dataManager.getLogThreshold()));
			inpMaxHistory.setText(Integer.toString(dataManager.getMaxHistory()));
			if (dataManager.getTraceLevel() != null) {
				ddTraceLevel.setSelectedKey(dataManager.getTraceLevel().name());
				ddTraceLevel.setText(dataManager.getTraceLevel().name());
			} else {
				ddTraceLevel.setSelectedElement(null);
			}
			inpLogFile.setText(dataManager.getTraceLogFile() != null ? dataManager.getTraceLogFile().getAbsolutePath() : "");
			chkHistoryEnabled.setChecked(dataManager.isKeepHistory());
			chkLogEnabled.setChecked(dataManager.isLogTraceAboveThreshold());
			btUpdate.setEnabled(true);
		} else {
			btUpdate.setEnabled(false);
		}
		
	}

	/**
	 * 
	 */
	private void createControls() {
	
		errorWarning = new ErrorWarning(this, "errorWarning");
		
		lbTraceStatus = new Label(this, "lbTraceStatus");
		lbTraceStatus.setText("Unknown");
		
		btOnOff = new Button(this, "btOnOff");
		btOnOff.setTitle("?");
		btOnOff.addSelectionListener(new SelectionListener() {
			@Override
			public void objectSelected(SelectionEvent event) {
				onTurnOnOff();
			}
		});
		
		//----
		
		ddTraceLevel = new DropDown(this, "ddTraceLevel");
		for (TraceLevel tl : ITraceDataManager.TraceLevel.values()) {
			ddTraceLevel.addElement(tl.name(), tl.name());
		}
		ddTraceLevel.setWidth(250);
		
		inpLogFile = new InputBox(this, "inpLogFile");
		inpLogFile.setReadonly(true);
		inpLogFile.setWidth(250);
		
		inpLogThreshold = new InputBox(this, "inpLogThreshold");
		inpLogThreshold.setWidth(250);
		
		chkLogEnabled = new CheckBox(this, "chkLogEnabled");
		chkLogEnabled.setLabel("Enabled");
		
		inpMaxHistory = new InputBox(this, "inpMaxHistory");
		inpMaxHistory.setWidth(250);
		
		chkHistoryEnabled = new CheckBox(this, "chkHistoryEnabled");
		chkHistoryEnabled.setLabel("Enabled");

		btUpdate = new Button(this, "btUpdate");
		btUpdate.setTitle("Update");
		btUpdate.addSelectionListener(new SelectionListener() {
			@Override
			public void objectSelected(SelectionEvent event) {
				onUpdateTraceOptions();
			}
		});
		
	}

	/**
	 * 
	 */
	protected void onUpdateTraceOptions() {

		StringBuilder errors = new StringBuilder();
		ITraceDataManager dataManager = Trace.getDataManager();
		if (dataManager != null) {
			
			String lvl = ddTraceLevel.getSelectedKey();
			if (lvl != null) {
				TraceLevel traceLevel = TraceLevel.valueOf(lvl);
				if (traceLevel != null) {
					dataManager.setTraceLevel(traceLevel);
				}
			}
			
			try {
				String s = inpLogThreshold.getText();
				if (!s.isEmpty()) {
					long threshold = Long.parseLong(s.trim());
					dataManager.setLogThreshold(threshold);
				}
			} catch (NumberFormatException nfe) {
				errors.append("Invalid Number in Log Threshold<br>");
			}

			dataManager.setLogTraceAboveThreshold(chkLogEnabled.isChecked());
			
			try {
				String s = inpMaxHistory.getText();
				if (!s.isEmpty()) {
					int maxHistory = Integer.parseInt(s.trim());
					dataManager.setMaxHistory(maxHistory);
				}
			} catch (NumberFormatException nfe) {
				errors.append("Invalid Number in Trace History Max<br>");
			}

			dataManager.setKeepHistory(chkHistoryEnabled.isChecked());
			
			if (errors.length() == 0) {
				errorWarning.showWarning("ITraceDataManager options updated.");
			} else {
				errorWarning.showError("ITraceDataManager updated with errors:<br>" + errors);
			}
		} else {
			errorWarning.showError("No TraceDataManager available");
		}
		updateStatus();
		
		
	}

	/**
	 * 
	 */
	protected void onTurnOnOff() {
		
		Trace.setEnabled(!Trace.isEnabled());
		if (Trace.isEnabled()) {
			lbTraceStatus.setText("Enabled");
			btOnOff.setTitle("Turn Off");
		} else {
			lbTraceStatus.setText("Disabled");
			btOnOff.setTitle("Turn On");
		}
		
	}
	
}
