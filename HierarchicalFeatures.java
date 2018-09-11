import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
//"/scratch/cluster/nrajani/ESFE_orig/src/main/resources/edl_2016/eng_id
public class HierarchicalFeatures {

	public static void main(String[] args) throws IOException {
		Set<String> docs = new HashSet<String>();
		Set<String> id = new HashSet<String>();
		
		Map<String,String> doc_title = new HashMap<String,String>();
		//Map<String,String> doc_context = new HashMap<String,String>();
		Map<String,String> doc_content = new HashMap<String,String>();
		
		Map<String,String> kb_title = new HashMap<String,String>();
		Map<String,String> kb_content = new HashMap<String,String>();
		
		Map<String,double[]> w2v = new HashMap<String,double[]>();
		try {
			BufferedReader br = new BufferedReader(new FileReader("/scratch/cluster/nrajani/EDL/id_doc_mention"));
			String line;
			while ((line = br.readLine()) != null) {
				String[] parts = line.split("\t");
				docs.add(parts[1]);
				id.add(parts[0]);		
			}
			System.out.println(docs.size()+"\t"+id.size());
			br.close();
			br = new BufferedReader(new FileReader("/scratch/cluster/nrajani/IBM/embedding/2016_eng_corpus.tsv"));
			while ((line = br.readLine()) != null) {
				String[] parts = line.split("\t");
				//System.out.println(parts[0]);
				if(docs.contains(parts[0])){
					doc_title.put(parts[0], parts[1]);
					doc_content.put(parts[0], parts[2]);
				}
			}
			br.close();
			br = new BufferedReader(new FileReader("/scratch/cluster/nrajani/nlp-entity-convnet/other_projects/word2vec/vectors.txt"));
			while ((line = br.readLine()) != null) {
				String[] parts = line.split(" ");
				double[] vec = new double[100];
				for(int i=1;i<vec.length;i++)
					vec[i] = Double.parseDouble(parts[i]);
				w2v.put(parts[0], vec);
			}
			br.close();
			System.out.println("here");
			br = new BufferedReader(new FileReader("/scratch/cluster/nrajani/nlp-entity-convnet/tmp"));
			while ((line = br.readLine()) != null) {
				String[] parts = line.split("\t");
				String tmp = parts[0].substring(2);
				String topic = parts[1].replaceAll("_"," ");
				if(id.contains(tmp)){
					kb_title.put(tmp, topic);
					kb_content.put(tmp, parts[2]);
				}
			}
			br.close();
			BufferedWriter bw = new BufferedWriter(new FileWriter("/scratch/cluster/nrajani/EDL/hierachical_feature"));
			br = new BufferedReader(new FileReader("/scratch/cluster/nrajani/EDL/id_doc_mention"));
			while ((line = br.readLine()) != null) {
				String[] parts = line.split("\t");
				String kb_id = parts[0];
				String mention_doc = parts[1];
				String mention = parts[2].toLowerCase();
			        //System.out.println(kb_id);
				if(kb_id.startsWith("NIL") || !kb_title.containsKey(kb_id)){
                                        bw.write("0.0,0.0,0.0,0.0,0.0,0.0\n");
                                        continue;
                                }
				String kb_title_txt = kb_title.get(kb_id);
				String kb_content_txt = kb_content.get(kb_id);
				String doc_title_txt = doc_title.get(mention_doc);
				String doc_content_txt = doc_content.get(mention_doc);
				String doc_context_txt = "";
				if(doc_content_txt.contains(mention)){
					String[] words = doc_content_txt.split("\\s+");
					int ind = Arrays.asList(words).indexOf(mention);
					int min_ind = Math.max(0, ind-10);
					int max_ind = Math.min(words.length-1,ind+10);
					for(int i = min_ind;i<max_ind;i++){
						doc_context_txt+=words[i]+" ";
					}
				}
				else
					System.err.println("But this doesnt contian the mention!");
				double[] kb_title_vec = new double[100];
				double[] kb_content_vec = new double[100];
				
				double[] doc_title_vec = new double[100];
				double[] doc_content_vec = new double[100];
				double[] doc_context_vec = new double[100];
				//kb_title
				parts = kb_title_txt.split(" ");
				int count=0;
				for(int i =0;i<parts.length;i++){
					if(w2v.containsKey(parts[i])){
						if(count==0){
							kb_title_vec = w2v.get(parts[i]);
						}else{
							double[] tmp = w2v.get(parts[i]);
							for(int j = 0; j<kb_title_vec.length;j++){
								kb_title_vec[j]+=tmp[j];
							}
						}
						count++;
					}		
				}
				if(count>0){
					for(int k=0;k<100;k++)
					kb_title_vec[k] = kb_title_vec[k]/count;
				}
				else{
					for(int k=0;k<100;k++)
						kb_title_vec[k] = 0.0;
				}
				//kb_content
				parts = kb_content_txt.split(" ");
				count=0;
				for(int i =0;i<parts.length;i++){
					if(w2v.containsKey(parts[i])){
						if(count==0)
							kb_content_vec = w2v.get(parts[i]);
						else{
							double[] tmp = w2v.get(parts[i]);
							for(int j = 0; j<kb_content_vec.length;j++){
								kb_content_vec[j]+=tmp[j];
							}
						}
						count++;
					}		
				}
				if(count>0){
					for(int k=0;k<100;k++)
					kb_content_vec[k] = kb_content_vec[k]/count;
				}
				else{
					for(int k=0;k<100;k++)
						kb_content_vec[k] = 0.0;
				}
				//doc_title
				parts = doc_title_txt.split(" ");
				count=0;
				for(int i =0;i<parts.length;i++){
					if(w2v.containsKey(parts[i])){
						if(count==0)
							doc_title_vec = w2v.get(parts[i]);
						else{
							double[] tmp = w2v.get(parts[i]);
							for(int j = 0; j<doc_title_vec.length;j++){
								doc_title_vec[j]+=tmp[j];
							}
						}
						count++;
					}		
				}
				if(count>0){
					for(int k=0;k<100;k++)
					doc_title_vec[k] = doc_title_vec[k]/count;
				}
				else{
					for(int k=0;k<100;k++)
						doc_title_vec[k] = 0.0;
				}
				//doc_content
				parts = doc_content_txt.split(" ");
				count=0;
				for(int i =0;i<parts.length;i++){
					if(w2v.containsKey(parts[i])){
						if(count==0)
							doc_content_vec = w2v.get(parts[i]);
						else{
							double[] tmp = w2v.get(parts[i]);
							for(int j = 0; j<doc_content_vec.length;j++){
								doc_content_vec[j]+=tmp[j];
							}
						}
						count++;
					}		
				}
				if(count>0){
					for(int k=0;k<100;k++)
					doc_content_vec[k] = doc_content_vec[k]/count;
				}
				else{
					for(int k=0;k<100;k++)
						doc_content_vec[k] = 0.0;
				}
				//doc_context
				parts = doc_context_txt.split(" ");
				count=0;
				for(int i =0;i<parts.length;i++){
					if(w2v.containsKey(parts[i])){
						if(count==0)
							doc_context_vec = w2v.get(parts[i]);
						else{
							double[] tmp = w2v.get(parts[i]);
							for(int j = 0; j<doc_context_vec.length;j++){
								doc_context_vec[j]+=tmp[j];
							}
						}
						count++;
					}		
				}
				if(count>0){
					for(int k=0;k<100;k++)
					doc_context_vec[k] = doc_context_vec[k]/count;
				}
				else{
					for(int k=0;k<100;k++)
						doc_context_vec[k] = 0.0;
				}
				
				double title_title = cosineSimilarity(kb_title_vec, doc_title_vec);
				double title_context = cosineSimilarity(kb_title_vec, doc_context_vec);
				double title_content = cosineSimilarity(kb_title_vec, doc_content_vec);
				
				double content_title = cosineSimilarity(kb_content_vec, doc_title_vec);
				double content_context = cosineSimilarity(kb_content_vec, doc_context_vec);
				double content_content = cosineSimilarity(kb_content_vec, doc_content_vec);
				
				bw.write(title_title+","+title_context+","+title_content+","+content_title+","+content_context+","+content_content+"\n");
				
			}
			br.close();
			bw.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}
	public static double cosineSimilarity(double[] kb_title_vec, double[] doc_title_vec) {
	    double dotProduct = 0.0;
	    double normA = 0.0;
	    double normB = 0.0;
	    for (int i = 0; i < kb_title_vec.length; i++) {
	        dotProduct += kb_title_vec[i] * doc_title_vec[i];
	        normA += Math.pow(kb_title_vec[i], 2);
	        normB += Math.pow(doc_title_vec[i], 2);
	    }
	    if (Math.sqrt(normA) * Math.sqrt(normB) ==0.0)
		return 0.0;
	else   
	    return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
	}

}
