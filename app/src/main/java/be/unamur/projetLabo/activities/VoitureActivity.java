package be.unamur.projetLabo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import be.unamur.projetLabo.ProjetLabo;
import be.unamur.projetLabo.R;
import be.unamur.projetLabo.classes.Voiture;

public class VoitureActivity extends AppCompatActivity {

    private Voiture voiture;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voiture);

        ActionBar actionBar = VoitureActivity.this.getSupportActionBar();
        try{
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }catch (Exception e){
            Log.v("bwb", e.toString());
        }

        Intent intent = getIntent();
        voiture = (Voiture) intent.getSerializableExtra("voiture");

        ImageView voiturePhoto = (ImageView) findViewById(R.id.iv_voiture_photo);

        String url = ProjetLabo.BASE_URL + voiture.getPath();
        Picasso.with(this).load(url).into(voiturePhoto);

        VoitureActivity.this.setTitle("Votre selection : " + voiture.getName());
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
