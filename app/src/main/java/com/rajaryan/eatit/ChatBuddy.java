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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatBuddy#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatBuddy extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String Id,Name,type,Image;
    TextView name;
    Adapter adapter;
    Button send;
    RecyclerView rec;
    EditText message;
    int last;
    ImageView image;
    public ChatBuddy() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatBuddy.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatBuddy newInstance(String param1, String param2) {
        ChatBuddy fragment = new ChatBuddy();
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
        View view=inflater.inflate(R.layout.fragment_chat_buddy, container, false);
        message=view.findViewById(R.id.message);

        rec=view.findViewById(R.id.rec);
        rec.setLayoutManager(new LinearLayoutManager(getContext()));
        send=view.findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    OkHttpClient client = new OkHttpClient();
                    String status="s";
                    okhttp3.Request request = new okhttp3.Request.Builder()
                            .url("https://api.spoonacular.com/recipes/quickAnswer?apiKey=13eccaaea6a54c3289ff66823a024077&q="+message.getText().toString())
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
                                String answer=json.getString("answer");
                                HashMap<String,String> hashMap=new HashMap<>();
                                hashMap.put("Message",message.getText().toString());
                                hashMap.put("Reply",answer.toString());
                                DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Users/"+FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("ChatBot");
                                databaseReference.push().setValue(hashMap);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.e("res", myresponse);

                        }
                    });
                }

        });
        Query query= FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("ChatBot");
        FirebaseRecyclerOptions<ChatClass> option1 =
                new FirebaseRecyclerOptions.Builder<ChatClass>()
                        .setQuery(query,ChatClass.class)
                        .setLifecycleOwner(this)
                        .build();
        adapter=new Adapter(option1);
        rec.setAdapter(adapter);
        adapter.startListening();
        return view;
    }
    public class Adapter extends FirebaseRecyclerAdapter<ChatClass, Adapter.viewholder> {
        String time1;
        /**
         * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
         * {@link FirebaseRecyclerOptions} for configuration options.
         *
         * @param options
         */
        public Adapter(@NonNull FirebaseRecyclerOptions<ChatClass> options) {
            super(options);
        }

        @Override
        protected void onBindViewHolder(@NonNull viewholder viewholder, int i, @NonNull ChatClass recipeData) {
            viewholder.send.setText(recipeData.Message);
            viewholder.receive.setText(recipeData.Reply);
        }
        @NonNull
        @Override
        public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_layout, parent, false);
            return new viewholder(view);
        }

        public class viewholder extends RecyclerView.ViewHolder {
            TextView send, receive;
            public viewholder(@NonNull View itemView) {
                super(itemView);
                send = itemView.findViewById(R.id.send);
                receive=itemView.findViewById(R.id.receive);
            }
        }
    }
}