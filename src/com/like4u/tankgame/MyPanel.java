package com.like4u.tankgame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.like4u.tankgame.Collision.*;

public class MyPanel extends JPanel implements KeyListener, Runnable {
    Hero hero;
    final Vector<Enemy> enemys = new Vector<>();
    Vector<Tank> tanks=new Vector<>();

    Image image = null;
    Image image2 = null;
    int enemyNum = Config.ENEMY_NUM;


    public MyPanel(String key) throws IOException, ClassNotFoundException {

        File file = new File(Recorder.getRecordFile());
        if (!file.exists()) {
            key = "n";
            System.out.println("完了完了，存档丢了，已为你开新的一局");
        }
        switch (key)
        {
            case "y":
                Recorder.restoreGame();//读取存档
                restoreGame();//从存档恢复数据
                break;
            case "n":
                innitGame(600,500,5);
                break;
            default:
                System.out.println("你刚刚输入了啥？？报警了");
        }
        //坦克位置初始化,创建坦克对象



        image = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb.gif"));
        image2 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb2.gif"));
        new AePlayWave(Config.bgmFile).start();
    }


    public void innitGame(int heroX,int heroY,int heroSpeed){
        hero = new Hero(heroX, heroY);
        hero.setSpeed(heroSpeed);
        //初始化敌人坦克，加入到enemys集合中
        for (int i = 0; i < enemyNum; i++) {
            Enemy enemy = new Enemy((i + 1) * 100, 100);
            enemy.setDirect((int) (Math.random() * 4));
            new Thread(enemy).start();

            enemys.add(enemy);
        }
        tanks.add(hero);
        tanks.addAll(enemys);
        //给每个enemy设置其他坦克信息
        for (Enemy enemy:enemys){
            enemy.setEnemies(tanks);
        }
        hero.setEnemies(tanks);
        //准备存盘对象集合
        Recorder.setTanks(tanks);

    }

    public void restoreGame(){
        if((Recorder.getHero())==null||!Recorder.getHero().isAlive){

            return;
        }
        hero=Recorder.getHero();
        tanks=Recorder.getTanks();
        for (Tank tank:tanks){
            if (tank instanceof Enemy) {
                Enemy enemy= (Enemy)tank;
                enemys.add(enemy);
                new Thread(enemy).start();

            }
        }
        hero.setEnemies(tanks);
        synchronized (enemys){
                /*
              上一局的坦克留下的子弹需要重开线程
              因为正常来说子弹只会在坦克开炮的时候才会生成线程
              在击中坦克或者飞出边界才会销毁线程，否则就会由线程控制子弹坐标而一直移动
              而上一局的主动关闭的程序导致所有线程都中断了
              这会导致子弹还能绘制出来，绘制只和生命周期有关，但子弹线程却没了，子弹不动了
              我们需要对他们全部开启线程。
              */
            Iterator<Enemy> iterator = enemys.iterator();
            while (iterator.hasNext()) {
                Enemy enemy = iterator.next();
                enemy.setEnemies(tanks);
                for (int i=enemy.bulletPoll.size()-1;i>=0;i--) {
                    new Thread(enemy.bulletPoll.get(i)).start();
                }//之所以 用这种奇怪的遍历方式是因为可能出现currentModifyException，因为要是正常遍历的话可能这个shot刚刚拿到就被删除了
                // ，会导致集合元素数量发生变化导致无法全部遍历而报异常，而倒叙遍历删除不必担心这个问题
            }}
        Recorder.setTanks(tanks);
    }

    public void  showInfo(Graphics g){

        //玩家成绩
        g.setColor(Color.black);
        Font font = new Font("楷书", Font.BOLD, 25);
        g.setFont(font);
        g.drawString("累计击毁地方坦克：",1020,60);
        drawTank(1020,90,g,0,1);
        g.setColor(Color.black);
        g.drawString(String.valueOf(Recorder.getDestroyEnemyNum()),1080,130);

    }

    public void  showEnd(Graphics g){

        g.setColor(Color.black);
        Font font = new Font("隶书", Font.BOLD, 100);
        g.setFont(font);
        g.drawString("游戏结束：",500,200);
        System.out.println("你死了");
        System.exit(0);
    }
    @Override
    public void paint(Graphics g) {

        super.paint(g);
        g.fillRect(0, 0, 1000, 750);
        showInfo(g);
        if (hero==null){
            showEnd(g);
        }
        //生成坦克
        if (hero.isAlive)
            drawTank(hero.getX(), hero.getY(), g, hero.getDirect(), Hero.TYPE);
        else showEnd(g);


        if (hero.bulletPool != null) {
            //绘制子弹
            Iterator<Shot> iterator = hero.bulletPool.bulletPool.iterator();
            while (iterator.hasNext()) {
                Shot shot = iterator.next();
                if (shot.isAlive) {
                    drawShot(shot, g);
                } else {
                    iterator.remove();
                }
            }
        }




        //画出敌人坦克
        Iterator<Enemy> iterator = enemys.iterator();
        while (iterator.hasNext()) {
            Enemy enemy = iterator.next();
            //如果还活着就绘制
            if (enemy.isAlive) {

                drawTank(enemy.getX(), enemy.getY(), g, enemy.getDirect(), Enemy.TYPE);
            } else {
                iterator.remove();
                Recorder.addDestroyNum();
            }
            //画出敌人子弹
            for (int j = 0; j < enemy.bulletPoll.size(); j++) {
                Shot shot = enemy.bulletPoll.get(j);
                if (shot.isAlive) {
                    g.draw3DRect(shot.x, shot.y, 5, 5, false);
                } else {
                    enemy.bulletPoll.remove(shot);
                }
            }
        }
        /**
         * 爆炸
         * */
        if (bombs != null) {

            for (int i = 0; i < bombs.size(); i++) {
                Bomb bomb = bombs.get(i);
                if (bomb.life==60) new AePlayWave(Config.boomFile).start();
                if (bomb.life > 30) {
                    g.drawImage(image, bomb.x, bomb.y, 60, 60, this);
                } else {
                    g.drawImage(image2, bomb.x, bomb.y, 60, 60, this);
                }
                bomb.lifeDown();
                if (!bomb.isAlive) bombs.remove(bomb);
            }
        }

    }

    /**
     * x y左上角坐标
     * g 画笔
     * direct 方向
     * type类型
     */
    public void drawTank(int x, int y, Graphics g, int direct, int type) {
        switch (type) {

            case 0://己方坦克
                g.setColor(Color.cyan);
                break;
            case 1:
                g.setColor(Color.yellow);
                break;

        }
        switch (direct) {
            //0 1 2 3 ：上 右 下 左
            case 0:
                g.fill3DRect(x, y, 10, 60, false);
                g.fill3DRect(x + 30, y, 10, 60, false);
                g.fill3DRect(x + 10, y + 10, 20, 40, false);
                g.fillOval(x + 10, y + 20, 20, 20);
                g.drawLine(x + 20, y, x + 20, y + 30);
                break;
            case 1:
                g.fill3DRect(x, y, 60, 10, false);
                g.fill3DRect(x, y + 30, 60, 10, false);
                g.fill3DRect(x + 10, y + 10, 40, 20, false);
                g.fillOval(x + 20, y + 10, 20, 20);
                g.drawLine(x + 30, y + 20, x + 60, y + 20);
                break;
            case 2:
                g.fill3DRect(x, y, 10, 60, false);
                g.fill3DRect(x + 30, y, 10, 60, false);
                g.fill3DRect(x + 10, y + 10, 20, 40, false);
                g.fillOval(x + 10, y + 20, 20, 20);
                g.drawLine(x + 20, y + 60, x + 20, y + 30);
                break;
            case 3:
                g.fill3DRect(x, y, 60, 10, false);
                g.fill3DRect(x, y + 30, 60, 10, false);
                g.fill3DRect(x + 10, y + 10, 40, 20, false);
                g.fillOval(x + 20, y + 10, 20, 20);
                g.drawLine(x + 30, y + 20, x, y + 20);
                break;
        }

    }

    public void drawShot(Shot shot, Graphics g) {

        g.fill3DRect(shot.x, shot.y, 5, 5, false);
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyChar() == 's') {
            hero.setPreDirect(2);
            if (!hero.isTouchEnemy(hero.getX(),hero.getY()+1)){
                hero.setDirect(2);
                hero.moveDown();
            }

        } else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyChar() == 'w') {
            hero.setPreDirect(0);
            if (!hero.isTouchEnemy(hero.getX(),hero.getY()-1)){
                hero.setDirect(0);
                hero.moveUp();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyChar() == 'a') {
            hero.setPreDirect(3);
            if (!hero.isTouchEnemy(hero.getX()-1,hero.getY())){
                hero.setDirect(3);
                hero.moveLeft();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyChar() == 'd') {
            hero.setPreDirect(1);
            if (!hero.isTouchEnemy(hero.getX()+1,hero.getY())){
                hero.setDirect(1);
                hero.moveRight();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_J) {
            //发射子弹有上限

            if (hero.bulletPool.bulletPool.size() <= Hero.bulletNum)
                hero.shotEnemy();
        }
        this.repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void run() {

        while (true) {
            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            //碰撞检测
            if (hero!=null) {
                hitEnemy(hero, enemys);
                hitHero(enemys, hero);
            }


            this.repaint();

        }
    }


}

