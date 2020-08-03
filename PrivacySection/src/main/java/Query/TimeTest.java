package Query;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TimeTest {
    public static void main(String[] args) throws InterruptedException {
        long time1 = new Date().getTime();
        Thread.sleep(1000);
        System.out.println(" Q time " + (TimeUnit.MILLISECONDS.convert((new Date().getTime() - time1), TimeUnit.MILLISECONDS)));

    }


}
