package com.fjmob.ponto.formatter;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

import com.github.mikephil.charting.utils.ValueFormatter;

public class MyValueFormatter implements ValueFormatter {


    public MyValueFormatter() {
    }

    @Override
    public String getFormattedValue(float value) {
    	
    	long horaSaldo = (long) value;
    	
    	float minutoSaldo = (value%1)*60;
    	
        return horaSaldo + ":" + (new Float(minutoSaldo).longValue() < 10 ? "0"+new Float(minutoSaldo).longValue() : new Float(minutoSaldo).longValue());
    }
}