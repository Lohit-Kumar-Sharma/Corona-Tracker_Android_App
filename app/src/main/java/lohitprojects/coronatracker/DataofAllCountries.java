package lohitprojects.coronatracker;
/**
 * Created By Lohit Kumar - LinkedIn - LohitKumar01
 */
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
public class DataofAllCountries extends AppCompatActivity {

    private ListView listView;
    public ArrayList<HashMap<String, String>> arrayList;
    private static final String TAG = "DataofAllCountries";

    String allCountryUrl = "https://corona.lmao.ninja/countries";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dataof_all_countries);
        listView = (ListView) findViewById(R.id.listviewbt);
        showAllCountryData();


    }

    private void showAllCountryData() {
        Toast.makeText(DataofAllCountries.this,"Fetching Corona data from all Over the world, Kindly Wait",Toast.LENGTH_LONG).show();
        arrayList = new ArrayList<HashMap<String, String>>();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                allCountryUrl,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Do something with response
                        // Process the JSON

                        try{
                            // Loop through the array elements
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                HashMap<String, String> map = new HashMap<>();
                                JSONObject ob = response.getJSONObject(i);
                                Iterator<String> keysIterator = ob.keys();
                                while (keysIterator.hasNext()) {
                                    String jsonKey = keysIterator.next();
                                    String jsonVal = ob.getString(jsonKey);

                                    map.put(jsonKey,jsonVal);
                                }
                                arrayList.add(map);
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                        Log.i(TAG,"ArrayList"+arrayList);
                        passData(arrayList);
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred
                         Toast.makeText(DataofAllCountries.this,"Check Internet Connection",Toast.LENGTH_SHORT).show();
                    }
                }
        );
        MySingelton.getInstance(DataofAllCountries.this).addToRequestQue(jsonArrayRequest);



    }

    private void passData(ArrayList<HashMap<String, String>> arrayList) {
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);
    }
}
