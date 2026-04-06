package atm.simulator.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SignupTwo extends JFrame implements ActionListener {

    JTextField panTextField, aadharTextField;
    JRadioButton seniorYes, seniorNo, accYes, accNo;
    JComboBox<String> religionBox, categoryBox, incomeBox, educationBox, occupationBox;
    JButton next;
    String formno;

    SignupTwo(String formno) {
        this.formno = formno;

        // ===== BACKGROUND =====
        JPanel panel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g;

                int w = getWidth();
                int h = getHeight();

                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(15,32,39),
                        w, h, new Color(44,83,100)
                );

                g2.setPaint(gp);
                g2.fillRect(0,0,w,h);

                g2.setColor(new Color(255,255,255,40));
                g2.fillOval(-200,-200,500,500);
                g2.fillOval(w-300,h-300,400,400);
            }
        };

        panel.setLayout(null);
        setContentPane(panel);

        // ===== GLASS CARD =====
        JPanel card = new JPanel(){
            protected void paintComponent(Graphics g){
                Graphics2D g2 = (Graphics2D) g;

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(new Color(0,0,0,70));
                g2.fillRoundRect(15,15,getWidth()-15,getHeight()-15,30,30);

                g2.setColor(new Color(255,255,255,180));
                g2.fillRoundRect(0,0,getWidth()-15,getHeight()-15,30,30);
            }
        };

        card.setLayout(null);
        card.setOpaque(false);
        card.setBounds(200,40,700,600);
        panel.add(card);

        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                card.setLocation(
                        (getWidth()-card.getWidth())/2,
                        (getHeight()-card.getHeight())/2
                );
            }
        });

        JLabel step = new JLabel("Step 2 of 3");
        step.setFont(new Font("Segoe UI", Font.BOLD, 14));
        step.setBounds(520, 10, 120, 20);
        card.add(step);

        JLabel title = new JLabel("Additional Details");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setBounds(220, 40, 300, 30);
        card.add(title);

        int y = 100;

        card.add(label("Religion", 50, y));
        religionBox = combo(new String[]{"Hindu","Muslim","Christian","Sikh","Other"},250,y);
        card.add(religionBox);

        y+=50;
        card.add(label("Category",50,y));
        categoryBox = combo(new String[]{"General","OBC","SC","ST","Other"},250,y);
        card.add(categoryBox);

        y+=50;
        card.add(label("Income",50,y));
        incomeBox = combo(new String[]{"<1,50,000","<2,50,000","<5,00,000","Above 5,00,000"},250,y);
        card.add(incomeBox);

        y+=50;
        card.add(label("Education",50,y));
        educationBox = combo(new String[]{"Non-Graduate","Graduate","Post-Graduate","Doctorate"},250,y);
        card.add(educationBox);

        y+=50;
        card.add(label("Occupation",50,y));
        occupationBox = combo(new String[]{"Salaried","Self-Employed","Business","Student","Retired"},250,y);
        card.add(occupationBox);

        y+=50;
        card.add(label("PAN Number",50,y));
        panTextField = field(250,y);
        card.add(panTextField);

        y+=50;
        card.add(label("Aadhaar Number",50,y));
        aadharTextField = field(250,y);
        card.add(aadharTextField);

        y+=50;
        card.add(label("Senior Citizen",50,y));
        seniorYes = radio("Yes",250,y);
        seniorNo = radio("No",330,y);
        ButtonGroup g1 = new ButtonGroup();
        g1.add(seniorYes); 
        g1.add(seniorNo);
        card.add(seniorYes); 
        card.add(seniorNo);

        y+=50;
        card.add(label("Existing Account",50,y));
        accYes = radio("Yes",250,y);
        accNo = radio("No",330,y);
        ButtonGroup g2 = new ButtonGroup();
        g2.add(accYes); 
        g2.add(accNo);
        card.add(accYes); 
        card.add(accNo);

        y+=70;
        next = new JButton("NEXT ➜");
        next.setBounds(250,y,160,45);
        next.setBackground(new Color(15,32,39));
        next.setForeground(Color.WHITE);
        next.setFont(new Font("Segoe UI",Font.BOLD,15));
        next.addActionListener(this);
        card.add(next);

        setSize(1000,750);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    JLabel label(String t,int x,int y){
        JLabel l=new JLabel(t);
        l.setBounds(x,y,180,25);
        l.setFont(new Font("Segoe UI",Font.BOLD,14));
        return l;
    }

    JComboBox<String> combo(String[] items,int x,int y){
        JComboBox<String> c=new JComboBox<>(items);
        c.setBounds(x,y,300,30);
        return c;
    }

    JTextField field(int x,int y){
        JTextField f=new JTextField();
        f.setBounds(x,y,300,30);
        return f;
    }

    JRadioButton radio(String t,int x,int y){
        JRadioButton r=new JRadioButton(t);
        r.setBounds(x,y,80,30);
        r.setBackground(Color.WHITE);
        return r;
    }

    public void actionPerformed(ActionEvent ae) {

        String pan = panTextField.getText().trim().toUpperCase();
        String aadhaar = aadharTextField.getText().trim();

        // PAN Validation
        if(!pan.matches("[A-Z]{5}[0-9]{4}[A-Z]{1}")){
            JOptionPane.showMessageDialog(null,
                    "Invalid PAN Number\nFormat: ABCDE1234F");
            return;
        }

        // Aadhaar Validation
        if(!aadhaar.matches("\\d{12}")){
            JOptionPane.showMessageDialog(null,
                    "Aadhaar must be 12 digits");
            return;
        }

        // Senior Citizen check
        if(!seniorYes.isSelected() && !seniorNo.isSelected()){
            JOptionPane.showMessageDialog(null,
                    "Please select Senior Citizen option");
            return;
        }

        // Existing Account check
        if(!accYes.isSelected() && !accNo.isSelected()){
            JOptionPane.showMessageDialog(null,
                    "Please select Existing Account option");
            return;
        }

        try{
            Conn c=new Conn();

            String query="insert into signuptwo values('"+formno+"','"+
                    religionBox.getSelectedItem()+"','"+
                    categoryBox.getSelectedItem()+"','"+
                    incomeBox.getSelectedItem()+"','"+
                    educationBox.getSelectedItem()+"','"+
                    occupationBox.getSelectedItem()+"','"+
                    pan+"','"+
                    aadhaar+"','"+
                    (seniorYes.isSelected()?"Yes":"No")+"','"+
                    (accYes.isSelected()?"Yes":"No")+"')";

            c.s.executeUpdate(query);

            setVisible(false);
            new SignupThree(formno);

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}