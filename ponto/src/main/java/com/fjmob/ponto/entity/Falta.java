package com.fjmob.ponto.entity;

import java.util.Date;

public class Falta {

	private Integer id;
	private Date dataGravacao;
	private String comentario;

	public Falta(Integer id, Date dataGravacao, String comentario) {
		super();
		this.id = id;
		this.dataGravacao = dataGravacao;
		this.comentario = comentario;
	}

	public Falta() {
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
	
	
}