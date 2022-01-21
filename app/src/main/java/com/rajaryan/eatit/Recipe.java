package com.rajaryan.eatit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;

public class Recipe extends AppCompatActivity {
    TextView name,src,time,heart_score,price,instruction,license,credit,summary ,veg,nonveg;
    String name1,src1,time1,heart_score1,price1,instruction1,license1,credit1,link,ins,sum,sum1,l1;
    ImageView image;
    TabLayout tabLayout;
    FrameLayout ins1;
           LinearLayout nut1;
    TextView protine,carb,fiber,fat;
    String protine1,carb1,fiber1,fat1,protine2,carb2,fiber2,fat2;;
    String res;
    Button open;
    Boolean veg_status;
    private RecyclerView mRecyclerView,nutrition_list;
    private List<Object> viewItems = new ArrayList<>();
    private List<Object> viewItems1 = new ArrayList<>();
    private List<Object> viewItems2 = new ArrayList<>();
    WebView web;
    private RecyclerView.Adapter mAdapter;
    RecyclerAdapter1 mAdapter1;
    RecyclerAdapter2 Adapter2;
    private RecyclerView.LayoutManager layoutManager;
    RecyclerView steps_rec;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        Intent i=getIntent();
        id=i.getStringExtra("Id");
        Toast.makeText(getApplicationContext(),id,Toast.LENGTH_LONG).show();
        name=findViewById(R.id.name);
        src=findViewById(R.id.src);
        time=findViewById(R.id.time);
        heart_score=findViewById(R.id.heart);
        open=findViewById(R.id.open);
        veg=findViewById(R.id.veg);
        nonveg=findViewById(R.id.nonveg);
        price=findViewById(R.id.price);
        instruction=findViewById(R.id.ins1);
        mRecyclerView=findViewById(R.id.ingredients);
        tabLayout=findViewById(R.id.tab_layout);
        fat=findViewById(R.id.fat);
        carb=findViewById(R.id.carbs);
        protine=findViewById(R.id.protine);
        nutrition_list=findViewById(R.id.nutritions);
        steps_rec=findViewById(R.id.steps);
        steps_rec.setLayoutManager(new LinearLayoutManager(this));
        steps_rec.setHasFixedSize(true);
        fiber=findViewById(R.id.fiber);
        web=findViewById(R.id.web);
        tabLayout.addTab(tabLayout.newTab().setText("Ingredients"));
        tabLayout.addTab(tabLayout.newTab().setText("Directions"));
        tabLayout.addTab(tabLayout.newTab().setText("Nutrition"));
        ins1=findViewById(R.id.ins);
        nut1=findViewById(R.id.nutritions1);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String language = tab.getText().toString();
                if(language.equals("Ingredients")){
                    mRecyclerView.setVisibility(View.VISIBLE);
                    ins1.setVisibility(View.GONE);
                    nutrition_list.setVisibility(View.GONE);
                    nut1.setVisibility(View.GONE);
                }
                if(language.equals("Directions")){
                    mRecyclerView.setVisibility(View.GONE);
                    nutrition_list.setVisibility(View.GONE);
                    ins1.setVisibility(View.VISIBLE);
                    nut1.setVisibility(View.GONE);
                }
                if(language.equals("Nutrition")){
                    mRecyclerView.setVisibility(View.GONE);
                    nut1.setVisibility(View.VISIBLE);
                    nutrition_list.setVisibility(View.VISIBLE);
                    ins1.setVisibility(View.GONE);

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        mRecyclerView.setLayoutManager(layoutManager);
        LinearLayoutManager linearLayoutManager1=new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        nutrition_list.setLayoutManager(linearLayoutManager1);
        nutrition_list.setHasFixedSize(false);
        // specify an adapter (see also next example)


        summary=findViewById(R.id.sum1);
        credit=findViewById(R.id.credit);
        image=findViewById(R.id.image);
        OkHttpClient client = new OkHttpClient();
        String status="s";
        okhttp3.Request request1 = new okhttp3.Request.Builder()
                .url("https://api.spoonacular.com/recipes/"+id+"/nutritionWidget.json?apiKey=13eccaaea6a54c3289ff66823a024077")
                .get()
                .build();
        client.newCall(request1).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {
                String myresponse = response.body().string();

                Log.e("res",myresponse);
                try {
                    JSONObject songObject = new JSONObject(myresponse);
                    carb1=songObject.getString("carbs");
                    protine1=songObject.getString("protein");
                    fat1=songObject.getString("fat");
                    fiber1=songObject.getString("calories");
                    JSONArray jsonArray =songObject.getJSONArray("good");
                    for (int i=0; i<jsonArray.length(); ++i) {
                        JSONObject itemObj = jsonArray.getJSONObject(i);
                        String name = itemObj.getString("title");
                        String amount=itemObj.getString("amount");
                        String percentOfDailyNeeds=itemObj.getString("percentOfDailyNeeds");
                        Nutritions holidays1 = new Nutritions(amount,percentOfDailyNeeds,name);
                        viewItems1.add(holidays1);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            carb.setText(carb1+" Carbs");
                            protine.setText(protine1+" Protein");
                            fat.setText(fat1+" Fat");
                            fiber.setText(fiber1);
                            mAdapter1 = new RecyclerAdapter1(getApplicationContext(), viewItems1);
                            nutrition_list.setAdapter(mAdapter1);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("res", myresponse);
            }
        });
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url("https://api.spoonacular.com/recipes/"+id+"/information?apiKey=c612a2450d184d1cb437e493f0e7762d")
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {
                String myresponse = response.body().string();

                Log.e("res",myresponse);
                try {
                    JSONObject songObject = new JSONObject(myresponse);
                    JSONArray jsonArray =songObject.getJSONArray("extendedIngredients");
                    for (int i=0; i<jsonArray.length(); ++i) {
                        JSONObject itemObj = jsonArray.getJSONObject(i);
                        String name = itemObj.getString("name");
                        String id=itemObj.getString("id");
                        String amount=itemObj.getString("amount");
                        String unit=itemObj.getString("unit");
                        if(unit=="c")
                        Log.e("Name Ing",name);
                        ing holidays = new ing(name, id,amount,unit);
                        viewItems.add(holidays);
                    }




                    name1=songObject.getString("title");
                    src1=songObject.getString("sourceName").toString();
                    time1=String.valueOf(songObject.getString("readyInMinutes"));
                    heart_score1=String.valueOf(songObject.getString("healthScore"));
                    price1=String.valueOf(songObject.getString("pricePerServing"));
                    instruction1=songObject.getString("instructions").toString();
                    ins= Jsoup.parse(instruction1).wholeText();
                    sum=songObject.getString("summary").toString();
                    sum1=Jsoup.parse(sum).wholeText();
                    credit1=songObject.getString("creditsText").toString();
                    link=songObject.getString("image").toString();
                    veg_status=songObject.getBoolean("vegetarian");
                    l1=songObject.getString("sourceUrl").toString();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            name.setText(name1);
                            Picasso.get().load(link).into(image);
                            src.setText(src1);
                            time.setText(time1);
                            heart_score.setText(heart_score1);
                            price.setText(price1);
                            mAdapter = new RecyclerAdapter(getApplicationContext(), viewItems);
                            mRecyclerView.setAdapter(mAdapter);
                            if(veg_status){
                                veg.setVisibility(View.VISIBLE);
                                nonveg.setVisibility(View.GONE);
                            }
                            if(!veg_status){
                                veg.setVisibility(View.GONE);
                                nonveg.setVisibility(View.VISIBLE);
                            }
                            instruction.setText(ins);
                            open.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent i=new Intent(getApplicationContext(),WebLayout.class);
                                    i.putExtra("link",l1);
                                    startActivity(i);
                                }
                            });
                            credit.setText(credit1);
                            summary.setText(sum1);
                            Log.e("name",name1);
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("res", myresponse);
            }
        });



    }

    public class RecyclerAdapter2 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int TYPE = 1;
        private final Context context;
        private final List<Object> listRecyclerItem1;

        public RecyclerAdapter2(Context context, List<Object> listRecyclerItem) {
            this.context = context;
            this.listRecyclerItem1 = listRecyclerItem;
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder {

            private TextView name;
            private TextView number,amount;

            public ItemViewHolder(@NonNull View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.name);
                number=itemView.findViewById(R.id.number);
            }
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

            View layoutView = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.steps, viewGroup, false);
            return new ItemViewHolder((layoutView));


        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) viewHolder;
            steps holidays1 = (steps) listRecyclerItem1.get(i);
            itemViewHolder.name.setText(holidays1.getStep());
            itemViewHolder.number.setText(holidays1.getNumber());
        }

        @Override
        public int getItemCount() {
            return 10;
        }
    }

    public class RecyclerAdapter1 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int TYPE = 1;
        private final Context context;
        private final List<Object> listRecyclerItem1;

        public RecyclerAdapter1(Context context, List<Object> listRecyclerItem) {
            this.context = context;
            this.listRecyclerItem1 = listRecyclerItem;
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder {

            private TextView name;
            private TextView percentage,amount;

            public ItemViewHolder(@NonNull View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.name);
                amount=itemView.findViewById(R.id.quantity);
                percentage=itemView.findViewById(R.id.percentage);
            }
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                    View layoutView = LayoutInflater.from(viewGroup.getContext()).inflate(
                            R.layout.nutrition, viewGroup, false);
                    return new ItemViewHolder((layoutView));


        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                    ItemViewHolder itemViewHolder = (ItemViewHolder) viewHolder;
                    Nutritions holidays1 = (Nutritions) listRecyclerItem1.get(i);
                    itemViewHolder.name.setText(holidays1.getName());
                    itemViewHolder.amount.setText(holidays1.getAmount());
                    itemViewHolder.percentage.setText(holidays1.getPercentOfDailyNeeds()+" %");
        }

        @Override
        public int getItemCount() {
            return 10;
        }
    }
    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int TYPE = 1;
        private final Context context;
        private final List<Object> listRecyclerItem;

        public RecyclerAdapter(Context context, List<Object> listRecyclerItem) {
            this.context = context;
            this.listRecyclerItem = listRecyclerItem;
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder {

            private TextView name;
            private TextView date,quantity;

            public ItemViewHolder(@NonNull View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.name);
                quantity=itemView.findViewById(R.id.quantity);
            }
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

            View layoutView = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.ingredients, viewGroup, false);
            return new ItemViewHolder((layoutView));


        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) viewHolder;
            ing holidays = (ing) listRecyclerItem.get(i);
            itemViewHolder.name.setText(holidays.getName());
            itemViewHolder.quantity.setText(holidays.getAmount()+" "+holidays.getUnit());
        }

        @Override
        public int getItemCount() {
            return listRecyclerItem.size();
        }
    }
}