import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.function.Consumer;

public class Graph {

  Map<String, Artist> artistes = new HashMap<>();
  Map<Integer, Artist> artistById = new HashMap<>();

  public Graph(String artistsFilePath, String mentionsFilePath) {
    File artistFile = new File(artistsFilePath);
    File mentionFile = new File(mentionsFilePath);

    readLineByLine(artistFile, line -> {
      String[] split = line.split(",");
      String[] categories = split[2].split(";");
      Artist artist = new Artist(Integer.parseInt(split[0]), split[1], categories);
      artistes.put(artist.getNom(), artist);
      artistById.put(artist.getId(), artist);
    });

    readLineByLine(mentionFile, string -> {
      String[] split = string.split(",");
      Artist artist1 = artistById.get(Integer.parseInt(split[0]));
      Artist artist2 = artistById.get(Integer.parseInt(split[1]));
      int mentions = Integer.parseInt(split[2]);
      artist1.getPoids().put(artist2, 1d / mentions);
    });
  }

  private void readLineByLine(File file, Consumer<String> consumer) {
    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
      String s;
      while ((s = reader.readLine()) != null) {
        consumer.accept(s);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void trouverCheminLePlusCourt(String nomArtiste1, String nomArtiste2) {
    if (!artistes.containsKey(nomArtiste1) || !artistes.containsKey(nomArtiste2)) {
      throw new IllegalArgumentException("Les arguments ne sont pas valides");
    }

    Artist artist1 = artistes.get(nomArtiste1);
    Artist artist2 = artistes.get(nomArtiste2);
    SommetsAvecPoidsTotal sommetsWithWeight = cheminBFS(artist1, artist2);
    if (sommetsWithWeight == null) {
      throw new RuntimeException(
          "Aucun chemin entre " + artist1.getNom() + " et " + artist2.getNom());
    }
    affichage(sommetsWithWeight);
  }

  private void affichage(SommetsAvecPoidsTotal sommetsWithWeight) {
    if (sommetsWithWeight == null) {
      throw new IllegalArgumentException("Les arguments ne sont pas valides");
    }
    int[] sommets = sommetsWithWeight.getSommets();
    System.out.println("Longueur du chemin: " + (sommets.length - 1));
    System.out.println("Poids total: " + sommetsWithWeight.getPoids());

    System.out.println("Chemin:");
    for (int i = 0; i < sommets.length; i++) {
      Artist artist = artistById.get(sommets[i]);
      System.out.println(artist.getNom() + " (" + String.join(";", artist.getCategories()) + ")");
    }
  }

  private SommetsAvecPoidsTotal cheminBFS(Artist start, Artist end) {
    Queue<SommetsAvecPoidsTotal> queue = new ArrayDeque<>();
    Set<Integer> visited = new HashSet<>();
    queue.add(new SommetsAvecPoidsTotal(new int[]{start.getId()}, 0));

    while (!queue.isEmpty() && queue.peek().getSommets()[queue.peek().getSommets().length - 1] != end.getId()) {
      SommetsAvecPoidsTotal pathWithWeight = queue.poll();
      int[] path = pathWithWeight.getSommets();
      int current = path[path.length - 1];

      if (!visited.contains(current)) {
        visited.add(current);
        Map<Artist, Double> neighbors = artistById.get(current).getPoids();

        for (Map.Entry<Artist, Double> neighbor : neighbors.entrySet()) {
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
    if (!queue.isEmpty()) {
      return queue.peek();
    }
    return null;
  }

  public void trouverCheminMaxMentions(String nomArtiste1, String nomArtiste2) {
    if (!artistes.containsKey(nomArtiste1) || !artistes.containsKey(nomArtiste2)) {
      throw new IllegalArgumentException("Les arguments ne sont pas valides");
    }

    Artist start = artistes.get(nomArtiste1);
    Artist end = artistes.get(nomArtiste2);
    SommetsAvecPoidsTotal path = cheminDijkstra(start, end);

    if (path == null) {
      throw new RuntimeException("Aucun chemin entre " + start.getNom() + " et " + end.getNom());
    }
    affichage(path);
  }

  private static class DistanceNode {
    int id;
    double distance;

    DistanceNode(int id, double distance) {
      this.id = id;
      this.distance = distance;
    }
  }

  private SommetsAvecPoidsTotal cheminDijkstra(Artist start, Artist end) {
    Map<Integer, Double> distances = new HashMap<>();
    Map<Integer, Integer> previous = new HashMap<>();
    Set<Integer> visited = new HashSet<>();

    PriorityQueue<DistanceNode> queue = new PriorityQueue<>(Comparator.comparingDouble(n -> n.distance));

    distances.put(start.getId(), 0.0);
    queue.add(new DistanceNode(start.getId(), 0.0));

    while (!queue.isEmpty() && !visited.contains(end.getId())) {
      DistanceNode currentNode = queue.poll();
      int currentId = currentNode.id;

      if (visited.contains(currentId)) continue;
      visited.add(currentId);

      Artist currentArtist = artistById.get(currentId);
      for (Map.Entry<Artist, Double> entry : currentArtist.getPoids().entrySet()) {
        Artist neighbor = entry.getKey();
        int neighborId = neighbor.getId();
        double weight = entry.getValue();

        if (visited.contains(neighborId)) continue;

        double newDist = currentNode.distance + weight;
        double currentNeighborDist = distances.getOrDefault(neighborId, Double.POSITIVE_INFINITY);

        if (newDist < currentNeighborDist) {
          distances.put(neighborId, newDist);
          previous.put(neighborId, currentId);
          queue.add(new DistanceNode(neighborId, newDist));
        }
      }
    }

    if (!visited.contains(end.getId())) {
      return null;
    }

    return reconstructChemin(previous, start.getId(), end.getId(), distances.get(end.getId()));
  }

  private SommetsAvecPoidsTotal reconstructChemin(Map<Integer, Integer> previous, int start,
      int end, double totalWeight) {
    List<Integer> path = new ArrayList<>();

    // Start from end and work backwards
    for (Integer current = end; current != null; current = previous.get(current)) {
      path.add(0, current);
    }

    // Convert list to array
    int[] pathArray = new int[path.size()];
    for (int i = 0; i < path.size(); i++) {
      pathArray[i] = path.get(i);
    }

    return new SommetsAvecPoidsTotal(pathArray, totalWeight);
  }
}
