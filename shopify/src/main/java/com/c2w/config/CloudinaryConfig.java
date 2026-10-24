package com.c2w.config;

import java.util.Map;

import com.cloudinary.Cloudinary;

public class CloudinaryConfig {
    static {
        initializeCloudinary();
    }
    public static Cloudinary cloudinary;
    public static void initializeCloudinary() {
         cloudinary = new Cloudinary(Map.of(
                "cloud_name", "YOUR_CLOUD_NAME",
                "api_key", "YOUR_API_KEY",
                "api_secret", "YOUR_API_SECRET"));

    }
    public static Cloudinary getCloudinaryInstance() {
        return cloudinary;
    }
}
