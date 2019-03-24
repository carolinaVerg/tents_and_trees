import java.util.LinkedList;

public class Tree extends Square{
	private  Square tent;
	public Tree(int x, int y) {
		 super(x, y);
		 this.tent=null;
		 this.obj='T';
	}
	public Square getTent() {
		return tent;
	}
	public void setTent( Square tent) {
		this.tent = tent;
	}
	

	
	

	
}
