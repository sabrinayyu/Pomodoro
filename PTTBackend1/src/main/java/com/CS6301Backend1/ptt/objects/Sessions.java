package com.CS6301Backend1.ptt.objects;

import com.CS6301Backend1.ptt.views.ReportViews;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotEmpty;

@Entity // This tells Hibernate to make a table out of this class
@Table(name="sessions")
public class Sessions {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @JsonIgnore
    @ManyToOne(targetEntity = User.class, fetch=FetchType.EAGER)
    @JoinColumn(name="user_id", referencedColumnName="id")
    private User user;

    @JsonIgnore
    @ManyToOne(targetEntity = Projects.class, fetch=FetchType.EAGER)
    @JoinColumn(name="project_id", referencedColumnName="id")
    private Projects project;

    @NotNull @NotEmpty
    @JsonView(ReportViews.IncludeDefault.class)
    private String startTime;

    @NotNull @NotEmpty
    @JsonView(ReportViews.IncludeDefault.class)
    private String endTime;

    @NotNull
    private int counter = Integer.MIN_VALUE;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getstartTime() {
        return startTime;
    }

    public void setstartTime(String startTime) {
        this.startTime = startTime ;
    }

    public void setendTime(String endTime) {
        this.endTime = endTime ;
    }

    public String getendTime() {
        return endTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Projects getProject() {
        return project;
    }

    public void setProject (Projects project) {
        this.project = project;
    }

    public int getcounter() {
        return counter;
    }

    public void setcounter (int counter) {
        this.counter= counter;
    }

}
