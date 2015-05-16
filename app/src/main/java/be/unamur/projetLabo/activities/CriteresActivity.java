package be.unamur.projetLabo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.unamur.projetLabo.ProjetLabo;
import be.unamur.projetLabo.R;
import be.unamur.projetLabo.classes.Critere;
import be.unamur.projetLabo.classes.Voiture;
import be.unamur.projetLabo.request.OkHttpStack;
import be.unamur.projetLabo.request.PostRequest;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CriteresActivity extends AppCompatActivity {
    private ListView listViewCriteres;
    private Critere[] criSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criteres);
        ButterKnife.inject(this);

        listViewCriteres = (ListView) findViewById(R.id.listView_criteres);

        String URL = ProjetLabo.API_BASE_URL + "/criteres.json";

        StringRequest request = new StringRequest(URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {

                    JSONArray criArray = new JSONArray(s);
                    List<String> listCriteres = new ArrayList<String>();
                    criSave = new Critere[criArray.length()];

                    for (int i = 0; i < criArray.length(); i++) {

                        JSONObject criObj = criArray.getJSONObject(i);

                        // Create new Object "Criteres" for each criterion in the DB
                        Critere cri = new Critere(criObj);
                        criSave[i] = cri;

                        listCriteres.add(cri.getName() + "\n" + cri.getType());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(CriteresActivity.this,
                            android.R.layout.simple_list_item_multiple_choice, listCriteres);

                    listViewCriteres.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(CriteresActivity.this, "Error : " + volleyError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(CriteresActivity.this, new OkHttpStack());
        queue.add(request);

    }

    @OnClick(R.id.btn_envoyer)
    public void onClickBtnEnvoyer(View view) {

        SparseBooleanArray CriteresChoisis = listViewCriteres.getCheckedItemPositions();

        JSONArray criArrayChosen = new JSONArray();
        try {

            for (int i = 0; i < CriteresChoisis.size(); i++) {
                if(CriteresChoisis.valueAt(i)) {
                    JSONObject criObjChosen = new JSONObject();
                    criObjChosen.put("Id", criSave[CriteresChoisis.keyAt(i)].getId());
                    criObjChosen.put("Name", criSave[CriteresChoisis.keyAt(i)].getName());
                    criObjChosen.put("Type", criSave[CriteresChoisis.keyAt(i)].getType());
                    criArrayChosen.put(criObjChosen);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Envoi des données a l'API
        String URL = ProjetLabo.API_BASE_URL + "/criteres.json"; //url de l'api

        Map<String, String> params = new HashMap<String, String>();
        params.put("criteres", criArrayChosen.toString());

        PostRequest request = new PostRequest(URL, params, new Response.Listener<String>() {

            @Override
            public void onResponse(String responseData) {
                try {
                    JSONArray voitureArray = new JSONArray(responseData);
                    Voiture[] voitures = new Voiture[voitureArray.length()];

                    for (int i = 0; i < voitureArray.length(); i++) {

                        JSONObject voitureObj = voitureArray.getJSONObject(i);

                        // Create new Object "Criteres" for each criterion in the DB
                        voitures[i] = new Voiture(voitureObj);
                    }

                    //Appel de la l'activity voiture
                    if (voitures.length == 0) {
                        Toast.makeText(CriteresActivity.this, "Aucune voiture ne correspond à vos cirtères !", Toast.LENGTH_LONG).show();
                    }else {
                        Intent intent = new Intent(CriteresActivity.this, ListeVoituresActivity.class);
                        intent.putExtra("voitures", voitures);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    //Erreur lors du décodage du json
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Requête à échoué
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(CriteresActivity.this, new OkHttpStack());
        queue.add(request);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_profile:
                startActivity(new Intent(CriteresActivity.this, ProfileActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_default, menu);
        return super.onCreateOptionsMenu(menu);
    }

}
