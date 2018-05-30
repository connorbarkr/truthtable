import java.util.Scanner;

enum State {
  START, AND, OR, END, ERR;
}

public class truthtable {

  public static void main(String []args) {
    //get input
    Scanner scanner = new Scanner(System.in);
    System.out.println("Enter an expression:");
    char[] expression = scanner.next().toLowerCase().toCharArray();
    char[] variables = new char[] {'\0'};
    int rows;
    int currentDepth = 0;
    int result;
    int[][] values;
    StringBuilder rBuilder = new StringBuilder();
    StringBuilder dBuilder = new StringBuilder();
    String rowString;
    String divider;

    //main parsing sequence
    for (int i = 0; i < expression.length; i++) {
      boolean isAlpha = isAlpha(expression[i]);
      boolean unique = true;
      if (isAlpha) {
        for (int j = 0; j < variables.length; j++) {
          if (variables[j] == expression[i]) {
            unique = false;
            break;
          }
        }
        for (int j = 0; j < variables.length && unique; j++) {
          if (variables[0] == '\0') {
            variables[0] = expression[i];
          }
          else {
            //dynamic memory allocation
            char[] oldvariables = new char[variables.length];
            for (int k = 0; k < variables.length; k++) {
              oldvariables[k] = variables[k];
            }
            variables = new char[oldvariables.length + 1];
            for (int k = 0; k < oldvariables.length; k++) {
              variables[k] = oldvariables[k];
            }
            variables[variables.length - 1] = expression[i];
            unique = false;
          }
        }
      }
    }

    //truth table creation
    rows = (int) Math.pow(2, variables.length);
    values = new int[variables.length][rows];

    for (int i = 0; i < variables.length; i++) {
      int num = 0;
      int toggle = rows / ((int) Math.pow(2, i + 1));
      for (int j = 0; j < rows; j++) {
        if (j != 0 && j % toggle == 0) {
          num = flip(num);
        }
        values[i][j] = num;
      }
    }

    //build values and dividers
    rBuilder.append('|');
    dBuilder.append('+');
    for (int i = 0; i < variables.length; i++) {
      rBuilder.append(variables[i]);
      dBuilder.append('-');
      rBuilder.append('|');
      if (i != variables.length - 1) {
        dBuilder.append('-');
      }
    }
    dBuilder.append('+');
    rowString = rBuilder.toString();
    divider = dBuilder.toString();

    // print values and dividers
    System.out.println(divider);
    System.out.println(rowString);
    System.out.println(divider);

    for (int i = 0; i < rows; i++) {
      rBuilder = new StringBuilder();
      rBuilder.append('|');
      for (int j = 0; j < variables.length; j++) {
        rBuilder.append(intToChar(values[j][i]));
        rBuilder.append('|');
      }
      rowString = rBuilder.toString();
      System.out.println(rowString);
    }
    System.out.println(divider);

    char[] testVars = new char[] {'a', 'b', 'c'};
    int[] testVals = new int[] {1, 1, 0};
    char[] testExpression = new char[] {'a', '+', 'b', 'c'};
    System.out.println(parseExpression(testVars, testVals, testExpression));

  }

  public static int parseExpression(char[] vars, int[] vals, char[] expression) {
    State state = State.START;
    boolean result;
    if (!isAlpha(expression[0]) && expression[0] != '(') {
      System.out.println("ERR: Invalid expression");
      return null;
    }

    for (int i = 0; i < expression.length; i++) {
      char c = expression[i];
      char n;
      if (i != expression.length - 1) { n = expression[i + 1]; }
      else { n = '\0'; }

      switch (state) {
        case START:
          result = valueOf(c, vars, vals);
          if (n == '\0') {
            state = State.END;
            break;
          } else if (isAlpha(n)) {
            state = State.AND;
            break;
          } else if (n == '+') {
            state = State.OR;
            break;
          } else {
            System.out.println("ERR: Invalid expression");
            return;
          }
        case AND:
          result = result && valueOf(c, vars, vals);
          if (n == '\0') {
            state = State.END;
            break;
          } else if (isAlpha(n)) {
            state = State.AND;
            break;
          } else if (n == '+') {
            state = State.OR;
            break;
          } else {
            System.out.println("ERR: Invalid expression");
            return 2;
          }
        case OR:
          result = result || valueOf(c, vars, vals);
          if (n == '\0') {
            state = State.END;
            break;
          } else if (isAlpha(n)) {
            state = State.AND;
            break;
          } else if (n == '+') {
            state = State.OR;
            break;
          } else {
            System.out.println("ERR: Invalid expression");
            return null;
          }
        case END:
          return result;
        default:
          System.out.println("ERR: This should never happen. Ever.");
          return null;
      }
    }
  }

  public static boolean valueOf(char var, char[] vars, int[] vals) {
    for (int i = 0; i < vars.length; i++) {
      if (vars[i] == var) {
        return (vals[i] == 1);
      }
      return false;
    }
  }

  public static boolean isAlpha(char c) {
    return (c >= 97 && c <= 122);
  }

  public static char intToChar(int i) {
    switch (i) {
      case 0:
        return '0';
      case 1:
        return '1';
      default:
        return '\0';
    }
  }

  public static int flip(int i) {
    switch (i) {
      case 0:
        return 1;
      case 1:
        return 0;
      default:
        return 0;
    }
  }
}
