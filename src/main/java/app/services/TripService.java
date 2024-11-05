package app.services;
import app.exceptions.ApiException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class TripService {
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public TripService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper(); // object used to parse JSON data
        objectMapper.registerModule(new JavaTimeModule()); // Handle ZoneDate Time
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Dates remain in ISO format
    }

    // Modify this method to return a List of packing items
    public List<?> fetchPackingItems(String category) {
        try {
            if (List.of("beach", "city", "forest", "lake", "sea", "snow").contains(category.toLowerCase())) {

                // Make request URI
                URI uri = new URI("https://packingapi.cphbusinessapps.dk/packinglist/" + category);

                // HTTP request
                HttpRequest request = HttpRequest.newBuilder(uri).GET().build();

                // Send HTTP request and get the response
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200) {

                    // Parse JSON response into List of items
                    return objectMapper.readTree(response.body()).get("items").traverse(objectMapper)
                            .readValueAs(List.class);
                } else {
                    throw new ApiException(response.statusCode(), "Error fetching category items: " + category);
                }
            } else {
                throw new ApiException(HttpURLConnection.HTTP_NOT_FOUND, "Invalid category: " + category);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Unexpected error occurred: " + e.getMessage(), e);
        }
    }
}
