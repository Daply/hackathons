package com.luckydrive.model;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity(name="trip")
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trip_id")
    private Long tripId;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "driver_id")
    private User driver;
    
    @Enumerated(EnumType.STRING)
    private TripStatus status;
    
    @OneToOne
    @JoinColumn(name="start_point_id", nullable=false)
    private Point startPoint;
    
    @OneToOne
    @JoinColumn(name="end_point_id", nullable=false)
    private Point endPoint;
    
    @Column(name = "departure_time_lower_bound", columnDefinition = "time")
    private Time departureTimeLowerBound;
    
    @Column(name = "departure_time_upper_bound", columnDefinition = "time")
    private Time departureTimeUpperBound;
    
    @Column(name = "date_of_departure", columnDefinition = "date")
    private Date dateOfDeparture;

    @Column(name = "limit_number_of_passengers", columnDefinition = "int default 0")
    private int limitNumberOfPassengers = 0;
    
    @Column(name = "current_number_of_passengers", columnDefinition = "int default 0")
    private int currentNumberOfPassengers = 0;
    
    private String comments;
    
    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
        name = "trip_intermed_points_connect", 
        joinColumns = { @JoinColumn(name = "trip_id") }, 
        inverseJoinColumns = { @JoinColumn(name = "point_id") }
    )
    private Set<Point> intermediatePoints = new HashSet<>();
    
    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
        name = "trip_tag_connect", 
        joinColumns = { @JoinColumn(name = "trip_id") }, 
        inverseJoinColumns = { @JoinColumn(name = "tag_id") }
    )
    private Set<Tag> tags = new HashSet<>();
    
    @ManyToMany(mappedBy = "trips")
    private Set<User> passengers = new HashSet<>();

    public Trip() {
        
    }

    public Trip(User driver, Point startPoint, Point endPoint, Time departureTimeLowerBound,
            Time departureTimeUpperBound, Date dateOfDeparture, int limitNumberOfPassengers,
            String comments, Set<Point> intermediatePoints, Set<Tag> tags) {
        super();
        this.driver = driver;
        this.status = TripStatus.ACTIVE;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.departureTimeLowerBound = departureTimeLowerBound;
        this.departureTimeUpperBound = departureTimeUpperBound;
        this.dateOfDeparture = dateOfDeparture;
        this.limitNumberOfPassengers = limitNumberOfPassengers;
        this.comments = comments;
        this.intermediatePoints = intermediatePoints;
        this.tags = tags;
    }

    public TripStatus getStatus() {
        return status;
    }

    public void setStatus(TripStatus status) {
        this.status = status;
    }

    public Date getDateOfDeparture() {
        return dateOfDeparture;
    }

    public void setDateOfDeparture(Date dateOfDeparture) {
        this.dateOfDeparture = dateOfDeparture;
    }

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public User getDriver() {
        return driver;
    }

    public void setDriver(User driver) {
        this.driver = driver;
    }

    public Point getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Point startPoint) {
        this.startPoint = startPoint;
    }

    public Point getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(Point endPoint) {
        this.endPoint = endPoint;
    }

    public Time  getDepartureTimeLowerBound() {
        return departureTimeLowerBound;
    }

    public void setDepartureTimeLowerBound(Time departureTimeLowerBound) {
        this.departureTimeLowerBound = departureTimeLowerBound;
    }

    public Time getDepartureTimeUpperBound() {
        return departureTimeUpperBound;
    }

    public void setDepartureTimeUpperBound(Time departureTimeUpperBound) {
        this.departureTimeUpperBound = departureTimeUpperBound;
    }

    public int getLimitNumberOfPassengers() {
        return limitNumberOfPassengers;
    }

    public void setLimitNumberOfPassengers(int limitNumberOfPassengers) {
        this.limitNumberOfPassengers = limitNumberOfPassengers;
    }

    public int getCurrentNumberOfPassengers() {
        return currentNumberOfPassengers;
    }

    public void setCurrentNumberOfPassengers(int currentNumberOfPassengers) {
        this.currentNumberOfPassengers = currentNumberOfPassengers;
    }
    
    public void incrementPasseger() {
        this.currentNumberOfPassengers++;
    }
    
    public void decrementPasseger() {
        this.currentNumberOfPassengers--;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Set<Point> getIntermediatePoints() {
        return intermediatePoints;
    }

    public void setIntermediatePoints(Set<Point> intermediatePoints) {
        this.intermediatePoints = intermediatePoints;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }
    
    public Set<User> getPassengers() {
        return passengers;
    }

    public void setPassengers(Set<User> passengers) {
        this.passengers = passengers;
    }

    public void addPassenger(User passenger) {
        this.passengers.add(passenger);
    }
    
    public void removePassenger(User passenger) {
        this.passengers.remove(passenger);
    }
}
