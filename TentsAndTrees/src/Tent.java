import java.util.Iterator;
import java.util.ListIterator;

public class Tent extends Square{

	public Tent(int x, int y) {
		super( x, y);
		this.obj='t';
	}
	
	public int num_trees_around() {
		int ans=0;
		if(this.up!=null && this.up.obj=='T')
			ans++;
		if(this.down!=null && this.down.obj=='T')
			ans++;
		if(this.left!=null && this.left.obj=='T')
		ans++;
		if(this.right!=null && this.right.obj=='T')
		ans++;
		return ans;
	}

}
