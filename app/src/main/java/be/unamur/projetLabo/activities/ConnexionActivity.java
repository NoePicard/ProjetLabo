package be.unamur.projetLabo.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mrengineer13.snackbar.SnackBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import be.unamur.projetLabo.R;
import be.unamur.projetLabo.ProjetLabo;

import be.unamur.projetLabo.classes.Utilisateur;
import be.unamur.projetLabo.request.OkHttpStack;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ConnexionActivity extends ActionBarActivity implements SnackBar.OnMessageClickListener {

    private EditText login;
    private EditText password;
    private TextView error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        ButterKnife.inject(this);

       //Récupération vue
        login = (EditText) findViewById(R.id.txt_login);
        password = (EditText) findViewById(R.id.txt_password);
        error = (TextView) findViewById(R.id.lbl_error);

        showSnackBar();
    }
    public void showSnackBar(){
        new SnackBar.Builder(this)
                .withOnClickListener(this)
                .withMessage("Pas encore inscrit ?")
                .withActionMessage("INSCRIVEZ-VOUS")
                .withBackgroundColorId(R.color.primary)
                .withTextColorId(R.color.accent)
                .show();
    }

    @OnClick (R.id.btn_connexion)
    public void onClickBtnConnexion(View view) {
        //Check parmi les users inscrits
        String strLogin = login.getText().toString();
        String strPassword = password.getText().toString();
        if(!strLogin.isEmpty() || !strPassword.isEmpty()) {
            String URL = ProjetLabo.API_BASE_URL + "/users/" +strLogin+ ".json";
            StringRequest request = new StringRequest(URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    try{
                        JSONObject userJSON = new JSONObject(s);
                        if (userJSON.has("response")) {
                            if (userJSON.getBoolean("response")) {
                                if(password.getText().toString().equals(userJSON.getString("password"))){
                                    ProjetLabo.user.connexion();
                                    ProjetLabo.user.hydrate(userJSON);
                                    error.setText("");
                                    //Nouvelle activity
                                    if(ProjetLabo.user.isContrated()){

                                    }else{
                                        startActivity(new Intent(ConnexionActivity.this, ContratActivity.class));
                                        ConnexionActivity.this.finish();
                                    }
                                }
                            }
                            else {
                                showSnackBar();
                                password.setText("");
                                error.setText("Votre compte n'existe pas !");
                            }
                        } else {
                            showSnackBar();
                            password.setText("");
                            error.setText("Veuilliez réessayer !");
                        }
                    } catch (JSONException e) {
                        showSnackBar();
                        password.setText("");
                        error.setText("Veuilliez réessayer !");
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    showSnackBar();
                    Toast.makeText(ConnexionActivity.this, "Une erreur réseau est survenue !", Toast.LENGTH_LONG).show();
                }
            });

            RequestQueue queue = Volley.newRequestQueue(ConnexionActivity.this, new OkHttpStack());
            queue.add(request);
        }else{
            showSnackBar();
            password.setText("");
            error.setText("Veuillez encoder votre Login et Password !");
        }
    }
    @Override
    public void onMessageClick(Parcelable token) {
        startActivity(new Intent(ConnexionActivity.this, InscriptionActivity.class));
        ConnexionActivity.this.finish();
    }
}
