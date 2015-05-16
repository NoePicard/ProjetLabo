package be.unamur.projetLabo.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import be.unamur.projetLabo.ProjetLabo;
import be.unamur.projetLabo.R;

public class InfoVehiculeActivity extends ActionBarActivity {

    //public var for layout.
    public TextView lbl_car_model;
    public TextView lbl_renting_end_date;
    public ImageView iv_renting;
    /*
        external variables :
            getVoiture.getName()
            getVoiture.getDate() -- date format : jj-mm-aa
            getVoiture.getRentingStatus()
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_vehicule);

        //linking public var to layout
        iv_renting = (ImageView) findViewById(R.id.renting_true);
        lbl_car_model = (TextView) findViewById(R.id.car_model);
        lbl_renting_end_date = (TextView) findViewById(R.id.renting_end_date);

        if (getVoiture.getRentingStatus()== true) {
            iv_renting.setBackground(getResources().getDrawable(R.drawable.correct_v));

            //setting car model
            String car_model = ProjetLabo.user.getVoiture.getName();
            lbl_car_model.setText(car_model);

            //setting end of renting date
            String renting_end_date = ProjetLabo.user.getVoiture.getDate();
            lbl_renting_end_date.setText(renting_end_date);

        }
        else
            iv_renting.setBackground(getResources().getDrawable(R.drawable.wrong_x));

    }

}
