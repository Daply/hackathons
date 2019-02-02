package com.luckydrive.web.controller;

import org.json.JSONArray;
import org.json.JSONObject;

import com.luckydrive.model.CarInfo;
import com.luckydrive.model.CarRules;
import com.luckydrive.model.FavouriteRoute;
import com.luckydrive.model.HomeAddress;
import com.luckydrive.model.Organization;
import com.luckydrive.model.Point;
import com.luckydrive.model.Tag;
import com.luckydrive.model.Trip;
import com.luckydrive.model.User;
import com.luckydrive.model.UserMode;
import com.luckydrive.model.UserSchedule;

public class JsonConverter {
    
    public JSONObject createJsonFromTripsForDriver(Iterable<Trip> trips) {
        // create json object manually
        JSONObject tripsJson = new JSONObject();
        JSONArray tripsJsonArray = new JSONArray();
        JSONObject tripJson = new JSONObject();
        for (Trip trip : trips) {
            tripJson = new JSONObject();
            tripJson.put("startPoint", createJsonPoint(trip.getStartPoint()));
            tripJson.put("endPoint", createJsonPoint(trip.getEndPoint()));
            tripJson.put("departureTimeLowerBound", trip.getDepartureTimeLowerBound());
            tripJson.put("departureTimeUpperBound", trip.getDepartureTimeUpperBound());
            tripJson.put("dateOfDeparture", trip.getDateOfDeparture());
            tripJson.put("currentNumberOfPassengers", trip.getCurrentNumberOfPassengers());
            tripJson.put("limitNumberOfPassengers", trip.getLimitNumberOfPassengers());
            JSONArray intermdPointsJsonArray = new JSONArray();
            for (Point intermPoint : trip.getIntermediatePoints()) {
                intermdPointsJsonArray.put(createJsonPoint(intermPoint));
            }
            tripJson.put("intermediatePoints", intermdPointsJsonArray);
            JSONArray tagJsonArray = new JSONArray();
            for (Tag tag : trip.getTags()) {
                tagJsonArray.put(createJsonTag(tag));
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
        return tripsJson;
    }

       public JSONObject createJsonFromTripsForPassenger(Iterable<Trip> trips) {
            // create json object manually
            JSONObject tripsJson = new JSONObject();
            JSONArray tripsJsonArray = new JSONArray();
            JSONObject tripJson = null;
            for (Trip trip : trips) {
                tripJson = new JSONObject();
                tripJson.put("driverName", trip.getDriver().getName());
                tripJson.put("driverSurname", trip.getDriver().getSurname());
                tripJson.put("startPoint", createJsonPoint(trip.getStartPoint()));
                tripJson.put("endPoint", createJsonPoint(trip.getEndPoint()));
                tripJson.put("departureTimeLowerBound", trip.getDepartureTimeLowerBound());
                tripJson.put("departureTimeUpperBound", trip.getDepartureTimeUpperBound());
                tripJson.put("dateOfDeparture", trip.getDateOfDeparture());
                tripJson.put("currentNumberOfPassengers", trip.getCurrentNumberOfPassengers());
                tripJson.put("limitNumberOfPassengers", trip.getLimitNumberOfPassengers());
                JSONArray intermdPointsJsonArray = new JSONArray();
                for (Point intermPoint : trip.getIntermediatePoints()) {
                    intermdPointsJsonArray.put(createJsonPoint(intermPoint));
                }
                tripJson.put("intermediatePoints", intermdPointsJsonArray);
                JSONArray tagJsonArray = new JSONArray();
                for (Tag tag : trip.getTags()) {
                    tagJsonArray.put(createJsonTag(tag));
                }
                tripJson.put("tags", tagJsonArray);
                tripJson.put("comments", trip.getComments());
                tripsJsonArray.put(tripJson);
            }
            tripsJson.put("trips", tripsJsonArray);
            return tripsJson;
        }
       
        public JSONObject createJsonUser(User user) {
            // create json object manually
            JSONObject userJson = new JSONObject();
            
            userJson.put("name", user.getName());
            userJson.put("surname", user.getSurname());
            userJson.put("email", user.getEmail());
            userJson.put("phone", user.getPhone());
            
            if (user.getCarInfo() != null)
                userJson.put("carInfo", createJsonCarInfo(user.getCarInfo()));
            
            if (user.getSchedule() != null)
                userJson.put("schedule", createJsonSchedule(user.getSchedule()));

            userJson.put("userMode", user.getUserMode());  
            
            if (user.getOrganization() != null)
                userJson.put("organization", createJsonOrganization(user.getOrganization()));   
            
            JSONArray userFavRoutesJsonArray = new JSONArray();
            for (FavouriteRoute route: user.getFavouriteRoutes()) {
                userFavRoutesJsonArray.put(createJsonFavouriteRoute(route));
            }
            userJson.put("favouriteRoutes", userFavRoutesJsonArray);            
            JSONArray userHomeAddressesJsonArray = new JSONArray();
            for (HomeAddress homeAddress: user.getHomeAddresses()) {
                userHomeAddressesJsonArray.put(createHomeAddress(homeAddress));
            }
            userJson.put("homeAddresses", userHomeAddressesJsonArray);
            
            if (user.getUserMode().equals(UserMode.DRIVER))
                userJson.put("trips", createJsonFromTripsForDriver(user.getTrips()));
            if (user.getUserMode().equals(UserMode.PASSENGER))
                userJson.put("trips", createJsonFromTripsForPassenger(user.getTrips()));
            
            return userJson;
        }
        
    public JSONObject createJsonPoint(Point point) {
        JSONObject pointJson = new JSONObject();
        pointJson.put("lat", point.getLat());
        pointJson.put("lng", point.getLng());
        pointJson.put("address", point.getAddress());
        pointJson.put("radius", point.getRadius());
        return pointJson;
    }
    
    public JSONObject createJsonSchedule(UserSchedule schedule) {
        JSONObject scheduleJson = new JSONObject();
        scheduleJson.put("userDepTimeLowerBound", schedule.getUserDepTimeLowerBound());
        scheduleJson.put("userDepTimeUpperBound", schedule.getUserDepTimeUpperBound());
        return scheduleJson;
    }   
    
    public JSONObject createHomeAddress(HomeAddress address) {
        JSONObject homeJson = new JSONObject();
        homeJson.put("point", createJsonPoint(address.getPoint()));
        return homeJson;
    }   

    public JSONObject createJsonOrganization(Organization organization) {
        JSONObject organizationJson = new JSONObject();
        organizationJson.put("name", organization.getName());
        JSONArray organizationLocationPointsJsonArray = new JSONArray();
        for (Point point: organization.getLocationPoints()) {
            organizationLocationPointsJsonArray.put(createJsonPoint(point));
        }
        organizationJson.put("locationPoints", organizationLocationPointsJsonArray);
        return organizationJson;
    }   
    
    public JSONObject createJsonFavouriteRoute(FavouriteRoute route) {
        JSONObject routeJson = new JSONObject();
        routeJson.put("departmentPoint", createJsonPoint(route.getDepartmentPoint()));
        routeJson.put("destinationPoint", createJsonPoint(route.getDestinationPoint()));
        return routeJson;
    }   
    
    public JSONObject createJsonCarInfo(CarInfo carInfo) {
        JSONObject carInfoJson = new JSONObject();
        carInfoJson.put("carMark", carInfo.getCarMark());
        carInfoJson.put("carNumber", carInfo.getCarNumber());
        carInfoJson.put("carColor", carInfo.getCarColor());
        carInfoJson.put("carRules", createJsonCarRules(carInfo.getCarRules()));
        return carInfoJson;
    }       
    
    public JSONObject createJsonCarRules(CarRules carRules) {
        JSONObject carInfoRulesJson = new JSONObject();
        carInfoRulesJson.put("smokingAllowed", carRules.isSmokingAllowed());
        carInfoRulesJson.put("foodAllowed", carRules.isFoodAllowed());
        carInfoRulesJson.put("talkingAllowed", carRules.isTalkingAllowed());
        carInfoRulesJson.put("phoneAllowed", carRules.isPhoneAllowed());
        carInfoRulesJson.put("comments", carRules.getComments());
        return carInfoRulesJson;
    }
    
    public JSONObject createJsonTag(Tag tag) {
        JSONObject tagJson = new JSONObject();
        tagJson.put("tagName", tag.getTagName());
        tagJson.put("tagPoint", createJsonPoint(tag.getTagPoint()));
        return tagJson;
    }     
}
