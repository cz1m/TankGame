package com.like4u.tankgame;


import java.io.Serializable;
import java.util.Vector;

public class Hero extends Tank implements Serializable {
    public static final int TYPE=0;
    public static int bulletNum=5;//子弹上限

    BulletPoll bulletPool=new BulletPoll();
    Shot shot=null;

    public Hero(int x, int y) {
        super(x, y);
    }





    public void shotEnemy(){
        switch (getDirect()){
            case 0:
                shot = new Shot(getX() + 20, getY(), 0);
                break;
            case 1:
                shot = new Shot(getX() + 60, getY() + 20, 1);
                break;
            case 2:
                shot = new Shot(getX()+20,getY()+60,2);
                break;
            case 3:
                shot = new Shot(getX(),getY()+20,3);
                break;
        }
        /**
         * 每次按下j键会自动执行此方法，可是每次都会遍历列表然后为每个对象创建线程
         * 如果这个对象还活着，我们又创建了一个对象，次数遍历列表就会重复为那个还活着的对象创建线程
         * 这就会造成线程的浪费和资源的争用
         * 那个对象的资源会比平时消耗的更快，在这里就会让子弹速度越来也快。
         * 为了避免这个问题，未来需要通过线程池进行优化
         * */
        new Thread(shot).start();
        bulletPool.add(shot);
    }}

