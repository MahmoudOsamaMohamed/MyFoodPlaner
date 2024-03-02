package com.mahmoud.myfoodplaner;


import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mahmoud.myfoodplaner.dbmodels.Area;
import com.mahmoud.myfoodplaner.dbmodels.CashingLocalDataSource;
import com.mahmoud.myfoodplaner.dbmodels.Category;
import com.mahmoud.myfoodplaner.dbmodels.Ingerdiant;
import com.mahmoud.myfoodplaner.model.callbacks.AreasCallback;
import com.mahmoud.myfoodplaner.model.callbacks.CategoriesCallback;
import com.mahmoud.myfoodplaner.model.callbacks.DetailedCategoriesCallback;
import com.mahmoud.myfoodplaner.model.callbacks.IngredientsCallback;
import com.mahmoud.myfoodplaner.model.callbacks.ShortMealsCallback;
import com.mahmoud.myfoodplaner.model.MealsRemoteDataSource;
import com.mahmoud.myfoodplaner.model.MealsRemoteDataSourceImpl;
import com.mahmoud.myfoodplaner.model.pojos.AreaPojo;
import com.mahmoud.myfoodplaner.model.pojos.DetailedCategory;
import com.mahmoud.myfoodplaner.model.pojos.Ingredient;
import com.mahmoud.myfoodplaner.model.pojos.ShortMeal;
import com.mahmoud.myfoodplaner.model.pojos.SimpleCategory;

import org.reactivestreams.Subscription;

import java.util.LinkedHashMap;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.FlowableSubscriber;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements AreasCallback, DetailedCategoriesCallback, IngredientsCallback {
    BottomNavigationView bottomNavigationView;
    MealsRemoteDataSource mealsRemoteDataSource;
    public static final LinkedHashMap<String, String> countryCuisines = new LinkedHashMap<>();
    public static final LinkedHashMap<String, String> countryCodes = new LinkedHashMap<>();
    public static final LinkedHashMap<String,String>ingredientDescriptions=new LinkedHashMap<>();

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.setProperty("javax.net.debug", "ssl");
        countryCodes.put("American", "US");
        countryCodes.put("British", "GB");
        countryCodes.put("Canadian", "CA");
        countryCodes.put("Chinese", "CN");
        countryCodes.put("Croatian", "HR");
        countryCodes.put("Dutch", "NL");
        countryCodes.put("Egyptian", "EG");
        countryCodes.put("Filipino", "PH");
        countryCodes.put("Greek", "GR");
        countryCodes.put("French", "FR");
        countryCodes.put("Indian", "IN");
        countryCodes.put("Irish", "IE");
        countryCodes.put("Jamaican", "JM");
        countryCodes.put("Japanese", "JP");
        countryCodes.put("Italian", "IT");
        countryCodes.put("Kenyan", "KE");
        countryCodes.put("Malaysian", "MY");
        countryCodes.put("Mexican", "MX");
        countryCodes.put("Portuguese", "PT");
        countryCodes.put("Russian", "RU");
        countryCodes.put("Spanish", "ES");
        countryCodes.put("Thai", "TH");
        countryCodes.put("Polish", "PL");
        countryCodes.put("Turkish", "TR");
        countryCodes.put("Tunisian", "TN");
        countryCodes.put("Vietnamese", "VN");
        countryCodes.put("Unknown", "UNK");
        countryCodes.put("Moroccan", "MA");

        countryCuisines.put("American", "American cuisine is diverse, featuring influences from various immigrant groups. It includes dishes such as hamburgers, hot dogs, barbecue, and apple pie.");
        countryCuisines.put("British", "British cuisine is known for traditional dishes such as fish and chips, roast beef with Yorkshire pudding, and English breakfast. It also includes a variety of pies and puddings.");
        countryCuisines.put("Canadian", "Canadian cuisine is influenced by indigenous, British, French, and immigrant cultures. It features dishes such as poutine, butter tarts, and Nanaimo bars.");
        countryCuisines.put("Chinese", "Chinese cuisine is diverse, with regional variations in flavors and ingredients. It includes dishes such as stir-fries, noodles, dumplings, and Peking duck.");
        countryCuisines.put("Croatian", "Croatian cuisine is characterized by fresh, locally sourced ingredients and Mediterranean influences. It includes dishes such as seafood, grilled meats, and pastries.");
        countryCuisines.put("Dutch", "Dutch cuisine features hearty dishes such as stamppot (mashed potatoes with vegetables), herring, and Dutch pancakes.");
        countryCuisines.put("Egyptian", "Egyptian cuisine is known for its use of herbs and spices. Common dishes include kushari (a mix of rice, lentils, and pasta), falafel, and ful medames (fava beans).");
        countryCuisines.put("Filipino", "Filipino cuisine is a blend of indigenous, Spanish, Chinese, and American influences. It includes dishes such as adobo, sinigang, and lechon.");
        countryCuisines.put("Greek", "Greek cuisine emphasizes fresh ingredients, olive oil, and herbs. It includes dishes such as moussaka, souvlaki, Greek salad, and baklava.");
        countryCuisines.put("French", "French cuisine is known for its emphasis on fresh, high-quality ingredients and intricate cooking techniques. It includes dishes such as coq au vin, ratatouille, croissants, and crème brûlée.");
        countryCuisines.put("Indian", "Indian cuisine is diverse, with regional variations in flavors and ingredients. It features dishes such as curry, biryani, dosa, and samosas.");
        countryCuisines.put("Irish", "Irish cuisine features hearty and comforting dishes such as Irish stew, colcannon, soda bread, and boxty (potato pancakes).");
        countryCuisines.put("Jamaican", "Jamaican cuisine is known for its bold flavors and spices. It includes dishes such as jerk chicken, ackee and saltfish, curry goat, and patties.");
        countryCuisines.put("Japanese", "Japanese cuisine emphasizes fresh, seasonal ingredients and simple preparation techniques. It includes dishes such as sushi, sashimi, ramen, and tempura.");
        countryCuisines.put("Italian", "Italian cuisine is characterized by its simplicity and use of fresh ingredients. It includes dishes such as pasta, pizza, risotto, and gelato.");
        countryCuisines.put("Kenyan", "Kenyan cuisine is diverse, with influences from various ethnic groups and neighboring countries. Common dishes include nyama choma (grilled meat), ugali, and sukuma wiki (collard greens).");
        countryCuisines.put("Malaysian", "Malaysian cuisine is a fusion of Malay, Chinese, Indian, Thai, Javanese, and Sumatran influences. It includes dishes such as nasi lemak, rendang, laksa, and satay.");
        countryCuisines.put("Mexican", "Mexican cuisine is known for its bold flavors, spices, and use of fresh ingredients. It includes dishes such as tacos, burritos, enchiladas, and guacamole.");
        countryCuisines.put("Portuguese", "Portuguese cuisine features fresh seafood, olive oil, and hearty dishes. It includes dishes such as bacalhau (salted cod), caldo verde (kale soup), and pastéis de nata (custard tarts).");
        countryCuisines.put("Russian", "Russian cuisine is hearty and comforting, with dishes such as borscht, pelmeni, blini, and beef stroganoff.");
        countryCuisines.put("Spanish", "Spanish cuisine is known for its use of fresh, high-quality ingredients and bold flavors. It includes dishes such as paella, tapas, gazpacho, and churros.");
        countryCuisines.put("Thai", "Thai cuisine is known for its balance of sweet, sour, salty, and spicy flavors. It includes dishes such as pad Thai, green curry, tom yum soup, and mango sticky rice.");
        countryCuisines.put("Polish", "Polish cuisine features hearty and comforting dishes such as pierogi, kielbasa, bigos (hunter's stew), and żurek (sour rye soup).");
        countryCuisines.put("Turkish", "Turkish cuisine is known for its use of fresh ingredients, spices, and bold flavors. It includes dishes such as kebabs, borek, baklava, and Turkish delight.");
        countryCuisines.put("Tunisian", "Tunisian cuisine is a blend of Mediterranean, Arab, Berber, and Ottoman influences. It includes dishes such as couscous, tagine, brik, and harissa.");
        countryCuisines.put("Vietnamese", "Vietnamese cuisine emphasizes fresh ingredients, herbs, and a balance of flavors. It includes dishes such as pho, banh mi, spring rolls, and bún cha.");
        countryCuisines.put("Unknown", "Unknown cuisine description.");
        countryCuisines.put("Moroccan", "Moroccan cuisine is known for its use of spices, herbs, and slow-cooked dishes. It includes dishes such as tagine, couscous, pastilla, and harira.");

        ingredientDescriptions.put("Chicken", "Chicken is a type of poultry that is commonly used in cooking.");
        ingredientDescriptions.put("Salmon", "Salmon is a popular fish known for its pink flesh and rich flavor.");
        ingredientDescriptions.put("Beef", "Beef is meat that comes from cattle and is commonly used in various dishes.");
        ingredientDescriptions.put("Pork", "Pork is a type of pork meat that is commonly used in various dishes.");
        ingredientDescriptions.put("Avocado", "Avocado is a creamy fruit with a rich, nutty flavor and smooth texture.");
        ingredientDescriptions.put("Apple Cider Vinegar", "Apple cider vinegar is made from fermented apple juice and is commonly used in cooking and as a natural remedy.");
        ingredientDescriptions.put("Asparagus", "Asparagus is a nutrient-rich vegetable with a distinct flavor and crisp texture.");
        ingredientDescriptions.put("Aubergine", "Aubergine, also known as eggplant, is a versatile vegetable often used in Mediterranean and Asian cuisines.");
        ingredientDescriptions.put("Bacon", "Bacon is a cured meat typically made from pork belly or back cuts, known for its savory flavor and crispy texture.");
        ingredientDescriptions.put("Baking Powder", "Baking powder is a leavening agent used in baking to help doughs and batters rise.");
        ingredientDescriptions.put("Balsamic Vinegar", "Balsamic vinegar is a dark, syrupy vinegar with a sweet and tangy flavor, often used in salad dressings and marinades.");
        ingredientDescriptions.put("Basil", "Basil is a fragrant herb commonly used in Italian cuisine, prized for its fresh, peppery flavor.");
        ingredientDescriptions.put("Bay Leaf", "Bay leaf is a dried aromatic leaf commonly used to add flavor to soups, stews, and sauces.");
        ingredientDescriptions.put("Beef", "Beef is a popular meat obtained from cattle, known for its rich flavor and versatility in cooking.");
        ingredientDescriptions.put("Bicarbonate Of Soda", "Bicarbonate of soda, also known as baking soda, is a leavening agent used in baking and cooking.");
        ingredientDescriptions.put("Black Pepper", "Black pepper is a spice made from ground peppercorns, adding heat and flavor to a variety of dishes.");
        ingredientDescriptions.put("Borlotti Beans", "Borlotti beans, also known as cranberry beans, are a type of legume with a creamy texture and nutty flavor.");
        ingredientDescriptions.put("Broccoli", "Broccoli is a nutritious vegetable high in fiber and vitamins, often steamed, roasted, or stir-fried.");
        ingredientDescriptions.put("Brown Rice", "Brown rice is a whole grain rice with a nutty flavor and chewy texture, known for its nutritional benefits.");
        ingredientDescriptions.put("Butter", "Butter is a dairy product made from cream, commonly used in cooking, baking, and as a spread.");
        ingredientDescriptions.put("Cajun", "Cajun seasoning is a spice blend originating from Louisiana, known for its bold and spicy flavor profile.");
        ingredientDescriptions.put("Canned Tomatoes", "Canned tomatoes are tomatoes that have been preserved in cans, commonly used in sauces, soups, and stews.");
        ingredientDescriptions.put("Carrots", "Carrots are root vegetables known for their vibrant orange color and sweet flavor, rich in vitamins and antioxidants.");
        ingredientDescriptions.put("Cayenne Pepper", "Cayenne pepper is a hot chili pepper commonly used to add heat and flavor to dishes, sauces, and marinades.");
        ingredientDescriptions.put("Celery", "Celery is a crunchy, green vegetable with a mild, herbal flavor, often used in soups, salads, and stir-fries.");
        ingredientDescriptions.put("Cheddar Cheese", "Cheddar cheese is a popular cheese variety with a sharp flavor and smooth texture, commonly used in sandwiches, sauces, and snacks.");
        ingredientDescriptions.put("Cherry Tomatoes", "Cherry tomatoes are small, sweet tomatoes often used in salads, pasta dishes, and as a garnish.");
        ingredientDescriptions.put("Chicken Breast", "Chicken breast is a lean cut of chicken meat known for its mild flavor and tender texture, commonly grilled, baked, or sautéed.");
        ingredientDescriptions.put("Chickpeas", "Chickpeas, also known as garbanzo beans, are a versatile legume used in various cuisines, prized for their nutty flavor and creamy texture.");
        ingredientDescriptions.put("Chili Powder", "Chili powder is a spice blend made from ground chili peppers and other spices, commonly used to add heat and flavor to dishes.");
        ingredientDescriptions.put("Chocolate Chips", "Chocolate chips are small pieces of chocolate often used in baking cookies, cakes, and other desserts.");
        ingredientDescriptions.put("Cilantro", "Cilantro, also known as coriander leaf, is a fresh herb with a citrusy flavor used in various cuisines, particularly Mexican and Asian dishes.");
        ingredientDescriptions.put("Cinnamon", "Cinnamon is a sweet and spicy spice made from the inner bark of trees, commonly used in baking, cooking, and beverages.");
        ingredientDescriptions.put("Cocoa", "Cocoa is the powder derived from roasted cacao beans, commonly used to make chocolate and add flavor to baked goods and beverages.");
        ingredientDescriptions.put("Coconut Milk", "Coconut milk is a creamy liquid extracted from grated coconut meat, commonly used in curries, soups, and desserts.");
        ingredientDescriptions.put("Coriander", "Coriander, also known as cilantro, is a versatile herb used in cooking, known for its citrusy flavor and aroma.");
        ingredientDescriptions.put("Cornstarch", "Cornstarch, also known as cornflour, is a fine powder made from corn kernels, commonly used as a thickening agent in sauces, soups, and desserts.");
        ingredientDescriptions.put("Cumin", "Cumin is a spice made from the dried seeds of the cumin plant, commonly used in Indian, Middle Eastern, and Mexican cuisines.");
        ingredientDescriptions.put("Curry Powder", "Curry powder is a blend of spices commonly used in Indian and South Asian cuisines, adding warmth and flavor to dishes.");
        ingredientDescriptions.put("Eggs", "Eggs are a versatile ingredient used in cooking and baking, prized for their protein content and ability to bind ingredients together.");
        ingredientDescriptions.put("Garlic", "Garlic is a pungent bulb vegetable known for its distinct flavor and aroma, commonly used in cooking around the world.");
        ingredientDescriptions.put("Ginger", "Ginger is a spicy and aromatic root commonly used in cooking, baking, and beverages, prized for its medicinal properties.");
        ingredientDescriptions.put("Lemon", "Lemon is a citrus fruit known for its tart flavor and acidic juice, commonly used in cooking, baking, and beverages.");
        ingredientDescriptions.put("Mushrooms", "Mushrooms are fungi with a savory flavor and meaty texture, used in various cuisines, particularly in soups, sauces, and stir-fries.");
        ingredientDescriptions.put("Olive Oil", "Olive oil is a healthy fat derived from olives, commonly used in cooking, salad dressings, and as a finishing oil.");
        ingredientDescriptions.put("Onions", "Onions are aromatic bulb vegetables used as a base ingredient in many dishes, known for their savory flavor and ability to add depth to recipes.");

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);


        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);





        NavigationUI.setupWithNavController(bottomNavigationView, navController);

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(5000);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        bottomNavigationView.setVisibility(View.VISIBLE);
//                    }
//                });
//
//            }
//        }).start();
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.homeFragment) {
                navController.navigate(R.id.homeFragment);
            } else if (item.getItemId() == R.id.favourateFragment) {
                if(getSharedPreferences("user",MODE_PRIVATE)
                        .getString("email",null)!=null)
                navController.navigate(R.id.favourateFragment);
                else{

                    navController.navigate(R.id.signFragment);
                }
            } else if (item.getItemId() == R.id.planFragment) {
                if (getSharedPreferences("user",MODE_PRIVATE)
                        .getString("email",null)!=null)
                navController.navigate(R.id.planFragment);
                else{

                    navController.navigate(R.id.signFragment);
                }
            } else if (item.getItemId() == R.id.exploreFragment) {

                navController.navigate(R.id.exploreFragment);
            }
            else if (item.getItemId() == R.id.profileFragment) {
                if (getSharedPreferences("user",MODE_PRIVATE)
                        .getString("email",null)!=null)
                navController.navigate(R.id.profileFragment);
                else{

                    navController.navigate(R.id.signFragment);
                }
            }
            return true;
        });
        mealsRemoteDataSource = MealsRemoteDataSourceImpl.getInstance();
        CashingLocalDataSource.getInstance(this).getAllAreas()
                .subscribeOn(Schedulers.io()).take(1).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new FlowableSubscriber<List<Area>>() {
                    @Override
                    public void onSubscribe(@NonNull Subscription s) {
                        s.request(1);
                    }

                    @Override
                    public void onNext(List<Area> areas) {
                        if(areas.size() == 0){
                            mealsRemoteDataSource.getAllAreas(MainActivity.this);
                            mealsRemoteDataSource.getDetailedCategories(MainActivity.this);
                            mealsRemoteDataSource.getAllIngredients(MainActivity.this);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                 public void onComplete() {
//CashingLocalDataSource.getInstance(MainActivity.this).getAllIngerdiants().subscribeOn(Schedulers.io()).take(1)
//        .observeOn(AndroidSchedulers.mainThread()).subscribe(new FlowableSubscriber<List<Ingerdiant>>() {
//
//            @Override
//            public void onSubscribe(@NonNull Subscription s) {
//                s.request(1);
//            }
//
//            @Override
//            public void onNext(List<Ingerdiant> ingerdiants) {
//for(Ingerdiant ingerdiant:ingerdiants){
//
//}
//            }
//
//            @Override
//            public void onError(Throwable t) {
//
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        });
                   }
                });



}

    @Override
    public void onSuccessAreasResult(List<AreaPojo> areaPojos) {
        for(AreaPojo areaPojo:areaPojos){
            Area area = new Area();

            area.name = areaPojo.getStrArea();
            CashingLocalDataSource.getInstance(this).insertArea(area).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe();
        }

    }

    @Override
    public void onFailureAreasResult(String error) {

    }



    @Override
    public void onSuccessIngredientsResult(List<Ingredient> ingredients) {
        for (Ingredient ingredient : ingredients) {
            Ingerdiant ingerdiant = new Ingerdiant();
            ingerdiant.name = ingredient.getStrIngredient();

            CashingLocalDataSource.getInstance(this).insertIngerdiant(ingerdiant).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe();
        }

    }

    @Override
    public void onFailureIngredientsResult(String error) {

    }

    @Override
    public void onSuccessDetailedCategoriesResult(List<DetailedCategory> categories) {
        for(DetailedCategory category:categories){
            Category category1 = new Category();
            category1.name = category.getStrCategory();
            category1.img_url = category.getStrCategoryThumb();
            category1.description = category.getStrCategoryDescription();
            CashingLocalDataSource.getInstance(this).insertCategory(category1).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe();
        }
    }

    @Override
    public void onFailureDetailedCategoriesResult(String error) {

    }
    public static void hideBottomNav(){

    //    bottomNavigationView.setVisibility(View.GONE);
    }
    public static void showBottomNav(){

      //  bottomNavigationView.setVisibility(View.VISIBLE);
    }
}