package cn.hust.se.advancedSoft.client;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

import javax.crypto.Cipher;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import static java.lang.System.in;

public class GreetingClient extends JFrame {
    Socket socket = null;
    int port = 6606;
    String filename;
    static int sum = 0;

    private JPanel panel;
    private JLabel label_1;
    private JTextField filepathText;
    private JLabel label_2;
    private JTextField recBytes_Text;
    private JLabel label_4;
    private JTextField serverIpText;
    private JButton estConn_btn;
    private JLabel label_statue;
    private JButton interRec_btn;

    public GreetingClient() {
        this.setTitle("文件传输客户端");
        this.setSize(500, 300);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        label_1 = new JLabel("接收的文件");
        label_1.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
        label_1.setBounds(10, 10, 300, 25);
        panel.add(label_1);

        filepathText = new JTextField(20);
        filepathText.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
        filepathText.setHorizontalAlignment(JTextField.RIGHT);
        filepathText.setBounds(10, 40, 350, 35);
        panel.add(filepathText);

        label_2 = new JLabel("已接受字节数");
        label_2.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
        label_2.setBounds(10, 85, 150, 25);
        panel.add(label_2);

        recBytes_Text = new JTextField(20);
        recBytes_Text.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
        recBytes_Text.setHorizontalAlignment(JTextField.RIGHT);
        recBytes_Text.setBounds(155, 80, 150, 35);
        panel.add(recBytes_Text);

        label_4 = new JLabel("文件服务器IP地址");
        label_4.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
        label_4.setBounds(10, 130, 150, 25);
        panel.add(label_4);

        serverIpText = new JTextField(20);
        serverIpText.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
        serverIpText.setHorizontalAlignment(JTextField.RIGHT);
        serverIpText.setBounds(170, 125, 150, 35);
        serverIpText.setText("192.168.220.1");
        panel.add(serverIpText);


        estConn_btn = new JButton("建立TCP连接");
        estConn_btn.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
        estConn_btn.setBounds(30, 200, 130, 30);
        panel.add(estConn_btn);
        estConn_btn.addActionListener(event -> {
            new Thread(
                    () -> {
                        try {
                            socket = new Socket(serverIpText.getText(), port);
                            if (socket != null) {
                                label_statue.setText("连接已建立...");
                                DataInputStream is = new DataInputStream(socket.getInputStream());
                                filename = is.readUTF();
                                filepathText.setText("D:\\test\\" + filename);
                                label_statue.setText("连接已建立...");
                                FileOutputStream os = new FileOutputStream(new File("D:\\test\\" + filename));
                                new Thread(
                                        () -> {
                                            int len = 0;
                                            int sum = 0;
                                            byte[] bytes = new byte[1024];
                                            try {
                                                while ((len = is.read(bytes, 0, bytes.length)) != -1) {
                                                    os.write(bytes, 0, len);
                                                    sum += len;
                                                    recBytes_Text.setText(String.valueOf(sum));
                                                }
                                                os.close();
                                                is.close();
                                                socket.close();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                ).start();

                                filepathText.setText("D:\\test\\" + filename);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
            ).start();
        });


        label_statue = new JLabel("");
        label_statue.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
        label_statue.setBounds(200, 205, 110, 25);
        label_statue.setText("");
        panel.add(label_statue);

        interRec_btn = new JButton("中断接受");
        interRec_btn.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
        interRec_btn.setBounds(350, 200, 100, 30);
        interRec_btn.addActionListener(event->{
            if(socket!=null){
                try {
                    socket.shutdownInput();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        panel.add(interRec_btn);

        this.add(panel);
        this.setVisible(true);
    }


    public static void main(String[] args) throws IOException {
        GreetingClient greetingClient = new GreetingClient();
    }
}


