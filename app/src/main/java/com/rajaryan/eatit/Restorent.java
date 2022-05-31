package com.rajaryan.eatit;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
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
 * Use the {@link Restorent#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Restorent extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    RecyclerView rec;
    EditText search;
    FrameLayout frame1;
    private String mParam2;
    ImageButton search_btn;
    Adapter adapter;
    public Restorent() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Restorent.
     */
    // TODO: Rename and change types and number of parameters
    public static Restorent newInstance(String param1, String param2) {
        Restorent fragment = new Restorent();
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
        View view=inflater.inflate(R.layout.fragment_restorent, container, false);
        search=view.findViewById(R.id.search);
        search_btn=view.findViewById(R.id.search_buttom);
        frame1=view.findViewById(R.id.frame1);
        rec=view.findViewById(R.id.rec);
            search_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    List<Menu> new_items = new ArrayList<>();
                    OkHttpClient client = new OkHttpClient();
                    String status="s";
                    okhttp3.Request request = new okhttp3.Request.Builder()
                            .url("https://api.spoonacular.com/food/menuItems/search?apiKey=13eccaaea6a54c3289ff66823a024077&query="+search.getText().toString()+"&number=100")
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
                                JSONArray res= json.getJSONArray("menuItems");
                                for (int i = 0; i < res.length(); i++) {
                                    try {
                                        JSONObject songObject = res.getJSONObject(i);
                                        Menu song = new Menu();
                                        song.setName(songObject .getString("title").toString());
                                        song.setId(songObject.getString("id".toString()));
                                        song.setImg(songObject.getString("image"));
                                        song.setHotel(songObject.getString("restaurantChain"));
                                        new_items.add(song);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Log.e("E12","not");
                                    }
                                }
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference myRef = database.getReference("MenuSearch").child(search.getText().toString());
                                myRef.setValue(new_items);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.e("res", myresponse);
                        }
                    });
                    Query query= FirebaseDatabase.getInstance().getReference("MenuSearch").child(search.getText().toString());
                    FirebaseRecyclerOptions<Menu> option =
                            new FirebaseRecyclerOptions.Builder<Menu>()
                                    .setQuery(query,Menu.class)
                                    .setLifecycleOwner(getActivity())
                                    .build();
                    frame1.setVisibility(View.GONE);
                    adapter=new Adapter(option);
                    rec.setAdapter(adapter);
                    adapter.startListening();
                    rec.setLayoutManager(new LinearLayoutManager(getContext()));
                }
            });

        return view;
    }
    public class Adapter extends FirebaseRecyclerAdapter<Menu, Adapter.viewholder> {
        String time1;
        /**
         * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
         * {@link FirebaseRecyclerOptions} for configuration options.
         *
         * @param options
         */
        public Adapter(@NonNull FirebaseRecyclerOptions<Menu> options) {
            super(options);
        }

        @Override
        protected void onBindViewHolder(@NonNull viewholder viewholder, int i, @NonNull Menu recipeData) {
            viewholder.tittle.setText(recipeData.getName());
            viewholder.time.setText(recipeData.getHotel());
            viewholder.next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String search_string=recipeData.getHotel().replaceAll("\\s+","");
                    Intent i=new Intent(getActivity(),WebLayout.class);
                    i.putExtra("link",search_string+".com");
                    startActivity(i);
                }
            });
            Picasso.get().load(recipeData.getImg()).into(viewholder.image);

        }
        @NonNull
        @Override
        public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.menu_items, parent, false);
            return new viewholder(view);
        }

        @Override
        public int getItemCount() {
            return super.getItemCount();
        }

        public class viewholder extends RecyclerView.ViewHolder {
            TextView tittle, time;
            Button next;
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