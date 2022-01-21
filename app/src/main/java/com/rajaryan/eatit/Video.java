package com.rajaryan.eatit;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
 * Use the {@link Video#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Video extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    RecyclerView video_rec;
    private String mParam2;
    RecyclerAdapter adapter;
    FrameLayout frameLayout;
    EditText search;
    String searchQuery;
    ImageButton search_btn;
    private List<Object> viewItems1 = new ArrayList<>();
    public Video() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Video.
     */
    // TODO: Rename and change types and number of parameters
    public static Video newInstance(String param1, String param2) {
        Video fragment = new Video();
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
        View v=inflater.inflate(R.layout.fragment_video, container, false);
        search_btn=v.findViewById(R.id.search_buttom);
        video_rec=v.findViewById(R.id.video_rec);
        video_rec.setLayoutManager(new LinearLayoutManager(getContext()));
        video_rec.setHasFixedSize(true);
        search=v.findViewById(R.id.search);
        frameLayout=v.findViewById(R.id.frame);
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
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(searchQuery)){
                    Toast.makeText(getContext(),"Enter Your Desired Cuisine",Toast.LENGTH_LONG).show();
                }
                else {
                    frameLayout.setVisibility(View.GONE);
                    OkHttpClient client = new OkHttpClient();
                    String status="s";
                    okhttp3.Request request1 = new okhttp3.Request.Builder()
                            .url("https://api.spoonacular.com/food/videos/search?apiKey=e072820e209a4c0f8b583d543caa1dea&query="+searchQuery+"&number=4")
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

                                JSONArray jsonArray =songObject.getJSONArray("videos");
                                for (int i=0; i<jsonArray.length(); ++i) {
                                    JSONObject itemObj = jsonArray.getJSONObject(i);
                                    String title = itemObj.getString("title");
                                    String youTubeId=itemObj.getString("youTubeId").toString();
                                    String views=itemObj.getString("views").toString();
                                    String thumbnail=itemObj.getString("thumbnail").toString();
                                    String length=itemObj.getString("length").toString();
                                    String rating=itemObj.getString("rating").toString();

                                    VideoData holidays1 = new VideoData(title,youTubeId,rating,views,thumbnail,length);
                                    viewItems1.add(holidays1);
                                }
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        adapter = new RecyclerAdapter(getActivity(), viewItems1);
                                        video_rec.setAdapter(adapter);
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
            private TextView rating,view;
            Button button;
            ImageView imageView;
            public ItemViewHolder(@NonNull View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.name);
                rating=itemView.findViewById(R.id.rating);
                view=itemView.findViewById(R.id.views);
                button=itemView.findViewById(R.id.button);
                imageView=itemView.findViewById(R.id.image);
            }
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

            View layoutView = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.video_card, viewGroup, false);
            return new RecyclerAdapter.ItemViewHolder((layoutView));


        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            RecyclerAdapter.ItemViewHolder itemViewHolder = (RecyclerAdapter.ItemViewHolder) viewHolder;
            VideoData holidays = (VideoData) listRecyclerItem.get(i);
            itemViewHolder.name.setText(holidays.getTitle());
            itemViewHolder.view.setText(holidays.getViews());
            itemViewHolder.rating.setText(holidays.getRating());
            Picasso.get().load(holidays.getThumbnail()).fit().into(itemViewHolder.imageView);
            itemViewHolder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + holidays.getYouTubeId()));
                    Intent webIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://www.youtube.com/watch?v=" + holidays.getYouTubeId()));
                    try {
                        context.startActivity(appIntent);
                    } catch (ActivityNotFoundException ex) {
                        context.startActivity(webIntent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return listRecyclerItem.size();
        }
    }
}