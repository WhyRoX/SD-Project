import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Graph {

    public Graph(String artistsFilePath, String mentionsFilePath) {
        File artistFile = new File(artistsFilePath);
        File mentionFile = new File(mentionsFilePath);
        List<Artist> a = new ArrayList<>();
        readLineByLine(artistFile, line -> {
            String[] split = line.split(",");
            Artist artist = new Artist(Integer.parseInt(split[0]),split[1],split[2]);
            a.add(artist);
        });

        System.out.println(a.get(1000).toString());

    }

    private void readLineByLine(File file, Consumer<String> consumer){
        try {
            FileInputStream inputStream = new FileInputStream(file);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            String line;
            while (bufferedInputStream.available() > 0) {
                line = "";
                char c;
                while ((c = (char) bufferedInputStream.read()) != '\n') {
                    line += c;
                }
                consumer.accept(line);
            }

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
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
