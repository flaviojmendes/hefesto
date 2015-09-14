package com.fjmob.ponto.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.fjmob.ponto.entity.Falta;
import com.fjmob.ponto.entity.Historico;
import com.fjmob.ponto.entity.Mood;
import com.fjmob.ponto.persistence.FaltaDAO;
import com.fjmob.ponto.persistence.HistoricoDAO;
import com.fjmob.ponto.persistence.MoodDAO;
import com.thoughtworks.xstream.XStream;

public class ManipulacaoDadosHandler {

	public File exportarDados(List<Historico> historicos, List<Mood> moods, List<Falta> faltas) throws IOException {
		XStream xStream = new XStream();

		// Historicos
		String xml = xStream.toXML(historicos);
		
		File sdcard = Environment.getExternalStorageDirectory();
		File dir = new File(sdcard.getAbsolutePath() + "/pontoEletronico/");
		dir.mkdir();
		File file = new File(dir, "backup.xml");
		FileOutputStream os = new FileOutputStream(file);
		String data = xml;
		os.write(data.getBytes());
		os.close();
		
		// Moods
		String xmlMoods = xStream.toXML(moods);
		
		dir.mkdir();
		File fileMoods = new File(dir, "backupMoods.xml");
		FileOutputStream osMoods = new FileOutputStream(fileMoods);
		String dataMoods = xmlMoods;
		osMoods.write(dataMoods.getBytes());
		osMoods.close();

		// Faltas

		String xmlFaltas = xStream.toXML(faltas);

		dir.mkdir();
		File fileFaltas = new File(dir, "backupFaltas.xml");
		FileOutputStream osFaltas = new FileOutputStream(fileFaltas);
		String dataFaltas = xmlFaltas;
		osFaltas.write(dataFaltas.getBytes());
		osFaltas.close();


		return file;
	}
	
	@SuppressWarnings("unchecked")
	public void importarDados(Context context) {
		File sdcard = Environment.getExternalStorageDirectory();
		File dir = new File(sdcard.getAbsolutePath() + "/pontoEletronico/");
		File file = new File(dir, "backup.xml");
		
		 String result = "";
		    if ( file.exists() ) {
		        FileInputStream fis = null;
		        try {

		            fis = new FileInputStream(file);
		            char current;
		            while (fis.available() > 0) {
		                current = (char) fis.read();
		                result = result + String.valueOf(current);

		            }

		        } catch (Exception e) {
		            Log.d("TourGuide", e.toString());
		        } finally {
		            if (fis != null)
		                try {
		                    fis.close();
		                } catch (IOException ignored) {
		            }
		        }
		        
		        
		        XStream xStream = new XStream();
		        List<Historico> historicos = (List<Historico>) xStream.fromXML(result);
		        
		        for(Historico historico : historicos) {
		        	if(HistoricoDAO.getInstance(context).recuperarPorId(historico.getId()) == null) {
		        		HistoricoDAO.getInstance(context).salvar(historico);
		        	}
		        }
		        
		        
		    }
	}
	
	@SuppressWarnings("unchecked")
	public void importarDadosMoods(Context context) {
		File sdcard = Environment.getExternalStorageDirectory();
		File dir = new File(sdcard.getAbsolutePath() + "/pontoEletronico/");
		File file = new File(dir, "backupMoods.xml");
		
		 String result = "";
		    if ( file.exists() ) {
		        FileInputStream fis = null;
		        try {

		            fis = new FileInputStream(file);
		            char current;
		            while (fis.available() > 0) {
		                current = (char) fis.read();
		                result = result + String.valueOf(current);

		            }

		        } catch (Exception e) {
		            Log.d("TourGuide", e.toString());
		        } finally {
		            if (fis != null)
		                try {
		                    fis.close();
		                } catch (IOException ignored) {
		            }
		        }
		        
		        
		        XStream xStream = new XStream();
 		        List<Mood> moods = (List<Mood>) xStream.fromXML(result);
		        
		        for(Mood mood : moods) {
		        	if(MoodDAO.getInstance(context).recuperarPorId(mood.getId()) == null) {
		        		MoodDAO.getInstance(context).salvar(mood);
		        	}
		        }
		        
		        
		    }
	}

	public void importarDadosFaltas(Context context) {
		File sdcard = Environment.getExternalStorageDirectory();
		File dir = new File(sdcard.getAbsolutePath() + "/pontoEletronico/");
		File file = new File(dir, "backupFaltas.xml");

		String result = "";
		if ( file.exists() ) {
			FileInputStream fis = null;
			try {

				fis = new FileInputStream(file);
				char current;
				while (fis.available() > 0) {
					current = (char) fis.read();
					result = result + String.valueOf(current);

				}

			} catch (Exception e) {
				Log.d("TourGuide", e.toString());
			} finally {
				if (fis != null)
					try {
						fis.close();
					} catch (IOException ignored) {
					}
			}


			XStream xStream = new XStream();
			List<Falta> faltas = (List<Falta>) xStream.fromXML(result);

			for(Falta falta : faltas) {
				if(FaltaDAO.getInstance(context).recuperarPorId(falta.getId()) == null) {
					FaltaDAO.getInstance(context).salvar(falta);
				}
			}


		}
	}
	
	
}
