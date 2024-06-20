import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

class MyPanel extends JPanel implements ActionListener {

    JButton btnArr[][] = new JButton[3][3];
    int arr[][] = new int[3][3];
    int turn = 0;

    JButton winBtn = new JButton();
    JButton replayBtn = new JButton();

    Color backgroundColor = new Color(255, 255, 255);
    Color btnColor = new Color(200, 0, 0);
    Color pushedBtnColor1 = new Color(0, 255, 200);
    Color pushedBtnColor2 = new Color(0, 200, 255);
    Color optionBtnColor = new Color(255, 200, 200);
    Font gameBtnFont = new Font("고딕", Font.PLAIN, 20);
    Font optionBtnFont = new Font("고딕", Font.BOLD, 15);

    MyPanel() {

        this.setLayout(null);
        this.setBackground(backgroundColor);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                btnArr[i][j] = new JButton();
                btnArr[i][j].setBackground(btnColor);
                btnArr[i][j].setFont(gameBtnFont);
                btnArr[i][j].setText(i * 3 + j + 1 + "");
                btnArr[i][j].setSize(100, 100);
                btnArr[i][j].setLocation(43 + j * 100, 30 + i * 100);
                btnArr[i][j].addActionListener(this);
                this.add(btnArr[i][j]);
                arr[i][j] = 0;
            }
        }

        winBtn.setText("Winner");
        winBtn.setSize(160, 40);
        winBtn.setLocation(43, 350);
        winBtn.setBackground(optionBtnColor);
        winBtn.setFont(optionBtnFont);
        winBtn.addActionListener(this);
        this.add(winBtn);

        replayBtn.setText("RePlay");
        replayBtn.setSize(120, 40);
        replayBtn.setLocation(223, 350);
        replayBtn.setBackground(optionBtnColor);
        replayBtn.setFont(optionBtnFont);
        replayBtn.addActionListener(this);
        this.add(replayBtn);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (e.getSource() == btnArr[i][j]) {
                    if (turn % 2 == 0) {
                        btnArr[i][j].setText("O");
                        btnArr[i][j].setBackground(pushedBtnColor1);
                        arr[i][j] = 1;
                    } else {
                        btnArr[i][j].setText("X");
                        btnArr[i][j].setBackground(pushedBtnColor2);
                        arr[i][j] = 2;
                    }
                    btnArr[i][j].setEnabled(false);
                    turn++;
                }
            }
        }

        if (e.getSource() == replayBtn) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    btnArr[i][j].setText(i * 3 + j + 1 + "");
                    btnArr[i][j].setEnabled(true);
                    btnArr[i][j].setBackground(btnColor);
                    winBtn.setBackground(btnColor);
                    arr[i][j] = 0;
                }
            }
            winBtn.setText("Winner");
            turn = 0;
        }

        if (check() == 1) {
            winBtn.setText("Player1 WIN!");
            winBtn.setBackground(pushedBtnColor1);
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    btnArr[i][j].setEnabled(false);
                }
            }
        }
        if (check() == 2) {
            winBtn.setText("Player2 WIN!");
            winBtn.setBackground(pushedBtnColor2);
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    btnArr[i][j].setEnabled(false);
                }
            }
        }
    }

    int check() {

        int j = 0;
        for (int i = 0; i < 3; i++) {
            if (arr[i][j] == 1 && arr[i][j + 1] == 1 && arr[i][j + 2] == 1) {
                return 1;
            } else if (arr[i][j] == 2 && arr[i][j + 1] == 2 && arr[i][j + 2] == 2) {
                return 2;
            }
        }

        int i = 0;
        for (j = 0; j < 3; j++) {
            if (arr[i][j] == 1 && arr[i + 1][j] == 1 && arr[i + 2][j] == 1) {
                return 1;
            } else if (arr[i][j] == 2 && arr[i + 1][j] == 2 && arr[i + 2][j] == 2) {
                return 2;
            }
        }

        i = 0;
        if (arr[i][i] == 1 && arr[i + 1][i + 1] == 1 && arr[i + 2][i + 2] == 1) {
            return 1;
        } else if (arr[i][i] == 2 && arr[i + 1][i + 1] == 2 && arr[i + 2][i + 2] == 2) {
            return 2;
        }

        i = 0;
        if (arr[i][i + 2] == 1 && arr[i + 1][i + 1] == 1 && arr[i + 2][i] == 1) {
            return 1;
        } else if (arr[i][i + 2] == 2 && arr[i + 1][i + 1] == 2 && arr[i + 2][i] == 2) {
            return 2;
        }

        return 0;
    }
}

public class Tic {

    public static void main(String[] args) {

        JFrame frame = new JFrame("Tic Tac Toe");

        frame.setSize(400, 460);

        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension size = tk.getScreenSize();
        frame.setLocation((size.width - 400) / 2, (size.height - 460) / 2);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        frame.add(new MyPanel());

        frame.revalidate();
    }
}
