package com.rajaryan.eatit;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profile extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    TextView name,mail;
    EditText ing;
    ImageButton Add;
    RecyclerView items,rec;
    Adapter adapter;
    ImageButton exit;
    Adapter1 adapter1;
    String uid;
    private String mParam2;

    public Profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Profile.
     */
    // TODO: Rename and change types and number of parameters
    public static Profile newInstance(String param1, String param2) {
        Profile fragment = new Profile();
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
        View v=inflater.inflate(R.layout.fragment_profile, container, false);
        name=v.findViewById(R.id.name);
        mail=v.findViewById(R.id.email);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());
        if(account !=  null){
            String personName = account.getDisplayName();
            String personEmail = account.getEmail();
            Toast.makeText(getActivity(),personName + personEmail ,Toast.LENGTH_SHORT).show();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    name.setText(personName);
                    mail.setText(personEmail);
                }
            });
        }
        Add=v.findViewById(R.id.add);
        ing=v.findViewById(R.id.ing);
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        uid=FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(ing.getText().toString())){
                    Toast.makeText(getContext(),"Enter Valid Item",Toast.LENGTH_LONG).show();
                }
                else {
                    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Users");
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("title",ing.getText().toString());
                    databaseReference.child(uid).child("Fridge").push().setValue(hashMap);
                }
            }
        });
        items=v.findViewById(R.id.items);
        items.setLayoutManager(new LinearLayoutManager(getActivity()));
        Query query= FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Fridge");
        FirebaseRecyclerOptions<Tracker> option =
                new FirebaseRecyclerOptions.Builder<Tracker>()
                        .setQuery(query,Tracker.class)
                        .setLifecycleOwner(this)
                        .build();
        adapter=new Adapter(option);
        items.setAdapter(adapter);
        exit=v.findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent i=new Intent(getActivity(),MainActivity.class);
                startActivity(i);
            }
        });
        adapter.startListening();
        rec=v.findViewById(R.id.rec);
        rec.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        Query query1= FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Fridge");
        FirebaseRecyclerOptions<Tracker> option1 =
                new FirebaseRecyclerOptions.Builder<Tracker>()
                        .setQuery(query1,Tracker.class)
                        .setLifecycleOwner(this)
                        .build();
        adapter1=new Adapter1(option1);
        rec.setAdapter(adapter1);
        adapter1.startListening();
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
        adapter1.startListening();
    }
    public class Adapter1 extends FirebaseRecyclerAdapter<Tracker,Adapter1.viewholder> {
        /**
         * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
         * {@link FirebaseRecyclerOptions} for configuration options.
         *
         * @param options
         */
        public Adapter1(@NonNull FirebaseRecyclerOptions<Tracker> options) {
            super(options);
        }

        @Override
        protected void onBindViewHolder(@NonNull viewholder viewholder, int i, @NonNull Tracker tracker) {
            String item=tracker.getTitle();
            OkHttpClient client = new OkHttpClient();
            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url("https://api.spoonacular.com/recipes/findByIngredients?apiKey=e072820e209a4c0f8b583d543caa1dea&ingredients="+item+"&number=1")
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

                        JSONArray res= new JSONArray(myresponse);
                        for (int i = 0; i < res.length(); i++) {
                            try {
                                JSONObject songObject = res.getJSONObject(i);
                                String id=songObject.getString("id").toString();
                                okhttp3.Request request = new okhttp3.Request.Builder()
                                        .url("https://api.spoonacular.com/recipes/"+id+"/information?apiKey=13eccaaea6a54c3289ff66823a024077")
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
                                            String time1=json.getString("readyInMinutes");
                                            String image=json.getString("image");
                                            String name=json.getString("title");

                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    viewholder.time.setText(time1+ " Min");
                                                    Picasso.get().load(image).into(viewholder.image);
                                                    viewholder.itemView.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            Intent i=new Intent(getActivity(),
                                                                    Recipe.class);
                                                            i.putExtra("Id",id);
                                                            startActivity(i);
                                                        }
                                                    });
                                                    viewholder.tittle.setText(name);
                                                }
                                            });


                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        Log.e("res1", myresponse);
                                    }
                                });



                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e("E12","not");
                            }
                        }
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Recent Search");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.e("res", myresponse);
                }
            });
        }
        @NonNull
        @Override
        public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.food_item, parent, false);
            return new Adapter1.viewholder(view);
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
    public class Adapter extends FirebaseRecyclerAdapter<Tracker,Adapter.viewholder> {
        /**
         * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
         * {@link FirebaseRecyclerOptions} for configuration options.
         *
         * @param options
         */
        public Adapter(@NonNull FirebaseRecyclerOptions<Tracker> options) {
            super(options);
        }

        @Override
        protected void onBindViewHolder(@NonNull viewholder viewholder, int i, @NonNull Tracker tracker) {

            String id=getRef(i).getKey();
            viewholder.name.setText(tracker.getTitle());
            Log.e("name",tracker.getTitle());
            viewholder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseReference ref;
                    ref=FirebaseDatabase.getInstance().getReference("Users").child(uid).child("Fridge").child(id);
                    ref.removeValue();
                }
            });
        }
        @NonNull
        @Override
        public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fridge_item, parent, false);
            return new viewholder(view);
        }

        public class viewholder extends RecyclerView.ViewHolder {
            TextView name;
            ImageButton button;
            public viewholder(@NonNull View itemView) {
                super(itemView);
                name=itemView.findViewById(R.id.name);
                button=itemView.findViewById(R.id.button);
            }
        }
    }
}