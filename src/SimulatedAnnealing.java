import java.util.Random;


public class SimulatedAnnealing implements Algorithm {
	private Input input;
	
	//Starting temperature
	private double startingTemperature = 800;
	private double temperatureMovement = (double)1/1000000;

	private int maxTime = 10000000;
	
	private double getTemperature(int time){
		if(time > maxTime) return 0.0;
		return startingTemperature * Math.exp((-1)*temperatureMovement*time);
	}
	
	private int getRandomInt(int[] alreadyFound){
		Random random = new Random();
		
		while(true){
			boolean ok = true;
			int randomChoice = random.nextInt(input.getSize())+1;

			for(int i = 0; i < alreadyFound.length; i++){
				if(alreadyFound[i] == -1) break;
				if(alreadyFound[i] == randomChoice) ok = false;
			}
			
			if(ok){
				return randomChoice;
			}
		}
	}
	
	private int[] randomState(){
		int[] retval = new int[this.input.getSize()];
		for(int i = 0; i < retval.length; i++) retval[i] = -1;
		
		for(int i = 0; i < retval.length; i++){
			retval[i] = getRandomInt(retval);
		}
		
		return retval;
	}
	
	private int getEnergy(int[] tour){
		int length = 0;
		int[][] data = this.input.getData();
		
		for(int i = 0; i < tour.length-1; i++){
			length += data[tour[i]-1][tour[i+1]-1];
		}

		length += data[tour[tour.length-1]-1][tour[0]-1];

		return length;
	}
	
	private int[] generateNeighbor(int[] tour){
		/*
		Random random = new Random();
		int[] retval = new int[tour.length];
		for(int i = 0; i < tour.length; i++) retval[i] = -1;
		
		int splitPos = random.nextInt(tour.length+1);
		
		for(int i = 0; i < retval.length; i++){
			if(i == splitPos){
				retval[i%tour.length] = tour[(i+1)%tour.length];
				retval[(i+1)%tour.length] = tour[i%tour.length];
			}
			if(retval[i] == -1){
				retval[i] = tour[i];
			}
		}
		
		return retval;
		*/
		
		int[] retval = new int[tour.length];
		System.arraycopy(tour, 0, retval, 0, tour.length);
		Random random = new Random();
		int a = random.nextInt(tour.length);
		int b = random.nextInt(tour.length);
		
		int swap = retval[a];
		retval[a] = retval[b];
		retval[b] = swap;
		
		return retval;
	}
	
	private double P(int c, int n, double temp){
		int de = c-n;
		if(de > 0) return 1.0;
		//double retval = Math.exp(de/temp);
		double retval = Math.pow(2, de/temp);
		return retval;
	}

	public SimulatedAnnealing(Input input){
		this.input = input;
	}
	
	public Output doAlgo(){
		int time = 0;

		int[] current = randomState();
		int currentEnergy = getEnergy(current);
		
		int[] bestState = new int[current.length];
		System.arraycopy(current, 0, bestState, 0, current.length);
		int bestEnergy = currentEnergy;
		
		while(true){
			int[] next;
			int nextEnergy;
			
			next = generateNeighbor(current);
			nextEnergy = getEnergy(next);
			
			if(nextEnergy < bestEnergy){
				bestState = new int[next.length];
				System.arraycopy(next, 0, bestState, 0, bestState.length);
				bestEnergy = nextEnergy;
			}
			
			double temp = getTemperature(time);

			if(temp == 0.0){
				return new Output(input.getName(), input.getSize(), bestEnergy, bestState);
			}
			
			if(P(currentEnergy, nextEnergy, temp) > (new Random()).nextDouble()){
				//System.out.println("Swapping " + currentEnergy + " -> " + nextEnergy);
				current = next;
				currentEnergy = nextEnergy;
			}
		
			time++;
		}
	}
}
