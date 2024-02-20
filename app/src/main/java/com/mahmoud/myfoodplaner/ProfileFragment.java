package com.mahmoud.myfoodplaner;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.mahmoud.myfoodplaner.dbmodels.favourate.Favourate;
import com.mahmoud.myfoodplaner.dbmodels.favourate.FavourateLocalDataSource;
import com.mahmoud.myfoodplaner.dbmodels.table.TableLocalDataSource;
import com.mahmoud.myfoodplaner.dbmodels.table.table;
import com.mahmoud.myfoodplaner.login.SignFragment;
import com.mahmoud.myfoodplaner.model.FireBaseModel;

import org.reactivestreams.Subscription;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.FlowableSubscriber;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ProfileFragment extends Fragment {
    List<table>tablesList;
    List<Favourate>favouratesList;
    public ProfileFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView profilePhotoImageView = view.findViewById(R.id.profilePhotoImageView);
        TextView emailTextView = view.findViewById(R.id.emailTextView);
        TextView nameTextView = view.findViewById(R.id.nameTextView);
        Button logoutButton = view.findViewById(R.id.logoutButton);
        Button backupButton = view.findViewById(R.id.backupButton);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", MODE_PRIVATE);
        emailTextView.setText(sharedPreferences.getString("email",""));
        nameTextView.setText(sharedPreferences.getString("name",""));
        Glide.with(getActivity()).load(sharedPreferences.getString("photo","")).into(profilePhotoImageView);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken("899246879267-4pd3p2ug0k0r82q080pkf94e2uo4dvd6.apps.googleusercontent.com")
                        .requestEmail()
                        .build();

               GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);
               mGoogleSignInClient.signOut();
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user",MODE_PRIVATE);
                sharedPreferences.edit().clear().apply();
TableLocalDataSource.getInstance(getActivity()).deleteAllTables().subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread()).subscribe();
FavourateLocalDataSource.getInstance(getActivity()).deleteAllFavourate().subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread()).subscribe();
                NavHostFragment.findNavController(ProfileFragment.this).navigate(R.id.action_profileFragment_to_signFragment);//NavHostFragment

                // Sign out from FirebaseAuth
            }
        });
backupButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

        TableLocalDataSource.getInstance(getActivity()).getAllTables().subscribeOn(Schedulers.io())
                .take(1).observeOn(AndroidSchedulers.mainThread()).subscribe(new FlowableSubscriber<List<table>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Subscription s) {
                        s.request(1);
                    }

                    @Override
                    public void onNext(List<table> tables) {
                        tablesList = tables;
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {
                        FavourateLocalDataSource.getInstance(getActivity()).getAllFavourate().subscribeOn(Schedulers.io())
                                .take(1).observeOn(AndroidSchedulers.mainThread()).subscribe(new FlowableSubscriber<List<Favourate>>() {

                                    @Override
                                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Subscription s) {
                                        s.request(1);
                                    }

                                    @Override
                                    public void onNext(List<Favourate> favourates) {
                                        favouratesList = favourates;
                                    }

                                    @Override
                                    public void onError(Throwable t) {

                                    }

                                    @Override
                                    public void onComplete() {
                                        FireBaseModel fireBaseModel = new FireBaseModel(tablesList,favouratesList);
                                        Gson gson = new Gson();
                                        String json = gson.toJson(fireBaseModel);
                                        FirebaseDatabase database = FirebaseDatabase.getInstance("https://foodatuh-default-rtdb.firebaseio.com/");
                                        DatabaseReference myRef = database.getReference(
                                                getActivity().getSharedPreferences("user", MODE_PRIVATE).getString("id",""));
                                        myRef.setValue(json);
                                        Snackbar.make(view,"Backup Done",Snackbar.LENGTH_LONG).show();

                                    }
                                });
                    }
                });
    }
});
        // Write a message to the database

    }
}
//        FirebaseDatabase database = FirebaseDatabase.getInstance("https://foodatuh-default-rtdb.firebaseio.com/");
//        DatabaseReference myRef = database.getReference(
//                getActivity().getSharedPreferences("user", Context.MODE_PRIVATE).getString("id",""));
//        Log.i("fireee",myRef.toString());
//
//        myRef.setValue("Hello, World!");