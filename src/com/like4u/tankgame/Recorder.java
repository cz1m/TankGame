package com.like4u.tankgame;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Vector;

/**
 * @author Zhang Min
 * @version 1.0
 * @date 2023/7/9 9:53
 */
public class Recorder {

    private static int destroyEnemyNum=0;
    private static Vector<Tank> tanks=null;

    private static BufferedWriter bw=null;

    private static String recordFile="src\\com\\like4u\\gameRecord";
    private static Hero hero;

    public static String getRecordFile() {
        return recordFile;
    }

    public static int getDestroyEnemyNum() {
        return destroyEnemyNum;
    }

    public static void setDestroyEnemyNum(int destroyEnemyNum) {
        Recorder.destroyEnemyNum = destroyEnemyNum;
    }

    public static Vector<Tank> getTanks() {
        return tanks;
    }

    public static void setTanks(Vector<Tank> tanks) {
        Recorder.tanks = tanks;
    }

    public static Hero getHero() {
        return hero;
    }

    public static void addDestroyNum(){
        Recorder.destroyEnemyNum++;
    }
    public static void recordGame(){

        try {
            ObjectOutputStream op = new ObjectOutputStream(
                    new BufferedOutputStream(
                            Files.newOutputStream(Paths.get(recordFile))));
            op.writeObject(tanks);
            op.writeInt(destroyEnemyNum);
            op.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public static void restoreGame() throws IOException, ClassNotFoundException {
        File file = new File(recordFile);
        if (!file.exists()){

        }
        ObjectInputStream objectInputStream = new ObjectInputStream(
                new BufferedInputStream(
                        Files.newInputStream(Paths.get(recordFile))));
        Vector<Tank> tanks = (Vector<Tank>) objectInputStream.readObject();
        for (Tank tank : tanks) {
            if (!tank.isAlive) continue;
            //先把玩家坦克单独拿出来，剩下的就是敌人坦克
            if (tank instanceof Hero) {
                hero = (Hero) tank;
            }
        }
        Recorder.tanks=tanks;

        int i = objectInputStream.readInt();
        System.out.println("击毁数目"+i);
        objectInputStream.close();
    }


}
