
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.Native;


public class Selector implements ClipboardOwner {
	
	static String before;
	
    public interface CustomUser32 extends StdCallLibrary {
        CustomUser32 INSTANCE = (CustomUser32) Native.loadLibrary("user32", CustomUser32.class);
        HWND GetForegroundWindow();
        void keybd_event(byte bVk, byte bScan, int dwFlags, int dwExtraInfo);
    }

    public void lostOwnership(Clipboard clipboard, Transferable contents) {
    	  //Required by ClipboardOwner
    }

    void controlC(CustomUser32 customUser32) {
        customUser32.keybd_event((byte) 0x11 /* VK_CONTROL*/, (byte) 0, 0, 0);
        customUser32.keybd_event((byte) 0x43 /* 'C' */, (byte) 0, 0, 0);
        customUser32.keybd_event((byte) 0x43 /* 'C' */, (byte) 0, 2 /* KEYEVENTF_KEYUP */, 0);
        customUser32.keybd_event((byte) 0x11 /* VK_CONTROL*/, (byte) 0, 2 /* KEYEVENTF_KEYUP */, 0);// 'Left Control Up
    }

    String getClipboardText() {
    	try{
        return (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
    	}
    	catch(IOException e)
    	{
    		return "Error";
    	}
    	catch(UnsupportedFlavorException e)
    	{
    		return "Error";
    	}
    }

    void setClipboardText(String data) throws Exception {
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(data), this);
    }

    String getSelectedText(User32 user32, CustomUser32 customUser32) throws Exception {
    	
    	//Get handler 
        HWND hwnd = customUser32.GetForegroundWindow();
        char[] windowText = new char[512];
        user32.GetWindowText(hwnd, windowText, 512);
        
        //Display window title 
        String windowTitle = Native.toString(windowText);
        System.out.println("Window: [" + windowTitle + "]");
        
        before = getClipboardText();
        
        //Emulate Ctrl+C and catch content of clipboard 
        controlC(customUser32);
        Thread.sleep(100); 
        String text = getClipboardText();  
        // Restore what was previously in the clipboard
        //setClipboardText(before);
        
        return text;
    }

    public static String go(Selector foo) throws Exception {
    	
      return foo.getSelectedText(User32.INSTANCE, CustomUser32.INSTANCE);
    }
}