import java.util.*;

class Shop implements ShopInterface {
	
Map<String, Integer> stan_sklepu = new HashMap<>(); 
Map<String, Object> obiekty_do_synch = new HashMap<>();
boolean czy_udało_się_kupić = false;


	public void delivery(Map<String, Integer> goods){
		Iterator<Map.Entry<String, Integer>> iterator = goods.entrySet().iterator();
	
		while (iterator.hasNext()){
    	synchronized(this){
        Map.Entry<String, Integer> obecny_el = iterator.next();
     		String produkt = obecny_el.getKey();
				int ilość = obecny_el.getValue();
				int do_dodania;
 			  //System.out.println("Przekazane produkty: " + produkt + ":" + ilość);
				//zawiera produkt->sumuje ilość 
				if (stan_sklepu.containsKey(produkt) == true){
					do_dodania = stan_sklepu.get(produkt);
					ilość += do_dodania;
	      	stan_sklepu.put(produkt, ilość);
				}
				//nie zawiera -> dodaje 0 i tworzy nowy obiekt do synch
				else{
					stan_sklepu.put(produkt, ilość);
					if (obiekty_do_synch.get(produkt) == null) {
						obiekty_do_synch.put(produkt, new Object());
					//	System.out.println("Utworzono nowy obiekt");
					}
				}
			  
				// Informacja dla klienta, że dostarczono nowe produkty
				//notifyAll na obiekcie na który czekają konkretne wątki
					synchronized (obiekty_do_synch.get(produkt)) {
					obiekty_do_synch.get(produkt).notifyAll();
				}
	 		}
		}

}


	public boolean purchase (String productName, int quantity){
		String chce_kupić = productName;
		int ile_sztuk = quantity;
		int ile_w_sklepie;
		//System.out.println("Klient: " + chce_kupić + " " + ile_sztuk);
		//Jeśli w sklpeie nie ma jeszcze produktu który chce klient to jest dodawany i klient czeka na powiadomienie o nim
		if (obiekty_do_synch.get(chce_kupić) == null) {
			obiekty_do_synch.put(chce_kupić, new Object());
		}

		synchronized(obiekty_do_synch.get(chce_kupić)){
			if (stan_sklepu.containsKey(chce_kupić) == true && stan_sklepu.get(chce_kupić) >= ile_sztuk){
				//System.out.println("W sklepie jest produkt");
				ile_w_sklepie = stan_sklepu.get(chce_kupić);
				//System.out.println("Ilość w sklepie " + ile_w_sklepie);
				//	System.out.println("Dokonuje zakupu");
					stan_sklepu.put(chce_kupić, ile_w_sklepie - ile_sztuk);
					czy_udało_się_kupić = true;
					return czy_udało_się_kupić;

			}
			else{
				//Obecnie nie ma produktu w sklepie więc klient ma czekać
				//System.out.println("Obecnie za mało w sklepie");
				try {
					//System.out.println("Czekam na produkt - idę spać");
					obiekty_do_synch.get(chce_kupić).wait();//zatrzymanie wykonywania zakupu do momentu, aż delivery dokona powiadomienia o dostawie produktów 
					//umożliwienie innym wątką dostępu do metody
					//System.out.println("!!!! Zostałem obudzony bo pojawił się produkt w sklepie");
 				} catch(InterruptedException e) {
 					System.out.println(e.getMessage());
 				}
				if (stan_sklepu.get(chce_kupić) != null && stan_sklepu.get(chce_kupić) >= ile_sztuk) {
					stan_sklepu.put(chce_kupić, stan_sklepu.get(chce_kupić) - ile_sztuk);
					//	System.out.println("Dokonuje zakupu");
					czy_udało_się_kupić = true;
					return czy_udało_się_kupić;
				}

			}
	
		}
			
		//true - zakup zrealizowany, false - zakup niezrealizowany
		czy_udało_się_kupić = false;
		return czy_udało_się_kupić;
	}


	public Map<String, Integer> stock(){
		return stan_sklepu;
	}

}