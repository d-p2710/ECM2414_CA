import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CardGame {
    // CardGame is a singlwton class as once instantiated we don't want multiple games running only one at a given time.
    private static volatile GardGame instance;
    private int numPlayers;
    private Player[] players;
    private CardDeck[] decks;
    private List<Card> pack;

    private CardGame (int numPlayers) {
        this.numPlayers= numPlayers;
        players = new Player[numPlayers];
        decks = new CardDeck[numPlayers];
        pack = new ArrayList<>();
    }

    public static CardGame getInstance(int numPlayers) {
        /*uses the double-check locking idiom to reduce overhead of aquiring a lock by testing criterion without having the lock */
        CardGame result = instance:
        if (result == null) {
            synchronized (CardGame.class) {
                result = instance;
                if (result == null) {
                    instance = result = new CardGame(numPlayers);
                }
            }
        }
        return result;
    }

    public void startGame() {
        try{
            validateNumPlayers();
            loadAndValidatePackFile();
            allocateCards();
            checkInitialConditions();
            playGame();
        } catch (Exception e){
            e.printStackTrace();
        }
        initialisePlayersAndDecks();
    }

    private void initialisePlayersAndDecks(){
        for (int i = 0; i < players.length; i++) {
            players[i] = new Player(i + 1);
            decks[i] = new CardDeck(i + 1);
        }
    }

    private void validateNumPlayers(){
        Scanner scanner = new Scanner(System.in);
        do{
            System.out.println("Enter the number of players (1-10): ");
            while (!scanner.hasNextInt()){
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
            }
            numPlayers = scanner.nexrInt();
        }while (numPlayers <1 || numPlayers> 10);
        scanner.close();
    }

    private void loadAndValidatePackFile() throws FileNotFoundException{
        while (true){
            try{
                String packFileName = inputFile();
                File file =new File(packFileName);
                if (!file.exists()){
                    throw new FileNotFoundException("Error: The card pack file was not found.");
                    continue;
                }
                checkFile(packFileName);
                List<Card> pack = Files.Lines(file.toPath())
                        .map (String::trim)
                        .map(Interger::parseInt)
                        .filter(value -> {
                            if value is N
                        })
            }
        }
    }
    private String inputFile(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter location of pack to load: ");
        String packFileName = scanner.nextLine();
        return packFileName;
    }

    private int countLines(String fileName){
        Path path = Paths.get(fileName);
        try {
            return (int) Files.lines(path).count();
        } catch (IOException e){
            e.prinStackTrace();
            return 0;
        }
    }

    private boolean checkFile(String fileName) {
        while ((countLines(fileName) != 8 * numPlayers) &&  {
            fileName = inputFile();
            System.out.println("Error: The card pack doesn't contain enough cards for all players. The card pack file should have "+expectedLines+" lines. Please enter a valid file path");
        }
        return true
    }

    private void loadFile(String fileName) throws FileNotFoundException{
        String filename = inputFile();
        try {
            File file = new File(inputFile());
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                int value = Integer.parseInt(scanner.nextLine().trim());
                if (value < 1) {
                    System.out.println("Error: The card pack file is invalid.");
                    System


                }
                Card card = new Card(value);
                pack.add(card);
            }
            if wh

            scanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("Error: The card pack file was not found.");
            System.exit(1);
        }
    }





    public static void main(String[] args) {
        // Read the number of players from the command-line input
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of players: ");
        numPlayers = scanner.nextInt();
        scanner.close();

        CardGame game = CardGame.getInstance(numPlayers)
        game.startGame();
    }
}
