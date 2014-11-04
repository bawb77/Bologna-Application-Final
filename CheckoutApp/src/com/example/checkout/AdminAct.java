package com.example.checkout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Toast;

public class AdminAct extends Activity {
//array lists for managing items and inventory
	ArrayList<Items> itemList = new ArrayList<Items>();
	ArrayList<Items> EditedItemList = new ArrayList<Items>();
	ArrayList<CartItems> cartItems = new ArrayList<CartItems>();
	Bitmap[] picMap;
	// layout item instantiation
	GridView ItemGrid;
	CustomGridViewAdapter customGridAdapter;
	CustomGridViewAdapterCart customGridAdapterCart;
	double Total;
	boolean discount5;
	boolean discount10;
	EditText searchText;
	
	ImageView iv_group;
	EditText et_name;
	EditText et_price;
	boolean createNewItem;
	int selectedItem;
	
	String date_today;
	
	SqlLiteYouMeanIt db;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
       
        //starting Items for Grid
        db = new SqlLiteYouMeanIt(this);
        
        et_name = (EditText)findViewById(R.id.pNDisplay);
        et_price = (EditText)findViewById(R.id.pPDisplay);
        iv_group = (ImageView)findViewById(R.id.iv_group);
        
        Date date = new Date();
    	String dateFormat = "dd/MM/yyyy";
    	SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
    	date_today = sdf.format(date);
    	
        createNewItem = true;
        
        itemList = db.getAllItems();
        EditedItemList = db.getAllItems();
        mainListDisplay();
        searchText = (EditText) findViewById(R.id.searchTexta);
        searchText.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
			}	
        });
        et_name.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
			}	
        });
        et_price.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
			}	
        });
        searchText.addTextChangedListener(new TextWatcher(){
        	public void afterTextChanged(Editable s) {
        		editMainList();
        	}
			@Override public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
			@Override public void onTextChanged(CharSequence s, int start, int before,int count) {}
        }); 
        Bitmap nopic = BitmapFactory.decodeResource(this.getResources(), R.drawable.nopic);//0
        Bitmap grocery = BitmapFactory.decodeResource(this.getResources(), R.drawable.groceries);//1
        Bitmap auto = BitmapFactory.decodeResource(this.getResources(), R.drawable.automotive);//2
        Bitmap bath = BitmapFactory.decodeResource(this.getResources(), R.drawable.bath);//3
        Bitmap toys = BitmapFactory.decodeResource(this.getResources(), R.drawable.toys);//4
        Bitmap kitchen = BitmapFactory.decodeResource(this.getResources(), R.drawable.kitchenware);//5
        Bitmap clothes = BitmapFactory.decodeResource(this.getResources(), R.drawable.clothes);//6
        picMap = new Bitmap[]{nopic,grocery,auto,bath,toys,kitchen,clothes};
       // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        	  
    }
    public void update()
    {    	
    	itemList = db.getAllItems();
    	EditedItemList = db.getAllItems();
    	editMainList();
    }
    public void editMainList()
    {
    	EditedItemList.clear();
    	
    	String filter = searchText.getText().toString().toLowerCase();
    	if(!itemList.isEmpty())
    	{
    		for(Items tempItem: itemList)
    		{
    			if(tempItem.item.toLowerCase().startsWith(filter))
    			{
    				EditedItemList.add(tempItem);
    			}
    		}
    	}
    	mainListDisplay();
    }
    public void mainListDisplay()
    { 
    	//link GridView to itemlist
    	ItemGrid = (GridView)findViewById(R.id.itemGrida);
        customGridAdapter = new CustomGridViewAdapter(this, R.layout.row_grid, EditedItemList);
        ItemGrid.setAdapter(customGridAdapter);
        //set onClick listener for the GridView
        ItemGrid.setOnItemClickListener(new OnItemClickListener() {
        	@Override
			public void onItemClick(AdapterView<?> parent, View v,
				int position, long id) {
        		hideKeyboard();
        		
				addList(position);
			}
		});
    }
    //add items from the girdview list to the changes views
    public void addList(int i)
    {
    	Items item = EditedItemList.get(i);
    	et_name.setText(item.item);
    	et_price.setText(item.price + "");
    	
    	iv_group.setImageBitmap(item.getPic());
    	iv_group.setVisibility(ImageView.VISIBLE);
    	
    	selectedItem = item.itemId;
    	
    	createNewItem = false;
    }
    
    public void updatePN(View v)
    {
    	if(!et_name.getText().toString().matches("") && !et_price.getText().toString().matches(""))
    	{
    		if(createNewItem)
    		{
				//create alert dialog
    			ListAdapter adapter = new dialogAdapter(this,R.layout.dialog_row, picMap);
				AlertDialog.Builder builder = new AlertDialog.Builder(AdminAct.this);
				builder.setTitle("Select Type of Good");
				//set question answer choices
				builder.setSingleChoiceItems(adapter, -1 , new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int choice) {
						Integer new_id = db.getLastId() + 1;
						Log.v("ALC", new_id + " id");
						Items item = new Items(et_name.getText().toString(), Double.parseDouble(et_price.getText().toString()), new_id, picMap[choice] , 0);
						db.addResult(item);
						Toast.makeText(getBaseContext(), et_name.getText().toString() + " was added.", Toast.LENGTH_SHORT).show();
						db.addLog(new LogItem(et_name.getText().toString() + " was added", date_today, 2));
						dialog.dismiss();
						clearInfos();
						update();
	            	}
				}); 
				//create and then show the alert dialog
				builder.create();
				builder.show();	
    		} else
    		{
    			for(Items item : EditedItemList){
    				if(item.itemId == selectedItem){
    					Bitmap bitmap = ((BitmapDrawable)iv_group.getDrawable()).getBitmap();
		    			db.changeItem(item, et_name.getText().toString(), Double.parseDouble(et_price.getText().toString()), item.group, bitmap);
		    			
		    			Toast.makeText(getBaseContext(), et_name.getText().toString() + " was changed.", Toast.LENGTH_SHORT).show();
		    			db.addLog(new LogItem(et_name.getText().toString() + " was changed", date_today, 2));
		    			clearInfos();
		    			update();
    				}
    			}
    			
    		}
    	} else{
    		Toast.makeText(getBaseContext(), "Product name and price have to be filled.", Toast.LENGTH_SHORT).show();
    	}
    	
    	hideKeyboard();
    }
    
    public void changeGroup(View v)
    {
    	//create alert dialog
		ListAdapter adapter = new dialogAdapter(this,R.layout.dialog_row, picMap);
		AlertDialog.Builder builder = new AlertDialog.Builder(AdminAct.this);
		builder.setTitle("Select Type of Good");
		//set question answer choices
		builder.setSingleChoiceItems(adapter, -1 , new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int choice) {
				iv_group.setImageBitmap(picMap[choice]);
				dialog.dismiss();
        	}
		}); 
		//create and then show the alert dialog
		builder.create();
		builder.show();	
    }
    
    public void deleteP(View v)
    {
    	db.deleteItem(selectedItem);
    	Toast.makeText(getBaseContext(), et_name.getText().toString() + " was deleted.", Toast.LENGTH_SHORT).show();
    	db.addLog(new LogItem(et_name.getText().toString() + " was deleted", date_today, 2));
    	
    	clearInfos();
    	update();
    }
    
    public void clear(View v){
    	clearInfos();
    }
    public void hideKeyboard()
    {
    	InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE); 

    	inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                   InputMethodManager.HIDE_NOT_ALWAYS);
    }
    
    private void clearInfos(){
    	hideKeyboard();
    	
    	et_name.setText("");
    	et_price.setText("");
    	createNewItem = true;
    	selectedItem = 0;
    	
    	iv_group.setVisibility(ImageView.INVISIBLE);
    }
    
    public void showLogs(View v){
    	startActivity(new Intent(this, ShowLogs.class));
    }
    
    public void returnToCheckout(View v)
    {
    	db.addLog(new LogItem("Change to customer view", date_today, 0));
    	super.onBackPressed();
    	finish();
    }
    
	//remove items from the checkout cart and update the totals
    //update the display of the cart with the current arraylist of objects added
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.checkout_main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
