import java.util.*;

public class Artist {

  private final int id;
  private final String nom;
  private final String[] categories;
  private final Map<Artist, Integer> poids;
  private final Set<Artist> artisteAdjacent = new HashSet<>();

  public Artist(int id, String nom, String... categories) {
    this.id = id;
    this.nom = nom;
    this.categories = categories;
    this.poids = new HashMap<>();
  }


  public int getId() {
    return id;
  }

  public String getNom() {
    return nom;
  }

  public String[] getCategories() {
    return categories;
  }

  public Map<Artist, Integer> getPoids() {
    return poids;
  }

  public Set<Artist> getArtisteAdjacent() {
    return artisteAdjacent;
  }

  @Override
  public String toString() {
    return "Artist{" +
            "id=" + id +
            ", nom='" + nom + '\'' +
            ", categories=" + Arrays.toString(categories) +
            ", poids=" + poids.entrySet().stream().map(e -> e.getKey().getId()+":"+e.getValue()).toList() +
            ", artisteAdjacent=" + artisteAdjacent +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    Artist artist = (Artist) o;
    return id == artist.id;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
