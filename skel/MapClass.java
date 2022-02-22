import java.io.*;
import java.util.*;

public class MapClass implements Runnable {
    private final ArrayList<MapTask> tasks;
    private final int threadId;
    private final int workers;

    public MapClass(ArrayList<MapTask> tasks, int threadId, int workers) {
        this.tasks = tasks;
        this.threadId = threadId;
        this.workers = workers;
    }

    @Override
    public void run() {
        /*
        * Se calculeaza capetele start si end pentru fiecare thread
        */
        int start = (int)(threadId * Math.ceil(tasks.size() / workers));
        int end = (int)Math.min((threadId + 1) * Math.ceil(tasks.size() / workers), tasks.size());
        if (threadId == workers - 1 && end != tasks.size()) {
            end = tasks.size();
        }

        /*
        * Fiecare thread prelucreaza un numar de task-uri de tip map
        */
        for (int i = start; i < end; i++) {
            File f = new File(tasks.get(i).getFilePath());
            Map<Integer, Integer> map = new Hashtable();
            ArrayList<String> wordsList = new ArrayList<>();
            ArrayList<String> maxWords = new ArrayList<>();
            int maxLength = 0;

            try {
                FileReader fileReader = new FileReader(f);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                int ret;
                int j = 0;
                int counter = 0;
                // initializez cu spatiu pentru a intampina situatia cand cursorul este la inceputul fisierului
                char previousLetter = ' ';
                String word = "";
                String regex = ";:/?~\\.,><`[]{}()!@#$%^&-_+'=*\"| \t\r\n";
                /*
                * Se citeste din fisier fragmentul aferent task-ului respectandu-se ideea conform careia
                * daca inceputul fragmentului se afla in mijlocul unui cuvant se ignora acel cuvant partial
                * In acelasi timp, se extrag cuvintele din fragment si se pun intr-o lista
                */
                while((ret = bufferedReader.read()) != -1 && counter < tasks.get(i).getFragmentSize()) {
                    if (j == tasks.get(i).getOffset() - 1) {
                        previousLetter = (char) ret;
                    }
                    if (j >= tasks.get(i).getOffset()) {
                        char character = (char) ret;
                        if (!regex.contains(String.valueOf(character))) {
                            word = word.concat(String.valueOf(character));
                        } else {
                            if (!word.equals("") && regex.contains(String.valueOf(previousLetter))) {
                                wordsList.add(word);
                            }
                            previousLetter = ' ';
                            word = "";
                        }

                        counter++;
                    }
                    j++;
                }
                /*
                * in continuare, se respecta si regula conform careia daca fragmentul se termina in
                * mijlocul unui cuvant, se ia in considerare si acel cuvant
                * Se pune si acest ultim cuvant in lista de cuvinte
                */
                if (!word.equals("") && regex.contains(String.valueOf(previousLetter))) {
                    char character = (char)ret;
                    while (ret != -1 && !regex.contains(String.valueOf(character))) {
                        word = word.concat(String.valueOf(character));
                        ret = bufferedReader.read();
                        character = (char) ret;
                    }
                    wordsList.add(word);
                }
                /*
                * Creez dictionarul din lista de cuvinte si in acelasi timp calculez
                * lungimea maxima a cuvintelor
                */
                for (String w : wordsList) {
                    if(w.length() > maxLength) {
                        maxLength = w.length();
                    }
                    if (map.putIfAbsent(w.length(), 1) != null) {
                        map.put(w.length(), map.get(w.length()) + 1);
                    }
                }
                /*
                * Creez o lista de cuvinte de lungime maxima din fragmentul prelucrat ce
                * este corespunzatoare dictionarului
                */
                for (String w : wordsList) {
                    if(w.length() == maxLength) {
                        maxWords.add(w);
                    }
                }
                /*
                * Se adauga dictionarul si lista de cuvinte maximale task
                */
                tasks.get(i).setMaxWords(maxWords);
                tasks.get(i).setMap(new Hashtable<>(map));

                map.clear();
                fileReader.close();
                bufferedReader.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
