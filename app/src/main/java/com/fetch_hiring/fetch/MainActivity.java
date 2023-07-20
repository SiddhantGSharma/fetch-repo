package com.fetch_hiring.fetch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    //to display on the UI
    private RecyclerView recyclerView;
    private DataAdapter dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the layout for the activity to the XML layout file
        setContentView(R.layout.activity_main);

        // Find the RecyclerView in the activity's layout and set its layout manager
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchData();
    }

    // Method to fetch data from a URL on a separate thread to avoid blocking the UI thread
    private void fetchData() {
        // Create a new Executor with a single thread to execute tasks asynchronously
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            String dataJsonString = getDataFromUrl("https://fetch-hiring.s3.amazonaws.com/hiring.json");

            if (dataJsonString != null) {
                // Process the retrieved data here
                List<DataItem> dataItems = parseJson(dataJsonString);

                if (dataItems != null) {
                    // Apply grouping, sorting, and filtering
                    List<DataItem> sortedAndFilteredItems = processItems(dataItems);

                    runOnUiThread(() -> {
                        // Create a new DataAdapter with the sorted and filtered data
                        dataAdapter = new DataAdapter(sortedAndFilteredItems);
                        // Set the adapter on the RecyclerView to display the data on the UI
                        recyclerView.setAdapter(dataAdapter);
                    });
                }
            }
        });
    }

    // Method to retrieve data from the specified URL and return it as a String
    private String getDataFromUrl(String urlString) {
        HttpURLConnection urlConnection = null; // Used to establish the connection with the URL
        BufferedReader reader = null; // Used to read the data from the URL
        String dataJsonString = null; // The resulting JSON data as a String

        try {
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection(); // Open a connection to the URL
            urlConnection.setRequestMethod("GET"); // Set the HTTP request method to GET
            urlConnection.connect(); // Connect to the URL

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();

            if (inputStream == null) {
                // Nothing to do.
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            // Read the data line by line from the BufferedReader
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty. No point in parsing.
                return null;
            }

            dataJsonString = buffer.toString();
        } catch (IOException e) {
            // An error occurred while trying to retrieve the data from the URL
            Log.e(TAG, "Error", e);
            return null;
        } finally {
            // Close the URL connection and BufferedReader
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(TAG, "Error closing stream", e);
                }
            }
        }

        return dataJsonString;
    }

    // takes the JSON data as a string and uses the Gson library to convert it into a list of DataItem objects
    private List<DataItem> parseJson(String dataJsonString) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<DataItem>>() {}.getType();
        return gson.fromJson(dataJsonString, type);
    }

    private List<DataItem> processItems(List<DataItem> dataItems) {
        // Group the items by "listId" filtering out "null" values
        List<DataItem> groupedItems = dataItems.stream()
                .filter(item -> item.getName() != null && !item.getName().isEmpty())
                .collect(Collectors.groupingBy(DataItem::getListId))
                .values()
                .stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());

        // Sort the results first by "listId" then by "name"
        Collections.sort(groupedItems, Comparator
                .comparingInt(DataItem::getListId)
                .thenComparing(DataItem::getName));

        return groupedItems;
    }
}