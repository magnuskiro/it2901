package no.ntnu.qos.client;

import no.ntnu.qos.client.credentials.Token;

public class DataObject {

    private Sequencer sequencer;
    private Boolean sane;
    private int bandwidth;
    private Token token;

    public DataObject(Sequencer sequencer) {
        this.sequencer = sequencer;
    }

    public String getSoap(){
        return "";
    }

    public Sequencer getSequencer() {
        return sequencer;
    }

    public void setSequencer(Sequencer sequencer) {
        this.sequencer = sequencer;
    }

    public Boolean getSane() {
        return sane;
    }

    public void setSane(Boolean sane) {
        this.sane = sane;
    }

    public int getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(int bandwidth) {
        this.bandwidth = bandwidth;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }
}
