package be.unamur.projetLabo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import be.unamur.projetLabo.R;
import be.unamur.projetLabo.classes.Voiture;
import be.unamur.projetLabo.adapters.VoitureAdapter;
import butterknife.ButterKnife;

public class ListeVoituresActivity extends AppCompatActivity {

    Voiture[] voitures;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_voitures);
        ButterKnife.inject(this);


        ActionBar actionBar = ListeVoituresActivity.this.getSupportActionBar();
        try{
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }catch (Exception e){
            Log.v("bwb", e.toString());
        }

        Intent intent = getIntent();
        voitures = (Voiture[]) intent.getSerializableExtra("voitures");

        List<Voiture> voitureList;

        voitureList = new ArrayList<>();

        for (Voiture voiture : voitures) {
            voitureList.add(voiture);
        }

        RecyclerView rv = (RecyclerView) findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(ListeVoituresActivity.this);
        rv.setLayoutManager(llm);

        VoitureAdapter adapter = new VoitureAdapter(ListeVoituresActivity.this, voitureList);
        rv.setAdapter(adapter);

        if (voitureList.isEmpty()) {
            Toast.makeText(ListeVoituresActivity.this, "Aucune voiture ne correspond à vos cirtères !", Toast.LENGTH_LONG).show();
            ListeVoituresActivity.this.finish();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
