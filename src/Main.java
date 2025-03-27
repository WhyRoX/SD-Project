public class Main {

  public static void main(String[] args) {
    Graph graph = new Graph("artists.txt", "mentions.txt");

//    long startTime1 = System.currentTimeMillis();
    graph.trouverCheminLePlusCourt("The Beatles", "Kendji Girac");
//    long endTime1 = System.currentTimeMillis();
//    System.out.println("Time taken: " + (endTime1 - startTime1) + " ms");


    System.out.println("--------------------------");

//    long startTime = System.currentTimeMillis();
    graph.trouverCheminMaxMentions("The Beatles", "Kendji Girac");
//    long endTime = System.currentTimeMillis();
//    System.out.println("Time taken: " + (endTime - startTime) + " ms");
  }
}