package be.unamur.projetLabo.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import be.unamur.projetLabo.R;
import be.unamur.projetLabo.classes.Voiture;
import be.unamur.projetLabo.adapters.RVAdapter;
import be.unamur.projetLabo.classes.Voiture;
import butterknife.ButterKnife;

public class VoituresActivity extends AppCompatActivity {

    Voiture[] voitures;

    private List<Voiture> voit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voitures);
        ButterKnife.inject(this);

        Intent intent = getIntent();
        voitures = (Voiture[]) intent.getSerializableExtra("voitures");


        boolean b = true;
        voit = new ArrayList<>();
        int i;
        for(i=0; i<voitures.length; i++){
            voit.add(voitures[i]);
        }

        RecyclerView rv = (RecyclerView)findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(VoituresActivity.this);
        rv.setLayoutManager(llm);

        RVAdapter adapter = new RVAdapter(voit);
        rv.setAdapter(adapter);

    }

}
