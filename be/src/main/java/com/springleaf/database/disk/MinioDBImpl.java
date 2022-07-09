package com.springleaf.database.disk;

import com.springleaf.common.Config;
import com.springleaf.common.DefaultValues;
import io.minio.*;
import io.minio.errors.ErrorResponseException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.regex.Pattern;

public class MinioDBImpl implements DiskDataFunction{

    private MinioClient minioClient;


    public MinioDBImpl() {
        minioClient = MinioClient.builder()
                .endpoint(Config.getProperty(Config.MINIO_HOST))
                .credentials(Config.getProperty(Config.MINIO_USERNAME), Config.getProperty(Config.MINIO_PASSWORD))
                .build();
    }

    private String getFileName(String path) {
        String[] component = path.split("/");
        if (component != null) {
            return component[component.length - 1];
        }
        return path;
    }

    @Override
    public InputStream getDataStreamFromPath(String path) throws FileNotFoundException {
        try {
            exist(path);
        }
        catch (Exception e) {
            throw new FileNotFoundException();
        }
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(DefaultValues.DEFAULT_FOLDER)
                            .object(path)
                            .build()
            );
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean insertDataStream(String path, InputStream data) {
        try {
            minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(DefaultValues.DEFAULT_FOLDER)
                        .object(path)
                        .stream(data, data.available(), -1)
                        .build()
            );
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateDataStream(String path, InputStream data) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(DefaultValues.DEFAULT_FOLDER)
                            .object(path)
                            .stream(data, data.available(), -1)
                            .build()
            );
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public byte[] getDataFromPath(String path) {
        return new byte[0];
    }

    @Override
    public boolean delete(String path) throws FileNotFoundException {
        return true;
    }

    @Override
    public boolean hardDelete(String path) {
        return false;
    }

    @Override
    public boolean exist(String path) throws FileNotFoundException {
        try {
            minioClient.statObject(StatObjectArgs.builder()
                            .bucket(DefaultValues.DEFAULT_FOLDER)
                            .object(path)
                            .build());
            return true;
        }
        catch (ErrorResponseException e) {
            return false;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<String> getAllFileInFolder(String folder) {
        return null;
    }

    @Override
    public boolean insert(String path, byte[] data) {
        return false;
    }

    @Override
    public boolean update(String path, byte[] data) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(DefaultValues.DEFAULT_FOLDER)
                            .object(path)
                            .stream(inputStream, inputStream.available(), -1)
                            .build()
            );
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteFolder(String folder) {
        return false;
    }

    @Override
    public double getFileSize(String path) {
        try {
            exist(path);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            StatObjectResponse stat = minioClient.statObject(StatObjectArgs.builder()
                    .bucket(DefaultValues.DEFAULT_FOLDER)
                    .object(path)
                    .build()
            );
            return stat.size() / 1024;
        }
        catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public boolean createFolder(String path) {
        String pathRegex = ".*/$";
        Pattern tester = Pattern.compile(pathRegex);
        if (!tester.matcher(path).matches()) {
            path = path + "/";
        }
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(DefaultValues.DEFAULT_FOLDER)
                            .object(path)
                            .stream(new ByteArrayInputStream(new byte[]{}), 0, -1)
                            .build()
            );
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isFolder(String path) throws FileNotFoundException {
        return false;
    }

    @Override
    public boolean isFile(String path) throws FileNotFoundException {

        return false;
    }
}
