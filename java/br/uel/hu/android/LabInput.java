
package br.uel.hu.android;

import android.content.Context;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.HashMap;
import java.util.Map;

import br.uel.hu.lab.Test;
import br.uel.hu.lab.Eukaryotes;


public class LabInput extends AppCompatActivity {


	public static final String[] respostas = {"ignorar", "NEGATIVO (-)", "POSITIVO (+)", "Incerto (+/-)"};

	public Map<String,Spinner> sp;

	public Spinner createDropDown () {
		Spinner dropdown = new Spinner(this);
		ArrayAdapter<String> ad = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, respostas);
		ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dropdown.setAdapter(ad);
		return dropdown;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Eukaryotes testsData = new Test();
		this.sp = new HashMap<>();


		View view = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_scrolling, null);

		Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.toolbar_layout);
		toolBarLayout.setTitle(getTitle());


		LinearLayout l;
		TextView label;
		Spinner dpdn;
		String[] tests = testsData.tests();
		for(int c = 0; c < tests.length; c++)
		{
			dpdn = createDropDown();
			label = new TextView(this);
			l = new LinearLayout(this);
			l.addView(label);
			l.addView(dpdn);
			((LinearLayout)view.findViewById(R.id.content_content)).addView(l);
			sp.put(tests[c], dpdn);
			label.setText(tests[c]);
		}







		setContentView(view);

		((TextView)view.findViewById(R.id.content_text)).setText("");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_scrolling, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		//noinspection SimplifiableIfStatement
		return item.getItemId() == R.id.action_settings || super.onOptionsItemSelected(item);
	}
}