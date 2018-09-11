
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class CorrectType {

	public static void main(String[] args) throws IOException {
		Set<String> t = new HashSet<String>();
		Set<String> n = new HashSet<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader("/scratch/cluster/nrajani/EDL/g"));
			String line;
			while ((line = br.readLine()) != null) {
				String[] parts = line.split("\t");
				if(!parts[3].startsWith("NIL"))
				t.add(line);
				else
					n.add(parts[0]+"~"+parts[1]+"~"+parts[2]);
			}
			br.close();
			br = new BufferedReader(new FileReader("/scratch/cluster/nrajani/EDL/tmp"));
			while ((line = br.readLine()) != null) {
				String[] parts = line.split("\t");
				if(!parts[3].startsWith("NIL")){
					if(t.contains(line))
						System.out.println("c");
					else
						System.out.println("w");
				}
				else{
					String tmp = parts[0]+"~"+parts[1]+"~"+parts[2];
					if(n.contains(tmp)){
						System.out.println("c");
						}else
						System.out.println("w");
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
