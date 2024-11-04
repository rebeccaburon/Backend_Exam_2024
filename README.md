# PROJECT

##### This project is a RESTful service provider built with the Javalin framework in Java.

##### It demonstrates various functionalities,

##### including data management using DAOs, API error handling, JPA persistence, and testing.

# Task 3: Building a REST Service Provider with Javalin
## Document the output from my HTT request:

### Get all trips.

[
{
"id": 1,
"name": "Mountain Hiking Adventure",
"price": 120,
"category": "FOREST",
"start_time": [9, 0],
"end_time": [11, 0],
"start_position": "Mountain Base"
},
{
"id": 2,
"name": "Historical City Tour",
"price": 85,
"category": "CITY",
"start_time": [10, 0],
"end_time": [12, 30],
"start_position": "City Center"
},
{
"id": 3,
"name": "Sunset River Cruise",
"price": 95,
"category": "SEA",
"start_time": [15, 0],
"end_time": [18, 0],
"start_position": "Harbor"
}
]

### Get a trip by its id 3

{
"id": 3,
"name": "Sunset River Cruise",
"price": 95,
"category": "SEA",
"start_time": [15, 0],
"end_time": [18, 0],
"start_position": "Harbor"
}

### Create a new trip

It has been tested in the HTTP file, and the Array of Trips has been created.

### Update information about a trip.

It has been tested in the HTTP file, Trips has been updated.

### Delete a trip.

It has been tested in the HTTP file, Trips has been deleted.

### Add an existing guide to and existing trip.

It has been tested in the HTTP file, and the Array of hTrips has been deleted.

#### Observation

When i call the get all, the Guids, do not apear, and is something that i needed to add.
The table, that should be created when adding Guids to Trips, is not added correctly.

### Populate the database with trips and guides.

From the HTTP file:
The following trips and guids were created successfully.
Response file saved. > 2024-11-04T122540.201.json

## Theoretical question: Why do we suggest a PUT method for adding a guide to a trip instead of a POST method?

With put, i can update an existing Guide, to an existing Trip instead of haveing to 
POST a new Guide. 

# Routes:

| HTTP method | REST Ressource                         | Exceptions and status(es) |
|-------------|----------------------------------------|---------------------------|
| GET         | `/api/trips`                           | Statuscode 204 / 400      |
| GET         | `/api/trips/{id}`                      | Statuscode 404 / 400      |
| POST        | `/api/trips `                          | Statuscode 204 / 400      |
| PUT         | `/api/trips/{id}`                      | Statuscode   400          |
| DELET       | `/api/trips/{id}`                      | Statuscode   400          |
| PUT         | `/api/trips/{tripId}/guides/{guideId}` | Statuscode  204/400       |
| POST        | `/api/trips/populate`                  | Statuscode   400          |


# Task 4: REST Error Handling
##### Getting a trip by id, if the trip does not exist

{
"time of error": "2024-11-04 12:41:13",
"error message": {
"status": 400,
"message": "Trip with id 1 not found"
}
}

# Task  I have not made:
## Task 8: Security
## Task 6: Getting additional data from API
##  Task 7: Testing REST Endpoints - 7.4 Create a test method for each of the endpoints.

