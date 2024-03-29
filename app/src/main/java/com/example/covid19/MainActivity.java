package com.example.covid19;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.covid19.api.ApiUtilities;
import com.example.covid19.api.CountryData;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextView totalConfirm, totalActive, totalRecovered, totalDeath, totalTests, totalPopolation;
    private TextView todayConfirm, todayRecovered, todayDeath, dateTV;
    private PieChart pieChart;
    private TextView wish;

    private List<CountryData> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wish = findViewById(R.id.wish);

        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        TextView wish=(TextView) findViewById(R.id.wish);
        String message="";

        if(timeOfDay < 12){
            message=("Good Morning!");
        }else if(timeOfDay < 16){
            message=("Good Afternoon!");
        }else if(timeOfDay < 21){
            message=("Good Evening!");
        }else {
            message=("Good Night!");
        }
        wish.setText(message);




        list = new ArrayList<>();

        init();

        ApiUtilities.getApiInterface().getCountryData()
                .enqueue(new Callback<List<CountryData>>() {
                    @Override
                    public void onResponse(Call<List<CountryData>> call, Response<List<CountryData>> response) {
                        list.addAll(response.body());

                        for (int i=0; i<list.size(); i++){
                            if (list.get(i).getCountry().equals("India")){
                                int confirm = Integer.parseInt (list.get(i).getCases());
                                int active = Integer.parseInt (list.get(i).getActive());
                                int recovered = Integer.parseInt (list.get(i).getRecovered());
                                int death = Integer.parseInt (list.get(i).getDeaths());
                                int population = Integer.parseInt (list.get(i).getPopulation());

                                totalConfirm.setText(NumberFormat.getInstance().format(confirm));
                                totalActive.setText(NumberFormat.getInstance().format(active));
                                totalRecovered.setText(NumberFormat.getInstance().format(recovered));
                                totalDeath.setText(NumberFormat.getInstance().format(death));
                                totalPopolation.setText(NumberFormat.getInstance().format(population));

                                todayDeath.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayDeaths())));
                                todayConfirm.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayCases())));
                                todayRecovered.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayRecovered())));
                                totalTests.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTests())));

                                setText(list.get(i).getUpdated());

                                pieChart.addPieSlice(new PieModel("Confirm", confirm,getResources().getColor(R.color.yellow)));
                                pieChart.addPieSlice(new PieModel("Active", active,getResources().getColor(R.color.blue_pie)));
                                pieChart.addPieSlice(new PieModel("Recovered", recovered,getResources().getColor(R.color.green_pie)));
                                pieChart.addPieSlice(new PieModel("Death", death,getResources().getColor(R.color.red_pie)));
                                pieChart.startAnimation();

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<CountryData>> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Error :"+t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void setText(String updated) {
        DateFormat format = new SimpleDateFormat("MMM dd,yyyy");

        long milliseconds = Long.parseLong(updated);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);

        dateTV.setText("Updated at "+format.format(calendar.getTime()));
    }

    private void init(){

        totalConfirm = findViewById(R.id.totalConfirm);
        totalActive = findViewById(R.id.totalActive);
        totalRecovered = findViewById(R.id.totalRecovered);
        totalDeath = findViewById(R.id.totalDeath);
        todayConfirm = findViewById(R.id.todayConfirm);
        totalTests = findViewById(R.id.totalTests);
        todayRecovered = findViewById(R.id.todayRecovered);
        todayDeath = findViewById(R.id.todayDeath);
        totalPopolation = findViewById(R.id.totalPopulation);
        pieChart = findViewById(R.id.pieChart);
        dateTV = findViewById(R.id.date);

        wish = findViewById(R.id.wish);

    }
}