package com.example.calendarapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.Inflater;

public class SocShowActivity extends AppCompatActivity {
    int i;
    String id,gmail,fb,desc;
    Boolean k=false;
    ImageView imageView;
    RecyclerView recyclerView;
    LinearLayout linearLayout;
    LayoutInflater layoutInflater;
    RecyclerView.Adapter adapter;
    List<ListItems> listItems =new ArrayList<>();
    TextView soc_name,soc_desc,view_more,defaultText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soc_show);
        soc_name=findViewById(R.id.soc_name);
        imageView=findViewById(R.id.image);
        soc_desc=findViewById(R.id.soc_desc);
        view_more=findViewById(R.id.view_more);
        defaultText=findViewById(R.id.default_text);
        linearLayout=findViewById(R.id.linearLayout);

        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(SocShowActivity.this));


        view_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(k){
                    view_more.setText("View More");
                    adapter=new SocietyCardAdaptor(listItems.subList(0,3), new ClickListener() {
                        @Override
                        public void onPositionClicked(int position) {

                        }

                        @Override
                        public void onLongClicked(int position) {

                        }
                    }, SocShowActivity.this,"eventPage");
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
                else {
                    view_more.setText("View Less");
                    adapter=new SocietyCardAdaptor(listItems, new ClickListener() {
                        @Override
                        public void onPositionClicked(int position) {

                        }

                        @Override
                        public void onLongClicked(int position) {

                        }
                    }, SocShowActivity.this,"eventPage");
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
                k=!k;
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        Intent intent =getIntent();
        id=intent.getExtras().getString("id");
        addBackImage(imageView);
        getEventData();
        getCordiData();
    }
    public void addBackImage(ImageView imageView){
        if(id.equals("effervescence")){
            soc_name.setTextSize(TypedValue.COMPLEX_UNIT_SP,50);
            soc_desc.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
            imageView.setImageResource(R.drawable.effe_background);
        }
        if(id.equals("asmita")){
            imageView.setImageResource(R.drawable.asmita_background);
        }
        if(id.equals("gymkhana")) {
            soc_name.setTextSize(TypedValue.COMPLEX_UNIT_SP,50);
            soc_desc.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
            imageView.setImageResource(R.drawable.gymkhana_background);
        }
        if(id.equals("dance")) {
            soc_name.setTextSize(TypedValue.COMPLEX_UNIT_SP,50);
            soc_desc.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
            imageView.setImageResource(R.drawable.image);
        }
        if(id.equals("virtuosi"))
            imageView.setImageResource(R.drawable.virtuosi_background);
        if(id.equals("rangtarangini")) {
            soc_name.setTextSize(TypedValue.COMPLEX_UNIT_SP,45);
            soc_desc.setTextSize(TypedValue.COMPLEX_UNIT_SP,35);
            imageView.setImageResource(R.drawable.rang_background);
        }
        if(id.equals("nirmiti"))
            imageView.setImageResource(R.drawable.nirmiti_background);
        if(id.equals("ams"))
            imageView.setImageResource(R.drawable.ams_background);
        if(id.equals("sarasva"))
            imageView.setImageResource(R.drawable.sarasva_background);
        if(id.equals("sports"))
            imageView.setImageResource(R.drawable.spirit_background);
        if(id.equals("iiic"))
            imageView.setImageResource(R.drawable.iiic_background);
        if(id.equals("aparoksha")) {
            soc_name.setTextSize(TypedValue.COMPLEX_UNIT_SP,50);
            soc_desc.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
            imageView.setImageResource(R.drawable.apk_background);
        }
        if(id.equals("geekhaven")) {
            soc_name.setTextSize(TypedValue.COMPLEX_UNIT_SP,50);
            soc_desc.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
            imageView.setImageResource(R.drawable.geekhaven_background);
        }
        if(id.equals("tesla")) {
            imageView.setImageResource(R.drawable.tesla_background);
            soc_desc.setTextSize(TypedValue.COMPLEX_UNIT_SP,35);
        }
    }
    public void addCordisToView(ArrayList<String> arrayList){
//        View view=getLayoutInflater().inflate(R.layout.soc_profile_cordi,linearLayout,false);
//        TextView name=view.findViewById(R.id.cordi_name);
//        name.setText(arrayList.get(0));
//        linearLayout.addView(view);
        for(i=0;i<arrayList.size();i++){

            if(i%2==0){
                View view=getLayoutInflater().inflate(R.layout.soc_profile_cordi,null);
                TextView name=view.findViewById(R.id.cordi_name);
                name.setText(arrayList.get(i));
                linearLayout.addView(view);
            }
            else{
                View view=getLayoutInflater().inflate(R.layout.soc_profile_cordi_alt,null);
                TextView name=view.findViewById(R.id.cordi_name);
                name.setText(arrayList.get(i));
                linearLayout.addView(view);
            }
        }

    }
    public void getCordiData(){
        String url = "https://socupdate.herokuapp.com/societies";
        final ProgressDialog progressDialog = new ProgressDialog(SocShowActivity.this);
        progressDialog.setMessage("Loading data....");
        progressDialog.show();
        StringRequest stringRequest =new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    ArrayList<String> cordis = new ArrayList<>();
                    JSONObject json =new JSONObject(response);
                    Iterator<String> iterator= json.keys();
                    while (iterator.hasNext()){
                        String key = iterator.next();
                        JSONObject jsonObject = json.getJSONObject(key);
                        if(key.equals(id)) {
                            if (jsonObject.has("coordinators")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("coordinators");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    cordis.add(jsonArray.getString(i));
                                }
                            }
                            gmail=jsonObject.getString("email");
                            desc=jsonObject.getString("name");
                            soc_desc.setText(jsonObject.getString("desc"));
                            soc_name.setText(desc);
                            break;

                        }
                    }
                    addCordisToView(cordis);
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(SocShowActivity.this);
        requestQueue.add(stringRequest);
    }
    public void getEventData(){
        String url = "https://socupdate.herokuapp.com/societies/"+id+"/events";
        final ProgressDialog progressDialog = new ProgressDialog(SocShowActivity.this);
        progressDialog.setMessage("Loading data....");
        progressDialog.show();
        StringRequest stringRequest =new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json =new JSONObject(response);
                    Iterator<String> iterator= json.keys();
                    while (iterator.hasNext()){
                        String key = iterator.next();
                        JSONObject jsonObject = json.getJSONObject(key);
                        ArrayList<String> attachmentsList =new ArrayList<>();
                        if(jsonObject.has("attachments")){
                            JSONArray jsonArray = jsonObject.getJSONArray("attachments");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                attachmentsList.add(jsonArray.getString(i));
                            }
                        }
                        ListItems items = new ListItems(
                                jsonObject.getString("name"),
                                jsonObject.getString("desc"),
                                jsonObject.getString("byName"),
                                "Date: "+jsonObject.getString("date"),
                                "Time: "+jsonObject.getString("time"),
                                "Venue: "+jsonObject.getString("venue"),"",key,attachmentsList
                        );
                        listItems.add(items);
                    }

                    if(listItems.size()==0){
                        defaultText.setVisibility(View.VISIBLE);
                        view_more.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                    }
                    else if(k || listItems.size()<=3){
                        recyclerView.setVisibility(View.VISIBLE);
                        if(listItems.size()<=3)
                            view_more.setVisibility(View.GONE);
                        adapter=new SocietyCardAdaptor(listItems, new ClickListener() {
                            @Override
                            public void onPositionClicked(int position) {

                            }

                            @Override
                            public void onLongClicked(int position) {

                            }
                        }, SocShowActivity.this,"eventPage");
                    }
                    else{
                        recyclerView.setVisibility(View.VISIBLE);
                        adapter=new SocietyCardAdaptor(listItems.subList(0,3), new ClickListener() {
                            @Override
                            public void onPositionClicked(int position) {

                            }

                            @Override
                            public void onLongClicked(int position) {

                            }
                        }, SocShowActivity.this,"eventPage");
                    }
                    recyclerView.setAdapter(adapter);
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(SocShowActivity.this);
        requestQueue.add(stringRequest);
    }
}
