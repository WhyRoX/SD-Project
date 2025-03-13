import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

public class Graph {
    Map<Integer, Artist> artists = new HashMap<>();
    public Graph(String artistsFilePath, String mentionsFilePath) {
        File artistFile = new File(artistsFilePath);
        File mentionFile = new File(mentionsFilePath);
        readLineByLine(artistFile, line -> {
            String[] split = line.split(",");
            String[] categories = split[2].split(";");
            Artist artist = new Artist(Integer.parseInt(split[0]),split[1],categories);
            artists.put(artist.getId(), artist);
            System.out.println(artist);
        });

        readLineByLine(mentionFile,string -> {
            String[] split = string.split(",");
            Artist artist1 = artists.get(Integer.parseInt(split[0]));
            Artist artist2 = artists.get(Integer.parseInt(split[1]));
            int mentions = Integer.parseInt(split[2]);

            //System.out.println(artist1.getNom() + "est en lien avec " + artist2.getNom() +" avec "+ mentions+" mentions");
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

    public void trouverCheminLePlusCourt(String string, String string2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'trouverCheminLePlusCourt'");
    }

    public void trouverCheminMaxMentions(String string, String string2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'trouverCheminMaxMentions'");
    }


}
