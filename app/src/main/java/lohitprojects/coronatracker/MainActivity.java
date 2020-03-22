package lohitprojects.coronatracker;
/**
 * Created By Lohit Kumar - LinkedIn - LohitKumar01
 */

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.navigation.ui.AppBarConfiguration;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private AppBarConfiguration mAppBarConfiguration;
    private TextView tvTotalcases, tvTotalrecovered, tvTotalDeaths, tvSearchByCountry, tvAllCountriesData, currentDate;
    private ImageView btnCountrySearch;
    private String apiUrl = "https://corona.lmao.ninja/all";
    Iterator<String> keysIterator;
    private String totCase ="";
    private String totDeaths ="";
    private String totrecovered ="";
    Date date=java.util.Calendar.getInstance().getTime();  ;
    protected void onStart() {
        super.onStart();

        ConnectivityManager ConnectionManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = ConnectionManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()==true )
        {

        }
        else
        {
            Toast.makeText(MainActivity.this, "Turn ON your internet to see data!", Toast.LENGTH_LONG).show();

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = findViewById(R.id.toolbar);
      //  setSupportActionBar(toolbar);
        tvTotalcases = findViewById(R.id.tvTotalCases);
        tvTotalrecovered = findViewById(R.id.tvTotalrecovered);
        tvTotalDeaths = findViewById(R.id.tvTotalDeaths);
        btnCountrySearch = findViewById(R.id.btnCountrySearch);
        tvSearchByCountry = findViewById(R.id.tvSearchByCountry);
       // tvAllCountriesData = findViewById(R.id.tvAllCountriesData);
        currentDate = findViewById(R.id.currentDate);
        currentDate.setText("Last Updated On: "+date);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
         mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        tvTotalcases.setOnClickListener(this);
        tvTotalrecovered.setOnClickListener(this);
        tvTotalDeaths.setOnClickListener(this);
        tvSearchByCountry.setOnClickListener(this);
        btnCountrySearch.setOnClickListener(this);
   //     tvAllCountriesData.setOnClickListener(this);
        loadData();
    }

    private void loadData() {
        String full_url = apiUrl;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                full_url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            keysIterator =response.keys();
                            for(int i =0; keysIterator.hasNext();i++)
                            {
                                while (keysIterator.hasNext()) {
                                    String keyStr = (String) keysIterator.next();
                                    String valueStr = response.getString(keyStr);
                                if(keyStr.equalsIgnoreCase("cases"))
                                {
                                    totCase = valueStr;
                                    tvTotalcases.setText(valueStr);
                                }
                                if(keyStr.equalsIgnoreCase("recovered"))
                                {
                                    totrecovered = valueStr;
                                    tvTotalrecovered.setText(valueStr);
                                }
                                if(keyStr.equalsIgnoreCase("deaths"))
                                {
                                    totDeaths = valueStr;
                                    tvTotalDeaths.setText(valueStr);
                                }
                                }
                            }
                    } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }
        );
                MySingelton.getInstance(MainActivity.this).addToRequestQue(jsonObjectRequest);
            }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onClick(View view) {
        if(view == tvTotalcases){
            Toast.makeText(MainActivity.this,"Cases "+totCase,Toast.LENGTH_LONG).show();
        }
        if(view == tvTotalrecovered){
            Toast.makeText(MainActivity.this,"Recoveries "+totrecovered,Toast.LENGTH_LONG).show();
        }
        if(view == tvTotalDeaths){
            Toast.makeText(MainActivity.this,"Deaths "+totDeaths,Toast.LENGTH_LONG).show();
        }
        if(view == tvSearchByCountry || view == btnCountrySearch){
            startActivity(new Intent(MainActivity.this, DataByCountry.class));
        }
//        if(view == tvAllCountriesData){
//            startActivity(new Intent(MainActivity.this, DataofAllCountries.class));
//        }


    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        return false;
    }
}
