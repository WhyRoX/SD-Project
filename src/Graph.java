import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
    try {
      FileInputStream inputStream = new FileInputStream(file);
      Scanner scanner = new Scanner(inputStream);
      while (scanner.hasNext()) {
        consumer.accept(scanner.nextLine());
      }
    } catch (FileNotFoundException e) {
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

    while (!queue.isEmpty()) {
      SommetsAvecPoidsTotal pathWithWeight = queue.poll();
      int[] path = pathWithWeight.getSommets();
      int current = path[path.length - 1];

      if (current == end.getId()) {
        return pathWithWeight;
      }

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
    return null;
  }

  public void trouverCheminMaxMentions(String nomArtiste1, String nomArtiste2) {
    if (!artistes.containsKey(nomArtiste1) || !artistes.containsKey(nomArtiste2)) {
      throw new IllegalArgumentException("Les arguments ne sont pas valides");
    }

    Artist start = artistes.get(nomArtiste1);
    Artist end = artistes.get(nomArtiste2);
    SommetsAvecPoidsTotal path = dijkstra(start, end);

    if (path == null) {
      throw new RuntimeException("Aucun chemin entre " + start.getNom() + " et " + end.getNom());
    }
    affichage(path);
  }

  private SommetsAvecPoidsTotal dijkstra(Artist start, Artist end) {
    // Track distances and previous nodes
    Map<Integer, Double> distances = new HashMap<>();
    Map<Integer, Integer> previous = new HashMap<>();
    Set<Integer> settled = new HashSet<>();

    // Priority queue with nodes ordered by distance
    PriorityQueue<Integer> queue = new PriorityQueue<>(
        Comparator.comparingDouble(distances::get)
    );

    // Initialize distances
    for (Integer id : artistById.keySet()) {
      distances.put(id, Double.POSITIVE_INFINITY);
    }

    // Set start distance to 0
    distances.put(start.getId(), 0.0);
    queue.add(start.getId());

    while (!queue.isEmpty()) {
      Integer currentId = queue.poll();

      // Skip if already processed
      if (settled.contains(currentId)) {
        continue;
      }

      // If we've reached the target
      if (currentId.equals(end.getId())) {
        return reconstructChemin(previous, start.getId(), end.getId(), distances.get(end.getId()));
      }

      // Mark as processed
      settled.add(currentId);

      // Process neighbors
      Artist currentArtist = artistById.get(currentId);
      for (Map.Entry<Artist, Double> neighborEntry : currentArtist.getPoids().entrySet()) {
        Artist neighborArtist = neighborEntry.getKey();
        int neighborId = neighborArtist.getId();
        double weight = neighborEntry.getValue();

        // Skip if neighbor already processed
        if (settled.contains(neighborId)) {
          continue;
        }

        double newDistance = distances.get(currentId) + weight;

        // Update if found better path
        if (newDistance < distances.get(neighborId)) {
          distances.put(neighborId, newDistance);
          previous.put(neighborId, currentId);
          queue.add(neighborId);
        }
      }
    }

    return null; // No path found
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
