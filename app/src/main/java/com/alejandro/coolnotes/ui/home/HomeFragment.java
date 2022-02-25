package com.alejandro.coolnotes.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.alejandro.coolnotes.MainActivity;
import com.alejandro.coolnotes.R;
import com.alejandro.coolnotes.databinding.FragmentHomeBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class HomeFragment extends Fragment {

    private final int RC_SIGN_IN = 1;
    private FragmentHomeBinding binding;
    private GoogleSignInOptions gso;
    private GoogleSignInClient mGoogleSignInClient;
    private SignInButton signInButton;
    private LinearLayout linearLogged;
    private LinearLayout linearNotLogged;
    private TextView tvLogged;
    private Button loadDataCloud;
    private StorageReference stoRef;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        MainActivity main = (MainActivity) getActivity();
        gso = main.getGso();
        mGoogleSignInClient = main.getmGoogleSignInClient();
        linearLogged = getView().findViewById(R.id.loggin_logged);
        linearNotLogged = getView().findViewById(R.id.loggin_not_logged);
        stoRef = FirebaseStorage.getInstance().getReference();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());
        updateUI(account);
    }

    private void updateUI(GoogleSignInAccount account) {
        if (account != null) {
            linearNotLogged.setVisibility(View.GONE);
            linearLogged.setVisibility(View.VISIBLE);

            tvLogged = getView().findViewById(R.id.account_logged);
            tvLogged.setText("You're logged with " + account.getEmail());

            loadDataCloud = getView().findViewById(R.id.load_cloud_button);
            loadDataCloud.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String origin = "vaults/vault" + account.getId() + ".dat";
                    StorageReference islandRef = stoRef.child(origin);

                    File fileName = new File(getActivity().getFilesDir(), "/" + "vault.dat");

                    islandRef.getFile(fileName).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getContext(), "load vault from cloud success", Toast.LENGTH_SHORT).show();

                            Navigation.findNavController(getView()).navigate(R.id.navigation_dashboard);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            System.out.println("load vault from cloud fail");
                        }
                    });
                }
            });

        } else {
            linearLogged.setVisibility(View.GONE);
            signInButton = binding.getRoot().findViewById(R.id.sign_in_button);
            signInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    signIn();
                }
            });
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            updateUI(null);
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
}
