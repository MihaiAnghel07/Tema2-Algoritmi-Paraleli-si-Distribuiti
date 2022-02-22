
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


public class Coordinator {
    private final String inputFilename;
    private final String outputFilename;
    private final int workers;
    private ArrayList<MapTask> tasks;
    private ArrayList<ReduceTask> reduceTasks;
    private List<Output> output;

    public Coordinator(String inputFilename, String outputFilename, int workers) {
        this.inputFilename = inputFilename;
        this.outputFilename = outputFilename;
        this.workers = workers;
        this.tasks = new ArrayList<>();
        this.output = Collections.synchronizedList(new ArrayList<>());
    }

    public void run() {
        Generator generator = new Generator(inputFilename, tasks);
        /*
        * generator imparte input-ul in fragmente de dimensiuni fixe (task-uri)
        * se creeaza 'workers' thread-uri care vor aplica map pe task-urile asignate lor
        */
        tasks = generator.generateMapTasks();
        Thread[] mapThreads = new Thread[workers];
        for (int i = 0; i < workers; i++) {
            mapThreads[i] = new Thread(new MapClass(tasks, i, workers));
        }

        for (int i = 0; i < workers; i++) {
            mapThreads[i].start();
        }

        for (int i = 0; i < workers; i++) {
            try {
                mapThreads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        /*
        * A doua functie a lui generator este de a genera task-urile de tip reduce
        */
        reduceTasks = generator.generateReduceTasks();

        /*
         * Se creeaza 'workers' thread-uri care vor aplica reduce & processing pe task-urile asignate lor
         */
        Thread[] reduceThreads = new Thread[workers];
        for (int i = 0; i < workers; i++) {
            reduceThreads[i] = new Thread(new ReduceClass(reduceTasks, i, workers, output));
        }

        for (int i = 0; i < workers; i++) {
            reduceThreads[i].start();
        }

        for (int i = 0; i < workers; i++) {
            try {
                reduceThreads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        /*
        * Se sorteaza output-ul in functie de rang
        */
        Collections.sort(output, (o1, o2) -> {
            if (Double.parseDouble(o2.getRank()) - Double.parseDouble(o1.getRank()) < 0)
                return -1;
            else if (Double.parseDouble(o2.getRank()) - Double.parseDouble(o1.getRank()) > 0)
                return 1;

            return 0;
        });

        /*
        * Se face scrierea pe disc
        */
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(outputFilename));
            for (Output value : output) {
                out.write(value.getFilename() + ","
                        + value.getRank() + ","
                        + value.getMaxLenWords().get(0).length()
                        + "," + value.getMaxLenWords().size() + '\n');
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
