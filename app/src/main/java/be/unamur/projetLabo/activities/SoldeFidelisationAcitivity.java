package be.unamur.projetLabo.activities;

import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import be.unamur.projetLabo.ProjetLabo;
import be.unamur.projetLabo.R;
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
        lbl_solde_capital = (TextView) findViewById(R.id.points_fidelisation);
        int solde_capital = ProjetLabo.user.getCapital();
        lbl_solde_capital.setText(solde_capital);
        iv_correct_gps = (ImageView) findViewById(R.id.points_gps_fidelisation);
        iv_correct_plein = (ImageView) findViewById(R.id.points_plein_fidelisation);
        iv_correct_conseils = (ImageView) findViewById(R.id.points_conseils_fidelisation);
        // l'utilisateur a toujours accès aux conseils.
        iv_correct_conseils.setBackground(getResources().getDrawable(R.drawable.correct_v));
        //tests d'affichage pour GPS et Plein.
        if (solde_capital >= 3) {
            iv_correct_gps.setBackground(getResources().getDrawable(R.drawable.correct_v));
        } else {
            iv_correct_gps.setBackground(getResources().getDrawable(R.drawable.wrong_x));
            iv_correct_plein.setBackground(getResources().getDrawable(R.drawable.wrong_x));
        }
        if (solde_capital >= 5) {
            iv_correct_plein.setBackground(getResources().getDrawable(R.drawable.correct_v));
        } else {
            iv_correct_plein.setBackground(getResources().getDrawable(R.drawable.wrong_x));
        }
    }

    @OnClick(R.id.btn_retour)
    public void OnReturnButton (Parcelable token){
        startActivity(new Intent(SoldeFidelisationAcitivity.this, ConnexionActivity.class));
        SoldeFidelisationAcitivity.this.finish();
    }
}
