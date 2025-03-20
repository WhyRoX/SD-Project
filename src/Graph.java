import org.w3c.dom.Node;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.function.Consumer;

public class Graph {
    Map<String, Artist> artistes = new HashMap<>();
    public Graph(String artistsFilePath, String mentionsFilePath) {
        File artistFile = new File(artistsFilePath);
        File mentionFile = new File(mentionsFilePath);
        Map<Integer,Artist> artistById = new HashMap<>();
        readLineByLine(artistFile, line -> {
            String[] split = line.split(",");
            String[] categories = split[2].split(";");
            Artist artist = new Artist(Integer.parseInt(split[0]),split[1],categories);
            artistes.put(artist.getNom(), artist);
            artistById.put(artist.getId(),artist);
            System.out.println(artist);
        });

        readLineByLine(mentionFile,string -> {
            String[] split = string.split(",");
            Artist artist1 = artistById.get(Integer.parseInt(split[0]));
            Artist artist2 = artistById.get(Integer.parseInt(split[1]));
            int mentions = Integer.parseInt(split[2]);
            artist1.getPoids().put(artist2,mentions);
        });

        int i = 0;
        for (Artist a : artistes.values()){
            System.out.println("for logic");
            if(i++ >= 1000)
                break;
            System.out.println(a);
        }
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
        // TODO Auto-generated method stub
        if(!artistes.containsKey(nomArtiste1) || !artistes.containsKey(nomArtiste2)){
            throw new IllegalArgumentException("Les arguments ne sont pas valides");
        }

        Artist artist1 = artistes.get(nomArtiste1);
        Artist artist2 = artistes.get(nomArtiste2);


        Set<Integer> visited = new HashSet<>();

        Queue<String> sommets = new ArrayDeque<>();

        sommets.add(artist1.getNom());
        recursifCheminLePlusCourt(artist1, artist2, sommets,visited,null);

        System.out.println(sommets);
        System.out.println(visited);
    }
    int i = 0;
    List<Queue<String>> paths = new ArrayList<>();
    private Queue<String> recursifCheminLePlusCourt(Artist artist, Artist cible, Queue<String> sommets, Set<Integer> visited, NodeArtiste parent) {
        visited.add(artist.getId());

        if (artist.equals(cible)) {
            Queue<String> path = new ArrayDeque<>(sommets);
            paths.add(path);
            if(artistes.size() == visited.size()){
                return paths.stream().min(Comparator.comparingInt(Queue::size)).orElse(path);
            }
            return path;
        }

        for (Artist a : artist.getArtisteAdjacent()) {
            if (!visited.contains(a.getId())) {
                sommets.add(a.getNom());
                recursifCheminLePlusCourt(a, cible, sommets, visited, new NodeArtiste(parent, a));
                sommets.remove(a.getNom());
            }
        }
        return null;
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
