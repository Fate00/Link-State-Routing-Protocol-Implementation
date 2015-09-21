package test;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Testcase {

	static String str;

	public static void main(String[] args) throws Exception {
		 
		boolean flag = true;

		while (flag) {
			System.out.println("******************MENU******************");
			System.out.println("Please make your choice:");
			System.out.println(" 1-Load File.");
			System.out.println(" 2-Build Routing Table For Selected Router.");
			System.out.println(" 3-Out Optimal Path and Minimum Cost.");
			System.out.println(" 4-Exit.");
			System.out.println("****************************************");
			
			Scanner sc_choice = new Scanner(System.in);
			int num = sc_choice.nextInt();
			
			switch (num) {
			case 1:
				System.out.println("Please load original routing table data file:");
				Scanner sc_1 = new Scanner(System.in);
				str = sc_1.nextLine();
				showRoutingtable(str);
				break;
				
			case 2:
				System.out.println("Please select a router:");
				Scanner sc_4 = new Scanner(System.in);
				int num_R = sc_4.nextInt();
				num_R -= 1;
				System.out.println("The routing table for router " + (num_R+1) + " is: ");
				System.out.println("Start End Nexthop");
				Display(num_R);
				break;
				
			case 3:
				System.out.println("Please input the source router number: ");
				Scanner sc_3 = new Scanner(System.in);
				int a = sc_3.nextInt() - 1;
				System.out.println("Please input the destination router number: ");
				int b = sc_3.nextInt() - 1;
				int cost = getShorestPath(getTable(str), a, b);
				System.out.println("The total cost is: " + cost);
				break;
				
			case 4:
				flag = false;
				break;
			}
		}
	}

	public static int getShorestPath(int[][] table, int start, int end) {
		 
		boolean[] Label = new boolean[table[0].length];// is label
		int[] subscript_set = new int[table[0].length];
		// To save all the sets of nodes which are labeled in a time order, actually it's a stack which is represented by a array
		int i_count = 0;// The order and number of the labeled vertex
		int[] distance = table[start].clone();// The original distance from V0 to other nodes
		int index = start;// From the first node
		subscript_set[i_count] = index;// Save all the labeled subscript to the subscript set 
		
		int[] pathIndex = new int[table[0].length];// Record the previous node number of node number of the shortest path tree 
		for(int i = 0; i < pathIndex.length; i++) {// Initialize
			pathIndex[i] = start;
		}
		Label[index] = true;
		while(i_count < table[0].length) {
			// Labal v_start, find the nearest node to v_start in row W[start][0]
			int min = Integer.MAX_VALUE;
			// Find the subscript of shortest distance to v_start
			for(int i = 0; i < distance.length; i++) {
				if(!Label[i] && distance[i] != -1 && i != index) {
					// If this node can be connected and it's not labeled
					if(distance[i] < min) {
						min = distance[i];
						index = i;// Record this subscript
					}
				}
			}
			if(index == end) {// Terminate if find the current node
				break;
			}
			Label[index] = true;// Label the node
			i_count++;
			subscript_set[i_count] = index;// Save all the labeled subscript to the subscript set
			// After add a node, update all the distance not belongs to N
			for(int i = 0; i < distance.length; i++) {
				// If this node can't be accessible previously, and the new node can connect to this node which is not in N
				if(distance[i] == -1 && table[index][i] != -1 && !Label[i]) {// If it isn't accessible before, but now it can
					distance[i] = distance[index] + table[index][i];
					pathIndex[i] = index;// Update the previous node of the current one in the shortest path
				} else if(table[index][i] != -1 && distance[index] + table[index][i] < distance[i]) {
					// If it's accessible before, but the new distance is more shorter, then update
					distance[i] = distance[index] + table[index][i];
					pathIndex[i] = index;// Update the previous node of the current one in the shortest path
				}
			}// all the shortest distances form the every node to the first one are update
		}
		// If the traversal is complete, all the shortest distances form the every node to the first one are saved in distance
		
		System.out.print("The shorest path from " + (start + 1) + " to " + (end + 1) + " is ");
		findShorestPath(pathIndex, start, end, distance.length);
		System.out.print("\n");
		return (distance[end] - distance[start]);
	}

	public static void findShorestPath(int[] pathIndex, int start, int end, int length) {
		 
		int[] path = new int[length];// Difine the array which saving the path
		int i = end;
		path[0] = i;
		int j = 1;
		
		while(pathIndex[i] != start) {
			i = pathIndex[i];
			path[j] = i;// Traversal reversely
			j++;
		}
		
		path[j] = start;
		for(int k = j; k > 0; k--) {
			System.out.print("R" + (path[k] + 1) + "-");
		}
		
		System.out.print("R" + (path[0] + 1));
	}

	public static void Display(int num_R) throws IOException {
		 
		getRoutingTable(str);
		String[][] dis = new String[1][3];
		for(int next_R = 0; next_R < Global.j; next_R++) {
			if(next_R == num_R) {
				next_R = next_R + 1;
			}
			dis[0][0] = "R" + (num_R+1);
			dis[0][1] = "R" + (next_R+1);
			dis[0][2] = Multi_d(getTable(str), num_R, next_R);        //i start node j end node
			System.out.println(" " + dis[0][0] + "   " + dis[0][1] + "    " + dis[0][2]);
			
		}
	}

	public static String Multi_d(int[][] table, int start, int end) {
		 
		boolean[] Label = new boolean[table[0].length];// isLabeled
		int[] subscript_set = new int[table[0].length];
		// To save all the sets of nodes which are labeled in a time order, actually it's a stack which is represented by a array
		int i_count = 0;// The order and number of the labeled vertex
		int[] distance = table[start].clone();// The original distance from V0 to other nodes
		int index = start;// From the first node
		subscript_set[i_count] = index;// Save all the labeled subscript to the subscript set
		
		int[] pathIndex = new int[table[0].length];// Record the previous node number of node number of the shortest path tree
		for(int i = 0; i < pathIndex.length; i++) {// Initialize
			pathIndex[i] = start;
		}
		Label[index] = true;
		while(i_count < table[0].length) {
			// Labal v_start, find the nearest node to v_start in row W[start][0]
			int min = Integer.MAX_VALUE;
			// Find the subscript of shortest distance to v_start
			for(int i = 0; i < distance.length; i++) {
				if(!Label[i] && distance[i] != -1 && i != index) {
					// If this node can be connected and it's not labeled
					if(distance[i] < min) {
						min = distance[i];
						index = i;// Record this subscript
					}
				}
			}
			if(index == end) {// Terminate if find the current node
				break;
			}
			Label[index] = true;// Label the node
			i_count++;
			subscript_set[i_count] = index;// Save all the labeled subscript to the subscript set
			// After add a node, update all the distance not belongs to N
			for(int i = 0; i < distance.length; i++) {
				// If this node can't be accessible previously, and the new node can connect to this node which is not in N
				if(distance[i] == -1 && table[index][i] != -1 && !Label[i]) {// If it isn't accessible before, but now it can
					distance[i] = distance[index] + table[index][i];
					pathIndex[i] = index;// Update the previous node of the current one in the shortest path
				} else if(table[index][i] != -1 && distance[index] + table[index][i] < distance[i]) {
					// If it's accessible before, but the new distance is more shorter, then update
					distance[i] = distance[index] + table[index][i];
					pathIndex[i] = index;// Update the previous node of the current one in the shortest path
				}
			}// all the shortest distances form the every node to the first one are update
		}
		// If the traversal is complete, all the shortest distances form the every node to the first one are saved in distance
		
		
		return FindNextHop(pathIndex, start, end, distance.length);
	
	}
	

	public static String FindNextHop(int[] pathIndex, int start, int end, int length) {
		 
		int[] path = new int[length];// Define the array to save path
		int i = end;
		path[0] = i;
		int j = 1;
		while (pathIndex[i] != start) {
			i = pathIndex[i];
			path[j] = i;// Traversal reversely
			j++;
		}
		path[j] = start;
		int a=0;
		for (int k = j; k > 0; k--) {
			a++;
			if(a==2)
				return ("R"+(path[k]+1));
		};		
		if(a==1) {
			return "-";
		}
		 return null;
	}

	public static int[][] getTable(String textname) throws IOException {
		 
		String path = "/Users/WayneHu/Documents/workspace/CS542_Project/";
		String file = path + textname;
		BufferedReader br = new BufferedReader(new FileReader(file));
		int row = 0, len = 0;
		String str = null;
		while ((str = br.readLine()) != null) {
			row++; 
			String s1[] = str.split(" ");
			len = s1.length;

		}
		int table[][] = new int[row][len];

		int i = 0, j = 0;
		BufferedReader buff = new BufferedReader(new FileReader(file));
		while ((str = buff.readLine()) != null) {
			String[] s1 = str.split("\\s{1,}");

			for (i = 0; i < len; i++) {
				table[j][i] = Integer.parseInt(s1[i]);
			}
			j++;
		}
		br.close();
		return table;
	}

	public static void showRoutingtable(String textname) throws IOException {
		
		String path = "/Users/WayneHu/Documents/workspace/CS542_Project/";
		String file = path + textname;
		BufferedReader br = new BufferedReader(new FileReader(file)); // Read Files
		String str = null;
		while ((str = br.readLine()) != null) {
			System.out.println(str);
		}
		br.close();
	}	
	
	public static void getRoutingTable(String textName) {
		 
		int[][] originalTable = new int[100][100];
		String path = "/Users/WayneHu/Documents/workspace/CS542_Project/";
		String file = path + textName;
		
		try {
			BufferedReader input = new BufferedReader(new FileReader(file));
			String text = null;
			try {
				while((text = input.readLine()) != null) {
					String[] str = text.split("\\s{1,}");
					for (Global.j = 0; Global.j < str.length; Global.j++) {
						originalTable[Global.i][Global.j] = Integer.parseInt(str[Global.j]);
					}
					Global.i++;
				}
				
			} catch (IOException e) {
				 
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			 
			e.printStackTrace();
		}

	}
	
}

class Global {
	public static int i = 0;
	public static int j = 0;
}


