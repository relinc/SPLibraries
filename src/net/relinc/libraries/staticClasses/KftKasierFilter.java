package net.relinc.libraries.staticClasses;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public final class KftKasierFilter {
    static {
    	System.setProperty("java.library.path", "C:\\Program Files\\SUREPulse\\app\\libs");
        System.loadLibrary("C:\\Program Files\\SUREPulse\\app\\libs\\kftKasierFilter");    // loads kftKasierFilter.dll
    }

    private native double [] lowPassFilter(double [] data, double f_t);
    
    public static double[] lowPassKftKasier(double[] data, double lowPass, double frequency)
    {
    	System.out.println(lowPass/frequency);
    	if(frequency<=0) {
    		return data;
    	}
    	return new KftKasierFilter().lowPassFilter(data, lowPass/frequency);
    }
}
