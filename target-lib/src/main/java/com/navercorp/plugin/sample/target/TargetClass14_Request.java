package com.navercorp.plugin.sample.target;

import java.util.Map;

public class TargetClass14_Request {
    private final String clientAddress;
    private final String serverHostName;
    private final String procedure;
    private final String argument;
    private final Map<String, String> metadatas;
    
    public TargetClass14_Request(String clientAddress, String serverHostName, String procedure, String argument, Map<String, String> metadatas) {
        this.clientAddress = clientAddress;
        this.serverHostName = serverHostName;
        this.procedure = procedure;
        this.argument = argument;
        this.metadatas = metadatas;
    }

    public String getClientAddress() {
        return clientAddress;
    }
    
    public String getServerHostName() {
        return serverHostName;
    }

    public String getProcedure() {
        return procedure;
    }
    
    public String getArgument() {
        return argument;
    }
    
    public String getMetadata(String name) {
        return metadatas.get(name);
    }
}