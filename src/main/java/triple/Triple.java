package triple;

public class Triple {

    public final static int SELECTIVITY_BITS = 4;
    public long triples[]= new long[3];
    public static int DATA_TYPE_BITS = 64;
    //int varaibles[] = new int[3];

    public Triple(long s ,long p , long o){
        triples[0] = s ;
        triples[1] = p;
        triples[2] = o;
    }

    public void embedSelectivity( long selectivity){
        int position = DATA_TYPE_BITS - SELECTIVITY_BITS -2;
        triples[1] = (triples[1]<<position) + selectivity;
    }


    //TODO TODO TODO not tested
    public int getPredicateSelectivity(){
        int position = DATA_TYPE_BITS - SELECTIVITY_BITS -2;
        return  bitExtracted(triples[1] ,SELECTIVITY_BITS ,position);
    }

    public static int extractPredicateSelectivity(long predicate){
        int position = DATA_TYPE_BITS - SELECTIVITY_BITS -2;
        return  bitExtracted(predicate,SELECTIVITY_BITS ,position);
    }
    // Function to extract k bits from p position
    // and returns the extracted value as integer
    private static int bitExtracted(long number, int k, int position){
        return (int)(((1 << k) - 1) & (number >> (position - 1)));
    }
}
