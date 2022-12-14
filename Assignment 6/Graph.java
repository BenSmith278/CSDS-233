import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Graph {
    class Vertex {
        private String id;
        private ArrayList<Edge> edges = new ArrayList<>();  // adjacency list
        private Vertex parent;
        private boolean encountered;

        public Vertex(String ident) {
            id = ident;
            encountered = false;
        }
    }

    class Edge {
        private int endNode;
    }

    private Vertex[] vertices;
    private int numVertices;
    private int maxNum;

    public Graph(int maximum) {
        vertices = new Vertex[maximum];
        numVertices = 0;
        maxNum = maximum;
    }

    // UPDATE EDGE ENDNODES AFTER ADD NODE IN CASE NODE IS IN FRONT
    public boolean addNode(String name) {
        if(findIndex(name) != -1)  // if vertex already exists in vertices
            return false;
        addNodeHelper(name);

        System.out.println("After Add Node " + name +": ");
        printGraph();
        return true;
    }

    private void addNodeHelper(String name) {
        // grow vertices if array "vertices" is too small
        if(numVertices >= maxNum-1) {
            Vertex[] newVertices = new Vertex[maxNum+5];
            for(int i=0; i<maxNum; i++) {
                newVertices[i] = vertices[i];
            }

            maxNum += 5;
            vertices = newVertices;
        }

        // place vertex in correct alphabetical spot in vertices
        int j = numVertices;
        for(int i=numVertices-1; i>-1; i--) {
            if(vertices[i].id.compareTo(name) > 0) {
                j--;
                vertices[i+1] = vertices[i];
            }
        }

        numVertices++;
        vertices[j] = new Vertex(name);

        // update edge endNode variables
        for(int n=0;n<numVertices;n++) {
            for(int m=vertices[n].edges.size()-1;m>=0;m--) {
                if(vertices[n].edges.get(m).endNode >= j)
                    vertices[n].edges.get(m).endNode++;
            }
        }

    }

    public boolean addNodes(String[] names) {
        for(String name : names)
            if(findIndex(name) != -1)  // if vertex exists in vertices
                return false;

        for(String name : names)
            addNodeHelper(name); 

        System.out.println("After Add Nodes: ");
        printGraph();
        return true;
    }

    private int findIndex(String name) {
        // return index of name in vertices
        for(int i=0; i<numVertices; i++) {
            if(vertices[i].id == name) {
                return i;
            }
        }

        return -1;  // if not found
    }

    public boolean addEdge(String from, String to) {
        // get indices of from and to
        int i = findIndex(from);
        int j = findIndex(to);
        if(i == -1 || j == -1)
            return false;
        addEdgeHelper(i, j);
        
        System.out.println("After Add Edge " + from + " -> " + to + ": ");
        printGraph();
        return true;
    }

    private void addEdgeHelper(int i, int j) {        
        // add an edge from i to j
        Edge newEdge = new Edge();
        newEdge.endNode = j;

        // place edge in correct spot in edges
        int n = vertices[i].edges.size();
        for(int m=n-1; m>-1; m--) {
            if(vertices[i].edges.get(m).endNode > newEdge.endNode) {
                n--;
            }
        }
        vertices[i].edges.add(n, newEdge);

        // add an edge to j for Node i
        newEdge = new Edge();
        newEdge.endNode = i;

        n = vertices[j].edges.size();
        for(int m=n-1; m>-1; m--) {
            if(vertices[j].edges.get(m).endNode > newEdge.endNode) {
                n--;
            }
        }
        vertices[j].edges.add(n, newEdge);
    }

    public boolean addEdges(String from, String[] tolist) {
        int i = findIndex(from);
        
        // check inputs
        if(i == -1)
            return false;
        for(String to : tolist) {
            if(findIndex(to) == -1)
                return false;
        }

        for(String to : tolist)
            addEdgeHelper(i, findIndex(to));

        System.out.println("After Add Edges: ");
        printGraph();
        return true;
    }

    private void removeEdgeHelper(String from, String to) {
        int fromIndex = findIndex(from);
        int toIndex = findIndex(to);

        for(int i=0; i<vertices[fromIndex].edges.size();i++) {
            if(vertices[fromIndex].edges.get(i).endNode == toIndex)
                vertices[fromIndex].edges.remove(vertices[fromIndex].edges.get(i));
        }
    }

    public boolean removeEdge(String from, String to) {
        // removes edge from -> to if it exists
        int fromIndex = findIndex(from);
        int toIndex = findIndex(to);
        if(fromIndex == -1 || toIndex == -1)
            return false;
        
        removeEdgeHelper(from, to);
        removeEdgeHelper(to, from);  // remove both edges
        System.out.println("After Remove Edge " + from + " -> " + to + ": ");
        printGraph();
        return true;
    }

    public boolean removeAllEdges(String from) {
        int fromIndex = findIndex(from);
        if(fromIndex == -1)
            return false;
        
        removeAllEdgesHelper(from);
        System.out.println("After Remove All Edges From Node " + from + ": ");
        printGraph();
        return true;
    }

    public ArrayList<String> removeAllEdgesHelper(String from) {
        int fromIndex = findIndex(from);
        ArrayList<String> removed = new ArrayList<>();
        if(fromIndex == -1)
            return removed;

        for(int i=0;i<vertices[fromIndex].edges.size();i++) {
            removed.add(vertices[vertices[fromIndex].edges.get(i).endNode].id);
            removeEdgeHelper(from, vertices[vertices[fromIndex].edges.get(i).endNode].id);
        }

        return removed;
    }

    private void removeNodeHelper(String name) {
        int i = findIndex(name);
        
        // remove all edges pointing to vertex
        removeAllEdgesHelper(name);
        for(int n=0;n<numVertices;n++) {
            removeEdgeHelper(vertices[n].id, name);
        }

        // shift vertices over
        int j;
        for(j=i;j<numVertices-1;j++) {
            vertices[j] = vertices[j+1];
        }

        // update edge endNode variables
        for(int n=0;n<numVertices-1;n++) {
            for(int m=0;m<vertices[n].edges.size();m++) {
                if(vertices[n].edges.get(m).endNode >= i)
                    vertices[n].edges.get(m).endNode--;
            }
        }

        vertices[j] = null;
        numVertices--;
    }

    public boolean removeNode(String name) {
        int i = findIndex(name);
        if(i == -1)
            return false;

        removeNodeHelper(name);
        System.out.println("After Remove Node " + name + ": ");
        printGraph();
        return true;
    }

    public boolean removeNodes(String[] nodelist) {
        for(String name : nodelist){
            removeNodeHelper(name);
        }

        return true;
    }

    public void printGraph() {
        // for each vertex, print adjacency list
        for(int i=0;i<numVertices;i++) {
            System.out.print(vertices[i].id + ": ");

            for(int j=0;j<vertices[i].edges.size();j++) {
                System.out.print(vertices[vertices[i].edges.get(j).endNode].id);

                // add comma if appropriate
                if(j < vertices[i].edges.size() - 1)
                    System.out.print(", ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public String[] DFS(String from, String to, String neighborOrder) {
        if(neighborOrder == "alphabetical")
            myDFS(from, null);
        else if(neighborOrder == "reverse")
            myReverseDFS(from, null);
        else
            return new String[0];
        String[] trav = new String[maxNum];

        for(int j=0;j<numVertices;j++) {  // reset vertices
            vertices[j].encountered = false;
        }

        String iter = to;
        int i = 0;
        while(iter != from && vertices[findIndex(iter)].parent != null) {
            trav[i] = iter;
            iter = vertices[findIndex(iter)].parent.id;
            i++;
        }
        trav[i] = from;

        System.out.println("DFS " + neighborOrder + " From " + from + " to " + to + ": ");
        while(i >= 0) {
            System.out.print(trav[i]);
            if(i > 0)
                System.out.print(", ");
            i--;
        }
        
        System.out.println();
        return trav;
    }

    private void myDFS(String from, Vertex parent) {
        int i = findIndex(from);
        if(i == -1)
            return;
        vertices[i].encountered = true;
        vertices[i].parent = parent;

        for(Edge e : vertices[i].edges) {
            if(!vertices[e.endNode].encountered) {
                myDFS(vertices[e.endNode].id, vertices[i]);
            }
        }
    }

    private void myReverseDFS(String from, Vertex parent) {
        int i = findIndex(from);
        if(i == -1)
            return;
        vertices[i].encountered = true;
        vertices[i].parent = parent;

        for(int j=vertices[i].edges.size()-1;j>=0;j--) {
            if(!vertices[vertices[i].edges.get(j).endNode].encountered) {
                myReverseDFS(vertices[vertices[i].edges.get(j).endNode].id, vertices[i]);
            }
        }
    }

    public String[] BFS(String from, String to, String neighborOrder) {
        int i = findIndex(from);
        int j = findIndex(to);
        if(i == -1 || j == -1)
            return new String[0];
        if(neighborOrder != "alphabetical" && neighborOrder != "reverse")
            return new String[0];

        String[] trav = new String[maxNum];
        myBFS(i, j, trav, neighborOrder);

        for(Vertex v : vertices) {  // reset vertices
            if(v != null)
                v.encountered = false;
        }

        String iter = to;
        int n = 0;
        while(iter != from && vertices[findIndex(iter)].parent != null) {
            trav[n] = iter;
            iter = vertices[findIndex(iter)].parent.id;
            n++;
        }
        trav[n] = from;

        System.out.println("BFS " + neighborOrder + " From " + from + " to " + to + ": ");
        while(n >= 0) {
            System.out.print(trav[n]);
            if(n > 0)
                System.out.print(", ");
            n--;
        }
        
        System.out.println();
        return trav;
    }

    private String[] myBFS(int i, int j, String[] trav, String neighborOrder) {
        vertices[i].parent = null;
        vertices[i].encountered = true;
        Queue<Vertex> q = new LinkedList<>();
        q.add(vertices[i]);

        int t = 0;
        while(!q.isEmpty()) {
            Vertex v = q.remove();
            trav[t] = v.id;

            if(v.id == vertices[j].id)
                break;

            if(neighborOrder == "alphabetical") {
                for(int n=0;n<v.edges.size();n++) {
                    Vertex w = vertices[v.edges.get(n).endNode];

                    if(!w.encountered) {
                        w.encountered = true;
                        w.parent = v;
                        q.add(w);
                    }
                }
            } else {
                for(int n=v.edges.size()-1;n>=0;n--) {
                    Vertex w = vertices[v.edges.get(n).endNode];

                    if(!w.encountered) {
                        w.encountered = true;
                        w.parent = v;
                        q.add(w);
                    }
                }
            } 
            t++;
        }

        return trav;
    }

    public String[] shortestPath(String from, String to) {
        int i = findIndex(from);
        int j = findIndex(to);
        if(i == -1 || j == -1)
            return new String[0];

        String[] trav = new String[maxNum];
        myBFS(i, j, trav, "alphabetical");

        String iter = to;
        int n = 0;
        while(iter != from && vertices[findIndex(iter)].parent != null) {
            trav[n] = iter;
            iter = vertices[findIndex(iter)].parent.id;
            n++;
        }
        trav[n] = from;

        System.out.println("Shortest Path From " + from + " to " + to + ": ");
        while(n >= 0) {
            if(trav[n] != null) {
                System.out.print(trav[n]);
                if(n > 0)
                    System.out.print(", ");
            }
            n--;
        }

        System.out.println();
        return trav;
    }

    public String[] secondShortestPath(String from, String to) {
        String[] sPath = myBFS(findIndex(from), findIndex(to), new String[maxNum], "alphabetical");
        String[] secondSPath = new String[sPath.length+5];

        for(int i=1;i<sPath.length-1;i++) {
            // for edges in sPath[i]
                // remove edge
                // find new shortest path
                // compare to current second shortest path
                    // replace if shorter
                // replace edge
        }

        // print
        return secondSPath;
    }
}