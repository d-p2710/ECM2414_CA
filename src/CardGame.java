import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;

import static java.lang.Integer.parseInt;

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
    private static ArrayList<Integer> pack = new ArrayList<>();
    private volatile boolean gameOver = false;

    public static void main(String[] args) throws IOException {
        // Read the number of players from the command-line input
        String filePath;
        boolean validPlayerNo = false;
        // Process the input pack file
        while (true) {
            Scanner scanner = new Scanner(System.in);
            if (!validPlayerNo) {
                try {
                    System.out.print("Enter the number of players: ");
                    numPlayers = scanner.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input for number of players.");
                    continue;
                }
            }
            validPlayerNo = true;
            // Get the location of the input file
            System.out.print("Please enter location of pack to load: ");
            filePath = scanner.next();
            try {
                if (processInputPack(numPlayers, filePath)){
                    break;
                }
            } catch (IOException e) {
                System.err.println("Error reading the input pack file: " + e.getMessage());
                // Request a valid pack file in case of an error
            }
        }
        pack = readFileIntoPack(numPlayers,filePath);
        CardGame game = CardGame.getInstance();
        game.startGame(pack);

    }

    private static boolean processInputPack(int n, String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            for (int i = 0; i < 8 * n; i++) {
                String line = reader.readLine();
                if (line == null) {
                    System.err.println("Error: Input pack file is incomplete.");
                    return false;
                }
                try {
                    int value = parseInt(line);
                } catch (NumberFormatException e) {
                    System.err.println("Error: Invalid integer in input pack file.");
                    return false;
                }
            }
            return true;
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
            players = new ArrayList<>();
            decks = new ArrayList<>();
            initialisePlayersAndDecks(numPlayers);
            allocateCards(pack);
            Thread[] playerThreads = new Thread[numPlayers];
            for (int i = 0; i < numPlayers; i++) {
                playerThreads[i] = new Thread(players.get(i));
                playerThreads[i].start();
            }
            int winner = 0;
            while(!gameOver) {
                int count = 1;
                for (Thread thread: playerThreads) {
                    if (!thread.isAlive()) {
                        winner = count;
                        System.out.println("Game over");
                        gameOver = true;
                        break;
                    }
                    count++;
                }
                if (gameOver){
                    for (int i=0; i<numPlayers; i++){
                        if (i+1!=winner){
                            players.get(i).writeToOutputFile("Player " +winner+ " has informed player" +(i+1)+ " that player" +winner+ " has won.");
                            playerThreads[i].interrupt();
                        }
                    }
                }
            }
            for (Thread thread : playerThreads) {
                thread.interrupt();
            }
            for (Player player: players){
                player.recordFinalState(player.getHand());
            }
            for (CardDeck cardDeck : decks){
                cardDeck.recordFinalState(cardDeck.getDeck());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initialisePlayersAndDecks(int numPlayers) throws IOException {
        for (int i = 0; i < numPlayers; i++) {
            Player player = new Player(i + 1,gameOver);
            players.add(player);
            CardDeck deck = new CardDeck(i + 1);
            decks.add(deck);
        }
    }

    private void allocateCards(ArrayList<Integer> pack) {
        int index = 0;
        int deckIndex =0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < numPlayers; j++) {
                Card card = new Card(index, pack.get(index));
                players.get(j).addCardToHand(card);
                index++;
            }

        }
        for (int i = 0; i < pack.size() - (numPlayers* 4); i++) {
            Card card = new Card(index, pack.get(index));
            decks.get(deckIndex).addCardToDeck(card);
            index++;
            deckIndex = (deckIndex + 1) % numPlayers;
        }
        for (int i = 0; i <numPlayers; i++){
            players.get(i).setLHSDeck(decks.get(i));
            players.get(i).setRHSDeck(decks.get((i + 1)%numPlayers));
        }
    }

}
