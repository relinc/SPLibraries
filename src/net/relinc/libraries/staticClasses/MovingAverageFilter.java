package net.relinc.libraries.staticClasses;

public final class MovingAverageFilter {
    private static double findMovingAverage(double [] data, double f_t,int idx){
    	int range=(int)(.5/f_t);
    	double value=0;
    	for(int idxFilter=-range; idxFilter<range;idxFilter++) {
    		if(idx+idxFilter<=0)
    		{
    			value+=data[0];
    		}
    		else if(idx+idxFilter>=data.length-1)
    		{
    			value+=data[data.length-1];
    		}
    		else
    		{
    			value+=data[idx+idxFilter];
    		}
    	}
    	return value/(2*range);
    }
    public static double [] movingAverageLowPass(double [] data, double f_t) {
    	
    	double [] filtered_data = new double[data.length];
    	
    	for(int idx=0; idx<data.length; idx++){
    		filtered_data[idx]=findMovingAverage(data,  f_t, idx);
    	}
    	
    	return filtered_data;
    }
}
