import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Exercise {

    private JList JListOfText;
    private JButton ButtonOfStart;
    private JLabel LabelOfNum;
    private JLabel LabelOfWords;
    private JLabel LabelOfPunct;
    private JPanel root;
    private JLabel digitsLabel;
    private JLabel prepinLabel;
    private JLabel wordLabel;

    private static class CountResult {
        public List<String> digits = new ArrayList<>();
        public List<String> prepin = new ArrayList<>();;
        public List<String> words = new ArrayList<>();;
    }

    public static CountResult count_all(String str){
        CountResult res = new CountResult();

        int state = 0; // 0 - пустое состояние, 1 - число, 2 - знак препинания, 3 - слово
        StringBuilder buffer = new StringBuilder();

        for (int i = 0; i <= str.length(); i++) {
            char nowChar = (i < str.length()) ? str.charAt(i) : ' '; // добавляем пробел в конце для завершения последнего токена

            if (Character.isDigit(nowChar)) {
                if (state == 1) {
                    buffer.append(nowChar);
                } else {
                    flushBuffer(res, state, buffer);
                    buffer.append(nowChar);
                    state = 1;
                }
            } else if (Character.isLetter(nowChar)) {
                if (state == 3) {
                    buffer.append(nowChar);
                } else {
                    flushBuffer(res, state, buffer);
                    buffer.append(nowChar);
                    state = 3;
                }
            } else if (isPunctuation(nowChar)) {
                if (state == 2) {
                    buffer.append(nowChar);
                } else {
                    flushBuffer(res, state, buffer);
                    buffer.append(nowChar);
                    state = 2;
                }
            } else { // пробел или другой разделитель
                flushBuffer(res, state, buffer);
                state = 0;
            }
        }

        return res;
    }

    private static void flushBuffer(CountResult res, int state, StringBuilder buffer) {
        if (buffer.length() == 0) return;

        String token = buffer.toString();
        switch (state) {
            case 1 -> res.digits.add(token);
            case 2 -> res.prepin.add(token);
            case 3 -> res.words.add(token);
        }
        buffer.setLength(0);
    }

    private static boolean isPunctuation(char ch) {
        return "!.,;:?".indexOf(ch) != -1;
    }

    public Exercise() {
        ButtonOfStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String str = (String) JListOfText.getSelectedValue();
                System.out.println(str);

                CountResult c = count_all(str);
                digitsLabel.setText(String.valueOf(c.digits));
                prepinLabel.setText(String.valueOf(c.prepin));
                wordLabel.setText(String.valueOf(c.words));
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Exercise");
        frame.setContentPane(new Exercise().root);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
