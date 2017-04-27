package trieu.propertyguru.utils.httpclient;


import static trieu.propertyguru.utils.Utils.RANDOM;

/**
 * Created by Apple on 2/13/17.
 */

public class BadNetwork {
    static public final int DELAY = 2000;
    static public final int PERCENT_FAILURE = 0;
    private int percentFailure;
    private int delay;

    private BadNetwork(int percentFailure, int delay) {
        this.percentFailure = percentFailure;
        this.delay = delay;
    }

    static public BadNetwork create(int percentFailure, int delay){
        BadNetwork badNetwork = new BadNetwork(percentFailure, delay);
        return badNetwork;
    }

    public int getDelay() {
        return delay;
    }

    public boolean isFailure(){
        int i = RANDOM.nextInt(100) + 1;
        if(i <= percentFailure){
            return true;
        }
        return false;
    }
}
