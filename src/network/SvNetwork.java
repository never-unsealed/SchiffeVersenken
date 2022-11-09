package network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//Class for socket and network I/O operations
public class SvNetwork
{
    private ServerSocket serverSocket = null;
    private Socket socket = null;
    private DataInputStream inputStream = null;
    private DataOutputStream outputStream = null;
    public String outWord = "";
    public SV_NETWORK_TYPE networkType;

    public SvNetwork(SV_NETWORK_TYPE type, String hostname, int port) throws Exception
    {
        this.networkType = type;

        switch (type)
        {
            case NETWORK_TYPE_SERVER:

                System.out.println("Server");
                this.serverSocket = new ServerSocket(port);
                this.socket = this.serverSocket.accept();

                break;

            case NETWORK_TYPE_CLIENT:

                if(hostname != null)
                {
                    this.socket = new Socket(hostname, port);
                }
                else
                {
                    throw new RuntimeException("No hostname.");
                }

                break;

            default:

                throw new RuntimeException("Invalid type.");
        }

        this.networkType = type;

        this.inputStream = new DataInputStream(
                this.socket.getInputStream()
        );

        this.outputStream = new DataOutputStream(
                this.socket.getOutputStream()
        );
    }

    public void sendWord(String word) throws IOException
    {
        this.outputStream.writeUTF(word);
    }

    public void receiveWord() throws IOException
    {
        this.outWord =  this.inputStream.readUTF();
        //Return valur reserved
    }

    //Close resources
    public void closeNetwork()
    {
        try
        {
            if(serverSocket != null)
                serverSocket.close();

            if(socket != null)
                socket.close();

            if(inputStream != null)
                inputStream.close();

            if(outputStream != null)
                outputStream.close();
        }
        catch(Exception e)
        {
        }
    }
}
