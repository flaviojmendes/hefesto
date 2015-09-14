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

import com.fjmob.ponto.entity.Mood;

public class MoodDAO {
	 
	 
    public static final String NOME_TABELA = "Mood";
    public static final String COLUNA_ID = "id";
    public static final String COLUNA_DATA_GRAVACAO = "dataGravacao";
    public static final String COLUNA_COMENTARIO = "comentario";
    public static final String COLUNA_MOOD = "mood";
 
 
    public static final String SCRIPT_CRIACAO_TABELA = "CREATE TABLE " + NOME_TABELA + "("
            + COLUNA_ID + " INTEGER PRIMARY KEY," + 
    		COLUNA_DATA_GRAVACAO + " TEXT, " + COLUNA_COMENTARIO + " TEXT, " + COLUNA_MOOD + " TEXT)";
 
    public static final String SCRIPT_DELECAO_TABELA =  "DROP TABLE IF EXISTS " + NOME_TABELA;
 
 
    private SQLiteDatabase dataBase = null;
 
 
    private static MoodDAO instance;
     
    public static MoodDAO getInstance(Context context) {
        if(instance == null)
            instance = new MoodDAO(context);
        return instance;
    }
 
    private MoodDAO(Context context) {
        PersistenceHelper persistenceHelper = PersistenceHelper.getInstance(context);
        dataBase = persistenceHelper.getWritableDatabase();
    }
 
    public void salvar(Mood mood) {
        ContentValues values = gerarContentValuesMood(mood);
        dataBase.insert(NOME_TABELA, null, values);
    }
 
    public List<Mood> recuperarTodos() {
        String queryReturnAll = "SELECT * FROM " + NOME_TABELA;
        Cursor cursor = dataBase.rawQuery(queryReturnAll, null);
        List<Mood> moods = construirMoodPorCursor(cursor);
 
        return moods;
    }

    public Mood recuperarPorId(int id) {
    	String queryReturnAll = "SELECT * FROM " + NOME_TABELA + " WHERE " + COLUNA_ID + "= " + id;
    	Cursor cursor = dataBase.rawQuery(queryReturnAll, null);
    	List<Mood> moods = construirMoodPorCursor(cursor);

    	if(moods.size() > 0) {
    		return moods.get(0);
    	}
    	return null;
    }
    
    public List<Mood> recuperarTodosOrdenados() {
        String queryReturnAll = "SELECT * FROM " + NOME_TABELA + " ORDER BY  "+COLUNA_ID +  " DESC";
        Cursor cursor = dataBase.rawQuery(queryReturnAll, null);
        List<Mood> moods = construirMoodPorCursor(cursor);
 
        return moods;
    }
    
    public List<Mood> recuperarTodosOrdenadosAsc() {
        String queryReturnAll = "SELECT * FROM " + NOME_TABELA + " ORDER BY  "+COLUNA_ID +  " ASC";
        Cursor cursor = dataBase.rawQuery(queryReturnAll, null);
        List<Mood> moods = construirMoodPorCursor(cursor);
 
        return moods;
    }
 
    public void deletar(Mood mood) {
 
        String[] valoresParaSubstituir = {
                String.valueOf(mood.getId())
        };
 
        dataBase.delete(NOME_TABELA, COLUNA_ID + " =  ?", valoresParaSubstituir);
    }
 
    public void editar(Mood mood) {
        ContentValues valores = gerarContentValuesMood(mood);
 
        String[] valoresParaSubstituir = {
                String.valueOf(mood.getId())
        };
 
        dataBase.update(NOME_TABELA, valores, COLUNA_ID + " = ?", valoresParaSubstituir);
    }
 
    public void fecharConexao() {
        if(dataBase != null && dataBase.isOpen())
            dataBase.close(); 
    }
 
 
    private List<Mood> construirMoodPorCursor(Cursor cursor) {
        List<Mood> moods = new ArrayList<Mood>();
        if(cursor == null)
            return moods;
         
        try {
 
            if (cursor.moveToFirst()) {
                do {
 
                    int indexID = cursor.getColumnIndex(COLUNA_ID);
                    int indexDataGravacao = cursor.getColumnIndex(COLUNA_DATA_GRAVACAO);
                    int indexComentario = cursor.getColumnIndex(COLUNA_COMENTARIO);
                    int indexMood = cursor.getColumnIndex(COLUNA_MOOD);
                    
                    int id = cursor.getInt(indexID);
                    String dataGravacaoTemp = cursor.getString(indexDataGravacao);
                    Date dataGravacao = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dataGravacaoTemp);
                    String comentario = cursor.getString(indexComentario);
                    String mood = cursor.getString(indexMood);
                    
                    Mood moodVO = new Mood(id, mood, dataGravacao, comentario);
 
                    moods.add(moodVO);
 
                } while (cursor.moveToNext());
            }
             
        } catch (ParseException e) {
        	cursor.close();
		} finally {
            cursor.close();
        }
        return moods;
    }
 
    private ContentValues gerarContentValuesMood(Mood mood) {
        ContentValues values = new ContentValues();
        values.put(COLUNA_DATA_GRAVACAO, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(mood.getDataGravacao()));
        values.put(COLUNA_COMENTARIO, mood.getComentario());
        values.put(COLUNA_MOOD, mood.getMood());
        
        return values;
    }

	public void deletarTodos() {
		dataBase.delete(NOME_TABELA, null, null);
	}
}
