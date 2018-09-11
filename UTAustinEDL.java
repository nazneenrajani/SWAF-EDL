
public class UTAustinEDL {

	public static void main(String[] args) throws Exception {
		String inputDir1 = "/scratch/cluster/nrajani/EDL/sup/english"; //cs_2014
		String inputDir2 = "src/main/resources/edl_2015/all_lang/sup/"; //cs_2015 //2015_unsupervised
		Integer nsys = 6;//38
		String year1= "2015";
		String year2 = "2015";
		String key1 = "src/main/resources/edl_2014/2014_key";
		String key2 = "/scratch/cluster/nrajani/EDL/2015_key";
		//String query1="src/main/resources/q_2013";
		//String query2="src/main/resources/q_2014";
		String out_file1 = "/scratch/cluster/nrajani/EDL/both_out";
		String out_file2 = "src/main/resources/edl_2015/all_lang/zjhu";
		String feature_file1 = "/scratch/cluster/nrajani/EDL/both.arff";
		String feature_file2 = "src/main/resources/edl_2015/all_lang/zjhu.arff";
		
		EDLFeatureExtractor fe1 = new EDLFeatureExtractor(nsys);
		fe1.getFiles(inputDir1);
		for(int sys=0; sys<nsys;sys++){
			fe1.getFeatures(year1, nsys, key2, fe1.REOutputs[sys]);
		}
		for(int i=0;i<nsys;i++){
			String[] nargs=new String[2];
			nargs[0]=fe1.REOutputs[i];
			nargs[1]=key2;
			fe1.edlscorer_2015[i].run(nargs);	
		}
		fe1.getSlotsAndConfidences(year1);
		fe1.writeOutput(nsys,year1,feature_file1,out_file1);
	
	}
}
