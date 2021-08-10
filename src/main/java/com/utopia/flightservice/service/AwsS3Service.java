package com.utopia.flightservice.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class AwsS3Service {

    public void uploadFlightCsv(MultipartFile csv) throws IOException {
        Region region = Region.US_EAST_2;
        String bucketName = "flight-csv-bucket";
        String key = csv.getName();

        S3Client s3 = S3Client.builder().region(region).build();

        File file = convertMultipartFileToFile(csv);

        System.out.println("Uploading file");

        s3.putObject(PutObjectRequest.builder().bucket(bucketName).key(key).build(),
                RequestBody.fromFile(file));

        System.out.println("Closing");
        s3.close();
        file.delete();
        System.out.println("Closed");
    }

    private File convertMultipartFileToFile(MultipartFile multipart) throws IOException {
        File converted = new File("flight.csv");
        FileOutputStream stream = new FileOutputStream(converted);
        stream.write(multipart.getBytes());
        stream.close();
        return converted;
    }
}
