package com.updatetool;

import com.geekhome.http.IHttpListenerRequest;
import com.geekhome.http.IRequestsDispatcher;
import com.geekhome.http.IResponse;
import com.geekhome.http.jetty.RedirectionResponse;
import org.apache.commons.io.FileUtils;

import java.io.File;

public class FactoryResetDispatcher implements IRequestsDispatcher {
    @Override
    public IResponse dispatch(IHttpListenerRequest request) throws Exception {
        try {
            File factoryDirectory = new File("." + File.separator + "factorybin");
            File targetDirectory = new File("." + File.separator + "serverbin");
            File usersFile = new File("." + File.separator + "geekhomerealm.properties");
            FileUtils.deleteDirectory(targetDirectory);
            FileUtils.copyDirectory(factoryDirectory, targetDirectory);
            FileUtils.writeStringToFile(usersFile, "user: OBF:1yte1vv11vu91yt8,user");
            return new RedirectionResponse("/resetsuccess.htm");
        } catch (Exception ex) {
            return new RedirectionResponse("/resetfailure.htm");
        }
    }

    @Override
    public boolean isRequestSupported(IHttpListenerRequest request) {
        return request.getUrl().getOriginalString().toUpperCase().equals("/FACTORYRESET");
    }
}