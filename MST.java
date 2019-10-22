package apps;

import structures.*;
import java.util.ArrayList;

public class MST {
	
	/**
	 * Initializes the algorithm by building single-vertex partial trees
	 * 
	 * @param graph Graph for which the MST is to be found
	 * @return The initial partial tree list
	 */
	public static PartialTreeList initialize(Graph graph) {
	
		/* COMPLETE THIS METHOD */
		
		PartialTreeList L = new PartialTreeList() ; // 1. Create an empty list L of partial trees.
		
		for (int i = 0 ; i < graph.vertices.length ; i++)  { // 2. Separately for each vertex v in the graph:
			
			PartialTree T = new PartialTree(graph.vertices[i]) ; // Create a partial tree T containing only v.
			
			 // Mark v as belonging to T (this will be implemented in a particular way in the code).
			
			MinHeap<PartialTree.Arc> P = T.getArcs() ; // Create a priority queue (heap) P
			
			 // associate it with T.
			
			for (Vertex.Neighbor j = graph.vertices[i].neighbors ; j != null ; j = j.next) { // Insert all of the arcs (edges) connected to v into P. The lower the weight on an arc, the higher its priority.
				
				PartialTree.Arc t = new PartialTree.Arc(graph.vertices[i], j.vertex, j.weight) ;
				P.insert(t);

			}
			
			L.append(T); // Add the partial tree T to the list L.
			
		}
		
		return L;
	}

	/**
	 * Executes the algorithm on a graph, starting with the initial partial tree list
	 * 
	 * @param ptlist Initial partial tree list
	 * @return Array list of all arcs that are in the MST - sequence of arcs is irrelevant
	 */
	public static ArrayList<PartialTree.Arc> execute(PartialTreeList ptlist) {
		
		/* COMPLETE THIS METHOD */
		
		ArrayList<PartialTree.Arc> output = new ArrayList<PartialTree.Arc>() ;
		
		while (ptlist.size() > 1) {
			
			PartialTree PTX = ptlist.remove(); // Remove the first partial tree PTX from L.
			
			MinHeap<PartialTree.Arc> PQX = PTX.getArcs() ; // Let PQX be PTX's priority queue.
			
			PartialTree.Arc a = PQX.deleteMin() ; // Remove the highest-priority arc from PQX. Say this arc is a.
			Vertex v2 = a.v2 ;
			
			MinHeap<PartialTree.Arc> tempHeap = new MinHeap<PartialTree.Arc>(PQX) ;
			
			PartialTree.Arc counterpart = new PartialTree.Arc(a.v2, a.v1, a.weight) ;
			
		while (output.contains(counterpart) || (a.v1.getRoot().equals(a.v2.getRoot()))) { // If v2 also belongs to PTX, go back to Step 4 and pick the next highest priority arc
				
				a = tempHeap.deleteMin() ;
				v2 = a.v2 ;
				counterpart = new PartialTree.Arc(a.v2, a.v1, a.weight) ;
				
			}
		
			output.add(a) ; // Report a - this is a component of the minimum spanning tree.
			
			PartialTree PTY = ptlist.removeTreeContaining(v2) ; // Find the partial tree PTY to which v2 belongs. Remove PTY from the partial tree list L. Let PQY be PTY's priority queue.
			
			Vertex m = PTY.getRoot() ;
			m.parent = PTX.getRoot() ;
			
			PTX.merge(PTY);
			
			ptlist.append(PTX);
			
		}

		return output ;
	}
}
