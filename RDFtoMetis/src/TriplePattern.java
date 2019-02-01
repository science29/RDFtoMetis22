import java.util.ArrayList;
import java.util.HashMap;

public class TriplePattern {
    final static int thisIsVariable = -1;
    public long triples[]= new long[3];
    public String stringTriple[] = new String[3] ;
    public long fixedTriples[] = new long[3];
    public HashMap<Long , Integer> variablesIndex;
    //int varaibles[] = new int[3];

    public TriplePattern(long s ,long p , long o){
        triples[0] = s ;
        triples[1] = p;
        triples[2] = o;
        fixedTriples[0] = s ;
        fixedTriples[1] = p;
        fixedTriples[2] = o;
    }



    public void findStringTriple(HashMap<Long,String> reverseDictionary) {
        this.stringTriple[0] = reverseDictionary.get(triples[0]);
        this.stringTriple[1] = reverseDictionary.get(triples[1]);
        this.stringTriple[2] = reverseDictionary.get(triples[2]);
    }
}
