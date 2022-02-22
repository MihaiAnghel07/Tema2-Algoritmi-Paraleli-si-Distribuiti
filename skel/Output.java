import java.util.ArrayList;
/*
* Aceasta clasa ajuta la formarea output-ului cu informatia necesara
*/
public class Output {
    private final String filename;
    private final String rank;
    private final ArrayList<String> maxLenWords;

    public Output(String filename, String rank, ArrayList<String> maxLenWords) {
        this.filename = filename;
        this.rank = rank;
        this.maxLenWords = maxLenWords;
    }

    public String getFilename() {
        return filename;
    }

    public String getRank() {
        return rank;
    }

    public ArrayList<String> getMaxLenWords() {
        return maxLenWords;
    }
}
