# Flight Service

## Endpoints:

GET ALL FLIGHTS (user): /search/flights

SEARCH FLIGHTS BY LOCATION INFO: /search/flightsbylocation
- So for example, /search/flightsbylocation?originId=LAX&destinationId=JFK

GET ALL ROUTES: /utopia_airlines/routes

GET SINGLE ROUTE: utopia_airlines/routes/{id}

GET ALL FLIGHTS (admin): /flightservice/flights

GET SINGLE FLIGHT, UPDATE/DELETE SINGLE FLIGHT: /flightservice/flights/{id}

CREATE NEW FLIGHT: /flightservice/flights
