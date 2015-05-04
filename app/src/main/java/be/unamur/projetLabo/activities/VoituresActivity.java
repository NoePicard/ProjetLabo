package be.unamur.projetLabo.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import be.unamur.projetLabo.R;
import be.unamur.projetLabo.adapters.RVAdapter;
import be.unamur.projetLabo.classes.Voitures;
import butterknife.ButterKnife;

public class VoituresActivity extends AppCompatActivity {

    private List<Voitures> voit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voitures);
        ButterKnife.inject(this);

        boolean b = true;

        voit.add(new Voitures(1, "abc", 2, 2, "a", b));
        voit.add(new Voitures(1, "def", 2 ,2, "a", b));
        voit.add(new Voitures(1, "ghi", 2, 2, "a", b));

        RecyclerView rv = (RecyclerView)findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(VoituresActivity.this);
        rv.setLayoutManager(llm);

        RVAdapter adapter = new RVAdapter(voit);
        rv.setAdapter(adapter);



    }

}
