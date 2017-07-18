import java.util.*;
import java.util.concurrent.*;

public class BidyoKarera {    
    final static int MIN_HORSE = 3;
    
    public static void main(String[] args) {
        BidyoKarera bidyoKarera = new BidyoKarera();
        Scanner scanner = new Scanner(System.in);
        List<Horse> horses = new ArrayList<Horse>();
        int healthyHorses = 0;
        int horseCount;
        int trackLength;
        CyclicBarrier gate;
        CyclicBarrier barnGate;
        
        System.out.println("\n========================");
        System.out.println("Welcome to Bidyo Karera!");
        System.out.println("========================");
        
        while(healthyHorses < MIN_HORSE) {
            System.out.print("\nEnter no. of horses (minimum of 3) : ");
            horseCount = bidyoKarera.acceptValidInt(MIN_HORSE);
            
            System.out.println("\nDrafting horses...");

            // GENERATE HORSES
            for(int ctr = 0; ctr < horseCount; ctr++) {
                Horse horse = new Horse();
                if(horse.isHealthy()) {
                    horses.add(horse);
                    healthyHorses++; 
                }
            }
            
            if(healthyHorses < 3) {
                System.out.println("Less than three horses are healthy, draft horses again...");
                healthyHorses = 0;
                horses.clear();
            }
        }
        
        System.out.println(healthyHorses + " horses are healthy and ready to race.");
        ExecutorService executor = Executors.newFixedThreadPool(healthyHorses);
        
        // Ask Distance of track
        System.out.print("\nEnter length of track (minimum of 20 meters) : ");
        
        // Create executor
        trackLength = bidyoKarera.acceptValidInt(20);
        Race race = new Race(healthyHorses, trackLength);
        
        // Get and assign names, warcry of horses
        horses.forEach(horse -> {
            System.out.print("\n\tEnter horse name : ");
            horse.setName(scanner.nextLine());
            
            System.out.print("\tEnter horse warcry : ");
            horse.setWarcry(scanner.nextLine());
        
            // RUN all threads
            horse.setRace(race);
            executor.execute(horse);
            //new Thread(horse, horse.getName()).start();
        });
        
        executor.shutdown();
        
    }    
    
    private int acceptValidInt(int min) {
		Scanner scanner = new Scanner(System.in);
		boolean validInput = false;
		int input = 0;
		
		while(!validInput) {
			try{
				input = scanner.nextInt();
				
				if(input < min) {
					System.out.print("\nInvalid input, try again : ");
					validInput = false;
				} else {
					validInput = true;
				}
			}catch(InputMismatchException ime) {
				System.out.print("\nInvalid input, try again : ");
				scanner = new Scanner(System.in);
			}
		}
		
		return input;
	}
    
    private int acceptValidInt(int min, int max) {
		Scanner scanner = new Scanner(System.in);
		boolean validInput = false;
		int input = 0;
		
		while(!validInput) {
			try{
				input = scanner.nextInt();
				
				if(input < min || input > max) {
					System.out.print("\nInvalid input, try again : ");
					validInput = false;
				} else {
					validInput = true;
				}
			}catch(InputMismatchException ime) {
				System.out.print("\nInvalid input, try again : ");
				scanner = new Scanner(System.in);
			}
		}
		
		return input;
	}
}