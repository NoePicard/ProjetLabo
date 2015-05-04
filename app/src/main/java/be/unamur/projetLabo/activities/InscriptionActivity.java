package be.unamur.projetLabo.activities;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.github.mrengineer13.snackbar.SnackBar;

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

public class InscriptionActivity extends AppCompatActivity implements SnackBar.OnMessageClickListener{
    private EditText login;
    private EditText password;
    private TextView error;
    private CheckBox fidele;

    public void showSnackBar(){
        new SnackBar.Builder(this)
                .withOnClickListener(this)
                .withMessage("Déjà inscrit ? N'attendez-plus !")
                .withActionMessage("CONNECTEZ-VOUS")
                .withBackgroundColorId(R.color.primary)
                .withTextColorId(R.color.accent)
                .show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        ButterKnife.inject(this);
        //Récupération vue
        login = (EditText) findViewById(R.id.txt_login);
        password = (EditText) findViewById(R.id.txt_password);
        fidele = (CheckBox) findViewById(R.id.chb_fidele);
        error = (TextView) findViewById(R.id.lbl_error);

        showSnackBar();
    }
    @OnClick(R.id.btn_inscription)
    public void onClickBtnInscription(View view) {
        String strLogin = login.getText().toString();
        String strPassword = password.getText().toString();
        if(!strLogin.isEmpty() || !strPassword.isEmpty()) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("login", login.getText().toString());
            params.put("password", password.getText().toString());
            params.put("fidele", Boolean.toString(fidele.isChecked()));

            String URL = ProjetLabo.API_BASE_URL + "/users.json";

            PostRequest requestAddUser = new PostRequest(URL, params, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    try {
                        JSONObject userJSON = new JSONObject(s);
                        if (userJSON.has("response")) {
                            if(userJSON.getBoolean("response")) {
                                ProjetLabo.user.connexion();
                                ProjetLabo.user.hydrate(userJSON);
                                error.setText("");
                                startActivity(new Intent(InscriptionActivity.this, ContratActivity.class));
                                InscriptionActivity.this.finish();
                            }else{
                                showSnackBar();
                                password.setText("");
                                login.setText("");
                                error.setText("Ce login existe déjà ! Choississez-en un autre");
                            }
                        } else {
                            showSnackBar();
                            password.setText("");
                            error.setText("Une erreur est survenue veuillez réessayer");
                        }
                    } catch (JSONException e) {
                        showSnackBar();
                        password.setText("");
                        error.setText("Une erreur est survenue veuillez réessayer");
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            showSnackBar();
                            Toast.makeText(InscriptionActivity.this, "Une erreur réseau est survenue !", Toast.LENGTH_LONG).show();
                        }
                    });
            RequestQueue queue = Volley.newRequestQueue(InscriptionActivity.this, new OkHttpStack());
            queue.add(requestAddUser);
        }else{
            showSnackBar();
            password.setText("");
            error.setText("Veuillez encoder votre Login et Password !");
        }
    }

    @Override
    public void onMessageClick(Parcelable token) {
        startActivity(new Intent(InscriptionActivity.this, ConnexionActivity.class));
        InscriptionActivity.this.finish();
    }
}
