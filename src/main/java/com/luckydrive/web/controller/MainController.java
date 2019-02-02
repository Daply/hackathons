package com.luckydrive.web.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.luckydrive.config.security.IAuthenticationFacade;
import com.luckydrive.config.security.LuckyDriveUserPrincipal;
import com.luckydrive.model.AdditionPassengerToTrip;
import com.luckydrive.model.CarInfo;
import com.luckydrive.model.CarRules;
import com.luckydrive.dto.JoinTripResponse;
import com.luckydrive.dto.NewPassengerNotification;
import com.luckydrive.dto.PrivateChatMessageDTO;
import com.luckydrive.dto.UserShortInfo;
import com.luckydrive.model.ChatMessage;
import com.luckydrive.model.FavouriteRoute;
import com.luckydrive.model.HomeAddress;
import com.luckydrive.model.Notification;
import com.luckydrive.model.Organization;
import com.luckydrive.model.Point;
import com.luckydrive.model.RemovingPassengerFromTrip;
import com.luckydrive.model.PrivateChatMessage;
import com.luckydrive.model.Tag;
import com.luckydrive.model.Trip;
import com.luckydrive.model.TripStatus;
import com.luckydrive.model.User;
import com.luckydrive.model.UserMode;
import com.luckydrive.model.UserSchedule;
import com.luckydrive.repository.CarInfoRepository;
import com.luckydrive.repository.CarRulesRepository;
import com.luckydrive.repository.ChatMessageRepository;
import com.luckydrive.repository.FavouriteRouteRepository;
import com.luckydrive.repository.HomeAddressRepository;
import com.luckydrive.repository.NotificationRepository;
import com.luckydrive.repository.PointRepository;
import com.luckydrive.repository.TagRepository;
import com.luckydrive.repository.TripRepository;
import com.luckydrive.repository.UserRepository;

import net.bytebuddy.utility.RandomString;

@Controller
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

	@Autowired
	ChatMessageRepository chatMessageRepository;

	private RandomString randomStringGenerator = new RandomString(8);
	
	private JsonConverter jsonConv = new JsonConverter();

	   @RequestMapping("/")
	    public String index() {
	        return "index.html";
	    }
	
	@RequestMapping("/login")
	public String login() {
		return "login.html";
	}

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
	
	@RequestMapping(value = "/home", 
	        method = RequestMethod.GET,
	        produces = "application/json")
	public @ResponseBody String getAuthenticatedUser() {
	    // This returns a JSON or XML with the users
	    // just for test
	    Authentication authentication = authenticationFacade.getAuthentication();
	    LuckyDriveUserPrincipal userPrinc = (LuckyDriveUserPrincipal) authentication.getPrincipal();
	    User userParsed = userPrinc.getAppUser();
	    Optional<User> userOpt = userRepository.findById(userParsed.getUserId());
	    
	    User user = userOpt.get();
	    if (user.getUserMode().equals(UserMode.DRIVER))
	        user.setTrips(tripRepository.getDriversTrips(user.getUserId()));
	    
	    JSONObject json = jsonConv.createJsonUser(user);
	    
	    
	    return json.toString();
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
            trip.setStatus(TripStatus.NOT_ACTIVE);
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
	            response = jsonConv.createJsonFromTripsForDriver(trips).toString();
	    }
	    else if (user.getUserMode().equals(UserMode.PASSENGER)) {
	        trips = tripRepository.getPassengersTrips(user.getUserId());
	        trips = tripRepository.getDriversTrips(user.getUserId());
	        if (trips == null)
	            response = jsonConv.createJsonFromTripsForPassenger(trips).toString();
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
                response = jsonConv.createJsonFromTripsForDriver(trips).toString();
        }
        else if (user.getUserMode().equals(UserMode.PASSENGER)) {
            trips = tripRepository.getCompletedPassengersTrips(user.getUserId());
            if (trips == null)
                response = jsonConv.createJsonFromTripsForPassenger(trips).toString();
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
			return jsonConv.createJsonFromTripsForPassenger(trips).toString();
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
			return jsonConv.createJsonFromTripsForPassenger(trips).toString();
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
			return jsonConv.createJsonFromTripsForPassenger(trips).toString();
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
	        method = RequestMethod.POST, consumes = "application/json")
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

				NewPassengerNotification newPassengerNotification = new NewPassengerNotification(
						getUserShortInfo(updatedUser));

				String chatChannelUuid = updatedTrip.getChatChannelUuid();
				if (chatChannelUuid != null) {
					chatChannelUuid = randomStringGenerator.nextString();
					newPassengerNotification.setChatChannelUuid(chatChannelUuid);
					updatedTrip.setChatChannelUuid(chatChannelUuid);
					tripRepository.save(updatedTrip);
					messagingTemplate.convertAndSendToUser(updatedTrip.getDriver().getEmail(), "/private",
							notification);
				}
				messagingTemplate.convertAndSendToUser(updatedUser.getEmail(), "/private",
						new JoinTripResponse(chatChannelUuid, notification));
			}

		}
	}

	private UserShortInfo getUserShortInfo(User user) {
		return new UserShortInfo(user.getEmail(), user.getName(), user.getSurname());
	}

	@MessageMapping("/{tripId}/tripChat/{channelUuid}")
	@SendTo("/trip/chat/reply/{channelUuid}")
	public ChatMessage broadcastToAllChatParticipants(@DestinationVariable("channelUuid") String channelUuid,
			@DestinationVariable("tripId") Long tripId, @Payload ChatMessage message, Principal sender) {
		submitTripChatMessage(message, sender.getName(), tripId);
		return message;
	}

	private void submitTripChatMessage(ChatMessage message, String senderUsername, Long tripId) {
		Optional<Trip> trip = tripRepository.findById(tripId);
		if (trip.isPresent()) {
			Trip updatedTrip = trip.get();
			updatedTrip.addMessage(message);
			User sender = userRepository.findByEmail(senderUsername);
			if (sender != null) {
				message.setTimeSent(LocalTime.now());
				message.setSender(sender);
				tripRepository.save(updatedTrip);
				chatMessageRepository.save(message);
			}
		}
	}

	@MessageMapping("/chatOf2")
	public void sendPrivateChatMessage(@Payload PrivateChatMessageDTO message, Principal sender) {
		Optional<User> recipient = userRepository.findById(message.getRecipientId());
		if (recipient.isPresent()) {
			submitPrivateMessage(message, sender.getName(), recipient.get());
			messagingTemplate.convertAndSendToUser(sender.getName(), "/chat/reply", message);
		}
	}

	private void submitPrivateMessage(PrivateChatMessageDTO messageDTO, String senderUsername, User recipient) {
		User sender = userRepository.findByEmail(senderUsername);
		if (sender != null) {
			PrivateChatMessage message = new PrivateChatMessage(sender, messageDTO.getContent(), recipient,
					LocalTime.now());
			chatMessageRepository.save(message);
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


	@MessageMapping("/chat.addUser")
	@SendTo("/topic/public")
	public String addUser(String chatMessage, SimpMessageHeaderAccessor headerAccessor) {
		// Add username in web socket session
		// headerAccessor.getSessionAttributes().put("username",
		// chatMessage.getSender());
		System.out.println(chatMessage);
		String message = "text: " + chatMessage;
		return message;
	}


}
