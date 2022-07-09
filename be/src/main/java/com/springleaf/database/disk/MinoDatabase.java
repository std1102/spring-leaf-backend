package com.springleaf.database.disk;

import com.springleaf.common.Config;
import com.springleaf.common.DefaultValues;
import com.springleaf.database.Database;
import io.minio.*;

public class MinoDatabase implements Database {

    private MinioClient minioClient;

    @Override
    public void setup() {
        minioClient = MinioClient.builder()
                .endpoint(Config.getProperty(Config.MINIO_HOST))
                .credentials(Config.getProperty(Config.MINIO_USERNAME), Config.getProperty(Config.MINIO_PASSWORD))
                .build();
        try {
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(DefaultValues.DEFAULT_FOLDER).build())) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(DefaultValues.DEFAULT_FOLDER).build());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
