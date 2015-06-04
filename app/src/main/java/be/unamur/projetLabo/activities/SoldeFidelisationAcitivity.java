package be.unamur.projetLabo.activities;

import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import be.unamur.projetLabo.ProjetLabo;
import be.unamur.projetLabo.R;
import be.unamur.projetLabo.request.OkHttpStack;
import be.unamur.projetLabo.request.PostRequest;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SoldeFidelisationAcitivity extends ActionBarActivity {

    private TextView lbl_solde_capital;
    private ImageView iv_correct_gps;
    private ImageView iv_correct_conseils;
    private ImageView iv_correct_plein;
    @Override
    //Creating layout on the Smartphone screen.
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solde_fidelisation_acitivity);
        ButterKnife.inject(this);

        //Récupération de la vue
        lbl_solde_capital = (TextView) findViewById(R.id.points_fidelisation);
        iv_correct_gps = (ImageView) findViewById(R.id.points_gps_fidelisation);
        iv_correct_plein = (ImageView) findViewById(R.id.points_plein_fidelisation);
        iv_correct_conseils = (ImageView) findViewById(R.id.points_conseils_fidelisation);

        //SETTING USER FIDELE -> TRUE
        if (!ProjetLabo.user.isFidele()){
            ProjetLabo.user.setFidele(true);
            ProjetLabo.user.setToApi(SoldeFidelisationAcitivity.this);
        }
        int solde_capital = ProjetLabo.user.getCapital();
        lbl_solde_capital.setText(Integer.toString(solde_capital));

        // Valeur par défaut
        iv_correct_conseils.setBackground(getResources().getDrawable(R.drawable.correct_v));
        iv_correct_gps.setBackground(getResources().getDrawable(R.drawable.wrong_x));
        iv_correct_plein.setBackground(getResources().getDrawable(R.drawable.wrong_x));

        //Application des contraintes
        if (solde_capital >= 3) {
            iv_correct_gps.setBackground(getResources().getDrawable(R.drawable.correct_v));
        }
        if (solde_capital >= 5) {
            iv_correct_plein.setBackground(getResources().getDrawable(R.drawable.correct_v));
        }
    }

    @OnClick(R.id.btn_retour)
    public void OnReturnButton (View view){
        SoldeFidelisationAcitivity.this.finish();
    }
}
