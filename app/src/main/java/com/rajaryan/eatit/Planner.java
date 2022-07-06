package com.rajaryan.eatit;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Planner#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Planner extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView rec;
    ImageButton exit;
    Adapter adapter;
    public Planner() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Planner.
     */
    // TODO: Rename and change types and number of parameters
    public static Planner newInstance(String param1, String param2) {
        Planner fragment = new Planner();
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
        View view=inflater.inflate(R.layout.fragment_planner, container, false);
        rec=view.findViewById(R.id.rec);
        Query query= FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("WishList");
        FirebaseRecyclerOptions<Menu> option =
                new FirebaseRecyclerOptions.Builder<Menu>()
                        .setQuery(query,Menu.class)
                        .setLifecycleOwner(getActivity())
                        .build();
        adapter=new Adapter(option);
        rec.setAdapter(adapter);
        adapter.startListening();
        rec.setLayoutManager(new LinearLayoutManager(getContext()));
        exit=view.findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent i=new Intent(getActivity(),MainActivity.class);
                startActivity(i);
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
            String key=getRef(i).getKey();
            viewholder.next.setText("View");
            viewholder.delete.setVisibility(View.VISIBLE);
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
            viewholder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseReference query= FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("WishList").child(key);
                    query.removeValue();
                }
            });
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
            ImageButton delete;
            ImageView image;
            public viewholder(@NonNull View itemView) {
                super(itemView);
                tittle = itemView.findViewById(R.id.tittle);
                next=itemView.findViewById(R.id.next);
                time = itemView.findViewById(R.id.time);
                image = itemView.findViewById(R.id.image);
                delete=itemView.findViewById(R.id.delete);
            }
        }
    }
}