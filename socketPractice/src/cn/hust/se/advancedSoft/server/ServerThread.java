package cn.hust.se.advancedSoft.server;

import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread {
    Socket socket=null;
    String filename;
    File selectedFile;

    public ServerThread(Socket socket){
        this.socket=socket;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public File getSelectedFile() {
        return selectedFile;
    }

    public void setSelectedFile(File selectedFile) {
        this.selectedFile = selectedFile;
    }

    public ServerThread(Socket socket, String filename, File selectedFile){
        this.socket=socket;
        this.filename=filename;
        this.selectedFile=selectedFile;
    }

    public void run(){
        if(socket!=null){
            DataOutputStream os=null;
            DataInputStream in=null;
            BufferedInputStream bis=null;
            if(filename!=null){
                try {
                    os=new DataOutputStream(socket.getOutputStream());
                    os.writeUTF(filename);
                } catch (IOException e) {
                    if(socket==null)
                        System.out.println("连接尚未建立");
                    e.printStackTrace();
                }
            }
            try {
                in = new DataInputStream(socket.getInputStream());
                bis=new BufferedInputStream(new FileInputStream(selectedFile.getAbsolutePath()));
                byte[] buf = new byte[1024];
                int length;
                while((length = bis.read(buf, 0, buf.length))!=-1)
                {
                    os.write(buf, 0, length);
                    os.flush();
                }
                in.close();
                buf.clone();
                os.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
