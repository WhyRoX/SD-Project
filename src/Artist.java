import java.util.Arrays;

public class Artist {

  private final int id;
  private final String nom;
  private final String[] categories;

  public Artist(int id, String nom, String... categories) {
    this.id = id;
    this.nom = nom;
    this.categories = categories;
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

  @Override
  public String toString() {
    return "Artist{" +
            "id=" + id +
            ", nom='" + nom + '\'' +
            ", categories='" + Arrays.toString(categories) + '\'' +
            '}';
  }
}
