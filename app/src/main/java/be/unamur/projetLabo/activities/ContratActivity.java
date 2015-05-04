package be.unamur.projetLabo.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
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

public class ContratActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contrat);
        ButterKnife.inject(this);

        WebView webViewContrat = (WebView) findViewById(R.id.wv_contrat);
        webViewContrat.loadUrl(ProjetLabo.CONTRAT_BASE_URL);
    }

    @OnClick(R.id.btn_contrat_accepte)
    public void onClickBtnInscription(View view) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("contrat", "true");

        String URL = ProjetLabo.API_BASE_URL + "/contrats/"+ ProjetLabo.user.getId()+".json";
        PostRequest requestAddUser = new PostRequest(URL, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject userJSON = new JSONObject(s);
                    if (userJSON.has("response")) {
                        if (userJSON.getBoolean("response")) {
                            ProjetLabo.user.setContrated(true);
                            //New Activity
                            Toast.makeText(ContratActivity.this, "Contrat accepté !", Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (JSONException e) {
                    Toast.makeText(ContratActivity.this, "Une erreur réseau est survenue !", Toast.LENGTH_LONG).show();
                }
            }
        },
        new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(ContratActivity.this, "Une erreur réseau est survenue !", Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(ContratActivity.this, new OkHttpStack());
        queue.add(requestAddUser);
    }
}
