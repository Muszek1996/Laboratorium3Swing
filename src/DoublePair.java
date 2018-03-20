public class DoublePair {

    public String getKey() {
        return key;
    }

    public String key;

    public String getValue() {
        return value;
    }

    public String value;
    public DoublePair(String keyLocal, String valueLocal){
        key=keyLocal;
        value= valueLocal;
    }

    public String toString() {
        return getKey();
    }
}
