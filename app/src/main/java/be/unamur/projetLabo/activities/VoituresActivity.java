package be.unamur.projetLabo.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import be.unamur.projetLabo.R;
import be.unamur.projetLabo.classes.Voiture;
import be.unamur.projetLabo.adapters.VoitureAdapter;
import be.unamur.projetLabo.classes.Voiture;
import butterknife.ButterKnife;

public class VoituresActivity extends AppCompatActivity {

    Voiture[] voitures;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voitures);
        ButterKnife.inject(this);

        Intent intent = getIntent();
        voitures = (Voiture[]) intent.getSerializableExtra("voitures");

        List<Voiture> voitureList;

        voitureList = new ArrayList<>();

        for (Voiture voiture : voitures) {
            voitureList.add(voiture);
        }

        RecyclerView rv = (RecyclerView) findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(VoituresActivity.this);
        rv.setLayoutManager(llm);

        VoitureAdapter adapter = new VoitureAdapter(this, voitureList);
        rv.setAdapter(adapter);

        if (voitureList.isEmpty()) {
            Toast.makeText(VoituresActivity.this, "Aucune voiture ne correspond à vos cirtères !", Toast.LENGTH_LONG).show();
            VoituresActivity.this.finish();
        }
    }
}
