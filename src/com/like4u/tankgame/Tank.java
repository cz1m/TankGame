package com.like4u.tankgame;

import java.io.Serializable;
import java.util.Vector;

public class Tank implements Serializable {

    public boolean isAlive=true;
    private int x;
    private int y;

    public Vector< Tank> enemies=new Vector<>();
    private Integer preDirect=0;
    private int speed=5;
    private int direct;
    //上右下左移动
    public synchronized void moveUp(){
        y-=speed;
        if (y<0){y=0;}
    }
    public synchronized void moveRight(){
        x+=speed;
        if (x>Config.WIDTH){x=Config.WIDTH;}
    }
    public synchronized void moveDown(){
        y+=speed;
        if (y>Config.HEIGHT){y=Config.HEIGHT;}
    }
    public synchronized void moveLeft(){
        x-=speed;
        if (x<0){x=0;}
    }

    public boolean isTouchEnemy(int x, int y){

        switch (this.getPreDirect()){

            case 0://上
            case 2://下
                //当前坦克向上
                for (Tank enemy:enemies){
                    if (this==enemy||!enemy.isAlive) continue;
                    //敌人坦克向上或向下
                    if (enemy.getDirect()==0||enemy.getDirect()==2) {
                        if (x>enemy.getX()-40&&x<enemy.getX()+40
                                &&y>enemy.getY()-60&&y<enemy.getY()+60) return true;
                    }
                    //敌人坦克向右或向左
                    if (enemy.getDirect()==1||enemy.getDirect()==3){
                        if (x>enemy.getX()-40&&x<enemy.getX()+60
                                &&y> enemy.getY()-60&&y<enemy.getY()+40) return true;
                    }
                }

                break;
            case 1://右
            case 3://左
                //当前坦克向右
                for (Tank enemy:enemies){
                    if ((this==enemy||!enemy.isAlive)) continue;
                    //敌人坦克向上或向下
                    if (enemy.getDirect()==0||enemy.getDirect()==2){
                        if (x>enemy.getX()-60&&x<enemy.getX()+40
                                &&y> enemy.getY()-40&&y<enemy.getY()+100) return true;
                    }
                    //敌人坦克向左或向右
                    if (enemy.getDirect()==1||enemy.getDirect()==3){
                        if (x>enemy.getX()-60&&x<enemy.getX()+60
                                &&y> enemy.getY()-40&&y<enemy.getY()+40) return true;
                    }

                }

                break;

        }
        return false;
    }

    public void setEnemies(Vector<Tank> enemies) {
        this.enemies = enemies;
    }
    public Integer getPreDirect() {
        return preDirect;
    }

    public void setPreDirect(Integer preDirect) {
        this.preDirect = preDirect;
    }
    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getDirect() {
        return direct;
    }

    public void setDirect(int direct) {
        this.direct = direct;
    }

    public Tank(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Tank{" +
                "isAlive=" + isAlive +
                ", x=" + x +
                ", y=" + y +
                ", speed=" + speed +
                ", direct=" + direct +
                '}';
    }
}
