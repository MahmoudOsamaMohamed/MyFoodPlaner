package com.mahmoud.myfoodplaner.login;


import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.mahmoud.myfoodplaner.R;
import com.mahmoud.myfoodplaner.dbmodels.User;
import com.mahmoud.myfoodplaner.dbmodels.favourate.Favourate;
import com.mahmoud.myfoodplaner.dbmodels.favourate.FavourateLocalDataSource;
import com.mahmoud.myfoodplaner.dbmodels.table.TableLocalDataSource;
import com.mahmoud.myfoodplaner.dbmodels.table.table;
import com.mahmoud.myfoodplaner.model.FireBaseModel;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class SignFragment extends Fragment {

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;

    private Button signInButton;
    private Button continueAsGuest;
    private TextView result;
   SharedPreferences    sharedPreferences;

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    boolean found;
    public SignFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: ");
        User user = new User();
        user.id="1";


        sharedPreferences = getActivity().getSharedPreferences("user",MODE_PRIVATE);
if(sharedPreferences.getString("email",null)!=null){
    NavHostFragment.findNavController(SignFragment.this).navigate(R.id.action_signFragment_to_homeFragment);
}

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign, container, false);
        BottomNavigationView navBar = getActivity().findViewById(R.id.bottom_navigation_view);
        Log.i("totot",navBar.getVisibility()+"");
        navBar.setVisibility(View.GONE);
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Your custom back button behavior here
                // If you want to allow normal back button behavior, call the following line:
                // super.handleOnBackPressed();
            }
        });
        signInButton = view.findViewById(R.id.google_btn);
        continueAsGuest = view.findViewById(R.id.guest_btn);
        continueAsGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SignFragment.this).navigate(R.id.action_signFragment_to_homeFragment);
            }
        });
//        result = view.findViewById(R.id.result);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("899246879267-4pd3p2ug0k0r82q080pkf94e2uo4dvd6.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);
        mAuth = FirebaseAuth.getInstance();

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }
                    }
                });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Log.i("sign in",user.getEmail()+" "+user.getDisplayName()+" "+user.getPhotoUrl()+" 1");


sharedPreferences.edit().putString("email",user.getEmail()).apply();
sharedPreferences.edit().putString("name",user.getDisplayName()).apply();
sharedPreferences.edit().putString("id",user.getUid()).apply();
sharedPreferences.edit().putString("photo",user.getPhotoUrl().toString()).apply();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference(user.getUid());
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String json = dataSnapshot.getValue(String.class);
                        Gson gson = new Gson();
                        FireBaseModel fireBaseModel = gson.fromJson(json, FireBaseModel.class);

                        // Now 'fireBaseModel' contains the data from the database
                        // You can use it as needed

                        List<table> tablesList = fireBaseModel.getPlans();
                        List<Favourate> favouratesList = fireBaseModel.getFavourates();
                        for(table table:tablesList){
                            TableLocalDataSource.getInstance(getActivity()).insertTable(table)
                                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();

                        }
                        for (Favourate favourate:favouratesList){
                            FavourateLocalDataSource.getInstance(getActivity()).insertFavourate(favourate)
                                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
                        }

                        // Perform actions with the retrieved data
                        // ...
                    } else {
                        // The data does not exist at the specified location
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e(TAG, "Error reading from the database", databaseError.toException());
                }
            });

            Log.i("prefs",sharedPreferences.toString());
            NavHostFragment.findNavController(this).navigate(R.id.action_signFragment_to_homeFragment);

        }
    }

}

