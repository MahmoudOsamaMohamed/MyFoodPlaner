package com.mahmoud.myfoodplaner.model.pojos;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class LongMeal {
    private String idMeal;
    private String strMeal;
    private Object strDrinkAlternate;
    private String strCategory;
    private String strArea;

    public String getIdMeal() {
        return idMeal;
    }

    public String getStrMeal() {
        return strMeal;
    }

    public String getStrCategory() {
        return strCategory;
    }

    public String getStrArea() {
        return strArea;
    }

    public String getStrInstructions() {
        return strInstructions;
    }

    public String getStrMealThumb() {
        return strMealThumb;
    }

    public String getStrTags() {
        return strTags;
    }

    public String getStrYoutube() {
        return strYoutube;
    }
    public Pair<List<String>, List<String>> getStrIngredient() {
        ArrayList<String> ingredients = new ArrayList<>();
        ArrayList<String> measures = new ArrayList<>();
        if(strIngredient1 != null&&strIngredient1.length()>0){
            ingredients.add(strIngredient1);
            measures.add(strMeasure1);
        }
        if(strIngredient2 != null&&strIngredient2.length()>0){
            ingredients.add(strIngredient2);
            measures.add(strMeasure2);
        }
        if(strIngredient3 != null&&strIngredient3.length()>0){
            ingredients.add(strIngredient3);
            measures.add(strMeasure3);
        }
        if(strIngredient4 != null&&strIngredient4.length()>0){
            ingredients.add(strIngredient4);
            measures.add(strMeasure4);
        }
        if(strIngredient5 != null&&strIngredient5.length()>0){
            ingredients.add(strIngredient5);
            measures.add(strMeasure5);
        }
        if(strIngredient6 != null&&strIngredient6.length()>0){
            ingredients.add(strIngredient6);
            measures.add(strMeasure6);
        }
        if(strIngredient7 != null&&strIngredient7.length()>0){
            ingredients.add(strIngredient7);
            measures.add(strMeasure7);
        }
        if(strIngredient8 != null&&strIngredient8.length()>0){
            ingredients.add(strIngredient8);
            measures.add(strMeasure8);
        }
        if(strIngredient9 != null&&strIngredient9.length()>0){
            ingredients.add(strIngredient9);
            measures.add(strMeasure9);
        }
        if(strIngredient10 != null&&strIngredient10.length()>0){
            ingredients.add(strIngredient10);
            measures.add(strMeasure10);
        }
        if(strIngredient11 != null&&strIngredient11.length()>0){
            ingredients.add(strIngredient11);
            measures.add(strMeasure11);
        }
        if(strIngredient12 != null&&strIngredient12.length()>0){
            ingredients.add(strIngredient12);
            measures.add(strMeasure12);
        }
        if(strIngredient13 != null&&strIngredient13.length()>0){
            ingredients.add(strIngredient13);
            measures.add(strMeasure13);
        }
        if(strIngredient14 != null&&strIngredient14.length()>0){
            ingredients.add(strIngredient14);
            measures.add(strMeasure14);
        }
        if(strIngredient15 != null&&strIngredient15.length()>0){
            ingredients.add(strIngredient15);
            measures.add(strMeasure15);
        }
        if(strIngredient16 != null&&strIngredient16.length()>0){
            ingredients.add(strIngredient16);
            measures.add(strMeasure16);
        }
        if(strIngredient17 != null&&strIngredient17.length()>0){
            ingredients.add(strIngredient17);
            measures.add(strMeasure17);
        }
        if(strIngredient18 != null&&strIngredient18.length()>0){
            ingredients.add(strIngredient18);
            measures.add(strMeasure18);
        }
        if(strIngredient19 != null&&strIngredient19.length()>0){
            ingredients.add(strIngredient19);
            measures.add(strMeasure19);
        }
        if(strIngredient20 != null&&strIngredient20.length()>0){
            ingredients.add(strIngredient20);
            measures.add(strMeasure20);
        }
        return new Pair<>(ingredients, measures);


    }

    private String strInstructions;
    private String strMealThumb;
    private String strTags;
    private String strYoutube;
    private String strIngredient1;
    private String strIngredient2;
    private String strIngredient3;
    private String strIngredient4;
    private String strIngredient5;
    private String strIngredient6;
    private String strIngredient7;
    private String strIngredient8;
    private String strIngredient9;
    private String strIngredient10;
    private String strIngredient11;
    private String strIngredient12;
    private String strIngredient13;
    private String strIngredient14;
    private String strIngredient15;
    private String strIngredient16;
    private String strIngredient17;
    private String strIngredient18;
    private String strIngredient19;
    private String strIngredient20;
    private String strMeasure1;
    private String strMeasure2;
    private String strMeasure3;
    private String strMeasure4;
    private String strMeasure5;
    private String strMeasure6;
    private String strMeasure7;
    private String strMeasure8;
    private String strMeasure9;
    private String strMeasure10;
    private String strMeasure11;
    private String strMeasure12;
    private String strMeasure13;
    private String strMeasure14;
    private String strMeasure15;
    private String strMeasure16;
    private String strMeasure17;
    private String strMeasure18;
    private String strMeasure19;
    private String strMeasure20;
    private String strSource;
    private Object strImageSource;
    private Object strCreativeCommonsConfirmed;
    private Object dateModified;
}
