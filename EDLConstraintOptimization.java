import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class EDLConstraintOptimization {
	static Map<String,String> enttype;
	static Map<String,Map<String,Double>> slotfiller;
	 static Map<String,Map<String,String>> origslot;
	static Map<String,Set<String>> fills;
	static Map<String,Integer> sysrank_13;
	static Map<String,Integer> sysrank_14;
	static Map<String,Integer> sysrank_15;
	static List<String> singleValuedSlots;
	static List<String> listValuedSlots;
	static List<String> systems13;
	static List<String> systems14;
	static List<String> systems15;
	Map<String,Boolean> slotfills = new HashMap<String,Boolean>();

	public EDLConstraintOptimization(){
		enttype = new HashMap<String,String>();
		slotfiller = new HashMap<String,Map<String,Double>>();
		origslot = new HashMap<String,Map<String,String>>();
		Set<String> perSlots = new HashSet<String>();
		Set<String> orgSlots = new HashSet<String>();
		fills = new HashMap<String,Set<String>>();
		sysrank_13 = new HashMap<String,Integer>();
		sysrank_15 = new HashMap<String,Integer>();
		sysrank_13.put("SFV2013_01",1);
		sysrank_13.put("SFV2013_07",2);
		sysrank_13.put("SFV2013_05",3);
		sysrank_13.put("SFV2013_15",4);
		sysrank_13.put("SFV2013_08",5);
		sysrank_13.put("SFV2013_09",6);
		sysrank_13.put("SFV2013_03",7);
		sysrank_13.put("SFV2013_13",8);
		sysrank_13.put("SFV2013_16",9);
		sysrank_13.put("SFV2013_10",10);
		systems13 = Arrays.asList(
				"SFV2013_01","SFV2013_07","SFV2013_05","SFV2013_15","SFV2013_08","SFV2013_09","SFV2013_03","SFV2013_13","SFV2013_16","SFV2013_10");

		sysrank_14 = new HashMap<String,Integer>();
		sysrank_14.put("lcc",1);
		sysrank_14.put("RPI_BLENDER",2);
		sysrank_14.put("MSIIPL",3);
		sysrank_14.put("NYU",4);
		sysrank_14.put("Tohoku",5);
		sysrank_14.put("ICTCAS",6);
		sysrank_14.put("HITS",7);
		sysrank_14.put("UBC",8);
		sysrank_14.put("vera",9);
		sysrank_14.put("BUPTTeam",10);
		sysrank_14.put("CSFG",11);
                sysrank_14.put("UI_CCG",12);
                sysrank_14.put("IBM",13);
                sysrank_14.put("OSU",14);
		sysrank_14.put("BUPT_PRIS",15);
                sysrank_14.put("lkd",16);
                sysrank_14.put("CohenCMU",17);
		systems14 = Arrays.asList(
				"SFV2014_01","SFV2014_18","SFV2014_02","lsv","SFV2014_08","SFV2014_09","SFV2014_03","SFV2014_11","SFV2014_16","SFV2014_06","SFV2014_04","SFV2014_05","SFV2014_07","SFV2014_10","SFV2014_13","SFV2014_14","SFV2014_15","SFV2014_17");
		sysrank_15.put("IBM",1); //stanford
		sysrank_15.put("BUPTTeam",2);//hlt
		sysrank_15.put("RPI",3);//saft
		sysrank_15.put("hltcoe",4);//umass
		sysrank_15.put("CMU",5);//bbn
		sysrank_15.put("OSU",6);//ufv
		sysrank_15.put("HITS",7);//nyu
		sysrank_15.put("ZJU",8);//dcd
		sysrank_15.put("UI_CCG",9);//uw
		sysrank_15.put("sdu",10); //stanford
}
	//String year, int nsys, String key, String file, String query)
	public static void main(String[] args) throws IOException {
		EDLConstraintOptimization co = new EDLConstraintOptimization();
		//		for(int n=0;n<1;n++){
		//			ConfidenceOptimization co = new ConfidenceOptimization();
		//			co.print(n);
		//			SurScore ss =new SurScore();
		//			String[] s = new String[4];
		//			s[0]="co_"+n;
		//			s[1]="/Users/nrajani/Documents/workspace/SFEnsemble/keys/key_file_2014";
		//			s[2]="anydoc";
		//			s[3]="nocase";
		//			//ss.run(s);
		//		}
		BufferedReader featureReader = null;
		try {
			featureReader = new BufferedReader (new FileReader(args[0]));
		} catch (FileNotFoundException e) {
			System.exit (1);
		}
		String line;
		while ((line = featureReader.readLine()) != null) {
			String[] prov = line.split("\t");
			if(!enttype.containsKey(prov[4]))
				enttype.put(prov[4],prov[5]);
			if(args[1].equals("2014")){
					if(!fills.containsKey(prov[4])){
						Set<String> tmp = new HashSet<String>();
						tmp.add(prov[2].toLowerCase().trim()+"~"+prov[3].trim());
						fills.put(prov[4],tmp);
					}
					else{
						Set<String> tmp = fills.get(prov[4]);
						tmp.add(prov[2].toLowerCase().trim()+"~"+prov[3].trim());
						fills.put(prov[4],tmp);
					}
					String uniq = prov[4]+"~"+prov[2].toLowerCase().trim()+"~"+prov[3].trim();
					if(!origslot.containsKey(uniq)){
                                                Map<String,String> sysconf = new HashMap<String,String>();
                                                        sysconf.put(prov[0], line);
                                                        origslot.put(uniq,sysconf);
                                        }
                                        else{
                                                Map<String,String> sysconf = origslot.get(uniq);
                                                sysconf.put(prov[0], line);
                                                        origslot.put(uniq,sysconf);
                                }

					if(!slotfiller.containsKey(uniq)){
                                                Map<String,Double> sysconf = new HashMap<String,Double>();
                                                        sysconf.put(prov[0], Double.parseDouble(prov[6]));
                                                        //System.out.println(prov[2].substring(0,10));
                                                        slotfiller.put(uniq,sysconf);
                                        }
                                        else{
                                                Map<String,Double> sysconf = slotfiller.get(uniq);
						sysconf.put(prov[0], Double.parseDouble(prov[6]));
                                                        slotfiller.put(uniq,sysconf);
                                }
					
			}
	
		else{
					if(!fills.containsKey(prov[4])){
                                                Set<String> tmp = new HashSet<String>();
                                                tmp.add(prov[2].toLowerCase().trim()+"~"+prov[3].trim());
                                                fills.put(prov[4],tmp);
                                        }
                                        else{
                                                Set<String> tmp = fills.get(prov[4]);
                                                tmp.add(prov[2].toLowerCase().trim()+"~"+prov[3].trim());
                                                fills.put(prov[4],tmp);
                                        }
                                        String uniq = prov[4]+"~"+prov[2].toLowerCase().trim()+"~"+prov[3].trim();
                                        if(!origslot.containsKey(uniq)){
                                                Map<String,String> sysconf = new HashMap<String,String>();
                                                        sysconf.put(prov[0], line);
                                                        origslot.put(uniq,sysconf);
                                        }
                                        else{
                                                Map<String,String> sysconf = origslot.get(uniq);
                                                sysconf.put(prov[0], line);
                                                        origslot.put(uniq,sysconf);
                                }
					if(!slotfiller.containsKey(uniq)){
                                                Map<String,Double> sysconf = new HashMap<String,Double>();
                                                        sysconf.put(prov[0], Double.parseDouble(prov[7]));
                                                        //System.out.println(prov[2].substring(0,10));
                                                        slotfiller.put(uniq,sysconf);
                                        }
                                        else{
                                                Map<String,Double> sysconf = slotfiller.get(uniq);
                                                        //System.out.println(prov[2].substring(0,10));
                                                        sysconf.put(prov[0], Double.parseDouble(prov[7]));
                                                        slotfiller.put(uniq,sysconf);
                                }

			}
		}
		featureReader.close();
//		for (int i=1;i<=100;i++)
//			co.optimize(i,args[1]);
		//	co.print(i);
		//co.optimize(i);
		//co.optimize_list(i);
		//co.optimize();
	co.write(args[1]);
		//co.print();
		//co.listvalue();
		//co.write_list();
		//co.classifier();
	}

	private void classifier() throws NumberFormatException, IOException {
		Map<String,Double> prec = new HashMap<String,Double>();
		BufferedReader bw = new BufferedReader(new FileReader("sconf15")); //2014_s4
		String line; 
		while((line=bw.readLine())!=null){
			String[] s = line.split("\t");
			String key = s[0]+"~"+s[1]+"~"+s[4].toLowerCase().trim();
			double value = Double.parseDouble(s[6]);
			prec.put(key, value);
		}
		bw.close();
		bw = new BufferedReader(new FileReader("lconf15")); //2014_s4
		while((line=bw.readLine())!=null){
			String[] s = line.split("\t");
			String key = s[0]+"~"+s[1]+"~"+s[4].toLowerCase().trim();
			double value = Double.parseDouble(s[6]);
			prec.put(key, value);
		}
		bw.close();
		BufferedReader b = new BufferedReader(new FileReader("jaccard_final.txt"));
		BufferedWriter br = new BufferedWriter(new FileWriter("newconf"));
		bw = new BufferedReader(new FileReader("jaccard_final.txt")); //2014_s4
		while((line=bw.readLine())!=null){
			String conf = b.readLine();
			String[] c = conf.split(",");
			String[] s = line.split("\t");
			String key = s[0]+"~"+s[1]+"~"+s[4].toLowerCase().trim();
			if(prec.containsKey(key)){
				int zero =0;
				for(int j=0;j<10;j++){
					if(Double.parseDouble(c[j])==0)
						zero++;
				}
				if(zero==9){
					for(int j=0;j<10;j++){
						if(Double.parseDouble(c[j])!=0 && prec.get(key)>0.2)
							c[j]=String.valueOf(prec.get(key));
					}
				}
			}
			for(int j=0;j<10;j++){
				if(j!=9)
					br.write(c[j]+",");
				else
					br.write(c[j]);
			}
			br.write("\n");
		}
		bw.close();
		br.close();
		b.close();
	}

	private void listvalue() throws IOException {

		BufferedReader bw = new BufferedReader(new FileReader("2015_eval"));
		String line; 
		Map<String,Double> me = new HashMap<String,Double>();
		String prefix ="SF13_ENG_";
		String oldkey="SF13_ENG_001:per:age";
		int correct =0, wrong =0;
		while((line=bw.readLine())!=null){
			String[] s = line.split("\t");
			if(s[0].equals(oldkey)){
				if(s[2].equals("C"))
					correct++;
				else
					wrong++;
			}
			else{
				me.put(s[0],correct/(double)(correct+wrong));
				oldkey=s[0];
				correct=0;
				wrong=0;
				if(s[2].equals("C"))
					correct++;
				else
					wrong++;
			}
		}
		bw.close();
		for(String k: listValuedSlots){
			double result =0.0;int count=0;
			if(k.startsWith("per")){
				for(int i=1;i<=50;i++){
					String queryID = prefix+String.format("%03d", i);
					if(me.containsKey(queryID+":"+k)){
						result+=me.get(queryID+":"+k);
						count++;
					}
				}
				System.out.println(k+"\t"+String.valueOf(result/count));
			}
			else{
				for(int i=51;i<=100;i++){
					String queryID = prefix+String.format("%03d", i);
					if(me.containsKey(queryID+":"+k)){
						result+=me.get(queryID+":"+k);
						count++;
					}
				}
				System.out.println(k+"\t"+String.valueOf(result/count));
			}
		}
	}

	private void print() throws NumberFormatException, IOException {
		BufferedWriter br = new BufferedWriter(new FileWriter("newconf"));
		Map<String,Double> prec = new HashMap<String,Double>();
		BufferedReader bw = new BufferedReader(new FileReader("2015_unsuper")); //2014_s4
		String line; 
		Set<String> sf = new HashSet<String>();
		while((line=bw.readLine())!=null){
			String[] s = line.split("\t");
			String key = s[0]+"~"+s[1]+"~"+s[4].toLowerCase().trim();
			String sys;
			if(s[2].startsWith("SFV"))
				sys = s[2].substring(0,13);
			else sys = s[2];
			double value = (1/(double)sysrank_15.get(sys))*Double.parseDouble(s[7]);
			prec.put(key, value);
		}
		bw.close();
		bw = new BufferedReader(new FileReader("sconf15")); //2014_s4
		while((line=bw.readLine())!=null){
			String[] s = line.split("\t");
			String key = s[0]+"~"+s[1];
			//if(prec.containsKey(key+"~"+s[4])&&(prec.get(key+"~"+s[4])<(0.95))){
			if(prec.containsKey(key+"~"+s[2])){
				if(!sf.contains(key)){
					sf.add(key);
					br.write(line);
					br.write("\n");
				}
				//System.out.println(line);

			}
		}
		bw.close();
		bw = new BufferedReader(new FileReader("lconf15")); //lvn
		while((line=bw.readLine())!=null){
			String[] s = line.split("\t");
			String key = s[0]+"~"+s[1];
			//if(prec.containsKey(key+"~"+s[4])&&(prec.get(key+"~"+s[4].toLowerCase().trim())<(0.95))){
			if(prec.containsKey(key+"~"+s[2])){
				br.write(line);
				br.write("\n");
			}
		}
		bw.close();/*
		bw = new BufferedReader(new FileReader("2015_unsuper")); 
		while((line=bw.readLine())!=null){
			String[] s = line.split("\t");
			String key = s[0]+"~"+s[1];
			//if(listValuedSlots.contains(s[1])){
			//System.out.println(key+"~"+s[4]);
			if(listValuedSlots.contains(s[1])&&!s[3].equals("NIL")&&prec.containsKey(key+"~"+s[4].toLowerCase().trim())&&(prec.get(key+"~"+s[4].toLowerCase().trim())>=(0.9))){ //0.18
				//System.out.println(line);
				br.write(line);
				br.write("\n");	
				//				if(slotfills.containsKey(key)){
				//					slotfills.put(key, true);
				//				}
			}
			else if(slotfills.containsKey(key) && slotfills.get(key)==false){
				slotfills.remove(key);
				slotfills.put(key,true);
				//System.out.println(line);
				br.write(line);
				br.write("\n");
			}
		}
		bw.close();*/
/*		for(String key : slotfills.keySet()){
			if(slotfills.get(key)==false){	
				String output_string = new String("");
				String[] data= key.split("~");
				output_string =  data [0] + "\t" + data [1] + "\t" + "optimized" + "\t" + "NIL";
				//System.out.println(output_string);
				br.write(output_string);
				br.write("\n");
			}
		}*/
		br.close();
	}

	public void optimize(int n,String year) throws IOException {
		System.out.println(n);
		String y="";
		if(year.equals("2015"))
			y = "16";
		else
			y="14";
		BufferedWriter br = new BufferedWriter(new FileWriter("os"+y+"_"+n+".m"));
		int x_counter=0;boolean flag,start=true;
		String a ="A=[";
		String x = "x0=[";
		String x1 = "x1=[";
		for(int j=0;j<n;j++){
			a = a+"1 ";
			x = x+"0;";
			x1 = x1+"1;";
		}
		a= a.trim()+"];";
		x=x.substring(0, x.length()-1)+"];";
		x1=x1.substring(0, x1.length()-1)+"];";
		br.write(a); br.write("\n");
		br.write(x); br.write("\n");	
		br.write(x1); br.write("\n");
		for(String key:slotfiller.keySet()){
			x_counter=0;flag=false;
			String[] fields = key.split("~");
			Set<String> tmp = fills.get(fields[0]);
			String type = enttype.get(fields[0]);
			if(tmp.size()==0){
				br.write("exit;");
				continue;
			}
			double b=0;
			if(year.equals("2015")){
                                        if(key.startsWith("NIL")){
                                                if(type.equals("PER"))
                                                        b=1.72;
                                                else if(type.equals("GPE"))
                                                        b = 1.42;
                                                else if(type.equals("ORG"))
                                                        b=2.16;
                                                else if(type.equals("FAC"))
                                                        b=1.53;
                                                else if(type.equals("LOC"))
                                                        b=1.38;
                                                else
                                                        b=1.0;
                                        }
                                        else{
                                                if(type.equals("PER"))
                                                        b=3.6;
                                                else if(type.equals("GPE"))
                                                        b = 2.73;
                                                else if(type.equals("ORG"))
                                                        b=3.1;
                                                else if(type.equals("FAC"))
                                                        b=2.92;
                                                else if(type.equals("LOC"))
                                                        b=2.59;
                                                else
                                                        b=1.0;
						 }
                                }
                                else{
                                        if(key.startsWith("NIL")){
                                                if(type.equals("PER"))
                                                        b=2.43;
                                                else if(type.equals("GPE"))
                                                        b =3.7;
                                                else if(type.equals("ORG"))
                                                        b=2.11;
                                        }
                                        else{
                                                if(type.equals("PER"))
                                                        b=3.5;
                                                else if(type.equals("GPE"))
                                                        b = 3.7;
                                                else if(type.equals("ORG"))
                                                        b=2.9;
                                        }
                                }


			for(String k:tmp){
				x_counter++;
				Map<String,Double> sysconf = slotfiller.get(fields[0]+"~"+k);
				for(String sys: sysconf.keySet()){
					double w =0;
					if(year.equals("2015"))
						w =   1.0/7.0; //for 2016  //1/(double)sysrank_15.get(sys);
					else
						w = 1/(double)sysrank_14.get(sys);
					//result+= w*(x-sysconf.get(sys))*(x-sysconf.get(sys));
					if(tmp.size()==n){
						if(flag){
							br.write("+"+w+"*(x("+x_counter+")-"+sysconf.get(sys)+")*(x("+x_counter+")-"+sysconf.get(sys)+")");
						}
						else{
							if(start){
								start = false;
								br.write("b="+b+";");br.write("\n");
								br.write("f = @(x) "+w+"*(x("+x_counter+")-"+sysconf.get(sys)+")*(x("+x_counter+")-"+sysconf.get(sys)+")");
							}
							else{
								br.write(";[x,fval] = fmincon(f,x0,A,b,[],[],x0,x1);");br.write("\n");
								br.write("dlmwrite('s"+y+"_"+n+"',x','-append');");br.write("\n");
								br.write("b="+b+";");br.write("\n");
								br.write("f = @(x) "+w+"*(x("+x_counter+")-"+sysconf.get(sys)+")*(x("+x_counter+")-"+sysconf.get(sys)+")");
							}
							flag = true;
						}
						//bw.write(fields[0]+"\t"+fields[1]+"\t"+k+"\t"+w+"\t"+sysconf.get(sys)+"\n");
						//System.out.println(fields[0]+"\t"+fields[1]+"\t"+k+"\t"+w+"\t"+sysconf.get(sys));
					}
				}
			}

		}
		br.write(";[x,fval] = fmincon(f,x0,A,b,[],[],x0,x1);");br.write("\n");
		br.write("dlmwrite('s"+y+"_"+n+"',x','-append');");br.write("\n");
		br.write("exit;");
		br.close();
	}
	public static void optimize_list(int n) throws IOException {
		BufferedWriter br = new BufferedWriter(new FileWriter("ol_"+n+".m"));
		 //BufferedWriter br = new BufferedWriter(new FileWriter("lout15_"+n));
		BufferedReader bw = new BufferedReader(new FileReader("list_value"));
		String line;Map<String,Double> list_map = new HashMap<String,Double>(); 
		while((line=bw.readLine())!=null){
			String[] s = line.split("\t");
			list_map.put(s[0],Double.parseDouble(s[1]));
		}
		bw.close();
		int x_counter=0;boolean flag,start=true;
				String a ="A=[";
				String x = "x0=[";
				String x1 = "x1=[";
				for(int j=0;j<n;j++){
					a = a+"1 ";
					x = x+"0;";
					x1 = x1+"1;";
				}
				a= a.trim()+"];";
				x=x.substring(0, x.length()-1)+"];";
				x1=x1.substring(0, x1.length()-1)+"];";
				br.write(a); br.write("\n");
				br.write(x); br.write("\n");
				br.write(x1); br.write("\n");
		for(String key:slotfiller.keySet()){
			x_counter=0;flag=false;
			String[] fields = key.split("~");
			Set<String> tmp = fills.get(fields[0]+"~"+fields[1]);
			if(tmp.isEmpty()){
				continue;
			}
			for(String k:tmp){
				x_counter++;
				Map<String,Double> sysconf = slotfiller.get(fields[0]+"~"+fields[1]+"~"+k);
				for(String sys: sysconf.keySet()){
					double w = 1;
					if(tmp.size()==n){
											if(flag){
													br.write("+"+w+"*(x("+x_counter+")-"+sysconf.get(sys)+")*(x("+x_counter+")-"+sysconf.get(sys)+")");
												}
												else{
													if(start){
														start = false;
														br.write("b="+tmp.size()*list_map.get(fields[1])+";");br.write("\n");
														br.write("f = @(x) "+w+"*(x("+x_counter+")-"+sysconf.get(sys)+")*(x("+x_counter+")-"+sysconf.get(sys)+")");
												}
											else{
													br.write(";[x,fval] = fmincon(f,x0,A,b,[],[],x0,x1);");br.write("\n");
														br.write("dlmwrite('l_"+n+"',x','-append');");br.write("\n");
													br.write("b="+tmp.size()*list_map.get(fields[1])+";");br.write("\n");
														br.write("f = @(x) "+w+"*(x("+x_counter+")-"+sysconf.get(sys)+")*(x("+x_counter+")-"+sysconf.get(sys)+")");
											}
													flag = true;
										}
		//				br.write(fields[0]+"\t"+fields[1]+"\t"+k+"\t"+sys+"\n");
						//if((sysconf.get(sys)*(1/(double)sysrank_14.get(sys)))>0.5) //0.5
						//System.out.println(fields[0]+"\t"+fields[1]+"\toptimized\t*\t"+k+"\t*\t1");
						//System.out.println(fields[0]+"\t"+fields[1]+"\t"+k+"\t"+w+"\t"+sysconf.get(sys));
					}
				}
			}

		}
				br.write(";[x,fval] = fmincon(f,x0,A,b,[],[],x0,x1);");br.write("\n");
				br.write("dlmwrite('l_"+n+"',x','-append');");br.write("\n");
				br.write("exit;");
		br.close();
	}
	public static void write(String year) throws IOException {
		for(int i=1;i<=28;i++){
		String y ="";
			if(year.equals("2015"))
                        y = "16";
                	else
                        y="14";
			File f = new File("s"+y+"_"+i);
                        if(f.exists()){
			System.out.println(i);
			BufferedWriter br = new BufferedWriter(new FileWriter("sconf"+y+"_"+i));
			BufferedReader bw = new BufferedReader(new FileReader(f));
			String line; List<String> maxi = new ArrayList<String>();Map<String,String> sysmap;
			while((line=bw.readLine())!=null){
				maxi.add(line);
			}
			bw.close();
			//System.out.println(maxi.size());
			List<String> temp; int counter=0;
			for(String key:slotfiller.keySet()){
				temp = new ArrayList<String>();
				sysmap = new HashMap<String,String>();
				String[] fields = key.split("~");
				Set<String> tmp = fills.get(fields[0]);
				if(tmp.size()==i){
					String l = maxi.get(0);
					String[] s = l.split(",");
					counter++;int n =0;
					for(String k:tmp){
						sysmap.put(fields[0]+"~"+k, s[n]);
						n++;
					}
					for(String k:tmp){
						Map<String,Double> sysconf = slotfiller.get(fields[0]+"~"+k);
						Map<String,String> slot = origslot.get(fields[0]+"~"+k);
						for(String sys: sysconf.keySet()){
							br.write(slot.get(sys)+"\t"+sysmap.get(fields[0]+"~"+k));
							br.write("\n");
							//System.out.println(fields[0]+"\t"+fields[1]+"\t"+k+"\t"+sys);
							//temp.add(fields[0]+"\t"+fields[1]+"\t"+k+"\t"+sys);
						}
					}
				}	
			}
			br.close();
			System.out.println(counter);
				}
			}
	}
	public static void write_list() throws IOException {
		for(int i=1;i<=200;i++){
			File f = new File("l_"+i);
			if(f.exists()){
				System.out.println(i);
				BufferedWriter br = new BufferedWriter(new FileWriter("lconf15_"+i));
				BufferedReader bw = new BufferedReader(new FileReader(f));
				String line; List<String> maxi = new ArrayList<String>();
				while((line=bw.readLine())!=null){
					maxi.add(line);
				}
				bw.close();
				List<String> temp; int counter=0;Map<String,String> sysmap;
				for(String key:slotfiller.keySet()){
					temp = new ArrayList<String>();
					sysmap = new HashMap<String,String>();
					String[] fields = key.split("~");
					Set<String> tmp = fills.get(fields[0]+"~"+fields[1]);
					if(tmp.size()==i){
						String l = maxi.get(counter);
						String[] s = l.split(",");
						counter++; int n =0;
						for(String k:tmp){
							sysmap.put(fields[0]+"~"+fields[1]+"~"+k, s[n]);
							n++;
						}
						for(String k:tmp){
							Map<String,Double> sysconf = slotfiller.get(fields[0]+"~"+fields[1]+"~"+k);
							for(String sys: sysconf.keySet()){
								br.write(fields[0]+"\t"+fields[1]+"\t"+k+"\t"+sys+"\t"+sysmap.get(fields[0]+"~"+fields[1]+"~"+k));
								br.write("\n");
								//System.out.println(fields[0]+"\t"+fields[1]+"\t"+k+"\t"+sys);
								//temp.add(fields[0]+"\t"+fields[1]+"\t"+k+"\t"+sys);
							}
						}
						//						for(int k =0;k<temp.size();k++){
						//							br.write(temp.get(k)+"\t"+s[k]);
						//							br.write("\n");
						//						}
					}	
				}
				br.close();
			}
		}
	}
}
