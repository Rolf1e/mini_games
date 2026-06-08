package fr.rolfie.minigames.games.domain.arrowwordpuzzle.generators;

import fr.rolfie.minigames.games.domain.arrowwordpuzzle.ArrowWordPuzzleGame;
import fr.rolfie.minigames.games.domain.arrowwordpuzzle.DefinitionBO;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * https://stackoverflow.com/questions/943113/algorithm-to-generate-a-crossword#1021800
 */
@RequiredArgsConstructor
public class RandomGenerator implements Generator {

    private final int width;
    private final int height;

    @Override
    public ArrowWordPuzzleGame generate(List<DefinitionBO> dictionary) {
        final var words = dictionary.stream()
                .map(DefinitionBO::answer)
                // 1. Sort the words by length, longest first.
                .sorted(RandomGenerator::wordComparator)
                // 2. Filter word longer that the grid size.
                .filter(word -> fitsInGrid(word, width, height))
                .toList();

        final var horizontal = new ArrayList<String>();
        final var vertical = new ArrayList<String>();

        // 3. Search through all the words that are already on the board and see if there are any 
        // possible intersections (any common letters) with this word.

        for (final var word : words) {

            canBePlaced(horizontal, vertical, word)
                    .ifPresent(direction -> {
                        switch (direction) {
                            case HORIZONTAL -> horizontal.add(word);
                            case VERTICAL -> vertical.add(word);
                        }
                    });
        }

        final var validatedDefinitions = new ArrayList<DefinitionBO>(dictionary.size());


        return new ArrowWordPuzzleGame(validatedDefinitions);
    }

    enum Direction {
        HORIZONTAL,
        VERTICAL
    }

    private static Optional<Direction> canBePlaced(List<String> horizontal, List<String> vertical, String word) {
        if (horizontal.isEmpty() && vertical.isEmpty()) {
            return Optional.of(Direction.HORIZONTAL);
        }

        return checkDirection(horizontal, word)
                .or(() -> checkDirection(vertical, word));
    }

    private static Optional<Direction> checkDirection(List<String> placedWords, String word) {
        for (final var placedWord : placedWords) {
            // 4. If there is no intersection, then this word cannot be placed here.
            final boolean hasIntersection = hasIntersection(placedWord, word);

            // 5. If there is a possible location for this word.
//            if (hasIntersection && hasLocation(placedWords, word)) {
//                return true;
//            }
        }
        return Optional.empty();
    }

    private static boolean hasLocation(List<DefinitionBO> placedWord, DefinitionBO word) {
        return true;
    }

    private static boolean fitsInGrid(String word, int width, int height) {
        return word.length() <= width && word.length() <= height;
    }


    private static boolean hasIntersection(String word1, String word2) {
        for (final var c : word1.toCharArray()) {
            if (word2.contains(String.valueOf(c))) {
                return true;
            }
        }
        return false;
    }

    private static int wordComparator(String a, String b) {
        return Integer.compare(b.length(), a.length());
    }

}
