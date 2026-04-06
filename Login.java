package atm.simulator.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;

public class Login extends JFrame implements ActionListener {

    JButton login, signup, clear;
    JTextField cardTextField;
    JPasswordField pinTextField;
    JCheckBox showPin;

    Login() {

        setTitle("ATM Banking System");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Background Image
        JPanel panel = new JPanel() {
            Image bg = new ImageIcon(
                    getClass().getResource("/atm/simulator/system/icons/bg3.png")
            ).getImage();

            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);

                // Dark overlay
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(new Color(0,0,0,110));
                g2.fillRect(0,0,getWidth(),getHeight());
            }
        };

        panel.setLayout(null);
        setContentPane(panel);

        // Glass Card
        JPanel card = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                // Shadow
                g2.setColor(new Color(0,0,0,80));
                g2.fillRoundRect(10,10,getWidth()-10,getHeight()-10,20,20);

                // Card
                g2.setColor(new Color(255,255,255,235));
                g2.fillRoundRect(0,0,getWidth()-10,getHeight()-10,20,20);

                super.paintComponent(g);
            }
        };

        card.setOpaque(false);
        card.setLayout(null);
        card.setBounds(250, 110, 420, 330);
        panel.add(card);

        // Title
        JLabel text = new JLabel("ATM Banking System");
        text.setFont(new Font("Segoe UI", Font.BOLD, 24));
        text.setHorizontalAlignment(JLabel.CENTER);
        text.setBounds(60, 25, 300, 30);
        card.add(text);

        // Card Number
        JLabel cardno = new JLabel("Card Number");
        cardno.setFont(new Font("Segoe UI",Font.BOLD,14));
        cardno.setBounds(40, 90, 120, 25);
        card.add(cardno);

        cardTextField = new JTextField();
        cardTextField.setBounds(160, 90, 210, 35);
        styleField(cardTextField);
        card.add(cardTextField);

        // PIN
        JLabel pin = new JLabel("PIN");
        pin.setFont(new Font("Segoe UI",Font.BOLD,14));
        pin.setBounds(40, 145, 120, 25);
        card.add(pin);

        pinTextField = new JPasswordField();
        pinTextField.setBounds(160, 145, 210, 35);
        styleField(pinTextField);
        card.add(pinTextField);

        // Show PIN
        showPin = new JCheckBox("Show PIN");
        showPin.setBounds(160, 185, 120, 20);
        showPin.setOpaque(false);
        card.add(showPin);

        showPin.addActionListener(e -> {
            pinTextField.setEchoChar(showPin.isSelected() ? (char)0 : '*');
        });

        // Buttons
        login = createButton("LOGIN", 40, 240, new Color(0,140,255));
        clear = createButton("CLEAR", 155, 240, Color.GRAY);
        signup = createButton("SIGN UP", 270, 240, new Color(0,180,140));

        card.add(login);
        card.add(clear);
        card.add(signup);

        login.addActionListener(this);
        clear.addActionListener(this);
        signup.addActionListener(this);

        getRootPane().setDefaultButton(login);

        setVisible(true);
    }

    void styleField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.BOLD, 14));
        field.setHorizontalAlignment(JTextField.CENTER);
        field.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createLineBorder(new Color(0,140,255),2));
            }

            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            }
        });
    }

    JButton createButton(String text, int x, int y, Color color) {
        JButton btn = new JButton(text);
        btn.setBounds(x, y, 110, 40);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(color.darker());
            }

            public void mouseExited(MouseEvent e) {
                btn.setBackground(color);
            }
        });

        return btn;
    }

    public void actionPerformed(ActionEvent ae) {

        if (ae.getSource() == clear) {
            cardTextField.setText("");
            pinTextField.setText("");
        }

        else if (ae.getSource() == login) {

            String cardno = cardTextField.getText().trim();
            String pin = new String(pinTextField.getPassword()).trim();

            if (!cardno.matches("\\d{16}")) {
                JOptionPane.showMessageDialog(null, "Enter valid 16-digit Card Number");
                return;
            }

            if (!pin.matches("\\d{4}")) {
                JOptionPane.showMessageDialog(null, "PIN must be 4 digits");
                return;
            }

            try {
                Conn c = new Conn();

                String query = "select * from signupthree where card_number = '" 
                                + cardno + "' and pin = '" + pin + "'";

                ResultSet rs = c.s.executeQuery(query);

                if (rs.next()) {
                    setVisible(false);
                    new Transactions(cardno).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Incorrect Card Number or PIN");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        else if (ae.getSource() == signup) {
            setVisible(false);
            new SignupOne().setVisible(true);
        }
    }

    public static void main(String args[]) {
        new Login();
    }
}