package com.geekhome.common;

import com.geekhome.common.logging.ILogger;
import com.geekhome.common.logging.LoggingService;
import com.geekhome.coremodule.ActivationStatus;
import com.geekhome.coremodule.settings.AutomationSettings;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Hashtable;

public class LicenseManager implements ILicenseManager {
    private static final String TABLE_ADAPTERS = "Adapters";
    private AutomationSettings _automationSettings;
    private Hashtable<String, ActivationStatus> _activationErrors = new Hashtable<>();
    private ILogger _logger = LoggingService.getLogger();
    private char[] _codeTable = {
            'a','b','c','d','e','f','g','h',
            'i','j','k','l','m','n','p','r',
            's','t','u','v','w','x','y','z',
            '0','1','2','3','4','5','6','7',
    };

    public LicenseManager(AutomationSettings automationSettings) {
        _automationSettings = automationSettings;
    }

    @Override
    public void reportAdapter(INamedObject adapter, String activationId) throws NoSuchAlgorithmException {
        String activationCode = _automationSettings.readTable(TABLE_ADAPTERS, activationId);
        if (!hash(activationId).equals(activationCode)) {
            ActivationStatus activationStatus = new ActivationStatus(adapter.getName(), activationId, activationCode);
            _activationErrors.put(activationId, activationStatus);
        }
    }

    @Override
    public Hashtable<String, ActivationStatus> getActivationErrors() {
        return _activationErrors;
    }

    @Override
    public String hash(String toHash) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(toHash.getBytes());
        byte[] digest32 = md.digest();
        byte[] digest8 = new byte[] {
                (byte)(digest32[0] + digest32[3] - digest32[4] + digest32[24]) ,
                (byte)(digest32[2] + digest32[1] - digest32[5] + digest32[26]),
                (byte)(digest32[8] + digest32[7] - digest32[6] + digest32[29]),
                (byte)(digest32[13] + digest32[10] - digest32[12] + digest32[30]),
                (byte)(digest32[14] + digest32[20] - digest32[11] + digest32[28]),
                (byte)(digest32[21] + digest32[19] - digest32[16] + digest32[31]),
                (byte)(digest32[23] + digest32[15] - digest32[17] + digest32[27]),
                (byte)(digest32[22] + digest32[18] - digest32[9] + digest32[25])
        };

        return new String(new char[] {
                _codeTable[(digest8[0] + 128) % 32],
                _codeTable[(digest8[1] + 128) % 32],
                _codeTable[(digest8[2] + 128) % 32],
                _codeTable[(digest8[3] + 128) % 32],
                _codeTable[(digest8[4] + 128) % 32],
                _codeTable[(digest8[5] + 128) % 32],
                _codeTable[(digest8[6] + 128) % 32],
                _codeTable[(digest8[7] + 128) % 32],
        });
    }

    @Override
    public boolean enterLicense(String activationId, String code) {
        try {
            String codeCheck = hash(activationId);
            _automationSettings.changeSetting(TABLE_ADAPTERS, activationId, code);
            if (code.equals(codeCheck)) {
                _activationErrors.remove(activationId);
                return true;
            }
        } catch (Exception ignored) {
            _logger.warning(ignored.toString());
        }

        return false;
    }
}
