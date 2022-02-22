
public class Tema2 {
    public static void main(String[] args) {
        if (args.length < 3) {
            System.err.println("Usage: Tema2 <workers> <in_file> <out_file>");
            return;
        }
        /*
          presupunem ca utilizator-ul care cere procesarea unui set de date este main-ul
          si exista un coordonator care se ocupa de cerere
        */
        try {
            int workers = Integer.parseInt(args[0]);
            String inputFilename = args[1];
            String outFilename = args[2];
            Coordinator coordinator = new Coordinator(inputFilename, outFilename, workers);
            coordinator.run();
        }catch(NumberFormatException ex) {
            System.err.println("Invalid string in argument");
        }
    }
}
