package com.example.checkout;

import java.text.DecimalFormat;
import java.util.ArrayList;
import android.app.Activity;
//imports
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
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
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;
public class CheckoutMainActivity extends Activity {
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
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_main);
        //set total to zero to start
        getWindow().setSoftInputMode(
  		      WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        
        Total = 0.00;
        // set starting total to EditText box
        String stringdouble= Double.toString(Total);
        EditText totalText = (EditText) findViewById(R.id.total);
        totalText.setText(stringdouble);
        discount5 = false;
        discount10 = false;
        //starting Items for Grid
        SqlLiteYouMeanIt db = new SqlLiteYouMeanIt(this);
        itemList = db.getAllItems();
        if(itemList.isEmpty())
        {Log.v("ALC", "Fill her up");
        itemList.add(new Items("A man's arm",1.99,1));
        itemList.add(new Items("Wolverine",97.00,2));
        itemList.add(new Items("A Bit of String",2.34,3));
        itemList.add(new Items("FaceSucker",56.34,4));
        itemList.add(new Items("Your Mother's love",0.00,5));
        itemList.add(new Items("Armpit muncher",25.67,6));
        itemList.add(new Items("Hugs",1.00,7));
        itemList.add(new Items("A night at the Roxbury",300.00, 8));
        itemList.add(new Items("Sucka",3.50,9));
        itemList.add(new Items("Morgan Freeman",0.01,10));
        db.addGroupResults(itemList);
        }
        EditedItemList = db.getAllItems();
        mainListDisplay();
        searchText = (EditText) findViewById(R.id.searchText);
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
    public void clearAll(View v)
    {
    	cartItems.removeAll(cartItems);
    	updateCart();
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
    //calculate price total for all objects in the checkout cart
    public double calcTotal()
    {
    	double TempTotal=0;
    	if(!cartItems.isEmpty())
    	{
    		for(CartItems tempI : cartItems)
	    	{
	    		TempTotal += (tempI.getPrice() * tempI.getQuantity());
	    	}
    	}
		return TempTotal;
    }
    //display the current total
    public void displayTotal (double d)
    {
		EditText totalText = (EditText) findViewById(R.id.total);
		DecimalFormat df = new DecimalFormat("#.##");
		totalText.setText(df.format(d));
		displayTax(addTax(d));
    }
    //display total plus tax
    public void displayTax (double d)
    {
    	EditText totalText = (EditText) findViewById(R.id.withTax);
		DecimalFormat df = new DecimalFormat("#.##");
		totalText.setText(df.format(d));
    }
    //add items from the girdview list to the cart list
    public void addList(int i)
    {
    	boolean match = true;
    	for(CartItems temp :cartItems)
    	{
    		if(EditedItemList.get(i).getItem().equals(temp.getItem()))
    		{
    			temp.setQuantity((temp.getQuantity() + 1));
    			updateCart();
    			match = false;
    		}
    	}
    	if(match)
    	{
    		cartItems.add(new CartItems((EditedItemList.get(i).getItem()), (EditedItemList.get(i).getPrice()),1));
    			updateCart();
    			ListView cList = (ListView)findViewById(R.id.cartList);
    			//set onclick listener for removal of items from the checkout cart
    			cList.setOnItemClickListener(new OnItemClickListener() {
		    		public void onItemClick(AdapterView<?> parent, View v, int position, long id)
		    		{
		    			//remove items and decrease total
		    			exterminateItem(v, position);
		    		}
    			});
    	}	
    }
    //remove items from the checkout cart and update the totals
    public void exterminateItem(View v, int position)
    {
    	if(cartItems.get(position).getQuantity() > 1)
    	{
    		cartItems.get(position).setQuantity(cartItems.get(position).getQuantity() - 1);
    		updateCart();
    	}
    	else 
    	{
    	cartItems.remove(position);
    	updateCart();
    	}
    	
    }
    //update the display of the cart with the current arraylist of objects added
    public void updateCart()
    {
    	ListView cList = (ListView)findViewById(R.id.cartList);
    	customGridAdapterCart = new CustomGridViewAdapterCart(this, R.layout.row_grid, cartItems);
        cList.setAdapter(customGridAdapterCart);
        displayTotal(calcTotal());
    }
    //toggle a 5% discount to the current total and recalculate
    public void Dis5(View v)
    { 
    	if(!discount10)
    	{
    		boolean on = ((ToggleButton) v).isChecked();
        if (on) {
        	displayTotal(calcTotal() - (calcTotal()*0.05));
        	discount5 = true;
        }
        else {
        	displayTotal(calcTotal());
        	discount5=false;
        }
    	}
    	else
    	{
    		((ToggleButton) v).setChecked(false);
    	}
    	
    }
  //toggle a 10% discount to the current total and recalculate
    public void Dis10(View v)
    {
    	if(!discount5)
    	{
    			boolean on = ((ToggleButton) v).isChecked();
        if (on) {
        	displayTotal(calcTotal() - (calcTotal()*0.10));
        	discount10 = true;
        }
        else {
        	displayTotal(calcTotal());
        	discount10 = false;
        }
    	}
    	else
    	{
    		((ToggleButton) v).setChecked(false);
    	}
    
    }
    //add 15% tax to passed double amount
    public double addTax(double d)
    {
    	d = d * 1.15;
    	return d;
    }
    //checkout
    public void checkOut(View v)
    {
    	
    	//launch alert dialog box for payment type
    	final double checkoutTotal = addTax(calcTotal());
    	AlertDialog builder = new AlertDialog.Builder(CheckoutMainActivity.this).create();
    	//input for cash back
    	final EditText input = new EditText(this);
    	input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
    	//create alert box
    	builder.setTitle(R.string.payment);
    	builder.setButton(AlertDialog.BUTTON_POSITIVE, "Visa", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(getBaseContext(), "Thanks for the Visa", Toast.LENGTH_LONG).show();
			}//visa
		});
    	builder.setButton(AlertDialog.BUTTON_NEGATIVE, "Debit", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(getBaseContext(), "Thanks for the Debit", Toast.LENGTH_LONG).show();
			}//debit
		});
    	builder.setButton(AlertDialog.BUTTON_NEUTRAL, "Cash", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//create change alert box
				AlertDialog builder2 = new AlertDialog.Builder(CheckoutMainActivity.this).create();
				DecimalFormat df = new DecimalFormat("#.##");
		        builder2.setView(input);
		    	builder2.setTitle("Change for: $" + df.format(checkoutTotal));
		    	//onclick for after cash received
		    	builder2.setButton(AlertDialog.BUTTON_POSITIVE, "Change", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						double num=0;
						num = Double.parseDouble(input.getText().toString());
						DecimalFormat df = new DecimalFormat("#.##");
						//Inform user of change required
						Toast.makeText(getBaseContext(), "Change Due: $"+ df.format(num - checkoutTotal), Toast.LENGTH_LONG).show();
					}
				});
		    	builder2.show();
		    	//hide keyboard until use input required
		    	getWindow().setSoftInputMode(
		    		      WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			}//cash
		});
    	builder.show();
        //clear the checkout list and update totals
    	cartItems.clear();
    	updateCart();
    	displayTotal(calcTotal());
    }
    public void adminClick(View v)
    {
    	
    }
    public void cashBack(View v)
    {
    	final EditText input = new EditText(this);
    	input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
    	AlertDialog builder = new AlertDialog.Builder(CheckoutMainActivity.this).create();
        builder.setTitle(R.string.cashb);
        builder.setView(input);
    	//onClick for Cash Back
    	builder.setButton(AlertDialog.BUTTON_POSITIVE, "Okay", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				double num=0;
				num = Double.parseDouble(input.getText().toString());
				cartItems.add(new CartItems("Cash Back", num ,1));
    			updateCart();
    			ListView cList = (ListView)findViewById(R.id.cartList);
    			//set onClick listener for removal of items from the checkout cart
    			cList.setOnItemClickListener(new OnItemClickListener() {
		    		public void onItemClick(AdapterView<?> parent, View v, int position, long id)
		    		{
		    			//remove items and decrease total
		    			exterminateItem(v, position);
		    		}
    			});
			}
		});
    	builder.show();
    
    }
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