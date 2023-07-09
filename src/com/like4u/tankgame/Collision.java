package com.like4u.tankgame;

import java.util.*;

public class Collision {
    public static Vector<Bomb> bombs=new Vector<>();

    /**
     * 碰撞检测
     * 正常来说应该把这几个数组写成常量封装在Enemy类里面
     * 或者写在配置类里面，挖个坑以后出皮肤系统在说吧
     * */

    public static void hitEnemy(Hero hero, Vector<Enemy> enemies){

        for (Shot shot:hero.bulletPool.bulletPool){
            for (Enemy enemy:enemies) {
                collision(shot,enemy);
            }
        }
    }

    /**
     * 检测子弹和tank是否相撞,如果相撞会把子弹和坦克生命置为false，并原地生成炸弹
     * */
    public static void collision(Shot s, Tank tank) {
        switch (tank.getDirect()) {
            case 0:
            case 2:
                if (s.x > tank.getX() && s.x < tank.getX() + 40
                        && s.y > tank.getY() && s.y < tank.getY() + 60) {
                    s.isAlive = false;
                    tank.isAlive = false;
                    Bomb bomb = new Bomb(tank.getX(), tank.getY());
                    bombs.add(bomb);


                }
                break;
            case 1:
            case 3:
                if (s.x > tank.getX() && s.x < tank.getX() + 60
                        && s.y > tank.getY() && s.y < tank.getY() + 40) {
                    s.isAlive = false;
                    tank.isAlive = false;
                    Bomb bomb = new Bomb(tank.getX(), tank.getY());
                    bombs.add(bomb);
                }
                break;
        }

    }

    public static void hitHero(Vector<Enemy> enemies,Hero hero){
        for (Enemy enemy : enemies) {
            int size = enemy.bulletPoll.size();
            if (size == 0) break;
            for (int i = 0; i < size; i++) {
                Shot s = enemy.bulletPoll.get(i);
                collision(s, hero);
            }

        }


    }
}
