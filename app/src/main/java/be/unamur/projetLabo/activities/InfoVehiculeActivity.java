package be.unamur.projetLabo.activities;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cardiomood.android.controls.gauge.SpeedometerGauge;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

import be.unamur.projetLabo.ProjetLabo;
import be.unamur.projetLabo.R;
import butterknife.OnClick;

public class InfoVehiculeActivity extends ActionBarActivity {

    //public var for layout.
    public TextView lbl_car_model;
    public TextView lbl_renting_end_date;
    public ImageButton ib_car_doors;
    public ImageButton ib_car_key;
    public ImageButton ib_bluetooth_activated;
    public ImageButton ib_car_case;
    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter(); //SETTING BT ADAPTER
    private final static int REQUEST_CODE_ENABLE_BLUETOOTH = 0;
    public SpeedometerGauge sv_speedometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_vehicule);

        /*
            LINKING VAR TO LAYOUT
         */
        lbl_car_model = (TextView) findViewById(R.id.car_model);
        lbl_renting_end_date = (TextView) findViewById(R.id.renting_end_date);
        ib_bluetooth_activated = (ImageButton) findViewById(R.id.bluetooth_activated);
        ib_car_case = (ImageButton) findViewById(R.id.car_case);
        ib_car_doors = (ImageButton) findViewById(R.id.car_doors);
        ib_car_key = (ImageButton) findViewById(R.id.car_key);
        sv_speedometer = (SpeedometerGauge) findViewById(R.id.speedometer);

        /*
            SETTING END OF RENT DATE
        */
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        lbl_renting_end_date.setText(dateFormat.format(ProjetLabo.user.getVoiture().getEnd().getTime()));

       /*
            SETTING CAR MODEL
        */
        String car_model = ProjetLabo.user.getVoiture().getName();
        lbl_car_model.setText(car_model);


        /*
            SETTING IMAGEVIEWS
         */

        //verifying is Bluetooth is supported
        if (bluetoothAdapter == null) {
            Toast.makeText(InfoVehiculeActivity.this, "La fonction Bluetooth n'est pas supportée",
                    Toast.LENGTH_SHORT).show();
            InfoVehiculeActivity.this.finish();
        }
        else {
            // settings if bluetooth isn't enabled
            if (!bluetoothAdapter.isEnabled()){
                ib_bluetooth_activated.setBackground(getResources().getDrawable(R.drawable.blueetooth_red));
                // setting every img button to "gone" (unclickable)
                ib_car_doors.setVisibility(View.GONE);
                ib_car_case.setVisibility(View.GONE);
                ib_car_key.setVisibility(View.GONE);
                /*
                    SETTING GAUGE TO GONE
                 */
            }
            else {
                ib_bluetooth_activated.setBackground(getResources().getDrawable(R.drawable.blueetooth_green));
                if (ProjetLabo.user.getVoiture().isOpenDoor()) {
                    ib_car_doors.setBackground(getResources().getDrawable(R.drawable.car_door_red));
                    // setting img button to "gone" (unclickable)
                    ib_car_case.setVisibility(View.GONE);
                    ib_car_key.setVisibility(View.GONE);

                }
                else {
                    ib_car_doors.setBackground(getResources().getDrawable(R.drawable.car_door_green));
                    // check if car_case is opened. If well, access to key in case unlocked
                    if (ProjetLabo.user.getVoiture().isOpenEtui()){
                        ib_car_case.setBackground(getResources().getDrawable(R.drawable.car_case_red));
                        // check if key is inside the case
                        if (ProjetLabo.user.getVoiture().isKeyInEtui())
                            ib_car_key.setBackground(getResources().getDrawable(R.drawable.car_key_green));
                        else
                            ib_car_key.setBackground(getResources().getDrawable(R.drawable.car_key_red));
                    }
                    else {
                        ib_car_case.setBackground(getResources().getDrawable(R.drawable.car_case_green));
                        // setting img button to "gone" (unclickable)
                        ib_car_key.setVisibility(View.GONE);
                    }

                }

                /*
                    SET fuel_gauge TO VISIBLE.
                 */
                // Add label converter
                sv_speedometer.setLabelConverter(new com.cardiomood.android.controls.gauge.SpeedometerGauge.LabelConverter() {
                    public String getLabelFor(double progress, double maxProgress) {
                        return String.valueOf(ProjetLabo.user.getVoiture().getFuelQuantity());
                    }
                });

                // configure value range and ticks
                sv_speedometer.setMaxSpeed(60);
                sv_speedometer.setMajorTickStep(5);
                sv_speedometer.setMinorTicks(4);

                // Configure value range colors
                sv_speedometer.addColoredRange(0, 10, Color.RED);
                sv_speedometer.addColoredRange(15, 25, Color.YELLOW);
                sv_speedometer.addColoredRange(25, 60, Color.GREEN);

            }
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
            ib_car_doors.setVisibility(View.GONE);
            ib_car_case.setVisibility(View.GONE);
            ib_car_key.setVisibility(View.GONE);
            sv_speedometer.setVisibility(View.GONE);
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
            // setting all img buttons : if available, if gone...
            if (ProjetLabo.user.getVoiture().isOpenDoor()) {
                ib_car_doors.setBackground(getResources().getDrawable(R.drawable.car_door_red));
                // setting img button to "GONE" (unclickable)
                ib_car_case.setVisibility(View.GONE);
                ib_car_key.setVisibility(View.GONE);
            }
            else {
                ib_car_doors.setBackground(getResources().getDrawable(R.drawable.car_door_green));
                // check if car_case is opened. If well, access to key in case unlocked
                if (ProjetLabo.user.getVoiture().isOpenEtui()){
                    ib_car_case.setBackground(getResources().getDrawable(R.drawable.car_case_red));
                    // check if key is inside the case
                    if (ProjetLabo.user.getVoiture().isKeyInEtui())
                        ib_car_key.setBackground(getResources().getDrawable(R.drawable.car_key_green));
                    else
                        ib_car_key.setBackground(getResources().getDrawable(R.drawable.car_key_red));
                }
                else {
                    ib_car_case.setBackground(getResources().getDrawable(R.drawable.car_case_green));
                    // setting img button to "GONE" (unclickable)
                    ib_car_key.setVisibility(View.GONE);
                }

            }

        }
    }

    @OnClick(R.id.car_doors)
    public void OnCarDoorsButton (){
        if ((!ProjetLabo.user.getVoiture().isOpenDoor()) && (!ProjetLabo.user.getVoiture().isOpenEtui())){
            //setting drawable and OpenDoor to true
            ProjetLabo.user.getVoiture().setOpenDoor(true);
            ib_car_doors.setBackground(getResources().getDrawable(R.drawable.car_door_red));
            // setting img button to "GONE" (unclickable)
            ib_car_case.setVisibility(View.GONE);
            ib_car_key.setVisibility(View.GONE);
        }
        if ((ProjetLabo.user.getVoiture().isOpenDoor()) && (!ProjetLabo.user.getVoiture().isOpenEtui())){
            // setting drawable and OpenDoor to false
            ProjetLabo.user.getVoiture().setOpenDoor(false);
            ib_car_doors.setBackground(getResources().getDrawable(R.drawable.car_door_green));
            // setting car_case and car_key
            ib_car_case.setBackground(getResources().getDrawable(R.drawable.car_case_green));
            // setting img button to "GONE" (unclickable)
            ib_car_key.setVisibility(View.GONE);
        }
    }


    @OnClick (R.id.car_case)
    public void OnCarCaseButton (){
        if ((!ProjetLabo.user.getVoiture().isOpenDoor()) && (ProjetLabo.user.getVoiture().isOpenEtui())){
            // setting drawable and OpenEtui to false
            ProjetLabo.user.getVoiture().setOpenEtui(false);
            ib_car_case.setBackground(getResources().getDrawable(R.drawable.car_case_green));
            // setting img button to "GONE" (unclickable)
            ib_car_key.setVisibility(View.GONE);
        }
        if ((!ProjetLabo.user.getVoiture().isOpenDoor())&& (!ProjetLabo.user.getVoiture().isOpenEtui())){
            // setting drawable and OpenEtui to true
            ProjetLabo.user.getVoiture().setOpenEtui(true);
            ib_car_case.setBackground(getResources().getDrawable(R.drawable.car_case_red));
            if (ProjetLabo.user.getVoiture().isKeyInEtui())
                ib_car_key.setBackground(getResources().getDrawable(R.drawable.car_key_green));
            else
                ib_car_key.setBackground(getResources().getDrawable(R.drawable.car_key_red));
        }
    }


    @OnClick(R.id.car_key)
    public void OnCarKeyButton (){
        if ((ProjetLabo.user.getVoiture().isOpenEtui()) && (ProjetLabo.user.getVoiture().isKeyInEtui())){
            // setting drawable and KeyInEtui to false
            ProjetLabo.user.getVoiture().setKeyInEtui(false);
            ib_car_key.setBackground(getResources().getDrawable(R.drawable.car_key_red));
        }
        if ((ProjetLabo.user.getVoiture().isOpenEtui()) && (!ProjetLabo.user.getVoiture().isKeyInEtui())){
            // setting drawable and KeyInEtui to true
            ProjetLabo.user.getVoiture().setKeyInEtui(true);
            ib_car_key.setBackground(getResources().getDrawable(R.drawable.car_key_green));
        }
    }

}
