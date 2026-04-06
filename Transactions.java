package atm.simulator.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Transactions extends JFrame implements ActionListener {

    JButton deposit, withdraw, fastCash, miniStatement, pinChange, balanceEnquiry, exit, netBanking;
    String cardno;

    Transactions(String cardno) {
        this.cardno = cardno;

        setTitle("ATM Dashboard");

        // Modern Dark Gradient Background
        JPanel background = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;

                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(8, 32, 50),
                        getWidth(), getHeight(),
                        new Color(0, 92, 151)
                );

                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        background.setLayout(new GridBagLayout());
        setContentPane(background);

        // Dark Card
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setPreferredSize(new Dimension(720, 430));
        card.setBackground(new Color(25, 25, 25));
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        background.add(card);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel welcome = new JLabel("Welcome");
        welcome.setForeground(Color.WHITE);
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JLabel cardLabel = new JLabel(
                "Card: XXXX-XXXX-XXXX-" +
                        cardno.substring(cardno.length() - 4)
        );
        cardLabel.setForeground(Color.LIGHT_GRAY);

        JPanel left = new JPanel(new GridLayout(2,1));
        left.setOpaque(false);
        left.add(welcome);
        left.add(cardLabel);

        JLabel heading = new JLabel("Select Your Transaction", SwingConstants.CENTER);
        heading.setForeground(Color.WHITE);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 22));

        header.add(left, BorderLayout.WEST);
        header.add(heading, BorderLayout.CENTER);

        card.add(header, BorderLayout.NORTH);

        // Buttons Panel
        JPanel grid = new JPanel(new GridLayout(4, 2, 15, 15));
        grid.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        grid.setOpaque(false);

        Color btnColor = new Color(45, 45, 45);

        deposit = createButton("Deposit", btnColor);
        withdraw = createButton("Withdraw", btnColor);
        fastCash = createButton("Fast Cash", btnColor);
        miniStatement = createButton("Mini Statement", btnColor);
        pinChange = createButton("PIN Change", btnColor);
        balanceEnquiry = createButton("Balance", btnColor);
        netBanking = createButton("Net Banking", btnColor);
        exit = createButton("Exit", btnColor);

        grid.add(deposit);
        grid.add(withdraw);
        grid.add(fastCash);
        grid.add(miniStatement);
        grid.add(pinChange);
        grid.add(balanceEnquiry);
        grid.add(netBanking);
        grid.add(exit);

        card.add(grid, BorderLayout.CENTER);

        JLabel status = new JLabel("Secure Banking Session Active");
        status.setForeground(Color.GRAY);
        status.setHorizontalAlignment(SwingConstants.CENTER);

        card.add(status, BorderLayout.SOUTH);

        // Actions
        deposit.addActionListener(this);
        withdraw.addActionListener(this);
        fastCash.addActionListener(this);
        miniStatement.addActionListener(this);
        pinChange.addActionListener(this);
        balanceEnquiry.addActionListener(this);
        netBanking.addActionListener(this);
        exit.addActionListener(this);

        setSize(900, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(14, 10, 14, 10));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(0,120,215));
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(color);
            }
        });

        return btn;
    }

    public void actionPerformed(ActionEvent ae) {

        if (ae.getSource() == deposit) navigate(new Deposit(cardno));
        else if (ae.getSource() == withdraw) navigate(new Withdraw(cardno));
        else if (ae.getSource() == balanceEnquiry) navigate(new BalanceEnquiry(cardno));
        else if (ae.getSource() == fastCash) navigate(new FastCash(cardno));
        else if (ae.getSource() == miniStatement) navigate(new MiniStatement(cardno));
        else if (ae.getSource() == pinChange) navigate(new PinChange(cardno));
        else if (ae.getSource() == netBanking) navigate(new NetBanking(cardno));
        else if (ae.getSource() == exit) {
            setVisible(false);
            new Login().setVisible(true);
        }
    }

    void navigate(JFrame frame) {
        setVisible(false);
        frame.setVisible(true);
    }
}