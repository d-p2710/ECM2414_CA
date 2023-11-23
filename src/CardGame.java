import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;
import java.io.BufferedReader;
import java.io.FileReader;

public class CardGame {
    // CardGame is a singleton class as once instantiated we don't want multiple games running only one at a given time.
    private static CardGame instance;

    // Private constructor to prevent instantiation from outside
    private CardGame() {
    }

    public static CardGame getInstance() {
        if (instance == null) {
            instance = new CardGame();
        }
        return instance;
    }

    private static int numPlayers;
    private ArrayList<Player> players;
    private ArrayList<CardDeck> decks;
    private static int[] pack;
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
        CardGame game = CardGame.getInstance();
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
                    int value = Integer.parseInt(line);
                    // Process the value as needed
                    System.out.println("Read value: " + value);
                } catch (NumberFormatException e) {
                    System.err.println("Error: Invalid integer in input pack file.");
                    return;
                }
            }
        }
    }

    private static int[] readFileIntoPack(int n, String filePath) throws IOException{
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            for (int i = 0; i < 8 * n; i++) {
                String line = reader.readLine();
                if (line == null) {
                    throw new IOException("Input pack file is incomplete.");
                }
                int value = Integer.parseInt(line);
                pack[i] = value;

            }
        }
        return pack;
    }

    private CardGame(int numPlayers) {
        CardGame.numPlayers = numPlayers;
        players = new ArrayList<Player>();
        decks = new ArrayList<CardDeck>();
    }

    public void startGame(int[] pack) {
        try {
            initialisePlayersAndDecks(numPlayers);
            allocateCards(pack);
<<<<<<< Updated upstream
            //playGame();
=======
            for (Player player : players) {
                System.out.println("Player Initialised: "+player.getPlayerID());
                PlayGameRunnable playGameRunnable = new PlayGameRunnable(player, decks);
                Thread thread = new Thread(playGameRunnable);
                thread.start();
                thread.join();
                System.out.println(thread);
            }


>>>>>>> Stashed changes
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initialisePlayersAndDecks(int numPlayers) {
        for (int i = 0; i < numPlayers; i++) {
            Player player = new Player(i + 1);
            players.add(player);
            CardDeck deck = new CardDeck(i + 1);
        }
        for (int i = 0; i <numPlayers; i++){
            players.get(i).setLHSDeckId((i + numPlayers) % numPlayers + 1);
            players.get(i).setRHSDeckId(i == 0 ? numPlayers : i + 1);
        }
    }

    private void allocateCards(int[] pack) {
        int index = 0;
        for (int i = 0; i < numPlayers * 4; i++) {
            Card card = new Card(index, pack[index]);
            for (int j = 0; j < 4; j++) {
                players.get(i).addCardtoHand(j,card);
            }
            index++;
        }
        for (int i = 0; i < pack.length - (numPlayers* 4); i++) {
            Card card = new Card(index, pack[index]);
            decks.get(i).addCardtoDeck(card);
            index++;

        }
    }
    public static boolean checkWinCondition(Card[] hand) {
        if (hand == null || hand.length == 0) {
            // Handle edge cases, like an empty array or null reference
            return false;
        }

        int firstNumber = hand[0].getValue();

        for (int i = 1; i < hand.length; i++) {
            if (hand[i].getValue() != firstNumber) {
                // If any number is different, return false
                return false;
            }
        }

        // All numbers are the same
        return true;
    }
<<<<<<< Updated upstream
=======


>>>>>>> Stashed changes
}
