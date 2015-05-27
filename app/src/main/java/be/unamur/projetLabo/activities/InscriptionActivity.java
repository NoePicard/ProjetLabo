package be.unamur.projetLabo.activities;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import java.util.Random;

import be.unamur.projetLabo.ProjetLabo;
import be.unamur.projetLabo.R;
import be.unamur.projetLabo.classes.Utilisateur;
import be.unamur.projetLabo.exception.UserConnectionException;
import be.unamur.projetLabo.request.OkHttpStack;
import be.unamur.projetLabo.request.PostRequest;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InscriptionActivity extends BaseActivity implements SnackBar.OnMessageClickListener{
    private LinearLayout layout1;
    private LinearLayout layout2;
    private EditText login;
    private EditText password;
    private TextView error;
    private CheckBox fidele;
    private EditText name;
    private EditText firstName;

    public void showSnackBar(){
        new SnackBar.Builder(this)
                .withOnClickListener(this)
                .withMessage("Déjà inscrit ? N'attendez-plus !")
                .withActionMessage("CONNECTEZ-VOUS")
                .withBackgroundColorId(R.color.primary)
                .withTextColorId(R.color.accent)
                .withDuration((short) 0)
                .show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        ButterKnife.inject(this);
        //Récupération vue

        layout1 = (LinearLayout) findViewById(R.id.layout1_inscription);
        layout2 = (LinearLayout) findViewById(R.id.layout2_inscription);

        name = (EditText) layout1.findViewById(R.id.txt_name);
        firstName = (EditText) layout1.findViewById(R.id.txt_firstName);
        fidele = (CheckBox) layout1.findViewById(R.id.chb_fidele);
        login = (EditText) layout2.findViewById(R.id.txt_login);
        password = (EditText) layout2.findViewById(R.id.txt_password);
        error = (TextView) layout2.findViewById(R.id.lbl_error);

        showSnackBar();
    }

    @OnClick(R.id.btn_inscription)
    public void onClickBtnInscription(View view) {
        String strName = name.getText().toString();
        String strFirstName = firstName.getText().toString();
        final String strLogin;
        final String strPassword;

        if(!strName.isEmpty() || !strFirstName.isEmpty()) {
            showProgressBar();

            strLogin = strFirstName.substring(0,1).toLowerCase() + strName.toLowerCase();
            strPassword = randomString(8);

            Map<String, String> params = new HashMap<String, String>();
            params.put("login", strLogin);
            params.put("password", strPassword);
            params.put("fidele", Boolean.toString(fidele.isChecked()));

            String URL = ProjetLabo.API_BASE_URL + "/users.json";

            PostRequest requestAddUser = new PostRequest(URL, params, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    try {
                        JSONObject userJSON = new JSONObject(s);
                        if (userJSON.has("response")) {
                            if(userJSON.getBoolean("response")) {
                                try{
                                    ProjetLabo.user = new Utilisateur(userJSON);
                                    error.setText("");
                                    layout1.setVisibility(View.GONE);
                                    login.setText(strLogin);
                                    password.setText(strPassword);
                                    layout2.setVisibility(View.VISIBLE);
                                } catch (UserConnectionException e) {
                                    Toast.makeText(InscriptionActivity.this, "Impossible de récupérer vos données", Toast.LENGTH_LONG).show();
                                }

                            }else{
                                hideProgressBar();
                                password.setText("");
                                login.setText("");
                                error.setText("Ce login existe déjà ! Choississez-en un autre");
                            }
                        } else {
                            hideProgressBar();
                            password.setText("");
                            error.setText("Une erreur est survenue veuillez réessayer");
                        }
                    } catch (JSONException e) {
                        hideProgressBar();
                        password.setText("");
                        error.setText("Une erreur est survenue veuillez réessayer");
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            hideProgressBar();
                            Toast.makeText(InscriptionActivity.this, "Une erreur réseau est survenue !", Toast.LENGTH_LONG).show();
                        }
                    });

            RequestQueue queue = Volley.newRequestQueue(InscriptionActivity.this, new OkHttpStack());
            queue.add(requestAddUser);
        }else{
            password.setText("");
            error.setText("Veuillez encoder votre Login et Password !");
        }

    }

    @OnClick(R.id.btn_nextInscription)
    public void onClickBtnNextInscription(View view) {
        startActivity(new Intent(InscriptionActivity.this, ContratActivity.class));
        InscriptionActivity.this.finish();
    }

    @Override
    public void onMessageClick(Parcelable token) {
        startActivity(new Intent(InscriptionActivity.this, ConnexionActivity.class));
        InscriptionActivity.this.finish();
    }

    private static final String passwordDomain = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static Random rnd = new Random();
    private String randomString(int passwordSize)
    {
        StringBuilder sb = new StringBuilder( passwordSize );
        for( int i = 0; i < passwordSize; i++ )
            sb.append(passwordDomain.charAt(rnd.nextInt(passwordDomain.length())));
        return sb.toString();
    }
}
