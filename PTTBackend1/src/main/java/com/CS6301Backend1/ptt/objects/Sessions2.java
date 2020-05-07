package com.CS6301Backend1.ptt.objects;

import com.CS6301Backend1.ptt.views.ReportViews;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotEmpty;

@Component
public class Sessions2 {

    @NotNull @NotEmpty
    @JsonView(ReportViews.IncludeDefault.class)
    private String startingTime;

    @NotNull @NotEmpty
    @JsonView(ReportViews.IncludeDefault.class)
    private String endingTime;

    @JsonView(ReportViews.IncludeDefault.class)
    private double hoursWorked;


    public String getstartingTime() {
        return startingTime;
    }

    public void setstartingTime(String startingTime) {
        this.startingTime = startingTime ;
    }

    public void setendingTime(String endingTime) {
        this.endingTime = endingTime ;
    }

    public String getendingTime() {
        return endingTime;
    }

    public double gethoursWorked() {
        return hoursWorked;
    }

    public void sethoursWorked(double hoursWorked) {
        this.hoursWorked = hoursWorked;
    }
}
