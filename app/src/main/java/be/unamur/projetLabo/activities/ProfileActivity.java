package be.unamur.projetLabo.activities;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.GregorianCalendar;

import be.unamur.projetLabo.ProjetLabo;
import be.unamur.projetLabo.R;
import be.unamur.projetLabo.classes.Utilisateur;
import be.unamur.projetLabo.classes.VoitureLoue;
import be.unamur.projetLabo.fragment.DatePickerFragment;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileActivity extends AppCompatActivity implements DatePickerFragment.OnDatePickerSetListener  {
    private CardView cvVoitureLoue;
    private Button btnLouer;
    private ImageView voiturePhoto;
    private TextView voitureName;
    private TextView lblLogin;
    private Button btnFidele;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.inject(this);


        cvVoitureLoue = (CardView)findViewById(R.id.cvVoitureLoue);
        btnLouer = (Button) findViewById(R.id.btnLouer);
        btnFidele = (Button) findViewById(R.id.btnFidele);
        voiturePhoto = (ImageView) findViewById(R.id.voiture_photo);
        voitureName = (TextView) findViewById(R.id.voiture_name);
        lblLogin = (TextView) findViewById(R.id.lblLogin);

        lblLogin.setText(ProjetLabo.user.getLogin());

        VoitureLoue voiture = ProjetLabo.user.getVoiture();
        if(voiture != null){
            //On affiche les infos sur la voiture
            cvVoitureLoue.setVisibility(View.VISIBLE);
            btnLouer.setVisibility(View.GONE);
            voitureName.setText(voiture.getName());
            String url = ProjetLabo.BASE_URL + voiture.getPath();
            Picasso.with(ProfileActivity.this).load(url).into(voiturePhoto);
        }else{
            //On cache la carte et on affiche le btn "Louer"
            cvVoitureLoue.setVisibility(View.GONE);
            btnLouer.setVisibility(View.VISIBLE);
        }

        if(ProjetLabo.user.isFidele()){
            btnFidele.setText("Mon compte fidélisation");
        }else{
            btnFidele.setText("Devenir fidéle");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }
    @OnClick(R.id.btnLouer)
    public void onClickBtnLouer(View view) {
        startActivity(new Intent(ProfileActivity.this, CriteresActivity.class));
    }
    @OnClick(R.id.cvVoitureLoue)
    public void onClickCvVoiture(View view) {
        startActivity(new Intent(ProfileActivity.this, InfoVehiculeActivity.class));
    }
    @OnClick(R.id.btnFidele)
    public void onClickBtnFidele(View view) {
        startActivity(new Intent(ProfileActivity.this, SoldeFidelisationAcitivity.class));
    }
    @OnClick(R.id.btnRendre)
    public void onClickBtnRendre(View view) {

    }
    @OnClick(R.id.btnPlain)
    public void onClickBtnPlain(View view) {

    }
    @OnClick(R.id.btnProlonger)
    public void onClickBtnProlonger(View view) {
        DialogFragment dateFragment = new DatePickerFragment(ProjetLabo.user.getVoiture().getEnd());
        dateFragment.show(getSupportFragmentManager(), "timePicker");
    }

    @Override
    public void onDatePickerSet(int year, int monthOfYear, int dayOfMonth) {
        Calendar date = new GregorianCalendar();
        date.set(year, monthOfYear, dayOfMonth);
        long prolongation = date.getTime().getTime();
        if(prolongation > ProjetLabo.user.getVoiture().getEnd().getTime().getTime()){

        }else{
            Toast.makeText(ProfileActivity.this, "Votre nouvelle date de fin de location doit être suppérieur a l'ancienne.", Toast.LENGTH_LONG).show();
        }
    }

    }
