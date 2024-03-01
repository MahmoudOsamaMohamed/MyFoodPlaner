package com.mahmoud.myfoodplaner.details;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.mahmoud.myfoodplaner.R;
import com.mahmoud.myfoodplaner.dbmodels.favourate.Favourate;
import com.mahmoud.myfoodplaner.dbmodels.favourate.FavourateLocalDataSource;
import com.mahmoud.myfoodplaner.dbmodels.table.TableLocalDataSource;
import com.mahmoud.myfoodplaner.dbmodels.table.table;
import com.mahmoud.myfoodplaner.model.MealsRemoteDataSourceImpl;
import com.mahmoud.myfoodplaner.model.pojos.LongMeal;
import com.mahmoud.myfoodplaner.plan.DaySelectionDialogFragment;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.reactivestreams.Subscription;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.FlowableSubscriber;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class MealDetailsFragment extends Fragment implements DetailsView, DaySelectionDialogFragment.DaySelectionListener {
    YouTubePlayerView youtube_player_view;
    Button favorite_btn, add_to_plan_btn;

    ImageView meal_image;
    TextView meal_name, meal_category, meal_area, meal_description;
    RecyclerView stepsRecyclerView;
    TextView stepsTv;
    RecyclerView ingredientsRecyclerView;
    DetailsPresenter detailsPresenter;
    LongMeal longMealGlobal;

    public MealDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detailsPresenter = new DetailsPresenter(this, MealsRemoteDataSourceImpl.getInstance(), FavourateLocalDataSource.getInstance(getActivity()));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meal_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        youtube_player_view = view.findViewById(R.id.youtube_player_view);
        detailsPresenter.getMealDetails(MealDetailsFragmentArgs.fromBundle(getArguments()).getMealId());
        meal_image = view.findViewById(R.id.meal_img);
        meal_name = view.findViewById(R.id.meal_name);
        meal_category = view.findViewById(R.id.meal_category);
        meal_area = view.findViewById(R.id.meal_area);
        meal_description = view.findViewById(R.id.meal_description);
        stepsRecyclerView = view.findViewById(R.id.steps_recycler_view);
        stepsTv = view.findViewById(R.id.steps_tv);
        ingredientsRecyclerView = view.findViewById(R.id.ingredinets_recycler_view);
        favorite_btn = view.findViewById(R.id.add_to_favorite);
        add_to_plan_btn = view.findViewById(R.id.add_to_plan);
        add_to_plan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(longMealGlobal!=null){
                if (getActivity().getSharedPreferences("user", MODE_PRIVATE).getString("email", null) != null) {
                    if (getActivity().getSharedPreferences("weak_day", 0).getString("day", null) != null) {
                        table t = new table();

                        TableLocalDataSource.getInstance(getActivity()).
                                getAllTables().subscribeOn(Schedulers.io())
                                .take(1).observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new FlowableSubscriber<List<table>>() {
                                    @Override
                                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Subscription s) {
                                        s.request(1);
                                    }

                                    @Override
                                    public void onNext(List<table> tables) {
                                        t.id = String.valueOf(tables.size() + 1);
                                    }

                                    @Override
                                    public void onError(Throwable t) {

                                    }

                                    @Override
                                    public void onComplete() {
                                        t.mealName = longMealGlobal.getStrMeal();
                                        t.img_url = longMealGlobal.getStrMealThumb();
                                        t.Day = getActivity().getSharedPreferences("weak_day", 0).getString("day", null);
                                        TableLocalDataSource.getInstance(getActivity()).insertTable(t).subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread()).subscribe(new CompletableObserver() {

                                                    @Override
                                                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                                                    }

                                                    @Override
                                                    public void onComplete() {
                                                        getActivity().getSharedPreferences("weak_day", 0).edit().clear().apply();
                                                        NavHostFragment.findNavController(MealDetailsFragment.this).navigate(R.id.action_mealDetailsFragment_to_planFragment);
                                                    }

                                                    @Override
                                                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                                                    }
                                                });
                                    }
                                });
                    } else {
                        showDialog();
//                        DaySelectionDialogFragment dialogFragment = new DaySelectionDialogFragment();
//                        dialogFragment.setDaySelectionListener(MealDetailsFragment.this);
//                        dialogFragment.show(getActivity().getSupportFragmentManager(), "daySelectionDialog");
                    }


                } else {
                    Toast.makeText(getContext(), "Please Login First", Toast.LENGTH_LONG).show();
                }

            }}
        });
        favorite_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity().getSharedPreferences("user", MODE_PRIVATE).getString("email", null) != null){
                if(longMealGlobal!=null){
                Favourate favourat = new Favourate();
                favourat.img_url = longMealGlobal.getStrMealThumb();
                favourat.mealName = longMealGlobal.getStrMeal();
                favourat.id = "num";
                FavourateLocalDataSource.getInstance(getContext()).getAllFavourate().subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).take(1).subscribe(new FlowableSubscriber<List<Favourate>>() {
                            @Override
                            public void onSubscribe(Subscription s) {
                                s.request(1);
                            }

                            @Override
                            public void onNext(List<Favourate> favourates) {
                                favourat.id = String.valueOf(favourates.size() + 1);

                            }

                            @Override
                            public void onError(Throwable t) {
Snackbar.make(view, "Error " + t.getMessage(), Snackbar.LENGTH_LONG).show();
                            }

                            @Override
                            public void onComplete() {
                                detailsPresenter.addToFavourites(favourat);
                            }
                        });
            }}
            else{
                    Toast.makeText(getActivity(), "Please login first", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void showMealDetails(LongMeal longMeal) {
        longMealGlobal = longMeal;
        if (longMeal.getStrMeal() != null)
            meal_name.setText(longMeal.getStrMeal());
        if (longMeal.getStrCategory() != null)
            meal_category.setText(longMeal.getStrCategory());
        if (longMeal.getStrArea() != null)
            meal_area.setText(longMeal.getStrArea());

        if (longMeal.getStrMealThumb() != null)
            Glide.with(getContext()).load(longMeal.getStrMealThumb()).into(meal_image);
        if (longMeal.getStrInstructions().contains("\n")) {
            meal_description.setVisibility(View.GONE);
            stepsTv.setVisibility(View.VISIBLE);
            stepsRecyclerView.setVisibility(View.VISIBLE);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            stepsRecyclerView.setLayoutManager(layoutManager);
            MealStepsAdapter mealStepsAdapter = new MealStepsAdapter(Arrays.asList(
                    (longMeal.getStrInstructions().split("\r\n"))).stream().filter(x -> !x.isEmpty()).collect(Collectors.toList()));
            stepsRecyclerView.setAdapter(mealStepsAdapter);
        } else {
            meal_description.setText(longMeal.getStrInstructions());
            meal_description.setVisibility(View.VISIBLE);
            stepsTv.setVisibility(View.GONE);
            stepsRecyclerView.setVisibility(View.GONE);

        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        ingredientsRecyclerView.setLayoutManager(layoutManager);
        Log.i("ingredient", longMeal.getStrIngredient().toString());
        IngredientPairsAdapter ingredientPairsAdapter = new IngredientPairsAdapter(longMeal.getStrIngredient());
        ingredientsRecyclerView.setAdapter(ingredientPairsAdapter);
        youtube_player_view.initialize(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                super.onReady(youTubePlayer);
                try {
                    youTubePlayer.loadVideo(longMeal.getStrYoutube().substring(32), 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void showMealDetailsError(String error) {

    }

    @Override
    public void onDaySelected(String selectedDay) {
        Toast.makeText(getActivity(), "Selected day: " + selectedDay, Toast.LENGTH_SHORT).show();
        table t = new table();
        t.id = "number";

        TableLocalDataSource.getInstance(getActivity()).getAllTables().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).take(1).subscribe(new FlowableSubscriber<List<table>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Subscription s) {
                        s.request(1);

                        Log.i("insert to paln i", "onSubscribe");
                    }

                    @Override
                    public void onNext(List<table> tables) {
                        Log.i("insert to paln i", tables.size() + "");
                        t.id = String.valueOf(tables.size() + 1);
                        t.Day = selectedDay;
                        t.mealName = longMealGlobal.getStrMeal();
                        t.img_url = longMealGlobal.getStrMealThumb();


                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.i("insert to paln i", "onError " + t.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.i("insert to paln i", "onComplete  " + t.id);
                        TableLocalDataSource.getInstance(getActivity()).insertTable(t)
                                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CompletableObserver() {
                                    @Override
                                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                                        Log.i("insert to paln", "onSubscribe");
                                    }

                                    @Override
                                    public void onComplete() {
                                        Log.i("insert to paln ", "onComplete C");
                                    }

                                    @Override
                                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                                        Log.i("insert to paln", "onError " + e.getMessage());
                                    }
                                });

                    }
                });


    }

    void showDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetlayoutday);
        LinearLayout layout_saturday = dialog.findViewById(R.id.layout_saterday);
        LinearLayout layout_sunday = dialog.findViewById(R.id.layout_sunday);
        LinearLayout layout_monday = dialog.findViewById(R.id.layout_monday);
        LinearLayout layout_tuesday = dialog.findViewById(R.id.layout_thuesday);
        LinearLayout layout_wednesday = dialog.findViewById(R.id.layout_wednesday);
        LinearLayout layout_thursday = dialog.findViewById(R.id.layout_thursday);
        LinearLayout layout_friday = dialog.findViewById(R.id.layout_friday);
        layout_saturday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                TableLocalDataSource.getInstance(getActivity()).getAllTables().subscribeOn(Schedulers.io())
                        .take(1).observeOn(AndroidSchedulers.mainThread()).subscribe(new FlowableSubscriber<List<table>>() {

                            @Override
                            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Subscription s) {
                                s.request(1);
                            }

                            @Override
                            public void onNext(List<table> tables) {
                                table t = new table();
                                t.id = String.valueOf(tables.size() + 1);
                                t.mealName = longMealGlobal.getStrMeal();
                                t.img_url = longMealGlobal.getStrMealThumb();
                                t.Day = getResources().getStringArray(R.array.days_of_week)[0];
                                TableLocalDataSource.getInstance(getActivity()).insertTable(t).
                                        subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CompletableObserver() {

                                            @Override
                                            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                                            }

                                            @Override
                                            public void onComplete() {
                                                Snackbar.make(getView(), "Added to day " + getResources().getStringArray(R.array.days_of_week)[0], Snackbar.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                                            }
                                        });

                            }

                            @Override
                            public void onError(Throwable t) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
        });
        layout_sunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                              TableLocalDataSource.getInstance(getActivity()).getAllTables().subscribeOn(Schedulers.io())
                                      .take(1).observeOn(AndroidSchedulers.mainThread()).subscribe(new FlowableSubscriber<List<table>>() {

                                          @Override
                                          public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Subscription s) {
                                              s.request(1);
                                          }

                                          @Override
                                          public void onNext(List<table> tables) {
                                              table t = new table();
                                              t.id = String.valueOf(tables.size() + 1);
                                              t.mealName = longMealGlobal.getStrMeal();
                                              t.img_url = longMealGlobal.getStrMealThumb();
                                              t.Day = getResources().getStringArray(R.array.days_of_week)[1];
                                              TableLocalDataSource.getInstance(getActivity()).insertTable(t).
                                                      subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CompletableObserver() {

                                                          @Override
                                                          public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                                                          }

                                                          @Override
                                                          public void onComplete() {
                                                              Snackbar.make(getView(), "Added to day " + getResources().getStringArray(R.array.days_of_week)[1], Snackbar.LENGTH_SHORT).show();
                                                          }

                                                          @Override
                                                          public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                                                          }
                                                      });

                                          }

                                          @Override
                                          public void onError(Throwable t) {

                                          }

                                          @Override
                                          public void onComplete() {

                                          }
                                      });
                            }




        });
        layout_monday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                TableLocalDataSource.getInstance(getActivity()).getAllTables().subscribeOn(Schedulers.io())
                        .take(1).observeOn(AndroidSchedulers.mainThread()).subscribe(new FlowableSubscriber<List<table>>() {

                            @Override
                            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Subscription s) {
                                s.request(1);
                            }

                            @Override
                            public void onNext(List<table> tables) {
                                table t = new table();
                                t.id = String.valueOf(tables.size() + 1);
                                t.mealName = longMealGlobal.getStrMeal();
                                t.img_url = longMealGlobal.getStrMealThumb();
                                t.Day = getResources().getStringArray(R.array.days_of_week)[2];
                                TableLocalDataSource.getInstance(getActivity()).insertTable(t).
                                        subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CompletableObserver() {

                                            @Override
                                            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                                            }

                                            @Override
                                            public void onComplete() {
                                                Snackbar.make(getView(), "Added to day " + getResources().getStringArray(R.array.days_of_week)[2], Snackbar.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                                            }
                                        });

                            }

                            @Override
                            public void onError(Throwable t) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
        });
        layout_tuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                TableLocalDataSource.getInstance(getActivity()).getAllTables().subscribeOn(Schedulers.io())
                        .take(1).observeOn(AndroidSchedulers.mainThread()).subscribe(new FlowableSubscriber<List<table>>() {

                            @Override
                            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Subscription s) {
                                s.request(1);
                            }

                            @Override
                            public void onNext(List<table> tables) {
                                table t = new table();
                                t.id = String.valueOf(tables.size() + 1);
                                t.mealName = longMealGlobal.getStrMeal();
                                t.img_url = longMealGlobal.getStrMealThumb();
                                t.Day = getResources().getStringArray(R.array.days_of_week)[3];
                                TableLocalDataSource.getInstance(getActivity()).insertTable(t).
                                        subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CompletableObserver() {

                                            @Override
                                            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                                            }

                                            @Override
                                            public void onComplete() {
                                                Snackbar.make(getView(), "Added to day " + getResources().getStringArray(R.array.days_of_week)[3], Snackbar.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                                            }
                                        });

                            }

                            @Override
                            public void onError(Throwable t) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
    });
        layout_wednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                TableLocalDataSource.getInstance(getActivity()).getAllTables().subscribeOn(Schedulers.io())
                        .take(1).observeOn(AndroidSchedulers.mainThread()).subscribe(new FlowableSubscriber<List<table>>() {

                            @Override
                            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Subscription s) {
                                s.request(1);
                            }

                            @Override
                            public void onNext(List<table> tables) {
                                table t = new table();
                                t.id = String.valueOf(tables.size() + 1);
                                t.mealName = longMealGlobal.getStrMeal();
                                t.img_url = longMealGlobal.getStrMealThumb();
                                t.Day = getResources().getStringArray(R.array.days_of_week)[4];
                                TableLocalDataSource.getInstance(getActivity()).insertTable(t).
                                        subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CompletableObserver() {

                                            @Override
                                            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                                            }

                                            @Override
                                            public void onComplete() {
                                                Snackbar.make(getView(), "Added to day " + getResources().getStringArray(R.array.days_of_week)[4], Snackbar.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                                            }
                                        });

                            }

                            @Override
                            public void onError(Throwable t) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
        });
        layout_thursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                TableLocalDataSource.getInstance(getActivity()).getAllTables().subscribeOn(Schedulers.io())
                        .take(1).observeOn(AndroidSchedulers.mainThread()).subscribe(new FlowableSubscriber<List<table>>() {

                            @Override
                            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Subscription s) {
                                s.request(1);
                            }

                            @Override
                            public void onNext(List<table> tables) {
                                table t = new table();
                                t.id = String.valueOf(tables.size() + 1);
                                t.mealName = longMealGlobal.getStrMeal();
                                t.img_url = longMealGlobal.getStrMealThumb();
                                t.Day = getResources().getStringArray(R.array.days_of_week)[5];
                                TableLocalDataSource.getInstance(getActivity()).insertTable(t).
                                        subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CompletableObserver() {

                                            @Override
                                            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                                            }

                                            @Override
                                            public void onComplete() {
                                                Snackbar.make(getView(), "Added to day " + getResources().getStringArray(R.array.days_of_week)[5], Snackbar.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                                            }
                                        });

                            }

                            @Override
                            public void onError(Throwable t) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
        });
        layout_friday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                TableLocalDataSource.getInstance(getActivity()).getAllTables().subscribeOn(Schedulers.io())
                        .take(1).observeOn(AndroidSchedulers.mainThread()).subscribe(new FlowableSubscriber<List<table>>() {

                            @Override
                            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Subscription s) {
                                s.request(1);
                            }

                            @Override
                            public void onNext(List<table> tables) {
                                table t = new table();
                                t.id = String.valueOf(tables.size() + 1);
                                t.mealName = longMealGlobal.getStrMeal();
                                t.img_url = longMealGlobal.getStrMealThumb();
                                t.Day = getResources().getStringArray(R.array.days_of_week)[6];
                                TableLocalDataSource.getInstance(getActivity()).insertTable(t).
                                        subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CompletableObserver() {

                                            @Override
                                            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                                            }

                                            @Override
                                            public void onComplete() {
                                                Snackbar.make(getView(), "Added to day " + getResources().getStringArray(R.array.days_of_week)[6], Snackbar.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                                            }
                                        });

                            }

                            @Override
                            public void onError(Throwable t) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        }
        public View getView2() {
            return getView();
        }
}