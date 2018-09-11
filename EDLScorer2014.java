
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EDLScorer2014 {
	Map<String, String> judgement = new HashMap<String, String> ();
	Map<String,String> mpOutput = new HashMap<String,String>();
	Map<String,Double> mpConfidence = new HashMap<String,Double>();
	Map<String,Integer> mpTarget = new HashMap<String,Integer>();
	Map<String,String> rel_prov = new HashMap<String,String>();
	Map<String,String> offset_rel = new HashMap<String,String>();
	Map<String,String> fil_rel_prov = new HashMap<String,String>();
	Map<String,String> fil_offset_rel = new HashMap<String,String>();
	public  void run (String[] args) throws IOException {
		String responseFile = args[0];
		String keyFile = args[1];
		BufferedReader keyReader = null;
		try {
			keyReader = new BufferedReader (new FileReader(keyFile));
		} catch (FileNotFoundException e) {
			System.out.println ("Unable to open judgement file " + keyFile);
			System.exit (1);
		}
		String line;
		while ((line = keyReader.readLine()) != null) {
			String[] fields = line.trim().split("\t", 5);
			String key;
			if(fields[3].startsWith("NIL"))
				key = fields[1].trim()+"~"+fields[2].trim(); //name~doc:offsets
			else
				key = fields[1].trim()+"~"+fields[2].trim()+"~"+fields[3].trim();//name~doc:offsets~clustID
			if(judgement.containsKey(key))
				System.err.println("Repeated instance");
			else
				judgement.put(key, "c");
		}
		keyReader.close();
		BufferedReader responseReader = null;
		try {
			responseReader = new BufferedReader (new FileReader(responseFile));
		} catch (FileNotFoundException e) {
			System.out.println ("Unable to open responses file " + responseFile);
			System.exit (1);
		}
		// String line;
		while ((line = responseReader.readLine()) != null) {
			String[] fields = line.trim().split("\t");
			if(fields.length<6)
				continue;
			String key;
			if(fields[1].contains("~") || fields[1].equals(""))
				continue;
			if(fields[3].startsWith("NIL"))
				key = fields[1].trim()+"~"+fields[2].trim();
			else
				key = fields[1].trim()+"~"+fields[2].trim()+"~"+fields[3].trim();

			double conf = Double.parseDouble(fields[5]);
			String output = fields[1].trim().toLowerCase()+"\t"+fields[2].trim()+"\t"+fields[3].trim()+"\t"+fields[4].trim();//+"~"+fields[6].trim();
			//System.err.println(key);
			mpConfidence.put(key, conf);
			mpOutput.put(key, output);
			if(judgement.containsKey(key))
				mpTarget.put(key, 0); // 0==correct
			else
				mpTarget.put(key, 1);
			String offset = fields[2].trim().split(":")[1];
			String rel_off = fields[2].trim().split(":")[0];
			offset_rel.put(key, offset);
			rel_prov.put(key, rel_off);
		}
	}
}
