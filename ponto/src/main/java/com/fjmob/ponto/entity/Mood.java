package com.fjmob.ponto.entity;

import java.util.Date;

public class Mood {
	
	private Integer id;
	private Date dataGravacao;
	private String mood;
	private String comentario;

	public Mood(Integer id, String mood, Date dataGravacao, String comentario) {
		super();
		this.id = id;
		this.mood = mood;
		this.dataGravacao = dataGravacao;
		this.comentario = comentario;
	}

	public Mood() {
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}


	public Date getDataGravacao() {
		return dataGravacao;
	}

	public void setDataGravacao(Date dataGravacao) {
		this.dataGravacao = dataGravacao;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public String getMood() {
		return mood;
	}

	public void setMood(String mood) {
		this.mood = mood;
	}
	
	
}