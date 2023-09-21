package application;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.nlp.coref.data.CorefChain;
import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.util.StringUtils;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.pipeline.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.pipeline.*;
import java.util.Properties;
import java.util.stream.Collectors;

import Jwiki.Jwiki;
import edu.stanford.nlp.ling.CoreAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.MentionsAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import java.util.*;


public class ChatBot {
    String name;
    String phrases = "";
    static int counter = 0;
    String userLang = "en";
    Properties props = new Properties();
    StanfordCoreNLP pipelinePhrase;
    StanfordCoreNLP pipelineAns;
    
    public ChatBot(String name, String userLang){
        this.name = name;
        this.userLang = userLang;
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner");
         pipelinePhrase = new StanfordCoreNLP(props);
         pipelineAns= new StanfordCoreNLP(props);
    };
    public String sendPhrase(String phrase){

        String ans = "";
        if(userLang!="en") {
        	phrase = translate(phrase, userLang, "en");
        }
        phrase=dataClean(phrase);
        System.out.println("phrase" + phrase);
        String[] stringArray = phrase.split(" ");

        // Send phrase to the POS tagger; Returns an ArrayList of possible keywords
        ArrayList<String> list = pos(phrase);
        String[] taggedData = new String[list.size()];
        taggedData = list.toArray(taggedData);
        // Loop to find first keyword
        for(int i = 0; i< taggedData.length; i++){
            // If the first keyword is found call search() to find second keyword
            if(taggedData[i].equals("experience")){
                ans=search("experience", taggedData);
                break;
            };
            if(taggedData[i].equals("travel")){
                ans=search("travel", taggedData);
                break;
            };
            if(taggedData[i].equals("goal")){
                ans=search("goal", taggedData);
                break;
            };
            if(taggedData[i].equals("hobby")){
                ans=search("hobby", taggedData);
                break;
            };
            if(taggedData[i].equals("school")){
                ans=search("school", taggedData);
                break;
            };
            if(taggedData[i].equals("volunteer")){
                ans=search("volunteer", taggedData);
                break;
            };
            if(taggedData[i].equals("salary")){
                ans=search("salary", taggedData);
                break;
            };
            if(taggedData[i].equals("skills")){
                ans=search("skills", taggedData);
                break;
            };
            if(taggedData[i].equals("training")){
                ans=search("training", taggedData);
                break;
            };
            if(taggedData[i].equals("certifications")){
                ans=search("certifications", taggedData);
                break;
            };
        };
    
      

      //If none of the keywords were found look in the miscellaneous csv for generic questions
      if(ans.length()==0){
    	  System.out.println(stringArray + "SA");
          ans=search("miscellaneous", stringArray);
      }
      //If the ansswer is still empty no keywords were found
      return ans.length()!=0?ans:"Can you please rephrase the question?";    
  }

  public String search(String keyword, String[] stringArray){
      //String csvPath="C:\\Users\\Brandon\\Desktop\\csvs\\" + keyword + ".csv";
      String csvPath="C:\\Users\\Jesse\\Desktop\\310IndividualProject\\310IndividualProject\\Individual Project\\csvs\\" + keyword + ".csv";
      ArrayList<String> data = new ArrayList<String>();
      String row = "";
      boolean breakOut = false;
      String ans = "";
      try(BufferedReader csvReader = new BufferedReader(new FileReader(csvPath))){
          while ((row = csvReader.readLine()) != null) {
              String[] rowData = row.split(",");
               data.add(rowData[0]);
               data.add(rowData[1]);
          };
      }catch(FileNotFoundException e){
          System.out.println(e);                        
      }catch(IOException e){
          System.out.println(e);
      }

      for(int j = 0; j<stringArray.length; j++){
          for(int k=0; k<data.size(); k+=2){
              if(stringArray[j].equals(data.get(k))){
                  ans = data.get(k+1).toString();
                  breakOut=true;
              }
          }
          if(breakOut==true)
              break;      
          }
      if(ans.length()==0)
          ans = "Could you be a little more specific please?";

      return ans;
  }

  public String dataClean(String phrase){
      String cleanedPhrase=phrase.toLowerCase();
      cleanedPhrase=cleanedPhrase.replace("?","").replace(".","").replace(",","").replace("!","");
      return cleanedPhrase;
  };

  	public String wiki(String phrase) {
  		Jwiki jwiki = new Jwiki(phrase);
		String[] sentences = (String[]) jwiki.getExtractText().split("\\.");
		System.out.println(sentences[0] + ".");
		return sentences[0];
  	}
    
    
  	public String translate(String text, String langFrom, String langTo)  {
  		String webAppUrl = "https://script.google.com/macros/s/AKfycbwrazV1jr40DPVw_zQjKDedioI9MvXt_iJfU9x3h_t4pdT0l_K1RS_CaqsyuTtqwRJc/exec";
  		try {
  		String urlStr = webAppUrl +
                "?q=" + URLEncoder.encode(text, "UTF-8") +
                "&target=" + langTo +
                "&source=" + langFrom;
        URL url = new URL(urlStr);
        StringBuilder response = new StringBuilder();
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        String res = response.toString();  
        res=res.replace("&#39;", "'").replace("Ã©", "é").replace("Ã¨", "è").replace("Ãª", "ê").replace("Ã®", "î");        
        return res;}
  		catch(Exception err) {
  			System.out.print(err+ "err");
  			return "";  		}
  	}

  

    public ArrayList<String> pos(String text){

        ArrayList<String> possibleKeywords = new ArrayList<>();
        Properties property = new Properties();
        property.setProperty("annotators", "tokenize,ssplit,pos");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(property);
        CoreDocument document = pipeline.processToCoreDocument(text);
        for (CoreLabel token : document.tokens()) {
            //System.out.println(token.word() + "   " + token.tag());
            if (token.tag().equals("NN") || token.tag().equals("NNS") || token.tag().equals("JJ") || token.tag().equals("JJS") || token.tag().equals("CD") || token.tag().equals("VB") || token.tag().equals("UH") || token.tag().equals("WRB") || token.tag().equals("RB") || token.tag().equals("DT")){
                possibleKeywords.add(token.word());
            }
        }
        return possibleKeywords;
    }

   // coreference function
    String coreference(String phrase) {
    	if(counter>=3) {
    		phrases = phrases.substring(phrases.indexOf(".",2));
    	};
    	counter++;
    	phrases = phrases + ". " + phrase;
    	System.out.println(phrases);
 	   	Document doc = new Document(phrases);
 	   	Map<Integer, CorefChain> ar = doc.coref();
        for (Entry<Integer, CorefChain> me : ar.entrySet()) {
        	
            Pattern regex = Pattern.compile("\"([^\"]*)\"");
            ArrayList<String> allMatches = new ArrayList<String>();
            Matcher matcher = regex.matcher(me.getValue().toString());
            while(matcher.find()){
            	allMatches.add(matcher.group(1));
            };
            for (int i = 0; i < allMatches.size(); i++) {
				if(allMatches.get(i).toString().equals("it")) {
					phrase = phrase.replace("it", allMatches.get(0).toString());
				}
			}
        }
        System.out.println(ar.toString());
        System.out.println(phrase);
    	return phrase;
    }

    String NER(String phrase, String ans) {
        Annotation annotationPhrase = new Annotation(phrase);
        Annotation annotationAns = new Annotation(ans);
        pipelinePhrase.annotate(annotationPhrase);
        pipelineAns.annotate(annotationAns);
        List<CoreMap> multiWordsExpPhrase = annotationPhrase.get(MentionsAnnotation.class);
        List<CoreMap> multiWordsExpAns = annotationAns.get(MentionsAnnotation.class);
        for(CoreMap multiWordPhrase: multiWordsExpPhrase) {
        	String custNERClassPhrase = multiWordPhrase.get(NamedEntityTagAnnotation.class);
        	System.out.println(multiWordPhrase +" : " +custNERClassPhrase);
        	for(CoreMap multiWordAns: multiWordsExpAns) {
        		String custNERClassAns = multiWordAns.get(NamedEntityTagAnnotation.class);
        		if(custNERClassAns == custNERClassPhrase) {
        			ans = ans.replaceAll(multiWordAns.toString(), multiWordPhrase.toString());
        		}
        		if(custNERClassAns.equals("PERSON")||custNERClassAns.equals("COUNTRY")||custNERClassAns.equals("ORGANIZATION")) {
    				ans+= " More info on " + multiWordPhrase.toString()+ ": " +wiki(multiWordPhrase.toString().toLowerCase()) + ".";
    			}
        	}
        }
        if(userLang!="en") {return translate(ans, "en", userLang);}
        return ans;
    }
}