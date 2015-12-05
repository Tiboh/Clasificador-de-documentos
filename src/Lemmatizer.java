import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.List;

public class Lemmatizer {

	private Hashtable<String, String> ht;

	public Lemmatizer(String lemmatizerPathFile) {
		this.ht = new Hashtable<String, String>();

		try {
			File fileDir = new File(lemmatizerPathFile);
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileDir), "UTF8"));
			String str;
			while ((str = in.readLine()) != null) {
				addLineIntoHt(str);
			}
			in.close();
		} 
		catch (UnsupportedEncodingException e) 
		{
			System.out.println(e.getMessage());
		} 
		catch (IOException e) 
		{
			System.out.println(e.getMessage());
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
	}

	private void addLineIntoHt(String line){
		Tokenizer tokenizer = new Tokenizer();
		List<String> myTwoWords = tokenizer.extractWords(line);
		putWithoutDoublons(myTwoWords.get(1), myTwoWords.get(0));
		putWithoutDoublons(myTwoWords.get(0), myTwoWords.get(0));
	}
	
	private String putWithoutDoublons(String key, String value) {
        if (this.ht.containsKey(key)) {
         return value;
        } else {
          return this.ht.put(key, value);
        }
     }

	public String lemmatize(String word){
		word = word.toLowerCase();
		String retour = word;
		if(this.ht.containsKey(word)){
			retour = this.ht.get(word);
		}
		return retour;
	}

}
