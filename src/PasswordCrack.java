import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;


public class PasswordCrack {
	public static ArrayList<String> dictionary = new ArrayList<String>();
	public static ArrayList<String> fullDictionary = new ArrayList<String>();
	public static String dictionaryInput = "", passwordsInput = "";
	
	public static void main(String args[]) {
		dictionaryInput = args[0];
		passwordsInput = args[1];
		long start = System.nanoTime();    
		setupDictionary();
		long elapsedTime = System.nanoTime() - start;
		System.out.println("Time to set up dictionary: " + (double)(elapsedTime/1000000000) + " seconds");
		crackPasswords();
	}

	private static void setupDictionary() {
		BufferedReader br;
		String line = "";
		try {
			br = new BufferedReader(new FileReader(dictionaryInput));
	        while ((line = br.readLine()) != null) 
	        {
	        	dictionary.add(line.toLowerCase().trim()); 
        	    fullDictionary.add(line.toLowerCase().trim()); 
	        }
			br.close();} 
		catch (Exception e) { e.printStackTrace(); }
	}

	private static void mangle() {
		for(String word : dictionary) {
			for(int i = 32; i < 127; i++) {
				fullDictionary.add((char)i + word);
				fullDictionary.add(word + (char)i);
				fullDictionary.add(word.substring(0, 1) + word.substring(1, word.length()).toUpperCase() + (char)i);
				fullDictionary.add((char)i + word.substring(0, 1) + word.substring(1, word.length()).toUpperCase());
				if(word.length() > 1) {
					fullDictionary.add(word.substring(0, word.length()-1) + (char)i);
					fullDictionary.add(word.substring(1) + (char)i);
					fullDictionary.add(new StringBuilder(word.substring(0, word.length()-1)).reverse().toString() + (char)i);
					fullDictionary.add(new StringBuilder(word.substring(1)).reverse().toString() + (char)i);
					fullDictionary.add((char)i + word.substring(0, word.length()-1));
					fullDictionary.add((char)i + word.substring(1));
					fullDictionary.add((char)i + new StringBuilder(word.substring(0, word.length()-1)).reverse().toString());
					fullDictionary.add((char)i + new StringBuilder(word.substring(1)).reverse().toString());
				}
			}
			
			if(word.length() > 1) {
				fullDictionary.add(word.substring(0, word.length()-1));
				fullDictionary.add(word.substring(1));
				fullDictionary.add(new StringBuilder(word.substring(0, word.length()-1)).reverse().toString());
				fullDictionary.add(new StringBuilder(word.substring(1)).reverse().toString());
			}
			
			fullDictionary.add(new StringBuilder(word.substring(0, 1).toUpperCase() + word.substring(1, word.length()-1)).reverse().toString());
			
			fullDictionary.add(word.substring(0, 1) + word.substring(1, word.length()-1).toUpperCase());
			
			fullDictionary.add(new StringBuilder(word).reverse().toString());
			
			fullDictionary.add(word+word);
			
			fullDictionary.add(new StringBuilder(word + word).reverse().toString());	

			fullDictionary.add(word + new StringBuilder(word).reverse().toString());	
			
			fullDictionary.add(new StringBuilder(word).reverse().toString() + word);
			
			fullDictionary.add(word.toUpperCase());
			
			fullDictionary.add(word.substring(0, 1).toUpperCase() + word.substring(1));

			fullDictionary.add(new StringBuilder(word.substring(0, 1).toUpperCase() + word.substring(1)).reverse().toString());

			fullDictionary.add(word.substring(0, 1) + word.substring(1, word.length()).toUpperCase());
			
			String sTrInG = "", StRiNg = "";
			for(int i = 0; i < word.length(); i++) {
				if (i % 2 == 0) {
					sTrInG += word.substring(i, i + 1);
					StRiNg += word.substring(i, i + 1).toUpperCase();
				} else {
					sTrInG += word.substring(i, i + 1).toUpperCase();
					StRiNg += word.substring(i, i + 1);
				}
			}
			
			fullDictionary.add(sTrInG);
			fullDictionary.add(StRiNg);
		}
	}	
	
	private static void crackPasswords() {
		BufferedReader br;
		String line = "";
		
		ArrayList<String> salts = new ArrayList<String>();
		ArrayList<String> hashes = new ArrayList<String>();
		
		try {
			br = new BufferedReader(new FileReader(passwordsInput));
	        while ((line = br.readLine()) != null) 
	        {
	        	dictionary.add(line.split(":")[4].split(" ")[0].toLowerCase().trim());
	        	dictionary.add(line.split(":")[4].split(" ")[1].toLowerCase().trim());	        	
	        	fullDictionary.add(line.split(":")[4].split(" ")[0].toLowerCase().trim());
	        	fullDictionary.add(line.split(":")[4].split(" ")[1].toLowerCase().trim());
	        	salts.add(line.split(":")[1].substring(0, 2));
	        	hashes.add(line.split(":")[1]);
	        }
			br.close();} 
		catch (Exception e) { e.printStackTrace(); }
		
		mangle();
		
		for(int i = 0; i < salts.size(); i++) 
        	System.out.println(crack(salts.get(i), hashes.get(i)));
	}

	private static String crack(String salt, String hash) {
		long start = System.nanoTime();    
		for(int i = 0; i < fullDictionary.size(); i++) {
			if(fullDictionary.get(i).length() > 8) {
				if (jcrypt.crypt(salt, fullDictionary.get(i).substring(0, 8)).equals(hash))
					return fullDictionary.get(i) + " : " + (double)(System.nanoTime() - start)/1000000000 + " seconds";
			} else {
				if (jcrypt.crypt(salt, fullDictionary.get(i)).equals(hash))
					return fullDictionary.get(i) + " : " + (double)(System.nanoTime() - start)/1000000000 + " seconds";
			}
		}
		return "NOPASSWORDFOUND";
	}
}
