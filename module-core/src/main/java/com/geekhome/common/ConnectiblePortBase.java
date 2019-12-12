package com.geekhome.common;

public class ConnectiblePortBase extends PortBase implements IConnectible {
    private boolean _isConnected;
    private long _lastSeenTimestamp;

    public ConnectiblePortBase(String id) {
        super(id);
        _isConnected = true;
    }

    @Override
    public boolean isConnected() {
        return _isConnected;
    }

    @Override
    public void markDisconnected() {
        _isConnected = false;
    }

    public long getLastSeenTimestamp() {
        return _lastSeenTimestamp;
    }

    void updateLastSeen(long timestamp) {
        _lastSeenTimestamp = timestamp;
    }
}
