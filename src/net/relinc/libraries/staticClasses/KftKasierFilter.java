package net.relinc.libraries.staticClasses;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public final class KftKasierFilter {
    static {
    	//System.setProperty("java.library.path", "C:\\Program Files\\SUREPulse\\app\\libs");
        //System.load("C:\\Program Files\\SUREPulse\\app\\libs\\kftKasierFilter.dll");    // loads kftKasierFilter.dll
    	System.loadLibrary("kftKasierFilter");
    }

    private native double [] lowPassFilter(double [] data, double f_t, int taps);
    
    public static double[] lowPassKftKasier(double[] data, double lowPass, double frequency, int taps)
    {
    	System.out.println(lowPass/frequency);
    	if(frequency<=0) {
    		return data;
    	}
    	if(lowPass ==0) {
    		return data;
    	}
    	System.out.println("my new taps");
    	System.out.println(taps);
    	return new KftKasierFilter().lowPassFilter(data, lowPass/frequency, taps);
    }
}
