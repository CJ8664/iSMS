package iSMS;
import java.util.*;
import javax.microedition.midlet.*;
import javax.microedition.io.Connector;
import javax.microedition.lcdui.*;
import javax.wireless.messaging.*;
import java.io.IOException;
public class iSMS extends MIDlet implements ItemStateListener, CommandListener
{
  public static Display d=null;
  public  iSMS mid;
  public Form M;
  private Command st;
  private Command r;
  private Command e;
  private DateField df;
  private Date ct;
  private Timer tm;
  private SnoozeTimer ts;
  private boolean f = false;
  private TextField p;
  private TextField m;
  public Alert MA;
  public String add,txt;
  public iSMS(){
    MA=new Alert("Information",null,null,AlertType.ALARM);
    MA.setTimeout(5000);
    M = new Form("Set the Details:");
    p=new TextField("Phone Number","",256,TextField.PHONENUMBER);
    m=new TextField("Message","",160,TextField.ANY);
    ct = new Date();
    df = new DateField("Time and Date",DateField.DATE_TIME);
    df.setDate(ct);
    st = new Command("Start",Command.SCREEN, 1);
    r = new Command("Reset",Command.SCREEN, 1);
    e = new Command("Exit",Command.EXIT, 1);
    M.append(df);
    M.append(p);
    M.append(m);
    M.addCommand(st);
    M.addCommand(r);
    M.addCommand(e);
    M.setCommandListener(this);
    M.setItemStateListener(this);
  }
  public void startApp ()throws MIDletStateChangeException{
    if(d==null)
        {
            initMIDlet();
        }
  }
  private void initMIDlet(){
       d=Display.getDisplay(this);
        Splash splash = new Splash(d,M);
    }
  public void pauseApp(){ }
  public void destroyApp(boolean unconditional){ }
  public void itemStateChanged(Item item){
    if (item==df)
    {
      if (df.getDate().getTime() < ct.getTime())
        f=false;
      else
        f=true;
    }
  }
  public void commandAction(Command c, Displayable s){
    if (c==st)
    {
      promptAndSend();
      txt=m.getString();
      if (f==false)
      {
        Alert al = new Alert("Unable to set alarm","Please choose another date and time.",null,null);
        al.setTimeout(Alert.FOREVER);
        al.setType(AlertType.ERROR);
        d.setCurrent(al);
      }
      else
      {
        tm=new Timer();
        ts=new SnoozeTimer();
        long amount=df.getDate().getTime() - ct.getTime()-11000;
        tm.schedule(ts,amount);
        M.removeCommand(st);
        M.removeCommand(r);
        M.deleteAll();
        M.setTitle("Alarm Set");
        M.append("Minimize the application");
      }
    }
    else if (c==r)
    {
      df.setDate(ct = new Date());
    }
    else if (c==e)
    {
      destroyApp(false);
      notifyDestroyed();
    }
  }
  private void promptAndSend (){
        add = p.getString();
        if (!isValidPhoneNumber (add))
        {
            MA.setString ("Invalid phone number");
            d.setCurrent (MA);
            return;
        }
        String statusMessage="Proper Number and Alarm";
        MA.setString (statusMessage);
     }
  private static boolean isValidPhoneNumber (String number){
        char[] chars=number.toCharArray ();
        if (chars.length==0){return false;}
        int startPos = 0;
        if (chars[0]=='+'){startPos=1;}
        for (int i = startPos; i < chars.length; ++i)
        {
            if (!Character.isDigit (chars[i])) {return false;}
        }
        return true;
    }
  private class SnoozeTimer extends TimerTask implements Runnable{
    public final void run()
    {
    int colonIdx=add.substring(6).indexOf(":");
    if (colonIdx > 0)
    {
      add = add.substring(0,colonIdx + 6);
    }
    MessageConnection smsconn = null;
    String add="sms://"+p.getString();
    try
      {
      smsconn = (MessageConnection)Connector.open(add);
      TextMessage txtmessage =(TextMessage)smsconn.newMessage("text");
      txtmessage.setAddress(add);
      txtmessage.setPayloadText(txt);
      smsconn.send(txtmessage);
      }
    catch (Throwable t)
    {
      System.out.println("Send caught: ");
    }
    if (smsconn != null)
      try
      {
        smsconn.close();
      }
      catch (IOException ioe)
      {
        System.out.println("Closing connection caught: ");
      }
     AlertType.ERROR.playSound(d);
     MA.setString("Sending to "+ p.getString());
     d.setCurrent(MA);
     M.deleteAll();
     M.append("You can now Exit the application");
     M.setTitle("Sent");
     cancel();
    }
  }
}
