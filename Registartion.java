package com.example.blood_bank_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearSnapHelper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.interfaces.StringRequestListener;
import com.example.blood_bank_project.Utils.EndPoints;
import com.example.blood_bank_project.Utils.VolleySingleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Registartion extends AppCompatActivity {
    EditText nameEt,passwordEt,idEt,cityEt,bloodGroupEt,mobileEt;
    Button submitButton;
    TextView textView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_registartion);
        nameEt =findViewById (R.id.name);
        passwordEt=findViewById (R.id.password);
        idEt=findViewById (R.id.id);
        cityEt=findViewById (R.id.city);
        bloodGroupEt=findViewById (R.id.blood_group);
        mobileEt=findViewById (R.id.number);

        submitButton=findViewById (R.id.submit_button);


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
           String name,city,blood_group,password,mobile,id;
                name=nameEt.getText ().toString ();
                city=cityEt.getText ().toString ();
                blood_group=bloodGroupEt.getText ().toString ();
                password=passwordEt.getText ().toString ();
                mobile=mobileEt.getText ().toString ();
                id=idEt.getText ().toString ();
              if(isValid (name,city,blood_group,password,mobile,id)){
register (name,city,blood_group,password,mobile,id);
              }
            }
        });

    }

    private void register(final String name, final String city, final String blood_group, final String password,
                          final String mobile, final String id){
        StringRequest stringRequest=new StringRequest (Request.Method.POST, EndPoints.register_url, new Response.Listener<String> ( ) {
            @Override
            public void onResponse(String response) {
                if (response.contains("success")) {
                    PreferenceManager.getDefaultSharedPreferences (getApplicationContext ()).edit ()
                            .putString ("city",city).apply ();
                   Intent i=new Intent (Registartion.this,Login.class);
                   startActivity (i);

                }else {
                    Toast.makeText (Registartion.this, response, Toast.LENGTH_SHORT).show ( );
                }
            }
        }, new Response.ErrorListener ( ) {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText (Registartion.this, "Something went wrong", Toast.LENGTH_SHORT).show ( );
                Log.d ("VOLLEY",error.getMessage ());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>params=new HashMap<> ();
                params.put ("name",name);
                params.put ("city",city);
                params.put ("blood_group",blood_group);
                params.put ("password",password);
                params.put ("number",mobile);
                params.put ("email",id);

                return  params;
            }
        };
        VolleySingleton.getInstance (this).addToRequestQueue (stringRequest);
    }
private  boolean isValid(String name,String city,String blood_group, String password,String mobile,String id){
    List<String> valid_blood_group=new ArrayList<> ();
    valid_blood_group.add("A+");
    valid_blood_group.add("A-");
    valid_blood_group.add("B+");
    valid_blood_group.add("B-") ;
    valid_blood_group.add("AB+");
    valid_blood_group.add("AB-") ;
    valid_blood_group.add("O+");
    valid_blood_group.add("O-");



        if(name.isEmpty ()){
            showMesage ("name is empty");
            return  false;
        }else if(city.isEmpty ()){
            showMesage ("city name is required");
            return false;
        }else if (!valid_blood_group.contains(blood_group)){
            showMesage ("Blood group invalid choose from "+valid_blood_group);
            return  false;
        }else if(password.isEmpty ()){
            showMesage ("password is empty");
            return false;
        }
        else if(mobile.length ()!=10){
            showMesage ("invalid mobile number");
            return false;
        }else if(id.isEmpty ()){
            showMesage ("Email id is empty");
            return false;
        }

        return true;
}
   private void showMesage(String  msg){
       Toast.makeText (this, msg, Toast.LENGTH_SHORT).show ( );
   }

    }

