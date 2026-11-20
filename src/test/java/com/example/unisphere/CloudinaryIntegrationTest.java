package com.example.unisphere;

import com.cloudinary.Cloudinary;
import com.cloudinary.api.ApiResponse;
import com.cloudinary.utils.ObjectUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class CloudinaryIntegrationTest {

    @Autowired
    private Cloudinary cloudinary;

    @Test
    void testCloudinaryConnection() throws Exception {

        // Sends a ping request to verify Cloudinary connectivity and authentication.
        ApiResponse response = cloudinary.api().ping(ObjectUtils.emptyMap());

        // Verifies that Cloudinary reports a successful response.
        assertEquals("ok", response.get("status"));
    }
    @Test
    void testImageUpload() throws Exception {

        // Loads the test image from the test resource's directory.
        File file = new ClassPathResource("ronaldo_crying.png").getFile();

        // Uploads the image to Cloudinary.
        Map result = cloudinary.uploader().upload(
                file,
                ObjectUtils.emptyMap()
        );

        // Displays the uploaded image's URL and public ID.
        System.out.println("Image URL: " + result.get("secure_url"));
        System.out.println("Public ID: " + result.get("public_id"));

        // Verifies that Cloudinary returned an image URL.
        assertNotNull(result.get("secure_url"));
    }
}
