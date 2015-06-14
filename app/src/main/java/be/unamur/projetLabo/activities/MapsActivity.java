package be.unamur.projetLabo.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import be.unamur.projetLabo.ProjetLabo;
import be.unamur.projetLabo.R;
import be.unamur.projetLabo.classes.Utilisateur;
import be.unamur.projetLabo.exception.UserConnectionException;
import be.unamur.projetLabo.request.OkHttpStack;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapsActivity extends BaseActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.inject(this);
        setUpMapIfNeeded();

        ActionBar actionBar = MapsActivity.this.getSupportActionBar();
        try{
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }catch (Exception e){
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }


    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        final MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(50.4658513, 4.8578139));
        markerOptions.describeContents();
        //markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.car_on_map));
        markerOptions.title("Parking");

        String URL = ProjetLabo.API_BASE_URL + "/place/free.json";
        StringRequest request = new StringRequest(URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try{
                    JSONObject userJSON = new JSONObject(s);
                    if (userJSON.has("place")) {
                        markerOptions.snippet("Parkez-vous à l'emplacement '"+ userJSON.getString("place") +"' ");
                        markerOptions.visible(true);
                        mMap.addMarker(markerOptions);
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(50.4665398, 4.8578139), 10));
                        ProjetLabo.user.getVoiture().setParking(userJSON.getString("place"));
                    } else {
                        Toast.makeText(MapsActivity.this, "Une erreur réseau est survenue !", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(MapsActivity.this, "Une erreur réseau est survenue !", Toast.LENGTH_LONG).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        hideProgressBar();
                        Toast.makeText(MapsActivity.this, "Une erreur réseau est survenue !", Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(MapsActivity.this, new OkHttpStack());
        queue.add(request);

    }

    private boolean rendreVerifPlain() {
        if (ProjetLabo.user.getVoiture().getFuelQuantity() < 25) {
            new AlertDialog.Builder(MapsActivity.this)
                    .setTitle("Faites le plein !")
                    .setMessage("Vous devez rendre un véhicule contenant au minimum 25 litres de carburant.")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            MapsActivity.this.finish();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return false;
        }
        return true;
    }

    private boolean verifEtatVoiture(){
        if (!ProjetLabo.user.getVoiture().isKeyInEtui() || ProjetLabo.user.getVoiture().isOpenEtui() || ProjetLabo.user.getVoiture().isOpenDoor()) {
            new AlertDialog.Builder(MapsActivity.this)
                    .setTitle("Verifiez votre voiture !")
                    .setMessage("Votre voiture doit contenir les clés, l'étui ainsi que la porte doivent être fermés.")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            MapsActivity.this.finish();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return false;
        }
        return true;
    }

    @OnClick(R.id.btnCarDropped)
    public void onClickBtnCarDropped(View v){
        new AlertDialog.Builder(this)
                .setTitle("Confirmez")
                .setMessage("La voiture " + ProjetLabo.user.getVoiture().getName() +" a-t-elle bien été stationnée à l'emplacement demandé ?")
                .setPositiveButton("Je confirme", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (MapsActivity.this.rendreVerifPlain() && MapsActivity.this.verifEtatVoiture()) {
                            ProjetLabo.user.getVoiture().setRendu(true);
                            ProjetLabo.user.getVoiture().setToApi(MapsActivity.this);
                            ProjetLabo.user.setVoiture(null);
                            ProjetLabo.user.setToApi(MapsActivity.this);
                            MapsActivity.this.finish();
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        MapsActivity.this.finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
