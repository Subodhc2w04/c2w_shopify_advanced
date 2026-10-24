package com.c2w.model.delivery;

import java.util.ArrayList;
import java.util.List;


public class RouteInfo {

    private LocationModel origin;
    private LocationModel destination;
    private double distanceKm;
    private int estimatedMinutes;
    private List<LocationModel> waypoints = new ArrayList<>();

    public RouteInfo() {
    }

    public RouteInfo(LocationModel origin, LocationModel destination, double distanceKm, int estimatedMinutes,
            List<LocationModel> waypoints) {
        this.origin = origin;
        this.destination = destination;
        this.distanceKm = distanceKm;
        this.estimatedMinutes = estimatedMinutes;
        if (waypoints != null) {
            this.waypoints = waypoints;
        }
    }

    public LocationModel getOrigin() {
        return origin;
    }

    public void setOrigin(LocationModel origin) {
        this.origin = origin;
    }

    public LocationModel getDestination() {
        return destination;
    }

    public void setDestination(LocationModel destination) {
        this.destination = destination;
    }

    public double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public int getEstimatedMinutes() {
        return estimatedMinutes;
    }

    public void setEstimatedMinutes(int estimatedMinutes) {
        this.estimatedMinutes = estimatedMinutes;
    }

    public List<LocationModel> getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(List<LocationModel> waypoints) {
        this.waypoints = waypoints;
    }

    public String getFormattedDistance() {
        return String.format("%.1f km", distanceKm);
    }

    public String getFormattedEta() {
        if (estimatedMinutes <= 0) {
            return "~5 mins";
        } else if (estimatedMinutes < 60) {
            return "~" + estimatedMinutes + " mins";
        } else {
            int hrs = estimatedMinutes / 60;
            int mins = estimatedMinutes % 60;
            return "~" + hrs + " hr " + mins + " mins";
        }
    }

    @Override
    public String toString() {
        return "RouteInfo{" +
                "origin=" + origin +
                ", destination=" + destination +
                ", distanceKm=" + distanceKm +
                ", estimatedMinutes=" + estimatedMinutes +
                ", waypointsCount=" + waypoints.size() +
                '}';
    }
}
