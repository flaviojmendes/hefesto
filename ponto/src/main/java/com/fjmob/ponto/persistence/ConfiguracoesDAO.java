package com.fjmob.ponto.persistence;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.fjmob.ponto.entity.Configuracoes;

public class ConfiguracoesDAO {
	 
	 
    public static final String NOME_TABELA = "Configuracoes";
    public static final String COLUNA_ID = "id";
    public static final String COLUNA_JORNADA_HORAS = "jornadaHoras";
    public static final String COLUNA_JORNADA_MINUTOS = "jornadaMinutos";
    public static final String COLUNA_SABADO_UTIL = "sabadoUtil";
    public static final String COLUNA_SALDO_ACUMULADO = "saldoAcumulado";
    public static final String COLUNA_SALDO_ACUMULADO_MINUTOS = "saldoAcumuladoMinutos";
 
 
    public static final String SCRIPT_CRIACAO_TABELA = "CREATE TABLE " + NOME_TABELA + "("
            + COLUNA_ID + " INTEGER PRIMARY KEY," + COLUNA_JORNADA_HORAS + " INTEGER,"
            	+ COLUNA_JORNADA_MINUTOS + " INTEGER, "+ COLUNA_SABADO_UTIL + " INTEGER, " + COLUNA_SALDO_ACUMULADO + " INTEGER, " +
            COLUNA_SALDO_ACUMULADO_MINUTOS + " INTEGER)";

    public static final String SCRIPT_DELECAO_TABELA =  "DROP TABLE IF EXISTS " + NOME_TABELA;
 
 
    private SQLiteDatabase dataBase = null;
 
 
    private static ConfiguracoesDAO instance;
     
    public static ConfiguracoesDAO getInstance(Context context) {
        if(instance == null)
            instance = new ConfiguracoesDAO(context);
        return instance;
    }
 
    private ConfiguracoesDAO(Context context) {
        PersistenceHelper persistenceHelper = PersistenceHelper.getInstance(context);
        dataBase = persistenceHelper.getWritableDatabase();
    }
 
    public void salvar(Configuracoes Configuracoes) {
        ContentValues values = gerarContentValuesConfiguracoes(Configuracoes);
        dataBase.insert(NOME_TABELA, null, values);
    }
 
    public List<Configuracoes> recuperarTodos() {
        String queryReturnAll = "SELECT * FROM " + NOME_TABELA;
        Cursor cursor = dataBase.rawQuery(queryReturnAll, null);
        List<Configuracoes> configuracoes = construirConfiguracoesPorCursor(cursor);
 
        return configuracoes;
    }

    public Configuracoes recuperarPorId(int id) {
    	String queryReturnAll = "SELECT * FROM " + NOME_TABELA + " WHERE " + COLUNA_ID + "= " + id;
    	Cursor cursor = dataBase.rawQuery(queryReturnAll, null);
    	List<Configuracoes> Configuracoess = construirConfiguracoesPorCursor(cursor);

    	if(Configuracoess.size() > 0) {
    		return Configuracoess.get(0);
    	}
    	return null;
    }
    
    public List<Configuracoes> recuperarTodosOrdenados() {
        String queryReturnAll = "SELECT * FROM " + NOME_TABELA + " ORDER BY  "+COLUNA_ID;
        Cursor cursor = dataBase.rawQuery(queryReturnAll, null);
        List<Configuracoes> configuracoes = construirConfiguracoesPorCursor(cursor);
 
        return configuracoes;
    }
 
    public void deletar(Configuracoes configuracoes) {
 
        String[] valoresParaSubstituir = {
                String.valueOf(configuracoes.getId())
        };
 
        dataBase.delete(NOME_TABELA, COLUNA_ID + " =  ?", valoresParaSubstituir);
    }
 
    public void editar(Configuracoes configuracoes) {
        ContentValues valores = gerarContentValuesConfiguracoes(configuracoes);
 
        String[] valoresParaSubstituir = {
                String.valueOf(configuracoes.getId())
        };
 
        dataBase.update(NOME_TABELA, valores, COLUNA_ID + " = ?", valoresParaSubstituir);
    }
 
    public void fecharConexao() {
        if(dataBase != null && dataBase.isOpen())
            dataBase.close(); 
    }
 
 
    private List<Configuracoes> construirConfiguracoesPorCursor(Cursor cursor) {
        List<Configuracoes> Configuracoess = new ArrayList<Configuracoes>();
        if(cursor == null)
            return Configuracoess;
         
        try {
 
            if (cursor.moveToFirst()) {
                do {
 
                    int indexID = cursor.getColumnIndex(COLUNA_ID);
                    int indexJornadaHoras = cursor.getColumnIndex(COLUNA_JORNADA_HORAS);
                    int indexJornadaMinutos = cursor.getColumnIndex(COLUNA_JORNADA_MINUTOS);
                    int indexSabadoUtil = cursor.getColumnIndex(COLUNA_SABADO_UTIL);
                    int indexSaldoAcumulado = cursor.getColumnIndex(COLUNA_SALDO_ACUMULADO);
                    int indexSaldoAcumuladoMinutos = cursor.getColumnIndex(COLUNA_SALDO_ACUMULADO_MINUTOS);
 
                    int id = cursor.getInt(indexID);
                    Integer jornadaHoras = cursor.getInt(indexJornadaHoras);
                    Integer jornadaMinutos = cursor.getInt(indexJornadaMinutos);
                    Boolean sabadoUtil = cursor.getInt(indexSabadoUtil) == 1 ? true : false;
                    Integer saldoAcumulado = cursor.getInt(indexSaldoAcumulado);
                    Integer saldoAcumuladoMinutos = cursor.getInt(indexSaldoAcumuladoMinutos);
 
                    Configuracoes ConfiguracoesVO = new Configuracoes(id, jornadaHoras, jornadaMinutos, sabadoUtil, saldoAcumulado, saldoAcumuladoMinutos);
 
                    Configuracoess.add(ConfiguracoesVO);
 
                } while (cursor.moveToNext());
            }
             
        } finally {
            cursor.close();
        }
        return Configuracoess;
    }
 
    private ContentValues gerarContentValuesConfiguracoes(Configuracoes configuracoes) {
        ContentValues values = new ContentValues();
        values.put(COLUNA_JORNADA_HORAS, configuracoes.getJornadaHoras());
        values.put(COLUNA_JORNADA_MINUTOS, configuracoes.getJornadaMinutos());
        values.put(COLUNA_SABADO_UTIL, configuracoes.getSabadoUtil() ? 1 : 0);
        values.put(COLUNA_SALDO_ACUMULADO, configuracoes.getSaldoAcumulado());
        values.put(COLUNA_SALDO_ACUMULADO_MINUTOS, configuracoes.getSaldoAcumuladoMinutos());

        return values;
    }

	public void deletarTodos() {
		dataBase.delete(NOME_TABELA, null, null);
	}
}
