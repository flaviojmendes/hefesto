package com.fjmob.ponto.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PersistenceHelper extends SQLiteOpenHelper {
	 
    public static final String NOME_BANCO =  "PtDB";
    public static final int VERSAO =  10;
     
    private static PersistenceHelper instance;
     
    private PersistenceHelper(Context context) {
        super(context, NOME_BANCO, null, VERSAO);
    }
     
    public static PersistenceHelper getInstance(Context context) {
        if(instance == null)
            instance = new PersistenceHelper(context);
         
        return instance;
    }
 
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ConfiguracoesDAO.SCRIPT_CRIACAO_TABELA);
        db.execSQL(HistoricoDAO.SCRIPT_CRIACAO_TABELA_HISTORICO);
        db.execSQL(MoodDAO.SCRIPT_CRIACAO_TABELA);
        db.execSQL(FaltaDAO.SCRIPT_CRIACAO_TABELA);
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	if(newVersion == 4) {
    		db.execSQL(ConfiguracoesDAO.SCRIPT_DELECAO_TABELA);
    		db.execSQL(ConfiguracoesDAO.SCRIPT_CRIACAO_TABELA);
    		
    	}
    	if(newVersion == 5 && oldVersion >= 4) {
    		db.execSQL("ALTER TABLE " + ConfiguracoesDAO.NOME_TABELA + " ADD COLUMN " + ConfiguracoesDAO.COLUNA_SABADO_UTIL + " INTEGER;");
    	}
    	if(newVersion == 6 && oldVersion >= 4) {
    		db.execSQL("ALTER TABLE " + HistoricoDAO.NOME_TABELA + " ADD COLUMN " + HistoricoDAO.COLUNA_COMENTARIO + " TEXT;");
    	}
    	if(newVersion == 7 && oldVersion >= 6) {
    		
    		db.execSQL(MoodDAO.SCRIPT_CRIACAO_TABELA);
    		
    	} else if(newVersion == 7 && oldVersion == 5) {
    		
    		db.execSQL("ALTER TABLE " + HistoricoDAO.NOME_TABELA + " ADD COLUMN " + HistoricoDAO.COLUNA_COMENTARIO + " TEXT;");
    		db.execSQL(MoodDAO.SCRIPT_CRIACAO_TABELA);
    	}

        if(newVersion == 10 && oldVersion < 8) {
            db.execSQL(FaltaDAO.SCRIPT_CRIACAO_TABELA);
        }

        if(newVersion == 10) {
            try {
                db.execSQL("ALTER TABLE " + ConfiguracoesDAO.NOME_TABELA + " ADD COLUMN " + ConfiguracoesDAO.COLUNA_SALDO_ACUMULADO + " INTEGER;");
            } catch (Exception e) {

            }
            try {
                db.execSQL("ALTER TABLE " + ConfiguracoesDAO.NOME_TABELA + " ADD COLUMN " + ConfiguracoesDAO.COLUNA_SALDO_ACUMULADO_MINUTOS + " INTEGER;");
            } catch (Exception e) {

            }
        }
    }
    
 
    
}
