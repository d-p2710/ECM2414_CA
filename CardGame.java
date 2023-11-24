import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import static java.lang.Integer.parseInt;

public class CardGame {
    // CardGame is a singleton class as once instantiated we don't want multiple games running only one at a given time.
    private static volatile CardGame instance;
    // Private constructor to prevent instantiation from outside
    private static int numPlayers;
    private final ArrayList<Player> players;
    private ArrayList<CardDeck> decks;
    private static ArrayList<Integer> pack=new ArrayList<>();

    private CardGame(int numPlayers){
        CardGame.numPlayers = numPlayers;
        players = new ArrayList<>();
        decks = new ArrayList<>();
    }

    public static CardGame getInstance(int numPlayers) {
        /*uses the double-check locking idiom to reduce overhead of acquiring a lock by testing criterion without having the lock */
        CardGame result = instance;
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
    public static void main(String[] args) throws IOException {
        // Read the number of players from the command-line input
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of players: ");
        numPlayers = scanner.nextInt();
        String filePath;
        // Process the input pack file
        while (true) {
            // Get the location of the input file
            System.out.print("Please enter location of pack to load: ");
            filePath = scanner.next();
            try {
                processInputPack(numPlayers, filePath);
                // If the processing is successful, break out of the loop
                break;
            } catch (IOException e) {
                System.err.println("Error reading the input pack file: " + e.getMessage());
                // Request a valid pack file in case of an error
            }
        }
        pack = readFileIntoPack(numPlayers,filePath);
        CardGame game = CardGame.getInstance(numPlayers);
        game.startGame(pack);
    }

    private static void processInputPack(int n, String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            for (int i = 0; i < 8 * n; i++) {
                String line = reader.readLine();
                if (line == null) {
                    System.err.println("Error: Input pack file is incomplete.");
                    return;
                }
                try {
                    int value = parseInt(line);
                    // Process the value as needed
                    System.out.println("Read value: " + value);
                } catch (NumberFormatException e) {
                    System.err.println("Error: Invalid integer in input pack file.");
                    return;
                }
            }
        }
    }

    private static ArrayList<Integer> readFileIntoPack(int n, String filePath) throws IOException{
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            for (int i = 0; i < 8 * n; i++) {
                String line = reader.readLine();
                if (line == null) {
                    throw new IOException("Input pack file is incomplete.");
                }
                int value = parseInt(line);
                pack.add(value);
            }
        }
        return pack;
    }

    public void startGame(ArrayList<Integer> pack) {
        try {
            initialisePlayersAndDecks(numPlayers);
            allocateCards(pack);
            for (CardDeck cardDeck: decks){
                System.out.println(cardDeck);
            }
            playGame();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void playGame() throws InterruptedException {
        for (Player player : players) {
            System.out.println("Player Initialised: "+player.getPlayerID());
            Thread thread = new Thread(player);
            thread.start();
            thread.join();
            System.out.println(thread);
        }
    }


    private void initialisePlayersAndDecks(int numPlayers) throws IOException {
        for (int i = 0; i < numPlayers; i++) {
            Player player = new Player(i + 1);
            players.add(player);
            CardDeck deck = new CardDeck(i + 1);
            decks.add(deck);
        }
        for (int i = 0; i < numPlayers; i++) {
            players.get(i).setLHSDeck(decks.get(i));
            players.get(i).setRHSDeck(decks.get((i + 1) % numPlayers));
        }
    }

    private void allocateCards(ArrayList<Integer> pack) {
        int index = 0;
        int deckIndex = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < numPlayers; j++) {
                Card card = new Card(index, pack.get(index));
                System.out.println(index);
                players.get(j).addCardToHand(card);
                index++;
            }
        }
        for (int i = 0; i < pack.size() - (numPlayers * 4); i++) {
            Card card = new Card(index, pack.get(index));
            System.out.println("i:" + i);
            decks.get(deckIndex).addCardToDeck(card);
            index++;
            deckIndex = (deckIndex + 1) % decks.size(); // Wrap around to 0 when it reaches the number of decks
        }
    }
}