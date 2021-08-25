package br.uel.hu.android;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

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



		try {
			Bundle args = getIntent().getExtras();
			test_names = args.getStringArrayList(LabInput.TEST_NAMES);
			Map<String,Boolean> tr = new HashMap<>();

			LinearLayout container = (LinearLayout) findViewById(R.id.results);
			TextView t = (TextView) findViewById(R.id.results_text);


			for(String tn : test_names)
				tr.put(tn,(Bacteria.SINAIS.charAt(Bacteria.IMPRECISO) == args.getChar(tn)) ? null : (args.getChar(tn) == Bacteria.SINAIS.charAt(Bacteria.POSITIVO)));
			List<Bacteria> table = LabInput.testsData.populate(null);

			t.setText("Calculando...");

			t.setText(results(container,table,tr) + " resultados encontrados");

		} catch (NullPointerException n) {

			back(null);
		}



	}

	private List<String> test_names;

	public void back (View v) {
		startActivity(new Intent(LabOutput.this,LabInput.class));
	}

	public int results (int count, LinearLayout ll, List<Bacteria> bact, Map<String, Boolean> tests) {
		tests = new HashMap<String, Boolean>(tests);
		for(Map.Entry<String, Boolean> p: tests.entrySet())
			if(p.getValue() == null)
			{
				tests.put(p.getKey(),false);
				count = results(count,ll,bact,tests);

				tests.put(p.getKey(),true);
				count = results(count,ll,bact,tests);

				tests.remove(p.getKey());
				System.out.print('\n');
				return results(count,ll,bact,tests);
			} else System.out.print("\t" + p.getKey() + "\t" + p.getValue());
		System.out.print('\n');
		bact = results(bact,tests);


		for(Bacteria b : bact)
			System.out.println(Bacteria.probability(b.getResult(),3) + "\t" + b.getName());
		return count + 1;
	}
	public List<Bacteria> results (List<Bacteria> probabilities, Map<String,Boolean> tests) {
		ArrayList<Bacteria> possibilities = new ArrayList<>();
		for(Bacteria b : probabilities)
			possibilities.add(b.test(tests));
		Collections.sort(possibilities,new Bacteria.Sort());
		return possibilities;
	}
	public int results (LinearLayout ll, List<Bacteria> bact, Map<String,Boolean> tests) {
		return results(0, ll,bact,tests);
	}
}