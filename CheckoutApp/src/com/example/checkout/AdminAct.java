package com.example.checkout;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

public class AdminAct extends Activity {
//array lists for managing items and inventory
	ArrayList<Items> itemList = new ArrayList<Items>();
	ArrayList<Items> EditedItemList = new ArrayList<Items>();
	ArrayList<CartItems> cartItems = new ArrayList<CartItems>();
	// layout item instantiation
	GridView ItemGrid;
	CustomGridViewAdapter customGridAdapter;
	CustomGridViewAdapterCart customGridAdapterCart;
	double Total;
	boolean discount5;
	boolean discount10;
	EditText searchText;
	
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

        getWindow().setSoftInputMode(
  		      WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
       
        //starting Items for Grid
        db = new SqlLiteYouMeanIt(this);
        
        et_name = (EditText)findViewById(R.id.pNDisplay);
        et_price = (EditText)findViewById(R.id.pPDisplay);
        
        date_today = Calendar.DAY_OF_MONTH + "/" + Calendar.MONTH + "/" + Calendar.YEAR;
        
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
        searchText.addTextChangedListener(new TextWatcher(){
        	public void afterTextChanged(Editable s) {
        		editMainList();
        		mainListDisplay();
        	}
			@Override public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
			@Override public void onTextChanged(CharSequence s, int start, int before,int count) {}
        });   
    } 
    public void editMainList()
    {
    	EditedItemList.clear();
    	
    	String filter = searchText.getText().toString().toLowerCase();
    	Log.v("ALC", filter);
    	if(!itemList.isEmpty())
    	{
    		for(Items tempItem: itemList)
    		{
    			Log.v("ALC", tempItem.item);
    			if(tempItem.item.toLowerCase().startsWith(filter))
    			{
    				EditedItemList.add(tempItem);
    			}
    		}
    	}
    }
    public void mainListDisplay()
    { 
    	//link GridView to itemlist
    	ItemGrid = (GridView)findViewById(R.id.itemGrid);
        customGridAdapter = new CustomGridViewAdapter(this, R.layout.row_grid, EditedItemList);
        ItemGrid.setAdapter(customGridAdapter);
        //set onClick listener for the GridView
        ItemGrid.setOnItemClickListener(new OnItemClickListener() {
        	@Override
			public void onItemClick(AdapterView<?> parent, View v,
				int position, long id) {
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
    	
    	selectedItem = item.itemId;
    	
    	createNewItem = false;
    }
    
    public void updatePN()
    {
    	if(et_name.getText().toString() != "" && et_price.getText().toString() != ""){
    		if(createNewItem)
    		{
    			Integer new_id = db.getLastId() + 1;
    			Items item = new Items(et_name.getText().toString(), Double.parseDouble(et_price.getText().toString()), new_id, null, 0);
    			db.addResult(item);
    			
    			Toast.makeText(getBaseContext(), et_name.getText().toString() + " was added.", Toast.LENGTH_SHORT).show();
    			db.addLog(new LogItem(et_name.getText().toString() + " was added", date_today, 2));
    		} else
    		{
    			Items item = EditedItemList.get(selectedItem);
    			db.changeItem(item, et_name.getText().toString(), Double.parseDouble(et_price.getText().toString()), item.group, null);
    			
    			Toast.makeText(getBaseContext(), et_name.getText().toString() + " was changed.", Toast.LENGTH_SHORT).show();
    			db.addLog(new LogItem(et_name.getText().toString() + " was changed", date_today, 2));
    		}
    	}
    }
    
    public void deleteP()
    {
    	db.deleteItem(selectedItem);
    	Toast.makeText(getBaseContext(), et_name.getText().toString() + " was deleted.", Toast.LENGTH_SHORT).show();
    	db.addLog(new LogItem(et_name.getText().toString() + " was deleted", date_today, 2));
    	
    	clearInfos();
    }
    
    public void clear(){
    	clearInfos();
    }
    
    private void clearInfos(){
    	et_name.setText("");
    	et_price.setText("");
    	createNewItem = true;
    	selectedItem = 0;
    }
    
    public void returnToCheckout()
    {
    	super.onBackPressed();
    }
    
    @Override
	protected void onPause() {
		super.onPause();
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
