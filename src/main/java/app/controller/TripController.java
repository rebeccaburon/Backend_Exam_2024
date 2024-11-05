package app.controller;

import app.enums.Category;
import app.populator.Populator;
import app.dao.TripDAO;
import app.dto.GuideDTO;
import app.dto.TripDTO;
import app.exceptions.ApiException;
import app.services.TripService;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class TripController {
    private TripDAO tripDAO;
    private Populator populator;
    private TripService tripService;

    public TripController(EntityManagerFactory emf) {
        this.tripDAO = new TripDAO(emf);
        this.populator = new Populator(emf);
        this.tripService = new TripService();
    }

    //adding a Populator method

    public void populateDatabase(Context ctx) {
        try {
            List<TripDTO> tripsList = TripDTO.toDTOList(populator.create5Trips());
            List<GuideDTO> guidsList = GuideDTO.toGuideDTOList(populator.create5Guides());

            populator.create5Trips();
            populator.create5Guides();

            if (!tripsList.isEmpty() && !guidsList.isEmpty()) {
                ctx.status(201);
                ctx.json(tripsList);
                ctx.json(guidsList);
                ctx.result("The following trips and guids were created successfully.");
            }

        } catch (Exception e) {
            throw new ApiException(400, e.getMessage());
        }
    }

    public void createTrips(Context ctx) {
        try {
            TripDTO[] newTrips = ctx.bodyAsClass(TripDTO[].class);
            TripDTO[] savedTrips = new TripDTO[newTrips.length];

            int i = 0;
            for (TripDTO trip : newTrips) {
                TripDTO savedTrip = tripDAO.create(trip);
                savedTrips[i] = savedTrip;
                i++;
            }
            ctx.res().setStatus(201);
            ctx.json(savedTrips, TripDTO.class);
        } catch (Exception e) {
            throw new ApiException(400, e.getMessage());
        }
    }

    public void getAllTrips(Context ctx) {
        List<TripDTO> listofTrips = tripDAO.getAll();

        try {
            if (listofTrips.isEmpty()) {
                throw new ApiException(204, "No CONCTENT! No Trips was found in the database");
            } else {
                ctx.status(200);
                ctx.json(listofTrips);
            }
        } catch (ApiException e) {
            throw new ApiException(400, e.getMessage());
        }
    }

    public void getTripById(Context ctx) {
        Long tripId = Long.parseLong(ctx.pathParam("id"));
        try {
            TripDTO tripDTO = tripDAO.getById(tripId);
            if (tripDTO != null) {

                //Fetching packing items
                String category = tripDTO.getCategory().name().toLowerCase();
                List<?> packingItems = tripService.fetchPackingItems(category);

                ctx.status(200);
                ctx.json(tripDTO);
                ctx.json(packingItems);
            } else {
                throw new ApiException(204, "No CONTEN! Trip  with id " + tripId + " was not found");
            }
        } catch (ApiException e) {
            e.printStackTrace(); //adding for debugging
            throw new ApiException(400, e.getMessage());
        }
    }

    public void updateTrip(Context ctx) {
        try {
            Long tripId = Long.parseLong(ctx.pathParam("id"));
            TripDTO tripDTOUpdate = ctx.bodyAsClass(TripDTO.class);
            tripDTOUpdate.setId(tripId);

            TripDTO updatedTrip = tripDAO.update(tripDTOUpdate);
            ctx.res().setStatus(200);
            ctx.json(updatedTrip, TripDTO.class);
        } catch (EntityNotFoundException e) {
            throw new ApiException(400, e.getMessage());

        }
    }

    public void deleteTrip(Context ctx) {
        try {
            Long tripId = Long.parseLong(ctx.pathParam("id"));
            tripDAO.delete(tripId);
            ctx.status(204);
            ctx.result("Trip is deleted successfully");
        } catch (Exception e) {
            throw new ApiException(400, e.getMessage());
        }
    }

    public void addGuidToTrip(Context ctx) {
        try {
            Long tripId = Long.parseLong(ctx.pathParam("tripId"));

            Long guideId = Long.parseLong(ctx.pathParam("guideId"));

            TripDTO tripWithGuid = tripDAO.addGuideToTrip(tripId, guideId);

            if (tripId != 0 && guideId != 0) {
                ctx.status(200);
                ctx.json(tripWithGuid, TripDTO.class);
                ctx.result("New guide " + guideId + " added successfully");
            } else {
                throw new ApiException(204, "NO CONTENT! Trip with id " + tripId + " was not added");
            }

        } catch (ApiException e) {
            throw new ApiException(400, e.getMessage());
        }
    }


    public void getTripsByGuide(Context ctx) {
        try {
            Long guideId = Long.parseLong(ctx.pathParam("guideId"));
            List<TripDTO> tripsByGuid = tripDAO.getTripsByGuide(guideId);
            if (tripsByGuid.isEmpty()) {
                throw new ApiException(204, "No Content! No Trips by Guid, with id " + guideId + " was  found");
            } else {
                ctx.status(200);
                ctx.json(tripsByGuid);
            }
        } catch (ApiException e) {
            throw new ApiException(400, e.getMessage());
        }
    }

    //adding getTripsByCateGory

    public void getTripsByCategory(Context ctx) {
        try {
            String categoryString = ctx.pathParam("category").toUpperCase();
            Category category = Category.valueOf(categoryString); //Converting String to enum

            List<TripDTO> tripsByCategory = tripDAO.getAll().stream()
                    .filter(trip -> trip.getCategory() == category)
                    .toList();

            if (tripsByCategory.isEmpty()) {
                throw new ApiException(204, "No Content! No Trips by Category " + category + " was  found");
            } else {
                ctx.status(200);
                ctx.json(tripsByCategory);
            }
        } catch (Exception e) {
            throw new ApiException(400, e.getMessage());
        }
    }

    //adding getTotalPriceByGuide
    public void getTotalPriceByGuid(Context ctx) {
        try {
            List<TripDTO> trips = tripDAO.getAll();
            List<Map<String, Double>> summary = trips.stream()
                    .filter(trip -> trip.getGuide() != null)
                    .collect(Collectors.groupingBy(
                            trip -> trip.getGuide().getId(),
                            Collectors.summingDouble(TripDTO::getPrice)
                    ))
                    .entrySet().stream() // Stream the grouped result entries
                    .map(entry -> {
                        // Explicitly create the map to match List<Map<String, Double>>
                        Map<String, Double> guideTotal = Map.of(
                                "guideId", entry.getKey().doubleValue(), // Convert Long to Double
                                "totalPrice", entry.getValue()
                        );
                        return guideTotal;
                    })
                    .toList();

            if (!summary.isEmpty()) {
                ctx.status(200);
                ctx.json(summary);
            } else {
                throw new ApiException(204, "No Content! No Guids with a total prices was found ");

            }

        } catch (Exception e) {
            throw new ApiException(400, e.getMessage());
        }
    }

    //adding fetchPackingItems
    public void getPackingItemsByCategory(Context ctx) {
        String category = ctx.pathParam("category");

        try {
            List<?> listOfItems = tripService.fetchPackingItems(category);


            if (listOfItems != null) {
                ctx.status(200);
                ctx.json(listOfItems);
                ctx.result("Packing items fetched sucessfully for category: " + category);

            } else {
                throw new ApiException(400, "ERROR in fetching category" + category);
            }
        } catch (Exception e) {
            ctx.status(500);
            ctx.result("Unexpected error occured: " + e.getMessage());
        }
    }

    //adding getPackingItemWeightSum
    public void getPackingItemsWeightSum(Context ctx) {
        Long tripId = Long.parseLong(ctx.pathParam("id"));

        try {
            TripDTO tripDTO = tripDAO.getById(tripId);

            if (tripDTO != null) {
                // Fetch packing items based on category
                String category = tripDTO.getCategory().name().toLowerCase();
                List<?> packingItems = tripService.fetchPackingItems(category);

                int totalWeight = packingItems.stream()
                        .mapToInt(item -> {
                            // Cast each item to a Map<String, Object> to access fields by name
                            Map<String, Object> itemMap = (Map<String, Object>) item;
                            // Retrieve the weight of the item as an int

                            int weight = (int) itemMap.get("weightInGrams");
                            // Retrieve the quantity of the item as an int
                            int quantity = (int) itemMap.get("quantity");

                            // Calculate the total weight for this item by multiplying weight * quantity
                            int resultOnTotalWeight = weight * quantity;
                            return resultOnTotalWeight;

                        })
                        .sum(); // Sum up all the individual item weights to get the total weight
                ctx.status(200);
                ctx.json(totalWeight);


            } else {
                throw new ApiException(204, "No Content! No totalt weight  was  found");
            }
        } catch (Exception e) {
            throw new ApiException(400, e.getMessage());
        }
    }


}
