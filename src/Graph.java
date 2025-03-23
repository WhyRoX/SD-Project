import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.function.Consumer;

public class Graph {
    Map<String, Artist> artistes = new HashMap<>();
    Map<Integer,Artist> artistById = new HashMap<>();
    public Graph(String artistsFilePath, String mentionsFilePath) {
        File artistFile = new File(artistsFilePath);
        File mentionFile = new File(mentionsFilePath);

        readLineByLine(artistFile, line -> {
            String[] split = line.split(",");
            String[] categories = split[2].split(";");
            Artist artist = new Artist(Integer.parseInt(split[0]),split[1],categories);
            artistes.put(artist.getNom(), artist);
            artistById.put(artist.getId(),artist);
        });

        readLineByLine(mentionFile,string -> {
            String[] split = string.split(",");
            Artist artist1 = artistById.get(Integer.parseInt(split[0]));
            Artist artist2 = artistById.get(Integer.parseInt(split[1]));
            int mentions = Integer.parseInt(split[2]);
            artist1.getPoids().put(artist2,1d/mentions);
        });
    }

    private void readLineByLine(File file, Consumer<String> consumer){
        try {
            FileInputStream inputStream = new FileInputStream(file);
            Scanner scanner = new Scanner(inputStream);
            while (scanner.hasNext()){
                consumer.accept(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void trouverCheminLePlusCourt(String nomArtiste1, String nomArtiste2) {
        if(!artistes.containsKey(nomArtiste1) || !artistes.containsKey(nomArtiste2)){
            throw new IllegalArgumentException("Les arguments ne sont pas valides");
        }

        Artist artist1 = artistes.get(nomArtiste1);
        Artist artist2 = artistes.get(nomArtiste2);

        affichage(cheminBFS(artist1,artist2));
    }

    private void affichage(SommetsAvecPoidsTotal sommetsWithWeight) {
        if(sommetsWithWeight == null){
            System.out.println("Pas de chemin trouvé");
            return;
        }
        int[] sommets = sommetsWithWeight.getSommets();
        System.out.println("Longueur du chemin: "+(sommets.length-1));
        System.out.println("Poids total: "+sommetsWithWeight.getPoids());

        System.out.println("Chemin:");
        for (int i = 0; i < sommets.length; i++) {
            Artist artist = artistById.get(sommets[i]);
            System.out.println(artist.getNom()+" ("+ String.join(";",artist.getCategories()) +")");
        }
    }

private SommetsAvecPoidsTotal cheminBFS(Artist start, Artist end) {
    Queue<SommetsAvecPoidsTotal> queue = new ArrayDeque<>();
    Set<Integer> visited = new HashSet<>();
    queue.add(new SommetsAvecPoidsTotal(new int[]{start.getId()}, 0));

    while (!queue.isEmpty()) {
        SommetsAvecPoidsTotal pathWithWeight = queue.poll();
        int[] path = pathWithWeight.getSommets();
        int current = path[path.length - 1];

        if (current == end.getId()) return pathWithWeight;

        if (!visited.contains(current)) {
            visited.add(current);
            Map<Artist, Double> neighbors = artistById.get(current).getPoids();

            for (Map.Entry<Artist,Double> neighbor : neighbors.entrySet()) {
                Artist neighborArtist = neighbor.getKey();
                double weight = neighbor.getValue();
                int[] array = new int[path.length + 1];
                for (int i = 0; i < path.length; i++) {
                    array[i] = path[i];
                }
                array[path.length] = neighborArtist.getId();
                weight += pathWithWeight.getPoids();
                queue.add(new SommetsAvecPoidsTotal(array, weight));
            }
        }
    }
    return null;
}

    public static class SommetsAvecPoidsTotal {
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

    class NodeArtiste {
        private NodeArtiste parent;
        private Artist value;

        public NodeArtiste(NodeArtiste parent, Artist value) {
            this.parent = parent;
            this.value = value;
        }

        public NodeArtiste getParent() {
            return parent;
        }

        public Artist getValue() {
            return value;
        }
    }

    public void trouverCheminMaxMentions(String string, String string2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'trouverCheminMaxMentions'");
    }


}
