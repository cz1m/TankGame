package com.like4u.tankgame;



import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Scanner;


public class TankGame extends JFrame {
    MyPanel mp=null;
    static Scanner scanner= new Scanner(System.in);
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        TankGame tankGame = new TankGame();


    }
    public TankGame() throws IOException, ClassNotFoundException {

        System.out.println("是否继续上一局？y/n");
        String key = scanner.next();
        while (!key.equalsIgnoreCase("y") && !key.equalsIgnoreCase("n")) {
            System.out.println("请输入 y 或 n：");
            key = scanner.next().toLowerCase();
        }

        mp = new MyPanel(key);

        this.add(mp);//面板是游戏的绘图区域
        this.setSize(Config.WIDTH,Config.HEIGHT);
        this.addKeyListener(mp);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        new Thread(mp).start();
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                Recorder.recordGame();
                System.exit(0);
            }
        });
    }

}

