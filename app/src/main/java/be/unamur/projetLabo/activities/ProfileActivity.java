package be.unamur.projetLabo.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import be.unamur.projetLabo.ProjetLabo;
import be.unamur.projetLabo.R;
import be.unamur.projetLabo.classes.Utilisateur;
import be.unamur.projetLabo.classes.VoitureLoue;
import be.unamur.projetLabo.fragment.DatePickerFragment;
import be.unamur.projetLabo.request.OkHttpStack;
import be.unamur.projetLabo.request.PostRequest;
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
        /*startActivity(new Intent(ProfileActivity.this, InfoVehiculeActivity.class));*/
        Toast.makeText(ProfileActivity.this, "Activité non disponible pour le moment", Toast.LENGTH_LONG).show();
    }
    @OnClick(R.id.btnFidele)
    public void onClickBtnFidele(View view) {
        /*startActivity(new Intent(ProfileActivity.this, SoldeFidelisationAcitivity.class));*/
        Toast.makeText(ProfileActivity.this, "Activité non disponible pour le moment", Toast.LENGTH_LONG).show();
    }

    private void rendreVoiture(){
        Map<String, String> params = new HashMap<String, String>();
        params.put("idLocation", Integer.toString(ProjetLabo.user.getVoiture().getIdLocation()));

        String URL = ProjetLabo.API_BASE_URL + "/rendres/voitures.json";

        PostRequest requestAddUser = new PostRequest(URL, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject userJSON = new JSONObject(s);
                    if (userJSON.has("response")) {
                        if(userJSON.getBoolean("response")) {
                            ProjetLabo.user.setVoiture(null);
                            ProfileActivity.this.recreate();
                        }else{
                            Toast.makeText(ProfileActivity.this, "Impossible de rendre votre véhicule pour le moment", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(ProfileActivity.this, "Impossible de rendre votre véhicule pour le moment", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(ProfileActivity.this, "Impossible de rendre votre véhicule pour le moment", Toast.LENGTH_LONG).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(ProfileActivity.this, "Une erreur réseau est survenue !", Toast.LENGTH_LONG).show();
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(ProfileActivity.this, new OkHttpStack());
        queue.add(requestAddUser);

    }

    @OnClick(R.id.btnRendre)
    public void onClickBtnRendre(View view) {
        Calendar date = Calendar.getInstance();
        if(date.compareTo(ProjetLabo.user.getVoiture().getEnd()) < 0){
            new AlertDialog.Builder(this)
                    .setTitle("Rendre ce véhicule")
                    .setMessage("Êtes-vous sur de vouloir rendre ce véhicule avant la date prévue ?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            rendreVoiture();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }else{
            rendreVoiture();
        }
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
        final Calendar calProlongation = new GregorianCalendar();
        calProlongation.set(year, monthOfYear, dayOfMonth);
        long prolongation = calProlongation.getTime().getTime();
        if(prolongation > ProjetLabo.user.getVoiture().getEnd().getTime().getTime()){
            Map<String, String> params = new HashMap<String, String>();
            params.put("idLocation", Integer.toString(ProjetLabo.user.getVoiture().getIdLocation()));
            params.put("end", Long.toString(prolongation));

            String URL = ProjetLabo.API_BASE_URL + "/prolongers.json";

            PostRequest requestAddUser = new PostRequest(URL, params, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    try {
                        JSONObject userJSON = new JSONObject(s);
                        if (userJSON.has("response")) {
                            if(userJSON.getBoolean("response")) {
                                ProjetLabo.user.getVoiture().setEnd(calProlongation);
                            }else{
                                Toast.makeText(ProfileActivity.this, "Ce véhicule ne peut pas être prolongé jusqu'a la date voulue !", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(ProfileActivity.this, "Impossible de prolonger votre véhicule pour le moment", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(ProfileActivity.this, "Impossible de prolonger votre véhicule pour le moment", Toast.LENGTH_LONG).show();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(ProfileActivity.this, "Une erreur réseau est survenue !", Toast.LENGTH_LONG).show();
                        }
                    });
            RequestQueue queue = Volley.newRequestQueue(ProfileActivity.this, new OkHttpStack());
            queue.add(requestAddUser);
        }else{
            Toast.makeText(ProfileActivity.this, "Votre nouvelle date de fin de location doit être suppérieur a l'ancienne.", Toast.LENGTH_LONG).show();
        }
    }

    }
