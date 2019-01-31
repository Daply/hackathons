package com.luckydrive.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.JoinColumn;

@Entity(name="point")
public class Point {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long point_id;
    
    @Column(nullable=false)
	private double lat;
    
    @Column(nullable=false)
	private double lng;
	
    @Column(nullable=true)
	private double radius;
	
    @Column(nullable=false)
	private String address;

    @ManyToMany(mappedBy = "intermediatePoints")
    private Set<Trip> trips = new HashSet<>();
    
    @JsonIgnore
    @ManyToMany(mappedBy = "locationPoints")
    private Set<Organization> organizations = new HashSet<>();

    public Point() {
    	
    }
    
	public Point(final double lng, final double lat, String address, double radius) {
		this.lng = lng;
		this.lat = lat;
		this.address = address;
	}

	public Long getPoint_id() {
		return point_id;
	}

	public void setPoint_id(Long point_id) {
		this.point_id = point_id;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Set<Trip> getTrips() {
		return trips;
	}

	public void setTrips(Set<Trip> trips) {
		this.trips = trips;
	}
	
	
}
