package com.example.xmppconnection;

public interface IXmppListener {
    void connectError(Exception e);
    void connected();
    void authenticated(boolean resumed);
    void connectionClosed();
    void connectionClosedOnError(Exception e);
}
