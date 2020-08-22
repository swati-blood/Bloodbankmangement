package com.example.blood_bank_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.blood_bank_project.Utils.EndPoints;
import com.example.blood_bank_project.Utils.VolleySingleton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {
EditText et_blood_group,et_city;
Button submit_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_search);
        et_blood_group=findViewById (R.id.et_blood_group);
        et_city=findViewById (R.id.et_city);
        submit_button=findViewById (R.id.submit_button);
        submit_button.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                String blood_group=et_blood_group.getText ().toString ();
                String city=et_city.getText ().toString ();
                if(isValid(blood_group,city)){
                    get_search_results(blood_group,city);
                }
            }

            private void get_search_results(final String blood_group, final String city) {

                StringRequest stringRequest=new StringRequest
                        (Request.Method.POST, EndPoints.search_donors, new Response.Listener<String> ( ) {
                            @Override
                            public void onResponse(String response) {
                                Intent intent= new Intent (SearchActivity.this,SearchResults.class);
                                intent.putExtra ("city",city);
                                intent.putExtra ("blood_group",blood_group);
                                intent.putExtra ("json",response);
                                startActivity (intent);
                            }
                        }, new Response.ErrorListener ( ) {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText (SearchActivity.this, "Something  went wrong", Toast.LENGTH_SHORT).show ( );

                            }
                        }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String>params=new HashMap<> ();
                        params.put ("city",city);
                       params.put ("blood_group",blood_group);

                        return  params;
                    }
                };
                VolleySingleton.getInstance (SearchActivity.this).addToRequestQueue (stringRequest);
            }


            private boolean isValid(String blood_group, String city) {
                List<String >valid_blood_groups=new ArrayList<> ();
                valid_blood_groups.add("A+");
                valid_blood_groups.add("A-");
                valid_blood_groups.add("AB+");
                valid_blood_groups.add("AB-");
                valid_blood_groups.add("B+");
                valid_blood_groups.add("B-");
                valid_blood_groups.add("O+");
                valid_blood_groups.add("O-");
                if(!valid_blood_groups.contains (blood_group)){
                    showmsg("Blood group invalid choose from"+valid_blood_groups);
                return  false;
                } else if (city.isEmpty ()) {
                    showmsg("Enter city name");
                    return  false;

                }
            return true;
            }
        });

    }
    private void showmsg(String msg){
        Toast.makeText (this, msg, Toast.LENGTH_SHORT).show ( );
    }
}
