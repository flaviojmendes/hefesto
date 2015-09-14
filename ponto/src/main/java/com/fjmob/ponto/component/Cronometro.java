package com.fjmob.ponto.component;

import com.fjmob.ponto.R;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar.LayoutParams;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.Chronometer;

public class Cronometro extends Chronometer{

	private static Cronometro instance;
	
	public Cronometro(Context context) {
		super(context);
	}
	
	public static Cronometro getInstance(Context context){
        if(instance == null) {
             instance = new Cronometro(context);
             instance.setTextColor(context.getResources().getColor(R.color.primary_text));
             instance.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
             instance.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Medium.ttf"));
             instance.setGravity(Gravity.END|Gravity.CENTER_VERTICAL);
             instance.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
 			instance.setTextColor(context.getResources().getColor(R.color.accent));
 			
        }
        return instance;
	}
	

}
