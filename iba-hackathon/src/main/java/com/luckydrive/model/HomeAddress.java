package com.luckydrive.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity(name="home_address")
public class HomeAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "home_address_id")
    private Long homeAddressId;
    
    @OneToOne
    @JoinColumn(name="point_id", nullable=false)
    private Point point;
    
    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    public HomeAddress() {
        
    }

    public HomeAddress(Point point, User user) {
        super();
        this.point = point;
        this.user = user;
    }

    public Long getHomeAddressId() {
        return homeAddressId;
    }

    public void setHomeAddressId(Long homeAddressId) {
        this.homeAddressId = homeAddressId;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    
    
    
}
