package com.geekhome.common;


import com.geekhome.coremodule.ActivationStatus;

import java.security.NoSuchAlgorithmException;
import java.util.Hashtable;


public interface ILicenseManager {
    void reportAdapter(INamedObject adapterId, String activationId) throws NoSuchAlgorithmException;
    Hashtable<String, ActivationStatus> getActivationErrors();

    String hash(String toHash) throws NoSuchAlgorithmException;

    boolean enterLicense(String adapterId, String code);
}
