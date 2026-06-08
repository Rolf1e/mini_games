package fr.rolfie.minigames.games.domain.arrowwordpuzzle.generators.csp;

import fr.rolfie.minigames.games.domain.arrowwordpuzzle.ArrowWordPuzzleGame;
import fr.rolfie.minigames.games.domain.arrowwordpuzzle.DefinitionBO;
import fr.rolfie.minigames.games.domain.arrowwordpuzzle.generators.Generator;

import java.util.*;

/**
 *
 * https://github.com/AhmadYahya97/CrosswordsGeneration/tree/master
 */
public class CspGenerator implements Generator {

    @Override
    public ArrowWordPuzzleGame generate(List<DefinitionBO> dictionary) {
        try {
            final List<String> words = dictionary.stream().map(DefinitionBO::answer).toList();
            generateWithCsp(words);
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static void generateWithCsp(List<String> listOfWords) throws CloneNotSupportedException {

        // --------------
        // hashmap from size to value object
        // --------------------------
        final Map<Integer, Value> newMap = new HashMap<>();

        for (String str : listOfWords) {
            int size = str.length();

            if (newMap.containsKey(size)) {
                Value val = newMap.get(size);
                val.add(str);
            } else {
                Value val = new Value(size);
                val.add(str);
                newMap.put(size, val);
            }
        }

        for (Integer size : newMap.keySet()) {
            newMap.get(size).setCombinations();
        }
        // ----------------------------
        // the grid
        // -------------

        char[][] grid2 = {{' ', ' ', ' ', '*', '*', ' ', ' ', ' ', ' '},
                {' ', '*', ' ', ' ', ' ', ' ', '*', '*', ' '}, {' ', '*', ' ', '*', '*', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', '*', ' ', '*', ' ', '*'}, {'*', ' ', '*', ' ', ' ', ' ', '*', ' ', '*'},
                {'*', ' ', '*', ' ', '*', ' ', ' ', ' ', ' '}, {' ', ' ', ' ', ' ', '*', '*', ' ', '*', ' '},
                {' ', '*', '*', ' ', ' ', ' ', ' ', '*', ' '}, {' ', ' ', ' ', ' ', '*', '*', ' ', '*', ' '}};

        char[][] grid3 = {{' ', ' ', ' ', ' ', ' ', '*', ' ', ' ', ' ', '*', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', '*', ' ', ' ', ' ', '*', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', '*', ' ', ' ', ' ', '*', ' ', ' ', ' '},
                {'*', '*', '*', '*', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '*', '*'},
                {' ', ' ', ' ', '*', ' ', ' ', ' ', ' ', '*', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', '*', '*', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', '*', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', '*', '*', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', '*', ' ', ' ', ' ', ' ', '*', ' ', ' ', ' '},
                {'*', '*', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '*', '*', '*', '*'},
                {' ', ' ', ' ', '*', ' ', ' ', ' ', '*', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', '*', ' ', ' ', ' ', '*', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', '*', ' ', ' ', ' ', '*', ' ', ' ', ' ', ' ', ' '},

        };
        char[][] grid = {{' ', ' ', ' ', ' ', ' '}, {' ', ' ', ' ', '*', ' '}, {' ', '*', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' '}, {' ', ' ', ' ', '*', ' '}};
        // -----------------
        // int x,y;
        //
        // x = new Scanner(System.in).nextInt();
        // y = new Scanner(System.in).nextInt();
        //
        // char grid2[][] = new char[x][y];
        //
        // for (int i = 0; i < x; i++) {
        // for (int j = 0; j < y; j++) {
        // grid2[i][j] = ' ';
        // }
        // }
        //
        // generateAmericanStyle(grid2);
        // print(grid2);

        final List<Variable> horVariables = new ArrayList<>();
        final List<Variable> verVariables = new ArrayList<>();
        findHorSlots(horVariables, grid, newMap);
        findVerSlots(verVariables, grid, newMap);
        print(grid);

        // connecting the neighbors
        // ------------------
        for (Variable horVariable : horVariables) {
            for (Variable verVariable : verVariables) {
                int[] info = horVariable.intersects(verVariable);
                if (info != null) {
                    horVariable
                            .addAnotherN(new IntersectionInfo(verVariable.uniqueID, info[0], info[1]));
                }
            }
            horVariable.setMostConstrainingVariableHeuristic2();
        }
        // ------------------

        // connecting the neighbors
        // -------------------
        for (Variable verVariable : verVariables) {
            for (Variable horVariable : horVariables) {
                int[] info = verVariable.intersects(horVariable);

                if (info != null) {
                    verVariable
                            .addAnotherN(new IntersectionInfo(horVariable.uniqueID, info[0], info[1]));

                }
            }
            verVariable.setMostConstrainingVariableHeuristic2();

        }
        // -------------------
        // defining heuristic1
        // -----------
        final MostConstrainingVariablePriority heuristic1 = new MostConstrainingVariablePriority();
        // -----------
        // defining id based sort
        // ------------
        final UniqueIdPriority sortByID = new UniqueIdPriority();
        // ------------
        // defining the initial state
        // ------------
        final List<Variable> initialState = new ArrayList<>();
        // ------------
        // adding the variables
        // ------------
        initialState.addAll(horVariables);
        initialState.addAll(verVariables);


        // ------------
        // sorting the initial state
        // --------------------
        initialState.sort(heuristic1);
        // --------------------

        final List<Variable> shit = new ArrayList<>();
        for (Variable v : initialState) {
            shit.add((Variable) v.clone());
        }

        final List<Variable> triedShit = new ArrayList<>(shit);

        triedShit.sort(sortByID);

        // starting the DFS
        // -----------------------

        // defining the initial state
        final State initialS = new State(initialState);

        // defining the stack
        final Stack<State> states = new Stack<>();

        // pushing the initial state to the stack
        states.push(initialS);

        int iterationNumber = 0;

        char[][] currentGrid;

        System.out.println(">>> " + states.size());

        while (!states.isEmpty()) {

            iterationNumber++;

            System.out.println("This is iteration #" + iterationNumber);

            // printing the stack
            printStack(states);

            // creating an array list of variables
            final List<Variable> temp = new ArrayList<>();

            // printing the current grid
            currentGrid = printBasedOnState(states.peek(), grid);

            // checking if the current grid is solved
            if (isSolved(currentGrid)) {

                final List<String> hors = new ArrayList<>();
                final List<String> vers = new ArrayList<>();

                for (int i = 0; i < states.peek().variables.size(); i++) {
                    if (states.peek().variables.get(i).startX == states.peek().variables.get(i).endX) {
                        hors.add(states.peek().variables.get(i).currentValue);
                    } else {
                        vers.add(states.peek().variables.get(i).currentValue);
                    }
                }
                break;
            }

            // cloning the peek to the temp
            for (Variable v : states.peek().variables) {
                temp.add((Variable) v.clone());
            }

            // creating a new temp that holds the original temp values sorted by ID
            final List<Variable> temp2 = new ArrayList<>(temp);

            // sorting the new temp
            temp2.sort(sortByID);

            // getting the variable with the highest heuristic
            Variable choosenVariable = temp.getFirst();

            System.out.println(choosenVariable);
            int cc = choosenVariable.mostConstrainingVariableHeuristic;

            int minIndex = 0;

            int minValue = choosenVariable.mostConstrainedVariableHeuristic;

            for (int k = 0; k < temp.size(); k++) {
                if (temp.get(k).mostConstrainingVariableHeuristic == cc && temp.get(k).val != null) {
                    if (temp.get(k).mostConstrainedVariableHeuristic <= minValue) {
                        minIndex = k;
                        minValue = temp.get(k).mostConstrainedVariableHeuristic;
                    }
                } else {
                    break;
                }
            }

            // now min index represent the variable with both most constraining variable and
            // minimum remaining value
            choosenVariable = temp.get(minIndex);

            System.out.println(choosenVariable);

            // if the variable is already filled
            if (choosenVariable.mostConstrainedVariableHeuristic <= 0) {
                states.clear();
                break;
            }

            // this list will have all the used words
            List<String> newList = new ArrayList<>();

            // adding the used words in the grid
            newList.addAll(states.peek().wordsUsed);

            // adding the used words in that variable ( useful in backtracking )
            newList.addAll(triedShit.get(choosenVariable.uniqueID).words);

            // getting a list of all the values that can fit initially (with the current
            // constraints) and also sorted by their min conflict
            List<String> values = choosenVariable.assignAValue(newList, temp2);

            System.out.println(values);

            // if there's no values, then backtrack
            if (values == null) {
                triedShit.get(choosenVariable.uniqueID).words.clear();
                states.pop();
                printStack(states);
                continue;
            }

            boolean ff = false;

            // otherwise we will traverse the values to see who satisfy both the forward
            // checking and the arc consistency
            for (String value : values) {

                List<Variable> t1 = new ArrayList<>();

                for (Variable v : states.peek().variables) {
                    t1.add((Variable) v.clone());
                }
                Variable choosen = t1.get(minIndex);

                List<Variable> t2 = new ArrayList<>(t1);

                t2.sort(sortByID);

                boolean ffff = choosen.addConstraintsToNeighbors(value, t2, newList);

                // if the ith value satisfies both the arc consistency and the forward checking
                // then we will assign it safely
                if (ffff) {
                    choosen.currentValue = value;
                    triedShit.get(choosen.uniqueID).words.add(value);
                    t1.sort(heuristic1);
                    State nextState = new State(t1);
                    nextState.selectedWord = choosen.currentValue;
                    states.push(nextState);
                    ff = true;
                    break;

                }

            }

            // if the loop is done and there's no value that satisfy the forward checking
            // and the arc consistency, then we will backtrack
            if (!ff) {
                triedShit.get(choosenVariable.uniqueID).words.clear();
                states.pop();
                printStack(states);
            }
        }

        if (states.isEmpty()) {
            System.out.println("no solution found");
        } else {
            System.out.println("Optimal solution found");
        }
        // -------------------------

    }

    public static void generateAmericanStyle(char[][] arr) {

        if (arr[0].length <= 2)
            return;

        int start = arr[0].length / 2 - 1;
        int end = arr[0].length / 2 + 1;

        int base = (int) (Math.random() * (end - start + 1)) + start;

        boolean flag = false;

        for (int i = 0; i < arr.length; i++) {

            if (i <= arr.length * 0.2 || i >= arr.length * 0.8) {
                int[] array = new int[arr[0].length - Math.min(i, arr[0].length)];
                for (int k = 0; k < array.length; k++) {
                    array[k] = k;
                }

                for (int k = 0; k < 0.2 * arr[0].length - i; k++) {
                    int rand = (int) (Math.random() * (arr[0].length - i - k));
                    array[rand] = array[rand] + array[array.length - 1 - i];
                    array[array.length - 1] = array[rand] - array[array.length - 1 - i];
                    array[rand] = array[rand] - array[array.length - 1 - i];
                    arr[i][array[array.length - 1 - i]] = '*';
                }
            } else {

                if (!flag) {
                    arr[i][base] = '*';
                    flag = true;
                } else {
                    int rand = (int) (Math.random() * 2) + 1;
                    base = base - rand;
                    if (base >= 0) {
                        arr[i][base] = '*';
                    }
                }

            }

        }

        for (int i = 0; i < arr.length; i++) {
            for (int j = arr[0].length - Math.min(i, arr[0].length); j < arr[0].length; j++) {
                System.out.print("-");
                arr[i][j] = arr[arr.length - 1 - i][arr[0].length - 1 - j];
            }
            System.out.println();
        }

    }

    public static void generateBritishStyle(char[][] arr) {

        int[] random = new int[arr[0].length / 2];

        for (int i = 0; i < random.length; i++) {
            random[i] = i * 2;
        }

        Integer[] selected = new Integer[random.length / 4];

        int tracker = random.length - 1;

        for (int i = 0; i < selected.length; i++) {
            int s = (int) (Math.random() * (tracker + 1));
            selected[i] = random[s];

            random[tracker] = random[tracker] ^ random[s];
            random[s] = random[tracker] ^ random[s];
            random[tracker] = random[tracker] ^ random[s];

            tracker--;
        }

        Integer[] selected2 = new Integer[selected.length];
        for (int i = 0; i < selected.length; i++) {
            selected2[i] = selected[i] + 1;
        }

        Collections.shuffle(Arrays.asList(selected2));

        for (int i = 0; i < arr.length; i++) {
            if (i % 2 == 0) {
                if (!inIt(i, selected)) {
                    arr[i][(int) ((Math.random()) * arr[i].length)] = '*';
                }
            } else {
                for (int j = 0; j < arr[i].length; j++) {
                    if (j % 2 == 1) {
                        arr[i][j] = '*';
                    }
                }
                if (inIt(i, selected2)) {
                    int s = (int) ((Math.random()) * arr[i].length);
                    if (s % 2 == 1) {
                        s--;
                    }
                    arr[i][s] = '*';
                }

            }
        }

    }

    public static boolean inIt(int aim, Integer[] selected2) {
        for (final Integer integer : selected2) {
            if (integer == aim)
                return true;
        }
        return false;
    }

    public static void printStack(Stack<State> states) {

        System.out.println("-*-*-*-*-*-");
        for (State state : states) {
            System.out.println(state.wordsUsed);
            System.out.println();
        }
        System.out.println("-*-*-*-*-*-");

    }

    public static char[][] printBasedOnState(State s, char[][] grid) {

        char[][] newGrid = new char[grid.length][grid[0].length];

        for (int i = 0; i < grid.length; i++) {
            System.arraycopy(grid[i], 0, newGrid[i], 0, grid[0].length);
        }

        List<Variable> vars = s.variables;

        for (Variable var : vars) {
            assign(newGrid, var);
        }
        print(newGrid);
        System.out.println();
        System.out.println("______");
        System.out.println();
        return newGrid;
    }

    public static boolean isSolved(char[][] grid) {
        for (final char[] chars : grid) {
            for (int j = 0; j < grid[0].length; j++) {
                if (chars[j] == ' ')
                    return false;
            }
        }
        return true;
    }

    public static void assign(char[][] arr, Variable var) {
        if (var.startX == var.endX) {
            for (int i = 0; i < var.currentValue.length(); i++) {
                arr[var.startX][var.startY + i] = var.currentValue.charAt(i);
            }
        } else {
            for (int i = 0; i < var.currentValue.length(); i++) {
                arr[var.startX + i][var.startY] = var.currentValue.charAt(i);
            }
        }
    }

    public static Variable getByID(ArrayList<Variable> state, int id) {
        for (Variable variable : state) {
            if (variable.uniqueID == id)
                return variable;
        }
        return null;
    }

    public static void print(char[][] arr) {
        for (final char[] chars : arr) {
            for (final char aChar : chars) {
                System.out.print(aChar + ",");
            }
            System.out.println();
        }
    }

    public static void findHorSlots(List<Variable> horVariables, char[][] grid, Map<Integer, Value> newMap) {

        for (int i = 0; i < grid.length; i++) {
            int tracker = 0;
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == '*') {
                    if (tracker < j) {
                        int size = j - tracker;
                        if (size != 1) {
                            Value val = newMap.get(size);
                            if (val != null) {
                                Variable var = new Variable(i, tracker, i, j - 1, val);
                                System.out.println(var);
                                horVariables.add(var);
                            }
                        }
                    }
                    tracker = j + 1;
                }
            }
            if (tracker != grid[i].length) {

                int size = grid[i].length - tracker;
                if (size != 1) {
                    Value val = newMap.get(size);
                    if (val != null) {
                        Variable var = new Variable(i, tracker, i, grid[i].length - 1, val);
                        System.out.println(var);

                        horVariables.add(var);
                    }
                }

            }
        }

    }

    public static void findVerSlots(List<Variable> verVariables, char[][] grid, Map<Integer, Value> newMap) {

        for (int i = 0; i < grid[0].length; i++) {
            int tracker = 0;
            for (int j = 0; j < grid.length; j++) {
                if (grid[j][i] == '*') {
                    if (tracker < j) {

                        int size = j - tracker;
                        if (size != 1) {
                            Value val = newMap.get(size);
                            if (val != null) {
                                Variable var = new Variable(tracker, i, j - 1, i, val);
                                System.out.println(var);

                                verVariables.add(var);
                            }
                        }
                    }
                    tracker = j + 1;
                }
            }
            if (tracker != grid.length) {

                int size = grid.length - tracker;
                if (size != 1) {
                    Value val = newMap.get(size);
                    if (val != null) {
                        Variable var = new Variable(tracker, i, grid.length - 1, i, val);
                        System.out.println(var);

                        verVariables.add(var);
                    }
                }

            }
        }

    }


}
