
public class AISearch {
	public AISearch(){
		Input input = new Input("in/file535.txt");
		SimulatedAnnealing sim = new SimulatedAnnealing(input);
		Output x = sim.doAlgo();
		System.out.println(x);
		x.toFile();
	}
	
	public static void main(String[] args){
		new AISearch();
	}
}
