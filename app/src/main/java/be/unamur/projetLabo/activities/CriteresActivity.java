package be.unamur.projetLabo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.unamur.projetLabo.ProjetLabo;
import be.unamur.projetLabo.R;
import be.unamur.projetLabo.classes.Critere;
import be.unamur.projetLabo.classes.Voiture;
import be.unamur.projetLabo.fragment.DatePickerFragment;
import be.unamur.projetLabo.request.OkHttpStack;
import be.unamur.projetLabo.request.PostRequest;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CriteresActivity extends BaseActivity implements DatePickerFragment.OnDatePickerSetListener {
    private ListView listViewCriteres;
    private EditText btnStart;
    private EditText btnEnd;
    private Critere[] criSave;
    private long start;
    private long end;
    private String StartOrEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criteres);
        ButterKnife.inject(this);

        ActionBar actionBar = CriteresActivity.this.getSupportActionBar();
        try{
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }catch (Exception e){
        }


        listViewCriteres = (ListView) findViewById(R.id.listView_criteres);
        btnStart = (EditText) findViewById(R.id.txt_startDate);
        btnEnd = (EditText) findViewById(R.id.txt_endDate);

        showProgressBar();

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

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(CriteresActivity.this, android.R.layout.simple_list_item_multiple_choice, listCriteres);
                    listViewCriteres.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                hideProgressBar();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideProgressBar();
                Toast.makeText(CriteresActivity.this, "Error : " + volleyError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(CriteresActivity.this, new OkHttpStack());
        queue.add(request);

    }

    @OnClick(R.id.txt_startDate)
    public void onClickTxtStartDate(View v) {
        DialogFragment dateFragment = new DatePickerFragment();
        dateFragment.show(getSupportFragmentManager(), "timePicker");
        StartOrEnd = "start";
    }

    @OnClick(R.id.txt_endDate)
    public void onClickTxtEndDate(View v) {
        DialogFragment dateFragment = new DatePickerFragment();
        dateFragment.show(getSupportFragmentManager(), "timePicker");
        StartOrEnd = "end";
    }

    @Override
    public void onDatePickerSet(int year, int monthOfYear, int dayOfMonth) {
        Calendar date = new GregorianCalendar();
        date.set(year, monthOfYear, dayOfMonth);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
        switch (StartOrEnd) {
            case "start":
                start = date.getTime().getTime();
                btnStart.setText(dateFormat.format(start));
                break;
            case "end":
                end = date.getTime().getTime();
                btnEnd.setText(dateFormat.format(end));
                break;
            default:
                start = 0;
                end = 0;
                break;
        }
        if (start != 0 && end != 0) {
            if (start > end && end != 0) {
                Toast.makeText(CriteresActivity.this, "Veuillez choisir une date de fin correcte.", Toast.LENGTH_LONG).show();
                end = 0;
                btnEnd.setText("");
            }
        }
        if(start != 0){
            date = new GregorianCalendar();
            if ((date.getTimeInMillis() - (1000 * 60 * 60 * 24)) > start) {
                Toast.makeText(CriteresActivity.this, "Veuillez choisir une date de début correcte.", Toast.LENGTH_LONG).show();
                start = 0;
                btnStart.setText("");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideProgressBar();
    }

    @OnClick(R.id.btn_next)
    public void onClickBtnNext(View view) {
        if (start != 0 && end != 0) {
            if (start < end) {
                SparseBooleanArray CriteresChoisis = listViewCriteres.getCheckedItemPositions();

                JSONArray criArrayChosen = new JSONArray();
                try {

                    for (int i = 0; i < CriteresChoisis.size(); i++) {
                        if (CriteresChoisis.valueAt(i)) {
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
                action_bar_progress.setVisible(true);
                //Envoi des données a l'API
                String URL = ProjetLabo.API_BASE_URL + "/criteres.json"; //url de l'api

                Map<String, String> params = new HashMap<String, String>();
                params.put("criteres", criArrayChosen.toString());
                params.put("start", Long.toString(start));
                params.put("end", Long.toString(end));

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
                                action_bar_progress.setVisible(false);
                                Toast.makeText(CriteresActivity.this, "Aucune voiture ne correspond à vos cirtères !", Toast.LENGTH_LONG).show();
                            } else {
                                Intent intent = new Intent(CriteresActivity.this, ListeVoituresActivity.class);
                                intent.putExtra("voitures", voitures);
                                intent.putExtra("Debut", start);
                                intent.putExtra("Fin", end);
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            action_bar_progress.setVisible(false);
                            //Erreur lors du décodage du json
                        }
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                action_bar_progress.setVisible(false);
                                //Requête à échoué
                            }
                        });

                RequestQueue queue = Volley.newRequestQueue(CriteresActivity.this, new OkHttpStack());
                queue.add(request);
            } else {

                Toast.makeText(CriteresActivity.this, "Veuilliez indiquer des dates de début et de fin de location correcte", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(CriteresActivity.this, "Veuilliez indiquer des dates de début et de fin de location correcte", Toast.LENGTH_LONG).show();
        }
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
