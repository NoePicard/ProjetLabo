package be.unamur.projetLabo.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import be.unamur.projetLabo.R;
import butterknife.ButterKnife;

public class VoituresActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voitures);
        ButterKnife.inject(this);



    }

}
