import java.io.FileWriter;
import java.io.IOException;


public class Output {
	private String name;
	private int size;
	private int length;
	private int[] tour;
	
	public Output(String name, int size, int length, int[] tour){
		this.name = name;
		this.size = size;
		this.length = length;
		this.tour = tour;
	}
	
	public int getLength(){
		return this.length;
	}
	
	public void toFile(){
		String output = this.toString();
	
		try {
			FileWriter fw = new FileWriter("out/tour"+this.name+".txt");
			fw.write(output);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	public String toString(){
		StringBuilder output = new StringBuilder();

		output.append("NAME = ");
		output.append(this.name);
		output.append(",\n");
		
		output.append("TOURSIZE = ");
		output.append(this.size);
		output.append(",\n");
		
		output.append("LENGTH = ");
		output.append(this.length);
		output.append(",\n");
		
		for(int i = 0; i < tour.length; i++){
			output.append(tour[i]);
			if(i != tour.length-1){
				output.append(",");
			}
		}

		return output.toString();
	}
}
