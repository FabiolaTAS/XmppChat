package com.example.xmppconnection;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterListener;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.jid.Jid;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class XMPPConnectionWrapper implements ConnectionListener, RosterListener {
    private static XMPPConnectionWrapper instance;
    private static AbstractXMPPConnection mConnection;

    private static final String HOST = "192.168.0.27";
    private static final int PORT = 5222;
    private static final String USER = "admin";
    private static final String PASSWORD = "admin";

    private IXmppListener listener;
    private XMPPTCPConnectionConfiguration config;

    private Roster roster;

    public static XMPPConnectionWrapper getInstance() {
        if(instance == null){
            instance = new XMPPConnectionWrapper();
        }
        return instance;
    }

    private XMPPConnectionWrapper configureConnection() throws XmppStringprepException, UnknownHostException {

        InetAddress addr = InetAddress.getByName(HOST);
        HostnameVerifier verifier = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return false;
            }
        };

        XMPPTCPConnectionConfiguration.Builder builder = XMPPTCPConnectionConfiguration.builder()
                .setUsernameAndPassword(USER, PASSWORD)
                .setXmppDomain(HOST)
                .setHost(HOST)
                .setPort(PORT)
                .setHostAddress(addr)
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                .setSendPresence(true)
                .setHostnameVerifier(verifier);

        config = builder.build();

        return this;
    }

    private XMPPConnectionWrapper connectUser(final String email, final String password){
        final AbstractXMPPConnection conn2 = new XMPPTCPConnection(config);
        new Runnable() {
            @Override
            public void run() {
                try {
                    mConnection = conn2;
                    configureListeners();

                    mConnection = conn2.connect();
                    mConnection.login(email, password);
                } catch (XMPPException e) {
                    listener.connectError(e);
                } catch (SmackException e) {
                    listener.connectError(e);
                } catch (IOException e) {
                    listener.connectError(e);
                } catch (InterruptedException e) {
                    listener.connectError(e);
                }
            }
        }.run();
        return this;
    }

    public void connect(String email, String password){
        try {
            this.configureConnection().connectUser(email, password);
        } catch (XmppStringprepException e) {
            listener.connectError(e);
        } catch (UnknownHostException e) {
            listener.connectError(e);
        }
    }

    private void configureListeners(){
        mConnection.addConnectionListener(this);

        roster = Roster.getInstanceFor(mConnection);
        roster.addRosterListener(this);
    }

    public void setListener(IXmppListener listener) {
        this.listener = listener;
    }

    public AbstractXMPPConnection getConnection(){
        return mConnection;
    }

    @Override
    public void connected(XMPPConnection connection) {
        listener.connected();
    }

    @Override
    public void authenticated(XMPPConnection connection, boolean resumed) {
        listener.authenticated(resumed);
    }

    @Override
    public void connectionClosed() {
        listener.connectionClosed();
    }

    @Override
    public void connectionClosedOnError(Exception e) {
        listener.connectionClosedOnError(e);
    }

    @Override
    public void entriesAdded(Collection<Jid> addresses) {

    }

    @Override
    public void entriesUpdated(Collection<Jid> addresses) {

    }

    @Override
    public void entriesDeleted(Collection<Jid> addresses) {

    }

    @Override
    public void presenceChanged(Presence presence) {

    }
}
