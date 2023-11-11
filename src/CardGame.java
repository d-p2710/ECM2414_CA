import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class CardGame {
    public static final int MAX_HAND_SIZE = 4;
    // CardGame is a singleton class as once instantiated we don't want multiple games running only one at a given time.
    private static volatile CardGame instance;

    private static int numPlayers;
    private Player[] players;
    private CardDeck[] decks;
    private List<Card> pack;
    private int[][] playersHands;

    private CardGame (int numPlayers) {
        CardGame.numPlayers = numPlayers;
        players = new Player[numPlayers];
        decks = new CardDeck[numPlayers];
        pack = new ArrayList<>();
        playersHands= new int [numPlayers][MAX_HAND_SIZE];
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

    public void startGame() {
        try{
            validateNumPlayers();
            loadAndValidatePackFile();
            allocateCards(pack);
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
            playersHands[i] = new players[i].getHand();
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
            numPlayers = scanner.nextInt();
        }while (numPlayers <1 || numPlayers> 10);
    }

    private void loadAndValidatePackFile() throws FileNotFoundException{
        while (true){
            try{
                String packFileName = inputFile();
                File file =new File(packFileName);
                if (!file.exists()){
                    throw new FileNotFoundException("Error: The card pack file was not found.");
                }
                checkFile(packFileName);
                pack = Files.readAllLines(file.toPath())
                        .stream()
                        .map(String::trim)
                        .map(Integer::parseInt)
                        .peek(value -> {
                            if (value<1){
                                throw new IllegalArgumentException("Error: The pack file is invalid as it contains non-positive integers.");
                            }
                        })
                        .map(Card::new)
                        .toList();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private String inputFile(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter location of pack to load: ");
        return scanner.nextLine();
    }

    private int countLines(String fileName){
        Path path = Paths.get(fileName);
        try (Stream<String> lines = Files.lines(path)){
            return Math.toIntExact(lines.count());
        } catch ( IOException | ArithmeticException e){
            e.printStackTrace();
            return 0;
        }
    }

    private boolean checkFile(String fileName) {
        while (countLines(fileName) != 8 * numPlayers){
            fileName = inputFile();
            System.out.println("Error: The card pack doesn't contain enough cards for all players. The card pack file should have "+ 8*numPlayers +" lines. Please enter a valid file path");
        }
        return true;
    }
    private boolean validateCardValues(List<Card> pack){
        return pack.stream().allMatch(card -> card.getValue() >=1);
    }

    private void playGame() {
    }
    //

    private void checkInitialConditions() {
    }


    private void allocateCards(List<Card> cards){
        for ( int i=0; i<numPlayers; i++){
            for (int j= 0; j<MAX_HAND_SIZE; j++){
            players[i].getHand()[j]=cards.remove(0);
            }
        }
        for (int i=0; i<numPlayers; i++){
            for(int j=0; j<MAX_HAND_SIZE; j++){
                decks[i].addCard(cards.remove(0));
            }
        }
      
    public static void main(String[] args) {
        // Read the number of players from the command-line input
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of players: ");
        numPlayers = scanner.nextInt();
        scanner.close();

        CardGame game = CardGame.getInstance(numPlayers);
        game.startGame();
    }
}
