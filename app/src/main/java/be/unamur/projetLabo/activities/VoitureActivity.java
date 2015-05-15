package be.unamur.projetLabo.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.Date;

import be.unamur.projetLabo.ProjetLabo;
import be.unamur.projetLabo.R;
import be.unamur.projetLabo.classes.Voiture;
import be.unamur.projetLabo.fragment.DatePickerFragment;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VoitureActivity extends AppCompatActivity implements DatePickerFragment.OnDatePickerSetListener {

    private Voiture voiture;
    private TextView lblDescriptionVoiture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voiture);
        ButterKnife.inject(this);

        ActionBar actionBar = VoitureActivity.this.getSupportActionBar();
        try{
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }catch (Exception e){
            Log.v("bwb", e.toString());
        }

        lblDescriptionVoiture = (TextView) findViewById(R.id.lbl_Description_Vehicule);

        Intent intent = getIntent();
        voiture = (Voiture) intent.getSerializableExtra("voiture");

        ImageView voiturePhoto = (ImageView) findViewById(R.id.iv_voiture_photo);

        String url = ProjetLabo.BASE_URL + voiture.getPath();
        Picasso.with(this).load(url).into(voiturePhoto);

        VoitureActivity.this.setTitle("Votre selection : " + voiture.getName());
        lblDescriptionVoiture.setText("Ce véhicule comporte "+ voiture.getNbSeat() +" sièges, muni de "+ voiture.getNbDoor() +"  portes il vous conduira où vous le souhaitez.");
    }

    @OnClick(R.id.btn_louer)
    public void onClickBtnLouer(View v){
        DialogFragment dateFragment = new DatePickerFragment();
        dateFragment.show(getSupportFragmentManager(), "timePicker");
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            case R.id.action_profile:
                //Lancer activité profil
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_default, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onDatePickerSet(int year, int monthOfYear, int dayOfMonth) {
        //Action après sélection de la date.
    }
}
