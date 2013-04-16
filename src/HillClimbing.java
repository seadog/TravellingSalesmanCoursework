import java.util.Random;
import java.util.concurrent.CountDownLatch;


public class HillClimbing implements Algorithm {
	private Input input;
	private static final int NUM_THREADS = 8;
	private static final int NUM_RUNS = NUM_THREADS * 5000;

	public HillClimbing(Input input){
		this.input = input;
	}

	public Output doAlgo() {
		Output best = null;

		for(int i = 0; i < NUM_RUNS/NUM_THREADS; i++){
			System.out.println("Run #"+i);
			Output bestOf = createThreadsAndRun();
			if(best == null){
				best = bestOf;
				continue;
			}
			if(best.getLength() > bestOf.getLength())
				best = bestOf;
		}

		return best;
	}
	
	private Output createThreadsAndRun(){
		Output[] outputs = new Output[NUM_THREADS];
		CountDownLatch cd = new CountDownLatch(NUM_THREADS);
		
		for(int i = 0; i < NUM_THREADS; i++){
			Thread x = new HillClimbRun(this.input, outputs, i, cd);
			x.start();
		}
		
		try {
			cd.await();
		} catch (InterruptedException e) {
			System.out.println("BAD!");
			System.exit(-1);
		}
		
		Output best = outputs[0];
		for(Output x : outputs){
			if(x.getLength() < best.getLength()){
				best = x;
			}
		}
		
		return best;
	}
	
	private class HillClimbRun extends Thread {
		private Output[] outputs;
		private int index;
		private CountDownLatch cd;
		private Input input;
		
		private static final int MAX_RETRIES = 3;
		
		public HillClimbRun(Input input, Output[] outputs, int index, CountDownLatch cd){
			this.outputs = outputs;
			this.index = index;
			this.cd = cd;
			this.input = input;
		}
		
		public void run(){
			int retries_left = MAX_RETRIES;
			int[] best = randomState();
			int i = 0;
			
			
			while(i < 1000){
				int[] test = generateNeighbor(best);
				if(getLength(test) < getLength(best)){
					best = test;
					i = 0;
				} else {
					i++;
				}
			}
			
			outputs[index] = new Output(input.getName(), input.getSize(), getLength(best), best);
			cd.countDown();
			return;
			
	
			/*
			while(true){
				int[][] tests = new int[numberNeighborsGenerate()][best.length];
				
				for(int j = 0; j < numberNeighborsGenerate(); j++){
					tests[j] = generateNeighbor(best);
				}
		
				int[] best_new = getBestImprovement(tests, best);
				if(best_new == null){
					if(retries_left == 1){
						outputs[index] = new Output(input.getName(), input.getSize(), getLength(best), best);
						cd.countDown();
						return;
					}
					retries_left--;
					continue;
				}
				best = best_new;
			}*/
		}
		
		private int[] getBestImprovement(int[][] runs, int[] best){
			int[] bestSoFar = best;
			int bestSoFarLen = getLength(best);
			boolean foundOne = false;
			
			for(int[] run : runs){
				if(getLength(run) < bestSoFarLen){
					bestSoFar = run;
					bestSoFarLen = getLength(run);
					foundOne = true;
				}
			}
			
			if(!foundOne) return null;
			return bestSoFar;
		}
		
		private int numberNeighborsGenerate(){
			//return this.input.getSize();
			//return this.input.getSize()/4;
			return 512;
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
		
		private int getLength(int[] tour){
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
			
			int splitPos = random.nextInt(tour.length);
			
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
	}
	

}
