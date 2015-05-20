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

        //Récupération de la vue
        lbl_solde_capital = (TextView) findViewById(R.id.points_fidelisation);
        iv_correct_gps = (ImageView) findViewById(R.id.points_gps_fidelisation);
        iv_correct_plein = (ImageView) findViewById(R.id.points_plein_fidelisation);
        iv_correct_conseils = (ImageView) findViewById(R.id.points_conseils_fidelisation);

        //SETTING USER FIDELE -> TRUE
        if (ProjetLabo.user.isFidele()== false){
            Map<String, String> params = new HashMap<String, String>();
            params.put("fidele", Boolean.toString(true));

            int usrID = ProjetLabo.user.getId();
            String URL = ProjetLabo.API_BASE_URL + "/fideles/"+ usrID+".json";
            PostRequest requestAddUser = new PostRequest(URL, params, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    try {
                        JSONObject userJSON = new JSONObject(s);
                        if (userJSON.has("response")) {
                            if(userJSON.getBoolean("response")) {
                                ProjetLabo.user.setFidele(true);
                            }
                            else{
                                SoldeFidelisationAcitivity.this.finish();
                            }
                        } else {
                           SoldeFidelisationAcitivity.this.finish();
                        }
                    } catch (JSONException e) {
                        SoldeFidelisationAcitivity.this.finish();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(SoldeFidelisationAcitivity.this, "Une erreur réseau est survenue !", Toast.LENGTH_LONG).show();
                        }
                    });
            RequestQueue queue = Volley.newRequestQueue(SoldeFidelisationAcitivity.this, new OkHttpStack());
            queue.add(requestAddUser);
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
    public void OnReturnButton (Parcelable token){
        startActivity(new Intent(SoldeFidelisationAcitivity.this, ConnexionActivity.class));
        SoldeFidelisationAcitivity.this.finish();
    }
}
