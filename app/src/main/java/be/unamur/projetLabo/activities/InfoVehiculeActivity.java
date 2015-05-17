package be.unamur.projetLabo.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Date;

import be.unamur.projetLabo.ProjetLabo;
import be.unamur.projetLabo.R;

public class InfoVehiculeActivity extends ActionBarActivity {

    //public var for layout.
    public TextView lbl_car_model;
    public TextView lbl_renting_end_date;
    public TextView lbl_fuel_quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_vehicule);

        //linking public var to layout
        lbl_car_model = (TextView) findViewById(R.id.car_model);
        lbl_renting_end_date = (TextView) findViewById(R.id.renting_end_date);


        //setting end of renting date
        String renting_end_date = ProjetLabo.user.getVoiture().getEnd().toString();
        lbl_renting_end_date.setText(renting_end_date);

        //setting car model
        String car_model = ProjetLabo.user.getVoiture().getName();
        lbl_car_model.setText(car_model);

        //setting fuel quantity
        String fuelQuantity = Float.toString(ProjetLabo.user.getVoiture().getFuelQuantity());

        //Setting ImageViews
        ProjetLabo.user.getVoiture().getFuelQuantity();
    }

}
