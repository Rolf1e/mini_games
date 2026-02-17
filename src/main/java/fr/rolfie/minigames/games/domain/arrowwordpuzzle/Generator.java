package fr.rolfie.minigames.games.domain.arrowwordpuzzle;

import java.util.List;

interface Generator {
    ArrowWordPuzzleGame generate(List<DefinitionBO> dictionary);
}
