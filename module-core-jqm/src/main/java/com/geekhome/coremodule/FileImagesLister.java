package com.geekhome.coremodule;

import com.geekhome.common.KeyValue;
import com.geekhome.common.configuration.JSONArrayList;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class FileImagesLister implements IImagesLister {
    private List<String> _allowedImagesExtensions = Arrays.asList(".JPG",".PNG",".GIF",".SVG");

    public JSONArrayList<KeyValue> listImages() throws IOException {
        String currentDir = new File(".").getCanonicalPath();
        String imagesDirectory = currentDir + File.separator + "photos";
        JSONArrayList<KeyValue> result = new JSONArrayList<>();
        File folder = new File(imagesDirectory);
        if (folder.exists()) {

            File[] listOfFiles = folder.listFiles();
            if (listOfFiles != null && listOfFiles.length > 0) {
                for (int i = 0; i < listOfFiles.length; i++) {
                    File file = listOfFiles[i];
                    if (file.isFile() && isImageFile(file)) {
                        result.add(new KeyValue(String.valueOf(i), file.getName()));
                    }
                }
            }
        }
        return result;
    }


    private boolean isImageFile(File file) {
        String fileName = file.getName();
        String extension = file.getName().substring(fileName.indexOf(".")).toUpperCase();

        return _allowedImagesExtensions.contains(extension);
    }
}