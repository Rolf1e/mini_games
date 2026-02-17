package fr.rolfie.minigames.games.domain.arrowwordpuzzle;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * https://stackoverflow.com/questions/943113/algorithm-to-generate-a-crossword#1021800
 */
@RequiredArgsConstructor
public class RandomGenerator implements Generator {

    private final int width;
    private final int height;

    @Override
    public ArrowWordPuzzleGame generate(List<DefinitionBO> dictionary) {
        final var words = new ArrayList<>(dictionary);

        // 1. Sort all the words by length, descending.
        words.sort(RandomGenerator::wordComparator);

        // 2. Search through all the words that are already on the board and see if there are any 
        // possible intersections (any common letters) with this word.
        final var validatedDefinitions = new ArrayList<DefinitionBO>(dictionary.size());


        for (final var word : words) {
            // 3. Filter word longer that the grid size.
            if (!fitsInGrid(word, width, height)) {
                continue;
            }

            if (canBePlaced(validatedDefinitions, word)) {
                validatedDefinitions.add(word);
            }
        }

        return new ArrowWordPuzzleGame(validatedDefinitions);
    }

    private static boolean canBePlaced(List<DefinitionBO> placedWords, DefinitionBO word) {
        for (final var placedWord : placedWords) {

            // 4. If there is no intersection, then this word cannot be placed here.
            if (!hasIntersection(placedWord, word)) {
                return false;
            }

            // 5. If there is a possible location for this word.

        }

        // otherwise, continue searching for a place (step 4).
        // 6. Continue this loop until all the words are either placed or unable to be placed.

        return true;
    }

    private static boolean fitsInGrid(DefinitionBO word, int width, int height) {
        return word.answer().length() <= width && word.answer().length() <= height;
    }


    private static boolean hasIntersection(DefinitionBO word1, DefinitionBO word2) {
        for (final var c : word1.answer().toCharArray()) {
            if (word2.answer().contains(String.valueOf(c))) {
                return true;
            }
        }
        return false;
    }

    private static int wordComparator(DefinitionBO a, DefinitionBO b) {
        return Integer.compare(b.answer().length(), a.answer().length());
    }

}
