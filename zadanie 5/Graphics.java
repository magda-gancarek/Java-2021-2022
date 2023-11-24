import java.util.*;

class Graphics implements GraphicsInterface {
	
	CanvasInterface płótno;

//dostarczenie płótna
	public void setCanvas(CanvasInterface płótno2) {
		płótno = płótno2;
	}

//wypełnienie kolorem od pozycji StartingPoint	
	public void fillWithColor(Position startingPosition, Color color) throws GraphicsInterface.WrongStartingPosition, GraphicsInterface.NoCanvasException {
	
	//gdy nie ma płótna
		if (płótno == null) throw new NoCanvasException();
    
    try {
			//zmiana koloru w StratingPoint
			płótno.setColor(startingPosition, color);
		} catch (CanvasInterface.CanvasBorderException e) {
			throw new WrongStartingPosition();
    } catch (ArrayIndexOutOfBoundsException e) {
      throw new WrongStartingPosition();
		} catch (CanvasInterface.BorderColorException e) {
			try {
				płótno.setColor(startingPosition, e.previousColor); 
				} catch (Exception ex1) {}

			//zły punkt startowy	
      throw new WrongStartingPosition(); 
		}

		List<Position> punkty_spr = new LinkedList<>();
		Queue<Position> kolejka_do_spr = new ArrayDeque<>();

		//dodanie pierwszyego elemetu do kolejki elementów do zamalowania
		//System.out.println("Punkt Startowy: " + startingPosition);
		kolejka_do_spr.add(startingPosition);
		//System.out.println(kolejka_do_spr.toString());


		while (kolejka_do_spr.size() > 0) {
			//kolejka z usuniętym pierwszym elementem
      Position obecna_pozycja = kolejka_do_spr.remove(); //remove() <- zwraca wyjątek, poll() <-null

			//gdy lista sprawdzonych elementów posiada już obecną_pozycję to przechodzimy do kolejnego elementu	
			if (punkty_spr.contains(obecna_pozycja)) {
				
				continue;
			} 
			//do listy sprawdzonych elementów dodajemy obecnie sprawdzany element ( bo po tym przejściu pętli będzie już sprawdzony)
			else {
				punkty_spr.add(obecna_pozycja);
			}
			
			try {
				płótno.setColor(obecna_pozycja, color);
			} catch (CanvasInterface.CanvasBorderException e) {
				continue;
			} catch (CanvasInterface.BorderColorException e) {
				try {
					płótno.setColor(obecna_pozycja, e.previousColor);
					continue;
				} catch (Exception ex2) {} //pusty catch
			}
			
			//dodanie 4 punktów dookoła obecnie_sprawdzanego
			
			//1
			if (punkty_spr.contains(new Position2D(obecna_pozycja.getCol() + 1, obecna_pozycja.getRow()))){
				//System.out.println("Zawiera już ten element");
			}
			else{
				kolejka_do_spr.add(new Position2D(obecna_pozycja.getCol() + 1, obecna_pozycja.getRow()));
			}
			//2
			if (punkty_spr.contains(new Position2D(obecna_pozycja.getCol() - 1, obecna_pozycja.getRow()))){
			//	System.out.println("Zawiera już ten element");
			}
			else{
				kolejka_do_spr.add(new Position2D(obecna_pozycja.getCol() - 1, obecna_pozycja.getRow()));
			}
			//3
			if (punkty_spr.contains(new Position2D(obecna_pozycja.getCol(), obecna_pozycja.getRow() - 1))){
			//	System.out.println("Zawiera już ten element");
			}
			else{
				kolejka_do_spr.add(new Position2D(obecna_pozycja.getCol(), obecna_pozycja.getRow() - 1));
			}

			//4
			if (punkty_spr.contains(new Position2D(obecna_pozycja.getCol(), obecna_pozycja.getRow() + 1))){
			//	System.out.println("Zawiera już ten element");
			}
			else{
				kolejka_do_spr.add(new Position2D(obecna_pozycja.getCol(), obecna_pozycja.getRow() + 1));
			}


		}
		
	}
	
//z zadania 4 Position2D klasa wewnętrzna
  class Position2D implements Position {

    private final int col;
    private final int row;

    public Position2D(int col, int row) {
      this.col = col;
      this.row = row;
    }

    public int getRow() {
      return row;
    }

    public int getCol() {
      return col;
    }
    
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      Position2D other = (Position2D) obj;
      return col == other.col && row == other.row;
    }
    
  }
}
