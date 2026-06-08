package fr.rolfie.minigames.games.domain.arrowwordpuzzle.generators;

import fr.rolfie.minigames.games.domain.arrowwordpuzzle.ArrowWordPuzzleGame;
import fr.rolfie.minigames.games.domain.arrowwordpuzzle.DefinitionBO;

import java.util.List;

public interface Generator {
    ArrowWordPuzzleGame generate(List<DefinitionBO> dictionary);
}
