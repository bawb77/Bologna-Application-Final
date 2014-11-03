package com.example.checkout;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.print.PrintManager;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class ShowLogs extends Activity {
	
	List<LogItem> logs;
	
	Button bt_print;
	Button bt_return;
	ListView lv_logs;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_logs);
	    
	    bt_print = (Button)findViewById(R.id.bt_print);
	    bt_return = (Button)findViewById(R.id.bt_retAP);
	    lv_logs = (ListView)findViewById(R.id.lv_logs);
	
	    SqlLiteYouMeanIt db = new SqlLiteYouMeanIt(this);
	    
	    logs = db.getAllLogs();
	}

	public void deleteLogs(View v){
		AlertDialog diaBox = AskOption();
		diaBox.show();
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
}
