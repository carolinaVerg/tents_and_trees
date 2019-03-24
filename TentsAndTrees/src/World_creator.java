

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;



class World_creator {
	private Square [][] world;
	private Tree [] treeArray;
	private int num_trees;
	private int n;
	private int m;
	private int [] num_of_tentRow;
	private int [] num_of_tentCol;
	
	public World_creator(int n , int m) {
		this.n = n;
		this.m = m;
		num_trees = (n*m)/5;
		treeArray = new Tree [num_trees];
		num_of_tentRow = new int[n];
		num_of_tentCol = new int[m];	
		this.world = new Square [n][m];
		genrate_game();	
		linked_square();
	}
	private void linked_square() {
		for(int i = 0 ; i<world.length;i++) {
			for(int j = 0 ; j<world.length; j++) {
				if(i!=0) {
					world[i][j].setLeft(world[i-1][j]);
					if(j!=0)
						world[i][j].getDiagonals().add(world[i-1][j-1]);
					if(j!=m-1) world[i][j].getDiagonals().add(world[i-1][j+1]);
				}
				if(j!=0) {
					world[i][j].setUp(world[i][j-1]);
					
				}
				if(i!=n-1) {
					world[i][j].setRight(world[i+1][j]);
					if(j!=0)
						world[i][j].getDiagonals().add(world[i+1][j-1]);
					if(j!=m-1) world[i][j].getDiagonals().add(world[i+1][j+1]);
				}
				if(j!=m-1) {
					world[i][j].setDown(world[i][j+1]);
				}
			}
		}
		
	}
	private void genrate_game() {
		for(int i = 0 ; i<num_trees; i++) {
			if(put_tree(i)) {
				treeArray = new Tree [num_trees];
				num_of_tentRow = new int[n];
				num_of_tentCol = new int[m];
				this.world = new Square [n][m];
				i = -1;
			}
		}
		for(int i = 0 ; i < n; ++i) {
			for(int j = 0 ; j < m; ++j) {
				if(world[i][j]==null) {
					world[i][j] = new Square(i,j);
					}
				else if(world[i][j].getObj()=='t') {
					world[i][j].setObj('f');
				}
			}
		}
	}
	private boolean put_tree(int i) {
		int counter = 0;
		while(++counter<n*n*m*m) {
			int x = (int)(Math.random()*n);
			int y = (int)(Math.random()*m);
			Tree tree = new Tree(x,y);
			if(world[x][y]==null&&put_tents(tree)) {
				treeArray[i] = new Tree(x,y);
				world[x][y] = treeArray[i];
				return false;
				
			}
		}
		return true;
	}
	private boolean put_tents(Tree tree) {
		ArrayList<Square> where_put_tent= new ArrayList<Square>(4);
		where_put_tent.add(new Square(tree.getX()-1,tree.getY()));
		where_put_tent.add(new Square(tree.getX()+1,tree.getY()));
		where_put_tent.add(new Square(tree.getX(),tree.getY()-1));
		where_put_tent.add(new Square(tree.getX(),tree.getY()+1));
		while(!where_put_tent.isEmpty()) {
			int i = (int)(Math.random()*where_put_tent.size());
			Square p = where_put_tent.get(i);
			try {
				if(world[ p.getX()][p.getY()]==null&&isClear(p)) {
					world[p.getX()][p.getY()] =new Square (p.getX(),p.getY());
					world[p.getX()][p.getY()].setObj('t');
					++num_of_tentRow[p.getY()];
					++num_of_tentCol[p.getX()];
					return true;
				}
				where_put_tent.remove(i);
				
			}catch(IndexOutOfBoundsException e){
				where_put_tent.remove(i);
			}
			
		}
		return false;
	}
	private boolean isClear(Square p) {
		boolean ans = true;
		if(p.getX()<n-1) {
			ans = (world[ p.getX()+1][p.getY()]==null||world[ p.getX()+1][p.getY()].getObj()=='T');
			if(p.getY()<m-1&&ans){
				ans = (world[ p.getX()+1][p.getY()+1]==null || world[ p.getX()+1][p.getY()+1].getObj()=='T');
				}
			if(p.getY()>0&&ans){
				ans = (world[ p.getX()+1][p.getY()-1]==null||world[ p.getX()+1][p.getY()-1].getObj()=='T');
				}
		}
		if(p.getX()>0&&ans) {
			ans = (world[ p.getX()-1][p.getY()]==null||world[ p.getX()-1][p.getY()].getObj()=='T');
			if(p.getY()<m-1&&ans) {
				ans = (world[ p.getX()-1][p.getY()+1]==null||world[ p.getX()-1][p.getY()+1].getObj()=='T');
				}
			if(p.getY()>0&&ans) {
				ans = (world[ p.getX()-1][p.getY()-1]==null||world[ p.getX()-1][p.getY()-1].getObj()=='T');
				}
		}
		if(p.getY()<m-1&&ans) {
				ans = (world[ p.getX()][p.getY()+1]==null||world[ p.getX()][p.getY()+1].getObj()=='T');
			}
		if(p.getY()>0&&ans) {
				ans = (world[ p.getX()][p.getY()-1]==null||world[ p.getX()][p.getY()-1].getObj()=='T');
			}
		return ans;
		}
		
	public void print_World() {
		System.out.print(" |");
		for(int i = 0;i<m-1;++i) {
			System.out.print(num_of_tentCol[i]+"|");
		}
		System.out.println(num_of_tentCol[m-1]);
		for(int i = 0 ; i < n; ++i) {
			
			System.out.print(num_of_tentRow[i]+"|");
			for(int j = 0 ; j < m-1; ++j) {
				System.out.print(world[i][j].toString()+"|");
			}
			System.out.println(world[i][m-1].toString());
		}
	}
	@Override
	public String toString() {
		
		String s = " |";
		for(int i = 0;i<m-1;++i) {
			s +=num_of_tentCol[i]+"|";
		}
		s +=num_of_tentCol[m-1]+"\n";
		for(int j = 0 ; j<n;j++) {
			s += num_of_tentRow[j] + "|";
			for (int i = 0;i<m;i++) {
				s += world[i][j].getObj() + "|";
		
			}
			s += "\n";
		}
		return s;
	}
	public Square[][] getWorld() {
		return world;
	}
	public void setWorld(Square[][] world) {
		this.world = world;
	}
	public Tree[] getTreeArray() {
		return treeArray;
	}
	public void setTreeArray(Tree[] treeArray) {
		this.treeArray = treeArray;
	}
	public int getNum_trees() {
		return num_trees;
	}
	public void setNum_trees(int num_trees) {
		this.num_trees = num_trees;
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
	public int[] getNum_of_tentRow() {
		return num_of_tentRow;
	}
	public void setNum_of_tentRow(int[] num_of_tentRow) {
		this.num_of_tentRow = num_of_tentRow;
	}
	public int[] getNum_of_tentCol() {
		return num_of_tentCol;
	}
	public void setNum_of_tentCol(int[] num_of_tentCol) {
		this.num_of_tentCol = num_of_tentCol;
	}
	
	
}
