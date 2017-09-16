package iSMS;
import java.util.TimerTask;
public class CountDown extends TimerTask
{
    Splash s;
    public  void run(){s.dismiss();}
}
