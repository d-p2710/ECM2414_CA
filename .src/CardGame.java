import java.util.Scanner;

public class CardGame {
    //gyyu
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
        for (int i = 0; i < players.length; i++) {
            players[i] = new Player(i + 1);
            decks[i] = new CardDeck(i + 1);
        }
    }
    private void loadFile(String fileName) {
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
        return true
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
