package be.unamur.projetLabo.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import be.unamur.projetLabo.ProjetLabo;
import be.unamur.projetLabo.R;
import be.unamur.projetLabo.classes.VoitureLoue;
import be.unamur.projetLabo.fragment.DatePickerFragment;
import be.unamur.projetLabo.request.OkHttpStack;
import be.unamur.projetLabo.request.PostRequest;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileActivity extends BaseActivity implements DatePickerFragment.OnDatePickerSetListener {
    private CardView cvVoitureLoue;
    private Button btnLouer;
    private ImageView voiturePhoto;
    private TextView voitureName;
    private TextView lblLogin;
    private TextView lblDateLocation;
    private Button btnFidele;
    private Button btnRendre;
    private LinearLayout llTitleVoiture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.inject(this);


        cvVoitureLoue = (CardView) findViewById(R.id.cvVoitureLoue);
        btnLouer = (Button) findViewById(R.id.btnLouer);
        btnFidele = (Button) findViewById(R.id.btnFidele);
        btnRendre = (Button) findViewById(R.id.btnRendre);
        voiturePhoto = (ImageView) findViewById(R.id.voiture_photo);
        voitureName = (TextView) findViewById(R.id.voiture_name);
        lblLogin = (TextView) findViewById(R.id.lblLogin);
        lblDateLocation = (TextView) findViewById(R.id.date_location);
        llTitleVoiture = (LinearLayout) findViewById(R.id.TitleVoiture);

        lblLogin.setText(ProjetLabo.user.getLogin());

        VoitureLoue voiture = ProjetLabo.user.getVoiture();
        if (voiture != null) {
            //On affiche les infos sur la voiture
            cvVoitureLoue.setVisibility(View.VISIBLE);
            btnLouer.setVisibility(View.GONE);
            voitureName.setText(voiture.getName());

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            lblDateLocation.setText("Loué du " + dateFormat.format(voiture.getStart().getTime()) + " au " + dateFormat.format(voiture.getEnd().getTime()));

            String url = ProjetLabo.BASE_URL + voiture.getPath();
            Picasso.with(ProfileActivity.this).load(url).into(voiturePhoto);

            if (ProjetLabo.NOW().compareTo(ProjetLabo.user.getVoiture().getStart()) < 0) {
                btnRendre.setText("Annuler");
            } else if (ProjetLabo.NOW().compareTo(ProjetLabo.user.getVoiture().getEnd()) > 0) {
                llTitleVoiture.setBackgroundColor(getResources().getColor(R.color.warning));
            }
        } else {
            //On cache la carte et on affiche le btn "Louer"
            cvVoitureLoue.setVisibility(View.GONE);
            btnLouer.setVisibility(View.VISIBLE);
        }

        if (ProjetLabo.user.isFidele()) {
            btnFidele.setText("Mon compte fidélisation");
        } else {
            btnFidele.setText("Devenir fidèle");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btnLouer)
    public void onClickBtnLouer(View view) {
        startActivity(new Intent(ProfileActivity.this, CriteresActivity.class));
    }

    @OnClick(R.id.cvVoitureLoue)
    public void onClickCvVoiture(View view) {
        /*startActivity(new Intent(ProfileActivity.this, InfoVehiculeActivity.class));*/
        Toast.makeText(ProfileActivity.this, "Activité actuellement indisponible", Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.btnFidele)
    public void onClickBtnFidele(View view) {
        startActivity(new Intent(ProfileActivity.this, SoldeFidelisationAcitivity.class));
    }

    private boolean rendreVerifPlain() {
        if (ProjetLabo.user.getVoiture().getFuelQuantity() < 25) {
            new AlertDialog.Builder(ProfileActivity.this)
                    .setTitle("Faites le plein !")
                    .setMessage("Vous devez rendre un véhicule contenant au minimum 25 litres de carburant.")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return false;
        }
        return true;
    }

    @OnClick(R.id.btnRendre)
    public void onClickBtnRendre(View view) {
        if (ProjetLabo.NOW().compareTo(ProjetLabo.user.getVoiture().getStart()) < 0) {
            //Rendre le véhicule avant que la location n'ai commencer.
            new AlertDialog.Builder(this)
                    .setTitle("Annuler votre location")
                    .setMessage("Êtes-vous sûr de vouloir annuler cette location avant la date prévue ?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (ProfileActivity.this.rendreVerifPlain()) {
                                ProjetLabo.user.getVoiture().setRendu(true);
                                ProjetLabo.user.getVoiture().setToApi(ProfileActivity.this);
                                ProjetLabo.user.setVoiture(null);
                                ProjetLabo.user.setToApi(ProfileActivity.this);
                                ProfileActivity.this.recreate();
                            }
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

        } else {
            if (ProjetLabo.NOW().compareTo(ProjetLabo.user.getVoiture().getEnd()) < 0) {
                //Date avant la fin
                new AlertDialog.Builder(this)
                        .setTitle("Rendre ce véhicule")
                        .setMessage("Êtes-vous sûr de vouloir rendre ce véhicule avant la date prévue ?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (ProfileActivity.this.rendreVerifPlain()) {
                                    ProjetLabo.user.getVoiture().setRendu(true);
                                    ProjetLabo.user.getVoiture().setToApi(ProfileActivity.this);
                                    ProjetLabo.user.setVoiture(null);
                                    ProjetLabo.user.setToApi(ProfileActivity.this);
                                    ProfileActivity.this.recreate();
                                }
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            } else {
                if (ProjetLabo.NOW().compareTo(ProjetLabo.user.getVoiture().getEnd()) > 0) {
                    //retard
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    int nbDays = Math.round((ProjetLabo.NOW().getTimeInMillis() - ProjetLabo.user.getVoiture().getEnd().getTimeInMillis()) / (1000 * 60 * 60 * 24));
                    float prixRetard = ProjetLabo.user.getVoiture().getPrice() * nbDays;
                    new AlertDialog.Builder(this)
                            .setTitle("Retard")
                            .setMessage("Cette voiture devait être rendue pour le " + dateFormat.format(ProjetLabo.user.getVoiture().getEnd().getTime()) + ". \n " +
                                    "Veuillez payer la somme de " + prixRetard + " € pour le retard")
                            .setPositiveButton("Payer", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    if (ProfileActivity.this.rendreVerifPlain()) {
                                        ProjetLabo.user.getVoiture().setRendu(true);
                                        ProjetLabo.user.getVoiture().setToApi(ProfileActivity.this);
                                        ProjetLabo.user.setVoiture(null);
                                        ProjetLabo.user.setToApi(ProfileActivity.this);
                                        ProfileActivity.this.recreate();
                                    }
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                } else {
                    if (ProfileActivity.this.rendreVerifPlain()) {
                        ProjetLabo.user.getVoiture().setRendu(true);
                        ProjetLabo.user.getVoiture().setToApi(ProfileActivity.this);
                        ProjetLabo.user.setVoiture(null);
                        ProjetLabo.user.setToApi(ProfileActivity.this);
                        ProfileActivity.this.recreate();
                    }
                }
            }
        }
    }

    @OnClick(R.id.btnPlain)
    public void onClickBtnPlain(final View view) {
        final SeekBar input = new SeekBar(this);
        input.setMax(50);
        input.setProgress((int) Math.floor(ProjetLabo.user.getVoiture().getFuelQuantity()));
        final AlertDialog dialog = new AlertDialog.Builder(ProfileActivity.this)
                .setTitle("Faire le plein")
                .setMessage("Réservoir : " + (int) ProjetLabo.user.getVoiture().getFuelQuantity() + "/50 litres")
                .setView(input)
                .setPositiveButton("Payer", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (input.getProgress() > ProjetLabo.user.getVoiture().getFuelQuantity()) {
                            final float prixFidele = (input.getProgress() - ProjetLabo.user.getVoiture().getFuelQuantity()) * 1 / 10;
                            if (prixFidele < ProjetLabo.user.getCapital()) {
                                new AlertDialog.Builder(ProfileActivity.this)
                                        .setTitle("Payer avec votre compte fidélité")
                                        .setMessage("Vous possédez " + ProjetLabo.user.getCapital() + " points sur votre compte fidélité. Souhaitez-vous payer votre plein (" + prixFidele + " points) avec ces points ?")
                                        .setPositiveButton("Compte fidélité", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                ProjetLabo.user.subCapital(prixFidele);
                                            }
                                        })
                                        .setNegativeButton("Cash", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // do nothing
                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
                                ProjetLabo.user.getVoiture().setFuelQuantity((int) Math.floor(ProjetLabo.user.getVoiture().getFuelQuantity() + input.getProgress()));
                                ProjetLabo.user.setToApi(ProfileActivity.this);
                            }
                        } else {
                            new AlertDialog.Builder(ProfileActivity.this)
                                    .setTitle("Plein négatif")
                                    .setMessage("Vous ne pouvez pas vendre de l'essence, uniquement en acheter.")
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            ProfileActivity.this.onClickBtnPlain(view);
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Do nothing.
                    }
                }).show();

        input.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                dialog.setMessage("Réservoir : " + String.valueOf(progress) + "/50 litres");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }
        });
    }

    @OnClick(R.id.btnProlonger)
    public void onClickBtnProlonger(View view) {
        DialogFragment dateFragment = new DatePickerFragment(ProjetLabo.user.getVoiture().getEnd());
        dateFragment.show(getSupportFragmentManager(), "timePicker");
    }

    @Override
    public void onDatePickerSet(int year, int monthOfYear, int dayOfMonth) {
        final Calendar calProlongation = new GregorianCalendar();
        calProlongation.set(year, monthOfYear, dayOfMonth);
        long prolongation = calProlongation.getTime().getTime();
        if (prolongation > ProjetLabo.user.getVoiture().getEnd().getTime().getTime()) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("idLocation", Integer.toString(ProjetLabo.user.getVoiture().getIdLocation()));
            params.put("end", Long.toString(prolongation));

            String URL = ProjetLabo.API_BASE_URL + "/prolongers.json";

            PostRequest requestAddUser = new PostRequest(URL, params, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    try {
                        JSONObject userJSON = new JSONObject(s);
                        if (userJSON.has("response")) {
                            if (userJSON.getBoolean("response")) {
                                int nbDays = Math.round((calProlongation.getTimeInMillis() - ProjetLabo.user.getVoiture().getEnd().getTimeInMillis()) / (1000 * 60 * 60 * 24));
                                //1 point pas semaine de location
                                ProjetLabo.user.addCapital(((float) 1 / 7) * (float) nbDays);
                                ProjetLabo.user.getVoiture().setEnd(calProlongation);
                                ProjetLabo.user.setToApi(ProfileActivity.this);
                                ProfileActivity.this.recreate();
                            } else {
                                Toast.makeText(ProfileActivity.this, "Ce véhicule ne peut pas être prolongé jusqu'à la date voulue !", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(ProfileActivity.this, "Il est actuellement impossible de prolonger votre véhicule", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(ProfileActivity.this, "Il est actuellement impossible de prolonger votre véhicule", Toast.LENGTH_LONG).show();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(ProfileActivity.this, "Une erreur réseau est survenue !", Toast.LENGTH_LONG).show();
                        }
                    });
            RequestQueue queue = Volley.newRequestQueue(ProfileActivity.this, new OkHttpStack());
            queue.add(requestAddUser);
        } else {
            Toast.makeText(ProfileActivity.this, "Votre nouvelle date de fin de location doit être supérieure a l'ancienne.", Toast.LENGTH_LONG).show();
        }
    }
}
