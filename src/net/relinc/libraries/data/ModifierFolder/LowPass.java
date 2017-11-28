package net.relinc.libraries.data.ModifierFolder;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import net.relinc.libraries.data.DataSubset;
import net.relinc.libraries.data.DataSubset.baseDataType;
import net.relinc.libraries.staticClasses.SPMath;
import net.relinc.libraries.staticClasses.SPOperations;
import net.relinc.libraries.staticClasses.SPSettings;
import net.relinc.libraries.fxControls.NumberTextField;

public class LowPass extends Modifier {

	private String lowPassDescription = "Lowpass Filter";
	private double lowPassValue;
	public NumberTextField valueTF;
	HBox holdGrid = new HBox();
	private double diameter;
	public double waveSpeed;
	public LowPass() {
		init();
	}
	
	public LowPass(double val)
	{
		init();
		setLowPassValue(val);

	}
	
	private void init()
	{
		modifierEnum = ModifierEnum.LOWPASS;
		valueTF = new NumberTextField("KHz", "KHz");
		valueTF.setText("1000");
		valueTF.updateLabelPosition();
		valueTF.textProperty().addListener((observable, oldValue, newValue) -> {
			valueTF.updateLabelPosition();
		});
		GridPane grid = new GridPane();

		grid.add(valueTF, 0, 0);
		grid.add(valueTF.unitLabel, 0, 0);
		VBox arrowsVBox = new VBox();
		Button upButton = new Button("");
		upButton.setGraphic(SPOperations.getIcon("/net/relinc/libraries/images/UpArrow.png", 10));
		upButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				double currentVal = Double.parseDouble(valueTF.getText());
				currentVal += SPMath.getPicoArrowIncrease(currentVal, true);
				valueTF.setText(new DecimalFormat(".#####").format(currentVal));
			}
		});
		Button downButton = new Button("");
		downButton.setGraphic(SPOperations.getIcon("/net/relinc/libraries/images/DownArrow.png", 10));
		downButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				double currentVal = Double.parseDouble(valueTF.getText());
				currentVal -= SPMath.getPicoArrowIncrease(currentVal, false);
				valueTF.setText(new DecimalFormat(".#####").format((currentVal)));
			}
		});
		arrowsVBox.getChildren().add(upButton);
		arrowsVBox.getChildren().add(downButton);
		arrowsVBox.setAlignment(Pos.CENTER);
		grid.add(arrowsVBox, 1, 0);
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(3);
		
		holdGrid.getChildren().add(grid);
		holdGrid.setAlignment(Pos.CENTER);
		
		checkBox = new CheckBox("Enable Lowpass Filter");
		checkBox.selectedProperty().bindBidirectional(activated);
		checkBox.disableProperty().bind(enabled.not());
	}

	//controls for trim data HBox
	
	
	@Override
	public String toString() {
		return "Lowpass Filter";
	}
	@Override
	public double[] applyModifierToData(double[] fullData, DataSubset activatedData) {
		//MASSIVE HACK, size of texas
		int taps = (int) (1.2*2*diameter/waveSpeed/(activatedData.Data.timeData[1] - activatedData.Data.timeData[0]));
		return applyFilterToData(fullData,activatedData,taps);
	}

	private double[] applyFilterToData(double[] fullData, DataSubset activatedData, int taps) {
		if(activatedData.getBaseDataType() == baseDataType.LOAD && SPSettings.globalLoadDataLowpassFilter <=0){
			return SPMath.fourierLowPassFilter(fullData, SPSettings.globalLoadDataLowpassFilter, 1 / (activatedData.Data.timeData[1] - activatedData.Data.timeData[0]),taps );
		}
		else if(activatedData.getBaseDataType() == baseDataType.DISPLACEMENT && SPSettings.globalDisplacementDataLowpassFilter<=0){
			return SPMath.fourierLowPassFilter(fullData, SPSettings.globalDisplacementDataLowpassFilter, 1 / (activatedData.Data.timeData[1] - activatedData.Data.timeData[0]),taps);
		}
		else if(activated.get()){
			return SPMath.fourierLowPassFilter(fullData, lowPassValue, 1 / (activatedData.Data.timeData[1] - activatedData.Data.timeData[0]),taps);
		}
		else {
			return fullData;
		}
	}

	@Override
	public List<Node> getTrimDataHBoxControls() {
		ArrayList<Node> list = new ArrayList<>();
		list.add(holdGrid);
		return list;
	}

	@Override
	public String getStringForFileWriting() {
		//legacy: for a short time, -1 was saved when not enabled. New protocol is to save nothing.
		return enabled.get() ? lowPassDescription + ":" + lowPassValue + SPSettings.lineSeperator : "";
	}

	public double getLowPassValue() {
		return lowPassValue;
	}

	public void setLowPassValue(double lowPass) {
		this.lowPassValue = lowPass;
	}
	public void setBarDiameter(double diameter) {
		this.lowPassValue = diameter;
	}
	public void setWaveSpeed(double waveSpeed) {
		this.waveSpeed = waveSpeed;
	}

	@Override
	public void setValuesFromDescriptorValue(String descrip, String val) {
		if(descrip.equals(lowPassDescription)){
			//it was saved, so it is enabled
			if(Double.parseDouble(val) != -1) //legacy. For a short time -1 was saved. Now nothing is saved if not enabled.
				enabled.set(true);
			lowPassValue = Double.parseDouble(val);
			activated.set(true);
		}
	}

	@Override
	public void configureModifier(DataSubset sub) {
		lowPassValue = valueTF.getDouble() * 1000;
	}
	
	@Override
	public void readModifierFromString(String line) {
		setValuesFromLine(line);
	}


}
