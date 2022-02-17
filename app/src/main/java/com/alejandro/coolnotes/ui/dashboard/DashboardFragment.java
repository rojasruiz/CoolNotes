package com.alejandro.coolnotes.ui.dashboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alejandro.coolnotes.AdapterNotes;
import com.alejandro.coolnotes.Note;
import com.alejandro.coolnotes.NoteActivity;
import com.alejandro.coolnotes.PersistenceVault;
import com.alejandro.coolnotes.R;
import com.alejandro.coolnotes.databinding.FragmentDashboardBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private View view;
    private Activity main;
    private RecyclerView recycler;
    private PersistenceVault vault;
    private ArrayList<Note> notesList;
    private FloatingActionButton fab;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        vault = new PersistenceVault();
        notesList = vault.getNotesList();
        main = getActivity();

        notesList = new ArrayList<Note>(); //TODO Persistence
        setActivityResultLauncher();

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        view = getView();
        setUpRecycler();
        configureButton();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void setActivityResultLauncher() {
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent intent = result.getData();
                            notesList = (ArrayList<Note>) intent.getExtras().get("notesList");
                            setUpRecycler();
                        }
                    }
                }
        );


    }

    private void configureButton() {
        fab = view.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Note newNote = new Note();
                notesList.add(newNote);
                Intent intent = new Intent(main, NoteActivity.class);
                intent.putExtra("notesList",notesList);
                intent.putExtra("newNote", true);
                activityResultLauncher.launch(intent);
            }
        });
    }

    private void setUpRecycler() {

        //TODO change input list
        recycler = view.findViewById(R.id.recycler_notes);
        GridLayoutManager mLayout = new GridLayoutManager(main, 2, GridLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(mLayout);
        RecyclerView.Adapter adapter = new AdapterNotes(main, notesList);
        recycler.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}