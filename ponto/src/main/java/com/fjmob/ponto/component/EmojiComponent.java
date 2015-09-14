package com.fjmob.ponto.component;

import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;


import com.fjmob.ponto.MainActivity;
import com.fjmob.ponto.entity.Mood;
import com.fjmob.ponto.persistence.MoodDAO;

public class EmojiComponent extends ImageView {

	 public EmojiComponent(Context context, AttributeSet attrs) {
	        this(context, attrs, 0);
	        
			this.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					saveMood(v);
				}

				
			});
	    }

	    public EmojiComponent(Context context, AttributeSet attrs, int defStyleAttr) {
	        this(context, attrs, defStyleAttr, 0);
	        
			this.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					saveMood(v);
				}
			});
	    }

	    public EmojiComponent(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
	        super(context, attrs, defStyleAttr, defStyleRes);
	        
			this.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					saveMood(v);
				}
			});
	    }
	
	public EmojiComponent(Context context) {
		super(context);
		
		this.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				saveMood(v);
			}
		});
	}

	
	private void hapticFeedback() {
		Vibrator vibrator;
		vibrator = (Vibrator)getContext().getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(15);
	}
	
	private void saveMood(View v) {
		hapticFeedback();
		Mood mood = new Mood();
		mood.setDataGravacao(new Date());
		mood.setMood(v.getContentDescription().toString());
		MoodDAO.getInstance(getContext()).salvar(mood);
		
		Intent intent = new Intent(getContext(), MainActivity.class);
		getContext().startActivity(intent);
	}
}
