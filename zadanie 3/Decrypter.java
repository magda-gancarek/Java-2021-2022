import java.util.HashMap;
import java.util.Map;
import java.lang.String;
import java.util.List;
import java.util.ArrayList;

public class Decrypter implements DecrypterInterface{

String prawidlowy = "Wydział Fizyki, Astronomii i Informatyki Stosowanej";
String deszyfrowany_napis;
int unikalne_znaki[] = {0,1,2,3,4,5,6,8,12,16,17,18,19,20,21,23,29,31,41,46,49,50};
int tab_p[] = new int[10];
int tab_k[] = new int[10];
int indeks_p = 0;
int indeks_k = 0;
boolean e;
boolean mozna_rozkodowac;

public void setInputText(String encryptedDocument){
	String napis;
	napis = encryptedDocument;
	//System.out.println(napis);
	if (napis == null){
		mozna_rozkodowac = false;
		return;
	}

	if (napis.length() < prawidlowy.length() ){
		mozna_rozkodowac = false;
		return;
	}

	napis = napis.replaceAll("\t", " ");
	napis = napis.replaceAll("\n", " ");
	napis = napis.replaceAll(" +", " ");

	
	ArrayList<String> lista_prawidlowa =  new ArrayList<>(List.of(prawidlowy.split("\\s")));
	ArrayList<String> lista_dekodowana =  new ArrayList<>(List.of(napis.split("\\s")));

	//	for (String wyś : lista_prawidlowa) {System.out.println(wyś);	}
	//	for (String wyś : lista_dekodowana) {  System.out.println(wyś);	}


	ArrayList<Integer> długosc_slow_prawidlowa = new ArrayList<Integer>();
  for (String s : lista_prawidlowa)
    długosc_slow_prawidlowa.add(s.length());

 ArrayList<Integer> długosc_slow = new ArrayList<Integer>();
  for (String s : lista_dekodowana)
     długosc_slow.add(s.length());

 //for (int wyś : długosc_slow) { System.out.println(wyś);	}
 //System.out.println("Prawidłowa");
 //for (int wyś : długosc_slow_prawidlowa) { System.out.println(wyś);	}

 //czy zawiera odpowiedni ciąg długości wyrazów
 boolean zawiera = długosc_slow.containsAll(długosc_slow_prawidlowa);
 //System.out.println(zawiera);
	int licznik_poprawnych_ciagow = 0;
	int d = 6;

	if (zawiera == true){
		for( int i = 0; i < (długosc_slow.size() - 5); i++) {
			ArrayList<Integer> sprawdzenie = new ArrayList<Integer> (długosc_slow.subList(i, d));
				if (sprawdzenie.equals(długosc_slow_prawidlowa) == true) {
				//boolean czy =	sprawdzenie.equals(długosc_slow_prawidlowa);
				//	System.out.println(czy + " Znaleziono na pozycji "+ i + " " + d);

					licznik_poprawnych_ciagow++;
					//System.out.println(licznik_poprawnych_ciagow);
					tab_p[licznik_poprawnych_ciagow-1] = i;
					tab_k[licznik_poprawnych_ciagow-1] = d;
				//	System.out.println("\t " + tab_p[licznik_poprawnych_ciagow-1]);
				//	System.out.println("\t " + tab_k[licznik_poprawnych_ciagow-1]);
					}
			d++;
		}
	}

 //for(int i=0; i < tab_p.length; i++) System.out.println("tab "+ tab_p[i]);


if(licznik_poprawnych_ciagow == 0){
		mozna_rozkodowac = false;
		return;
	}

if(licznik_poprawnych_ciagow == 1) {

	indeks_p = tab_p[licznik_poprawnych_ciagow-1];
	indeks_k = tab_k[licznik_poprawnych_ciagow-1];

	ArrayList<String> gotowe_do_dek = new ArrayList<String>(lista_dekodowana.subList(indeks_p, indeks_k));
 //for (String wyś : gotowe_do_dek) { System.out.println(wyś);	}

 deszyfrowany_napis = String.join(" ", gotowe_do_dek);
 //System.out.println(deszyfrowany_napis);

ArrayList<ArrayList<Integer> > pojawienie_znaku =  new ArrayList<ArrayList<Integer> > ();
ArrayList<ArrayList<Integer> > wzor =  new ArrayList<ArrayList<Integer> > ();

	for (int x : unikalne_znaki) {
  	char znak_d = deszyfrowany_napis.charAt(x); 
		char znak_p = prawidlowy.charAt(x); 
  	ArrayList<Integer> subList_d = new ArrayList < Integer > ();
		ArrayList<Integer> subList_p = new ArrayList < Integer > ();
		for(int i = 0; i < deszyfrowany_napis.length(); i++){
    	if(deszyfrowany_napis.charAt(i) == znak_d){
		 		subList_d.add(i);
			}
		 if(prawidlowy.charAt(i) == znak_p){
		  	subList_p.add(i);
		 }

	  }
	 pojawienie_znaku.add(subList_d);
	 wzor.add(subList_p);
  }
	e =  wzor.equals(pojawienie_znaku);
	if( e == true){
			//System.out.println("ok pojedynczy");
			}
}
 
 /*System.out.println("wzor");
 for (ArrayList<Integer> mark : wzor) { System.out.println(mark);}

 System.out.println("deszyfrowana");
 for (ArrayList<Integer> mark : pojawienie_znaku) { System.out.println(mark); }*/
 
 //Więcje niż jeden poprawny ciąg znaków

if(licznik_poprawnych_ciagow > 1) {
  //System.out.println(licznik_poprawnych_ciagow);

	for( int j = 0; j < licznik_poprawnych_ciagow; j++ ){
	ArrayList<ArrayList<Integer> > pojawienie_znaku =  new ArrayList<ArrayList<Integer> > ();
 	ArrayList<ArrayList<Integer> > wzor =  new ArrayList<ArrayList<Integer> > ();

	indeks_p = tab_p[j];
	indeks_k = tab_k[j];
  //System.out.println(indeks_p + " " + indeks_k);
  ArrayList<String> gotowe_do_dek = new ArrayList<String>(lista_dekodowana.subList(indeks_p, indeks_k));
  //for (String wyś : gotowe_do_dek) { System.out.println(wyś);	}

  deszyfrowany_napis = String.join(" ", gotowe_do_dek);
  //System.out.println(deszyfrowany_napis);

 		for (int x : unikalne_znaki) {
  		char znak_d = deszyfrowany_napis.charAt(x); 
			char znak_p = prawidlowy.charAt(x); 
  		ArrayList<Integer> subList_d = new ArrayList < Integer > ();
					 for (int wyś : subList_d) { System.out.println(wyś);	}
			ArrayList<Integer> subList_p = new ArrayList < Integer > ();

			for(int i = 0; i < deszyfrowany_napis.length(); i++){
    		if(deszyfrowany_napis.charAt(i) == znak_d){
		 			subList_d.add(i);
				}
			  if(prawidlowy.charAt(i) == znak_p){
		  		subList_p.add(i);
		 		}
		  }
		 pojawienie_znaku.add(subList_d);
		 wzor.add(subList_p);
  	}

	// for (ArrayList<Integer> mark : wzor) { System.out.println(mark); }

 //for (ArrayList<Integer> mark : pojawienie_znaku) { System.out.println(mark); }

 		e =  wzor.equals(pojawienie_znaku);
		//System.out.println("wartość e " + e);
 		if( e == true){
			//System.out.println("ok za " + j + " przejsciem pętli");
			break;
			
		}
	}
}



 //Czy tablice takie same
 /*boolean equalLists = (wzor.size() == pojawienie_znaku.size() && wzor.containsAll(pojawienie_znaku));
 //System.out.println(equalLists);*/


 if (e == true){
	 mozna_rozkodowac = true;
 }
 else
 mozna_rozkodowac = false;

 //System.out.println(mozna_rozkodowac);

}




public Map<Character, Character> getCode(){
	Map<Character,Character> map = new HashMap<Character,Character>();  
  
if ( mozna_rozkodowac == true){
	for (int x : unikalne_znaki) { 
 		map.put(prawidlowy.charAt(x),deszyfrowany_napis.charAt(x));	
	}
	
	/*for(Map.Entry m:map.entrySet()){  
	System.out.println(m.getKey()+" "+m.getValue()); }*/
}
  
	return map;
}	






public Map<Character, Character> getDecode(){
	Map<Character,Character> map_d = new HashMap<Character,Character>();  
	if ( mozna_rozkodowac == true){ 
		for (int x : unikalne_znaki) { 
 			map_d.put(deszyfrowany_napis.charAt(x),prawidlowy.charAt(x));	
		}
 	}
	return map_d;
}

}




 