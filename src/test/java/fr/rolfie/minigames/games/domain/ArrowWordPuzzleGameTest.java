package fr.rolfie.minigames.games.domain;

import fr.rolfie.minigames.games.domain.arrowwordpuzzle.ArrowWordPuzzleGame;
import fr.rolfie.minigames.games.domain.arrowwordpuzzle.DefinitionBO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ArrowWordPuzzleGameTest {

    private static final DefinitionBO PHONE_DEFINITION = new DefinitionBO(0, "phone", "A communication device");
    private static final DefinitionBO COMPUTER_DEFINITION = new DefinitionBO(1, "computer", "A computing device");
    private static final DefinitionBO ELEMENT_DEFINITION = new DefinitionBO(2, "element", "A particle of matter");
    private static final DefinitionBO BAT_DEFINITION = new DefinitionBO(3, "bat", "A flying mammal");
    private static final DefinitionBO ENGINEERING_DEFINITION = new DefinitionBO(4, "engineering", "A piece of furniture");
    private static final DefinitionBO CHAIR_DEFINITION = new DefinitionBO(5, "chair", "A piece of furniture");

    private ArrowWordPuzzleGame puzzle;

    @BeforeEach
    public void setUp() {
        final var definitions = List.of(PHONE_DEFINITION, COMPUTER_DEFINITION, ELEMENT_DEFINITION);
        puzzle = new ArrowWordPuzzleGame(definitions);
    }

    @Test
    public void shouldPlayArrowWordPuzzleGame() {
        final var response = puzzle.play(PHONE_DEFINITION.id(), "phone");

        Assertions.assertTrue(response.success());
    }

    @Test
    public void shouldFailArrowWordPuzzleGame() {
        final var response = puzzle.play(COMPUTER_DEFINITION.id(), "laptop");
        Assertions.assertFalse(response.success());
    }

    @Test
    public void shouldGenerateArrowWordPuzzleGame() {
        final var dictionary = List.of(
                PHONE_DEFINITION,
                COMPUTER_DEFINITION,
                ELEMENT_DEFINITION,
                BAT_DEFINITION
        );
        final var puzzle = ArrowWordPuzzleGame.generate(dictionary);

        Assertions.assertEquals(3, puzzle.getDefinitionsCount());
    }

    @Test
    public void shouldGenerateArrowWordPuzzleGameWithLimitedGridSize() {
        final var dictionary = List.of(
                PHONE_DEFINITION,
                COMPUTER_DEFINITION,
                ELEMENT_DEFINITION,
                BAT_DEFINITION,
                ENGINEERING_DEFINITION
        );
        final var puzzle = ArrowWordPuzzleGame.generate(dictionary, 10, 10);

        Assertions.assertEquals(3, puzzle.getDefinitionsCount());
    }

    @Test
    public void shouldGenerateArrowWordPuzzleGameWithBothDirection() {
        final var dictionary = List.of(
                BAT_DEFINITION,
                CHAIR_DEFINITION
        );
        final var puzzle = ArrowWordPuzzleGame.generate(dictionary, 5, 5);

        Assertions.assertEquals(3, puzzle.getDefinitionsCount());
    }


}
