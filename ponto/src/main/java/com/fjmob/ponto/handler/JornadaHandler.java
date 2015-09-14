package com.fjmob.ponto.handler;

import android.app.Activity;

import com.fjmob.ponto.entity.Configuracoes;
import com.fjmob.ponto.persistence.ConfiguracoesDAO;

public class JornadaHandler {
	private long tempoJornada = 28800000;
	private Activity activity;
	
	public JornadaHandler(Activity activity) {
		this.activity = activity;
	}
	
	public long recuperarTempoJornada() {
		Configuracoes configuracoes = ConfiguracoesDAO.getInstance(activity).recuperarPorId(1);
		if(configuracoes != null) {
			tempoJornada = configuracoes.getJornadaHoras() * 3600000;
			tempoJornada += configuracoes.getJornadaMinutos() * 60000;
		}
		
		return tempoJornada;
	}
	
	
}
