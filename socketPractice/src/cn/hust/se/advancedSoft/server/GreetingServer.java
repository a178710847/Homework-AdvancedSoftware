package cn.hust.se.advancedSoft.server;
//
//import java.net.*;
//import java.io.*;
//
//public class GreetingServer extends Thread {
//    private ServerSocket serverSocket;
//
//    public GreetingServer(int port) throws IOException
//    {
//        serverSocket = new ServerSocket(port);
//    }
//
//    public void run()
//    {
//        String filename="D:\\project\\codes.zip";
//        File file=new File(filename);
//        System.out.println("文件大小："+file.length()+"byte");
//        while(true)
//        {
//            try
//            {
//                System.out.println("等待远程连接，端口号为：" + serverSocket.getLocalPort() + "...");
//                Socket server = serverSocket.accept();
//                System.out.println("本机的地址："+server.getLocalAddress());
//                //读取文件，并且传给客户端
//                DataInputStream in = new DataInputStream(server.getInputStream());
//                DataOutputStream os=new DataOutputStream(server.getOutputStream());
//                BufferedInputStream bis=new BufferedInputStream(new FileInputStream(filename));
//                byte[] buf = new byte[1024];
//                int length;
//                while((length = bis.read(buf, 0, buf.length))!=-1)
//                {
//                    os.write(buf, 0, length);
//                    os.flush();
//                }
//                in.close();
//                buf.clone();
//                os.close();
//                server.close();
//            }catch(IOException e)
//            {
//                e.printStackTrace();
//                break;
//            }
//        }
//    }
//    public static void main(String [] args)
//    {
//        int port = 6606;
//        try
//        {
//            Thread t = new GreetingServer(port);
//            t.start();
//        }catch(IOException e)
//        {
//            e.printStackTrace();
//        }
//    }
//}


import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class GreetingServer extends JFrame {
    private ServerSocket server;
    private String filename;
    File selectedFile;
    File file;
    String ipAddress;
    Socket socket;

    private JPanel panel;
    private JLabel label_1;
    private JTextField filepathText;

    private JButton chooseFile_btn;
    private JFileChooser fileChooser;

    private JLabel label_2;
    private JTextField fileLengthText;
    private JLabel label_3;
    private JLabel label_4;
    private JTextField ipText;
    private JButton intercept_btn;
    private JLabel label_statue;
    private JButton startSend_btn;
    public GreetingServer() throws IOException {
        server=new ServerSocket(6606);


        this.setTitle("文件服务器传输程序");
        this.setSize(500, 300);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel();
        panel.setLayout(null);

        label_1 = new JLabel("发送的文件") ;
        label_1.setFont(new Font (Font.DIALOG, Font.BOLD, 18));
        label_1.setBounds(10,10,150,25);
        panel.add(label_1);

        filepathText = new JTextField(20);
        filepathText.setFont(new Font (Font.DIALOG, Font.BOLD, 18));
        filepathText.setHorizontalAlignment(JTextField.RIGHT);
        filepathText.setBounds(10,40,300,35);
        panel.add(filepathText);

        chooseFile_btn=new JButton("...");
        chooseFile_btn.setFont(new Font (Font.DIALOG, Font.BOLD, 15));
        chooseFile_btn.setBounds(310,40,100, 35);
        panel.add(chooseFile_btn);

        fileChooser = new JFileChooser();
        chooseFile_btn.addActionListener(event->{
            int i = fileChooser.showOpenDialog(getContentPane());

            if (i == JFileChooser.APPROVE_OPTION) {
                selectedFile = fileChooser.getSelectedFile();
                filepathText.setText(selectedFile.getAbsolutePath());
                file=new File(selectedFile.getPath());
                filename=selectedFile.getName();
                if(file!=null&&file.isFile()){
                    fileLengthText.setText(String.valueOf(file.length()));
                }
            }
        });


        label_2 = new JLabel("文件大小");
        label_2.setFont(new Font (Font.DIALOG, Font.BOLD, 18));
        label_2.setBounds(10,85,80,25);
        panel.add(label_2);

        fileLengthText = new JTextField(20);
        fileLengthText.setFont(new Font (Font.DIALOG, Font.BOLD, 18));
        fileLengthText.setHorizontalAlignment(JTextField.RIGHT);
        fileLengthText.setBounds(90,80,150,35);
        panel.add(fileLengthText);

        label_3 = new JLabel("BYTES");
        label_3.setFont(new Font (Font.DIALOG, Font.BOLD, 15));
        label_3.setBounds(250,85,60,25);
        panel.add(label_3);

        label_4 = new JLabel("本机IP");
        label_4.setFont(new Font (Font.DIALOG, Font.BOLD, 18));
        label_4.setBounds(10,130,80,25);
        panel.add(label_4);

        ipText = new JTextField(20);
        ipText.setFont(new Font (Font.DIALOG, Font.BOLD, 18));
        ipText.setHorizontalAlignment(JTextField.RIGHT);
        ipText.setBounds(90,125,150,35);
        ipText.setText(String.valueOf(InetAddress.getLocalHost().getHostAddress()));
        panel.add(ipText);

        intercept_btn=new JButton("开始侦听");
        intercept_btn.setFont(new Font (Font.DIALOG, Font.BOLD, 15));
        intercept_btn.setBounds(30,200,100, 30);
        panel.add(intercept_btn);
        intercept_btn.addActionListener(event->{
            label_statue.setText("等待连接");
            while(true){
                try {
                    socket=server.accept();
                    if(socket!=null){
                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
                label_statue.setText("连接已建立...");
        });

        label_statue = new JLabel("");
        label_statue.setFont(new Font (Font.DIALOG, Font.BOLD, 15));
        label_statue.setBounds(170,205,110,25);
        panel.add(label_statue);

        startSend_btn=new JButton("开始发送");
        startSend_btn.setFont(new Font (Font.DIALOG, Font.BOLD, 15));
        startSend_btn.setBounds(320,200,100, 30);
        panel.add(startSend_btn);
        startSend_btn.addActionListener(event->{
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
        });

        this.add(panel);
        this.setVisible(true);
    }

    public static void main(String[] arg) throws IOException {
        GreetingServer greetingServer = new GreetingServer();
    }
}


