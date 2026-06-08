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
    private static final DefinitionBO GAS_DEFINITION = new DefinitionBO(3, "gas", "A state of matter");
    private static final DefinitionBO ENGINEERING_DEFINITION = new DefinitionBO(4, "engineering", "A piece of furniture");

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
                GAS_DEFINITION
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
                GAS_DEFINITION,
                ENGINEERING_DEFINITION
        );
        final var puzzle = ArrowWordPuzzleGame.generate(dictionary, 10, 10);

        Assertions.assertEquals(3, puzzle.getDefinitionsCount());
    }

    @Test
    public void shouldGenerateArrowWordPuzzleGameWithinGrid() {
        final var dictionary = List.of(
                // horizontal
                new DefinitionBO(0, "aides", "soutiens"),
                new DefinitionBO(1, "lune", "astre"),
                new DefinitionBO(2, "il", "pronom personnel"),
                new DefinitionBO(3, "si", "note de musique"),
                new DefinitionBO(4, "elan", "avant un saut"),

                // vertical
                new DefinitionBO(5, "lille", "ville du nord de la france"),
                new DefinitionBO(6, "du", "dette"),
                new DefinitionBO(7, "pensa", "réfléchit"),
                new DefinitionBO(8, "sein", "poitrine"),

                // not used
                new DefinitionBO(9, "ordi", "machine de calcul"),

                new DefinitionBO(1,"april","Month of the New York primary vote,arts"),
                new DefinitionBO(1,"nbc","30 rock network,arts"),
                new DefinitionBO(1,"owe","need to pay,arts"),
                new DefinitionBO(1,"drone","Stingle bee,arts"),
                new DefinitionBO(1,"ale","Malt bevarage,arts"),
                new DefinitionBO(1,"vim","Energy,arts"),
                new DefinitionBO(1,"donne","Death, Be Not Proud poet,arts"),
                new DefinitionBO(1,"pan","Trash,arts"),
                new DefinitionBO(1,"ens","Somethings,arts"),
                new DefinitionBO(1,"toaster","Bread browner,arts"),
                new DefinitionBO(1,"asp","Snake that bit Celo,arts"),
                new DefinitionBO(1,"idle","Turn over,arts"),
                new DefinitionBO(1,"arum","Jack-in-the-pulpit,arts"),
                new DefinitionBO(1,"boredom","Ennui,arts"),
                new DefinitionBO(1,"ruse","Deceit,arts"),
                new DefinitionBO(1,"adorer","Lover,arts"),
                new DefinitionBO(1,"miller","Columbia University theater,arts"),
                new DefinitionBO(1,"sofa","Resting Place,arts"),
                new DefinitionBO(1,"masseur","Massage specialist,arts"),
                new DefinitionBO(1,"emit","Release,arts"),
                new DefinitionBO(1,"sago","Powdery strach,arts"),
                new DefinitionBO(1,"spy","Mole,arts"),
                new DefinitionBO(1,"lowcost","At a baragain price,arts"),
                new DefinitionBO(1,"fee","Something charged,arts"),
                new DefinitionBO(1,"ear","Ring site,arts"),
                new DefinitionBO(1,"otter","Aquatic charged,arts"),
                new DefinitionBO(1,"err","Misadd e.g.,arts"),
                new DefinitionBO(1,"ali","A.K.A. Clay,arts"),
                new DefinitionBO(1,"piano","Elton Johnh's medium,arts"),
                new DefinitionBO(1,"yes","Approved,arts"),
                new DefinitionBO(1,"res","Image specification, for short,arts"),
                new DefinitionBO(1,"etude","Study musical technique,arts"),
                new DefinitionBO(1,"add","Do sums,arts"),
                new DefinitionBO(1,"pro","Country club figure,arts"),
                new DefinitionBO(1,"ron","Palillo of Welcome Back Kotter,arts"),
                new DefinitionBO(1,"inn","Stopover site,arts"),
                new DefinitionBO(1,"leetide","Current running with the wind,arts"),
                new DefinitionBO(1,"napalm","Vietnam war weapon,arts"),
                new DefinitionBO(1,"blase","World-weary,arts"),
                new DefinitionBO(1,"cent","Small price to pay,arts"),
                new DefinitionBO(1,"overrules","Vetoes,arts"),
                new DefinitionBO(1,"win","Succeed,arts"),
                new DefinitionBO(1,"ems","Expressions of hesitation,arts"),
                new DefinitionBO(1,"tide","It may be masked or disagreeable,arts"),
                new DefinitionBO(1,"odor","Nobles,arts"),
                new DefinitionBO(1,"earls","Humble or degrade,arts"),
                new DefinitionBO(1,"abase","Wickedness site in Genesis,arts"),
                new DefinitionBO(1,"sodom","Criminal Minds pros,arts"),
                new DefinitionBO(1,"useup","Consume entirely,arts"),
                new DefinitionBO(1,"merry","____-go-round,arts"),
                new DefinitionBO(1,"erato","Mythical lyrist,arts"),
                new DefinitionBO(1,"mags","Cosmo and people e.g.,arts"),
                new DefinitionBO(1,"isotope","Praticular from of an element,arts"),
                new DefinitionBO(1,"maoris","Members of Polynesian New Zealand,arts"),
                new DefinitionBO(1,"scale","Bathroom item,arts"),
                new DefinitionBO(1,"lers","Sport,arts"),
                new DefinitionBO(1,"wear","Noted Palin impression,arts"),
                new DefinitionBO(1,"fey","___he drove out of sight..,arts"),
                new DefinitionBO(1,"ere","Small sougbird,arts"),
                new DefinitionBO(1,"tit","Kind of cross,arts"),
                new DefinitionBO(1,"tau","Wipe out,arts"),
                new DefinitionBO(1,"end","____vs. Wade,arts"),
                new DefinitionBO(1,"roe","hint,arts"),
                new DefinitionBO(1,"profilers","arts"),
                new DefinitionBO(1,"sth","sth,arts"),
                new DefinitionBO(1,"usma","technology"),
                new DefinitionBO(1,"asof","technology"),
                new DefinitionBO(1,"laird","technology"),
                new DefinitionBO(1,"prix","technology"),
                new DefinitionBO(1,"nape","technology"),
                new DefinitionBO(1,"alter","technology"),
                new DefinitionBO(1,"civicduty","technology"),
                new DefinitionBO(1,"nasty","technology"),
                new DefinitionBO(1,"suede","technology"),
                new DefinitionBO(1,"admire","technology"),
                new DefinitionBO(1,"cnn","technology"),
                new DefinitionBO(1,"maidofhonor","technology"),
                new DefinitionBO(1,"zoomin","technology"),
                new DefinitionBO(1,"goo","technology"),
                new DefinitionBO(1,"arson","technology"),
                new DefinitionBO(1,"halo","technology"),
                new DefinitionBO(1,"sgts","technology"),
                new DefinitionBO(1,"raid","technology"),
                new DefinitionBO(1,"cadet","technology"),
                new DefinitionBO(1,"circ","technology"),
                new DefinitionBO(1,"ssrs","technology"),
                new DefinitionBO(1,"dior","technology"),
                new DefinitionBO(1,"kazoo","technology"),
                new DefinitionBO(1,"ark","technology"),
                new DefinitionBO(1,"wetmop","technology"),
                new DefinitionBO(1,"backcountry","technology"),
                new DefinitionBO(1,"ope","technology"),
                new DefinitionBO(1,"asylum","technology"),
                new DefinitionBO(1,"laius","technology"),
                new DefinitionBO(1,"strum","technology"),
                new DefinitionBO(1,"westpoint","technology"),
                new DefinitionBO(1,"saute","technology"),
                new DefinitionBO(1,"arte","technology"),
                new DefinitionBO(1,"flew","technology"),
                new DefinitionBO(1,"ibsen","technology"),
                new DefinitionBO(1,"yser","technology"),
                new DefinitionBO(1,"alto","technology"),
                new DefinitionBO(1,"maha","technology"),
                new DefinitionBO(1,"upc","technology"),
                new DefinitionBO(1,"sri","technology"),
                new DefinitionBO(1,"miv","technology"),
                new DefinitionBO(1,"axis","technology"),
                new DefinitionBO(1,"andean","technology"),
                new DefinitionBO(1,"saudi","technology"),
                new DefinitionBO(1,"opted","technology"),
                new DefinitionBO(1,"fey","technology"),
                new DefinitionBO(1,"landho","technology"),
                new DefinitionBO(1,"alamo","technology"),
                new DefinitionBO(1,"itsin","technology"),
                new DefinitionBO(1,"retro","technology"),
                new DefinitionBO(1,"dryer","technology"),
                new DefinitionBO(1,"cumin","technology"),
                new DefinitionBO(1,"afoot","technology"),
                new DefinitionBO(1,"czars","technology"),
                new DefinitionBO(1,"noras","technology"),
                new DefinitionBO(1,"nosir","technology"),
                new DefinitionBO(1,"ogler","technology"),
                new DefinitionBO(1,"mods","technology"),
                new DefinitionBO(1,"haiku","technology"),
                new DefinitionBO(1,"ado","technology"),
                new DefinitionBO(1,"scat","technology"),
                new DefinitionBO(1,"gizmo","technology"),
                new DefinitionBO(1,"troop","technology"),
                new DefinitionBO(1,"scope","technology"),
                new DefinitionBO(1,"cdrom","technology"),
                new DefinitionBO(1,"keyup","technology"),
                new DefinitionBO(1,"acumen","technology"),
                new DefinitionBO(1,"writer","technology"),
                new DefinitionBO(1,"bassi","technology"),
                new DefinitionBO(1,"astab","technology"),
                new DefinitionBO(1,"cyrus","technology"),
                new DefinitionBO(1,"klute","technology"),
                new DefinitionBO(1,"nlers","technology"),
                new DefinitionBO(1,"taste","technology"),
                new DefinitionBO(1,"sofa","technology"),
                new DefinitionBO(1,"way","technology"),
                new DefinitionBO(1,"ill","technology"),
                new DefinitionBO(1,"net","technology"),
                new DefinitionBO(1,"two","technology"),
                new DefinitionBO(1,"ahmad","arts"),
                new DefinitionBO(1,"sawas","arts") 

        );
        final var puzzle = ArrowWordPuzzleGame.generate(dictionary, 5, 5);

        Assertions.assertEquals(9, puzzle.getDefinitionsCount());
    }


}
