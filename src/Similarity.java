import java.util.ArrayList;
import java.util.List;

public class Similarity {
	
	private List<String> glossary;
	private List<String> query;
	private List<List<String>> glossaries;
	
	Similarity(List<String> glossary, List<String> query, List<List<String>> glossaries){
		// TODO Auto-generated method stub
		this.glossary = glossary;
		this.query = query;
		this.glossaries = glossaries;
	}
	
	public double computeSimilary(){
		
		TFIDFCalculator calculator = new TFIDFCalculator();

		List<Double> idfs = new ArrayList<Double>();
		for(String term : this.query){
			idfs.add(calculator.idf(this.glossaries, term));
		}
		
		List<Double> tfidfs = new ArrayList<Double>();
		for(String term : this.query){
			tfidfs.add(calculator.tfIdf(this.glossary, this.glossaries, term));
		}
		
		double dotProduct = 0;
		for(int i = 0 ; i < this.query.size() ; i++){
			dotProduct += idfs.get(i)*tfidfs.get(i);
		}
		
		List<Double> squareIdfs = new ArrayList<Double>();
		List<Double> squareTfidfs = new ArrayList<Double>();
		for(int i = 0 ; i < this.query.size() ; i++){
			squareIdfs.add(Math.pow(idfs.get(i),2));
			squareTfidfs.add(Math.pow(tfidfs.get(i),2));
		}
		
		double normQuery = 0;
		double normDoc = 0;
		for(int i = 0 ; i < squareTfidfs.size() ; i++){
			normQuery += squareTfidfs.get(i);
			normDoc += squareIdfs.get(i);
		}
		normQuery = Math.sqrt(normQuery);
		normDoc = Math.sqrt(normDoc);
		
		double retour;
		
		if(normQuery != 0 && normDoc != 0){
			retour = dotProduct/(normQuery*normDoc);
		}else{
			retour = 0;
		}
		
		return retour;
	}

}


