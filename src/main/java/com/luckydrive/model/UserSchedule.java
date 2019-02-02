package com.luckydrive.model;

import java.sql.Time;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "user_schedule")
public class UserSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_schedule_id")
    private Long userScheduleId;
    
    @Column(name = "user_dep_time_lower_bound", columnDefinition = "time")
    private Time userDepTimeLowerBound;

    @Column(name = "user_dep_time_upper_bound", columnDefinition = "time")
    private Time userDepTimeUpperBound;

    public UserSchedule() {
        super();
    }
    
    public UserSchedule(Time userDepTimeLowerBound, Time userDepTimeUpperBound) {
        super();
        this.userDepTimeLowerBound = userDepTimeLowerBound;
        this.userDepTimeUpperBound = userDepTimeUpperBound;
    }

    public Long getUserScheduleId() {
        return userScheduleId;
    }

    public void setUserScheduleId(Long userScheduleId) {
        this.userScheduleId = userScheduleId;
    }

    public Time getUserDepTimeLowerBound() {
        return userDepTimeLowerBound;
    }

    public void setUserDepTimeLowerBound(Time userDepTimeLowerBound) {
        this.userDepTimeLowerBound = userDepTimeLowerBound;
    }

    public Time getUserDepTimeUpperBound() {
        return userDepTimeUpperBound;
    }

    public void setUserDepTimeUpperBound(Time userDepTimeUpperBound) {
        this.userDepTimeUpperBound = userDepTimeUpperBound;
    }
    
    
    
    
}
