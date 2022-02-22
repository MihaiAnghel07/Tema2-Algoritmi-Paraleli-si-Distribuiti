import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

public class MapTask {
    private final int offset;
    private final int fragmentSize;
    private final String filename;
    private final String filePath;
    // fiecare MapTask va contine o lista de cuvinte maximale
    private ArrayList<String> maxWords;
    // dictionar ce va tine perechile {lungime:nr_aparitii}
    private Map<Integer, Integer> map;

    public MapTask(String filename, int offset, int fragmentSize, String filePath) {
        this.filename = filename;
        this.offset = offset;
        this.fragmentSize = fragmentSize;
        this.filePath = filePath;
        this.maxWords = new ArrayList<>();
        this.map = new Hashtable();
    }

    public int getOffset() {
        return offset;
    }

    public int getFragmentSize() {
        return fragmentSize;
    }

    public String getFilename() {
        return filename;
    }

    public String getFilePath() {
        return filePath;
    }

    public ArrayList<String> getMaxWords() {
        return maxWords;
    }

    public void setMaxWords(ArrayList<String> maxWords) {
        this.maxWords = maxWords;
    }

    public Map<Integer, Integer> getMap() {
        return map;
    }

    public void setMap(Map<Integer, Integer> map) {
        this.map = map;
    }
}
