package net.relinc.libraries.data;

public class EngineeringStrain extends DataSubset {

	public EngineeringStrain(double[] t, double[] d){
		super(t, d);
	}

	@Override
	public double[] getUsefulTrimmedData() {
		return getTrimmedData();
	}
	
}