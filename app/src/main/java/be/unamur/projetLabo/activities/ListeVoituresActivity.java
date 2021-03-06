package be.unamur.projetLabo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import be.unamur.projetLabo.R;
import be.unamur.projetLabo.classes.Voiture;
import be.unamur.projetLabo.adapters.VoitureAdapter;
import butterknife.ButterKnife;

public class ListeVoituresActivity extends BaseActivity {

    private long start;
    private long end;

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
        }

        Intent intent = getIntent();
        start = intent.getLongExtra("Debut",0);
        end = intent.getLongExtra("Fin",0);
        //Passer par une variable Object pour ensuite caster pour éviter bug sur ancienne version
        Object[] tmp = (Object[]) intent.getSerializableExtra("voitures");
        Voiture[] voitures = Arrays.copyOf(tmp, tmp.length, Voiture[].class);

        List<Voiture> voitureList;

        voitureList = new ArrayList<>();

        for (Voiture voiture : voitures) {
            voitureList.add(voiture);
        }

        RecyclerView rv = (RecyclerView) findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(ListeVoituresActivity.this);
        rv.setLayoutManager(llm);

        VoitureAdapter adapter = new VoitureAdapter(ListeVoituresActivity.this, voitureList, start, end);
        rv.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_default, menu);
        return true;
    }



}
