package com.example.a10108309.service;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * AbstractService. This is used for the Observer pattern.
 */

public abstract class AbstractService implements Serializable, Runnable {

    private ArrayList<ServiceListener> listeners;
    private boolean error;

    /**
     * Contains a list of listeners.
     */
    public AbstractService(){
        listeners = new ArrayList<>();
    }

    /**
     * Adds a service listener to the list of listeners
     * @param serviceListener
     */
    public void addListener(ServiceListener serviceListener){
        listeners.add(serviceListener);
    }

    /**
     * Removes a listener from the list of listeners
     * @param serviceListener
     */
    public void removeListener(ServiceListener serviceListener){
        listeners.remove(serviceListener);
    }

    /**
     * Checks if the current service had an error.
     * @return
     */
    public boolean hasError() {
        return error;
    }

    /**
     * Called when the service has finished its request.
     * @param error
     */
    public void serviceCallComplete(boolean error){
        this.error = error;

        Message m = _handler.obtainMessage();
        Bundle b = new Bundle();

        b.putSerializable("service", this);
        m.setData(b);
        _handler.sendMessage(m);
    }

    /**
     * Handler for the services.
     */
    @SuppressLint("HandlerLeak")
    final Handler _handler = new Handler(){
        public void handleMessage(Message msg){
            AbstractService service = (AbstractService)msg.getData().getSerializable("service");

            for(ServiceListener serviceListener : service.listeners){
                serviceListener.serviceComplete(service);
            }
        }
    };
}
