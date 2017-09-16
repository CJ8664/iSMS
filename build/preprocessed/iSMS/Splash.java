package iSMS;
import java.util.Timer;
import javax.microedition.lcdui.*;;
public class Splash extends Canvas
{
    Image img;
    static boolean f=true;
    public Display d=null;
    private Displayable n=null;
    private Timer tim=new Timer();
    protected void showNotify(){
          tim.schedule(new CountDown(),3000);
        }
    public Splash(Display display,Displayable next){
     this.d=display;
     this.n=next;
     display.setCurrent(this);
    }
    protected void paint(Graphics g){
        if(f==true)
        {
        try {img=Image.createImage("iSMS/Splash.PNG");}
        catch (java.io.IOException e){}
        g.setColor(255, 255, 255);
        g.fillRect(0, 0, getWidth()+100, getHeight()+100);
        g.drawImage(img,getWidth()/2, getHeight()/2-15, Graphics.VCENTER| Graphics.HCENTER);
        g.setColor(0, 0, 0);
        g.drawString("Press any key.",getWidth()/4,300,Graphics.BASELINE|Graphics.HCENTER);
        f=false;
        }
        else{dismiss();}
    }
    protected void keyPressed(int keycode){dismiss();}
    protected void pointerPressed(int x,int y){dismiss();}
    public void dismiss(){
        tim.cancel();
        d.setCurrent(n);
    }
}
