package org.games;

public class Roulette {

    public Roulette() {
        System.out.println("Starting roulette");
        new Thread(this.new Wheel()).start();
    }

    public static void main(String [] args){
         new Roulette();
    }

    public class Wheel implements Runnable {

        @lombok.SneakyThrows
        public void run() {
            while (true) {
                Thread.sleep(1000);
                System.out.println("test ...");
            }
        }
    }

}
