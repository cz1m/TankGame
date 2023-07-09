package com.like4u.tankgame;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Hashtable;
import java.util.Vector;

public class Enemy extends Tank implements Runnable, Serializable {
    public static final int TYPE=1;
    public static int bulletNum=2;



    Shot shot=null;
    Vector<Shot> bulletPoll =new Vector<>();
    int sleepTime =50;
    public Enemy(int x, int y) {
        super(x, y);
    }



    @Override
    public void run() {
        while (isAlive) {


            if (bulletPoll.size()<Enemy.bulletNum){
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
                new Thread(shot).start();
                bulletPoll.add(shot);
                try {
                    Thread.sleep((int)(Math.random()*500));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
            //坦克移动

            switch (getPreDirect()) {
                case 0:
                    for (int i=0;i<((int)(Math.random()*30+10));i++){
                        if (!isTouchEnemy(this.getX(),this.getY()-1)) {
                            this.setDirect(getPreDirect());
                            moveUp();
                        }
                        try {
                            Thread.sleep(sleepTime);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    break;
                case 1:
                    for (int i=0;i<((int)(Math.random()*30+15));i++){
                        if (!isTouchEnemy(this.getX()+1,this.getY())) {
                            this.setDirect(getPreDirect());
                            moveRight();
                        }
                        try {
                            Thread.sleep(sleepTime);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    break;
                case 2:
                    for (int i=0;i<((int)(Math.random()*30+15));i++){
                        if (!isTouchEnemy(this.getX(),this.getY()+1)) {
                            this.setDirect(getPreDirect());
                            moveDown();
                        }
                        try {
                            Thread.sleep(sleepTime);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    break;
                case 3:
                    for (int i=0;i<((int)(Math.random()*30+10));i++){
                        if (!isTouchEnemy(this.getX()-1,this.getY())) {
                            this.setDirect(getPreDirect());
                            moveLeft();
                        }
                        try {
                            Thread.sleep(sleepTime);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    break;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            setPreDirect((int) (Math.random() * 4));

        }
    }
}
