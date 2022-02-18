package com.alejandro.coolnotes;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alejandro.coolnotes.ui.dashboard.DashboardFragment;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdapterNotes extends RecyclerView.Adapter<AdapterNotes.ViewHolder> {
    private final ArrayList<Note> mData;
    private final LayoutInflater mInflater;
    private final Context context;
    private final DashboardFragment fragment;

    private int pos = 0;

    public AdapterNotes(Context context, ArrayList<Note> data, DashboardFragment fragment) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.fragment = fragment;
    }

    public int getPos() {
        return this.pos;
    }

    public void decrementarPos() {
        this.pos--;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_notes, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Note note = mData.get(position);
        holder.title.setText(note.getTittle());
        holder.description.setText(note.getDescription());

        holder.delRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mData.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(0, mData.size());
                decrementarPos();
                PersistenceVault vault = fragment.getVault();
                vault.setNotesList(mData);
                vault.saveVaultToFile(fragment.getActivity().getFilesDir());
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.openNote(position);
            }
        });

        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.openNote(position);
            }
        });

        holder.description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.openNote(position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView description;
        Context context;
        ImageButton delRow;

        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.note_tittle_row);
            description = itemView.findViewById(R.id.note_description_row);
            delRow = itemView.findViewById(R.id.delRowButton);
            context = title.getContext();
        }
    }
}


