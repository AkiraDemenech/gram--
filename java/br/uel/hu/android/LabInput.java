
package br.uel.hu.android;


import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import br.uel.hu.lab.Test;
import br.uel.hu.lab.Eukaryotes;
import br.uel.hu.lab.Bacteria;


public class LabInput extends AppCompatActivity {



	public static final String TEST_NAMES = "\ttests";
	public static final Eukaryotes testsData = new Test();
	private Map<String,Spinner> sp;

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
		this.sp = new HashMap<>();

		setContentView(R.layout.activity_scrolling);


		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
		toolBarLayout.setTitle(getTitle());


		LinearLayout l, container = (LinearLayout) findViewById(R.id.content_content);
		TextView label;
		Spinner dpdn;
		String[] tests = testsData.tests();
		for(int c = 0; c < tests.length; c++)
		{
			dpdn = createDropDown();
		//	dpdn.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
			label = new TextView(this);
			l = new LinearLayout(this);
			l.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));
			l.setOrientation(LinearLayout.HORIZONTAL);
			l.addView(label);
			l.addView(dpdn);


			container.addView(l);



			sp.put(tests[c], dpdn);
			label.setText('\t' + tests[c] + '\t');
		}
		Button b = (Button) findViewById(R.id.button);
		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(LabInput.this, LabOutput.class);
				Bundle b = new Bundle();
				ArrayList<String> t = new ArrayList<>();
				for(Map.Entry<String,Spinner> e: sp.entrySet())
					if(!e.getValue().getSelectedItem().toString().equals(Bacteria.RESPOSTAS[Bacteria.IGNORAR]))
						for(int c = Bacteria.CONSIDERAR_A_PARTIR_DE; c < Bacteria.RESPOSTAS.length; c++)
							if(e.getValue().getSelectedItem().toString().equals(Bacteria.RESPOSTAS[c]))
							{
								b.putChar(e.getKey(), Bacteria.SINAIS.charAt(c));
								t.add(e.getKey());
								System.out.println(e.getKey() + '\t' + e.getValue().getSelectedItem());
							}
				b.putStringArrayList(TEST_NAMES,t);
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