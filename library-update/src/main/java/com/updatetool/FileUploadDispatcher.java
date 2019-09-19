package com.updatetool;

import com.geekhome.http.IHttpListenerRequest;
import com.geekhome.http.IRequestsDispatcher;
import com.geekhome.http.IResponse;
import com.geekhome.http.jetty.RedirectionResponse;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import javax.servlet.http.Part;
import java.io.File;
import java.io.FileOutputStream;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileUploadDispatcher implements IRequestsDispatcher {
    private String _thumbprintObfuscated;

    public FileUploadDispatcher(String thumbprintObfuscated) {
        _thumbprintObfuscated = thumbprintObfuscated;
    }

    @Override
    public IResponse dispatch(IHttpListenerRequest request) throws Exception {
        try {
            Part filePart = request.getPart("file");
            install(filePart);
            return new RedirectionResponse("/updatesuccess.htm");
        } catch (Exception ex) {
            return new RedirectionResponse("/updatefailure.htm");
        }
    }

    @Override
    public boolean isRequestSupported(IHttpListenerRequest request) {
        return request.getHttpMethod().equals("POST") && request.getUrl().getOriginalString().toUpperCase().equals("/UPLOAD");
    }

    private void install(Part filePart) throws Exception {
        File temporaryDirectory = new File("." + File.separator + "temp");
        File targetDirectory = new File("." + File.separator + "serverbin");
        if (temporaryDirectory.exists()) {
            FileUtils.deleteDirectory(temporaryDirectory);
        }
        unzipPackage(filePart);
        if (targetDirectory.exists()) {
            FileUtils.deleteDirectory(targetDirectory);
        }
        FileUtils.moveDirectory(temporaryDirectory, targetDirectory);
    }

    private void unzipPackage(Part filePart) throws Exception {
        byte[] buffer = new byte[102400];
        ZipInputStream zis = new ZipInputStream(filePart.getInputStream());
        ZipEntry ze = zis.getNextEntry();
        while( ze != null){
            File targetFile = new File("." + File.separator + "temp" + File.separator + ze.getName());
            new File(targetFile.getParent()).mkdirs();
            FileOutputStream fos = new FileOutputStream(targetFile);
            int len;
            while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
            fos.close();
            validateFile(targetFile);
            ze = zis.getNextEntry();
        }
    }

    private void validateFile(File targetFile) throws Exception {
        String ext = FilenameUtils.getExtension(targetFile.getAbsolutePath());
        if (!ext.toLowerCase().equals("jar")) {
            throw new Exception("Only jars are allowed to be placed in bin folder!");
        } else {
            JarFile jar = new JarFile(targetFile);
            JarVerifier jarVerifier = new JarVerifier(jar);
            try {
                jarVerifier.verify(_thumbprintObfuscated);
            } finally {
                jar.close();
            }
        }
    }
}