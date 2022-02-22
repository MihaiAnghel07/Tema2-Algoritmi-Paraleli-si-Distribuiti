import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Generator {
    private final String filename;
    private int nrFiles;
    private ArrayList<MapTask> tasks;
    private ArrayList<ReduceTask> reduceTasks;

    public Generator(String filename, ArrayList<MapTask> tasks) {
        this.filename = filename;
        this.tasks = tasks;
        this.reduceTasks = new ArrayList<>();
    }

    public ArrayList<MapTask> generateMapTasks() {
        /*
        * Se citeste fisierul de input, dimeniunea fragmentului si numarul de fisiere ce contin text
        */
        File f = new File(filename);
        try {
            Scanner scanner = new Scanner(f);
            int D = Integer.parseInt(scanner.next());
            nrFiles = Integer.parseInt(scanner.next());
            /*
            * Se deschid cele nrFiles fisiere, continutul lor este impartit in fragmente de
            * dimensiune D si se creeaza task-urile asupra carora se va aplica map
            */
            while (scanner.hasNextLine()) {
                String filePath = scanner.next();
                File f2 = new File(filePath);
                int offset = 0;

                while (offset < f2.length()) {
                    String fName = f2.getName() + ".txt";
                    if (offset + D <= f2.length()) {
                        tasks.add(new MapTask(fName, offset, D, filePath));
                        offset = offset + D;
                    } else {
                        tasks.add(new MapTask(fName, offset, (int)f2.length() - offset, filePath));
                        offset = (int)f2.length();
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return tasks;
    }

    public ArrayList<ReduceTask> generateReduceTasks() {

        /*
        * Se preiau datele partiale generate de Map si se creeaza noile task-uri de Reduce corespunzatoare
        * fiecarui fisier de input (deci vor fi nrFiles task-uri Reduce)
        */
        int k = 0;
        for (int i = 0; i < nrFiles; i++) {
            String filename = tasks.get(k).getFilename();
            ReduceTask reduceTask = new ReduceTask(filename);
            List<Map<Integer, Integer>> list = new ArrayList<>();
            while (k < tasks.size() && tasks.get(k).getFilename().equals(filename)) {
                list.add(tasks.get(k).getMap());
                reduceTask.addPartialMaxWords(tasks.get(k).getMaxWords());
                k++;
            }
            reduceTask.setListMap(list);
            reduceTasks.add(reduceTask);
        }

        return reduceTasks;
    }
}
