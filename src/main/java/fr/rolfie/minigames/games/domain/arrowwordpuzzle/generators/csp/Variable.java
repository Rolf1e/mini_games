package fr.rolfie.minigames.games.domain.arrowwordpuzzle.generators.csp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Variable implements Cloneable {

    Value val;

    static int counter = 0;

    List<Constraints> cons;
    List<Constraints> arcCons;

    List<String> words;
    int startX;
    int startY;
    int endX;
    int endY;

    int uniqueID;

    List<IntersectionInfo> anotherN;

    int[][] mostConstrainingValueHeuristic;

    String currentValue = "";
    int mostConstrainingVariableHeuristic;
    int mostConstrainedVariableHeuristic;

    int indexOfUsedWord;

    Variable(int startX, int startY, int endX, int endY, Value val) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        if (startX == endX) {
            for (int i = 0; i < (endY - startY + 1); i++) {
                currentValue += " ";
            }
        } else {
            for (int i = 0; i < (endX - startX + 1); i++) {
                currentValue += " ";
            }
        }
        this.val = val;
        cons = new ArrayList<>();
        arcCons = new ArrayList<>();
        anotherN = new ArrayList<>();
        words = new ArrayList<>();
        uniqueID = counter++;
        System.out.println(val);
        if (val != null) {
            mostConstrainingValueHeuristic = new int[26][val.size];
            setMostConstrainingValueHeuristic(new ArrayList<>());
        }

        indexOfUsedWord = -1;
    }

    public boolean arcCons(List<String> wordsUsed, List<Variable> sortedStateBasedOnID) {

        List<String> availableValues = findTheListOfAvailableWords();

        for (String s : wordsUsed) {
            availableValues.remove(s);
        }

        int[] arr = new int[val.size];

        Arrays.fill(arr, -1);

        for (int i = 0; i < anotherN.size(); i++) {
            if (sortedStateBasedOnID.get(anotherN.get(i).uniqueID).mostConstrainingVariableHeuristic != -1) {
                arr[anotherN.get(i).sourceIndex] = i;

                IntersectionInfo temp = anotherN.get(i);
                Variable neighbor = sortedStateBasedOnID.get(temp.uniqueID);
                for (int j = 0; j < 26; j++) {
                    System.out.println(Arrays.deepToString(neighbor.mostConstrainingValueHeuristic));
                    if (neighbor.mostConstrainingValueHeuristic[j][temp.destinationIndex] != 0) {
                        char c = (char) (j + 'a');
                        addArcConstraints(c, temp.sourceIndex);
                    }
                }
                boolean flagg = updateTheVariable(wordsUsed);
                if (!flagg) {
                    return false;
                }
                for (int j = 0; j < 26; j++) {
                    if (mostConstrainingValueHeuristic[j][temp.sourceIndex] != 0) {
                        char c = (char) (j + 'a');
                        neighbor.addArcConstraints(c, temp.destinationIndex);
                    }
                }

                flagg = neighbor.updateTheVariable(wordsUsed);
                if (!flagg) {
                    return false;
                }
            }
        }
        return true;

    }

    public List<String> assignAValue(List<String> wordsUsed, List<Variable> sortedStateBasedOnID) {

        List<String> availableValues = findTheListOfAvailableWords();

        for (String s : wordsUsed) {
            availableValues.remove(s);
        }
        int[] arr = new int[val.size];

        Arrays.fill(arr, -1);

        int cc = 0;

        for (int i = 0; i < anotherN.size(); i++) {
            if (sortedStateBasedOnID.get(anotherN.get(i).uniqueID).mostConstrainingVariableHeuristic != -1) {
                arr[anotherN.get(i).sourceIndex] = i;
                cc++;
            }
        }
        final List<FitValues> fitVals = new ArrayList<>();
        final List<String> values = new ArrayList<>();

        if (cc == 0) {
            if (availableValues.isEmpty()) {
                return null;
            }
            return availableValues;
        }

        for (int i = 0; i < availableValues.size(); i++) {

            String str = availableValues.get(i);
            System.out.println(str);

            int numberOfAvailableNeighborsOptions = 0;

            boolean flag = true;
            for (int j = 0; j < str.length(); j++) {

                char ch = str.charAt(j);
                System.out.println(ch);

                if (arr[j] != -1) {
                    IntersectionInfo temp = anotherN.get(arr[j]);
                    System.out.println(temp);
                    Variable neighbor = sortedStateBasedOnID.get(temp.uniqueID);
                    System.out.println(neighbor);
                    int number = neighbor.mostConstrainingValueHeuristic[ch - 'a'][temp.destinationIndex];
                    System.out.println(number);
                    if (number != 0) {
                        numberOfAvailableNeighborsOptions += number;
                    } else {
                        flag = false;
                        break;
                    }
                }
            }
            if (flag) {
                System.out.println("YESSSSSS");
                fitVals.add(new FitValues(numberOfAvailableNeighborsOptions, i));

            }
        }

        SortingTheFitValues sortParam = new SortingTheFitValues();
        fitVals.sort(sortParam);
        if (fitVals.isEmpty()) {
            return null;
        } else {
            for (FitValues fitVal : fitVals) {
                values.add(availableValues.get(fitVal.index));
            }
            return values;
        }

    }

    public boolean setMostConstrainingValueHeuristic(List<String> wordsUsed) {
        final List<String> availableValues = findTheListOfAvailableWords();
        System.out.println("acccccccccccccces");
        for (String s : wordsUsed) {
            availableValues.remove(s);
        }

        setMostConstrainedVariableHeuristic(availableValues.size());

        for (int i = 0; i < availableValues.size(); i++) {
            for (int j = 0; j < availableValues.get(i).length(); j++) {
                boolean f2 = false;

                boolean there = false;
                for (Constraints arcCon : arcCons) {
                    if (arcCon.position == j) {
                        there = true;
                        if (arcCon.letter == availableValues.get(i).charAt(j)) {
                            f2 = true;
                        }
                    }
                }

                if (there) {
                    if (!f2) {
                        availableValues.remove(i);
                        i--;
                        break;
                    }

                }
            }
        }

        for (String str : availableValues) {
            for (int j = 0; j < str.length(); j++) {
                char ch = str.charAt(j);
                int indexOfChar = ch - 'a';
                mostConstrainingValueHeuristic[indexOfChar][j]++;
                System.out.println("hyes");
            }
        }

        return !availableValues.isEmpty();
    }

    public List<String> findTheListOfAvailableWords() {

        final List<String> availableValues;
        if (!cons.isEmpty()) {
            availableValues = new ArrayList<>(val.get(cons.getFirst().letter, cons.getFirst().position));
            for (int i = 1; i < cons.size(); i++) {
                availableValues.retainAll(val.get(cons.get(i).letter, cons.get(i).position));
            }
        } else {
            availableValues = new ArrayList<>(val.listOfWords);
        }

        return availableValues;
    }

    Variable(int startX, int startY, int endX, int endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        if (startX == endX) {
            for (int i = 0; i < (endY - startY + 1); i++) {
                currentValue += " ";
            }
        } else {
            for (int i = 0; i < (endX - startX + 1); i++) {
                currentValue += " ";
            }
        }
        anotherN = new ArrayList<>();

        uniqueID = counter++;

    }

    public void setMostConstrainingVariableHeuristic2() {
        this.mostConstrainingVariableHeuristic = anotherN.size();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {

        Variable var = (Variable) super.clone();

        var.cons = new ArrayList<>();
        for (Constraints c : cons) {
            var.cons.add((Constraints) c.clone());
        }
        var.arcCons = new ArrayList<>();
        for (Constraints c : arcCons) {
            var.arcCons.add((Constraints) c.clone());
        }

        var.currentValue = new String(currentValue);

        return var;
    }

    public void setMostConstrainedVariableHeuristic(int mostConstrainedVariableHeuristic) {
        this.mostConstrainedVariableHeuristic = mostConstrainedVariableHeuristic;
    }

    public void addConstraints(char letter, int position) {
        cons.add(new Constraints(letter, position));
    }

    public void addArcConstraints(char letter, int position) {
        arcCons.add(new Constraints(letter, position));
    }

    public boolean addConstraintsToNeighbors(String assigned, List<Variable> sortedStateBasedOnID,
                                             List<String> wordsUsed) {

        int[] arr = new int[val.size];

        Arrays.fill(arr, -1);

        for (int i = 0; i < anotherN.size(); i++) {
            if (sortedStateBasedOnID.get(anotherN.get(i).uniqueID).mostConstrainingVariableHeuristic != -1) {
                arr[anotherN.get(i).sourceIndex] = i;
            }
        }

        for (int i = 0; i < assigned.length(); i++) {
            char ch = assigned.charAt(i);

            if (arr[i] != -1) {
                IntersectionInfo temp = anotherN.get(arr[i]);

                Variable neighbor = sortedStateBasedOnID.get(temp.uniqueID);
                neighbor.addConstraints(ch, temp.destinationIndex);
                neighbor.currentValue = neighbor.currentValue.substring(0, temp.destinationIndex) + ch
                        + neighbor.currentValue.substring(temp.destinationIndex + 1);
                neighbor.mostConstrainingVariableHeuristic--;
                boolean flagg = neighbor.updateTheVariable(wordsUsed);
                if (!flagg) {
                    return false;
                }
                flagg = neighbor.arcCons(wordsUsed, sortedStateBasedOnID);
                if (!flagg) {
                    return false;
                }
            }
        }
        mostConstrainingVariableHeuristic = -1;
        return true;
    }

    public boolean updateTheVariable(List<String> wordsUsed) {
        if (val != null) {
            mostConstrainingValueHeuristic = new int[26][val.size];
            return setMostConstrainingValueHeuristic(wordsUsed);
        }
        return false;
    }

    public void addAnotherN(IntersectionInfo info) {
        anotherN.add(info);
    }

    public int[] intersects(Variable var) {

        int[] info = new int[2];

        if (var.startX == var.endX && startX == endX) {
            return null;
        }

        if (var.startY == var.endY && startY == endY) {
            return null;
        }

        if (startX == endX) {
            if (startY <= var.startY && endY >= var.startY) {
                if (var.startX <= startX && var.endX >= startX) {
                    info[0] = var.startY - startY;
                    info[1] = startX - var.startX;

                    return info;
                } else
                    return null;
            } else
                return null;
        }

        if (startY == endY) {
            if (startX <= var.startX && endX >= var.startX) {
                if (var.startY <= startY && var.endY >= startY) {
                    info[0] = var.startX - startX;
                    info[1] = startY - var.startY;

                    return info;
                } else
                    return null;
            } else
                return null;
        }

        return null;
    }

    public String toString() {
        return mostConstrainingVariableHeuristic + "|" + mostConstrainedVariableHeuristic + "|(" + startX + "," + startY
                + ") to (" + endX + " ," + endY + ")  ->  {" + currentValue + "}" + "  " + uniqueID;
    }

    @Override
    public int hashCode() {
        final int prime = 997;
        int result = 1;
        result = prime * result + endX;
        result = prime * result + endY;
        result = prime * result + startX;
        result = prime * result + startY;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Variable other = (Variable) obj;
        if (endX != other.endX)
            return false;
        if (endY != other.endY)
            return false;
        if (startX != other.startX)
            return false;
        if (startY != other.startY)
            return false;
        return true;
    }

}
