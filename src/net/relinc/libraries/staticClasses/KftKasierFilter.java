package net.relinc.libraries.staticClasses;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public final class KftKasierFilter {
    static {
    	//System.setProperty("java.library.path", "C:\\Program Files\\SUREPulse\\app\\libs");
      //  System.load("C:\\Program Files\\SUREPulse\\app\\libs\\kftKasierFilter.dll");    // loads kftKasierFilter.dll
    	System.loadLibrary("kftKasierFilter");
    }

    private native double [] lowPassFilter(double [] data, double f_t, int taps);

    public static double[] lowPassKftKasier(double[] data, double lowPass, double frequency, int taps)
    {
    	if(frequency<=0) {
    		return data;
    	}
    	if(lowPass ==0) {
    		return data;
    	}
    	System.out.println(frequency/lowPass);

    	
    	return new KftKasierFilter().lowPassFilter(data, frequency/lowPass, taps);
   
    }
}
