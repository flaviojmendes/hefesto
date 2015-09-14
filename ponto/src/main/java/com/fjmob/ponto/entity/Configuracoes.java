package com.fjmob.ponto.entity;

public class Configuracoes {
	
	private Integer id;
	private Integer jornadaHoras;
	private Integer jornadaMinutos;
	private Integer saldoAcumulado;
	private Integer saldoAcumuladoMinutos;
	private Boolean sabadoUtil = false;

	
	public Configuracoes(Integer id, Integer jornadaHoras, Integer jornadaMinutos, Boolean sabadoUtil, Integer saldoAcumulado, Integer saldoAcumuladoMinutos) {
		super();
		this.id = id;
		this.jornadaHoras = jornadaHoras;
		this.jornadaMinutos = jornadaMinutos;
		this.sabadoUtil = sabadoUtil;
		this.saldoAcumulado = saldoAcumulado;
		this.saldoAcumuladoMinutos = saldoAcumuladoMinutos;
	}
	
	public Configuracoes() {
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getJornadaHoras() {
		return jornadaHoras;
	}

	public void setJornadaHoras(Integer jornadaHoras) {
		this.jornadaHoras = jornadaHoras;
	}

	public Integer getJornadaMinutos() {
		return jornadaMinutos;
	}

	public void setJornadaMinutos(Integer jornadaMinutos) {
		this.jornadaMinutos = jornadaMinutos;
	}

	public Boolean getSabadoUtil() {
		return sabadoUtil;
	}

	public void setSabadoUtil(Boolean sabadoUtil) {
		this.sabadoUtil = sabadoUtil;
	}


	public Integer getSaldoAcumulado() {
		return saldoAcumulado;
	}

	public void setSaldoAcumulado(Integer saldoAcumulado) {
		this.saldoAcumulado = saldoAcumulado;
	}

	public Integer getSaldoAcumuladoMinutos() {
		return saldoAcumuladoMinutos;
	}

	public void setSaldoAcumuladoMinutos(Integer saldoAcumuladoMinutos) {
		this.saldoAcumuladoMinutos = saldoAcumuladoMinutos;
	}
}