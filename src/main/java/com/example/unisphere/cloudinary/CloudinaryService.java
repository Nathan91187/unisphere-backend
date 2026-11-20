
package com.example.unisphere.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public UploadedImage upload(MultipartFile file, ImageFolder folder) throws IOException {

        // Uploads the file's contents to Cloudinary.
        Map result = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                        "resource_type", "image",
                        "folder", folder.getPath()
                )
        );

        // Extracts the image URL and public ID from Cloudinary's response.
        String secureUrl = (String) result.get("secure_url");
        String publicId = (String) result.get("public_id");
        if(secureUrl == null || publicId == null ){
            throw new IOException("Cloudinary didn't return the expected image details");
        }
        // Returns the uploaded image information.
        return new UploadedImage(secureUrl, publicId);
    }

    public void delete(String publicId) throws IOException {

        Map result = cloudinary.uploader().destroy(
                publicId,
                ObjectUtils.emptyMap()
        );

        String deletionResult = (String) result.get("result");

        if (!"ok".equals(deletionResult) && !"not found".equals(deletionResult)) {
            throw new IOException("Failed to delete image from Cloudinary.");
        }
    }
}
