public class Artist {
   private final int id;
   private final String nom;
   private final String categorie;

    public Artist(int id, String nom, String categorie) {
         this.id = id;
         this.nom = nom;
         this.categorie = categorie;
    }

   @Override
   public String toString() {
      return "Artist(id=" + id + ", nom=" + nom + ", categorie=" + categorie + ")";
   }

   public int getId() {
      return id;
   }

   public String getNom() {
      return nom;
   }

   public String getCategorie() {
      return categorie;
   }
}
