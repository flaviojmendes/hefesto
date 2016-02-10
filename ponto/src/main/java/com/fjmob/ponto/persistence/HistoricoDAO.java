package com.fjmob.ponto.persistence;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.fjmob.ponto.entity.Historico;

public class HistoricoDAO {
	 
	 
    public static final String NOME_TABELA = "Historico";
    public static final String COLUNA_ID = "id";
    public static final String COLUNA_DATA_GRAVACAO = "dataGravacao";
    public static final String COLUNA_COMENTARIO = "comentario";
 
 
    public static final String SCRIPT_CRIACAO_TABELA_HISTORICO = "CREATE TABLE " + NOME_TABELA + "("
            + COLUNA_ID + " INTEGER PRIMARY KEY," + 
    		COLUNA_DATA_GRAVACAO + " TEXT, " + COLUNA_COMENTARIO + " TEXT)";
 
    public static final String SCRIPT_DELECAO_TABELA =  "DROP TABLE IF EXISTS " + NOME_TABELA;
 
 
    private SQLiteDatabase dataBase = null;
 
 
    private static HistoricoDAO instance;
     
    public static HistoricoDAO getInstance(Context context) {
        if(instance == null)
            instance = new HistoricoDAO(context);
        return instance;
    }
 
    private HistoricoDAO(Context context) {
        PersistenceHelper persistenceHelper = PersistenceHelper.getInstance(context);
        dataBase = persistenceHelper.getWritableDatabase();
    }
 
    public void salvar(Historico historico) {
        ContentValues values = gerarContentValuesHistorico(historico);
        dataBase.insert(NOME_TABELA, null, values);
    }


    public List<Historico> recuperarTodos(int mes) {
        String queryReturnAll = "SELECT * FROM " + NOME_TABELA;
        Cursor cursor = dataBase.rawQuery(queryReturnAll, null);
        List<Historico> historicos = construirHistoricoPorCursor(cursor);

        return historicos;
    }

    public List<Historico> recuperarTodos() {
        String queryReturnAll = "SELECT * FROM " + NOME_TABELA;
        Cursor cursor = dataBase.rawQuery(queryReturnAll, null);
        List<Historico> historicos = construirHistoricoPorCursor(cursor);
 
        return historicos;
    }

    public Historico recuperarPorId(int id) {
    	String queryReturnAll = "SELECT * FROM " + NOME_TABELA + " WHERE " + COLUNA_ID + "= " + id;
    	Cursor cursor = dataBase.rawQuery(queryReturnAll, null);
    	List<Historico> historicos = construirHistoricoPorCursor(cursor);

    	if(historicos.size() > 0) {
    		return historicos.get(0);
    	}
    	return null;
    }
    
    public List<Historico> recuperarTodosOrdenados() {
        String queryReturnAll = "SELECT * FROM " + NOME_TABELA + " ORDER BY  "+COLUNA_ID +  " DESC";
        Cursor cursor = dataBase.rawQuery(queryReturnAll, null);
        List<Historico> historicos = construirHistoricoPorCursor(cursor);
 
        return historicos;
    }
 
    public void deletar(Historico historico) {
 
        String[] valoresParaSubstituir = {
                String.valueOf(historico.getId())
        };
 
        dataBase.delete(NOME_TABELA, COLUNA_ID + " =  ?", valoresParaSubstituir);
    }
 
    public void editar(Historico historico) {
        ContentValues valores = gerarContentValuesHistorico(historico);
 
        String[] valoresParaSubstituir = {
                String.valueOf(historico.getId())
        };
 
        dataBase.update(NOME_TABELA, valores, COLUNA_ID + " = ?", valoresParaSubstituir);
    }
 
    public void fecharConexao() {
        if(dataBase != null && dataBase.isOpen())
            dataBase.close(); 
    }
 
 
    private List<Historico> construirHistoricoPorCursor(Cursor cursor) {
        List<Historico> historicos = new ArrayList<Historico>();
        if(cursor == null)
            return historicos;
         
        try {
 
            if (cursor.moveToFirst()) {
                do {
 
                    int indexID = cursor.getColumnIndex(COLUNA_ID);
                    int indexDataGravacao = cursor.getColumnIndex(COLUNA_DATA_GRAVACAO);
                    int indexComentario = cursor.getColumnIndex(COLUNA_COMENTARIO);
 
                    int id = cursor.getInt(indexID);
                    String dataGravacaoTemp = cursor.getString(indexDataGravacao);
                    Date dataGravacao = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dataGravacaoTemp);
                    String comentario = cursor.getString(indexComentario);
                    
                    Historico historicoVO = new Historico(id, dataGravacao, comentario);
 
                    historicos.add(historicoVO);
 
                } while (cursor.moveToNext());
            }
             
        } catch (ParseException e) {
        	cursor.close();
		} finally {
            cursor.close();
        }
        return historicos;
    }
 
    private ContentValues gerarContentValuesHistorico(Historico historico) {
        ContentValues values = new ContentValues();
        values.put(COLUNA_DATA_GRAVACAO, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(historico.getDataGravacao()));
        values.put(COLUNA_COMENTARIO, historico.getComentario());
        
        return values;
    }

	public void deletarTodos() {
		dataBase.delete(NOME_TABELA, null, null);
	}
}
