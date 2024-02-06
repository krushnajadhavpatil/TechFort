package com.example.techfort.Activity;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.techfort.Api.ApiHandler;
import com.example.techfort.Api.ApiService;
import com.example.techfort.Api.PostData;
import com.example.techfort.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Compiler extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public final String TAG = Compiler.class.getSimpleName();

    TextView tvResult;
    EditText etInput;
    Button btnSubmit;
    Spinner dropdown;
    String[] items = new String[]{"c", "python3", "cpp14", "csharp", "java", "kotlin", "go"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compiler);

        tvResult = findViewById(R.id.tv_result);
        etInput = findViewById(R.id.et_input);
        btnSubmit = findViewById(R.id.btn_submit);
        dropdown = findViewById(R.id.spinner1);

        //Toolbar
        Toolbar setupToolbar = findViewById(R.id.compilerToolbar);
        setSupportActionBar(setupToolbar);
        getSupportActionBar().setTitle("Compiler");



        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(this);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "in onclick");

                ApiService apiService = ApiHandler.getRetrofitInstance();
                Call<String> execute = apiService.execute(new PostData(etInput.getText().toString()));

                tvResult.setText("Loading...");

                execute.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {

                        tvResult.setText("");

                        try {

                            if (response.isSuccessful()) {

                                JSONObject responseJson = new JSONObject(response.body().toString());
                                String output = responseJson.getString("output");
                                tvResult.setText(output);

                            } else {

                                Toast.makeText(Compiler.this, response.errorBody().string(), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            Toast.makeText(Compiler.this, "Failed Parsing JSON : " + e.getMessage(), Toast.LENGTH_SHORT).show();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                        tvResult.setText("Failed");
                        Toast.makeText(Compiler.this, "Failed : " + t.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ApiHandler.LANGUAGE = items[position];
        Log.d(TAG, items[position]);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}