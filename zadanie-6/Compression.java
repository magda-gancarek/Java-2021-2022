import java.util.*;
import java.util.stream.Collectors;


class Compression implements CompressionInterface{

  int długość_wejście;
	int skompresowany;
	int optymalny;
  boolean można_skompresować = false;
  List<String> słowa_na_wejściu = new LinkedList<>();
  Map<String, Integer> mapa_wystąpień = new HashMap<>();
	Map<String, Integer> posortowana_mapa;
  Map<String, String> naglowek = new HashMap<>();
  Map<String, String> Na_wyjsciu = new HashMap<>();
  int wyjscie;
	int n; //długość pojedyńczego wchodzącego napisu -> liczba znakow w  kluczu
	int dł_klucza_opt;
  int ilość_slów_opt; 
	String napis_koncowy;


  public void addWord(String word) {
    słowa_na_wejściu.add( licznikDlugosciWejscia(word) );
    StatystykaWystapien(word);
  }
//****************************************
  private String licznikDlugosciWejscia(String slowo) {
		n = slowo.length();
    długość_wejście += slowo.length();
    return slowo;
  }
//*****************************************

  private void StatystykaWystapien(String slowoDoMapy) {
    if (mapa_wystąpień.get(slowoDoMapy) != null) {
       mapa_wystąpień.put(slowoDoMapy, mapa_wystąpień.get(slowoDoMapy) + 1);
    } 
		else {
    	mapa_wystąpień.put(slowoDoMapy, 1);
    }
  }
//*****************************************
	private static Map<String, Integer> sortowanieWartosciOdNajwiekszej(Map<String, Integer> mapa_wystąpień) {

    List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(mapa_wystąpień.entrySet());

    Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
        public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
            return (o2.getValue()).compareTo(o1.getValue());
        }
    });
    Map<String, Integer> posortowana_mapa = new LinkedHashMap<String, Integer>();
    for (Map.Entry<String, Integer> entry : list) {
        posortowana_mapa.put(entry.getKey(), entry.getValue());
    }
   return posortowana_mapa;
}
//*****************************************
  public void compress() {
	//System.out.println("Długość klucza: " + n);

  /*for(var m :mapa_wystąpień.entrySet()){
    System.out.println("mapa_wystapien : "+ m.getKey()+" "+m.getValue()); }*/		

  Map<String, Integer> posortowana_mapa = sortowanieWartosciOdNajwiekszej(mapa_wystąpień);

  /*for(Map.Entry m :posortowana_mapa.entrySet()){
  	System.out.println("posortowana_mapa: "+ m.getKey()+" "+m.getValue()); }*/


 int optymalny = długość_wejście; 

	//n - liczba_znakow_w_kluczu 
  for (int i = 1; i < n; i++) {

		/*max liczba zamienianych słów -> na co możemy zmienić
		1->	0 
		2->	00 01
		4-> 000 001 010 011 
		2^i-1  bo od 1 zaczyna
		*/

  	for (int j = 1; j <= Math.pow(2, i - 1); j++) { 

  		skompresowany = długość_wejście;

    	for (int k = 0; k < posortowana_mapa.size(); k++) {
	   	 	String slowo = posortowana_mapa.keySet().toArray()[k].toString();
				//System.out.println(k + " słowo : " + slowo);
      	int ile_razy_wyst = posortowana_mapa.get (posortowana_mapa.keySet().toArray()[k]);
					
      	if (k < j) {
					//Zyskujemy na kompresji
					int zysk = slowo.length() * ile_razy_wyst; 
       	 	skompresowany = skompresowany - zysk; 
					//Tracimy na kompresji
					int strata = (i + slowo.length()) + ile_razy_wyst * i;
       		skompresowany = skompresowany + strata;
      	} 
				else {
					//Tracimy na kompresji
					int strata = ile_razy_wyst;
      		skompresowany = skompresowany + strata; 
      	}	

    	}

    	if (skompresowany < optymalny) {
				//System.out.println("Znaleziono najwydajniejsza kompresje");
				można_skompresować = true;
      	optymalny = skompresowany;
      	dł_klucza_opt = i;
      	ilość_slów_opt = j;
    	}

  	}
	}

  if (można_skompresować) {

	  for (int i = 0; i < ilość_slów_opt; i++) {
			
			//System.out.println( "ile miejsc ma zajac naglowek: " "%" + dł_klucza_opt + "s");	
			String Value = String.format("%" + dł_klucza_opt + "s", Integer.toBinaryString(i)).replaceAll(" ", "0");

			naglowek.put(Value, posortowana_mapa.keySet().toArray()[i].toString());
			
    }
		//System.out.println(naglowek);	
  }

}

//****************************************
  public Map<String, String> getHeader() {
     return naglowek;
  }

//****************************************
  public String getWord() {
     
	 Na_wyjsciu = naglowek.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
	//zwrócenie słowa i przesunięcie wyjście o 1 aby przy następnym wywołaniu było kolejne 
    if (Na_wyjsciu.get(słowa_na_wejściu.get(wyjscie)) == null) {
			//dodanie 1 gdy slowo nie ulega zmianie 
			napis_koncowy = "1" + słowa_na_wejściu.get(wyjscie++);
			//System.out.println(wyjscie);
      return napis_koncowy;

    } 
		else {
			//gdy slowo jest w naglowku
			napis_koncowy = Na_wyjsciu.get(słowa_na_wejściu.get(wyjscie++));//System.out.println(wyjscie);
      return napis_koncowy;
    }

  }
  
}

