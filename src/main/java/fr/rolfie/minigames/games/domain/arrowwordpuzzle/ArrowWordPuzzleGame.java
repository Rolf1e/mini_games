package fr.rolfie.minigames.games.domain.arrowwordpuzzle;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class ArrowWordPuzzleGame {

    private final List<DefinitionBO> definitions;

    public static ArrowWordPuzzleGame generate(List<DefinitionBO> dictionary) {
        return Factory.generate(dictionary, 10, 10);
    }

    public static ArrowWordPuzzleGame generate(List<DefinitionBO> dictionary, int width, int height) {
        return Factory.generate(dictionary, width, height);
    }

    public GameResponse play(int definitionId, String attempt) {
        final var definition = Objects.requireNonNull(
                definitions.get(definitionId),
                "Definition not found for id: " + definitionId
        );
        final var success = definition.answer().equalsIgnoreCase(attempt);
        return new GameResponse(success);
    }

    public int getDefinitionsCount() {
        return definitions.size();
    }


    /**
     * Generate a random ArrowWordPuzzleGame with a predefined set of definitions.
     */
    private static class Factory {

        static ArrowWordPuzzleGame generate(List<DefinitionBO> dictionary, int width, int height) {
            return new RandomGenerator(width, height).generate(dictionary);
        }
    }


}
