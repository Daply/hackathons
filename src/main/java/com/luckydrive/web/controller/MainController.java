package com.luckydrive.web.controller;

import java.security.Principal;
import java.sql.Time;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.luckydrive.config.security.IAuthenticationFacade;
import com.luckydrive.config.security.LuckyDriveUserPrincipal;
import com.luckydrive.model.AdditionPassengerToTrip;
import com.luckydrive.model.CarInfo;
import com.luckydrive.model.CarRules;
import com.luckydrive.model.FavouriteRoute;
import com.luckydrive.model.Notification;
import com.luckydrive.model.Point;
import com.luckydrive.model.RemovingPassengerFromTrip;
import com.luckydrive.model.Tag;
import com.luckydrive.model.Trip;
import com.luckydrive.model.TripStatus;
import com.luckydrive.model.User;
import com.luckydrive.model.UserMode;
import com.luckydrive.repository.CarInfoRepository;
import com.luckydrive.repository.CarRulesRepository;
import com.luckydrive.repository.FavouriteRouteRepository;
import com.luckydrive.repository.HomeAddressRepository;
import com.luckydrive.repository.NotificationRepository;
import com.luckydrive.repository.PointRepository;
import com.luckydrive.repository.TagRepository;
import com.luckydrive.repository.TripRepository;
import com.luckydrive.repository.UserRepository;

@RestController
public class MainController {

	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	
	@Autowired
	private IAuthenticationFacade authenticationFacade;

	@Autowired
	UserRepository userRepository;
	@Autowired
	HomeAddressRepository homeAddressRepository;
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
	NotificationRepository notificationRepository;
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
	
	@RequestMapping(value = "/user", 
	        method = RequestMethod.GET,
	        produces = "application/json")
	public @ResponseBody User getAuthenticatedUser() {
	    // This returns a JSON or XML with the users
	    // just for test
	    Authentication authentication = authenticationFacade.getAuthentication();
	    LuckyDriveUserPrincipal userPrinc = (LuckyDriveUserPrincipal) authentication.getPrincipal();
	    User userParsed = userPrinc.getAppUser();
	    Optional<User> user = userRepository.findById(userParsed.getUserId());
	    return user.get();
	}

	/**
	 * User reads notification -> notification must be deleted
	 */
	@RequestMapping(value = "/read/notification", method = RequestMethod.GET)
	public void userReadNotification() {
	    // This returns a JSON or XML with the users
	    // just for test
	    Authentication authentication = authenticationFacade.getAuthentication();
	    LuckyDriveUserPrincipal userPrinc = (LuckyDriveUserPrincipal) authentication.getPrincipal();
	    User userParsed = userPrinc.getAppUser();
	    Optional<User> userOpt = userRepository.findById(userParsed.getUserId());
	    User user = userOpt.get();
        Notification notification = notificationRepository.findUsersNotification(user.getUserId());
        notificationRepository.delete(notification);
	}
	
	/**
     * Update user info:
     * - home addresses
     * - phone number
     * - avatar picture
     * 
     * @param userId
     * @param tripId
     */
    @RequestMapping(value = "/update/info", params = {
            "user" }, method = RequestMethod.POST, consumes = "application/json")
    public void updateUserInfo(@RequestBody User user) {
        Authentication authentication = authenticationFacade.getAuthentication();
        LuckyDriveUserPrincipal userPrinc = (LuckyDriveUserPrincipal) authentication.getPrincipal();
        User userParsed = userPrinc.getAppUser();
        Optional<User> userOpt = userRepository.findById(userParsed.getUserId());
        User currentUser = userOpt.get();
        currentUser.setPhone(user.getPhone());
        currentUser.setHomeAddresses(user.getHomeAddresses());
        currentUser.setFavouriteRoutes(user.getFavouriteRoutes());
        userRepository.save(currentUser);
    }

    /**
     * Create new trip
     * FOR DRIVER
     * @param userId
     * @param trip
     */
    @RequestMapping(value = "/create/trip",
            method = RequestMethod.POST, 
            consumes = "application/json")
    public String createTrip(@RequestBody Trip trip) {
        Authentication authentication = authenticationFacade.getAuthentication();
        LuckyDriveUserPrincipal userPrinc = (LuckyDriveUserPrincipal) authentication.getPrincipal();
        User userParsed = userPrinc.getAppUser();
        Optional<User> userOpt = userRepository.findById(userParsed.getUserId());
        User user = userOpt.get();
        CarInfo carInfo = carInfoRepository.findDriversCarInfo(user.getUserId());
        if (user.getUserMode().equals(UserMode.DRIVER) &&
                carInfo != null) {
            trip.setStatus(TripStatus.ACTIVE);
            pointRepository.save(trip.getStartPoint());
            pointRepository.save(trip.getEndPoint());
            pointRepository.saveAll(trip.getIntermediatePoints());
            tagRepository.saveAll(trip.getTags());
            tripRepository.save(trip);
            Notification notification = new Notification();
            notification.setMessage("You are driver of trip: \n" +
            "from " + trip.getStartPoint().getAddress() + " \n" +
            "to " + trip.getEndPoint() +
            "Trip starts in 15 minutes ");
            notification.setMessageDate(trip.getDateOfDeparture()); //TODO set date as date of trip -15 minutes
            notification.setUser(trip.getDriver());
            notification.setTrip(trip);
            notification.setRead(false);
            notificationRepository.save(notification);
            return "OK";
        }
        else {
            return "ERROR";
        }
    }
    
    /**
     * Update trip (submit changes)
     * FOR DRIVER
     * @param userId
     * @param trip
     */
    @RequestMapping(value = "/update/trip",
            method = RequestMethod.POST, 
            consumes = "application/json")
    public void updateTrip(@RequestBody Trip trip) {
        Authentication authentication = authenticationFacade.getAuthentication();
        LuckyDriveUserPrincipal userPrinc = (LuckyDriveUserPrincipal) authentication.getPrincipal();
        User userParsed = userPrinc.getAppUser();
        Optional<User> userOpt = userRepository.findById(userParsed.getUserId());
        User user = userOpt.get();
        if (user.getUserMode().equals(UserMode.DRIVER) &&
                trip.getDriver().equals(user)) {
            pointRepository.save(trip.getStartPoint());
            pointRepository.save(trip.getEndPoint());
            pointRepository.saveAll(trip.getIntermediatePoints());
            tagRepository.saveAll(trip.getTags());
            tripRepository.save(trip);
            
            // change all passengers notifications
            List<Notification> notifications = new ArrayList<Notification>();
            for(Notification not: notificationRepository.findAllUsersNotificationsInOneTrip(trip.getTripId())) {
                not.setMessage("You are driver of trip: \n" +
                        "from " + trip.getStartPoint().getAddress() + " \n" +
                        "to " + trip.getEndPoint() +
                        "Trip starts in 15 minutes ");
                not.setMessageDate(trip.getDateOfDeparture()); //TODO set date as date of trip -15 minutes
                notifications.add(not);
            }
            notificationRepository.saveAll(notifications);
        }
    }
    
     /**
      * Delete trip
      * FOR DRIVER
      * @param userId
      * @param trip
      */
     @RequestMapping(value = "/delete/trip",
             method = RequestMethod.POST, 
             consumes = "application/json")
     public void deleteTrip(@RequestBody Trip trip) {
         Authentication authentication = authenticationFacade.getAuthentication();
         LuckyDriveUserPrincipal userPrinc = (LuckyDriveUserPrincipal) authentication.getPrincipal();
         User userParsed = userPrinc.getAppUser();
         Optional<User> userOpt = userRepository.findById(userParsed.getUserId());
         User user = userOpt.get();
         if (user.getUserMode().equals(UserMode.DRIVER) &&
                 trip.getDriver().equals(user)) {
             pointRepository.delete(trip.getStartPoint());
             pointRepository.delete(trip.getEndPoint());
             pointRepository.deleteAll(trip.getIntermediatePoints());
             tagRepository.deleteAll(trip.getTags());
             tripRepository.delete(trip);
             notificationRepository.deleteAllUsersNotificationsInOneTrip(trip.getTripId());
         }
     }
     
     /**
      * Add car information
      * FOR DRIVER
      * @param userId
      * @param carInfo
      */
     @RequestMapping(value = "/create/carinfo", 
             method = RequestMethod.POST, 
             consumes = "application/json")
     public void createCarInfo(@RequestBody CarInfo carInfo) {
         Authentication authentication = authenticationFacade.getAuthentication();
         LuckyDriveUserPrincipal userPrinc = (LuckyDriveUserPrincipal) authentication.getPrincipal();
         User userParsed = userPrinc.getAppUser();
         Optional<User> userOpt = userRepository.findById(userParsed.getUserId());
         User user = userOpt.get();
         if (user.getUserMode().equals(UserMode.DRIVER)) {
             carRulesRepository.save(carInfo.getCarRules());
             carInfoRepository.save(carInfo);
         }
     }
     
     /**
      * Add car information
      * FOR DRIVER
      * @param userId
      * @param carInfo
      */
     @RequestMapping(value = "/update/carinfo", 
             method = RequestMethod.POST, 
             consumes = "application/json")
     public void updateCarInfo(@RequestBody CarInfo carInfo) {
         Authentication authentication = authenticationFacade.getAuthentication();
         LuckyDriveUserPrincipal userPrinc = (LuckyDriveUserPrincipal) authentication.getPrincipal();
         User userParsed = userPrinc.getAppUser();
         Optional<User> userOpt = userRepository.findById(userParsed.getUserId());
         User user = userOpt.get();
         if (user.getUserMode().equals(UserMode.DRIVER)) {
             CarInfo usCarInfo = carInfoRepository.findDriversCarInfo(user.getUserId());
             usCarInfo.setCarColor(carInfo.getCarColor());
             usCarInfo.setCarMark(carInfo.getCarMark());
             usCarInfo.setCarNumber(carInfo.getCarNumber());
             usCarInfo.setCarRules(carInfo.getCarRules());
             CarRules usCarRules = carRulesRepository.findById(usCarInfo.getCarRules().getCarRulesId()).get();
             usCarRules.setFoodAllowed(carInfo.getCarRules().isFoodAllowed());
             usCarRules.setSmokingAllowed(carInfo.getCarRules().isSmokingAllowed());
             usCarRules.setComments(carInfo.getCarRules().getComments());
             carRulesRepository.save(usCarRules);
             carInfoRepository.save(usCarInfo);
         }
     }
     
     /**
      * Add car information
      * FOR DRIVER
      * @param userId
      * @param carInfo
      */
     @RequestMapping(value = "/delete/carinfo", 
             method = RequestMethod.GET)
     public String deleteCarInfo(@RequestParam("carid") final Long carid) {
         Authentication authentication = authenticationFacade.getAuthentication();
         LuckyDriveUserPrincipal userPrinc = (LuckyDriveUserPrincipal) authentication.getPrincipal();
         User userParsed = userPrinc.getAppUser();
         Optional<User> userOpt = userRepository.findById(userParsed.getUserId());
         User user = userOpt.get();
         if (user.getUserMode().equals(UserMode.DRIVER)) {
             CarInfo usCarInfo = carInfoRepository.findDriversCarInfo(user.getUserId());
             if (usCarInfo != null && usCarInfo.getCarInfoId().equals(carid)) {
                 carRulesRepository.delete(usCarInfo.getCarRules());
                 carInfoRepository.delete(usCarInfo);
                 return "OK";
             }
         }
         return "ERROR";
     }
	

	/**
	 * Return all user's active trips
	 * FOR DRIVER/PASSENGER
	 * - for driver these trips have status ACTIVE or NOT_ACTIVE
	 * - for passenger these trips are only ACTIVE
	 * @return
	 */
	@RequestMapping(value = "/get/trips", 
	        method = RequestMethod.GET, 
	        produces = "application/json")
	public @ResponseBody String getTrips() {
        Authentication authentication = authenticationFacade.getAuthentication();
        LuckyDriveUserPrincipal userPrinc = (LuckyDriveUserPrincipal) authentication.getPrincipal();
        User userParsed = userPrinc.getAppUser();
        Optional<User> userOpt = userRepository.findById(userParsed.getUserId());
        User user = userOpt.get();
	    String response = new String("{\"trips\":[]}");
	    Iterable<Trip> trips;
	    if (user.getUserMode().equals(UserMode.DRIVER)) {
	        trips = tripRepository.getDriversTrips(user.getUserId());
	        if (trips == null)
	            response = createJsonStringFromTripsForDriver(trips);
	    }
	    else if (user.getUserMode().equals(UserMode.PASSENGER)) {
	        trips = tripRepository.getPassengersTrips(user.getUserId());
	        trips = tripRepository.getDriversTrips(user.getUserId());
	        if (trips == null)
	            response = createJsonStringFromTripsForPassenger(trips);
	    }
		return response;
	}
	
	/**
     * Return all user's completed trips
     * FOR DRIVER/PASSENGER
     *  all trips in which user participated with any mode
     * @return
     */
    @RequestMapping(value = "/get/completed/trips", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody String getCompletedTrips() {
        Authentication authentication = authenticationFacade.getAuthentication();
        LuckyDriveUserPrincipal userPrinc = (LuckyDriveUserPrincipal) authentication.getPrincipal();
        User userParsed = userPrinc.getAppUser();
        Optional<User> userOpt = userRepository.findById(userParsed.getUserId());
        User user = userOpt.get();
        String response = new String("{\"trips\":[]}");
        Iterable<Trip> trips;
        if (user.getUserMode().equals(UserMode.DRIVER)) {
            trips = tripRepository.getCompletedDriversTrips(user.getUserId());
            if (trips == null)
                response = createJsonStringFromTripsForDriver(trips);
        }
        else if (user.getUserMode().equals(UserMode.PASSENGER)) {
            trips = tripRepository.getCompletedPassengersTrips(user.getUserId());
            if (trips == null)
                response = createJsonStringFromTripsForPassenger(trips);
        }
        return response;
    }

	/**
	 * Find trips from work location point to home 
	 * FOR PASSENGER
	 * 
	 * @param address
	 * @param lat
	 * @param lng
	 * @param radius
	 * @return
	 */
	@RequestMapping(value = "/find/trips/home",
	        method = RequestMethod.GET, 
	        produces = "application/json")
	public @ResponseBody String findTripsToHomeByPoint(@RequestBody Point workPoint) {
		Iterable<Trip> trips = tripRepository
		        .findTripByClosestEndPoints(workPoint.getLat(), workPoint.getLng(), workPoint.getRadius());
		if (trips == null)
			return "{\"trips\":[]}";
		else {
			return createJsonStringFromTripsForPassenger(trips).toString();
		}
	}

	/**
	 * Find trips from home to work location point 
	 * FOR PASSENGER
	 * 
	 * @param address
	 * @param lat
	 * @param lng
	 * @param radius
	 * @return
	 */
	@RequestMapping(value = "/find/trips/work",
	        method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String findTripsToWorkByPoint(@RequestBody Point homePoint) {
		Iterable<Trip> trips = tripRepository
		        .findTripByClosestStartPoints(homePoint.getLat(), homePoint.getLng(), homePoint.getRadius());
		if (trips == null)
			return "{\"trips\":[]}";
		else {
			return createJsonStringFromTripsForPassenger(trips).toString();
		}
	}

	/**
	 * Find trips by tags
	 * FOR PASSENGER
	 * 
	 * @param tags
	 * @return
	 */
	@RequestMapping(value = "/find/trips/tags", 
	        method = RequestMethod.GET, 
	        produces = "application/json", 
	        consumes = "application/json")
	public @ResponseBody String findTripsByTags(@RequestBody Set<String> tags) {
		Iterable<Trip> trips = tripRepository.findTripByTags(tags);
		if (trips == null)
			return "{\"trips\":[]}";
		else {
			return createJsonStringFromTripsForPassenger(trips).toString();
		}
	}

	/**
	 * Add passenger to trip 
	 * FOR PASSENGER
	 * 
	 * @param userId
	 * @param tripId
	 */
	@RequestMapping(value = "/add/passenger", 
	        method = RequestMethod.POST)
	public void addPassengerToTrip(@RequestBody AdditionPassengerToTrip passengerTrip) {
		Optional<User> user = userRepository.findById(passengerTrip.getUserId());
		if (user.isPresent()) {
			Optional<Trip> trip = tripRepository.findById(passengerTrip.getTripId());
			// if user who wants to join the trip is not a driver of
			// this trip
			if (trip.isPresent() && !trip.get().getDriver().getUserId().equals(passengerTrip.getUserId())) {
				Trip updatedTrip = trip.get();
				updatedTrip.addPassenger(user.get());
				updatedTrip.incrementPasseger();
				tripRepository.save(updatedTrip);
				User updatedUser = user.get();
				updatedUser.addTrip(updatedTrip);
				userRepository.save(updatedUser);
	            Notification notification = new Notification();
	            notification.setMessage("You are passenger of trip: \n" +
	            "from " + updatedTrip.getStartPoint().getAddress() + " \n" +
	            "to " + updatedTrip.getEndPoint() +
	            "Trip starts in 15 minutes ");
	            notification.setMessageDate(updatedTrip.getDateOfDeparture()); //TODO set date as date of trip -15 minutes
	            notification.setUser(user.get());
	            notification.setRead(false);
	            notificationRepository.save(notification);
			}
		}
	}

	/**
	 * Delete passenger from trip FOR DRIVER AND PASSENGER
	 * 
	 * @param userId
	 * @param tripId
	 */
	@RequestMapping(value = "/delete/passenger",
	        method = RequestMethod.POST)
	public void deletePassengerFromTrip(@RequestBody RemovingPassengerFromTrip delPassengerTrip) {
		Optional<User> driver = userRepository.findById(delPassengerTrip.getDriverId());
		Optional<User> user = userRepository.findById(delPassengerTrip.getUserId());
		if (driver.isPresent() && user.isPresent()) {
			Optional<Trip> trip = tripRepository.findById(delPassengerTrip.getTripId());
			if (!trip.get().getDriver().getUserId().equals(delPassengerTrip.getUserId())) {
				Trip updatedTrip = trip.get();
				updatedTrip.removePassenger(user.get());
				updatedTrip.decrementPasseger();
				tripRepository.save(updatedTrip);
			}
		}
	}

	/**
	 * Save favourite route (only if user as a passenger) if it is needed to save
	 * two points route is added also in editing user info
	 * 
	 * @param userId
	 * @param route
	 */
	@RequestMapping(value = "/save/favroute", params = {
			"user" }, method = RequestMethod.POST, consumes = "application/json")
	public void saveFavouriteRoute(@RequestBody FavouriteRoute route) {
        Authentication authentication = authenticationFacade.getAuthentication();
        LuckyDriveUserPrincipal userPrinc = (LuckyDriveUserPrincipal) authentication.getPrincipal();
        User userParsed = userPrinc.getAppUser();
        Optional<User> userOpt = userRepository.findById(userParsed.getUserId());
        User user = userOpt.get();
		pointRepository.save(route.getDepartmentPoint());
		pointRepository.save(route.getDestinationPoint());
		favouriteRouteRepository.save(route);
		user.addFavouriteRoutes(route);
		userRepository.save(user);
	}

	/**
	 * Save favourite route (only if user as a passenger) if it is needed to only
	 * one destination point route is added also in editing user info
	 * 
	 * @param userId
	 * @param destinationPoint
	 */
	@RequestMapping(value = "/save/point/route", params = {
			"user" }, method = RequestMethod.POST, consumes = "application/json")
	public void addPointAsFavouriteRoute(@RequestParam("user") Long userId, @RequestBody Point destinationPoint) {
        Authentication authentication = authenticationFacade.getAuthentication();
        LuckyDriveUserPrincipal userPrinc = (LuckyDriveUserPrincipal) authentication.getPrincipal();
        User userParsed = userPrinc.getAppUser();
        Optional<User> userOpt = userRepository.findById(userParsed.getUserId());
        User user = userOpt.get();
		Point departmentPoint = user.getOrganization().getLocationPoints().stream().findFirst().get();
		pointRepository.save(destinationPoint);
		FavouriteRoute route = new FavouriteRoute(user, departmentPoint, destinationPoint);
		favouriteRouteRepository.save(route);
	    user.addFavouriteRoutes(route);
	    userRepository.save(user);
	}

	public String createJsonStringFromTripsForDriver(Iterable<Trip> trips) {
		// create json object manually
		JSONObject tripsJson = new JSONObject();
		JSONArray tripsJsonArray = new JSONArray();
		JSONObject tripJson = null;
		for (Trip trip : trips) {
			tripJson = new JSONObject();
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
			for (Point intermPoint : trip.getIntermediatePoints()) {
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
			for (Tag tag : trip.getTags()) {
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
			JSONArray passengerJsonArray = new JSONArray();
			JSONObject passengerJson = null;
	        for (User user : trip.getPassengers()) {
	            passengerJson = new JSONObject();
	            passengerJson.put("name", user.getName());
	            passengerJson.put("surname", user.getSurname());
	            if (user.getPhone() != null && !user.getPhone().isEmpty())
	                passengerJson.put("phone", user.getPhone());
	            passengerJsonArray.put(passengerJson);
	        }
	        tripJson.put("passengers", passengerJsonArray);
		}
		tripsJson.put("trips", tripsJsonArray);
		return tripsJson.toString();
	}

	   public String createJsonStringFromTripsForPassenger(Iterable<Trip> trips) {
	        // create json object manually
	        JSONObject tripsJson = new JSONObject();
	        JSONArray tripsJsonArray = new JSONArray();
	        JSONObject tripJson = null;
	        for (Trip trip : trips) {
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
	            for (Point intermPoint : trip.getIntermediatePoints()) {
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
	            for (Tag tag : trip.getTags()) {
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
	
	/*
	 * @RequestMapping("/") public String index() { return "index.html"; }
	 */

	/*
	 * @RequestMapping("/notifications") public String notifications() { return
	 * "notifications.html"; }
	 */

	/*
	 * @RequestMapping(value = "/info", method = RequestMethod.GET) public String
	 * isItWorking(@RequestParam("name") final String name) { return
	 * "Run away from IBA, " + name; }
	 */

	@MessageMapping("/")
	public void greeting(Principal principal, String message) throws Exception {
		// Greeting greeting = new Greeting();
		// greeting.setContent("Hello !");
		String aaaa = message + " is sent through WebSocket";

		messagingTemplate.convertAndSend(aaaa);
	}

	/*
	 * @MessageMapping("/chat.sendMessage")
	 * 
	 * @SendTo("/topic/public") public ChatMessage sendMessage(@Payload ChatMessage
	 * chatMessage) { return chatMessage; }
	 * 
	 * @MessageMapping("/chat.addUser")
	 * 
	 * @SendTo("/topic/public") public ChatMessage addUser(@Payload ChatMessage
	 * chatMessage, SimpMessageHeaderAccessor headerAccessor) { // Add username in
	 * web socket session headerAccessor.getSessionAttributes().put("username",
	 * chatMessage.getSender()); return chatMessage; }
	 */

	/*
	 * @PostMapping("/")
	 * 
	 * @ResponseBody public ResponseEntity<?> getNotification(Principal principal) {
	 * String username = "Shrek".equals(principal.getName()) ? "GrustnyiSergei" :
	 * "Shrek";
	 * 
	 * messagingTemplate.convertAndSendToUser(username, "space/notification", new
	 * Notification(username + ", go with me"));
	 * 
	 * return ResponseEntity.ok(null); }
	 */

	/*
	 * public void addToTrip(Principal principal) { // TODO
	 * messagingTemplate.convertAndSendToUser(driver,
	 * "private-channel/notification", new Passenger((User) principal)); }
	 */

}
