package com.alejandro.coolnotes.ui.notifications;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.alejandro.coolnotes.MainActivity;
import com.alejandro.coolnotes.PersistenceVault;
import com.alejandro.coolnotes.databinding.FragmentNotificationsBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private Activity main;
    private PersistenceVault vault;
    private RecyclerView recycler;
    private FloatingActionButton fab;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //Persistence
        main = getActivity();
        vault = new PersistenceVault(main.getFilesDir());

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        setUpRecycler();
        configureButton();
    }

    private void configureButton() {
    }

    private void setUpRecycler() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}