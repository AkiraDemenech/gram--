package br.uel.hu.android;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ListView;
import java.util.ArrayList;
import android.widget.ListAdapter;
import android.widget.ArrayAdapter;

import android.widget.LinearLayout;

import br.uel.hu.lab.Bacteria;


public class LabOutput extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
		toolBarLayout.setTitle(getTitle());

		LinearLayout container = (LinearLayout) findViewById(R.id.results);
		TextView t = (TextView) findViewById(R.id.results_text);
		t.setText("Calculando...");

		try {
			int r = results(container,getIntent().getExtras());
			if(r != 1)
				t.setText(r + " resultados possíveis encontrados");
			else t.setText("Resultado:");

			t = new TextView(this);
			t.setText("Fim dos resultados :" + ((r > 1)?(')'):(Bacteria.SINAIS.charAt(Bacteria.IMPRECISO))));
			container.addView(t);

		} catch (NullPointerException n) {
			t.setText("Erro interno: não foi possível concluir a operação.");
		//	back(null);
		}



	}

	private List<String> test_names;

	public void back (View v) {
		startActivity(new Intent(LabOutput.this,LabInput.class));
	}

	public int results (int count, LinearLayout ll, List<Bacteria> bact, Map<String, Boolean> tests) {
		if(tests.size() == 0) // se não houver nada no mapeamento, nem tenta começar
			return count;
		tests = new HashMap<String, Boolean>(tests);
		for(Map.Entry<String, Boolean> p: tests.entrySet())
			if(p.getValue() == null)
			{
				tests.put(p.getKey(),false);
				count = results(count,ll,bact,tests);

				tests.put(p.getKey(),true);
				count = results(count,ll,bact,tests);

				tests.remove(p.getKey());
			//	System.out.print('\n');
				return results(count,ll,bact,tests);
			} //else System.out.print("\t" + p.getKey() + "\t" + p.getValue());
	//	System.out.print('\n');
		bact = Bacteria.results(bact,tests);

		ListAdapter ad = new ArrayAdapter<Bacteria>(this,android.R.layout.simple_list_item_1,bact);

		ListView li = new ListView(this);
		li.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
		li.setAdapter(ad);
		ll.addView(li);

		return count + 1;
	}

	protected int results (LinearLayout ll, Bundle args) {
		Map<String,Boolean> tr = new HashMap<>();
		test_names = args.getStringArrayList(LabInput.TEST_NAMES);
		List<Bacteria> bact = LabInput.testsData.populate(null);
		for(String tn : test_names)
			tr.put(tn,(Bacteria.SINAIS.charAt(Bacteria.IMPRECISO) == args.getChar(tn)) ? null : (args.getChar(tn) == Bacteria.SINAIS.charAt(Bacteria.POSITIVO)));
		return results(0, ll,bact,tr);
	}
}