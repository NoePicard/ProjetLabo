package be.unamur.projetLabo.activities;

import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Map;

import be.unamur.projetLabo.ProjetLabo;
import be.unamur.projetLabo.R;
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
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(50.4658513, 4.8578139));
        markerOptions.describeContents();
        //markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.car_on_map));
        markerOptions.title("Parking");
        markerOptions.snippet("Parkez-vous à l'emplacement 'A33' ");
        markerOptions.visible(true);
        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(50.4665398, 4.8578139), 10));
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
