package com.c2w.controller.delivery;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.c2w.model.delivery.LocationModel;
import com.c2w.model.delivery.Order;
import com.c2w.model.delivery.RouteInfo;

public class LocationController {

    public static final double PARTNER_LAT = 18.4539;
    public static final double PARTNER_LON = 73.8275;
    public static final String PARTNER_NAME = "Narhe, Pune";
    public static final String PARTNER_ADDRESS = "Shopify, Narhe, Pune - 411041";

    // LinkedHashMap sorted so specific sub-localities match before broad city
    // keywords
    private static final Map<String, double[]> KNOWN_LOCALITIES = new LinkedHashMap<>();

    // Pincode lookup for Pune and Maharashtra
    private static final Map<String, double[]> PINCODE_LOCATIONS = new HashMap<>();

    static {
        // --- 1. Specific Sub-localities & Landmarks (Ordered by specificity) ---
        KNOWN_LOCALITIES.put("hinjewadi phase 3", new double[] { 18.5740, 73.6890 });
        KNOWN_LOCALITIES.put("hinjewadi phase 2", new double[] { 18.5820, 73.7140 });
        KNOWN_LOCALITIES.put("hinjewadi phase 1", new double[] { 18.5910, 73.7380 });
        KNOWN_LOCALITIES.put("hinjewadi", new double[] { 18.5975, 73.7188 });
        KNOWN_LOCALITIES.put("pimple saudagar", new double[] { 18.5987, 73.7978 });
        KNOWN_LOCALITIES.put("pimple gurav", new double[] { 18.5870, 73.8150 });
        KNOWN_LOCALITIES.put("pimple nilakh", new double[] { 18.5850, 73.7850 });
        KNOWN_LOCALITIES.put("koregaon park", new double[] { 18.5362, 73.8940 });
        KNOWN_LOCALITIES.put("kalyani nagar", new double[] { 18.5463, 73.9034 });
        KNOWN_LOCALITIES.put("viman nagar", new double[] { 18.5679, 73.9143 });
        KNOWN_LOCALITIES.put("magarpatta city", new double[] { 18.5152, 73.9288 });
        KNOWN_LOCALITIES.put("magarpatta", new double[] { 18.5152, 73.9288 });
        KNOWN_LOCALITIES.put("kharadi", new double[] { 18.5515, 73.9349 });
        KNOWN_LOCALITIES.put("hadapsar", new double[] { 18.5089, 73.9259 });
        KNOWN_LOCALITIES.put("karve nagar", new double[] { 18.4899, 73.8203 });
        KNOWN_LOCALITIES.put("kothrud", new double[] { 18.5074, 73.8077 });
        KNOWN_LOCALITIES.put("shivaji nagar", new double[] { 18.5314, 73.8446 });
        KNOWN_LOCALITIES.put("shivajinagar", new double[] { 18.5314, 73.8446 });
        KNOWN_LOCALITIES.put("senapati bapat road", new double[] { 18.5310, 73.8290 });
        KNOWN_LOCALITIES.put("sb road", new double[] { 18.5310, 73.8290 });
        KNOWN_LOCALITIES.put("fc road", new double[] { 18.5240, 73.8420 });
        KNOWN_LOCALITIES.put("jm road", new double[] { 18.5230, 73.8460 });
        KNOWN_LOCALITIES.put("deccan", new double[] { 18.5167, 73.8415 });
        KNOWN_LOCALITIES.put("swargate", new double[] { 18.5018, 73.8636 });
        KNOWN_LOCALITIES.put("camp", new double[] { 18.5158, 73.8797 });
        KNOWN_LOCALITIES.put("mg road", new double[] { 18.5180, 73.8770 });
        KNOWN_LOCALITIES.put("baner", new double[] { 18.5590, 73.7868 });
        KNOWN_LOCALITIES.put("balewadi high street", new double[] { 18.5710, 73.7760 });
        KNOWN_LOCALITIES.put("balewadi", new double[] { 18.5768, 73.7748 });
        KNOWN_LOCALITIES.put("wakad", new double[] { 18.5987, 73.7660 });
        KNOWN_LOCALITIES.put("aundh", new double[] { 18.5602, 73.8031 });
        KNOWN_LOCALITIES.put("bavdhan", new double[] { 18.5135, 73.7725 });
        KNOWN_LOCALITIES.put("pashan", new double[] { 18.5380, 73.7910 });
        KNOWN_LOCALITIES.put("chinchwad", new double[] { 18.6387, 73.7937 });
        KNOWN_LOCALITIES.put("pimpri", new double[] { 18.6279, 73.8131 });
        KNOWN_LOCALITIES.put("nigdi", new double[] { 18.6539, 73.7718 });
        KNOWN_LOCALITIES.put("bhosari", new double[] { 18.6298, 73.8478 });
        KNOWN_LOCALITIES.put("dapodi", new double[] { 18.5830, 73.8240 });
        KNOWN_LOCALITIES.put("khadki", new double[] { 18.5642, 73.8340 });
        KNOWN_LOCALITIES.put("kasarwadi", new double[] { 18.6015, 73.8185 });
        KNOWN_LOCALITIES.put("katraj", new double[] { 18.4480, 73.8587 });
        KNOWN_LOCALITIES.put("dhankawadi", new double[] { 18.4680, 73.8520 });
        KNOWN_LOCALITIES.put("ambegaon", new double[] { 18.4520, 73.8390 });
        KNOWN_LOCALITIES.put("narhe", new double[] { 18.4539, 73.8275 });
        KNOWN_LOCALITIES.put("vadgaon", new double[] { 18.4650, 73.8290 });
        KNOWN_LOCALITIES.put("kondhwa", new double[] { 18.4740, 73.8910 });
        KNOWN_LOCALITIES.put("undri", new double[] { 18.4600, 73.9100 });
        KNOWN_LOCALITIES.put("wanowrie", new double[] { 18.4870, 73.8960 });
        KNOWN_LOCALITIES.put("vishrantwadi", new double[] { 18.5695, 73.8745 });
        KNOWN_LOCALITIES.put("dhanori", new double[] { 18.5880, 73.8890 });
        KNOWN_LOCALITIES.put("lohegaon", new double[] { 18.5980, 73.9180 });
        KNOWN_LOCALITIES.put("wagholi", new double[] { 18.5800, 73.9800 });
        KNOWN_LOCALITIES.put("manjri", new double[] { 18.5180, 73.9780 });
        KNOWN_LOCALITIES.put("loni kalbhor", new double[] { 18.4870, 74.0200 });
        KNOWN_LOCALITIES.put("ravet", new double[] { 18.6470, 73.7480 });
        KNOWN_LOCALITIES.put("punawale", new double[] { 18.6250, 73.7490 });
        KNOWN_LOCALITIES.put("tathawade", new double[] { 18.6150, 73.7540 });
        KNOWN_LOCALITIES.put("moshi", new double[] { 18.6850, 73.8520 });
        KNOWN_LOCALITIES.put("chakan", new double[] { 18.7580, 73.8580 });
        KNOWN_LOCALITIES.put("talegaon", new double[] { 18.7300, 73.6800 });
        KNOWN_LOCALITIES.put("navi mumbai", new double[] { 19.0330, 73.0297 });
        KNOWN_LOCALITIES.put("mumbai", new double[] { 19.0760, 72.8777 });
        KNOWN_LOCALITIES.put("thane", new double[] { 19.2183, 72.9781 });

        // --- 2. Pincode to Coordinate Mapping ---
        PINCODE_LOCATIONS.put("411001", new double[] { 18.5284, 73.8739 }); // Camp / Station
        PINCODE_LOCATIONS.put("411002", new double[] { 18.5074, 73.8562 }); // Shukrawar Peth
        PINCODE_LOCATIONS.put("411004", new double[] { 18.5167, 73.8415 }); // Deccan
        PINCODE_LOCATIONS.put("411005", new double[] { 18.5314, 73.8446 }); // Shivajinagar
        PINCODE_LOCATIONS.put("411007", new double[] { 18.5602, 73.8031 }); // Aundh
        PINCODE_LOCATIONS.put("411014", new double[] { 18.5679, 73.9143 }); // Viman Nagar
        PINCODE_LOCATIONS.put("411015", new double[] { 18.5695, 73.8745 }); // Vishrantwadi
        PINCODE_LOCATIONS.put("411018", new double[] { 18.6279, 73.8131 }); // Pimpri
        PINCODE_LOCATIONS.put("411019", new double[] { 18.6387, 73.7937 }); // Chinchwad
        PINCODE_LOCATIONS.put("411027", new double[] { 18.5987, 73.7978 }); // Pimple Saudagar
        PINCODE_LOCATIONS.put("411028", new double[] { 18.5089, 73.9259 }); // Hadapsar
        PINCODE_LOCATIONS.put("411033", new double[] { 18.6120, 73.7820 }); // Thergaon
        PINCODE_LOCATIONS.put("411037", new double[] { 18.4890, 73.8670 }); // Marketyard
        PINCODE_LOCATIONS.put("411038", new double[] { 18.5074, 73.8077 }); // Kothrud
        PINCODE_LOCATIONS.put("411041", new double[] { 18.4539, 73.8275 }); // Narhe / Vadgaon
        PINCODE_LOCATIONS.put("411043", new double[] { 18.4680, 73.8520 }); // Dhankawadi
        PINCODE_LOCATIONS.put("411044", new double[] { 18.6539, 73.7718 }); // Nigdi
        PINCODE_LOCATIONS.put("411045", new double[] { 18.5590, 73.7868 }); // Baner
        PINCODE_LOCATIONS.put("411046", new double[] { 18.4480, 73.8587 }); // Katraj
        PINCODE_LOCATIONS.put("411048", new double[] { 18.4740, 73.8910 }); // Kondhwa
        PINCODE_LOCATIONS.put("411051", new double[] { 18.4730, 73.8220 }); // Sinhagad Road
        PINCODE_LOCATIONS.put("411052", new double[] { 18.4899, 73.8203 }); // Karvenagar
        PINCODE_LOCATIONS.put("411057", new double[] { 18.5975, 73.7188 }); // Hinjewadi / Wakad
        PINCODE_LOCATIONS.put("411060", new double[] { 18.4600, 73.9100 }); // Undri
    }

    public LocationModel getPartnerLocation() {
        return new LocationModel(PARTNER_LAT, PARTNER_LON, PARTNER_NAME, PARTNER_ADDRESS);
    }

    /**
     * Resolves coordinates dynamically for any address string.
     * Order of resolution:
     * 1. Check for specific 6-digit Pincode.
     * 2. Check for specific locality keywords (e.g. Kothrud, Hinjewadi, Baner,
     * Wakad, Viman Nagar).
     * 3. Online OpenStreetMap geocoding fallback.
     * 4. Dynamic hash-based coordinate distribution (gives unique, realistic
     * coordinates per address).
     */
    public LocationModel getCoordinatesForAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            return new LocationModel(18.5074, 73.8077, "Customer Destination", "Kothrud, Pune");
        }

        String lowerAddr = address.toLowerCase();

        // 1. Check for 6-digit pincode in address
        Pattern pinPattern = Pattern.compile("\\b(4[0-9]{5})\\b");
        Matcher pinMatcher = pinPattern.matcher(address);
        if (pinMatcher.find()) {
            String pin = pinMatcher.group(1);
            if (PINCODE_LOCATIONS.containsKey(pin)) {
                double[] c = PINCODE_LOCATIONS.get(pin);
                return new LocationModel(c[0], c[1], address, address);
            }
        }

        // 2. Check for specific localities (ordered from most specific to general)
        for (Map.Entry<String, double[]> entry : KNOWN_LOCALITIES.entrySet()) {
            if (lowerAddr.contains(entry.getKey())) {
                double[] c = entry.getValue();
                return new LocationModel(c[0], c[1], address, address);
            }
        }

        // 3. Try Online OSM Geocoding
        try {
            LocationModel online = geocodeViaNominatim(address);
            if (online != null) {
                return online;
            }
        } catch (Exception ignored) {
        }

        // 4. Fallback: Deterministic dynamic coordinate spread across Pune Metropolitan
        // area
        int hash = Math.abs(address.hashCode());
        // Distribute coordinates in a realistic radius (3 km to 18 km) around Pune
        // center
        double angle = (hash % 360) * Math.PI / 180.0;
        double radiusKm = 3.0 + ((hash % 150) / 10.0); // 3.0 km to 18.0 km
        double latOffset = (radiusKm / 111.0) * Math.sin(angle);
        double lonOffset = (radiusKm / (111.0 * Math.cos(Math.toRadians(18.5204)))) * Math.cos(angle);

        double fallbackLat = Math.round((18.5204 + latOffset) * 10000.0) / 10000.0;
        double fallbackLon = Math.round((73.8567 + lonOffset) * 10000.0) / 10000.0;

        return new LocationModel(fallbackLat, fallbackLon, address, address);
    }

    private LocationModel geocodeViaNominatim(String address) {
        try {
            String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8.toString());
            String urlStr = "https://nominatim.openstreetmap.org/search?format=json&q=" + encodedAddress + "&limit=1";

            URI uri = URI.create(urlStr);
            URL url = uri.toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "ShopifyDeliveryApp/1.0 (JavaFX MVC)");
            conn.setConnectTimeout(1200);
            conn.setReadTimeout(1200);

            if (conn.getResponseCode() == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                String json = response.toString();
                int latIdx = json.indexOf("\"lat\":\"");
                int lonIdx = json.indexOf("\"lon\":\"");

                if (latIdx != -1 && lonIdx != -1) {
                    int latEnd = json.indexOf("\"", latIdx + 7);
                    int lonEnd = json.indexOf("\"", lonIdx + 7);

                    String latStr = json.substring(latIdx + 7, latEnd);
                    String lonStr = json.substring(lonIdx + 7, lonEnd);

                    double lat = Double.parseDouble(latStr);
                    double lon = Double.parseDouble(lonStr);
                    return new LocationModel(lat, lon, address, address);
                }
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    // Calculates the road distance in kilometers using the Haversine formula with a
    // 1.25x road factor.

    public double calculateDistanceKm(double lat1, double lon1, double lat2, double lon2) {
        final double EARTH_RADIUS = 6371.0; // km

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2.0) * Math.sin(dLat / 2.0)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                        * Math.sin(dLon / 2.0) * Math.sin(dLon / 2.0);

        double c = 2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1.0 - a));
        double straightDistance = EARTH_RADIUS * c;

        // Multiply by 1.25 for actual street navigation curvature
        double roadDistance = straightDistance * 1.25;

        return Math.max(1.2, Math.round(roadDistance * 10.0) / 10.0);
    }

    /**
     * Estimates travel duration in minutes based on distance and city speed (25
     * km/h) + traffic buffer.
     */
    public int calculateEstimatedMinutes(double distanceKm) {
        if (distanceKm <= 1.5) {
            return 8;
        }
        int travelMins = (int) Math.round((distanceKm / 24.0) * 60.0);
        return Math.max(8, travelMins + 4);
    }

    /**
     * Generates intermediate route waypoints between origin and destination for
     * smooth tracking line rendering.
     */
    public List<LocationModel> generateRouteWaypoints(LocationModel origin, LocationModel destination) {
        List<LocationModel> points = new ArrayList<>();
        points.add(origin);

        int intermediateSteps = 4;
        for (int i = 1; i <= intermediateSteps; i++) {
            double fraction = (double) i / (intermediateSteps + 1);

            double lat = origin.getLatitude() + fraction * (destination.getLatitude() - origin.getLatitude());
            double lon = origin.getLongitude() + fraction * (destination.getLongitude() - origin.getLongitude());

            // Add realistic road curve jitter
            double jitter = Math.sin(fraction * Math.PI) * 0.0035;
            lat += jitter;
            lon -= jitter * 0.5;

            points.add(new LocationModel(lat, lon, "Waypoint " + i, ""));
        }

        points.add(destination);
        return points;
    }

    /**
     * Builds and returns a complete RouteInfo model for an active Order.
     */
    public RouteInfo getRouteForOrder(Order order) {
        LocationModel origin = getPartnerLocation();
        String address = (order != null && order.getDeliveryAddress() != null
                && !order.getDeliveryAddress().trim().isEmpty())
                        ? order.getDeliveryAddress().trim()
                        : "Customer Destination";

        LocationModel destination = getCoordinatesForAddress(address);
        double distanceKm = calculateDistanceKm(
                origin.getLatitude(), origin.getLongitude(),
                destination.getLatitude(), destination.getLongitude());

        int etaMinutes = calculateEstimatedMinutes(distanceKm);
        List<LocationModel> waypoints = generateRouteWaypoints(origin, destination);

        return new RouteInfo(origin, destination, distanceKm, etaMinutes, waypoints);
    }

    /**
     * Calculates driving distance in km from the partner store location to customer
     * address.
     */
    public double calculateDistanceForAddress(String address) {
        System.out.println("}}}}}}}}}}}}}}}}}}}} in calculateDistanceForAddress For distance address is:" + address);
        LocationModel origin = getPartnerLocation();
        LocationModel destination = getCoordinatesForAddress(address);
        return calculateDistanceKm(
                origin.getLatitude(), origin.getLongitude(),
                destination.getLatitude(), destination.getLongitude());
    }

    /**
     * Calculates delivery charges dynamically like modern food & e-commerce apps
     * (e.g. Swiggy/Zomato/Blinkit):
     * - Base fee: ₹30 (covers first 3 km)
     * - Distance surcharge: ₹12 per km for additional distance beyond 3 km
     */
    public double calculateDeliveryChargeForDistance(double distanceKm) {

        double baseFee = 30.0;
        double baseDistanceKm = 3.0;
        double perKmBeyondBase = 12.0;

        if (distanceKm <= baseDistanceKm) {
            return baseFee;
        } else {
            double extraKm = distanceKm - baseDistanceKm;
            return Math.round(baseFee + (extraKm * perKmBeyondBase));
        }
       

    }

}
