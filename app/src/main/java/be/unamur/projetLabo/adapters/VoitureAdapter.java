package be.unamur.projetLabo.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import junit.framework.Test;

import java.util.List;

import be.unamur.projetLabo.ProjetLabo;
import be.unamur.projetLabo.R;
import be.unamur.projetLabo.activities.ListeVoituresActivity;
import be.unamur.projetLabo.activities.VoitureActivity;
import be.unamur.projetLabo.classes.Voiture;

public class VoitureAdapter extends RecyclerView.Adapter<VoitureAdapter.PersonViewHolder> {
    Context context;

    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView voitureName;
        TextView voitureNbSeat;
        TextView voitureNbDoor;
        TextView voiturePrix;
        TextView voiturePlusDetails;
        ImageView voiturePhoto;
        View view;

        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            voitureName = (TextView) itemView.findViewById(R.id.voiture_name);
            voitureNbSeat = (TextView) itemView.findViewById(R.id.voiture_nbSeat);
            voitureNbDoor = (TextView) itemView.findViewById(R.id.voiture_nbDoor);
            voiturePhoto = (ImageView) itemView.findViewById(R.id.voiture_photo);
            voiturePrix = (TextView) itemView.findViewById(R.id.voiture_prix);
            voiturePlusDetails = (TextView) itemView.findViewById(R.id.voiture_plusDetails);
            view = itemView;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    // item clicked
                }
            });
        }
    }

    List<Voiture> voituresList;

    public VoitureAdapter(Context context, List<Voiture> voit){
        this.voituresList = voit;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return voituresList.size();
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.voitures_list_item, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final PersonViewHolder personViewHolder, int i) {
        final Voiture itemVoiture = voituresList.get(i);
        personViewHolder.voitureName.setText(itemVoiture.getName());
        personViewHolder.voitureNbSeat.setText(Integer.toString(itemVoiture.getNbSeat()));
        personViewHolder.voitureNbDoor.setText(Integer.toString(itemVoiture.getNbDoor()));
        personViewHolder.voiturePrix.setText(itemVoiture.getPrice() + "€");
        personViewHolder.voiturePlusDetails.setText("Plus de détails...");

        String url = ProjetLabo.BASE_URL + itemVoiture.getPath();
        Picasso.with(context).load(url).into(personViewHolder.voiturePhoto);

        personViewHolder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VoitureActivity.class);
                intent.putExtra("voiture", itemVoiture);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}