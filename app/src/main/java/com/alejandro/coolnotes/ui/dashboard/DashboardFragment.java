package com.alejandro.coolnotes.ui.dashboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
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
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.alejandro.coolnotes.AdapterNotes;
import com.alejandro.coolnotes.Note;
import com.alejandro.coolnotes.NoteActivity;
import com.alejandro.coolnotes.PersistenceVault;
import com.alejandro.coolnotes.R;
import com.alejandro.coolnotes.databinding.FragmentDashboardBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.LandingAnimator;

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

        //Persistence
        main = getActivity();
        vault = new PersistenceVault(main.getFilesDir());
        notesList = vault.getNotesList();

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
                            vault.setNotesList(notesList);
                            vault.saveVaultToFile(main.getFilesDir());
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
        StaggeredGridLayoutManager mLayout = new StaggeredGridLayoutManager(2,1);
        recycler.setLayoutManager(mLayout);
        RecyclerView.Adapter adapter = new AdapterNotes(main, notesList, this);
        recycler.setAdapter(adapter);
        recycler.setItemAnimator(new LandingAnimator(new OvershootInterpolator(2.0f)));
        recycler.getItemAnimator().setRemoveDuration(220);
    }

    public void openNote(int notePos){
        Intent intent = new Intent(main, NoteActivity.class);
        intent.putExtra("notesList",notesList);
        intent.putExtra("newNote", false);
        intent.putExtra("notePos",notePos);
        activityResultLauncher.launch(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}