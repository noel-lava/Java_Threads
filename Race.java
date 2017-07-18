import java.util.*;
import java.util.concurrent.*;
    
public class Race { 
	private String[] leaderboard;
	static int nextPlace = 0;
	private final int trackLength;
    public CyclicBarrier gate;
    public CyclicBarrier barnGate;
    public CyclicBarrier finishLine;
    private static volatile List<Integer> speeds;
    private Random random;
        
	Race(int horseCount, int trackLength) {        
        Runnable horseReady = () -> System.out.println("\nAll horses are ready..");
        barnGate = new CyclicBarrier(horseCount, horseReady);
        
        Runnable raceStart = () -> System.out.println("\n===== START !!! =====");
        gate = new CyclicBarrier(horseCount, raceStart);
        
        Runnable finish = () -> printResult();
        finishLine = new CyclicBarrier(horseCount, finish);
        
        speeds = new ArrayList<Integer>(horseCount);
        
        random = new Random();
        this.trackLength = trackLength;
		this.gate = gate;
        this.barnGate = barnGate;
        this.leaderboard = new String[horseCount];
	}
	
	public synchronized void addOnBoard(String name){
		if(nextPlace < leaderboard.length) {
			leaderboard[nextPlace] = nextPlace + 1 + ". " + name;
			nextPlace++;
		}
	}
    
    public synchronized int boost(String name, int speed) {
        int newSpeed = speed;
        if(speed == speeds.get(0)) {
            newSpeed = random.nextInt(20) + 1;
            speeds.set(0, newSpeed);
            Collections.sort(speeds);
            if(newSpeed < speed) {
                System.out.println("\n" + name + "'s speed changes FROM " + speed + " To " + newSpeed + "\n");
            } else {
                System.out.println("\nBOOST " + name + "'s speed FROM " + speed + "kph TO " + newSpeed + "kph.\n");
            }
            
        }
        
        return newSpeed;
    }
	
	public void printResult() {
		System.out.println("\nThe race has ended!");
		System.out.println("====================");
		for(int ctr = 1; ctr <= leaderboard.length; ctr++){
			System.out.println(leaderboard[ctr-1]);
		}
	}
    
    public int getTrackLength() {
        return trackLength;
    }
    
    public void addSpeed(int speed) {
        this.speeds.add(speed);
        Collections.sort(speeds);
    }
 }
