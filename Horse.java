import java.util.*;
import java.util.concurrent.*;

public class Horse implements Runnable {
    private boolean isHealthy;
    private String warcry;
    private int speed;
    private int distanceLeft;
    private int gateDistance = 10;
    private String name;
    private Race race;
    
    Horse() {
        Random random = new Random();
        this.isHealthy = random.nextBoolean();
        this.speed = random.nextInt(10) + 1;
    }
    
    @Override
    public void run() {
        race.addSpeed(speed);
        
        try {
            // Wait for everyone at the barn
            race.barnGate.await();  
            
            // Go to gate and wait for everyone
            while(gateDistance > 0) {
                gateDistance -= speed;
                if(gateDistance > 0) {
                    System.out.println(System.currentTimeMillis() + " : " + name + " is walking to gate at " + speed + "kph. " + gateDistance + "km left...");
                }
            }
            System.out.println(System.currentTimeMillis() + " : " + name + " reaches gate...");

            race.gate.await();
            // Start running
            while(distanceLeft > 0) {
                distanceLeft -= speed;
                if(distanceLeft <= speed) {
                    break;
                }
                System.out.println(System.currentTimeMillis() + " : Horse " + name + " is running at " + speed + "kph, " + distanceLeft + "km left!" );
                
                // Check speed and boost if slowest
                speed = race.boost(name, speed);
            }

            // display warcry on finish, add on leaderboard
            System.out.println(System.currentTimeMillis() + " : " + warcry.toUpperCase() + "!!!");
            race.addOnBoard(name);
            
            race.finishLine.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
    
    public boolean isHealthy() {
        return this.isHealthy;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public void setWarcry(String warcry) {
        this.warcry = warcry;
    }
    
    public void setRace(Race race) {
        this.race = race;
        this.distanceLeft = race.getTrackLength();
    }
}