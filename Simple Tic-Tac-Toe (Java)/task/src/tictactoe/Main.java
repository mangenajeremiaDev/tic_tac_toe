package tictactoe;

import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        new TicTacToe().play();
    }
}

enum Token {
    X('X'), O('O'), FREE('_');

    private final char symbol;

    Token(char symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return String.valueOf(symbol);
    }
}

enum State {
    X_WINS("X wins"), O_WINS("O wins"), DRAW("Draw"), GAME_NOT_FINISHED("Game not finished"), UNMAPPED("");

    private final String message;

    State(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}

class TicTacToe {
    private final Token[][] board = new Token[3][3];
    private final Scanner scanner = new Scanner(System.in);
    private int freeCells = 9;
    private Token currentPlayer = Token.X;
    private State gameState = State.UNMAPPED;

    TicTacToe() {
        resetBoard();
    }

    private void resetBoard() {
        for (Token[] row : board) {
            Arrays.fill(row, Token.FREE);
        }
    }

    private void displayBoard() {
        System.out.println("---------");
        for (Token[] row : board) {
            System.out.print("| ");
            for (Token cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println("|");
        }
        System.out.println("---------");
    }

    private boolean isValidMove(int row, int col) {
        return row >= 1 && row <= 3 && col >= 1 && col <= 3 && board[row - 1][col - 1] == Token.FREE;
    }

    private void playerMove() {
        while (true) {
            System.out.print("Enter your move: ");
            String input = scanner.nextLine();
            if (!input.matches("^\\d+\\s\\d+$")) {
                System.out.println("You should enter numbers!");
                continue;
            }
            int row = Integer.parseInt(input.split(" ")[0]);
            int col = Integer.parseInt(input.split(" ")[1]);
            if (!isValidMove(row, col)) {
                System.out.println("Invalid move! Try again.");
                continue;
            }
            board[row - 1][col - 1] = currentPlayer;
            freeCells--;
            currentPlayer = (currentPlayer == Token.X) ? Token.O : Token.X;
            break;
        }
    }

    private void checkGameState() {
        for (int i = 0; i < 3; i++) {
            if (checkLine(board[i][0], board[i][1], board[i][2])) return;
            if (checkLine(board[0][i], board[1][i], board[2][i])) return;
        }
        if (checkLine(board[0][0], board[1][1], board[2][2]) || checkLine(board[0][2], board[1][1], board[2][0])) return;
        gameState = (freeCells == 0) ? State.DRAW : State.GAME_NOT_FINISHED;
    }

    private boolean checkLine(Token a, Token b, Token c) {
        if (a == b && b == c && a != Token.FREE) {
            gameState = (a == Token.X) ? State.X_WINS : State.O_WINS;
            return true;
        }
        return false;
    }

    public void play() {
        displayBoard();
        while (gameState == State.UNMAPPED || gameState == State.GAME_NOT_FINISHED) {
            playerMove();
            displayBoard();
            checkGameState();
        }
        System.out.println(gameState);
    }
}
