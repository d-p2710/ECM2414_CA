import java.util.Scanner;
public class CardGame {
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
    public static void main(String[] args) {
        // Read the number of players from the command-line input
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of players: ");
        int numPlayers = scanner.nextInt();
        scanner.close();

        CardGame game = new CardGame(numPlayers);
        game.startGame();
    }
}
