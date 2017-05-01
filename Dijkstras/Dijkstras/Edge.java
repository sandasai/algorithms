
public class Edge implements Comparable<Edge> {
	public Integer origin;
	public Integer v;
	public Integer l;
	
	Edge(Integer v, Integer l, Integer origin) {
		if (v < 0 || l < 0) 
			throw new IllegalArgumentException();
		this.v = v;
		this.l = l;
		this.origin = origin;
	}
	public String toString() {
		return v + ";" + l + ";from:" + origin;
	}
	
	/**
	 * We want to compare edges by their edge lengths for use in a heap in Dijkstras
	 */
	public int compareTo(Edge edge) {
		if (this.l < edge.l)
			return -1;
		else if (this.l > edge.l)
			return 1;
		return 0;
	}
}
