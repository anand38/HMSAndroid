package com.example.anand38.jobportal.bean;

/**
 * Created by anand38 on 10/6/17.
 */

public class Job {
    private String position;
    private String location;
    private String salary;
    private String description;
    private String openings;
    private String eligibility;
    private String posted_on;
    public Job(){

    }
    public Job(String position,String salary,String location){
        this.position=position;
        this.salary=salary;
        this.location=location;
    }
    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOpenings() {
        return openings;
    }

    public void setOpenings(String openings) {
        this.openings = openings;
    }

    public String getEligibility() {
        return eligibility;
    }

    public void setEligibility(String eligibility) {
        this.eligibility = eligibility;
    }

    public String getPosted_on() {
        return posted_on;
    }

    public void setPosted_on(String posted_on) {
        this.posted_on = posted_on;
    }
}
