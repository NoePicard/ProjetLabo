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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import be.unamur.projetLabo.ProjetLabo;
import be.unamur.projetLabo.R;
import be.unamur.projetLabo.classes.Voiture;
import be.unamur.projetLabo.classes.VoitureLoue;
import be.unamur.projetLabo.fragment.DatePickerFragment;
import be.unamur.projetLabo.request.OkHttpStack;
import be.unamur.projetLabo.request.PostRequest;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VoitureActivity extends AppCompatActivity {

    private Voiture voiture;
    private TextView lblDescriptionVoiture;
    private long start;
    private long end;

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
        start = intent.getLongExtra("Debut",0);
        end = intent.getLongExtra("Fin",0);

        ImageView voiturePhoto = (ImageView) findViewById(R.id.iv_voiture_photo);

        String url = ProjetLabo.BASE_URL + voiture.getPath();
        Picasso.with(this).load(url).into(voiturePhoto);

        VoitureActivity.this.setTitle("Votre selection : " + voiture.getName());
        lblDescriptionVoiture.setText("Ce véhicule comporte " + voiture.getNbSeat() + " sièges, muni de " + voiture.getNbDoor() + "  portes il vous conduira où vous le souhaitez.");
    }

    @OnClick(R.id.btn_louer)
    public void onClickBtnLouer(View v){
        String URL = ProjetLabo.API_BASE_URL + "/louers.json"; //url de l'api

        Map<String, String> params = new HashMap<String, String>();
        params.put("idUser", Integer.toString(ProjetLabo.user.getId()));
        params.put("idVoiture", Integer.toString(voiture.getId()));
        params.put("start", Long.toString(start));
        params.put("end", Long.toString(end));

        PostRequest request = new PostRequest(URL, params, new Response.Listener<String>() {

            @Override
            public void onResponse(String responseData) {
                try {
                    JSONObject louerJSON = new JSONObject(responseData);
                    if (louerJSON.has("response")){
                        if (louerJSON.getBoolean("response")){
                            Calendar calStart = new GregorianCalendar();
                            Calendar calEnd = new GregorianCalendar();
                            calStart.setTimeInMillis(start);
                            calEnd.setTimeInMillis(start);

                            VoitureLoue voit = new VoitureLoue(voiture, louerJSON.getInt("idLocation"), calStart, calEnd);
                            ProjetLabo.user.setVoiture(voit);

                            //Lancer l'actitité profile et fermet toutes les autres activité.
                            // Cela évite de revenir avec le btn retour dans la liste des véhicules à loué
                            Intent intent = new Intent(VoitureActivity.this, ProfileActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            VoitureActivity.this.finish();
                        } else {
                            Toast.makeText(VoitureActivity.this, "Une erreur est survenue veuillez réessayer", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(VoitureActivity.this, "Une erreur est survenue veuillez réessayer", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(VoitureActivity.this, "Une erreur est survenue veuillez réessayer", Toast.LENGTH_LONG).show();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(VoitureActivity.this, "Une erreur réseau est survenue veuillez réessayer", Toast.LENGTH_LONG).show();
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(VoitureActivity.this, new OkHttpStack());
        queue.add(request);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            case R.id.action_profile:
                startActivity(new Intent(VoitureActivity.this, ProfileActivity.class));
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

}
