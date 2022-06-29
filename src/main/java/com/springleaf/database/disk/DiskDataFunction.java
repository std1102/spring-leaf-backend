package com.springleaf.database.disk;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

// TODO need roll back function
public interface DiskDataFunction {
    public InputStream getDataStreamFromPath(String path) throws FileNotFoundException;
    public boolean insertDataStream(String path, InputStream data);
    public boolean updateDataStream(String path, InputStream data) throws FileNotFoundException;
    public byte[] getDataFromPath(String path);
    public boolean delete(String path) throws FileNotFoundException;
    public boolean hardDelete(String path);
    public boolean exist(String path) throws FileNotFoundException;
    // return list path of file
    public List<String> getAllFileInFolder(String folder);
    public boolean insert(String path, byte[] data);
    // actual is replace current file
    public boolean update(String path, byte[] data);
    public boolean deleteFolder(String folder);
    public double getFileSize(String path);
    public boolean createFolder(String path);
    public boolean isFolder(String path) throws FileNotFoundException;
    public boolean isFile(String path) throws FileNotFoundException;
}
