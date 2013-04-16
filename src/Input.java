import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Input {
	private String name;
	private int size;
	private int[][] data;
	
	public Input(String filename){
		String contents = parseFile(filename);
		String[] values = contents.split(",");
		
		this.name = values[0].substring(5);
		this.size = Integer.parseInt((values[1].substring(5)));
		
		this.data = extractData(values);
	}
	
	public String getName(){
		return this.name;
	}
	
	public int getSize(){
		return this.size;
	}
	
	public int[][] getData(){
		return this.data;
	}
	
	private void printInput(int padding){
		System.out.println(this.name);
		System.out.println(this.size);
		
		for(int[] data1 : this.data){
			for(int data2 : data1){
				System.out.printf("%"+padding+"d", data2);
			}
			System.out.println();
		}
	}
	
	private int[][] extractData(String[] values){
		int[][] retval = new int[this.size][this.size];
		for(int i = 0; i < this.size; i++){
			for(int j = 0; j < this.size; j++){
				retval[i][j] = -2;
			}
		}
		
		int index = 2;
		
		for(int i = 0; i < this.size; i++){
			for(int j = 0; j < this.size; j++){
				if(i == j){
					retval[i][j] = -1;
					continue;
				}
				
				if(retval[i][j] != -2){
					continue;
				}
				
				int value = Integer.parseInt(values[index]);
				
				retval[i][j] = value;
				retval[j][i] = value;
				
				index++;
			}
		}
		
		return retval;
	}

	private String parseFile(String filename){
		File file = new File(filename);

		if(!file.exists()){
			System.out.println("No such file");
			System.exit(-1);
		}
		
		if(!file.isFile()){
			System.out.println("Specified file isn't a file");
			System.exit(-1);
		}
		
		if(!file.canRead()){
			System.out.println("Cannot read file");
			System.exit(-1);
		}
		
		StringBuilder string = null;
		
		try {
			FileInputStream fis = new FileInputStream(file);
			string = new StringBuilder();
		
			char current;
			while(fis.available() > 0){
				current = (char)fis.read();
				if(Character.isLetterOrDigit(current) || current == ',' || current == '='){
					string.append(current);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		return string.toString();
	}
}
