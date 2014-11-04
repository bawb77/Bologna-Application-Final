package com.example.checkout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class ShowLogs extends Activity {
	
	ArrayList<LogItem> logs;
	
	Button bt_print;
	Button bt_return;
	ListView lv_logs;
	
	String username;
	String Temail;
	
	String date_today;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_logs);
	    
	    bt_print = (Button)findViewById(R.id.bt_print);
	    bt_return = (Button)findViewById(R.id.bt_retAP);
	    lv_logs = (ListView)findViewById(R.id.lv_logs);
	
	    updateListView();
	    
	    Date date = new Date();
    	String dateFormat = "dd_MM_yyyy";
    	SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
    	date_today = sdf.format(date);
	}
	
	private void updateListView(){
		SqlLiteYouMeanIt db = new SqlLiteYouMeanIt(this);
	    
	    logs = new ArrayList<LogItem>();
	    logs = db.getAllLogs();
	    
	    ArrayList<String> logs_str = new ArrayList<String>();
	    for(LogItem item : logs){
	    	String user = "";
	    	if(item.user_type == 0){
	    		user = "General";
	    	} else if(item.user_type == 1){
	    		user = "-- Customer";
	    	} else if(item.user_type == 2){
	    		user = "# Admin";
	    	}
	    	
	    	logs_str.add(user + " activity on " + item.date + ":\n" + item.value);
	    }
	    
	    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this, 
                android.R.layout.simple_list_item_1,
                logs_str);

        lv_logs.setAdapter(arrayAdapter); 
	}

	public void deleteLogs(View v){
		AlertDialog diaBox = AskOption();
		diaBox.show();
	}
	
	public void saveLogs(View v){
    	final EditText input = new EditText(this);
    	int type = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS;
    	final EditText inputT = new EditText(this);
    	inputT.setInputType(type);
    	
    	//create change alert box
		AlertDialog builder = new AlertDialog.Builder(ShowLogs.this).create();
        builder.setView(input);
    	builder.setTitle("Please enter your name:");
    	//onclick for after cash received
    	builder.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				username = input.getText().toString();
				
				//create change alert box
				AlertDialog builder2 = new AlertDialog.Builder(ShowLogs.this).create();
		        builder2.setView(inputT);
		    	builder2.setTitle("Please enter your teachers email:");
		    	//onclick for after cash received
		    	builder2.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Temail = inputT.getText().toString();
						
						sendEmail();
					}
		    	});
		    	
		    	builder2.show();
			}
    	});
    	builder.show();
	}
	
	public void sendEmail(){
		try {
			String filename = "/" + username + "POSLogs " + date_today + ".txt";
			
			Intent emailIntent = new Intent(Intent.ACTION_SEND);
			emailIntent.setType("text/plain");
			emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {Temail});
			emailIntent.putExtra(Intent.EXTRA_SUBJECT, "POS Logs");
			
			File file = new File(ShowLogs.this.getFilesDir().toString() + filename);
			if(file.exists()) {
				file.delete();
			}
			file.createNewFile();
			    
			FileOutputStream oFile = new FileOutputStream(file, false); 
			
			OutputStreamWriter fw = new OutputStreamWriter(oFile); 
			
			String text = username + "'s Log:\n\n";
            
            for(LogItem log : logs){
            	boolean change_user = false;
    	    	String user = "";
    	    	if(log.user_type == 0){
    	    		user = "General";
    	    		change_user = true;
    	    	} else if(log.user_type == 1){
    	    		user = "-- Customer";
    	    	} else if(log.user_type == 2){
    	    		user = "# Admin";
    	    	}
    	    	
    	    	String line = "";
    	    	
    	    	if(change_user){
    	    		line += "\n";
    	    	}
    	    	
    	    	line += user + " activity on " + log.date + ":\n" + log.value+ "\n";
    	    	
    	    	if(change_user){
    	    		line += "--------------------------------------------------------\n";
    	    	}
    	    	
    	    	line += "\n";
    	    	
    	    	text += line;
            }
            
            fw.close();
            
            emailIntent.putExtra(Intent.EXTRA_TEXT, text);
            
            //Uri uri = Uri.fromFile(file);
            //emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
			
            startActivity(Intent.createChooser(emailIntent, "Pick an Email provider"));

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error while saving file", Toast.LENGTH_LONG).show();
        }
	}
	
	public void printLogs(View v){
		PrintManager printManager = (PrintManager) this
	            .getSystemService(Context.PRINT_SERVICE);

	    String jobName = this.getString(R.string.app_name) + 
                        " Document";

	    printManager.print(jobName, new PrintLogsAdapter(this),
	             null);
	}
	
	 private AlertDialog AskOption()
	 {
	    AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this) 
	        //set message, title, and icon
	        .setTitle("Delete") 
	        .setMessage("Do you really want to delete all logs?")

	        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

	            public void onClick(DialogInterface dialog, int whichButton) { 
	                //your deleting code
	            	SqlLiteYouMeanIt db = new SqlLiteYouMeanIt(getBaseContext());
	        		db.deleteAllLogs();
	        		updateListView();
	                dialog.dismiss();
	            }   

	        })



	        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {

	                dialog.dismiss();
	            }
	        })
	        .create();
	        return myQuittingDialogBox;

	    }
	 
	 public void returnToAdminPage(View v)
	    {
	    	super.onBackPressed();
	    	finish();
	    }
}
