package com.example.xmppchat.repository

import android.os.AsyncTask
import androidx.lifecycle.MutableLiveData
import com.example.xmppconnection.IXmppListener
import com.example.xmppconnection.XmppApi

class XmppRepository : IXmppListener{
    private val apiXmpp : XmppApi = XmppApi(this)

    private val connection : MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    fun connect(email: String, passoword: String): MutableLiveData<Boolean> {
        class doAsync(val handler: () -> Unit) : AsyncTask<Void, Void, Void>() {
            init {
                execute()
            }

            override fun doInBackground(vararg params: Void?): Void? {
                handler()
                return null
            }
        }

        doAsync{
            apiXmpp.connectXmppServer(email, passoword)
        }
        return connection;
    }

    override fun connectError(e: Exception?) {
        e?.printStackTrace()
        this.connection.postValue(false);
    }

    override fun connected() {
        //this.connection.postValue(true);
    }

    override fun authenticated(resumed: Boolean) {
        this.connection.postValue(true);
    }

    override fun connectionClosed() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun connectionClosedOnError(e: java.lang.Exception?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}