import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Game implements Runnable {

    public static final int WIDTH = 500;
    public static final int HEIGHT = 580;


    private GameCourt board;

    public Game() {
        board = new GameCourt();
    }

    public void run(){
        final JFrame frame = new JFrame ("BOBA SWEEPER");
        frame.setSize(WIDTH + 1, HEIGHT + 2);

        final JPanel option_panel = new JPanel();

        frame.getContentPane().add(board, BorderLayout.CENTER);


        final JButton restart = new JButton("restart");
        option_panel.add(restart);
        frame.getContentPane().add(option_panel, BorderLayout.NORTH);
        restart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                board.reset();
                frame.remove(board);
                board = new GameCourt();
                frame.getContentPane().add(board, BorderLayout.CENTER);
                frame.repaint();
                frame.revalidate();
            }
        });


        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }
}

