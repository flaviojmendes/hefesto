package com.fjmob.ponto.persistence;

public class Nota {
	private int id;
	private String nota;
	
	public Nota() {
	}
	
	public Nota(int id, String nota) {
		super();
		this.id = id;
		this.nota = nota;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNota() {
		return nota;
	}
	public void setNota(String nota) {
		this.nota = nota;
	}
}