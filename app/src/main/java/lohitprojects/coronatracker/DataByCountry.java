package lohitprojects.coronatracker;
/**
 * Created By Lohit Kumar -   - LohitKumar01
 */
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class DataByCountry extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private EditText countrySearchTxt;
    private Button searchCountry;
    private String apiUrl = "https://corona.lmao.ninja/countries/";
    private String countryName="";
    private TextView tvcountryName,countryCases, countryTodayCases, countryTotDeaths, countryTodayDeath,
            countryTotRecovered, countryTotActive,countryTotCritical, countryTotcasesPerMillion;

    private Spinner spin;
    String[] country;
    private ArrayList<String> listCountries = new ArrayList<>();
    private static final String TAG = "DataByCountry";
    protected void onStart() {
        super.onStart();

        ConnectivityManager ConnectionManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = ConnectionManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()==true )
        {

        }
        else
        {
            Toast.makeText(DataByCountry.this, "Turn ON your internet to see data!", Toast.LENGTH_LONG).show();

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_by_country);
//        countrySearchTxt = findViewById(R.id.countrySearchTxt);
//        searchCountry = findViewById(R.id.btnCountrySearch);

        tvcountryName = findViewById(R.id.countryName);
        countryCases = findViewById(R.id.countryCases);
        countryTodayCases = findViewById(R.id.countryTodayCases);
        countryTotDeaths = findViewById(R.id.countryTotDeaths);
        countryTodayDeath = findViewById(R.id.countryTodayDeath);
        countryTotRecovered = findViewById(R.id.countryTotRecovered);
        countryTotActive = findViewById(R.id.countryTotActive);
        countryTotCritical = findViewById(R.id.countryTotCritical);
        countryTotcasesPerMillion = findViewById(R.id.countryTotcasesPerMillion);

         spin = (Spinner) findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(this);


        getAllCountryData();
//        searchCountry.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                countryName = countrySearchTxt.getText().toString();
//                loadCountryData(countryName);
//            }
//        });
    }

    private void loadCountryData(String countryName) {

                String full_url = apiUrl+countryName;
              // Toast.makeText(DataByCountry.this, " Full URL"+full_url, Toast.LENGTH_SHORT).show();
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                        full_url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    Iterator<String> keysIterator = response.keys();
                                    while (keysIterator.hasNext()) {
                                            String keyStr = (String) keysIterator.next();
                                            String valueStr = response.getString(keyStr);
                                            if(keyStr.equalsIgnoreCase("country"))
                                            {
                                                tvcountryName.setText(valueStr);
                                            }
                                            if(keyStr.equalsIgnoreCase("cases"))
                                            {
                                                countryCases.setText(valueStr);
                                            }
                                            if(keyStr.equalsIgnoreCase("todayCases"))
                                            {
                                                countryTodayCases.setText(valueStr);
                                            }
                                            if(keyStr.equalsIgnoreCase("deaths"))
                                            {
                                                countryTotDeaths.setText(valueStr);
                                            }
                                            if(keyStr.equalsIgnoreCase("todayDeaths"))
                                            {
                                                countryTodayDeath.setText(valueStr);
                                            }
                                            if(keyStr.equalsIgnoreCase("recovered"))
                                            {
                                                countryTotRecovered.setText(valueStr);
                                            }
                                            if(keyStr.equalsIgnoreCase("active"))
                                            {
                                                countryTotActive.setText(valueStr);
                                            }
                                            if(keyStr.equalsIgnoreCase("critical"))
                                            {
                                                countryTotCritical.setText(valueStr);
                                            }
                                            if(keyStr.equalsIgnoreCase("casesPerOneMillion"))
                                            {
                                                countryTotcasesPerMillion.setText(valueStr);
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
                MySingelton.getInstance(DataByCountry.this).addToRequestQue(jsonObjectRequest);
    }

    private void getAllCountryData() {

        String full_url = apiUrl;
     //   Toast.makeText(DataByCountry.this, " Full URL"+full_url, Toast.LENGTH_SHORT).show();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                full_url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for(int i=0;i<response.length();i++) {
                            JSONObject ob = response.getJSONObject(i);
                            Iterator<String> keysIterator = ob.keys();
                            while (keysIterator.hasNext()) {
                                String keyStr = (String) keysIterator.next();
                                String valueStr = ob.getString(keyStr);
                                if (keyStr.equalsIgnoreCase("country")) {
                                    listCountries.add(valueStr);
                                }
                            }
                        }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i(TAG,"Country List"+listCountries);
                        loadMySpinner(listCountries);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }
        );
        MySingelton.getInstance(DataByCountry.this).addToRequestQue(jsonArrayRequest);


    }

    private void loadMySpinner(ArrayList<String> listCountries) {
       // country = (String[]) listCountries.toArray();
        country = listCountries.toArray(new String[listCountries.size()]);
        Arrays.sort(country);
        ArrayAdapter aa = new ArrayAdapter(this,R.layout.spinner_dropdown_item,country);
        aa.setDropDownViewResource(R.layout.spinner_dropdown_item_two);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(getApplicationContext(), country[i], Toast.LENGTH_LONG).show();
        loadCountryData(country[i]);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
