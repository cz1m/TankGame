package com.like4u.tankgame;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BulletPoll implements Runnable, Serializable {
    List<Shot> bulletPool =new ArrayList<>();

    public BulletPoll() {
    }

    public void add(Shot shot){
        bulletPool.add(shot);
    }

    public void isAlive(Shot shot){
        switch (shot.direct){
            case 0:
                shot.y-= Shot.speed;
                break;
            case 1:
                shot.x+= Shot.speed;
                break;
            case 2:
                shot.y+= Shot.speed;
                break;
            case 3:
                shot.x-= Shot.speed;
                break;
        }
        System.out.println("x :1000 "+shot.x+" y:750"+shot.y+shot.getClass());
        if(!(shot.x>=0&&shot.x<=1000&&shot.y>=0&&shot.y<=750)){
            shot.isAlive=false;

        }
    }


    @Override
    public void run() {
        while (true){
            for (Shot shot:bulletPool){
                isAlive(shot);
                if (!shot.isAlive) bulletPool.remove(shot);
            }

        }
    }
}
