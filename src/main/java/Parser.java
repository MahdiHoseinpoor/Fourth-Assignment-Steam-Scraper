import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Parser {
    static List<Game> games = new ArrayList<>();
    public void setUp() {
        try{
            File input = new File("src/Resources/Video_Games.html");
            Document doc = Jsoup.parse(input, "UTF-8");
            Elements elements = doc.select("div.game");
            for (Element element : elements) {
                String name = element.select("h3.game-name").text();
                String ratingText = element.select("span.game-rating").text();
                double rating = Double.parseDouble(ratingText.replace("/5", ""));
                String priceText = element.select("span.game-price").text().replace("€", "").trim();
                int price = Integer.parseInt(priceText);
                Game game = new Game(name, rating, price);
                games.add(game);
            }
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
    public List<Game> searchByName(String keyword) {
        List<Game> result = new ArrayList<>();
        for (Game game : games) {
            if (game.getName().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(game);
            }
        }
        return result;
    }

    public List<Game> sortByName() {
        List<Game> sortedByName = new ArrayList<>(games);
        sortedByName.sort(Comparator.comparing(Game::getName));
        return sortedByName;
    }

    public List<Game> sortByRating() {
        List<Game> sortedByRating = new ArrayList<>(games);
        sortedByRating.sort(Comparator.comparingDouble(Game::getRating).reversed());
        return sortedByRating;
    }

    public List<Game> sortByPrice() {
        List<Game> sortedByPrice = new ArrayList<>(games);
        sortedByPrice.sort(Comparator.comparingInt(Game::getPrice).reversed());
        return sortedByPrice;
    }

    public static void main(String[] args) throws IOException {
        Parser parser = new Parser();
        Scanner scanner = new Scanner(System.in);
        parser.setUp();

        while (true) {
            System.out.println("Enter your keyword for search (or enter 'exit' for exit):");
            String keyword = scanner.nextLine().trim();
            if (keyword.equalsIgnoreCase("exit")) break;
            List<Game> searchResults = parser.searchByName(keyword);
            if (searchResults.isEmpty()) {
                System.out.println("404 not found");
                continue;
            }
            while (true) {
                System.out.println("Sort the search results by:");
                System.out.println("1. Name");
                System.out.println("2. Rating");
                System.out.println("3. Price");
                System.out.println("0. New Search");
                int sortChoice = scanner.nextInt();
                scanner.nextLine();
                if (sortChoice == 0) break;
                System.out.println("Order by:");
                System.out.println("1. Ascending");
                System.out.println("2. Descending");
                int orderChoice = scanner.nextInt();
                scanner.nextLine();
                List<Game> sortedResults = new ArrayList<>(searchResults);
                switch (sortChoice) {
                    case 1:
                        sortedResults.sort(Comparator.comparing(Game::getName));
                        break;
                    case 2:
                        sortedResults.sort(Comparator.comparingDouble(Game::getRating));
                        break;
                    case 3:
                        sortedResults.sort(Comparator.comparingInt(Game::getPrice));
                        break;
                    default:
                        System.out.println("Invalid choice");
                        continue;
                }
                if (orderChoice == 2) {
                    Collections.reverse(sortedResults);
                }
                System.out.println("Search Results:");
                sortedResults.forEach(System.out::println);
            }
        }
    }

}
