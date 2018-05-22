package pkgApp.controller;

import java.net.URL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.poi.ss.formula.functions.FinanceLib;

import com.sun.prism.paint.Color;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.text.FontWeight;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

import javafx.beans.value.*;

import pkgApp.RetirementApp;
import pkgCore.Retirement;

public class RetirementController implements Initializable {

	private RetirementApp mainApp = null;
	@FXML
	private TextField txtSaveEachMonth;
	@FXML
	private TextField txtYearsToWork;
	@FXML
	private TextField txtAnnualReturnWorking;
	@FXML
	private TextField txtWhatYouNeedToSave;
	@FXML
	private TextField txtYearsRetired;
	@FXML
	private TextField txtAnnualReturnRetired;
	@FXML
	private TextField txtRequiredIncome;
	@FXML
	private TextField txtMonthlySSI;

	private HashMap<TextField, String> hmTextFieldRegEx = new HashMap<TextField, String>();

	public RetirementApp getMainApp() {
		return mainApp;
	}

	public void setMainApp(RetirementApp mainApp) {
		this.mainApp = mainApp;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	
		hmTextFieldRegEx.put(txtYearsToWork, "(?<!-)\\b([0-9]|[1-3][0-9]|40)\\b\\d*?"); 
		hmTextFieldRegEx.put(txtAnnualReturnWorking, "(?<!-)\\b([0-9])\\b\\d*(\\.\\d*)?|10"); 
		hmTextFieldRegEx.put(txtYearsRetired, "(?<!-)\\b([0-9]|1[0-9]|20)\\b\\d*?"); 
		hmTextFieldRegEx.put(txtAnnualReturnRetired, "(?<!-)\\b([0-9])\\b\\d*(\\.\\d*)?|10");
		hmTextFieldRegEx.put(txtRequiredIncome, "(?<!-)\\b([3-9][0-9][0-9][0-9]|10000|264[2-9]|26[5-9][0-9]|2[7-9][0-9][0-9])\\b\\d*?");
		hmTextFieldRegEx.put(txtMonthlySSI, "(?<!-)\\b([0-9]|[1-9][0-9]|[1-9][0-9][0-9]|1[0-9][0-9][0-9]|2[0-5][0-9][0-9]|26[0-3][0-9]|264[12])\\b\\d*?");

		Iterator it = hmTextFieldRegEx.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			TextField txtField = (TextField) pair.getKey();
			String strRegEx = (String) pair.getValue();

			txtField.focusedProperty().addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
						Boolean newPropertyValue) {
					// If newPropertyValue = true, then the field HAS FOCUS
					// If newPropertyValue = false, then field HAS LOST FOCUS
					if (!newPropertyValue) {
						if (!txtField.getText().matches(strRegEx)) {
							txtField.setText("");
							txtField.requestFocus();
						}
					}
				}
			});
		}

		//
		// TODO: Validate Working Annual Return %, accept only numbers and decimals
		// TODO: Validate Years retired, accepted only decimals
		// TODO: Validate Retired Annual Return %, accept only numbers and deciamls
		// TODO: Validate Required Income, accept only decimals
		// TODO: Validate Monthly SSI, accept only decimals
	}

	@FXML
	public void btnClear(ActionEvent event) {
		System.out.println("Clear pressed");

		// disable read-only controls
		txtSaveEachMonth.clear();
		txtSaveEachMonth.setDisable(true);
		txtWhatYouNeedToSave.clear();
		txtWhatYouNeedToSave.setDisable(true);

		// Clear
		txtYearsToWork.clear();
		txtYearsToWork.setDisable(false);
		
		txtAnnualReturnWorking.clear();
		txtAnnualReturnWorking.setDisable(false);
		
		txtYearsRetired.clear();
		txtYearsRetired.setDisable(false);
		
		txtAnnualReturnRetired.clear();
		txtAnnualReturnRetired.setDisable(false);
		
		txtRequiredIncome.clear();
		txtRequiredIncome.setDisable(false);
		
		txtMonthlySSI.clear();
		txtMonthlySSI.setDisable(false);

		
	}

	@FXML
	public void btnCalculate() {

		System.out.println("calculating");
		
		Retirement r = new Retirement(Integer.parseInt(txtYearsToWork.getText()), Double.parseDouble(txtAnnualReturnWorking.getText()), Integer.parseInt(txtYearsRetired.getText()), Double.parseDouble(txtAnnualReturnRetired.getText()), Double.parseDouble(txtRequiredIncome.getText()), Double.parseDouble(txtMonthlySSI.getText()));
		
		double totalSave = -Retirement.PV(((r.getdAnnualReturnRetired()/100)/12), (r.getiYearsRetired()*12), (r.getdRequiredIncome()-r.getdMonthlySSI()), 0, false);
		double totalSaveRound = Math.round(totalSave*100.0)/100.0;
		String WhatYouNeedToSave = String.valueOf(totalSaveRound);
		
		
		double monthlySave = -Retirement.PMT(((r.getdAnnualReturnWorking()/100)/12), (r.getiYearsToWork()*12), 0, totalSaveRound, false);
		double monthlySaveRound = Math.round(monthlySave*100.0)/100.0;
		String SaveEachMonth = String.valueOf(monthlySaveRound);		
				
		
		txtSaveEachMonth.setDisable(false);
		txtWhatYouNeedToSave.setDisable(false);
		txtWhatYouNeedToSave.setText(WhatYouNeedToSave);
		txtSaveEachMonth.setText(SaveEachMonth);
	}
}
