package com.example.calendarapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.calendarapp.AdaptorActivity.ViewHolder;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.zip.Inflater;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class MainActivity2 extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    TextView tvStudentName, tvStudentEmailId;
    NavController navController;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    ImageView imgUser;
    MenuItem addEvent;
    FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;
    private RecyclerView recyclerView;
    private CardView cardView;
    private RecyclerView.Adapter adapter;
    private List<ListItems> listItems;
    private static String URL_DATA="https://socupdate.herokuapp.com/events";
    private static String url ="https://socupdate.herokuapp.com/societies/check";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mAuth=FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("271594298370-jmsnpsmnhm1ahm6viiag2gi2dnpqn0lg.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        subscribeToTopic();
        sendIdToken();
        drawerLayout=findViewById(R.id.drawerMainActivity);
        navigationView=findViewById(R.id.navigation_main);

        tvStudentName=navigationView.getHeaderView(0).findViewById(R.id.tvNavHeaderStudentName);
        tvStudentEmailId=navigationView.getHeaderView(0).findViewById(R.id.tvNavHeaderStudentId);
        imgUser=navigationView.getHeaderView(0).findViewById(R.id.imgNavHeaderUser);

        if(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()!=null){
            Picasso
                    .get()
                    .load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl())
                    .resize(92,92)
                    .transform(new CropCircleTransformation())
                    .into(imgUser);
        }

        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        tvStudentName.setText(user.getDisplayName());
        tvStudentEmailId.setText(user.getEmail());

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        mAppBarConfiguration=new AppBarConfiguration.
                Builder(R.id.navigation_nav_main, R.id.navigation_nav_profile, R.id.navigation_nav_subscription, R.id.navigation_nav_society)
                .setDrawerLayout(drawerLayout).build();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.nav_log_out: {
                            signOut();
                            break;
                        }
                        case R.id.nav_home: {
                            if(navController.getCurrentDestination().getId()!=R.id.navigation_nav_main)
                                navController.navigate(R.id.navigation_nav_main);
                            break;
                        }
                        case R.id.nav_subscription: {
//                        navController.navigate(R.id.navigation_nav_subscription);
//                        addEvent.setVisible(false);
//                        Intent intent =new Intent(getApplicationContext(),SubscribeActivity.class);
//                        intent.putExtra("val",2);
//                        startActivity(intent);
                            Intent i = new Intent(getApplicationContext(), SubscribeActivity.class);
                            i.putExtra("val", 2);
                            if (Build.VERSION.SDK_INT > 20) {
//                            ActivityOptions options =
//                                    ActivityOptions.makeSceneTransitionAnimation(MainActivity2.this);
//                            startActivity(i,options.toBundle());
                                startActivity(i);
                            } else {
                                startActivity(i);
                            }
                            break;
                        }
                        case R.id.nav_profile: {
                            if(navController.getCurrentDestination().getId()!=R.id.navigation_nav_profile)
                                navController.navigate(R.id.navigation_nav_profile);
                            break;
                        }
                        case R.id.nav_societies: {
                            if(navController.getCurrentDestination().getId()!=R.id.navigation_nav_society)
                                navController.navigate(R.id.navigation_nav_society);
                            break;
                        }
                    }
                drawerLayout.close();
                return false;
            }
        });
    }
    public void subscribeToTopic(){
        FirebaseMessaging.getInstance().subscribeToTopic("Event")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Subscribed to Event";
                        if (!task.isSuccessful()) {
                            msg = getString(R.string.msg_subscribe_failed);
                        }
                        Log.d("VARUN", msg);
                        Toast.makeText(MainActivity2.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
        FirebaseMessaging.getInstance().subscribeToTopic("iit2019091")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Subscribed to Event";
                        if (!task.isSuccessful()) {
                            msg = getString(R.string.msg_subscribe_failed);
                        }
                        Log.d("VARUN", msg);
                        Toast.makeText(MainActivity2.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void sendIdToken(){
//        final TextView textView = findViewById(R.id.check);
        final String[] token = new String[1];
        mAuth=FirebaseAuth.getInstance();
        final FirebaseUser user= mAuth.getCurrentUser();
        if(user!=null) {
            user.getIdToken(true)
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                Log.d("VARUN BHARDWAJ IDTOKEN", Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getToken()));
                                token[0] =task.getResult().getToken();
                                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newPlainText("idtoken", token[0]);
                                clipboard.setPrimaryClip(clip);
                                HashMap<String,String> map=new HashMap<String, String>();
                                map.put("token",token[0]);
                                final ProgressDialog progressDialog = new ProgressDialog(MainActivity2.this);
                                progressDialog.setMessage("Loading data....");
                                progressDialog.show();
                                RequestQueue requstQueue = Volley.newRequestQueue(MainActivity2.this);

                                JsonObjectRequest jsonobj = new JsonObjectRequest(Request.Method.POST, url,new JSONObject(map),
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                progressDialog.dismiss();
                                                try {
//                                                    textView.setText(response.getString("society"));
                                                    if(response.getString("society").equals("true")){
                                                        addEvent.setVisible(true);
                                                    }
                                                    progressDialog.dismiss();
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {

                                            }
                                        }
                                );
                                requstQueue.add(jsonobj);

                            } else
                                token[0] = "0";
                        }
                    });
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        addEvent=menu.findItem(R.id.add_event);
        return true;
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    @Override
    public void onBackPressed() {
        Intent homeScreenIntent = new Intent(Intent.ACTION_MAIN);
        homeScreenIntent.addCategory(Intent.CATEGORY_HOME);
        homeScreenIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(homeScreenIntent);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.add_event){
            startActivity(new Intent(MainActivity2.this,AddEventActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
    public void signOut(){
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(MainActivity2.this,"LogOut Successful",Toast.LENGTH_SHORT).show();
                        mAuth.signOut();
                        startActivity(new Intent(MainActivity2.this, LoginActivity.class));

                    }
                });
    }
}