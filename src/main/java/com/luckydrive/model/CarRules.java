package com.luckydrive.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="car_rules")
public class CarRules {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "car_rules_id")
    private Long carRulesId;
    
    @Column(name = "smoking_allowed")
    private boolean smokingAllowed;
    
    @Column(name = "food_allowed")
    private boolean foodAllowed;
    
    @Column(name = "talking_allowed")
    private boolean talkingAllowed;
    
    @Column(name = "phone_allowed")
    private boolean phoneAllowed;
    
    private String comments;

    public CarRules() {
        
    }
    
    public CarRules(boolean smokingAllowed, boolean foodAllowed, String comments) {
        super();
        this.smokingAllowed = smokingAllowed;
        this.foodAllowed = foodAllowed;
        this.comments = comments;
    }

    public Long getCarRulesId() {
        return carRulesId;
    }

    public void setCarRulesId(Long carRulesId) {
        this.carRulesId = carRulesId;
    }

    public boolean isSmokingAllowed() {
        return smokingAllowed;
    }

    public void setSmokingAllowed(boolean smokingAllowed) {
        this.smokingAllowed = smokingAllowed;
    }

    public boolean isFoodAllowed() {
        return foodAllowed;
    }

    public void setFoodAllowed(boolean foodAllowed) {
        this.foodAllowed = foodAllowed;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
    
    
}
