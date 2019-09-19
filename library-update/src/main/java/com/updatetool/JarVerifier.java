package com.updatetool;

import org.eclipse.jetty.util.security.Password;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class JarVerifier {
    private JarFile jarFile = null;

    JarVerifier(JarFile jarFile) {
        this.jarFile = jarFile;
    }

    public void verify(String thumbPrint)
            throws IOException, NoSuchAlgorithmException, CertificateEncodingException {
        Vector<JarEntry> entriesVec = new Vector<JarEntry>();

        // Ensure the jar file is signed.
        Manifest man = jarFile.getManifest();
        if (man == null) {
            throw new SecurityException("The provider is not signed");
        }

        // Ensure all the entries' signatures verify correctly
        byte[] buffer = new byte[8192];
        Enumeration entries = jarFile.entries();

        while (entries.hasMoreElements()) {
            JarEntry je = (JarEntry) entries.nextElement();

            // Skip directories.
            if (je.isDirectory()) continue;
            entriesVec.addElement(je);
            InputStream is = jarFile.getInputStream(je);

            // Read in each jar entry. A security exception will
            // be thrown if a signature/digest check fails.
            while ((is.read(buffer, 0, buffer.length)) != -1) {
                // Don't care
            }
            is.close();
        }

        Enumeration e = entriesVec.elements();

        while (e.hasMoreElements()) {
            JarEntry je = (JarEntry) e.nextElement();

            Certificate[] certs = je.getCertificates();
            if ((certs == null) || (certs.length == 0)) {
                if (!je.getName().startsWith("META-INF"))
                    throw new SecurityException("The provider has unsigned class files.");
            } else {
                for (Certificate certificate : certs) {
                    X509Certificate cert0 = (X509Certificate) certificate;
                    String signatureThumbprint = getThumbPrint(cert0);
                    if (!signatureThumbprint.equals("OBF:" + thumbPrint)) {
                        throw new SecurityException("Invalid package signature");
                    }
                }
            }
        }
    }

    public String getThumbPrint(X509Certificate cert)
            throws NoSuchAlgorithmException, CertificateEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] der = cert.getEncoded();
        md.update(der);
        byte[] digest = md.digest();
        String thumbprint = hexify(digest);
        return Password.obfuscate(thumbprint);
    }

    public String hexify (byte bytes[]) {

        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

        StringBuilder buf = new StringBuilder(bytes.length * 2);

        for (byte aByte : bytes) {
            buf.append(hexDigits[(aByte & 0xf0) >> 4]);
            buf.append(hexDigits[aByte & 0x0f]);
        }

        return buf.toString();
    }
}
