import java.util.LinkedList;

public class main {
 public static World world;
 public static World_creator wc;
	public static void main(String[] args) {
		wc = new World_creator(10,10);
		System.out.println(wc);
		world = new World(wc.getNum_trees(),wc.getWorld(),wc.getNum_of_tentRow(),wc.getNum_of_tentCol());
		world.solve();
		

	}

}
