import java.io.*;
import java.net.*;
import java.util.*;

public class Client
{
    private Scanner sc;
    private Socket s;
    private InputStream s1in;
    public DataInputStream dis;
    private OutputStream s1out;
    public DataOutputStream dos;

    Client(String address, int port )
    {
        try
        {
            this.sc=new Scanner(System.in);
            this.s=new Socket(address, port);
            System.out.println("Connected to the Server @ " + address);
            this.s1in=s.getInputStream();
            this.dis=new DataInputStream(s1in);
            this.s1out=s.getOutputStream();
            this.dos=new DataOutputStream(s1out);
        }
        catch (Exception e) 
        {
            System.out.println(e.getMessage());
        }
    }

    public void closeConnections() throws IOException
    {
        System.out.println("Closing Connection");
        dos.close();
        s1out.close();
        dis.close();
        s1in.close();
        s.close();
    }

    public String generateFileName(String r)
    {
        Date d = new Date();
        String f = "Received_" + r + "_" + d.getDate() + "" + d.getHours() + "" + d.getMinutes() + "" + d.getSeconds() + ".mp4";
        return f;
    }

    public void receiveFile(String f) throws Exception
    {
        String str = "";
        File file = new File(f);
        FileOutputStream fos = new FileOutputStream(file);
        byte buffer[] = new byte[4096];

        str = dis.readUTF();
        System.out.println(str);

        str = dis.readUTF();
        int filesize = Integer.parseInt(str);

        int read = 0;
        int totalRead = 0;
        int remaining = filesize;
                            
        while((read = dis.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) 
        {
            totalRead += read;
            remaining -= read;
            fos.write(buffer, 0, read);
        }
                            
        fos.close();
    }

    public void playVideo(String f) throws Exception
    {
        String command = "vlc " + f;
        Process p = Runtime.getRuntime().exec(command);
        p.waitFor();
    }

    
    public static void deleteFile(String f) throws Exception
    {
        File file=new File(f);
        file.deleteOnExit();
    }
    

	public static void main(String[] args) throws IOException, Exception
    {
        Client cl=new Client(args[0], 12345);

		String str = "", menu = "";
        menu = cl.dis.readUTF();
	    System.out.println(menu);

		str = cl.sc.next();
		cl.dos.writeUTF(str);

        if(str.equals("0"))
        {
            System.out.println("Client exits");
        }
        else
        {
            String name=cl.generateFileName(str);
            cl.receiveFile(name);
            cl.playVideo(name);
            cl.deleteFile(name);
        }                           
        
        cl.closeConnections();
	}	
}