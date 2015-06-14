package be.unamur.projetLabo.activities;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.Image;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import com.cardiomood.android.controls.gauge.SpeedometerGauge;

import com.cardiomood.android.controls.gauge.SpeedometerGauge;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

import be.unamur.projetLabo.ProjetLabo;
import be.unamur.projetLabo.R;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InfoVehiculeActivity extends BaseActivity {

    //public var for layout.
    public ImageView voiturePhoto;
    public ImageView iv_GPS;
    public TextView lbl_car_model;
    public ImageButton ib_car_doors;
    public ImageButton ib_car_key;
    public ImageButton ib_bluetooth_activated;
    public ImageButton ib_car_case;
    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter(); //SETTING BT ADAPTER
    public SpeedometerGauge sv_fuel_gauge;
    private SpeedometerGauge speedometer;

    //other var
    private final static int REQUEST_CODE_ENABLE_BLUETOOTH = 0;
    private float fuelQTT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_vehicule);
        ButterKnife.inject(this);

        ActionBar actionBar = InfoVehiculeActivity.this.getSupportActionBar();
        try{
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }catch (Exception e){
        }


        /*
            LINKING VAR TO LAYOUT
         */
        voiturePhoto = (ImageView) findViewById(R.id.iv_voiture_photo);
        lbl_car_model = (TextView) findViewById(R.id.car_model);
        ib_bluetooth_activated = (ImageButton) findViewById(R.id.bluetooth_activated);
        ib_car_case = (ImageButton) findViewById(R.id.car_case);
        ib_car_doors = (ImageButton) findViewById(R.id.car_doors);
        ib_car_key = (ImageButton) findViewById(R.id.car_key);
        sv_fuel_gauge = (SpeedometerGauge) findViewById(R.id.fuel_gauge);
        iv_GPS = (ImageView) findViewById(R.id.ivCarGPS);

        String url = ProjetLabo.BASE_URL + ProjetLabo.user.getVoiture().getPath();
        Picasso.with(this).load(url).into(voiturePhoto);

        /*
            SETTING CAR MODEL
        */
        String car_model = ProjetLabo.user.getVoiture().getName();
        lbl_car_model.setText(car_model);
        /*
                    SET fuel_gauge TO VISIBLE.

                */
        // Add label converter
        sv_fuel_gauge.setLabelConverter(new com.cardiomood.android.controls.gauge.SpeedometerGauge.LabelConverter() {
            @Override
            public String getLabelFor(double progress, double maxProgress) {
                return String.valueOf((int) Math.round(progress));
            }
        });


        // configure value range and ticks
        sv_fuel_gauge.setMaxSpeed(50);
        sv_fuel_gauge.setMajorTickStep(5);
        sv_fuel_gauge.setMinorTicks(4);


        // Configure value range colors
        sv_fuel_gauge.addColoredRange(0, 10, Color.RED);
        sv_fuel_gauge.addColoredRange(10, 25, Color.YELLOW);
        sv_fuel_gauge.addColoredRange(25, 50, Color.GREEN);
        fuelQTT = ProjetLabo.user.getVoiture().getFuelQuantity();
        //FuelGauge needle is gonna be set here after

       /*
            SETTING BUTTON VIEWS DEFAULT
        */

        //verifying is Bluetooth is supported
        if (bluetoothAdapter == null) {
            Toast.makeText(InfoVehiculeActivity.this, "La fonction Bluetooth n'est pas supportée", Toast.LENGTH_SHORT).show();
            InfoVehiculeActivity.this.finish();
        }
        else {
            // settings if bluetooth isn't enabled
            if (!bluetoothAdapter.isEnabled()){
                ib_bluetooth_activated.setBackground(getResources().getDrawable(R.drawable.blueetooth_red));
                // setting every img button to "gone" (unclickable)
                ib_car_doors.setBackground(getResources().getDrawable(R.drawable.car_door));
                        ib_car_doors.getBackground().setAlpha(128);
                ib_car_case.setBackground(getResources().getDrawable(R.drawable.car_case));
                        ib_car_case.getBackground().setAlpha(128);
                ib_car_key.setBackground(getResources().getDrawable(R.drawable.car_key));
                        ib_car_key.getBackground().setAlpha(128);
                sv_fuel_gauge.setSpeed(0, false);

            }
            else {
                ib_bluetooth_activated.setBackground(getResources().getDrawable(R.drawable.blueetooth_green));
                //configure FUEL GAUGE needle
                sv_fuel_gauge.setSpeed((int)fuelQTT, false);
                if (ProjetLabo.user.getVoiture().isOpenDoor()) {
                    ib_car_doors.setBackground(getResources().getDrawable(R.drawable.car_door_red));
                    // setting img button to "gone" (unclickable)
                    ib_car_case.setBackground(getResources().getDrawable(R.drawable.car_case));
                    ib_car_case.getBackground().setAlpha(128);
                    ib_car_key.setBackground(getResources().getDrawable(R.drawable.car_key));
                    ib_car_key.getBackground().setAlpha(128);

                }
                else {
                    ib_car_doors.setBackground(getResources().getDrawable(R.drawable.car_door_green));
                    // check if car_case is opened. If well, access to key in case unlocked
                    if (ProjetLabo.user.getVoiture().isOpenEtui()){
                        ib_car_case.setBackground(getResources().getDrawable(R.drawable.car_case_red));
                        //set car_doors to unclickable
                        ib_car_doors.setBackground(getResources().getDrawable(R.drawable.car_door));
                        ib_car_doors.getBackground().setAlpha(128);
                        // check if key is inside the case
                        if (ProjetLabo.user.getVoiture().isKeyInEtui())
                            ib_car_key.setBackground(getResources().getDrawable(R.drawable.car_key_green));
                        else
                            ib_car_key.setBackground(getResources().getDrawable(R.drawable.car_key_red));
                    }
                    else {
                        ib_car_case.setBackground(getResources().getDrawable(R.drawable.car_case_green));
                        // setting img button to unclickable
                        ib_car_key.setBackground(getResources().getDrawable(R.drawable.car_key));
                        ib_car_key.getBackground().setAlpha(128);
                    }

                }
            }
        }



        //GPS
        if(ProjetLabo.user.getVoiture().isGps()){
            iv_GPS.setVisibility(View.VISIBLE);
        }else{
            iv_GPS.setVisibility(View.GONE);
        }
    }



    @OnClick(R.id.bluetooth_activated)
    public void OnBluetoothButton (){
        if (!bluetoothAdapter.isEnabled()){
            Intent enableBlueTooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBlueTooth, REQUEST_CODE_ENABLE_BLUETOOTH);
            // SETTINGS ARE IN "onActivityResult" THAT CATCHES IF AND WHAT THE USER ANSWERS TO POPUP.
        }
        else {
            bluetoothAdapter.disable();
            ib_bluetooth_activated.setBackground(getResources().getDrawable(R.drawable.blueetooth_red));
            // setting every img button to "gone" (unclickable)
            ib_car_doors.setBackground(getResources().getDrawable(R.drawable.car_door));
            ib_car_doors.getBackground().setAlpha(128);
            ib_car_case.setBackground(getResources().getDrawable(R.drawable.car_case));
            ib_car_case.getBackground().setAlpha(128);
            ib_car_key.setBackground(getResources().getDrawable(R.drawable.car_key));
            ib_car_key.getBackground().setAlpha(128);
            sv_fuel_gauge.setSpeed(0, false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != REQUEST_CODE_ENABLE_BLUETOOTH)
            return;
        // check if the user accepted to enable bluetooth (if well, enter the if)
        if (resultCode == RESULT_OK) {
            ib_bluetooth_activated.setBackground(getResources().getDrawable(R.drawable.blueetooth_green));
            sv_fuel_gauge.setSpeed((int)fuelQTT, false);
            //setting to available or not
            if (ProjetLabo.user.getVoiture().isOpenDoor()) {
                ib_car_doors.getBackground().setAlpha(255);
                ib_car_doors.setBackground(getResources().getDrawable(R.drawable.car_door_red));
                // setting img button to "GONE" (unclickable)
                ib_car_case.setBackground(getResources().getDrawable(R.drawable.car_case));
                ib_car_case.getBackground().setAlpha(128);
                ib_car_key.setBackground(getResources().getDrawable(R.drawable.car_key));
                ib_car_key.getBackground().setAlpha(128);
            }
            else {
                ib_car_doors.setBackground(getResources().getDrawable(R.drawable.car_door_green));
                //setting car_doors and etui to clickable
                ib_car_doors.getBackground().setAlpha(255);
                ib_car_case.getBackground().setAlpha(255);
                // check if car_case is opened. If well, access to key in case unlocked
                if (ProjetLabo.user.getVoiture().isOpenEtui()){
                    ib_car_case.setBackground(getResources().getDrawable(R.drawable.car_case_red));
                    // making car_doors unclickable
                    ib_car_doors.setBackground(getResources().getDrawable(R.drawable.car_door));
                    ib_car_doors.getBackground().setAlpha(128);
                    // check if key is inside the case
                    if (ProjetLabo.user.getVoiture().isKeyInEtui())
                        ib_car_key.setBackground(getResources().getDrawable(R.drawable.car_key_green));
                    else
                        ib_car_key.setBackground(getResources().getDrawable(R.drawable.car_key_red));
                }
                else {
                    ib_car_case.setBackground(getResources().getDrawable(R.drawable.car_case_green));
                    // setting img button to "GONE" (unclickable)
                    ib_car_key.setBackground(getResources().getDrawable(R.drawable.car_key));
                    ib_car_key.getBackground().setAlpha(128);
                    ib_car_doors.setBackground(getResources().getDrawable(R.drawable.car_door_green));
                    ib_car_doors.getBackground().setAlpha(255);
                }

            }

        }
    }

    @OnClick(R.id.car_doors)
    public void OnCarDoorsButton (){
        if (bluetoothAdapter.isEnabled()){
            if ((!ProjetLabo.user.getVoiture().isOpenEtui()) && (!ProjetLabo.user.getVoiture().isOpenDoor())){
                //setting drawable and OpenDoor to true
                ProjetLabo.user.getVoiture().setOpenDoor(true);
                ib_car_doors.setBackground(getResources().getDrawable(R.drawable.car_door_red));
                ib_car_doors.getBackground().setAlpha(255);
                // setting img button to "GONE" (unclickable)
                ib_car_case.setBackground(getResources().getDrawable(R.drawable.car_case));
                ib_car_case.getBackground().setAlpha(128);
                ib_car_key.setBackground(getResources().getDrawable(R.drawable.car_key));
                ib_car_key.getBackground().setAlpha(128);
            }
            else if ((!ProjetLabo.user.getVoiture().isOpenEtui()) && (ProjetLabo.user.getVoiture().isOpenDoor())){
                // setting drawable and OpenDoor to false
                ProjetLabo.user.getVoiture().setOpenDoor(false);
                ib_car_doors.setBackground(getResources().getDrawable(R.drawable.car_door_green));

                // setting car_case and car_key
                ib_car_case.setBackground(getResources().getDrawable(R.drawable.car_case_green));
                ib_car_case.getBackground().setAlpha(255);
                // setting img button to "GONE" (unclickable)
                ib_car_key.setBackground(getResources().getDrawable(R.drawable.car_key));
                ib_car_key.getBackground().setAlpha(128);
            }
            else {
                Toast.makeText(InfoVehiculeActivity.this, "Fermer d'abord l'étui", Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(InfoVehiculeActivity.this, "Activer d'abord Bluetooth", Toast.LENGTH_LONG).show();
        }
    }


    @OnClick (R.id.car_case)
    public void OnCarCaseButton (){
        if (bluetoothAdapter.isEnabled()){
            if ((!ProjetLabo.user.getVoiture().isOpenDoor()) && (ProjetLabo.user.getVoiture().isOpenEtui())){
                // setting drawable and OpenEtui to false
                ProjetLabo.user.getVoiture().setOpenEtui(false);
                ib_car_case.setBackground(getResources().getDrawable(R.drawable.car_case_green));
                //setting car_doors to clickable
                ib_car_doors.setBackground(getResources().getDrawable(R.drawable.car_door_green));
                ib_car_doors.getBackground().setAlpha(255);
                // setting img button to "GONE" (unclickable)
                ib_car_key.setBackground(getResources().getDrawable(R.drawable.car_key));
                ib_car_key.getBackground().setAlpha(128);
            }
            else if ((!ProjetLabo.user.getVoiture().isOpenDoor())&& (!ProjetLabo.user.getVoiture().isOpenEtui())){
                // setting drawable and OpenEtui to true
                ProjetLabo.user.getVoiture().setOpenEtui(true);
                ib_car_case.setBackground(getResources().getDrawable(R.drawable.car_case_red));
                // setting car_doors to unclickable
                ib_car_doors.setBackground(getResources().getDrawable(R.drawable.car_door));
                ib_car_doors.getBackground().setAlpha(128);
                if (ProjetLabo.user.getVoiture().isKeyInEtui())
                    ib_car_key.setBackground(getResources().getDrawable(R.drawable.car_key_green));
                else
                    ib_car_key.setBackground(getResources().getDrawable(R.drawable.car_key_red));
            }
            else {
                Toast.makeText(InfoVehiculeActivity.this, "Fermer d'abord les portières", Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(InfoVehiculeActivity.this, "Activer d'abord Bluetooth", Toast.LENGTH_LONG).show();
        }

    }


    @OnClick(R.id.car_key)
    public void OnCarKeyButton (){
        if (bluetoothAdapter.isEnabled()){
            if ((ProjetLabo.user.getVoiture().isOpenEtui()) && (ProjetLabo.user.getVoiture().isKeyInEtui())){
                // setting drawable and KeyInEtui to false
                ProjetLabo.user.getVoiture().setKeyInEtui(false);
                ib_car_key.setBackground(getResources().getDrawable(R.drawable.car_key_red));
            }
            else if ((ProjetLabo.user.getVoiture().isOpenEtui()) && (!ProjetLabo.user.getVoiture().isKeyInEtui())){
                // setting drawable and KeyInEtui to true
                ProjetLabo.user.getVoiture().setKeyInEtui(true);
                ib_car_key.setBackground(getResources().getDrawable(R.drawable.car_key_green));
            }
            else {
                if (ProjetLabo.user.getVoiture().isOpenDoor()){
                    Toast.makeText(InfoVehiculeActivity.this, "Fermer d'abord les portières", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(InfoVehiculeActivity.this, "Ouvrir d'abord l'étui", Toast.LENGTH_LONG).show();
                }


            }
        }
        else{
            Toast.makeText(InfoVehiculeActivity.this, "Activer d'abord Bluetooth", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        ProjetLabo.user.setToApi(InfoVehiculeActivity.this);
        ProjetLabo.user.getVoiture().setToApi(InfoVehiculeActivity.this);
    }
}
