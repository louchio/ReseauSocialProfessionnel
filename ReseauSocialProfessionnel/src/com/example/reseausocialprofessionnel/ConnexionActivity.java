package com.example.reseausocialprofessionnel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ConnexionActivity extends Activity {

	private static final String strURL = "http://10.0.2.2/www/utilisateur.php";
	//	private String result ;
		private Button connexion ;
		private EditText Login ;
		private EditText password ;
		private JSONArray 	jArray;
		private JSONObject  json_data;
		Button btnLinkToRegister;

	    /** Called when the activity is first created. */
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.connexion);
	        Button connexion = (Button) findViewById(R.id.btnLogin);
	        final EditText Login = (EditText) findViewById(R.id.loginEmail);
	        final EditText password = (EditText) findViewById(R.id.loginPassword);
	        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);
	    
	        connexion.setOnClickListener(new View.OnClickListener() {
	        	
	            public void onClick(View v) {
	            	String result =null;
	            	InputStream is = null;
	            	StringBuilder sb = new StringBuilder();
	            	ArrayList<NameValuePair> nameValuePairs=new ArrayList<NameValuePair> ();
	            	nameValuePairs.add(new BasicNameValuePair("user",Login.getText().toString()));

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
	    			try{
	    				BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
	    		
	    				String line = null;
	    				while ((line = reader.readLine()) != null) {
	    					sb.append(line + "\n");
	    					
	    				}
	    				is.close();
	    				result=sb.toString();
	      	
	            	}catch(Exception e){
	        			Log.e("log_tag", "Error in http connection " + e.toString());
	        		}
	    			String essai=result.substring(0, 4) ;
	    			try {
	    				if (result.matches("<br >")){
	    					 essai=result.substring(0, 2) ; 
	    				}
	    				JSONArray jArray = new JSONArray(result);
	    				int b=jArray.length();
	    				
	    				   JSONObject json_data = jArray.getJSONObject(0);
	    			
	    				
	    				   String mot_pass =	json_data.getString("password").toString();
	    				   String login =	json_data.getString("login").toString();
	    				   if (mot_pass.equals(password.getText().toString()) ){
	    				    
	    					   Intent principale =new Intent(ConnexionActivity.this,MenuActivity.class);//MainActivity
	    					   startActivity(principale);
	    				
	    				   }
	    				   else {
	    					   
	    					   Toast.makeText(ConnexionActivity.this, "mot de pass invalide ", Toast.LENGTH_LONG).show();
	    				   }

	    		   }catch(JSONException e){
	    			 //  Log.e("log_tag", "Error parsing data " + e.toString());
	    			   Toast.makeText(ConnexionActivity.this, "Utilisateur Inexistant ", Toast.LENGTH_LONG).show();
	    		   }
	            }
	            });          
	     // Link to Register Screen
	        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {
	 
	            public void onClick(View view) {
	                Intent i = new Intent(ConnexionActivity.this,
	                        RegisterActivity.class);
	                startActivity(i);
	                //finish();
	            }
	        });
	    }
}