package be.unamur.projetLabo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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

import be.unamur.projetLabo.ProjetLabo;
import be.unamur.projetLabo.R;
import be.unamur.projetLabo.classes.Criteres;
import be.unamur.projetLabo.request.OkHttpStack;
import be.unamur.projetLabo.request.PostRequest;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CriteresActivity extends AppCompatActivity {
    private ListView listViewCriteres;
    private Criteres[] criSave;

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
                    criSave = new Criteres[criArray.length()];

                    for (int i = 0; i < criArray.length(); i++) {

                        JSONObject criObj = criArray.getJSONObject(i);

                        // Create new Object "Criteres" for each criterion in the DB
                        Criteres cri = new Criteres(criObj);
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
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(CriteresActivity.this, "Error : " + volleyError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(CriteresActivity.this, new OkHttpStack());
        queue.add(request);

    }

    @OnClick (R.id.btn_envoyer)
    public void onClickBtnEnvoyer(View view){

        SparseBooleanArray CriteresChoisis = listViewCriteres.getCheckedItemPositions();

        JSONArray criArrayChosen = new JSONArray();
        try {

            for (int i = 0; i < CriteresChoisis.size(); i++) {

                JSONObject criObjChosen = new JSONObject();

                criObjChosen.put("Id", criSave[CriteresChoisis.keyAt(i)].getId());
                criObjChosen.put("Name",criSave[CriteresChoisis.keyAt(i)].getName());
                criObjChosen.put("Type",criSave[CriteresChoisis.keyAt(i)].getType());

                criArrayChosen.put(criObjChosen);
            }

            //criArrayChosen.toString();

            startActivity(new Intent(CriteresActivity.this, VoituresActivity.class));

        } catch(JSONException e) {e.printStackTrace();}
    }
}
