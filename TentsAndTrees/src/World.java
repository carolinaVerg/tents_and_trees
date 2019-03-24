import java.util.LinkedList;


public class World {
	private int treeNum;
	private Square [][] worldMatrix;
	private int tentsInRow[];
	private int tentsInCol[];
	private int freeInRow[];
	private int freeInCol[];
	private LinkedList<Tree> leftTreeArray;
	private int n;
	private int m;
	private LinkedList<Square> leftFree;
	

	public World(int treeNum,Square [][] worldMatrix,int tentsInRow[],int tentsInCol[] ) {
		this.treeNum=treeNum;
		this.worldMatrix=worldMatrix;
		this.tentsInRow=tentsInRow;
		this.tentsInCol=tentsInCol;
		this.n=worldMatrix.length;
		this.m=worldMatrix[0].length;
		leftTreeArray=new LinkedList<>();
		leftFree = new LinkedList<Square>();
		this.freeInRow= new int [m];
		this.freeInCol=new int [n];
		genTreeList();
		genFreeList();
		genFreeInRow();
		genFreeInCol(); 
	}
	public void genTreeList() {
		int treeCounter = 0;
		for(int i = 0; i < tentsInRow.length ; ++i){
			for(int j = 0 ; j < tentsInCol.length; ++j) {
				if(worldMatrix[i][j].getObj()=='T') {
					this.leftTreeArray.add((Tree) worldMatrix[i][j]);
				}
			}
		}
	}
	public void genFreeList() {
		int treeCounter = 0;
		for(int i = 0; i < tentsInRow.length ; ++i){
			for(int j = 0 ; j < tentsInCol.length; ++j) {
				if(worldMatrix[i][j].getObj()=='f') {
					this.leftFree.add((Square) worldMatrix[i][j]);
				}
			}
		}
	}
	public void genFreeInCol() {
		for(int i = 0; i < worldMatrix.length ; ++i){
			for(int j=0; j<worldMatrix[0].length; j++ ) {
				if(worldMatrix[i][j].getObj()=='f')
					freeInCol[i]++;
			}
		}
	}
	public void genFreeInRow() {
		for(int i = 0; i < worldMatrix[0].length ; ++i){
			for(int j=0; j<worldMatrix.length; j++ ) {
				if(worldMatrix[j][i].getObj()=='f')
					freeInRow[i]++;
			}
		}
	}
	
	public Boolean onlyTentsLeft() {
		boolean changed=false;
			for(int i=0; i<worldMatrix.length; i++) {    //runs over calms
				if(this.tentsInCol[i]==this.freeInCol[i])
					for (int j=0; j<worldMatrix[i].length; j++)
						if((worldMatrix[i][j].getObj())=='f')
						{
							worldMatrix[i][j].eliminateAdj();
							removeFree(worldMatrix[i][j]);
							this.tentsInRow[j]--;
							this.tentsInCol[i]--;
							worldMatrix[i][j].setObj('t');
							changed=true;
						}
			}
			for(int i=0; i<worldMatrix[0].length; i++) {  // runs over rows
				if(this.tentsInRow[i]==this.freeInRow[i]) {
					for(Square s=worldMatrix[0][i];s!=null; s =s.getRight()) {
						if(s.getObj()=='f')
						{
							s.eliminateAdj();
							removeFree(s);
							this.tentsInRow[s.getY()]--;
							this.tentsInCol[s.getX()]--;
							s.setObj('t');
							changed=true;
						}
					}
				}			
			}	
			return changed;
	}
	public Boolean onlyGrassLeft() {
		boolean changed=false;
		for(int i=0; i<worldMatrix.length; i++) {    //runs over calms
			if(this.tentsInCol[i]==0)
				for (int j=0; j<worldMatrix[i].length; j++)
					if((worldMatrix[i][j].getObj())=='f')
					{
						removeFree(worldMatrix[i][j]);
						worldMatrix[i][j].setObj('g');
						changed=true;
						
					}
		}
		for(int i=0; i<worldMatrix[0].length; i++) {  // runs over rows
			if(this.tentsInRow[i]==0) {
				for(Square s=worldMatrix[0][i];s!=null; s =s.getRight()) {
					if(s.getObj()=='f')
					{
						
						this.freeInRow[s.getY()]--;
						this.freeInCol[s.getX()]--;
						worldMatrix[s.getX()][s.getY()].setObj('g');
						this.leftFree.remove(s);
						changed=true;
					}
				}
			}			
		}
		return changed;
	}
	public Boolean oneOptionLeftToTree() {
		boolean changed =false;
		LinkedList<Tree> newTreeList=(LinkedList<Tree>) this.leftTreeArray.clone();
		Square tent=null;
		for(Tree t:leftTreeArray){
			if(t.freeSquarse()==1 && is_tent_around_tide(t)) {
				if(t.up!=null && t.up.obj=='f') {
					tent=worldMatrix[t.getUp().getX()][t.getUp().getY()];
					tent.eliminateAdj();
					removeFree(t.getUp());
					this.tentsInRow[t.getUp().getY()]--;
					this.tentsInCol[t.getUp().getX()]--;
					tent.setObj('t');
					tent.tide=true;
					changed=true;
				}

				else if(t.down!=null && t.down.obj=='f'){
					tent=worldMatrix[t.getDown().getX()][t.getDown().getY()];
					tent.eliminateAdj();
					removeFree(t.getDown());
					this.tentsInRow[t.getDown().getY()]--;
					this.tentsInCol[t.getDown().getX()]--;
					tent.setObj('t');
					tent.tide=true;
					changed=true;
				}

				else if(t.left!=null && t.left.obj=='f'){
					tent =worldMatrix[t.getLeft().getX()][t.getLeft().getY()];
					tent.eliminateAdj();
					removeFree(t.getLeft());
					this.tentsInRow[t.getLeft().getY()]--;
					this.tentsInCol[t.getLeft().getX()]--;
					tent.setObj('t');
					tent.tide=true;
					changed=true;
				}
			
				else if(t.right!=null && t.right.obj=='f'){
					tent =worldMatrix[t.getRight().getX()][t.getRight().getY()];
					tent.eliminateAdj();
					removeFree(t.getRight());
					this.tentsInRow[t.getRight().getY()]--;
					this.tentsInCol[t.getRight().getX()]--;
					tent.setObj('t');
					tent.tide=true;
					changed=true;
				}
				t.setTent(tent);
				newTreeList.remove(t);
			}
				
		}
		this.leftTreeArray=newTreeList;
		return changed;
	}
	public Boolean squareWithNoTrees() {
		boolean changed=false;
		LinkedList<Square> newLeftList=(LinkedList<Square>) this.leftFree.clone();
		for(Square s:newLeftList) {
			if((s.getUp()==null||s.getUp().getObj()!='T')&&
			(s.getDown()==null||s.getDown().getObj()!='T')&&
			(s.getLeft()==null||s.getLeft().getObj()!='T')&&
			(s.getRight()==null||s.getRight().getObj()!='T')) {
				removeFree(s);
				s.setObj('g');
				changed=true;
			}
		}
		return changed;
	}
/*	public void chack_single_tents(Tent tent) {
		
		if(tent.up!=null && tent.up.obj=='t')
			ans++;
		if(tent.down!=null && tent.down.obj=='t')
			ans++;
		if(tent.left!=null && tent.left.obj=='t')
		ans++;
		if(tent.right!=null && tent.right.obj=='t')
		if(tent.num_trees_around()==1) {
			if(tent.up!=null && tent.up.getObj()=='f')
				tent.up
		}
	} */
	public boolean check_tuples_col() {
		boolean toRet=false;
		for(int i=0; i<worldMatrix.length; i++) { //runs over rows
			LinkedList<LinkedList<Square>> tuples = new LinkedList<>();
			LinkedList<Square> tupel=new LinkedList<>();
			int max_tent_col_i = 0;
			for(int j=0; j<worldMatrix[0].length; j++)
			{
				if(this.worldMatrix[i][j].getObj()=='f')
					tupel.add(worldMatrix[i][j]);
				else if(!tupel.isEmpty()) {
					tuples.add(tupel);
					if(tupel.size()%2==0) {
						max_tent_col_i += tupel.size()/2;
					}
					else {
						max_tent_col_i += tupel.size()/2 + 1;
					}
					tupel=new LinkedList<>();
					
				
				}
			}
			if(!tupel.isEmpty()) {
				tuples.add(tupel);
				if(tupel.size()%2==0) {
					max_tent_col_i += tupel.size()/2;
				}
				else {
					max_tent_col_i += tupel.size()/2 + 1;
				}
			}
			if(tentsInCol[i]==max_tent_col_i) {
				for(LinkedList<Square> t: tuples) {
					if(t.size()%2==1) {
						for(int j=0;j<t.size();j++) {
							if(j%2==0) {
								Square tent = t.get(j);
								tent.setObj('t');
								removeFree(tent);
								this.tentsInRow[tent.getY()]--;
								this.tentsInCol[tent.getX()]--;
								tent.eliminateAdj();
								toRet=true;
							}
							
						}
					}
					else {
						for(Square maybe_tent:t) {
						if((maybe_tent.left!=null) &&(maybe_tent.left.getObj()=='f')) {
							removeFree(maybe_tent.left);
							maybe_tent.left.setObj('g');
							toRet=true;
						}
						if((maybe_tent.right!=null) &&(maybe_tent.right.getObj()=='f')) {
							removeFree(maybe_tent.right);
							maybe_tent.right.setObj('g');
							toRet=true;
						}
					}
					}
				}
			}
			if(tentsInCol[i]==(max_tent_col_i-1)) {
				Square first = null;
				for(LinkedList<Square> t : tuples) {
					if(t.size() == 1&&first==null) {
						first = t.getFirst();
					}
					else if(t.size()==1) {
						Square secend = t.getFirst();
						if(secend.getY()-first.getY()==2) {
							Square mid = worldMatrix[first.getX()][first.getY()+1];
							if((mid.left!=null) &&(mid.left.getObj()=='f')) {
								removeFree(mid.left);
								mid.left.setObj('g');
								toRet=true;
							}
							if((mid.right!=null) &&(mid.right.getObj()=='f')) {
								removeFree(mid.right);
								mid.right.setObj('g');
								toRet=true;
							}
							
						}
						first = secend;
					}
					else if(t.size()%2==1) {
						for(int j = 0;j<t.size();++j) {
							if(j%2==1) {
								Square mid = t.get(j);
								if((mid.left!=null) &&(mid.left.getObj()=='f')) {
									removeFree(mid.left);
									mid.left.setObj('g');
									toRet=true;
								}
								if((mid.right!=null) &&(mid.right.getObj()=='f')) {
									removeFree(mid.right);
									mid.right.setObj('g');
									toRet=true;
								}
								
							}
							
						}
					}
				}
			}
			
		}
		return toRet;
				
	}
	
	public boolean check_tuples_row() {
		boolean toRet=false;
		for(int j=0; j<worldMatrix[0].length; j++) { //runs over rows
			LinkedList<LinkedList<Square>> tuples = new LinkedList<>();
			LinkedList<Square> tupel=new LinkedList<>();
			int max_tent_row_j = 0;
			for(int i=0; i<worldMatrix.length; i++)
			{
				if(this.worldMatrix[i][j].getObj()=='f')
					tupel.add(worldMatrix[i][j]);
				else if(!tupel.isEmpty()) {
					tuples.add(tupel);
					if(tupel.size()%2==0) {
						max_tent_row_j += tupel.size()/2;
					}
					else {
						max_tent_row_j += tupel.size()/2 + 1;
					}
					tupel=new LinkedList<>();
					
				
				}
			}
			if(!tupel.isEmpty()) {
				tuples.add(tupel);
				if(tupel.size()%2==0) {
					max_tent_row_j += tupel.size()/2;
				}
				else {
					max_tent_row_j += tupel.size()/2 + 1;
				}
			}
			if(tentsInRow[j]==max_tent_row_j) {
				for(LinkedList<Square> t: tuples) {
					if(t.size()%2==1) {
						for(int i=0;i<t.size();i++) {
							if(i%2==0) {
								Square tent = t.get(i);
								tent.setObj('t');
								removeFree(tent);
								this.tentsInRow[tent.getY()]--;
								this.tentsInCol[tent.getX()]--;
								tent.eliminateAdj();
								toRet=true;
							}
							
						}
					}
					else {
						for(Square maybe_tent:t) {
						if((maybe_tent.up!=null) &&(maybe_tent.up.getObj()=='f')) {
							removeFree(maybe_tent.up);
							maybe_tent.up.setObj('g');
							toRet=true;
						}
						if((maybe_tent.down!=null) &&(maybe_tent.down.getObj()=='f')) {
							removeFree(maybe_tent.down);
							maybe_tent.down.setObj('g');
							toRet=true;
						}
					}
					}
				}
			}
			if(tentsInRow[j]==(max_tent_row_j-1)) {
				Square first = null;
				for(LinkedList<Square> t : tuples) {
					if(t.size() == 1&&first==null) {
						first = t.getFirst();
					}
					else if(t.size()==1) {
						Square secend = t.getFirst();
						if(first.getX()-secend.getX()==2) {
							Square mid = worldMatrix[first.getX()+1][first.getY()];
							if((mid.up!=null) &&(mid.up.getObj()=='f')) {
								removeFree(mid.up);
								mid.up.setObj('g');
								toRet=true;
							}
							if((mid.down!=null) &&(mid.down.getObj()=='f')) {
								removeFree(mid.down);
								mid.down.setObj('g');
								toRet=true;
							}
							
						}
						first = secend;
					}
					else if(t.size()%2==1) {
						for(int i = 0;i<t.size();++i) {
							if(i%2==1) {
								Square mid = t.get(i);
								if((mid.up!=null) &&(mid.up.getObj()=='f')) {
									removeFree(mid.up);
									mid.up.setObj('g');
									toRet=true;
								}
								if((mid.down!=null) &&(mid.down.getObj()=='f')) {
									removeFree(mid.down);
									mid.down.setObj('g');
									toRet=true;
								}
								
							}
							
						}
					}
				}
			}
			
		}
		return toRet;
				
	}
	
	
	public boolean chack_tree_diagonals() {
		boolean changed=false;
		for(Tree tree: this.leftTreeArray) {
			if(tree.freeSquarse()==2) {
				if(tree.getUp()!=null&& tree.getRight()!=null) {
					if(tree.getUp().getObj()=='f' && tree.getRight().getObj()=='f')
						if(worldMatrix[tree.getRight().getX()][tree.getUp().getY()].getObj()=='f') {
							worldMatrix[tree.getRight().getX()][tree.getUp().getY()].setObj('g');
							removeFree(worldMatrix[tree.getRight().getX()][tree.getUp().getY()]);
							changed=true;
						}
				}
				if(tree.getUp()!=null&& tree.getLeft()!=null) {
					if(tree.getUp().getObj()=='f' && tree.getLeft().getObj()=='f')
						if(worldMatrix[tree.getLeft().getX()][tree.getUp().getY()].getObj()=='f') {
							worldMatrix[tree.getLeft().getX()][tree.getUp().getY()].setObj('g');
							removeFree(worldMatrix[tree.getLeft().getX()][tree.getUp().getY()]);
							changed=true;
						}
				}
				if(tree.getDown()!=null&& tree.getRight()!=null) {
					if(tree.getDown().getObj()=='f' && tree.getRight().getObj()=='f')
						if(worldMatrix[tree.getRight().getX()][tree.getDown().getY()].getObj()=='f') {
							worldMatrix[tree.getRight().getX()][tree.getDown().getY()].setObj('g');
							removeFree(worldMatrix[tree.getRight().getX()][tree.getDown().getY()]);
							changed=true;
						}
				}
				if(tree.getDown()!=null&& tree.getLeft()!=null) {
					if(tree.getDown().getObj()=='f' && tree.getLeft().getObj()=='f')
						if(worldMatrix[tree.getLeft().getX()][tree.getDown().getY()].getObj()=='f') {
							worldMatrix[tree.getLeft().getX()][tree.getDown().getY()].setObj('g');
							removeFree(worldMatrix[tree.getLeft().getX()][tree.getDown().getY()]);
							changed=true;
						}
				}
			}
		}
		return changed;
	}
	
	public boolean terminate() {
		boolean toReturn=true;
		for(int i=0; i< freeInCol.length; i++) {
			if(freeInCol[i]!=0)
				toReturn=false;
		}
		
		
		return toReturn;
	}
	
	public void solve() {
		boolean changed =true;
		while(!terminate()&&changed) {
			changed=false;
			changed=changed||onlyTentsLeft();
			if(changed)
				System.out.println("439\n"+main.wc);
			changed=changed||onlyGrassLeft();
			if(changed) 
				System.out.println("441\n"+main.wc);
			changed=changed||oneOptionLeftToTree();
			if(changed)
				System.out.println("443\n"+main.wc);
			changed=changed||squareWithNoTrees();
			if(changed)
				System.out.println("445\n"+main.wc);
			changed=changed||check_tuples_col();
			if(changed)
				System.out.println("447\n"+main.wc); 
			changed=changed||check_tuples_row();
			if(changed)
				System.out.println("449\n"+main.wc);
			changed=changed||chack_tree_diagonals();
			if(changed)
				System.out.println("451\n"+main.wc); 
		}
		if(!terminate()) {
			System.out.println("lpSolve\n"); 
		}
		
	}

	private int free_sqwers_rem() {
		// TODO Auto-generated method stub
		return 0;
	}
	public void print_world(Square [][] worldMatrix) {
		
	}
/*	public boolean correctness() {
		boolean correct = true;
		for(int i = 0; i<this.freeInRow.length;++i) {
			correct = correct&&(freeInRow[i]>=0);
			correct = correct &&(this.tentsInRow[i]>=0);
			if(freeInRow[i]==0) {
				correct = correct&&(tentsInRow[i]==0);
			}
		}
		for(int i = 0; i<this.freeInCol.length;++i) {
			correct = correct&&(freeInCol[i]>=0);
			correct = correct &&(this.tentsInCol[i]>=0);
			if(freeInCol[i]==0) {
				correct = correct&&(tentsInCol[i]==0);
			}
		}
		return correct;
	} */
	
	
	
	public void removeFree(Square free) {
		this.freeInCol[free.getX()] --;
		this.freeInRow[free.getY()]--;
		this.getLeftFree().remove(free);
	}
	public int getTreeNum() {
		return treeNum;
	}
	public void setTreeNum(int treeNum) {
		this.treeNum = treeNum;
	}
	public int[] getTentsInRow() {
		return tentsInRow;
	}
	public void setTentsInRow(int[] tentsInRow) {
		this.tentsInRow = tentsInRow;
	}
	public int[] getTentsInCol() {
		return tentsInCol;
	}
	public void setTentsInCol(int[] tentsInCol) {
		this.tentsInCol = tentsInCol;
	}
	public int[] getFreeInRow() {
		return freeInRow;
	}
	public void setFreeInRow(int[] freeInRow) {
		this.freeInRow = freeInRow;
	}
	public int[] getFreeInCol() {
		return freeInCol;
	}
	public void setFreeInCol(int[] freeInCol) {
		this.freeInCol = freeInCol;
	}
	public int getN() {
		return n;
	}
	public void setN(int n) {
		this.n = n;
	}
	public int getM() {
		return m;
	}
	public void setM(int m) {
		this.m = m;
	}
	public Square[][] getWorldMatrix() {
		return worldMatrix;
	}
	public void setWorldMatrix(Square[][] worldMatrix) {
		this.worldMatrix = worldMatrix;
	}
	public LinkedList<Tree> getLeftTreeArray() {
		return leftTreeArray;
	}
	public void setLeftTreeArray(LinkedList<Tree> leftTreeArray) {
		this.leftTreeArray = leftTreeArray;
	}
	public LinkedList<Square> getLeftFree() {
		return leftFree;
	}
	public void setLeftFree(LinkedList<Square> leftFree) {
		this.leftFree = leftFree;
	}
	public boolean is_tent_around_tide (Square tree) {
		if(tree.up!=null && tree.up.getObj()=='t' && !tree.up.tide )
			return false;
		if(tree.down!=null && tree.down.getObj()=='t' && !tree.down.tide)
			return false;
		if(tree.left!=null && tree.left.getObj()=='t' && !tree.left.tide)
			return false;
		if(tree.right!=null && tree.right.getObj()=='t' && !tree.right.tide)
			return false;
		return true;
	}
	

}
