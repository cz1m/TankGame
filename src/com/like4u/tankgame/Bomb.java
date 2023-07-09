package com.like4u.tankgame;

public class Bomb {
    int x,y;
    int life=60;
    boolean isAlive=true;

    public Bomb(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void lifeDown(){
        if (life>0){
            life--;
        }else isAlive=false;

    }
}
