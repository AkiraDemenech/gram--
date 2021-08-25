package br.uel.hu.android;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LabOutput extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
		toolBarLayout.setTitle(getTitle());

		TextView t = new TextView(this);
		t.setText("Adicionado");
		((LinearLayout)findViewById(R.id.results)).addView(t);


	}

	public void back (View v) {
		startActivity(new Intent(LabOutput.this,LabInput.class));
	}
}