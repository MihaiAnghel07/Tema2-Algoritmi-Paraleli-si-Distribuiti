import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReduceTask {
    private final String filename;
    private List<Map<Integer, Integer>> listMap;
    private ArrayList<ArrayList<String>> partialMaxWords;
    private ArrayList<String> maxWords;

    public ReduceTask(String filename) {
        this.filename = filename;
        listMap = new ArrayList<>();
        partialMaxWords = new ArrayList<>();
    }

    public void setListMap(List<Map<Integer, Integer>> listMap) {
        this.listMap = listMap;
    }

    public List<Map<Integer, Integer>> getListMap() {
        return listMap;
    }

    public String getFilename() {
        return filename;
    }

    public ArrayList<ArrayList<String>> getPartialMaxWords() {
        return partialMaxWords;
    }

    public void addPartialMaxWords(ArrayList<String> words) {
        this.partialMaxWords.add(words);
    }

    public ArrayList<String> getMaxWords() {
        return maxWords;
    }

    public void setMaxWords(ArrayList<String> maxWords) {
        this.maxWords = maxWords;
    }
}
