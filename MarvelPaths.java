package hw5;
 
import hw4.Edge;
import hw4.Graph;

import java.util.*;
import java.io.*;

public class MarvelPaths{

	public static Graph<String, String> graph;
	
	public MarvelPaths(){
		graph=new Graph<String, String>();
	}

	public MarvelPaths(String filename){
		graph=new Graph<String, String>();
		createNewGraph(filename);
	}

	public void createNewGraph(String filename){
		try {
			Map<String, Set<String>> charsInBooks = new HashMap<String,Set<String>>();
			Set<String> chars=new HashSet<String>();
			MarvelParser.readData(filename,charsInBooks,chars);
			//System.out.println("Read "+chars.size()+" characters who appear in "+charsInBooks.keySet().size() +" books.");
			
			for(String s:chars){
				graph.addNode(s);
			}

			for (Map.Entry<String, Set<String>> entry : charsInBooks.entrySet())
			{
				//System.out.println(entry.getKey() + "/" + entry.getValue());
				for(String s : entry.getValue()){
					for(String t : entry.getValue()){
						if(!s.equals(t)){
							graph.addEdge(s, t, entry.getKey());
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String findPath(String node1, String node2){
		//Node<String, String> start=graph.getNode(node1);
		//Node<String, String> dest=graph.getNode(node2);
		Queue<String> Q = new LinkedList<String>();
		Map<String, ArrayList<String>> M = new HashMap<String, ArrayList<String>>();
		Q.add(node1);
		ArrayList<String> fresh=new ArrayList<String>();
		M.put(node1, fresh);
		StringBuilder stringbuilder=new StringBuilder();
		boolean exists1=new Boolean(true);
		boolean exists2=new Boolean(true);
		if(!graph.nodeIn(node1)){
			stringbuilder.append("unknown character "+node1+"\n");
			exists1=false;
		}
		if(!graph.nodeIn(node2)&&!node1.equals(node2)){
			stringbuilder.append("unknown character "+node2+"\n");
			exists2=false;
		}
		if(exists1&&exists2){
			stringbuilder.append("path from "+node1+" to "+node2+":\n");
			if(!node1.equals(node2)){
				while(Q.size()>0){
					String n=Q.poll();
					if(n.equals(node2)){
						Iterator<String> itr1=M.get(node2).listIterator();
						Iterator<String> itr2=M.get(node2).listIterator();
						itr2.next();
						while(itr2.hasNext()){
							String n1=itr1.next();
							String n2=itr2.next();
							stringbuilder.append(n1+" to "+n2+" via "+graph.getEdge(n1, n2)+"\n");
						}
						String n1=itr1.next();
						stringbuilder.append(n1+" to "+node2+" via "+graph.getEdge(n1, node2)+"\n");
						String result=stringbuilder.toString();
						return result;
					}
					HashSet<Edge<String, String>> edges=graph.getEdges(n);
					ArrayList<String> strings=new ArrayList<String>();
					Iterator<Edge<String, String>> itr=edges.iterator();
					while(itr.hasNext()){
						String s=itr.next().getDest();
						strings.add(s);
					}
					Collections.sort(strings);
					for(String x:strings){
						if(!M.containsKey(x)){
							ArrayList<String> p=new ArrayList<String>();
							p.clear();
							if(M.containsKey(n)){
								for(String y:M.get(n)){
									p.add(y);
								}
							}
							p.add(n);
							M.put(x, p);
							Q.add(x);
						}
					}
				}
				stringbuilder.append("no path found\n");
			}
		}
		String result=stringbuilder.toString();
		return result;
	}
/*
	public static void main(String[] arg) {
		String file = arg[0];
		MarvelPaths marvel=new MarvelPaths(file);
	}
*/
}
