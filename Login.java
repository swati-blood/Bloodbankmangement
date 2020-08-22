package com.example.blood_bank_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.blood_bank_project.Utils.EndPoints;
import com.example.blood_bank_project.Utils.VolleySingleton;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    EditText numberEt,passwordEt;
    Button submit_button, register;

    Context context;

    ImageView login_logo;
    TextView guest;
    private static ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_login);
        //logo
        login_logo=findViewById (R.id.login_logo);

        //code
        context = Login.this;



        guest=findViewById (R.id.guest);
        guest.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, Guest.class);
                startActivity(i);

            }
        });

        numberEt = findViewById(R.id.username);
        passwordEt = findViewById(R.id.password);
        register = findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this, Registartion.class);
                startActivity(i);
            }
        });

        submit_button = findViewById(R.id.submit_button);
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               numberEt.setError (null);
               passwordEt.setError (null);

               String number=numberEt.getText ().toString ();
               String password=passwordEt.getText ().toString ();

               if(isValid (number,password)){
                   login(number,password);
               }
                }
        });
    }
    private  void login(final String number, final String password){
        StringRequest stringRequest=new StringRequest (Request.Method.POST, EndPoints.login_url, new Response.Listener<String> ( ) {
            @Override
            public void onResponse(String response) {
                if (!response.contains ("Invalid Credentials")) {
                    Toast.makeText (Login.this, response, Toast.LENGTH_SHORT).show ( );
                  startActivity (new Intent (Login.this,Navigation.class));
                  PreferenceManager.getDefaultSharedPreferences (getApplicationContext ()).edit ()
                          .putString ("number",number).apply ();
                    PreferenceManager.getDefaultSharedPreferences (getApplicationContext ()).edit ()
                            .putString ("city",response).apply ();
                  Login.this.finish ();
                }else {
                    Toast.makeText (Login.this, response, Toast.LENGTH_SHORT).show ( );
                }
            }
        }, new Response.ErrorListener ( ) {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText (Login.this, "Something  went wrong", Toast.LENGTH_SHORT).show ( );

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>params=new HashMap<> ();

                params.put ("number",number);
                params.put ("password",password);

                return  params;
            }
        };
        VolleySingleton.getInstance (this).addToRequestQueue (stringRequest);
    }


   private boolean isValid(String number,String  password){
        if(number.isEmpty ()){
            showMessage ("Empty Mobile Number");
            numberEt.setError ("Empty Mobile Number");
            return false;
        }else if(password.isEmpty ()){
            showMessage ("Empty Password");
        passwordEt.setError ("Empty Password");
            return false;
        }return true;
   }
   private void showMessage(String msg){
       Toast.makeText (context, msg, Toast.LENGTH_SHORT).show ( );
   }
}
