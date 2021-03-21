package com.gwt.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;


public class gwtServiceImpl extends RemoteServiceServlet {
    // Implementation of sample interface method
    public String getMessage(String msg) {
        return "Client said: \"" + msg + "\"<br>Server answered: \"Hi!\"";
    }
}