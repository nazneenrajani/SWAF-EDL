import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class NILClustering {
	static Map<String,String> nilmap;
	static String[] REOutputs;
	static String inputDir = "/Users/nrajani/Dropbox/workspace/ESFE_orig/src/main/resources/edl_2016/unsup";
	public static void main(String[] args) throws IOException{
		String type = "file";
		String year = "2015";
		if(type.equals("file")){
			if(year.equals("2015")){
				nilmap = new HashMap<String,String>();
				int count =0;
				String line;
				BufferedReader br = null;
				try {
					br = new BufferedReader (new FileReader("//scratch/cluster/nrajani/EDL/sup_english_hierarchy"));
				} catch (FileNotFoundException e) {
					System.exit (1);
				}			
				while((line=br.readLine())!=null){
					String[] prov = line.split("\t");
					String[] doc = prov[3].split(":");
					if(prov.length<7 || prov[4].startsWith("NIL")){
					//if(prov[4].startsWith("NIL")){
						String key = prov[2].toLowerCase().trim()+"~"+doc[0]+"~"+prov[5]+"~"+prov[6];
						//System.out.println(key);
						if(!nilmap.containsKey(key)){
							count++;
							String nilno = "NIL"+String.format("%04d", count);

							nilmap.put(key, nilno);
						}
					}
				}
				System.out.println(count);
				br.close();
				BufferedWriter bw = new BufferedWriter(new FileWriter("/scratch/cluster/nrajani/EDL/o_nil"));
				try {
					br = new BufferedReader (new FileReader("/scratch/cluster/nrajani/EDL/sup_english_hierarchy"));
				} catch (FileNotFoundException e) {
					System.exit (1);
				} 

				while((line=br.readLine())!=null){
					String[] prov = line.split("\t");
					String[] doc = prov[3].split(":");
					//if(prov[4].startsWith("NIL")){
					if(prov.length<7|| prov[4].startsWith("NIL")){
						String key = prov[2].toLowerCase().trim()+"~"+doc[0]+"~"+prov[5]+"~"+prov[6];
						if(nilmap.containsKey(key)){
							String nilno = nilmap.get(key);
							bw.write(prov[0]+"\t"+prov[1]+"\t"+prov[2]+"\t"+prov[3]+"\t"+nilno+"\t"+prov[5]+"\t"+prov[6]+"\t1.0"+"\n");
						}
						else
							System.err.println("Nil but not in map");
					}
					else{
						bw.write(line+"\n");
					}
				}
				bw.close();
				br.close();
			}
			else{
				nilmap = new HashMap<String,String>();
				int count =0;
				String line;
				BufferedReader br = null;
				try {
					br = new BufferedReader (new FileReader("/Users/nrajani/Dropbox/workspace/ESFE_orig/src/main/resources/edl_2014/sup/zjhu"));
				} catch (FileNotFoundException e) {
					System.exit (1);
				}			
				while((line=br.readLine())!=null){
					String[] prov = line.split("\t");
					String[] doc = prov[2].split(":");
					if(prov[3].startsWith("NIL")){
						String key = prov[1].toLowerCase().trim()+"~"+doc[0]+"~"+prov[4];
						if(!nilmap.containsKey(key)){
							count++;
							String nilno = "NIL"+String.format("%04d", count);

							nilmap.put(key, nilno);
						}
					}
				}
				System.out.println(count);
				br.close();
				BufferedWriter bw = new BufferedWriter(new FileWriter("/Users/nrajani/Dropbox/workspace/ESFE_orig/src/main/resources/edl_2014/sup/zjhu_nil"));
				try {
					br = new BufferedReader (new FileReader("/Users/nrajani/Dropbox/workspace/ESFE_orig/src/main/resources/edl_2014/sup/zjhu"));
				} catch (FileNotFoundException e) {
					System.exit (1);
				} 

				while((line=br.readLine())!=null){
					String[] prov = line.split("\t");
					String[] doc = prov[2].split(":");
					if(prov[3].startsWith("NIL")){
						String key = prov[1].toLowerCase().trim()+"~"+doc[0]+"~"+prov[4];
						if(nilmap.containsKey(key)){
							if(prov.length<6)
								continue;
							String nilno = nilmap.get(key);
							bw.write(prov[0]+"\t"+prov[1]+"\t"+prov[2]+"\t"+nilno+"\t"+prov[4]+"\t"+prov[5]+"\n");;//+"\t"+prov[7]+"\n");
						}
						else
							System.err.println("Nil but not in map");
					}
					else{
						bw.write(line+"\n");
					}
				}
				bw.close();
				br.close();
			}
		}
		else{
			if(year.equals("2015")){
				REOutputs = new String[7];
				getFiles(inputDir);
				nilmap = new HashMap<String,String>();
				int count =0;
				String line;
				BufferedReader br = null;
				for(int i=0; i<REOutputs.length;i++){
					try {
						br = new BufferedReader (new FileReader(REOutputs[i]));
					} catch (FileNotFoundException e) {
						System.exit (1);
					}			
					while((line=br.readLine())!=null){
						String[] prov = line.split("\t");
						String[] doc = prov[3].split(":");
						if(prov[4].startsWith("NIL")){
							String key = prov[2].toLowerCase().trim()+"~"+doc[0]+"~"+prov[5]+"~"+prov[6];
							if(!nilmap.containsKey(key)){
								String nilno = "NIL"+String.format("%06d", count);
								count++;
								nilmap.put(key, nilno);
							}
						}
					}
					System.out.println(count);
					br.close();
				}
				for(int i=0; i<REOutputs.length;i++){
					BufferedWriter bw = new BufferedWriter(new FileWriter(REOutputs[i]+"_nil"));
					try {
						br = new BufferedReader (new FileReader(REOutputs[i]));
					} catch (FileNotFoundException e) {
						System.exit (1);
					}
					while((line=br.readLine())!=null){
						String[] prov = line.split("\t");
						String[] doc = prov[3].split(":");
						if(prov[4].startsWith("NIL")){
							String key = prov[2].toLowerCase().trim()+"~"+doc[0]+"~"+prov[5]+"~"+prov[6];
							if(nilmap.containsKey(key)){
								String nilno = nilmap.get(key);
								bw.write(prov[0]+"\t"+prov[1]+"\t"+prov[2]+"\t"+prov[3]+"\t"+nilno+"\t"+prov[5]+"\t"+prov[6]+"\t"+prov[7]+"\n");
							}
							else
								System.err.println("Nil but not in map");
						}
						else{
							bw.write(line+"\n");
						}
					}
					bw.close();
					br.close();
				}
			}
			else{
				REOutputs = new String[12];
				getFiles(inputDir);
				nilmap = new HashMap<String,String>();
				int count =0;
				String line;
				BufferedReader br = null;
				for(int i=0; i<REOutputs.length;i++){
					try {
						br = new BufferedReader (new FileReader(REOutputs[i]));
					} catch (FileNotFoundException e) {
						System.exit (1);
					}			
					while((line=br.readLine())!=null){
						String[] prov = line.split("\t");
						String[] doc = prov[3].split(":");
						if(prov[4].startsWith("NIL")){
							String key = prov[2].toLowerCase().trim()+"~"+doc[0]+"~"+prov[5];
							if(!nilmap.containsKey(key)){
								String nilno = "NIL"+String.format("%04d", count);
								count++;
								nilmap.put(key, nilno);
							}
						}
					}
					System.out.println(count);
					br.close();
				}
				for(int i=0; i<REOutputs.length;i++){
					BufferedWriter bw = new BufferedWriter(new FileWriter(REOutputs[i]+"_nil"));
					try {
						br = new BufferedReader (new FileReader(REOutputs[i]));
					} catch (FileNotFoundException e) {
						System.exit (1);
					}
					while((line=br.readLine())!=null){
						String[] prov = line.split("\t");
						String[] doc = prov[3].split(":");
						if(prov[4].startsWith("NIL")){
							String key = prov[2].toLowerCase().trim()+"~"+doc[0]+"~"+prov[5];
							if(nilmap.containsKey(key)){
								String nilno = nilmap.get(key);
								bw.write(prov[0]+"\t"+prov[1]+"\t"+prov[2]+"\t"+prov[3]+"\t"+nilno+"\t"+prov[5]+"\t"+prov[6]+"\n");
							}
							else
								System.err.println("Nil but not in map");
						}
						else{
							bw.write(line+"\n");
						}
					}
					bw.close();
					br.close();
				}
			}
		}

	}
	public static void getFiles(String path){
		System.out.println(path);
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		int k=0;
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				System.out.println(listOfFiles[i].getName());
				REOutputs[k] = path+"/"+listOfFiles[i].getName();
				//System.out.println(REOutputs[k]);
				k++;
			}
		}
	}
}
