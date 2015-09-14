package com.fjmob.ponto.handler;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Vibrator;
import android.provider.Settings.Secure;
import android.support.v7.widget.CardView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.fjmob.ponto.R;
import com.fjmob.ponto.SaldoWidget;
import com.fjmob.ponto.component.Cronometro;
import com.fjmob.ponto.entity.Configuracoes;
import com.fjmob.ponto.entity.Falta;
import com.fjmob.ponto.entity.Historico;
import com.fjmob.ponto.entity.Mood;
import com.fjmob.ponto.persistence.ConfiguracoesDAO;
import com.fjmob.ponto.persistence.FaltaDAO;
import com.fjmob.ponto.persistence.HistoricoDAO;
import com.fjmob.ponto.persistence.MoodDAO;
import com.fjmob.ponto.task.RegistrarSaldoTask;
import com.fjmob.ponto.task.VerificarAdicionarHistoricoTask;
import com.fjmob.ponto.task.VerificarRemoverHistoricoTask;
import com.fjmob.ponto.util.Constantes;
import com.github.pavlospt.roundedletterview.RoundedLetterView;

public class HistoricoHandler {

	private Activity activity;
	private LinearLayout layoutHistoricos;
	private TextView saldoTotal;
	private TextView total;
	private Drawable drwbMood;
	private Drawable drwbClose;
	private Drawable drwbEnter;
	private Drawable drwbExit;
	private float scale;
	private Typeface robotoLight;
	private Typeface robotoMedium;
	private Typeface robotoRegular;
	private LayoutParams lytWrapWrap;
	private LayoutParams lytMatchMatch;
    private int scndryColor;
    private int primaryColor;
    private int primaryTextColor;
    private int accentColor;
    private int primaryDarkColor;
    private int dividerColor;

	@SuppressLint("SimpleDateFormat")
	private DateFormat sdfData = new SimpleDateFormat("yyyy/MM/dd");
	@SuppressLint("SimpleDateFormat")
	private DateFormat sdfHoraMin = new SimpleDateFormat("HH:mm");
	@SuppressLint("SimpleDateFormat")
	private DateFormat sdfHora = new SimpleDateFormat("HH:mm:ss");
	@SuppressLint("SimpleDateFormat")
	SimpleDateFormat sdfMesAno = new SimpleDateFormat("yyyy/MM");

	private Date dataCal;

	public HistoricoHandler(Activity activity, LinearLayout layoutHistoricos, TextView saldoTotal, Date dataCal, TextView total) {
		this.activity = activity;
		this.layoutHistoricos = layoutHistoricos;
		this.saldoTotal = saldoTotal;
		this.dataCal = dataCal;
		this.total = total;

		this.drwbClose = activity.getResources().getDrawable(R.drawable.ic_close);
		this.drwbMood = activity.getResources().getDrawable(R.drawable.ic_mood);
		this.drwbEnter = activity.getResources().getDrawable(R.drawable.enter);
		this.drwbExit = activity.getResources().getDrawable(R.drawable.exit);
		this.scale = activity.getResources().getDisplayMetrics().density;
		this.robotoLight = Typeface.createFromAsset(activity.getAssets(), "fonts/Roboto-Light.ttf");
		this.robotoMedium = Typeface.createFromAsset(activity.getAssets(), "fonts/Roboto-Medium.ttf");
		this.robotoRegular = Typeface.createFromAsset(activity.getAssets(), "fonts/Roboto-Regular.ttf");
		this.lytWrapWrap = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		this.lytMatchMatch = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        this.scndryColor = activity.getResources().getColor(R.color.secondary_text);
        this.primaryColor = activity.getResources().getColor(R.color.primary);
        this.primaryTextColor = activity.getResources().getColor(R.color.primary_text);
        this.accentColor = activity.getResources().getColor(R.color.accent);
        this.primaryDarkColor = activity.getResources().getColor(R.color.primary_dark);
        this.dividerColor = activity.getResources().getColor(R.color.divider);
	}

	public TreeMap<String, List<Historico>> montarMapaHistorico(List<Historico> historicos) {
		TreeMap<String, List<Historico>> mapaHistorico = new TreeMap<String, List<Historico>>(Collections.reverseOrder());

		Collections.sort(historicos, new Comparator<Historico>() {

			@Override
			public int compare(Historico his1, Historico his2) {
				return his1.getDataGravacao().compareTo(his2.getDataGravacao());
			}
		});

		for (Historico historico : historicos) {
			String dataKey = sdfData.format(historico.getDataGravacao());
			if(mapaHistorico.get(dataKey) == null) {
				mapaHistorico.put(dataKey, new ArrayList<Historico>());
			}
			mapaHistorico.get(dataKey).add(historico);
		}
		return mapaHistorico;
	}

	public TreeMap<String, List<Historico>> popularHistorico(Typeface robotoLight) {


		layoutHistoricos.removeAllViews();

		List<Historico> historicos = HistoricoDAO.getInstance(activity).recuperarTodos();

		TreeMap<String, List<Historico>> mapaHistorico = montarMapaHistorico(historicos);


		List<Falta> faltas= FaltaDAO.getInstance(activity).recuperarTodos();
		for(Falta falta : faltas) {
			mapaHistorico.put(sdfData.format(falta.getDataGravacao()), new ArrayList<Historico>());
		}

		String dataCalString = sdfMesAno.format(dataCal);

		for (Entry<String, List<Historico>> entry : mapaHistorico.entrySet()) {

			if(entry.getKey().startsWith(dataCalString)) {
				addDia(layoutHistoricos, entry, robotoLight);
			}
		}

		return mapaHistorico;
	}

	/**
	 * Adiciona dia
	 * @param linearLayout
	 * @param entry
	 * @param font
	 */
	private void addDia(LinearLayout linearLayout, 
			Entry<String, List<Historico>> entry, Typeface font) {


		int cardPadding = (int) (scale * 16);
		int cardPaddingLeft = (int) (scale * 5);

		CardView cardView = new CardView(activity);
		cardView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		cardView.setUseCompatPadding(true);


		LinearLayout linearVertCard = new LinearLayout(activity);
		linearVertCard.setLayoutParams(lytMatchMatch);
		linearVertCard.setOrientation(LinearLayout.VERTICAL);
		linearVertCard.setPadding(cardPaddingLeft, cardPaddingLeft, cardPaddingLeft, cardPadding);

		addLayoutTituloDia(linearVertCard, entry, font);



		LinearLayout linearCard = new LinearLayout(activity);
		linearCard.setLayoutParams(lytMatchMatch);
		linearCard.setOrientation(LinearLayout.HORIZONTAL);

//		RoundedLetterView rlv = new RoundedLetterView(activity);
//		rlv.setLayoutParams(lytWrapWrap);
//		rlv.getLayoutParams().width = (int) (scale*56);
//		rlv.getLayoutParams().height = (int) (scale*56);
//		rlv.setBackgroundColor(Color.BLUE);
//		rlv.setTitleText("A");
//
//
//		linearCard.addView(rlv);

		addHorariosDia(linearCard, entry, font);
		addFaltaDia(linearCard, entry, font);
		addSaldoDia(linearCard, entry, font);

		linearVertCard.addView(linearCard);

		addMood(scale, linearVertCard, entry.getKey());


		cardView.addView(linearVertCard);

		linearLayout.addView(cardView);
	}

	private void addMood(final float scale, LinearLayout linearVertCard, String dateString) {
		LinearLayout linearMood = new LinearLayout(activity);
		linearMood.setLayoutParams(lytMatchMatch);
		linearMood.setOrientation(LinearLayout.HORIZONTAL);
		linearMood.setGravity(Gravity.END|Gravity.CENTER_VERTICAL);

		int dpWidthInPx  = (int) (24 * scale);
		int dpHeightInPx = (int) (24 * scale);
		LayoutParams layoutParamsImgMood = new LayoutParams(dpWidthInPx, dpHeightInPx);
		layoutParamsImgMood.gravity = Gravity.CENTER_HORIZONTAL;
		
		
		
		List<Mood> moods = MoodDAO.getInstance(activity).recuperarTodosOrdenadosAsc();
		for (final Mood mood : moods) {
			if(sdfData.format(mood.getDataGravacao()).equals(dateString)) {
				LinearLayout linearMoodHour = new LinearLayout(activity);
				LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				layoutParams.leftMargin = (int) (10*scale);
				layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
				linearMoodHour.setLayoutParams(layoutParams);
				linearMoodHour.setOrientation(LinearLayout.VERTICAL);
				linearMoodHour.setGravity(Gravity.CENTER_HORIZONTAL);
				
				TextView txtHoraMood = new TextView(activity);
				txtHoraMood.setText(sdfHoraMin.format(mood.getDataGravacao()));
				txtHoraMood.setTextColor(scndryColor);
				LayoutParams layoutText = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				layoutText.gravity = Gravity.CENTER_HORIZONTAL;
				txtHoraMood.setLayoutParams(layoutText);
				txtHoraMood.setTypeface(robotoMedium);
				txtHoraMood.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
				txtHoraMood.setTextSize(TypedValue.COMPLEX_UNIT_SP,9);
				linearMoodHour.addView(txtHoraMood);
				
				ImageView imageMood = new ImageView(activity);
				imageMood.setLayoutParams(layoutParamsImgMood);
				imageMood.setImageResource(activity.getResources().getIdentifier(mood.getMood(), "drawable", activity.getPackageName()));
				imageMood.setOnLongClickListener(new OnLongClickListener() {

					@Override
					public boolean onLongClick(View v) {
						hapticFeedback();
						DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								switch (which){
								case DialogInterface.BUTTON_POSITIVE:
									MoodDAO.getInstance(activity).deletar(mood);

									LinearLayout linearLayout = (LinearLayout) activity.findViewById(R.id.listaHorarios);
									linearLayout.removeAllViews();


									TreeMap<String, List<Historico>> mapaHistorico = popularHistorico(robotoLight);

									TimerHandler timerHandler = new TimerHandler(Cronometro.getInstance(activity), activity);
									timerHandler.verificaIniciaTimer(mapaHistorico);
									calcularSaldoTotal(mapaHistorico);

									break;

								case DialogInterface.BUTTON_NEGATIVE:
									break;
								}
							}
						};

						AlertDialog.Builder builder = new AlertDialog.Builder(activity);
						builder.setMessage(activity.getResources().getString(R.string.apagar_mood_confirmacao))
						.setPositiveButton(activity.getResources().getString(R.string.sim), dialogClickListener)
						.setNegativeButton(activity.getResources().getString(R.string.nao), dialogClickListener).show();

						return true;
					}
				});
				
				
				
				linearMoodHour.addView(imageMood);
				
				linearMood.addView(linearMoodHour);
			}
			
		}
		
		
		
		LinearLayout linearRuler = new LinearLayout(activity);
		LayoutParams lytParams = new LayoutParams((int)(scale * 1), (int) (24*scale));
		lytParams.setMargins((int) (10 * scale), 0, 0, 0);
		linearRuler.setLayoutParams(lytParams);
		linearRuler.setOrientation(LinearLayout.VERTICAL);
		linearRuler.setBackgroundColor(dividerColor);
		linearMood.addView(linearRuler);
		
		
		// Icone adicionar Mood
		LayoutParams layoutParamsImg = new LayoutParams(dpWidthInPx, dpHeightInPx);
		layoutParamsImg.leftMargin = (int) (5*scale);
		layoutParamsImg.gravity = Gravity.END | Gravity.CENTER_VERTICAL;
		ImageView imageView = new ImageView(activity);
		imageView.setLayoutParams(layoutParamsImg);
		imageView.setImageDrawable(drwbMood);
		imageView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                hapticFeedback();

                AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                LayoutInflater inflater = activity.getLayoutInflater();


                builder.setView(inflater.inflate(R.layout.mood, null))
                        // Add action buttons
                        .setNegativeButton(R.string.sair, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                // 3. Get the AlertDialog from create()
                AlertDialog dialog = builder.create();


                dialog.show();

                TextView titleMood = (TextView) dialog.findViewById(R.id.title_mood);
                titleMood.setTypeface(robotoRegular);

            }
        });

		linearMood.addView(imageView);

		linearVertCard.addView(linearMood);
	}

	/**
	 * Adiciona layout do titulo do dia
	 * @param linearLayout
	 * @param entry
	 * @param font
	 */
	private void addLayoutTituloDia(LinearLayout linearLayout,
			Entry<String, List<Historico>> entry, Typeface font) {

		LinearLayout layoutDia = new LinearLayout(activity);
		layoutDia.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		layoutDia.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
		layoutDia.setOrientation(LinearLayout.VERTICAL);

		int padding  = (int) (15 * scale);
		layoutDia.setPadding(0, 0, 0, padding);


		addTituloDia(layoutDia, entry, font);

		//---------
		List<Historico> listaHistorico = entry.getValue();
		if(listaHistorico.size() % 2 != 0) {
			long tempoPresente = 0;
			long tempoFinal = 0;
			for(int i = 0 ; i < listaHistorico.size() ; i++) {
				long tempoInicial = listaHistorico.get(i).getDataGravacao().getTime();



				if(i < (listaHistorico.size()-1)) {
					tempoFinal = listaHistorico.get(i+1).getDataGravacao().getTime();
					tempoPresente = tempoPresente + (tempoFinal - tempoInicial);
				} else {
					JornadaHandler jornadaHandler = new JornadaHandler(activity);

					tempoFinal = tempoInicial + jornadaHandler.recuperarTempoJornada()-tempoPresente; 
				}
				i++;
			}

			Date dat = new Date(tempoFinal);
			Calendar cal = Calendar.getInstance();
			cal.setTime(dat);

			int seconds = cal.get(Calendar.SECOND);
			int minutes = cal.get(Calendar.MINUTE);
			int hours   = cal.get(Calendar.HOUR_OF_DAY);

			TextView txtFim = new TextView(activity);
			txtFim.setText(activity.getResources().getString(R.string.txt_est_saida) + " " + String.format("%02d",hours) + ":" + 
					String.format("%02d",minutes) + ":"	+ 
					String.format("%02d",seconds));
			txtFim.setTextColor(scndryColor);
			txtFim.setLayoutParams(lytMatchMatch);
			txtFim.setTypeface(robotoMedium);
			txtFim.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
			txtFim.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
			layoutDia.addView(txtFim);
		}
		//-------------


		linearLayout.addView(layoutDia);
	}

	/**
	 * Adiciona icone de calend��rio
	 * @param layoutDia
	 * @param entry 
	 */
	@SuppressWarnings("deprecation")
	private void addCalendarImg(LinearLayout layoutDia, final Entry<String, List<Historico>> entry) {
		
		LinearLayout layoutImg = new LinearLayout(activity);
		LayoutParams lytDummy = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
		lytDummy.weight = 0.1f;
		layoutImg.setLayoutParams(lytDummy);
		layoutImg.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
		layoutImg.setOrientation(LinearLayout.HORIZONTAL);
		


		int dpWidthInPx  = (int) (24 * scale);
		int dpHeightInPx = (int) (24 * scale);
		LayoutParams layoutParamsImg = new LayoutParams(dpWidthInPx, dpHeightInPx);
		layoutParamsImg.gravity = Gravity.END;

		ImageView acaoImg = new ImageView(activity);
		acaoImg.setLayoutParams(layoutParamsImg);
		acaoImg.setImageDrawable(drwbClose);


		acaoImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				hapticFeedback();
				DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						hapticFeedback();
						switch (which){
						case DialogInterface.BUTTON_POSITIVE:

							List<Mood> moods = MoodDAO.getInstance(activity).recuperarTodos();
							for(Mood mood : moods) {
								if(sdfData.format(mood.getDataGravacao()).equals(entry.getKey())) {
									MoodDAO.getInstance(activity).deletar(mood);
								}
							}
							List<Falta> faltas = FaltaDAO.getInstance(activity).recuperarTodos();
							for(Falta falta : faltas) {
								if(sdfData.format(falta.getDataGravacao()).equals(entry.getKey())) {
									FaltaDAO.getInstance(activity).deletar(falta);
								}
							}


							for(Historico historico : entry.getValue()) {
								HistoricoDAO.getInstance(activity).deletar(historico);
							}

							LinearLayout linearLayout = (LinearLayout) activity.findViewById(R.id.listaHorarios);
							linearLayout.removeAllViews();

							TreeMap<String, List<Historico>> mapaHistorico = popularHistorico(robotoLight);

							TimerHandler timerHandler = new TimerHandler(Cronometro.getInstance(activity), activity);
							timerHandler.verificaIniciaTimer(mapaHistorico);
							calcularSaldoTotal(mapaHistorico);

							break;

						case DialogInterface.BUTTON_NEGATIVE:
							hapticFeedback();
							break;
						}
					}
				};

				AlertDialog.Builder builder = new AlertDialog.Builder(activity);
				builder.setMessage(activity.getResources().getString(R.string.apagar_dia_confirmacao))
				.setPositiveButton(activity.getResources().getString(R.string.sim), dialogClickListener)
				.setNegativeButton(activity.getResources().getString(R.string.nao), dialogClickListener).show();

			}
		});

		layoutImg.addView(acaoImg);
		layoutDia.addView(layoutImg);

	}

	/**
	 * Adiciona t��tulo do dia
	 * @param linearLayout
	 * @param entry
	 * @param font
	 * yyyy/MM/dd
	 */
	private void addTituloDia(LinearLayout linearLayout,
			Entry<String, List<Historico>> entry, Typeface font) {
		
		LinearLayout layoutTitulo = new LinearLayout(activity);
		layoutTitulo.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		layoutTitulo.setGravity(Gravity.CENTER_VERTICAL);
		layoutTitulo.setOrientation(LinearLayout.HORIZONTAL);
		
		
		
		LinearLayout layoutDummy = new LinearLayout(activity);
		LayoutParams lytDummy = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
		lytDummy.weight = 0.1f;
		layoutDummy.setLayoutParams(lytDummy);
		layoutDummy.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
		layoutDummy.setOrientation(LinearLayout.HORIZONTAL);
		
		TextView txtDummy = new TextView(activity);


		txtDummy.setText(" ");
		txtDummy.setTextColor(primaryColor);
		
		LayoutParams lytDm = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		txtDummy.setLayoutParams(lytDm);
		txtDummy.setTypeface(font);
		txtDummy.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
		txtDummy.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
		layoutDummy.addView(txtDummy);
		
		
		layoutTitulo.addView(layoutDummy);
		
		TextView txtTitulo = new TextView(activity);

		String dataString = entry.getKey().substring(8,10) + "/" + entry.getKey().substring(5,7) + "/" + entry.getKey().substring(0,4);

		txtTitulo.setText(dataString);
		txtTitulo.setTextColor(scndryColor);
		
		LayoutParams lyt = new LayoutParams(0, LayoutParams.MATCH_PARENT);
		lyt.weight = 0.8f;
		txtTitulo.setLayoutParams(lyt);
		txtTitulo.setTypeface(robotoMedium);
		txtTitulo.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
		txtTitulo.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
		layoutTitulo.addView(txtTitulo);
		
		addCalendarImg(layoutTitulo, entry);
		
		linearLayout.addView(layoutTitulo);
	}

	/**
	 * Adiciona Saldo do dia
	 * @param linearLayout
	 * @param entry
	 * @param font
	 */
	private void addSaldoDia(LinearLayout linearLayout,
			Entry<String, List<Historico>> entry, Typeface font) {

		// Entra se n��o for igual �� data atual
		if(!sdfData.format(new Date()).equals(entry.getKey())) {

			List<Historico> listaHistorico = entry.getValue();


			if(listaHistorico.size() % 2 == 0) {

				long tempoPresente = 0;
				for(int i = 0 ; i < listaHistorico.size() ; i++) {
					long tempoInicial = listaHistorico.get(i).getDataGravacao().getTime();
					long tempoFinal = listaHistorico.get(i+1).getDataGravacao().getTime();
					tempoPresente = tempoPresente + (tempoFinal - tempoInicial);
					i++;
				}

				JornadaHandler jornadaHandler = new JornadaHandler(activity);

				Calendar cal = Calendar.getInstance();
				try {
					cal.setTime(sdfData.parse(entry.getKey()));
				} catch (ParseException e) {
					e.printStackTrace();
				}

				long tempoJornada = 0;

				if(cal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && 
						cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {

					tempoJornada += jornadaHandler.recuperarTempoJornada();
				}

				long tempoSaldo = tempoPresente - tempoJornada;

				int seconds = (int) TimeUnit.MILLISECONDS.toSeconds(tempoSaldo) % 60;
				int minutes = (int) TimeUnit.MILLISECONDS.toMinutes(tempoSaldo) % 60;
				int hours   = (int) TimeUnit.MILLISECONDS.toHours(tempoSaldo);



				TextView txtSaldo = new TextView(activity);
				txtSaldo.setText(entry.getKey());
				txtSaldo.setTextColor(primaryTextColor);
				txtSaldo.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				txtSaldo.setTypeface(robotoMedium);
				txtSaldo.setGravity(Gravity.END|Gravity.CENTER_VERTICAL);
				txtSaldo.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);

				if(tempoSaldo < 0) {
					txtSaldo.setTextColor(accentColor);
					txtSaldo.setText("-" + String.format("%03d",hours).replaceAll("-", "").replaceAll("00", "0") + ":" + 
							String.format("%03d",minutes).replaceAll("-", "").replaceAll("00", "0") + ":"	+
							String.format("%03d",seconds).replaceAll("-", "").replaceAll("00", "0"));

				} else {
					txtSaldo.setTextColor(accentColor);
					txtSaldo.setText("+" + String.format("%02d",hours) + ":" +
							String.format("%02d",minutes) + ":"	+ 
							String.format("%02d",seconds));


				}

				linearLayout.addView(txtSaldo);
			}



		} else {
			try {
				((ViewGroup)Cronometro.getInstance(activity).getParent()).removeView(Cronometro.getInstance(activity));
			} catch (Exception e) {

			}
			linearLayout.addView(Cronometro.getInstance(activity));




		}
	}


	private void addFaltaDia(LinearLayout linearLayout,
								Entry<String, List<Historico>> entry, Typeface font) {

		if(entry.getValue().size() == 0) {


			LinearLayout layoutHorarios = new LinearLayout(activity);
			layoutHorarios.setLayoutParams(lytWrapWrap);
			layoutHorarios.setGravity(Gravity.START | Gravity.TOP);
			layoutHorarios.setOrientation(LinearLayout.VERTICAL);

			List<Falta> faltas = FaltaDAO.getInstance(activity).recuperarTodos();
			Falta falta = null;

			for(Falta faltaVo : faltas) {
				if(sdfData.format(faltaVo.getDataGravacao()).equals(entry.getKey())) {
					falta = faltaVo;
					break;
				}
			}



			TextView txtFalta = new TextView(activity);
			txtFalta.setText(activity.getResources().getString(R.string.vc_faltou));
			txtFalta.setTextColor(scndryColor);
			txtFalta.setTypeface(font);
			txtFalta.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
			txtFalta.setLayoutParams(lytWrapWrap);
			int dpMarginInPx = (int) (10 * scale);
			txtFalta.setPadding(0, 0, dpMarginInPx, 0);
			final Falta finalFalta = falta;
			txtFalta.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							switch (which) {
								case DialogInterface.BUTTON_POSITIVE:
									FaltaDAO.getInstance(activity).deletar(finalFalta);


									String deviceId = Secure.getString(activity.getContentResolver(),
											Secure.ANDROID_ID);
									VerificarRemoverHistoricoTask histRemTask = new VerificarRemoverHistoricoTask(activity);
									histRemTask.execute(deviceId);


									LinearLayout linearLayout = (LinearLayout) activity.findViewById(R.id.listaHorarios);
									linearLayout.removeAllViews();

									TreeMap<String, List<Historico>> mapaHistorico = popularHistorico(robotoLight);

									TimerHandler timerHandler = new TimerHandler(Cronometro.getInstance(activity), activity);
									timerHandler.verificaIniciaTimer(mapaHistorico);
									calcularSaldoTotal(mapaHistorico);

									break;

								case DialogInterface.BUTTON_NEGATIVE:
									break;
							}
						}
					};

					AlertDialog.Builder builder = new AlertDialog.Builder(activity);
					builder.setMessage(activity.getResources().getString(R.string.apagar_falta_confirmacao))
							.setPositiveButton(activity.getResources().getString(R.string.sim), dialogClickListener)
							.setNegativeButton(activity.getResources().getString(R.string.nao), dialogClickListener).show();

					return true;
				}
			});

			layoutHorarios.addView(txtFalta);

			linearLayout.addView(layoutHorarios);

		}

	}


	@SuppressWarnings("deprecation")
	private void addHorariosDia(LinearLayout linearLayout,
			Entry<String, List<Historico>> entry, Typeface font) {


		LinearLayout layoutHorarios = new LinearLayout(activity);
		layoutHorarios.setLayoutParams(lytWrapWrap);
        layoutHorarios.setPadding((int) (15 * scale), 0, (int) (15 * scale), 0);
		layoutHorarios.setGravity(Gravity.START | Gravity.TOP);
		layoutHorarios.setOrientation(LinearLayout.VERTICAL);

		
		for(int i=0 ; i<entry.getValue().size() ; i++) {
			LinearLayout layoutHorariosPar = new LinearLayout(activity);
			layoutHorariosPar.setLayoutParams(lytWrapWrap);
			layoutHorariosPar.setGravity(Gravity.START|Gravity.TOP);
			layoutHorariosPar.setOrientation(LinearLayout.HORIZONTAL);
			
			final Historico historico = entry.getValue().get(i);
			addHorario(entry, font, scale, i, layoutHorariosPar, historico);
			
			if(i != entry.getValue().size() - 1) {
				final Historico historicoPar = entry.getValue().get(i+1);
				addHorario(entry, font, scale, i, layoutHorariosPar, historicoPar);
				i++;
			}
			
			layoutHorarios.addView(layoutHorariosPar);
		}
		linearLayout.addView(layoutHorarios);
	}



    private void addHorario(Entry<String, List<Historico>> entry,
			Typeface font, final float scale, int i,
			LinearLayout layoutHorariosPar, final Historico historico) {
		
		LinearLayout layoutHorario = new LinearLayout(activity);
		layoutHorario.setGravity(Gravity.START);
		LayoutParams lytParamsHorario = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		int marginTopInPx = (int) (15 * scale);

		if(i != (entry.getValue().size() - 1)) {
			layoutHorario.setPadding(0, 0, 0, marginTopInPx);
		} else {
			layoutHorario.setPadding(0, 0, 0, 0);
		}

		layoutHorario.setLayoutParams(lytParamsHorario);
		layoutHorario.setOrientation(LinearLayout.VERTICAL);
		
		LinearLayout layoutHorarioAcao = new LinearLayout(activity);
		layoutHorarioAcao.setLayoutParams(lytWrapWrap);
		layoutHorarioAcao.setGravity(Gravity.START);
		layoutHorarioAcao.setOrientation(LinearLayout.HORIZONTAL);

		addAcaoImg(layoutHorarioAcao, entry.getValue().indexOf(historico), historico);


		TextView txtHistorico = new TextView(activity);
		txtHistorico.setText(sdfHora.format(historico.getDataGravacao()));
		txtHistorico.setTextColor(scndryColor);
		txtHistorico.setTypeface(font);
		txtHistorico.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
		txtHistorico.setLayoutParams(lytWrapWrap);
		int dpMarginInPx = (int) (10 * scale);
		txtHistorico.setPadding(0, 0, dpMarginInPx, 0);
		txtHistorico.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which){
						case DialogInterface.BUTTON_POSITIVE:
							HistoricoDAO.getInstance(activity).deletar(historico);

							
							String deviceId = Secure.getString(activity.getContentResolver(),
									Secure.ANDROID_ID);
							VerificarRemoverHistoricoTask histRemTask = new VerificarRemoverHistoricoTask(activity);
							histRemTask.execute(deviceId);
							
							
							LinearLayout linearLayout = (LinearLayout) activity.findViewById(R.id.listaHorarios);
							linearLayout.removeAllViews();


							TreeMap<String, List<Historico>> mapaHistorico = popularHistorico(robotoLight);

							TimerHandler timerHandler = new TimerHandler(Cronometro.getInstance(activity), activity);
							timerHandler.verificaIniciaTimer(mapaHistorico);
							calcularSaldoTotal(mapaHistorico);

							break;

						case DialogInterface.BUTTON_NEGATIVE:
							break;
						}
					}
				};

				AlertDialog.Builder builder = new AlertDialog.Builder(activity);
				builder.setMessage(activity.getResources().getString(R.string.apagar_hora_confirmacao))
				.setPositiveButton(activity.getResources().getString(R.string.sim), dialogClickListener)
				.setNegativeButton(activity.getResources().getString(R.string.nao), dialogClickListener).show();

				return true;
			}
		});

		layoutHorarioAcao.addView(txtHistorico);

		layoutHorario.addView(layoutHorarioAcao);
		
		if(entry.getValue().indexOf(historico) % 2 == 0 && entry.getValue().indexOf(historico) > 0) {
			long tempoEntrada = historico.getDataGravacao().getTime();
			final long tempoSaidaAnterior = entry.getValue().get(entry.getValue().indexOf(historico) - 1).getDataGravacao().getTime();

			long tempoIntervalo = tempoEntrada - tempoSaidaAnterior;

			if(tempoIntervalo < Constantes.HORA_EM_MILISSEGUNDOS) {

				// Destaca horário a ser ajustado
				txtHistorico.setTextColor(primaryDarkColor);
				
				Button btnAjustarIntervalo = new Button(activity);
				btnAjustarIntervalo.setBackground(activity.getResources().getDrawable(R.drawable.ajuste_button));
				btnAjustarIntervalo.setText(activity.getResources().getString(R.string.ajustar_hora));

				int dpHeightInPx = (int) (25 * scale);
				int dpWidthInPx = (int) (75 * scale);


				LayoutParams lytParams = new LayoutParams(dpWidthInPx, dpHeightInPx);
				lytParams.topMargin = (int) (3*scale);
				lytParams.gravity = Gravity.CENTER_HORIZONTAL;
				btnAjustarIntervalo.setLayoutParams(lytParams);
				btnAjustarIntervalo.setTextColor(primaryColor);
				btnAjustarIntervalo.setTextSize(TypedValue.COMPLEX_UNIT_SP,9);
				btnAjustarIntervalo.setMinHeight(5);
				btnAjustarIntervalo.setMinWidth(5);
				btnAjustarIntervalo.setWidth(dpWidthInPx);

				btnAjustarIntervalo.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {



						DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								switch (which){
								case DialogInterface.BUTTON_POSITIVE:
									historico.setDataGravacao(new Date(tempoSaidaAnterior + Constantes.HORA_EM_MILISSEGUNDOS));

									HistoricoDAO.getInstance(activity).editar(historico);


									TreeMap<String, List<Historico>> mapaHistorico = popularHistorico(robotoLight);

									TimerHandler timerHandler = new TimerHandler(Cronometro.getInstance(activity), activity);
									timerHandler.verificaIniciaTimer(mapaHistorico);
									calcularSaldoTotal(mapaHistorico);
									break;

								case DialogInterface.BUTTON_NEGATIVE:
									break;
								}
							}
						};

						AlertDialog.Builder builder = new AlertDialog.Builder(activity);
						builder.setMessage(activity.getResources().getString(R.string.ajustar_hora_confirmacao))
						.setPositiveButton(activity.getResources().getString(R.string.sim), dialogClickListener)
						.setNegativeButton(activity.getResources().getString(R.string.nao), dialogClickListener).show();


					}
				});


				layoutHorario.addView(btnAjustarIntervalo);
			}


		}


		layoutHorariosPar.addView(layoutHorario);
	}

	@SuppressWarnings("deprecation")
	private void addAcaoImg(LinearLayout layoutHorario, int index, final Historico historico) {

		int dpWidthInPx  = (int) (20 * scale);
		int dpHeightInPx = (int) (20 * scale);
		LayoutParams layoutParamsImg = new LayoutParams(dpWidthInPx, dpHeightInPx);
		layoutParamsImg.gravity=Gravity.CENTER_VERTICAL;
		layoutParamsImg.rightMargin= (int) (3*scale);
		ImageView acaoImg = new ImageView(activity);
		acaoImg.setLayoutParams(layoutParamsImg);

		if(index % 2 == 0) {
			acaoImg.setImageDrawable(drwbEnter);
		} else {
			acaoImg.setImageDrawable(drwbExit);
		}



		acaoImg.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which){
						case DialogInterface.BUTTON_POSITIVE:
							HistoricoDAO.getInstance(activity).deletar(historico);


							LinearLayout linearLayout = (LinearLayout) activity.findViewById(R.id.listaHorarios);
							linearLayout.removeAllViews();


							TreeMap<String, List<Historico>> mapaHistorico = popularHistorico(robotoLight);


							TimerHandler timerHandler = new TimerHandler(Cronometro.getInstance(activity), activity);
							timerHandler.verificaIniciaTimer(mapaHistorico);
							calcularSaldoTotal(mapaHistorico);

							break;

						case DialogInterface.BUTTON_NEGATIVE:
							break;
						}
					}
				};

				AlertDialog.Builder builder = new AlertDialog.Builder(activity);
				builder.setMessage(activity.getResources().getString(R.string.apagar_hora_confirmacao))
				.setPositiveButton(activity.getResources().getString(R.string.sim), dialogClickListener)
				.setNegativeButton(activity.getResources().getString(R.string.nao), dialogClickListener).show();

				return true;
			}


		});

		layoutHorario.addView(acaoImg);
	}

	public void calcularSaldoTotal(TreeMap<String, List<Historico>> mapaHistorico) {
		// Monta Saldo Total
		long tempoJornadaTotal = 0;
		long tempoPresenteTotal = 0;



		for(Entry<String, List<Historico>> entry : mapaHistorico.entrySet())  {

			String dataCalString = sdfMesAno.format(dataCal);


				if(!entry.getKey().equals(sdfData.format(new Date()))) {

					List<Historico> listaHistorico = entry.getValue();
					if(listaHistorico.size() % 2 == 0) {
						for(int i = 0 ; i < listaHistorico.size() ; i++) {
							long tempoInicial = listaHistorico.get(i).getDataGravacao().getTime();
							long tempoFinal = listaHistorico.get(i+1).getDataGravacao().getTime();
							tempoPresenteTotal += tempoFinal - tempoInicial;
							i++;
						}

						JornadaHandler jornadaHandler = new JornadaHandler(activity);

						Calendar cal = Calendar.getInstance();
						try {
							cal.setTime(sdfData.parse(entry.getKey()));
						} catch (ParseException e) {
							e.printStackTrace();
						}
						if(cal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY &&
								cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {

							tempoJornadaTotal += jornadaHandler.recuperarTempoJornada();
						}

					}
				}


		}

		Configuracoes configuracoes = ConfiguracoesDAO.getInstance(activity).recuperarPorId(1);

		long tempoSaldoTotal = tempoPresenteTotal - tempoJornadaTotal;
		if(configuracoes != null) {
			if(configuracoes.getSaldoAcumulado() != null) {
				tempoSaldoTotal = tempoSaldoTotal + (configuracoes.getSaldoAcumulado()*3600000);
			}
			if(configuracoes.getSaldoAcumuladoMinutos() != null) {
				tempoSaldoTotal = tempoSaldoTotal + (configuracoes.getSaldoAcumuladoMinutos()*60000);
			}
		}
		int secondsTotal = (int) TimeUnit.MILLISECONDS.toSeconds(tempoSaldoTotal) % 60;
		int minutesTotal = (int) TimeUnit.MILLISECONDS.toMinutes(tempoSaldoTotal) % 60;
		int hoursTotal   = (int) TimeUnit.MILLISECONDS.toHours(tempoSaldoTotal);




		if(tempoSaldoTotal < 0) {
			String horasString = String.format("%03d",hoursTotal);
			String saldo = (horasString.equals("000") ? "-00" : horasString) + ":" +
					String.format("%02d",minutesTotal).replaceAll("-", "") + ":"	+
					String.format("%02d",secondsTotal).replaceAll("-", "");
			total.setText(saldo);
		} else {
			String saldo = "+"+String.format("%02d",hoursTotal) + ":" +
					String.format("%02d",minutesTotal).replaceAll("-", "") + ":"	+
					String.format("%02d",secondsTotal).replaceAll("-", "");
			total.setText(saldo);
		}


		// Monta Saldo Mensal
		long tempoJornada = 0;
		long tempoPresente = 0;



		for(Entry<String, List<Historico>> entry : mapaHistorico.entrySet())  {

			String dataCalString = sdfMesAno.format(dataCal);
			if(entry.getKey().startsWith(dataCalString)) {

				if(!entry.getKey().equals(sdfData.format(new Date()))) {

					List<Historico> listaHistorico = entry.getValue();
					if(listaHistorico.size() % 2 == 0) {
						for(int i = 0 ; i < listaHistorico.size() ; i++) {
							long tempoInicial = listaHistorico.get(i).getDataGravacao().getTime();
							long tempoFinal = listaHistorico.get(i+1).getDataGravacao().getTime();
							tempoPresente += tempoFinal - tempoInicial;
							i++;
						}

						JornadaHandler jornadaHandler = new JornadaHandler(activity);

						Calendar cal = Calendar.getInstance();
						try {
							cal.setTime(sdfData.parse(entry.getKey()));
						} catch (ParseException e) {
							e.printStackTrace();
						}
						if(cal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && 
								cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {

							tempoJornada += jornadaHandler.recuperarTempoJornada();
						}

					}
				}
			}

		}

		long tempoSaldo = tempoPresente - tempoJornada;
		int seconds = (int) TimeUnit.MILLISECONDS.toSeconds(tempoSaldo) % 60;
		int minutes = (int) TimeUnit.MILLISECONDS.toMinutes(tempoSaldo) % 60;
		int hours   = (int) TimeUnit.MILLISECONDS.toHours(tempoSaldo);


        String deviceId = Secure.getString(activity.getContentResolver(),
                Secure.ANDROID_ID);

        RegistrarSaldoTask regSaldoTask = new RegistrarSaldoTask(activity, tempoSaldo);
        regSaldoTask.execute(deviceId);

		if(tempoSaldo < 0) {
			String horasString = String.format("%03d",hours);
			String saldo = (horasString.equals("000") ? "-00" : horasString) + ":" + 
					String.format("%02d",minutes).replaceAll("-", "") + ":"	+ 
					String.format("%02d",seconds).replaceAll("-", "");
			saldoTotal.setText(saldo);
			RemoteViews rv = new RemoteViews(activity.getPackageName(), R.layout.saldo_widget);
			rv.setTextViewText(R.id.saldoClockWidget, saldo);
			ComponentName cn = new ComponentName(activity, SaldoWidget.class);
			AppWidgetManager.getInstance(activity).updateAppWidget(cn, rv);

		} else {
			String saldo = "+"+String.format("%02d",hours) + ":" + 
					String.format("%02d",minutes).replaceAll("-", "") + ":"	+ 
					String.format("%02d",seconds).replaceAll("-", "");
			saldoTotal.setText(saldo);
			RemoteViews rv = new RemoteViews(activity.getPackageName(), R.layout.saldo_widget);
			rv.setTextViewText(R.id.saldoClockWidget, saldo);
			ComponentName cn = new ComponentName(activity, SaldoWidget.class);
			AppWidgetManager.getInstance(activity).updateAppWidget(cn, rv);
		}


	}
	
	private void hapticFeedback() {
		Vibrator vibrator;
		vibrator = (Vibrator)activity.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(15);
	}
}
