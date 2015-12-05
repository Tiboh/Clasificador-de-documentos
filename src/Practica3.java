import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Practica3 {

    public static void main(String[] args) {
    	
    	final String ECO_GLOSSARY_LEM = "ecoGlossaryLem.txt";
    	final String POL_GLOSSARY_LEM = "polGlossaryLem.txt";
    	final String SPO_GLOSSARY_LEM = "spoGlossaryLem.txt";

    	Tokenizer tokenizer = new Tokenizer();
   
        List<String> ecoGlossary = tokenizer.extractWords(Util.readFile("res/ecoGlossary.txt"));
        List<String> polGlossary = tokenizer.extractWords(Util.readFile("res/polGlossary.txt"));
        List<String> spoGlossary = tokenizer.extractWords(Util.readFile("res/spoGlossary.txt"));
        
        Lemmatizer lem = new Lemmatizer("res/lemmatization-es.txt");
        
        // Lemmatize glossaries
        List<String> ecoGlossaryLem = new ArrayList<String>();
        for(int i = 0 ; i < ecoGlossary.size() ; i++){
        	ecoGlossaryLem.add(lem.lemmatize(ecoGlossary.get(i)));
        }
        Util.writeFileLineByLine(ECO_GLOSSARY_LEM, ecoGlossaryLem);
        List<String> polGlossaryLem = new ArrayList<String>();
        for(int i = 0 ; i < polGlossary.size() ; i++){
        	polGlossaryLem.add(lem.lemmatize(polGlossary.get(i)));
        }
        Util.writeFileLineByLine(POL_GLOSSARY_LEM, polGlossaryLem);
        List<String> spoGlossaryLem = new ArrayList<String>();
        for(int i = 0 ; i < spoGlossary.size() ; i++){
        	spoGlossaryLem.add(lem.lemmatize(spoGlossary.get(i)));
        }
        Util.writeFileLineByLine(SPO_GLOSSARY_LEM, spoGlossaryLem);
        
        List<List<String>> glossariesLem = Arrays.asList(ecoGlossaryLem, polGlossaryLem, spoGlossaryLem);
        
        List<String> inputEcoPath = new ArrayList<String>();
        List<String> inputPolPath = new ArrayList<String>();
        List<String> inputSpoPath = new ArrayList<String>();
        for(int i = 1 ; i <= 15 ; i++){
        	inputEcoPath.add("res/eco"+i+".txt");
        	inputPolPath.add("res/pol"+i+".txt");
        	inputSpoPath.add("res/spo"+i+".txt");
        }
        
        List<Integer> n = new ArrayList<Integer>();
        List<String> themes = new ArrayList<String>();
        List<Similaridad> simi = new ArrayList<Similaridad>();
        
        int nbOfFiles = 1;
        for(int i = 0 ; i < inputEcoPath.size() ; i++){
        	themes.add("ECO");
        	simi.add(classify(inputEcoPath.get(i), glossariesLem, lem, tokenizer));
        	n.add(nbOfFiles);
        	nbOfFiles++;
        }
        for(int i = 0 ; i < inputPolPath.size() ; i++){
        	themes.add("POL");
        	simi.add(classify(inputPolPath.get(i), glossariesLem, lem, tokenizer));
        	n.add(nbOfFiles);
        	nbOfFiles++;
        }
        for(int i = 0 ; i < inputSpoPath.size() ; i++){
        	themes.add("DEP");
        	simi.add(classify(inputSpoPath.get(i), glossariesLem, lem, tokenizer));
        	n.add(nbOfFiles);
        	nbOfFiles++;
        }
                
        generateHtml(n, themes, simi);
        
    }
    
    public static Similaridad classify(String pathFile, List<List<String>> glossariesLem, Lemmatizer lem, Tokenizer tokenizer){
    	
    	List<String> input = tokenizer.extractWords(Util.readFile(pathFile));
        List<String> inputLem = new ArrayList<String>();
        for(int i = 0 ; i < input.size() ; i++){
        	inputLem.add(lem.lemmatize(input.get(i)));
        }
        
        Similaridad simi = new Similaridad();
        
        Similarity sim = new Similarity(inputLem, glossariesLem.get(0), glossariesLem);
        simi.simiEco = sim.computeSimilary();
        Similarity sim1 = new Similarity(inputLem, glossariesLem.get(1), glossariesLem);
        simi.simiPol = sim1.computeSimilary();
        Similarity sim2 = new Similarity(inputLem, glossariesLem.get(2), glossariesLem);
        simi.simiSpo = sim2.computeSimilary();
        
        if(simi.simiEco >= simi.simiPol && simi.simiEco >= simi.simiSpo && simi.simiEco > 0.25){
        	simi.state = "ECO";
        }else if(simi.simiPol >= simi.simiEco && simi.simiPol >= simi.simiSpo && simi.simiPol > 0.25){
        	simi.state = "POL";
        }else if(simi.simiSpo >= simi.simiEco && simi.simiSpo >= simi.simiPol && simi.simiSpo > 0.25){
        	simi.state = "DEP";
        }else
        	simi.state = "COBERTURA INSUF.";
        return simi;
    }
    
    public static void generateHtml(List<Integer> n, List<String> themes, List<Similaridad> simi){
    	
    	StringBuilder builder = new StringBuilder();
    	builder.append("<!DOCTYPE html>");
    	builder.append("<html lang=\"es\" style=\"font-family:monospace;\">");
    	builder.append("<head><title>Classificador</title></head>");
    	builder.append("<body>");
    	
    	builder.append("<table border=\"1\" cellpadding=\"2\" cellspacing=\"0\" style=\"width:100% border-style:groove border-collapse:collapse;\">");
    	
    	builder.append("<tr>");
		builder.append("<td align=\"center\">N</td>");
		builder.append("<td align=\"center\">Thema</td>");
		builder.append("<td align=\"center\">Economia</td>");
		builder.append("<td align=\"center\">Politica</td>");
		builder.append("<td align=\"center\">Deporte</td>");
		builder.append("<td align=\"center\">Classificacion</td>");
    	builder.append("</tr>");

    	for(int i = 0 ; i < n.size() ; i++){
    		builder.append("<tr>");
    		
    		builder.append("<td align=\"center\">");
    		builder.append(n.get(i));
        	builder.append("</td>");
        	
        	builder.append("<td align=\"center\">");
    		builder.append(themes.get(i));
        	builder.append("</td>");
        	
        	builder.append("<td align=\"center\">");
            if(simi.get(i).simiEco >= simi.get(i).simiPol && simi.get(i).simiEco >= simi.get(i).simiSpo) builder.append("<strong style=\"color:orange;\">");
    		builder.append((int) Math.floor(simi.get(i).simiEco*100)+"%");
            if(simi.get(i).simiEco >= simi.get(i).simiPol && simi.get(i).simiEco >= simi.get(i).simiSpo) builder.append("</strong>");
        	builder.append("</td>");
        	builder.append("<td align=\"center\">");
            if(simi.get(i).simiPol >= simi.get(i).simiEco && simi.get(i).simiPol >= simi.get(i).simiSpo) builder.append("<strong style=\"color:orange;\">");
    		builder.append((int) Math.floor(simi.get(i).simiPol*100)+"%");
            if(simi.get(i).simiPol >= simi.get(i).simiEco && simi.get(i).simiPol >= simi.get(i).simiSpo) builder.append("</strong>");
        	builder.append("</td>");
        	builder.append("<td align=\"center\">");
            if(simi.get(i).simiSpo >= simi.get(i).simiPol && simi.get(i).simiSpo >= simi.get(i).simiEco) builder.append("<strong style=\"color:orange;\">");
    		builder.append((int) Math.floor(simi.get(i).simiSpo*100)+"%");
            if(simi.get(i).simiSpo >= simi.get(i).simiPol && simi.get(i).simiSpo >= simi.get(i).simiEco) builder.append("</strong>");
        	builder.append("</td>");
        	
        	String couleur;
    		if(simi.get(i).state.equals(themes.get(i))){
    			couleur = "green";
    		}
    		else{
    			couleur = "red";
    		}
    		builder.append("<td align=\"center\" style=\"color:"+couleur+"\">");
        	builder.append("<strong>");
    		builder.append(simi.get(i).state);
        	builder.append("</strong>");
        	builder.append("</td>");
    	
        	builder.append("</tr>");
    	}
    	
    	builder.append("</table>");
    	
    	builder.append("</body>");
    	builder.append("</html>");
    	String html = builder.toString();
    	
    	Util.writeTxtFile("classification.html", html);
    	
    }
	
	
}
