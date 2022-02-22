import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class ReduceClass implements Runnable{
    // lista cu task-uri de tip reduce
    private ArrayList<ReduceTask> tasks;
    private final int workers;
    private final int threadId;
    // lista cu obiecte de tip output cu ajutorul carora vom face scrierea formatata pe disc
    private List<Output> output;

    public ReduceClass(ArrayList<ReduceTask> reduceTasks, int threadId, int workers, List<Output> output) {
        this.tasks = reduceTasks;
        this.threadId = threadId;
        this.workers = workers;
        this.output = output;
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
        * Fiecare thread executa operatiile de reduce si processing pe intervalul
        * de task-uri alocat
        */
        for (int i = start; i < end; i++) {
            reduce(tasks.get(i));
        }
    }

    private void reduce(ReduceTask task) {
        /*
        * Se preia task-ul primit ca argument si se prelucreaza lista de dictionare partiale
        * pentru a creea un singur dictionar
        * Se calculeaza numarul cuvintelor din intreg documentul si lungimea maxima a cuvintelor
        * din lista task-ului
        */
        int maxLength = 0;
        int size = 0;
        Map<Integer, Integer> map = new Hashtable();
        for (int j = 0; j < task.getListMap().size(); j++) {
            for (Map.Entry entry : task.getListMap().get(j).entrySet()) {
                if ((int)entry.getKey() > maxLength) {
                    maxLength = (int)entry.getKey();
                }
                size += (int)entry.getValue();
                if (map.putIfAbsent((int)entry.getKey(), (int)entry.getValue()) != null) {
                    map.put((int)entry.getKey(), map.get(entry.getKey()) + (int)entry.getValue());
                }
            }
        }

        /*
        * Se combina listele de cuvinte maximale si se face dinnou triajul, pastrandu-se
        * doar cuvintele de lungime maxima
        */
        ArrayList<String> maxLenWords = new ArrayList<>();
        for (int i = 0; i < task.getPartialMaxWords().size(); i++) {
            for (int j = 0; j < task.getPartialMaxWords().get(i).size(); j++) {
                if (task.getPartialMaxWords().get(i).get(j).length() == maxLength) {
                    maxLenWords.add(task.getPartialMaxWords().get(i).get(j));
                }
            }
        }

        /*
        * Dupa ce se termina etapa de Reduce, se trece la etapa de Processing a task-ului respectiv
        */
        processing(task, maxLenWords, map, maxLength, size);
    }

    private void processing(ReduceTask task, ArrayList<String> maxLenWords, Map<Integer, Integer> map, int maxLength, int size) {
        /*
        * Se calculeaza sirul fibonacci pentru lungimea maxima primita ca argument
        * Se aplica formula pentru rang
        * Apoi creez o instanta a clasei Output pentru a adauga formatul de output aferent task-ului prelucrat
        */
        ArrayList<Integer> fibonacci = Fibonacci(maxLength);
        int rank = 0;
        for (Map.Entry entry : map.entrySet()) {
            rank += fibonacci.get((int)entry.getKey() - 1) * (int)entry.getValue();
        }

        String result = String.format("%.2f", (float)rank/size);
        Output out = new Output(task.getFilename().substring(0, task.getFilename().length() - 4), result, maxLenWords);
        output.add(out);
    }

    private ArrayList<Integer> Fibonacci(int n) {
        /*
        * Aceasta metoda ajuta la formarea sirului lui fibonacci
        */
        ArrayList<Integer> fibonacci = new ArrayList<>();
        int fib1 = 0;
        int fib2 = 1;
        for (int i = 0; i < n; i++) {
            fibonacci.add(fib1 + fib2);
            fib1 = fib2;
            fib2 = fibonacci.get(i);
        }

        return fibonacci;
    }
}
