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
    public static final int MAX_HAND_SIZE = 4;
    // CardGame is a singleton class as once instantiated we don't want multiple games running only one at a given time.
    private static volatile CardGame instance;

    private static int numPlayers;
    private Player[] players;
    private CardDeck[] decks;
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
        players = new Player[numPlayers];
        decks = new CardDeck[numPlayers];
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

    public void startGame(int[] pack) {
        try {
            initialisePlayersAndDecks(numPlayers);
            allocateCards(pack);
            //playGame();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initialisePlayersAndDecks(int numPlayers) {
        for (int i = 0; i < numPlayers; i++) {
            players[i] = new Player(i + 1);
            decks[i] = new CardDeck(i + 1);
        }
    }


    private void allocateCards(int[] pack) {
        int index = 0;
        for (int i = 0; i < numPlayers * MAX_HAND_SIZE; i++) {
            Card card = new Card(index, pack[index]);
            for (int j = 0; j < MAX_HAND_SIZE; j++) {
                players[i].addCardtoHand(j,card);
            }
            index++;
        }
        for (int i = 0; i < pack.length - (numPlayers* MAX_HAND_SIZE); i++) {
            Card card = new Card(index, pack[index]);
            decks[i].addCardtoDeck(card);
            index++;

        }
    }





}
