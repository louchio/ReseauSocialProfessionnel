package com.example.reseausocialprofessionnel;



import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

/*
 * On va faire impl�menter un listner � notre classe, ce qui veut dire que l'activit� interceptera d'elle
 * les �venements
 * Notre activit� d�tectera les touchers et les clics sur les vues qui sont inscrites 
 * */
public class MainActivity extends Activity{
	
	TextView txt;

	
	/**
	 * La m�thode est la premi�re qui est lanc� au d�marage d'une applucation
	 * Bundle : Il vaut null quand on quitte l'application apres un lancement 
	 * 			de l'application ou d'un d�marrage normale
	 */
	
	
	private TextView coucou = null;

	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);


		LinearLayout rootLayout = new LinearLayout(getApplicationContext());  
		txt = new TextView(getApplicationContext());  
		rootLayout.addView(txt);  
		setContentView(rootLayout);  

		// D�finir le texte et appeler la fonction de connexion.  
		txt.setText("Connexion..."); 
		// Appeler la m�thode pour r�cup�rer les donn�es JSON
		txt.setText(getServerData(strURL)); 
			    
	  }
	  
	  public static final String strURL = "http://10.0.2.2/www/ville.php";
	  private String getServerData(String returnString) {
			InputStream is = null;
			String result = "";
			// Envoyer la requ�te au script PHP.
			// Script PHP : $sql=mysql_query("select * from tblVille where Nom_ville like '".$_REQUEST['ville']."%'");
			// $_REQUEST['ville'] sera remplac� par L dans notre exemple.
			// Ce qui veut dire que la requ�te enverra les villes commen�ant par la lettre L
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("ville","L"));
			// Envoie de la commande http
			try{
				
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(strURL);
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				is = entity.getContent();
			}catch(Exception e){
				Log.e("log_tag", "Error in http connection " + e.toString());
			}
			
			Log.e("log_tag", " yowww");

			// Convertion de la requ�te en string
			try{
				BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				is.close();
				result=sb.toString();
			}catch(Exception e){
				Log.e("log_tag", "Error converting result " + e.toString());
			}
			// Parse les donn�es JSON
			try{
				JSONArray jArray = new JSONArray(result);
				for(int i=0;i<jArray.length();i++){
					JSONObject json_data = jArray.getJSONObject(i);
					// Affichage ID_ville et Nom_ville dans le LogCat
					Log.i("log_tag","ID_ville: "+json_data.getInt("ID_ville")+
							", Nom_ville: "+json_data.getString("Nom_ville")
					);
					// R�sultats de la requ�te
					returnString += "\n\t" + jArray.getJSONObject(i); 
				}
			}catch(JSONException e){
				Log.e("log_tag", "Error parsing data " + e.toString());
			}
		
			return returnString; 
		}
	  
	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}*/

}
