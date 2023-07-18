package com.example.ef_beatrizambrosio;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private EditText anioEditText;
    private Spinner paisSpinner;
    private Button obtenerHolidaysButton;
    private ListView holidayListView;

    private ApiService apiService;
    private DbUtils dbUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        anioEditText = findViewById(R.id.yearEditText);
        paisSpinner = findViewById(R.id.countrySpinner);
        obtenerHolidaysButton = findViewById(R.id.obtenerFeriados);
        holidayListView = findViewById(R.id.holidaysListView);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://date.nager.at/api/v3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

        List<String> countries = new ArrayList<>();
        countries.add("PE");
        ArrayAdapter<String> paisAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, countries);
        paisAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        paisSpinner.setAdapter(paisAdapter);
        dbUtils = new DbUtils(this);

        obtenerHolidaysButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String anioStr = anioEditText.getText().toString();
                int Anio = Integer.parseInt(anioStr);
                String selectedCountry = paisSpinner.getSelectedItem().toString();
                obtenerFeriadosAPI(Anio, selectedCountry);
            }
        });
        // Mostrar los feriados almacenados en la ListView
        List<Holiday> storedHolidays = dbUtils.getAllHolidays();
        ArrayAdapter<Holiday> holidayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, storedHolidays);
        holidayListView.setAdapter(holidayAdapter);

    }
    private void obtenerFeriadosAPI(int year, String country) {
        Call<List<Holiday>> call = apiService.getHolidays(year, country);
        call.enqueue(new Callback<List<Holiday>>() {
            @Override
            public void onResponse(Call<List<Holiday>> call, Response<List<Holiday>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Holiday> holidays = response.body();

                    // Almacena los feriados en la base de datos
                    dbUtils.insertHolidays(holidays);

                    // Actualiza la ListView con los feriados almacenados
                    List<Holiday> storedHolidays = dbUtils.getAllHolidays();
                    ArrayAdapter<Holiday> holidayAdapter = new ArrayAdapter<>(MainActivity.this,
                            android.R.layout.simple_list_item_1, storedHolidays);
                    holidayListView.setAdapter(holidayAdapter);
                }
            }
            @Override
            public void onFailure(Call<List<Holiday>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

}