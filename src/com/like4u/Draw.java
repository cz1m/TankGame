package com.like4u;

import javax.swing.*;
import java.awt.*;

public class Draw extends JFrame {
    private MyPanel mp=null;
    public static void main(String[] args) {
        new Draw();
    }
    public Draw(){
        mp=new MyPanel();
        this.add(mp);
        this.setSize(400,300);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
}

class MyPanel extends JPanel{

    @Override
    public void paint(Graphics g) {
        super.paint(g);
       // g.drawOval(10,10,100,100);
        //g.drawLine(10,10,100,100);
      /*  g.setColor(Color.cyan);
        g.fillRect(10,10,100,100);*/
     /*   Image image = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/photo.png"));
        g.drawImage(image,10,10,175,221,this);*/
        g.setColor(Color.BLUE);
        g.setFont(new Font("隶书",Font.BOLD,50));
        g.drawString("草拟吗",100,100);
    }
}