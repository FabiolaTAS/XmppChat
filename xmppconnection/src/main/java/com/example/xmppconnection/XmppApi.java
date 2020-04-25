package com.example.xmppconnection;

public class XmppApi{
    public XmppApi(IXmppListener listener){
        XMPPConnectionWrapper.getInstance().setListener(listener);
    }

    public void connectXmppServer(String email, String password){
        XMPPConnectionWrapper.getInstance().connect(email, password);
    }

}
