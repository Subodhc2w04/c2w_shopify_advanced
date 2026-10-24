package com.c2w.view.delivery;

import com.c2w.controller.delivery.LocationController;
import com.c2w.model.delivery.LocationModel;
import com.c2w.model.delivery.RouteInfo;
import com.gluonhq.maps.MapLayer;
import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;

import java.util.ArrayList;
import java.util.List;

public class DeliveryMapView {

    
    public static final double DEFAULT_START_LAT = LocationController.PARTNER_LAT;
    public static final double DEFAULT_START_LON = LocationController.PARTNER_LON;
    public static final String DEFAULT_START_NAME = LocationController.PARTNER_NAME;

    public static final double DEFAULT_DEST_LAT = 18.6279;
    public static final double DEFAULT_DEST_LON = 73.8131;

    /**
     * Creates dynamic Gluon Map node based on a calculated RouteInfo.
     */
    public static Region createGluonMapNode(RouteInfo routeInfo, double prefHeight) {
        VBox container = new VBox(10);
        container.setPrefHeight(prefHeight);
        container.setStyle(
                "-fx-background-color: #0f172a; -fx-background-radius: 14px; -fx-border-color: #334155; -fx-border-radius: 14px; -fx-padding: 16;");

        if (routeInfo == null) {
            LocationController locCtrl = new LocationController();
            routeInfo = locCtrl.getRouteForOrder(null);
        }

        LocationModel origin = routeInfo.getOrigin();
        LocationModel dest = routeInfo.getDestination();
        double distanceKm = routeInfo.getDistanceKm();
        String etaText = routeInfo.getFormattedEta();
        String distText = routeInfo.getFormattedDistance();

        String originName = (origin.getName() != null && !origin.getName().isEmpty())
                ? origin.getName()
                : DEFAULT_START_NAME;
        String destName = (dest.getName() != null && !dest.getName().isEmpty())
                ? dest.getName()
                : "Customer Delivery Destination";

        try {
            // Initialize Gluon MapView
            MapView mapView = new MapView();
            mapView.setPrefHeight(prefHeight - 50);
            mapView.setMaxHeight(prefHeight - 50);

            // Convert RouteInfo waypoints to Gluon MapPoints
            List<MapPoint> mapPoints = new ArrayList<>();
            for (LocationModel wp : routeInfo.getWaypoints()) {
                mapPoints.add(new MapPoint(wp.getLatitude(), wp.getLongitude()));
            }

            if (mapPoints.isEmpty()) {
                mapPoints.add(new MapPoint(origin.getLatitude(), origin.getLongitude()));
                mapPoints.add(new MapPoint(dest.getLatitude(), dest.getLongitude()));
            }

            // Calculate center between origin and destination
            double midLat = (origin.getLatitude() + dest.getLatitude()) / 2.0;
            double midLon = (origin.getLongitude() + dest.getLongitude()) / 2.0;
            mapView.setCenter(new MapPoint(midLat, midLon));

            // Dynamic zoom level based on distance
            int zoomLevel = 12;
            if (distanceKm > 50) {
                zoomLevel = 9;
            } else if (distanceKm > 25) {
                zoomLevel = 10;
            } else if (distanceKm > 12) {
                zoomLevel = 11;
            } else if (distanceKm > 5) {
                zoomLevel = 12;
            } else {
                zoomLevel = 13;
            }
            final int initialZoom = zoomLevel;
            mapView.setZoom(initialZoom);

            // Custom Dynamic Gluon Route & Pin Layer
            GluonRouteLayer routeLayer = new GluonRouteLayer(mapPoints, originName, destName);
            mapView.addLayer(routeLayer);

            Label routeTitle = new Label("Live Route Map");
            routeTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #38bdf8;");

            Label etaLabel = new Label(" ETA: " + etaText + " • Distance: " + distText + " (Optimal Route)");
            etaLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #34d399;");

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            // Zoom In / Out / Center Controls
            Button zoomInBtn = new Button("+");
            zoomInBtn.setStyle(
                    "-fx-background-color: #1e293b; -fx-text-fill: #ffffff; -fx-font-weight: bold; -fx-font-size: 13px; -fx-cursor: hand; -fx-border-color: #475569; -fx-border-radius: 4px;");
            zoomInBtn.setOnAction(e -> mapView.setZoom(mapView.getZoom() + 1));

            Button zoomOutBtn = new Button("-");
            zoomOutBtn.setStyle(
                    "-fx-background-color: #1e293b; -fx-text-fill: #ffffff; -fx-font-weight: bold; -fx-font-size: 13px; -fx-cursor: hand; -fx-border-color: #475569; -fx-border-radius: 4px;");
            zoomOutBtn.setOnAction(e -> mapView.setZoom(Math.max(1, mapView.getZoom() - 1)));

            Button recenterBtn = new Button("Center Route");
            recenterBtn.setStyle(
                    "-fx-background-color: #2563eb; -fx-text-fill: #ffffff; -fx-font-size: 11px; -fx-font-weight: bold; -fx-cursor: hand; -fx-border-radius: 4px;");
            recenterBtn.setOnAction(e -> {
                mapView.setCenter(new MapPoint(midLat, midLon));
                mapView.setZoom(initialZoom);
            });

            HBox controlsBox = new HBox(8, zoomInBtn, zoomOutBtn, recenterBtn);
            controlsBox.setAlignment(Pos.CENTER_RIGHT);

            HBox topBar = new HBox(12, routeTitle, etaLabel, spacer, controlsBox);
            topBar.setAlignment(Pos.CENTER_LEFT);

            StackPane mapHolder = new StackPane(mapView);
            mapHolder.setStyle("-fx-background-radius: 10px; -fx-border-color: #334155; -fx-border-radius: 10px;");
            VBox.setVgrow(mapHolder, Priority.ALWAYS);

            container.getChildren().addAll(topBar, mapHolder);
        } catch (Exception t) {
            t.printStackTrace();
            return container;
        }

        return container;
    }

   
    public static Region createGluonMapNode(String destinationAddress, double destLat, double destLon,
            double prefHeight) {
        LocationController locCtrl = new LocationController();
        LocationModel origin = locCtrl.getPartnerLocation();
        LocationModel dest = new LocationModel(destLat, destLon, destinationAddress, destinationAddress);
        double dist = locCtrl.calculateDistanceKm(origin.getLatitude(), origin.getLongitude(), destLat, destLon);
        int eta = locCtrl.calculateEstimatedMinutes(dist);
        List<LocationModel> waypoints = locCtrl.generateRouteWaypoints(origin, dest);
        RouteInfo routeInfo = new RouteInfo(origin, dest, dist, eta, waypoints);
        return createGluonMapNode(routeInfo, prefHeight);
    }

   
    public static class GluonRouteLayer extends MapLayer {
        private final List<MapPoint> waypoints = new ArrayList<>();
        private final Polyline polyline = new Polyline();
        private final Node startPin;
        private final Node endPin;

        public GluonRouteLayer(List<MapPoint> waypoints, String startLabel, String destLabel) {
            this.waypoints.addAll(waypoints);

            polyline.setStroke(Color.web("#3b82f6"));
            polyline.setStrokeWidth(5);
            polyline.setStrokeLineCap(StrokeLineCap.ROUND);
            polyline.setStrokeLineJoin(StrokeLineJoin.ROUND);
            polyline.getStrokeDashArray().addAll(12.0, 6.0);

            startPin = createPin("🛵 " + truncate(startLabel, 28), "#3b82f6");
            endPin = createPin("📍 " + truncate(destLabel, 32), "#10b981");

            this.getChildren().addAll(polyline, startPin, endPin);
        }

        private static String truncate(String text, int maxLen) {
            if (text == null) return "";
            if (text.length() <= maxLen) return text;
            return text.substring(0, maxLen - 3) + "...";
        }

        private Node createPin(String text, String colorHex) {
            Label label = new Label(text);
            label.setStyle("-fx-background-color: #1e293b; -fx-text-fill: " + colorHex
                    + "; -fx-font-weight: bold; -fx-font-size: 11px; -fx-padding: 4 9; -fx-background-radius: 6px; -fx-border-color: "
                    + colorHex + "; -fx-border-radius: 6px; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.5), 6, 0, 0, 2);");
            Circle dot = new Circle(5, Color.web(colorHex));
            VBox pinBox = new VBox(2, label, dot);
            pinBox.setAlignment(Pos.CENTER);
            return pinBox;
        }

        @Override
        protected void layoutLayer() {
            polyline.getPoints().clear();
            for (MapPoint wp : waypoints) {
                Point2D pt = getMapPoint(wp.getLatitude(), wp.getLongitude());
                if (pt != null) {
                    polyline.getPoints().addAll(pt.getX(), pt.getY());
                }
            }

            if (!waypoints.isEmpty()) {
                MapPoint startWp = waypoints.get(0);
                Point2D startPt = getMapPoint(startWp.getLatitude(), startWp.getLongitude());
                if (startPt != null) {
                    startPin.setLayoutX(startPt.getX() - 40);
                    startPin.setLayoutY(startPt.getY() - 35);
                }

                MapPoint endWp = waypoints.get(waypoints.size() - 1);
                Point2D endPt = getMapPoint(endWp.getLatitude(), endWp.getLongitude());
                if (endPt != null) {
                    endPin.setLayoutX(endPt.getX() - 40);
                    endPin.setLayoutY(endPt.getY() - 35);
                }
            }
        }
    }
}
