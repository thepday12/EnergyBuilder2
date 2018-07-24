package edv2.energybuilder.model;

public class EventPhase {
    private String event = "";
    private String phase = "";
    private String name = "";

    public EventPhase(String event, String phase, String name) {
        this.event = event;
        this.phase = phase;
        this.name = name;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /***
     *
     * @return phase+"_"+event
     */
    public String getPhaseEvent() {
        return phase+"_"+event;
    }
}
