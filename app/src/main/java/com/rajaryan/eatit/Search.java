
package com.rajaryan.eatit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Search#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Search extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView search_rec;
    String JSON_URL;
    Adapter adapter1,adapter2;
    RecyclerView type_cuisine;
    EditText search;
    Button open;
    Adapter2 adapter22;
    TextView artical;
    ImageView image;
    List<RecipeData> items;
    ImageButton search_btn;
    String artical_link;
    RecipeAdapter adapter;
    String searchQuery="indian";

    TabLayout tabLayout;
    public Search() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Search.
     */
    // TODO: Rename and change types and number of parameters
    public static Search newInstance(String param1, String param2) {
        Search fragment = new Search();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_search, container, false);
        tabLayout=v.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Brunch"));
        tabLayout.addTab(tabLayout.newTab().setText("Lunch"));
        tabLayout.addTab(tabLayout.newTab().setText("Snacks"));
        tabLayout.addTab(tabLayout.newTab().setText("Dinner"));
        search_btn=v.findViewById(R.id.search_buttom);
        tabLayout.selectTab(tabLayout.getTabAt(0));
        search_rec=v.findViewById(R.id.rec_search);
        open=v.findViewById(R.id.open);
        search=v.findViewById(R.id.search);
        artical=v.findViewById(R.id.artical);
        image=v.findViewById(R.id.image);
        DatabaseReference artical_title=FirebaseDatabase.getInstance().getReference().child("Artical").child("Tittle");
        artical_title.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String val=snapshot.getValue().toString();
                artical.setText(val);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference artical_title1=FirebaseDatabase.getInstance().getReference().child("Artical").child("Image");
        artical_title1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String val=snapshot.getValue().toString();
                Picasso.get().load(val).into(image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference artical_title2=FirebaseDatabase.getInstance().getReference().child("Artical").child("Link");
        artical_title2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                artical_link=snapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getActivity(),WebLayout.class);
                i.putExtra("link",artical_link);
                startActivity(i);
            }
        });
        type_cuisine=v.findViewById(R.id.type_cuisine);
        type_cuisine.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        Query query1= FirebaseDatabase.getInstance().getReference().child("Meals").child("Brunch");
        FirebaseRecyclerOptions<RecipeData> option =
                new FirebaseRecyclerOptions.Builder<RecipeData>()
                        .setQuery(query1,RecipeData.class)
                        .setLifecycleOwner(getActivity())
                        .build();
        adapter1=new Adapter(option);
        adapter1.startListening();
        type_cuisine.setAdapter(adapter1);
        items=new ArrayList<>();
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchQuery=search.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String type=tab.getText().toString();
                if(type.equals("Brunch")){
                    Query query= FirebaseDatabase.getInstance().getReference().child("Meals").child("Brunch");
                    FirebaseRecyclerOptions<RecipeData> option =
                            new FirebaseRecyclerOptions.Builder<RecipeData>()
                                    .setQuery(query,RecipeData.class)
                                    .setLifecycleOwner(getActivity())
                                    .build();
                    adapter1=new Adapter(option);
                    adapter1.startListening();
                    type_cuisine.setAdapter(adapter1);
                }
                if(type.equals("Lunch")){
                    Query query= FirebaseDatabase.getInstance().getReference().child("Meals").child("Lunch");
                    FirebaseRecyclerOptions<RecipeData> option =
                            new FirebaseRecyclerOptions.Builder<RecipeData>()
                                    .setQuery(query,RecipeData.class)
                                    .setLifecycleOwner(getActivity())
                                    .build();
                    adapter1=new Adapter(option);
                    adapter1.startListening();
                    type_cuisine.setAdapter(adapter1);
                }
                if(type.equals("Snacks")){
                    Query query= FirebaseDatabase.getInstance().getReference().child("Meals").child("Snacks");
                    FirebaseRecyclerOptions<RecipeData> option =
                            new FirebaseRecyclerOptions.Builder<RecipeData>()
                                    .setQuery(query,RecipeData.class)
                                    .setLifecycleOwner(getActivity())
                                    .build();
                    adapter1=new Adapter(option);
                    adapter1.startListening();
                    type_cuisine.setAdapter(adapter1);
                }
                if(type.equals("Dinner")){
                    Query query= FirebaseDatabase.getInstance().getReference().child("Meals").child("Dinner");
                    FirebaseRecyclerOptions<RecipeData> option =
                            new FirebaseRecyclerOptions.Builder<RecipeData>()
                                    .setQuery(query,RecipeData.class)
                                    .setLifecycleOwner(getActivity())
                                    .build();
                    adapter1=new Adapter(option);
                    adapter1.startListening();
                    type_cuisine.setAdapter(adapter1);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<RecipeData> new_items = new ArrayList<>();
                OkHttpClient client = new OkHttpClient();
                String status="s";
                okhttp3.Request request = new okhttp3.Request.Builder()
                        .url("https://api.spoonacular.com/recipes/complexSearch?apiKey=3eb3a2358c884a3ab7a26d748fd560bb&query="+searchQuery+"&number=4")
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

                        try {
                            JSONObject json = new JSONObject(myresponse);
                            JSONArray res= json.getJSONArray("results");
                            for (int i = 0; i < res.length(); i++) {
                                try {
                                    JSONObject songObject = res.getJSONObject(i);
                                    RecipeData song = new RecipeData();
                                    song.setTitle(songObject .getString("title").toString());
                                    song.setId(songObject.getString("id".toString()));
                                    song.setImage(songObject.getString("image"));
                                    Log.e("Title",song.getTitle());
                                    new_items.add(song);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.e("E12","not");
                                }
                            }
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Recent Search");
                            myRef.setValue(new_items);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.e("res", myresponse);
                    }
                });

                JSON_URL = "https://api.spoonacular.com/recipes/complexSearch?apiKey=3eb3a2358c884a3ab7a26d748fd560bb&query=indian&number=4";
                search_rec.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                Query query= FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Recent Search");
                FirebaseRecyclerOptions<RecipeData> option =
                        new FirebaseRecyclerOptions.Builder<RecipeData>()
                                .setQuery(query,RecipeData.class)
                                .setLifecycleOwner(getActivity())
                                .build();
                adapter2=new Adapter(option);
                search_rec.setAdapter(adapter2);
                adapter2.startListening();

            }
        });
        OkHttpClient client = new OkHttpClient();
        String status="s";
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url("https://api.spoonacular.com/recipes/complexSearch?apiKey=3eb3a2358c884a3ab7a26d748fd560bb&query="+searchQuery+"&number=4")
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

                try {
                    JSONObject json = new JSONObject(myresponse);
                    JSONArray res= json.getJSONArray("results");
                    for (int i = 0; i < res.length(); i++) {
                        try {
                            JSONObject songObject = res.getJSONObject(i);
                            RecipeData song = new RecipeData();
                            song.setTitle(songObject .getString("title").toString());
                            song.setId(songObject.getString("id".toString()));
                            song.setImage(songObject.getString("image"));
                            Log.e("Title",song.getTitle());
                            items.add(song);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("E12","not");
                        }
                    }
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Recent Search");
                    myRef.setValue(items);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("res", myresponse);
            }
        });
        search_rec.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        Query query= FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Recent Search");
        FirebaseRecyclerOptions<RecipeData> option1 =
                new FirebaseRecyclerOptions.Builder<RecipeData>()
                        .setQuery(query,RecipeData.class)
                        .setLifecycleOwner(this)
                        .build();
        adapter2=new Adapter(option1);
        search_rec.setAdapter(adapter2);
        adapter2.startListening();
        RecyclerView trivia=v.findViewById(R.id.trivia);
        trivia.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        Query query2= FirebaseDatabase.getInstance().getReference().child("Food Facts");
        FirebaseRecyclerOptions<RecipeData> option2 =
                new FirebaseRecyclerOptions.Builder<RecipeData>()
                        .setQuery(query2,RecipeData.class)
                        .setLifecycleOwner(getActivity())
                        .build();
        adapter22=new Adapter2(option2);
        trivia.setAdapter(adapter22);
        adapter22.startListening();
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter1.startListening();
        adapter2.startListening();
    }
    public class Adapter2 extends FirebaseRecyclerAdapter<RecipeData, Adapter2.viewholder> {
        String time1;
        /**
         * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
         * {@link FirebaseRecyclerOptions} for configuration options.
         *
         * @param options
         */
        public Adapter2(@NonNull FirebaseRecyclerOptions<RecipeData> options) {
            super(options);
        }

        @Override
        protected void onBindViewHolder(@NonNull viewholder viewholder, int i, @NonNull RecipeData recipeData) {
            viewholder.tittle.setText(recipeData.getTitle());
        }
        @NonNull
        @Override
        public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fact, parent, false);
            return new viewholder(view);
        }

        public class viewholder extends RecyclerView.ViewHolder {
            TextView tittle;
            public viewholder(@NonNull View itemView) {
                super(itemView);
                tittle = itemView.findViewById(R.id.title);
            }
        }
    }
    public class Adapter extends FirebaseRecyclerAdapter<RecipeData, Adapter.viewholder> {
        String time1;
        /**
         * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
         * {@link FirebaseRecyclerOptions} for configuration options.
         *
         * @param options
         */
        public Adapter(@NonNull FirebaseRecyclerOptions<RecipeData> options) {
            super(options);
        }

        @Override
        protected void onBindViewHolder(@NonNull viewholder viewholder, int i, @NonNull RecipeData recipeData) {
            viewholder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i=new Intent(getActivity(),
                            Recipe.class);
                    i.putExtra("Id",recipeData.getId());
                    startActivity(i);
                }
            });
            viewholder.tittle.setText(recipeData.getTitle());
            time1="30";
            OkHttpClient client = new OkHttpClient();
            String id=recipeData.getId();
            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url("https://api.spoonacular.com/recipes/"+id+"/information?apiKey=3eb3a2358c884a3ab7a26d748fd560bb")
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
                    try {
                        JSONObject json = new JSONObject(myresponse);
                        time1=json.getString("readyInMinutes");
                        Log.e("Time",time1);
                        recipeData.setTime(time1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.e("res1", myresponse);
                }
            });
            viewholder.time.setText(time1+ " Min");
            Picasso.get().load(recipeData.getImage()).into(viewholder.image);

        }
        @NonNull
        @Override
        public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.food_item, parent, false);
            return new viewholder(view);
        }

        public class viewholder extends RecyclerView.ViewHolder {
            TextView tittle, time;
            ImageButton next;
            ImageView image;
            public viewholder(@NonNull View itemView) {
                super(itemView);
                tittle = itemView.findViewById(R.id.tittle);
                next=itemView.findViewById(R.id.next);
                time = itemView.findViewById(R.id.time);
                image = itemView.findViewById(R.id.image);
            }
        }
    }
}