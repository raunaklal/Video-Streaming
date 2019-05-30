import java.io.*;
import java.net.*;
import java.util.*;

//To save the list of files we run the following command in the cmd
//dir|findstr ".mp4"|gawk "{print $4}" > menu.txt

class ClientHandler implements Runnable
{
	//Socket s;
	private Socket s;
    private InputStream s1in;
    public DataInputStream dis;
    private OutputStream s1out;
    public DataOutputStream dos;
    public String videoMenu[];
    public static final int size = 100;

	ClientHandler(Socket s)
	{
		try
		{
			this.s = s;
			this.s1in=s.getInputStream();
            this.dis=new DataInputStream(s1in);
            this.s1out=s.getOutputStream();
            this.dos=new DataOutputStream(s1out);
            new Thread(this).start();
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}

	public void run()
	{
		try
		{
			this.work();
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}

	void work() throws IOException, Exception
	{
		String menu = generateMenu();
        dos.writeUTF(menu);
                      
		String str = new String(dis.readUTF());
        int choice = Integer.parseInt(str);

        if(choice == 0)
        {
        	System.out.println("Client wants to Exit");
        }
        else
        {
        	System.out.println("Client has chosen " + videoMenu[choice-1] );
            dos.writeUTF(videoMenu[choice-1]+" is being played");
            sendFile(videoMenu[choice-1]);
        }

        dos.flush();
        closeConnections();
	}
    
    public String generateMenu() throws Exception
    {
    	videoMenu = new String[size]; 
    	String temp;
    	int count = 1;
		Scanner scr = new Scanner(new File("menu.txt"));
		String str = "";
		while(scr.hasNextLine())
		{
			temp = scr.nextLine();
			str += "Enter " + count + " for "+ temp + "\n";
			videoMenu[count-1] = temp;
			count++;
		}
		str += "Enter 0 to Exit \n";
		return str;
    }
	void sendFile(String f) throws Exception
	{
		File file = new File(f);
        FileInputStream fis = new FileInputStream(file);
        dos.writeUTF(file.length()+"");
       	byte buffer[] = new byte[4096];
        while (fis.read(buffer) > 0)
        {
       		dos.write(buffer);
        }
        fis.close();
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

}

public class Server 
{
	public static void main(String[] args) throws IOException
	{
		ServerSocket ss = null;
		Socket s;

		try
		{
			ss = new ServerSocket(12345);
            System.out.println("Server is Up");
			while(true)
			{
				s = ss.accept();
				System.out.println("Client " + s.getInetAddress() + " is Connected");
				ClientHandler obj = new ClientHandler(s);
			}
		}
        catch(Exception e)
        {
            ss.close();
            System.out.println(e.getMessage());
		}
	}
}