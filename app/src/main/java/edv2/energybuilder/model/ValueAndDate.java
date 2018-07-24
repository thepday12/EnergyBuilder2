package edv2.energybuilder.model;

public class ValueAndDate {
    private String date;
    private float value;
    private float compareValue = 0;

    public ValueAndDate(String date, float value, float compareValue) {
        this.date = date;
        this.value = value;
        this.compareValue = compareValue;
    }

    public ValueAndDate(String date, float value) {
        this.date = date;
        this.value = value;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public float getCompareValue() {
        return compareValue;
    }

    public void setCompareValue(float compareValue) {
        this.compareValue = compareValue;
    }
}
