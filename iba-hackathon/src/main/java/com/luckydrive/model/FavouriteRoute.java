package com.luckydrive.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity(name="favourite_route")
public class FavouriteRoute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favourite_route_id")
    private Long favouriteRouteId;
    
    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;
    
    @OneToOne
    @JoinColumn(name="dep_point_id", nullable=false)
    private Point departmentPoint;
    
    @OneToOne
    @JoinColumn(name="des_point_id", nullable=false)
    private Point destinationPoint;

    public FavouriteRoute() {
        
    }
    
    public FavouriteRoute(User user, Point departmentPoint, Point destinationPoint) {
        super();
        this.user = user;
        this.departmentPoint = departmentPoint;
        this.destinationPoint = destinationPoint;
    }

    public Long getFavouriteRouteId() {
        return favouriteRouteId;
    }

    public void setFavouriteRouteId(Long favouriteRouteId) {
        this.favouriteRouteId = favouriteRouteId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Point getDepartmentPoint() {
        return departmentPoint;
    }

    public void setDepartmentPoint(Point departmentPoint) {
        this.departmentPoint = departmentPoint;
    }

    public Point getDestinationPoint() {
        return destinationPoint;
    }

    public void setDestinationPoint(Point destinationPoint) {
        this.destinationPoint = destinationPoint;
    } 
    
}
