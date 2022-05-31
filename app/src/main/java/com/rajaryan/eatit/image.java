package com.rajaryan.eatit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;

public class image extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        ImageView image=findViewById(R.id.image);
        TextView name=findViewById(R.id.name);
        String name1=getIntent().getStringExtra("name");
        name.setText(name1);
        String id=getIntent().getStringExtra("id");
        OkHttpClient client = new OkHttpClient();
        String status="s";
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url("https://api.spoonacular.com/recipes/"+id+"/card?apiKey=13eccaaea6a54c3289ff66823a024077")
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
                    String link=json.getString("url");
                    ImageView image=findViewById(R.id.image);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Picasso.get().load(link).fit().into(image);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("res", myresponse);
            }
        });
        Button share=findViewById(R.id.send);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Drawable mDrawable = image.getDrawable();
                Bitmap mBitmap = ((BitmapDrawable) mDrawable).getBitmap();

                String path = MediaStore.Images.Media.insertImage(getContentResolver(), mBitmap, "Image Description", null);
                Uri uri = Uri.parse(path);

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(intent, "Share Image"));
            }
        });
    }

    public void back(View view) {
        onBackPressed();
    }
}