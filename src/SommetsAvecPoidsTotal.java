import java.util.Arrays;

public class SommetsAvecPoidsTotal {
    private final int[] sommets;
    private final double poids;

    public SommetsAvecPoidsTotal(int[] sommets, double poids) {
        this.sommets = sommets;
        this.poids = poids;
    }

    public int[] getSommets() {
        return sommets;
    }

    public double getPoids() {
        return poids;
    }

    @Override
    public String toString() {
        return "CheminAvecPoids{" +
                "sommets=" + Arrays.toString(sommets) +
                ", poids=" + poids +
                '}';
    }
}
