import java.util.Scanner;
public class CardGame {
    int numPlayers;
    // list of players
    private Player[] players;
    // list of decks
    private CardDeck[] decks;
    // list of cards in the pack
    private List<Card> pack;
    public CardGame(int numPlayers) {
        players = new Player[numPlayers];
        decks = new CardDeck[numPlayers];
        pack = new ArrayList<>();
    }

    public void startGame() {
        for (int i = 0; i < players.length; i++) {
            players[i] = new Player(i + 1);
            decks[i] = new CardDeck(i + 1);
        }
    }
    /*private void loadFile(String fileName) {
        try {
            File file = new File(inputFile());
            Scanner scanner = new Scanner(file);
            int count = 0;
            while (scanner.hasNextLine()) {
                count++;
                int value = Integer.parseInt(scanner.nextLine().trim());
                if (value < 1) {
                    System.out.println("Error: The card pack file is invalid.");


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

     */

    private String inputFile(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter location of pack to load: ");
        String packFileName = scanner.nextLine();
        return packFileName;
    }

    private int countLines(String fileName){
        Path path = Paths.get(fileName);
        try {
            int lines = Files.lines(path).count();
        } catch (IOException e) {
        }
        return lines;
    }

    private boolean checkFile(String fileName) {
        while ((countLines(fileName) != 8 * numPlayers) &&  {
            fileName = inputFile();
        }
    }


    public static void main(String[] args) {
        // Read the number of players from the command-line input
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of players: ");
        numPlayers = scanner.nextInt();
        scanner.close();

        CardGame game = new CardGame(numPlayers);
        game.startGame();
    }
}
