package com.example.blood_bank_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.widget.TextView;

import com.example.blood_bank_project.Adapter.RequestAdapter;
import com.example.blood_bank_project.Adapter.SearchAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SearchResults extends AppCompatActivity {
    List<Donor> donorList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_search_results);

        donorList=new ArrayList<> ();

        String json;
        String city,blood_group;
        Intent intent=getIntent ();

        json=intent.getStringExtra ("json");
        city=intent.getStringExtra ("city");
        blood_group=intent.getStringExtra ("blood_group");

        TextView heading=findViewById (R.id.heading);
        String str=" Donor in " +city+ " with blood group " +blood_group;
        heading.setText (str);

        Gson gson=new Gson ();
        Type type=new TypeToken<List<Donor>> () {}.getType ();

        List<Donor>data_models=gson.fromJson (json,type);
        if(data_models!=null && data_models.isEmpty ()){
            heading.setText ("No Results");
        }
        else if(data_models!=null){
            donorList.addAll (data_models);
        }

         RecyclerView recyclerView = findViewById (R.id.recycler_view);
         RecyclerView.LayoutManager layoutManager = new LinearLayoutManager
                (this, RecyclerView.VERTICAL, false);
         recyclerView.setLayoutManager (layoutManager);

         SearchAdapter adapter=new SearchAdapter (donorList,SearchResults.this);
         recyclerView.setAdapter (adapter);

    }
}
