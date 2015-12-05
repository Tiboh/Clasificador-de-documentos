import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

public class Tokenizer {
	
	private static final String DELIMETERS = " :;.,-'\t\n\r\"?!(){}[]”“";
	
	public List<String> extractWords(String text) {
	    List<String> words = new ArrayList<>();
	    StringTokenizer stk=new StringTokenizer(text,DELIMETERS);
        while(stk.hasMoreTokens())
        {
        	words.add(stk.nextToken());
        }
	    return words;
	}

}
