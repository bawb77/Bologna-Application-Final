package com.example.checkout;

import java.util.ArrayList;
import java.util.Calendar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
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
import android.widget.ListAdapter;
import android.widget.Toast;

public class AdminAct extends Activity {
//array lists for managing items and inventory
	ArrayList<Items> itemList = new ArrayList<Items>();
	ArrayList<Items> EditedItemList = new ArrayList<Items>();
	ArrayList<CartItems> cartItems = new ArrayList<CartItems>();
	//Drawable[] pic;
	Bitmap[] picMap;
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
        	}
			@Override public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
			@Override public void onTextChanged(CharSequence s, int start, int before,int count) {}
        }); 
 /*       pic = new Drawable[]{getResources().getDrawable( R.drawable.nopic),
        		getResources().getDrawable(R.drawable.groceries),
        		getResources().getDrawable(R.drawable.automotive),
        		getResources().getDrawable(R.drawable.bath),
        		getResources().getDrawable(R.drawable.toys),
        		getResources().getDrawable(R.drawable.kitchenware),
        		getResources().getDrawable(R.drawable.clothes),
        		};*/
        Bitmap nopic = BitmapFactory.decodeResource(this.getResources(), R.drawable.nopic);//0
        Bitmap grocery = BitmapFactory.decodeResource(this.getResources(), R.drawable.groceries);//1
        Bitmap auto = BitmapFactory.decodeResource(this.getResources(), R.drawable.automotive);//2
        Bitmap bath = BitmapFactory.decodeResource(this.getResources(), R.drawable.bath);//3
        Bitmap toys = BitmapFactory.decodeResource(this.getResources(), R.drawable.toys);//4
        Bitmap kitchen = BitmapFactory.decodeResource(this.getResources(), R.drawable.kitchenware);//5
        Bitmap clothes = BitmapFactory.decodeResource(this.getResources(), R.drawable.clothes);//6
        picMap = new Bitmap[]{nopic,grocery,auto,bath,toys,kitchen,clothes};
        	  
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
    
    public void updatePN(View v)
    {
    	
    	if(et_name.getText().toString() != "" && et_price.getText().toString() != ""){
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
						Items item = new Items(et_name.getText().toString(), Double.parseDouble(et_price.getText().toString()), new_id, picMap[choice] , 0);
						db.addResult(item);
    			
						Toast.makeText(getBaseContext(), et_name.getText().toString() + " was added.", Toast.LENGTH_SHORT).show();
						db.addLog(new LogItem(et_name.getText().toString() + " was added", date_today, 2));
						dialog.dismiss();
						clearInfos();
	            	}
				}); 
				//create and then show the alert dialog
				builder.create();
				builder.show();
				
    			
    		} else
    		{
    			Items item = EditedItemList.get(selectedItem);
    			db.changeItem(item, et_name.getText().toString(), Double.parseDouble(et_price.getText().toString()), item.group, item.pic);
    			
    			Toast.makeText(getBaseContext(), et_name.getText().toString() + " was changed.", Toast.LENGTH_SHORT).show();
    			db.addLog(new LogItem(et_name.getText().toString() + " was changed", date_today, 2));
    			clearInfos();
    		}
    	}
    	update();
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
    
    private void clearInfos(){
    	et_name.setText("");
    	et_price.setText("");
    	createNewItem = true;
    	selectedItem = 0;
    }
    
    public void returnToCheckout(View v)
    {
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
