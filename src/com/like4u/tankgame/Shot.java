package com.like4u.tankgame;

import java.io.Serializable;

public class Shot implements Runnable, Serializable {
    int x;
    int y;
    int direct;
    public final static int speed=4;
    int HZ=16;//60帧
    boolean isAlive=true;

    public Shot(int x, int y, int direct) {
        this.x = x;
        this.y = y;
        this.direct = direct;
    }

    @Override
    public void run() {
      while (isAlive){

          try {
              Thread.sleep(HZ);
          } catch (InterruptedException e) {
              throw new RuntimeException(e);
          }
          switch (direct){
              case 0:
                  y-=speed;
                  break;
              case 1:
                  x+=speed;
                  break;
              case 2:
                  y+=speed;
                  break;
              case 3:
                  x-=speed;
                  break;
          }

          if(!(x>=0&&x<=1000&&y>=0&&y<=750)){
              isAlive=false;
              break;
          }
      }
    }
}
