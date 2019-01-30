package com.luckydrive.web.controller;

import java.sql.Time;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.luckydrive.model.CarInfo;
import com.luckydrive.model.FavouriteRoute;
import com.luckydrive.model.Point;
import com.luckydrive.model.Tag;
import com.luckydrive.model.Trip;
import com.luckydrive.model.TripStatus;
import com.luckydrive.model.User;
import com.luckydrive.repository.CarInfoRepository;
import com.luckydrive.repository.CarRulesRepository;
import com.luckydrive.repository.FavouriteRouteRepository;
import com.luckydrive.repository.PointRepository;
import com.luckydrive.repository.TagRepository;
import com.luckydrive.repository.TripRepository;
import com.luckydrive.repository.UserRepository;


@RestController
//@RequestMapping("/you")
public class MainController {

	@Autowired
	UserRepository userRepository;
	@Autowired
	CarInfoRepository carInfoRepository;
	@Autowired
	CarRulesRepository carRulesRepository;
	@Autowired
	FavouriteRouteRepository favouriteRouteRepository;
	@Autowired
	PointRepository pointRepository;
	@Autowired
	TripRepository tripRepository;
	@Autowired
	TagRepository tagRepository;
	
	@RequestMapping(value = "/info", method = RequestMethod.GET)
	public String isItWorking(@RequestParam("name") final String name) {
		return "Run away from IBA, " + name;
	}

	@RequestMapping(value = "/allusers", method = RequestMethod.GET)
	public @ResponseBody Iterable<User> getAllUsers() {
		// This returns a JSON or XML with the users
	    // just for test
		return userRepository.findAll();
    }
	
	/* Search trips methods:
	 * - get driver's trips
	 * - search by two points, start and end
	 * - search by existing tags*/
    
	/**
	 * Return all trips created by driver
	 * FOR DRIVER
	 * @param userId
	 * @return
	 */
    @RequestMapping(value="/driver/get/trips", 
            method=RequestMethod.GET,
            produces = "application/json")
    public @ResponseBody String getDriversTrips(@RequestParam("user") Long userId) {
        List<Trip> trips = null;
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            trips = tripRepository.getDriversTrips(userId);
        }
        // create json object manually
        JSONObject tripsJson = new JSONObject();
        JSONArray array = new JSONArray();
        if (trips == null)
            trips = new LinkedList<Trip>();
        else {
            JSONObject json = null;
            for (Trip trip: trips) {
                json = new JSONObject();
                json.put("driverName", trip.getDriver().getName());
                json.put("driverSurname", trip.getDriver().getSurname());
                JSONObject startPoint = new JSONObject();
                startPoint.put("lat", trip.getStartPoint().getLat());
                startPoint.put("lng", trip.getStartPoint().getLng());
                startPoint.put("address", trip.getStartPoint().getAddress());
                startPoint.put("radius", trip.getStartPoint().getRadius());
                json.put("startPoint", startPoint);
                JSONObject endPoint = new JSONObject();
                endPoint.put("lat", trip.getEndPoint().getLat());
                endPoint.put("lng", trip.getEndPoint().getLng());
                endPoint.put("address", trip.getEndPoint().getAddress());
                endPoint.put("radius", trip.getEndPoint().getRadius());
                json.put("endPoint", endPoint);
                json.put("departureTimeLowerBound", trip.getDepartureTimeLowerBound());
                json.put("departureTimeUpperBound", trip.getDepartureTimeUpperBound());
                json.put("dateOfDeparture", trip.getDateOfDeparture());
                json.put("currentNumberOfPassengers", trip.getCurrentNumberOfPassengers());
                json.put("comments", trip.getComments());
                array.put(json);
            }
            tripsJson.put("trips", array);
        }
        return tripsJson.toString();
    }
	
	/**
	 * Find trips from work location point to home
	 * FOR PASSENGER
	 * @param address
	 * @param lat
	 * @param lng
	 * @param radius
	 * @return
	 */
    @RequestMapping(value="/find/trips/home", 
            params = { "address", "lat", "lng", "radius" }, 
            method=RequestMethod.GET,
            produces = "application/json")
    public @ResponseBody String findTripsToHomeByPoint(@RequestParam("address") String address, 
            @RequestParam("lat") double lat, 
            @RequestParam("lng") double lng, 
            @RequestParam("radius") double radius)
    {
        // /find/trips/home?address=partizanskaya&lat=53.868758&lng=27.645422&radius=0.03
    	Iterable<Trip> trips = tripRepository.findTripByClosestEndPoints(lat, lng, radius);
        if (trips == null)
            return "{\"trips\":[]}";
        else {
            // create json object manually
            return createJsonStringFromTrips(trips).toString();
        }
    }
    
    /**
     * Find trips from home to work location point
     * FOR PASSENGER
     * @param address
     * @param lat
     * @param lng
     * @param radius
     * @return
     */
    @RequestMapping(value="/find/trips/work", 
            params = { "address", "lat", "lng", "radius" }, 
            method=RequestMethod.GET,
            produces = "application/json")
    public @ResponseBody String findTripsToWorkByPoint(@RequestParam("address") String address, 
            @RequestParam("lat") double lat, 
            @RequestParam("lng") double lng, 
            @RequestParam("radius") double radius)
    {
        Iterable<Trip> trips = tripRepository.findTripByClosestStartPoints(lat, lng, radius);
        if (trips == null)
            return "{\"trips\":[]}";
        else {
            // create json object manually
            return createJsonStringFromTrips(trips).toString();
        }
    }
    
    /**
     * Find trips by input text
     * FOR PASSENGER
     * @param tags
     * @return
     */
    @RequestMapping(value="/find/trips/tags", 
            method=RequestMethod.GET,
            produces = "application/json",
            consumes = "application/json")
    public @ResponseBody String findTripsByTags(@RequestBody Set<String> tags) {
//        [
//        "Uruchye",
//        "Borisovsky trakt",
//        "Partizanskaya"
//        ]
    	Iterable<Trip> trips = tripRepository.findTripByTags(tags);
    	if (trips == null)
    	    return "{\"trips\":[]}";
    	else {
    	    // create json object manually
    	    return createJsonStringFromTrips(trips).toString();
    	}
    }
    
    /**
     * Add passenger to trip
     * FOR PASSENGER
     * @param userId
     * @param tripId
     */
    @RequestMapping(value="/add/passenger",
            params = { "user", "trip" }, 
            method=RequestMethod.GET)
    public void addPassengerToTrip(@RequestParam("user") Long userId, 
            @RequestParam("trip") Long tripId) {
        // /add/passenger?user=3&trip=2
        System.out.println();
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            Optional<Trip> trip = tripRepository.findById(tripId);
            // if user who wants to join the trip is not a driver of
            // this trip
            if (trip.isPresent() && !trip.get().getDriver().getUserId().equals(userId)) {
                Trip updatedTrip = trip.get();
                updatedTrip.addPassenger(user.get());
                updatedTrip.incrementPasseger();
                tripRepository.save(updatedTrip);
                User updatedUser = user.get();
                updatedUser.addTrip(updatedTrip);
                userRepository.save(updatedUser);
            }
        }
    }
    
    /**
     * Delete passenger from trip
     * FOR DRIVER AND PASSENGER
     * @param userId
     * @param tripId
     */
    @RequestMapping(value="/delete/passenger",
            params = { "driver", "user", "trip" }, 
            method=RequestMethod.GET)
    public void deletePassengerToTrip(@RequestParam("driver") Long driverId, 
            @RequestParam("user") Long userId,
            @RequestParam("trip") Long tripId) {
        // /delete/passenger?driver=2&user=3&trip=39
        System.out.println();
        Optional<User> driver = userRepository.findById(driverId);
        Optional<User> user = userRepository.findById(userId);
        if (driver.isPresent() && user.isPresent()) {
            Optional<Trip> trip = tripRepository.findById(tripId);
            // if user who wants to join the trip is not a driver of
            // this trip
            if (!trip.get().getDriver().getUserId().equals(userId)) {
                Trip updatedTrip = trip.get();
                updatedTrip.removePassenger(user.get());
                updatedTrip.decrementPasseger();
                tripRepository.save(updatedTrip);
            }
        }
    }
    /**
     * Update trip
     * FOR DRIVER
     * @param userId
     * @param tripId
     * @param trip
     */
    @RequestMapping(value="/update/trip",
            params = { "user", "trip" }, 
            method=RequestMethod.POST,
            consumes = "application/json")
    public void updateTrip(@RequestParam("user") Long userId, 
            @RequestParam("trip") Long tripId, 
            @RequestBody Trip trip) {
        // /add/passenger?user=1&trip=1
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            trip.setTripId(tripId);
            tripRepository.save(trip);
        }
    }
    
    /**
     * Add passenger to trip
     * FOR PASSENGER
     * @param userId
     * @param tripId
     */
    @RequestMapping(value="/update/info",
            params = { "user" }, 
            method=RequestMethod.POST,
            consumes = "application/json")
    public void updateUserInfo(@RequestParam("user") Long userId, 
            @RequestParam("time") Time usualTime,
            @RequestBody Set<FavouriteRoute> routes
            ) {
        // /add/passenger?user=1&trip=1
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            
            
            // update user
            userRepository.save(user.get());
            
            
        }
    }
    
    /* Save entities methods:
     * - create trip for driver (also when creating trip, saving it's
     *   tags and all the points)
     * - create driver's car info
     * - save favourite route for passengers 
     * 
     * Any user:
     * - logs in
     * - choose role: driver or passenger
     * - modify account info
     * 
     * 
     * Driver:
     * - creates trip
     * - deletes trip
     * - updates trip
     * - creates car info and  car rules
     * - updates car info and  car rules
     * - deletes car info and  car rules
     * 
     * 
     * Passenger:
     * - adds favourite routes
     * - sends request with 2 points, tags and time to find trips
     * 
     * 
     * */
    
    /** 
     * Create new point (for test for a while)
     * @param point
     */
    @RequestMapping(value="/save/point", 
            method=RequestMethod.POST,
            consumes = "application/json")
    public void createPoint(@RequestBody Point point) {
    	// JSON 
        // {
    	//	"lat": 27.6954545,
    	//	"lng": "53.9246919",
    	//	"address": "work", 
    	//  "radius": 0 (not necessary)
    	// }
    	pointRepository.save(point);
    }  
    
    /**
     * Create new trip (only if user is a driver)
     * @param userId
     * @param trip
     */
    @RequestMapping(value="/create/trip", 
    		params = { "user" }, 
            method=RequestMethod.POST,
            consumes = "application/json")
    public void createTrip(@RequestParam("user") Long userId, 
            @RequestBody Trip trip) {
    	// JSON 
        // {
    	//  "startPoint": {
        //        "lat": 27.6954545,
    	//	      "lng": "53.9246919",
    	//	      "address": "work", 
    	//        "radius": 0
        //  },
    	//  "endPoint": {
        //        "lat": 27.6954545,
    	//	      "lng": "53.9246919",
    	//	      "address": "home", 
    	//        "radius": 0
        //  },
    	//  "departureTimeLowerBound": "14:30:00",
    	//  "departureTimeUpperBound": "15:30:00",
    	//  "dateOfDeparture": "2019-02-02",
    	//  "limitNumberOfPassengers": 3,
    	//  "comments": "",
    	//  "intermediatePoints": {
    	//     {
        //        "lat": 27.6954545,
    	//	      "lng": "53.9246919",
    	//	      "address": "work", 
    	//        "radius": 0
        //     },
    	//     {
        //        "lat": 27.6954545,
    	//	      "lng": "53.9246919",
    	//	      "address": "work", 
    	//        "radius": 0
        //     }
    	//   },
    	//   "tags": {
    	//       {
    	//        "tagName": "Uruchye",
    	//        "tagPoint": {
        //               "lat": 27.6954545,
    	//	             "lng": "53.9246919",
    	//	             "address": "Uruchye", 
    	//               "radius": 0
    	//         }
    	//       },
    	//       {
    	//        "tagName": "Octyabrskaya",
    	//        "tagPoint": {
        //               "lat": 27.6954545,
    	//	             "lng": "53.9246919",
    	//	             "address": "Octyabrskaya", 
    	//               "radius": 0
    	//         }
    	//       }
    	//   }
    	// }
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
        	trip.setStatus(TripStatus.ACTIVE);
            pointRepository.save(trip.getStartPoint());
            pointRepository.save(trip.getEndPoint());
            pointRepository.saveAll(trip.getIntermediatePoints());
            tagRepository.saveAll(trip.getTags());
            tripRepository.save(trip);
        }
    }
    
    /**
     * Add car information (only if user as a driver)
     * @param userId
     * @param carInfo
     */
    @RequestMapping(value="/create/carinfo",
    		params = { "user" }, 
            method=RequestMethod.POST,
            consumes = "application/json")
    public void createCarInfo(@RequestParam("user") Long userId,
            @RequestBody CarInfo carInfo) {
    	// JSON 
        // {
    	//	"carMark": "Bentley",
    	//	"carNumber": "1111-AK7",
    	//	"carColor": "green", 
    	//  "carRules": {
        //       "smokingAllowed": false,
        //       "foodAllowed": true,
    	//       "comments": "im major"
        //  }
    	// }
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            carRulesRepository.save(carInfo.getCarRules());
            carInfo.setUser(user.get());
            carInfoRepository.save(carInfo);
        }
    }
    
    /**
     * Save favourite route (only if user as a passenger)
     * if it is needed to save two points
     * route is added also in editing user info
     * @param userId
     * @param route
     */
    @RequestMapping(value="/save/favroute", 
    		params = { "user" }, 
            method=RequestMethod.POST,
            consumes = "application/json")
    public void saveFavouriteRoute(@RequestParam("user") Long userId, 
            @RequestBody FavouriteRoute route) {
    	// JSON 
        // {
    	//	"departmentPoint": {
        //        "lat": 27.6954545,
    	//	      "lng": "53.9246919",
    	//	      "address": "work", 
    	//        "radius": 0
    	//  },
    	//	"destinationPoint": {
        //        "lat": 27.6954545,
    	//	      "lng": "53.9246919",
    	//	      "address": "work", 
    	//        "radius": 0
    	//  }
    	// }
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            pointRepository.save(route.getDepartmentPoint());
            pointRepository.save(route.getDestinationPoint());
            route.setUser(user.get());
            favouriteRouteRepository.save(route);
        }
    }
    
    /**
     * Save favourite route (only if user as a passenger)
     * if it is needed to only one destination point
     * route is added also in editing user info
     * @param userId
     * @param destinationPoint
     */
    @RequestMapping(value="/save/point/route",
    		params = { "user" },
            method=RequestMethod.POST,
            consumes = "application/json")
    public void addPointAsFavouriteRoute(@RequestParam("user") Long userId, 
    		@RequestBody Point destinationPoint) {
    	// JSON 
        // {
    	//	"lat": 27.6954545,
    	//	"lng": "53.9246919",
    	//	"address": "work", 
    	//  "radius": 0 (not necessary)
    	// }
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
        	Point departmentPoint = user.get()
        			.getOrganization()
        			.getLocationPoints()
        			.stream()
        			.findFirst()
        			.get();
            pointRepository.save(destinationPoint);
            FavouriteRoute route = new FavouriteRoute (user.get(), departmentPoint, destinationPoint);
            favouriteRouteRepository.save(route);
        }
    }
    
    
    public String createJsonStringFromTrips(Iterable<Trip> trips) {
        // create json object manually
        JSONObject tripsJson = new JSONObject();
        JSONArray tripsJsonArray = new JSONArray();
        JSONObject tripJson = null;
        for (Trip trip: trips) {
            tripJson = new JSONObject();
            tripJson.put("driverName", trip.getDriver().getName());
            tripJson.put("driverSurname", trip.getDriver().getSurname());
            JSONObject startPointJson = new JSONObject();
            startPointJson.put("lat", trip.getStartPoint().getLat());
            startPointJson.put("lng", trip.getStartPoint().getLng());
            startPointJson.put("address", trip.getStartPoint().getAddress());
            startPointJson.put("radius", trip.getStartPoint().getRadius());
            tripJson.put("startPoint", startPointJson);
            JSONObject endPointJson = new JSONObject();
            endPointJson.put("lat", trip.getEndPoint().getLat());
            endPointJson.put("lng", trip.getEndPoint().getLng());
            endPointJson.put("address", trip.getEndPoint().getAddress());
            endPointJson.put("radius", trip.getEndPoint().getRadius());
            tripJson.put("endPoint", endPointJson);
            tripJson.put("departureTimeLowerBound", trip.getDepartureTimeLowerBound());
            tripJson.put("departureTimeUpperBound", trip.getDepartureTimeUpperBound());
            tripJson.put("dateOfDeparture", trip.getDateOfDeparture());
            tripJson.put("currentNumberOfPassengers", trip.getCurrentNumberOfPassengers());
            tripJson.put("limitNumberOfPassengers", trip.getLimitNumberOfPassengers());
            JSONArray intermdPointsJsonArray = new JSONArray();
            JSONObject intermPointJson = null;
            for(Point intermPoint: trip.getIntermediatePoints()) {
                intermPointJson = new JSONObject();
                intermPointJson.put("lat", intermPoint.getLat());
                intermPointJson.put("lng", intermPoint.getLng());
                intermPointJson.put("address", intermPoint.getAddress());
                intermPointJson.put("radius", intermPoint.getRadius());
                intermdPointsJsonArray.put(intermPointJson);
            }
            tripJson.put("intermediatePoints", intermdPointsJsonArray);
            JSONArray tagJsonArray = new JSONArray();
            JSONObject tagJson = null;
            for(Tag tag: trip.getTags()) {
                tagJson = new JSONObject();
                tagJson.put("tagName", tag.getTagName());
                JSONObject tagPointJson = new JSONObject();
                tagPointJson.put("lat", tag.getTagPoint().getLat());
                tagPointJson.put("lng", tag.getTagPoint().getLng());
                tagPointJson.put("address", tag.getTagPoint().getAddress());
                tagPointJson.put("radius", tag.getTagPoint().getRadius());
                tagJson.put("tagPoint", tagPointJson);
                tagJsonArray.put(tagJson);
            }
            tripJson.put("tags", tagJsonArray);
            tripJson.put("comments", trip.getComments());
            tripsJsonArray.put(tripJson);
        }
        tripsJson.put("trips", tripsJsonArray);
        return tripsJson.toString();
    }
    
}
