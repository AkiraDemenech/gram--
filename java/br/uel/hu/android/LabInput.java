
package br.uel.hu.android;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.widget.LinearLayout;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.HashMap;
import java.util.Map;

import br.uel.hu.lab.Test;
import br.uel.hu.lab.Eukaryotes;
import br.uel.hu.lab.Bacteria;


public class LabInput extends AppCompatActivity {



	public Map<String,Spinner> sp;

	public Spinner createDropDown (String[] opt) {
		Spinner dropdown = new Spinner(this);
		ArrayAdapter<String> ad = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opt);
		ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dropdown.setAdapter(ad);
		return dropdown;
	}
	public Spinner createDropDown () {
		return createDropDown(Bacteria.RESPOSTAS);
	}

	public void clearInputs (View v) {
		for(String s: sp.keySet())
			sp.get(s).setSelection(0);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Eukaryotes testsData = new Test();
		this.sp = new HashMap<>();



		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
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
			((LinearLayout) findViewById(R.id.content_content)).addView(l);
			l.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT,0));
			l.setOrientation(LinearLayout.HORIZONTAL);
			l.addView(label);
			l.addView(dpdn);





			sp.put(tests[c], dpdn);
			label.setText('\t' + tests[c] + '\t');
		}
		setContentView(R.layout.activity_scrolling);
		Button b = (Button) findViewById(R.id.button);
		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(LabInput.this, LabOutput.class);
				Bundle b = new Bundle();
				for(Map.Entry<String,Spinner> e: sp.entrySet())
					if(!e.getValue().getSelectedItem().toString().equals(Bacteria.RESPOSTAS[Bacteria.IGNORAR]))
						for(int c = Bacteria.CONSIDERAR_A_PARTIR_DE; c < Bacteria.RESPOSTAS.length; c++)
							if(e.getValue().getSelectedItem().toString().equals(Bacteria.RESPOSTAS[c]))
							{
								b.putChar(e.getKey(), Bacteria.SINAIS.charAt(c));
								System.out.println(e.getKey() + '\t' + e.getValue().getSelectedItem());
							}
					/*	if(e.getValue().getSelectedItem().toString().equals(respostas[POSITIVO]))
							b.putInt(e.getKey(), 1);
						else if(e.getValue().getSelectedItem().toString().equals(respostas[NEGATIVO]))
							b.putInt(e.getKey(),-1);
						else b.putInt(e.getKey(),0); */
				i.putExtras(b);
				startActivity(i);
			}
		});
		b.setText("Testar!");
		((TextView) findViewById(R.id.content_text)).setText("Selecione os resultados:");



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