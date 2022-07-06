package com.rajaryan.eatit;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
 * Use the {@link Nutrination#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Nutrination extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    EditText search;
    ImageButton search_button;
    SwitchCompat switchCompat;
    String max_cal,url;
    TextView mf,mp,mc,mch,tf,tp,tc,tch,wf,wp,wc,wch,thf,thp,thc,thch,ff,fp,fc,fch,sf,sp,sc,sch,xf,xp,xc,xch;
    Boolean veg=false;
    RecyclerView m_rec,t_rec,w_rec,th_rec,f_rec,s_rec,x_rec;
    private List<Object> monday_list = new ArrayList<>();
    private List<Object> tuesday_list = new ArrayList<>();
    private List<Object> wednesday_list = new ArrayList<>();
    private List<Object> saturday_list = new ArrayList<>();
    private List<Object> thursday_list = new ArrayList<>();
    private List<Object> friday_list = new ArrayList<>();
    private List<Object> sunday_list = new ArrayList<>();
    String searchQuery;
    RecyclerAdapter madapter,tadapter,wadapter,thadapter,fadapter,sadapter,xadapter;
    public Nutrination() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Nutrination.
     */
    // TODO: Rename and change types and number of parameters
    public static Nutrination newInstance(String param1, String param2) {
        Nutrination fragment = new Nutrination();
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
        View v=inflater.inflate(R.layout.fragment_nutrination, container, false);
        switchCompat=v.findViewById(R.id.switch1);
        search=v.findViewById(R.id.search);
        search_button=v.findViewById(R.id.search_buttom);
        mf=v.findViewById(R.id.mf);
        mp=v.findViewById(R.id.mp);
        mc=v.findViewById(R.id.mc);
        mch=v.findViewById(R.id.mch);
        tf=v.findViewById(R.id.tf);
        tp=v.findViewById(R.id.tp);
        tc=v.findViewById(R.id.tc);
        tch=v.findViewById(R.id.tch);
        wf=v.findViewById(R.id.wf);
        wp=v.findViewById(R.id.wp);
        wc=v.findViewById(R.id.wc);
        wch=v.findViewById(R.id.wch);
        ff=v.findViewById(R.id.ff);
        fp=v.findViewById(R.id.fp);
        fc=v.findViewById(R.id.fc);
        fch=v.findViewById(R.id.fch);
        sf=v.findViewById(R.id.sf);
        sp=v.findViewById(R.id.sp);
        sc=v.findViewById(R.id.sc);
        sch=v.findViewById(R.id.sch);
        xf=v.findViewById(R.id.xf);
        xp=v.findViewById(R.id.xp);
        xc=v.findViewById(R.id.xc);
        xch=v.findViewById(R.id.xch);
       thf=v.findViewById(R.id.thf);
       thp=v.findViewById(R.id.thp);
       thc=v.findViewById(R.id.thc);
       thch=v.findViewById(R.id.thch);
        m_rec=v.findViewById(R.id.m_rec);
        m_rec.setLayoutManager(new LinearLayoutManager(getContext()));
        m_rec.setHasFixedSize(true);
        t_rec=v.findViewById(R.id.t_rec);
        t_rec.setLayoutManager(new LinearLayoutManager(getContext()));
        t_rec.setHasFixedSize(true);
        w_rec=v.findViewById(R.id.w_rec);
        w_rec.setLayoutManager(new LinearLayoutManager(getContext()));
        w_rec.setHasFixedSize(true);
        th_rec=v.findViewById(R.id.th_rec);
        th_rec.setLayoutManager(new LinearLayoutManager(getContext()));
        th_rec.setHasFixedSize(true);
        f_rec=v.findViewById(R.id.f_rec);
        f_rec.setLayoutManager(new LinearLayoutManager(getContext()));
        f_rec.setHasFixedSize(true);
        s_rec=v.findViewById(R.id.s_rec);
        s_rec.setLayoutManager(new LinearLayoutManager(getContext()));
        s_rec.setHasFixedSize(true);
        x_rec=v.findViewById(R.id.x_rec);
        x_rec.setLayoutManager(new LinearLayoutManager(getContext()));
        x_rec.setHasFixedSize(true);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchQuery= search.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        switchCompat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(switchCompat.isChecked()){
                    veg=true;
                }
                else {
                    veg=false;
                }
            }

        });
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("Data");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    data data=snapshot.getValue(com.rajaryan.eatit.data.class);
                    max_cal=data.getMax_calories();
                    url = "https://api.spoonacular.com/mealplanner/generate?apiKey=13eccaaea6a54c3289ff66823a024077&targetCalories="+max_cal;
                    OkHttpClient client = new OkHttpClient();
                    String status="s";
                    okhttp3.Request request1 = new okhttp3.Request.Builder()
                            .url(url)
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
                                JSONObject monday=songObject.getJSONObject("week").getJSONObject("monday");
                                JSONObject tuesday=songObject.getJSONObject("week").getJSONObject("tuesday");
                                JSONObject wednesday=songObject.getJSONObject("week").getJSONObject("wednesday");
                                JSONObject thursday=songObject.getJSONObject("week").getJSONObject("thursday");
                                JSONObject friday=songObject.getJSONObject("week").getJSONObject("friday");
                                JSONObject saturday=songObject.getJSONObject("week").getJSONObject("saturday");
                                JSONObject sunday=songObject.getJSONObject("week").getJSONObject("sunday");
                                JSONArray m_meals=monday.getJSONArray("meals");
                                for (int i=0; i<m_meals.length(); ++i) {
                                    JSONObject itemObj = m_meals.getJSONObject(i);
                                    String id = itemObj.getString("id").toString();
                                    String title=itemObj.getString("title").toString();
                                    String image=itemObj.getString("imageType").toString();
                                    String time=itemObj.getString("readyInMinutes").toString();
                                    RecipeData holidays1 = new RecipeData(id,title,image,time);
                                    monday_list.add(holidays1);
                                }
                                JSONArray t_meals=tuesday.getJSONArray("meals");
                                for (int i=0; i<t_meals.length(); ++i) {
                                    JSONObject itemObj =t_meals.getJSONObject(i);
                                    String id = itemObj.getString("id").toString();
                                    String title=itemObj.getString("title").toString();
                                    String image=itemObj.getString("imageType").toString();
                                    String time=itemObj.getString("readyInMinutes").toString();
                                    RecipeData holidays1 = new RecipeData(id,title,image,time);
                                    tuesday_list.add(holidays1);
                                }
                                JSONArray w_meals=wednesday.getJSONArray("meals");
                                for (int i=0; i<w_meals.length(); ++i) {
                                    JSONObject itemObj =w_meals.getJSONObject(i);
                                    String id = itemObj.getString("id").toString();
                                    String title=itemObj.getString("title").toString();
                                    String image=itemObj.getString("imageType").toString();
                                    String time=itemObj.getString("readyInMinutes").toString();
                                    RecipeData holidays1 = new RecipeData(id,title,image,time);
                                    wednesday_list.add(holidays1);
                                }
                                JSONArray th_meals=thursday.getJSONArray("meals");
                                for (int i=0; i<th_meals.length(); ++i) {
                                    JSONObject itemObj =th_meals.getJSONObject(i);
                                    String id = itemObj.getString("id").toString();
                                    String title=itemObj.getString("title").toString();
                                    String image=itemObj.getString("imageType").toString();
                                    String time=itemObj.getString("readyInMinutes").toString();
                                    RecipeData holidays1 = new RecipeData(id,title,image,time);
                                    thursday_list.add(holidays1);
                                }
                                JSONArray f_meals=friday.getJSONArray("meals");
                                for (int i=0; i<f_meals.length(); ++i) {
                                    JSONObject itemObj =f_meals.getJSONObject(i);
                                    String id = itemObj.getString("id").toString();
                                    String title=itemObj.getString("title").toString();
                                    String image=itemObj.getString("imageType").toString();
                                    String time=itemObj.getString("readyInMinutes").toString();
                                    RecipeData holidays1 = new RecipeData(id,title,image,time);
                                    friday_list.add(holidays1);
                                }
                                JSONArray s_meals=saturday.getJSONArray("meals");
                                for (int i=0; i<s_meals.length(); ++i) {
                                    JSONObject itemObj =s_meals.getJSONObject(i);
                                    String id = itemObj.getString("id").toString();
                                    String title=itemObj.getString("title").toString();
                                    String image=itemObj.getString("imageType").toString();
                                    String time=itemObj.getString("readyInMinutes").toString();
                                    RecipeData holidays1 = new RecipeData(id,title,image,time);
                                    saturday_list.add(holidays1);
                                }
                                JSONArray x_meals=sunday.getJSONArray("meals");
                                for (int i=0; i<x_meals.length(); ++i) {
                                    JSONObject itemObj =x_meals.getJSONObject(i);
                                    String id = itemObj.getString("id").toString();
                                    String title=itemObj.getString("title").toString();
                                    String image=itemObj.getString("imageType").toString();
                                    String time=itemObj.getString("readyInMinutes").toString();
                                    RecipeData holidays1 = new RecipeData(id,title,image,time);
                                    sunday_list.add(holidays1);
                                }
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            JSONObject nutrition=monday.getJSONObject("nutrients");
                                            JSONObject tnutrition=tuesday.getJSONObject("nutrients");
                                            JSONObject wnutrition=wednesday.getJSONObject("nutrients");
                                            JSONObject thnutrition=thursday.getJSONObject("nutrients");
                                            JSONObject fnutrition=friday.getJSONObject("nutrients");
                                            JSONObject snutrition=saturday.getJSONObject("nutrients");
                                            JSONObject xnutrition=sunday.getJSONObject("nutrients");
                                            mf.setText(nutrition.getString("fat").toString());
                                            mp.setText(nutrition.getString("protein").toString());
                                            mc.setText(nutrition.getString("calories").toString());
                                            mch.setText(nutrition.getString("carbohydrates").toString());
                                            tf.setText(tnutrition.getString("fat").toString());
                                            tp.setText(tnutrition.getString("protein").toString());
                                            tc.setText(tnutrition.getString("calories").toString());
                                            tch.setText(tnutrition.getString("carbohydrates").toString());
                                            wf.setText(wnutrition.getString("fat").toString());
                                            wp.setText(wnutrition.getString("protein").toString());
                                            wc.setText(wnutrition.getString("calories").toString());
                                            wch.setText(wnutrition.getString("carbohydrates").toString());
                                            ff.setText(fnutrition.getString("fat").toString());
                                            fp.setText(fnutrition.getString("protein").toString());
                                            fc.setText(fnutrition.getString("calories").toString());
                                            fch.setText(fnutrition.getString("carbohydrates").toString());
                                            sf.setText(snutrition.getString("fat").toString());
                                            sp.setText(snutrition.getString("protein").toString());
                                            sc.setText(snutrition.getString("calories").toString());
                                            sch.setText(snutrition.getString("carbohydrates").toString());
                                            xf.setText(xnutrition.getString("fat").toString());
                                            xp.setText(xnutrition.getString("protein").toString());
                                            xc.setText(xnutrition.getString("calories").toString());
                                            xch.setText(xnutrition.getString("carbohydrates").toString());
                                            thf.setText(thnutrition.getString("fat").toString());
                                            thp.setText(thnutrition.getString("protein").toString());
                                            thc.setText(thnutrition.getString("calories").toString());
                                            thch.setText(thnutrition.getString("carbohydrates").toString());
                                            fadapter = new RecyclerAdapter(getActivity(), friday_list);
                                            f_rec.setAdapter(fadapter);
                                            madapter = new RecyclerAdapter(getActivity(), monday_list);
                                            m_rec.setAdapter(madapter);
                                            tadapter = new RecyclerAdapter(getActivity(), tuesday_list);
                                            t_rec.setAdapter(tadapter);
                                            wadapter = new RecyclerAdapter(getActivity(), wednesday_list);
                                            w_rec.setAdapter(wadapter);
                                            thadapter = new RecyclerAdapter(getActivity(), thursday_list);
                                            th_rec.setAdapter(thadapter);
                                            sadapter = new RecyclerAdapter(getActivity(), saturday_list);
                                            s_rec.setAdapter(sadapter);
                                            xadapter = new RecyclerAdapter(getActivity(), sunday_list);
                                            x_rec.setAdapter(xadapter);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.e("res", myresponse);
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(searchQuery)){
                    Toast.makeText(getContext(),"Enter Valid Calories",Toast.LENGTH_LONG).show();
                }
                else {

                    url = "https://api.spoonacular.com/mealplanner/generate?apiKey=13eccaaea6a54c3289ff66823a024077&targetCalories="+searchQuery+"&diet=vegetarian";
                    if(veg){
                        url="https://api.spoonacular.com/mealplanner/generate?apiKey=13eccaaea6a54c3289ff66823a024077&targetCalories="+searchQuery+"&diet=vegan";
                    }
                    else {
                        url = "https://api.spoonacular.com/mealplanner/generate?apiKey=13eccaaea6a54c3289ff66823a024077&targetCalories="+searchQuery;
                    }
                    OkHttpClient client = new OkHttpClient();
                    String status="s";
                    okhttp3.Request request1 = new okhttp3.Request.Builder()
                            .url(url)
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
                                JSONObject monday=songObject.getJSONObject("week").getJSONObject("monday");
                                JSONObject tuesday=songObject.getJSONObject("week").getJSONObject("tuesday");
                                JSONObject wednesday=songObject.getJSONObject("week").getJSONObject("wednesday");
                                JSONObject thursday=songObject.getJSONObject("week").getJSONObject("thursday");
                                JSONObject friday=songObject.getJSONObject("week").getJSONObject("friday");
                                JSONObject saturday=songObject.getJSONObject("week").getJSONObject("saturday");
                                JSONObject sunday=songObject.getJSONObject("week").getJSONObject("sunday");
                                JSONArray m_meals=monday.getJSONArray("meals");
                                for (int i=0; i<m_meals.length(); ++i) {
                                    JSONObject itemObj = m_meals.getJSONObject(i);
                                    String id = itemObj.getString("id").toString();
                                    String title=itemObj.getString("title").toString();
                                    String image=itemObj.getString("imageType").toString();
                                    String time=itemObj.getString("readyInMinutes").toString();
                                    RecipeData holidays1 = new RecipeData(id,title,image,time);
                                    monday_list.add(holidays1);
                                }
                                JSONArray t_meals=tuesday.getJSONArray("meals");
                                for (int i=0; i<t_meals.length(); ++i) {
                                    JSONObject itemObj =t_meals.getJSONObject(i);
                                    String id = itemObj.getString("id").toString();
                                    String title=itemObj.getString("title").toString();
                                    String image=itemObj.getString("imageType").toString();
                                    String time=itemObj.getString("readyInMinutes").toString();
                                    RecipeData holidays1 = new RecipeData(id,title,image,time);
                                    tuesday_list.add(holidays1);
                                }
                                JSONArray w_meals=wednesday.getJSONArray("meals");
                                for (int i=0; i<w_meals.length(); ++i) {
                                    JSONObject itemObj =w_meals.getJSONObject(i);
                                    String id = itemObj.getString("id").toString();
                                    String title=itemObj.getString("title").toString();
                                    String image=itemObj.getString("imageType").toString();
                                    String time=itemObj.getString("readyInMinutes").toString();
                                    RecipeData holidays1 = new RecipeData(id,title,image,time);
                                    wednesday_list.add(holidays1);
                                }
                                JSONArray th_meals=thursday.getJSONArray("meals");
                                for (int i=0; i<th_meals.length(); ++i) {
                                    JSONObject itemObj =th_meals.getJSONObject(i);
                                    String id = itemObj.getString("id").toString();
                                    String title=itemObj.getString("title").toString();
                                    String image=itemObj.getString("imageType").toString();
                                    String time=itemObj.getString("readyInMinutes").toString();
                                    RecipeData holidays1 = new RecipeData(id,title,image,time);
                                    thursday_list.add(holidays1);
                                }
                                JSONArray f_meals=friday.getJSONArray("meals");
                                for (int i=0; i<f_meals.length(); ++i) {
                                    JSONObject itemObj =f_meals.getJSONObject(i);
                                    String id = itemObj.getString("id").toString();
                                    String title=itemObj.getString("title").toString();
                                    String image=itemObj.getString("imageType").toString();
                                    String time=itemObj.getString("readyInMinutes").toString();
                                    RecipeData holidays1 = new RecipeData(id,title,image,time);
                                    friday_list.add(holidays1);
                                }
                                JSONArray s_meals=saturday.getJSONArray("meals");
                                for (int i=0; i<s_meals.length(); ++i) {
                                    JSONObject itemObj =s_meals.getJSONObject(i);
                                    String id = itemObj.getString("id").toString();
                                    String title=itemObj.getString("title").toString();
                                    String image=itemObj.getString("imageType").toString();
                                    String time=itemObj.getString("readyInMinutes").toString();
                                    RecipeData holidays1 = new RecipeData(id,title,image,time);
                                    saturday_list.add(holidays1);
                                }
                                JSONArray x_meals=sunday.getJSONArray("meals");
                                for (int i=0; i<x_meals.length(); ++i) {
                                    JSONObject itemObj =x_meals.getJSONObject(i);
                                    String id = itemObj.getString("id").toString();
                                    String title=itemObj.getString("title").toString();
                                    String image=itemObj.getString("imageType").toString();
                                    String time=itemObj.getString("readyInMinutes").toString();
                                    RecipeData holidays1 = new RecipeData(id,title,image,time);
                                    sunday_list.add(holidays1);
                                }
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            JSONObject nutrition=monday.getJSONObject("nutrients");
                                            JSONObject tnutrition=tuesday.getJSONObject("nutrients");
                                            JSONObject wnutrition=wednesday.getJSONObject("nutrients");
                                            JSONObject thnutrition=thursday.getJSONObject("nutrients");
                                            JSONObject fnutrition=friday.getJSONObject("nutrients");
                                            JSONObject snutrition=saturday.getJSONObject("nutrients");
                                            JSONObject xnutrition=sunday.getJSONObject("nutrients");
                                            mf.setText(nutrition.getString("fat").toString());
                                            mp.setText(nutrition.getString("protein").toString());
                                            mc.setText(nutrition.getString("calories").toString());
                                            mch.setText(nutrition.getString("carbohydrates").toString());
                                            tf.setText(tnutrition.getString("fat").toString());
                                            tp.setText(tnutrition.getString("protein").toString());
                                            tc.setText(tnutrition.getString("calories").toString());
                                            tch.setText(tnutrition.getString("carbohydrates").toString());
                                            wf.setText(wnutrition.getString("fat").toString());
                                            wp.setText(wnutrition.getString("protein").toString());
                                            wc.setText(wnutrition.getString("calories").toString());
                                            wch.setText(wnutrition.getString("carbohydrates").toString());
                                            ff.setText(fnutrition.getString("fat").toString());
                                            fp.setText(fnutrition.getString("protein").toString());
                                            fc.setText(fnutrition.getString("calories").toString());
                                            fch.setText(fnutrition.getString("carbohydrates").toString());
                                            sf.setText(snutrition.getString("fat").toString());
                                            sp.setText(snutrition.getString("protein").toString());
                                            sc.setText(snutrition.getString("calories").toString());
                                            sch.setText(snutrition.getString("carbohydrates").toString());
                                            xf.setText(xnutrition.getString("fat").toString());
                                            xp.setText(xnutrition.getString("protein").toString());
                                            xc.setText(xnutrition.getString("calories").toString());
                                            xch.setText(xnutrition.getString("carbohydrates").toString());
                                            thf.setText(thnutrition.getString("fat").toString());
                                            thp.setText(thnutrition.getString("protein").toString());
                                            thc.setText(thnutrition.getString("calories").toString());
                                            thch.setText(thnutrition.getString("carbohydrates").toString());
                                            fadapter = new RecyclerAdapter(getActivity(), friday_list);
                                            f_rec.setAdapter(fadapter);
                                            madapter = new RecyclerAdapter(getActivity(), monday_list);
                                            m_rec.setAdapter(madapter);
                                            tadapter = new RecyclerAdapter(getActivity(), tuesday_list);
                                            t_rec.setAdapter(tadapter);
                                            wadapter = new RecyclerAdapter(getActivity(), wednesday_list);
                                            w_rec.setAdapter(wadapter);
                                            thadapter = new RecyclerAdapter(getActivity(), thursday_list);
                                            th_rec.setAdapter(thadapter);
                                            sadapter = new RecyclerAdapter(getActivity(), saturday_list);
                                            s_rec.setAdapter(sadapter);
                                            xadapter = new RecyclerAdapter(getActivity(), sunday_list);
                                            x_rec.setAdapter(xadapter);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.e("res", myresponse);
                        }
                    });
                }
            }
        });

        return v;
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
            ImageButton button;

            public ItemViewHolder(@NonNull View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.name);


                button=itemView.findViewById(R.id.next);

            }
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

            View layoutView = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.planner_card, viewGroup, false);
            return new RecyclerAdapter.ItemViewHolder((layoutView));


        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            RecyclerAdapter.ItemViewHolder itemViewHolder = (RecyclerAdapter.ItemViewHolder) viewHolder;
            RecipeData holidays = (RecipeData) listRecyclerItem.get(i);
            itemViewHolder.name.setText(holidays.getTitle());
            itemViewHolder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i=new Intent(getActivity(),
                            Recipe.class);
                    i.putExtra("Id",holidays.getId());
                    startActivity(i);
                }
            });
        }

        @Override
        public int getItemCount() {
            return listRecyclerItem.size();
        }
    }
}