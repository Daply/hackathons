package com.luckydrive.model;

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

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;
    
    private String name;
    
    private String surname;
    
    private String email;
    
    @JsonIgnore
    private String password;
    
    private String phone;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "user_mode")
    private UserMode userMode;
    
    @OneToOne
    @JoinColumn(name="organization_id", nullable=false)
    private Organization organization;
    
    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
        name = "user_trip_connect", 
        joinColumns = { @JoinColumn(name = "user_id") }, 
        inverseJoinColumns = { @JoinColumn(name = "trip_id") }
    )
    private Set<Trip> trips = new HashSet<>();

    @OneToMany(mappedBy="user")
    private Set<FavouriteRoute> favouriteRoutes;
    
    @OneToMany(mappedBy="user")
    private Set<HomeAddress> homeAddresses;
    
    public User() {
    	
    }

	public User(String name, String surname, String email, String password, Organization organization) {
		super();
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.password = password;
		this.organization = organization;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

    public void addTrip(Trip trip) {
        this.trips.add(trip);
    }

	public Set<Trip> getTrips() {
        return trips;
    }

    public void setTrips(Set<Trip> trips) {
        this.trips = trips;
    }

    public Set<FavouriteRoute> getFavouriteRoutes() {
		return favouriteRoutes;
	}

	public void setFavouriteRoutes(Set<FavouriteRoute> favouriteRoutes) {
		this.favouriteRoutes = favouriteRoutes;
	}

	public void addFavouriteRoutes(FavouriteRoute favouriteRoute) {
	    this.favouriteRoutes.add(favouriteRoute);
	}
	   
	public void removeFavouriteRoutes(FavouriteRoute favouriteRoute) {
	    this.favouriteRoutes.remove(favouriteRoute);
	}
	
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public UserMode getUserMode() {
        return userMode;
    }

    public void setUserMode(UserMode userMode) {
        this.userMode = userMode;
    }

    public Set<HomeAddress> getHomeAddresses() {
        return homeAddresses;
    }

    public void setHomeAddresses(Set<HomeAddress> homeAddresses) {
        this.homeAddresses = homeAddresses;
    }
}