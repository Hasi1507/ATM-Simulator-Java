package atm.simulator.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class SignupThree extends JFrame implements ActionListener {

    JRadioButton saving,current,fixed,recurring;
    JCheckBox atm,internet,mobile,email,cheque,eStatement;
    JButton submit,cancel;
    String formno;

    SignupThree(String formno){
        this.formno=formno;

        // ===== BACKGROUND =====
        JPanel panel=new JPanel(){
            protected void paintComponent(Graphics g){
                super.paintComponent(g);

                Graphics2D g2=(Graphics2D)g;
                int w=getWidth();
                int h=getHeight();

                GradientPaint gp=new GradientPaint(
                        0,0,new Color(15,32,39),
                        w,h,new Color(44,83,100));

                g2.setPaint(gp);
                g2.fillRect(0,0,w,h);

                g2.setColor(new Color(255,255,255,40));
                g2.fillOval(-200,-200,500,500);
                g2.fillOval(w-300,h-300,400,400);
            }
        };

        panel.setLayout(null);
        setContentPane(panel);

        // glass card
        JPanel card=new JPanel(){
            protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g;

                g2.setColor(new Color(0,0,0,70));
                g2.fillRoundRect(15,15,getWidth()-15,getHeight()-15,30,30);

                g2.setColor(new Color(255,255,255,180));
                g2.fillRoundRect(0,0,getWidth()-15,getHeight()-15,30,30);
            }
        };

        card.setLayout(null);
        card.setOpaque(false);
        card.setBounds(200,40,700,560);
        panel.add(card);

        addComponentListener(new ComponentAdapter(){
            public void componentResized(ComponentEvent e){
                card.setLocation(
                        (getWidth()-card.getWidth())/2,
                        (getHeight()-card.getHeight())/2);
            }
        });

        JLabel step=new JLabel("Step 3 of 3");
        step.setBounds(520,10,120,20);
        step.setFont(new Font("Segoe UI",Font.BOLD,14));
        card.add(step);

        JLabel title=new JLabel("Account Details");
        title.setBounds(230,40,300,30);
        title.setFont(new Font("Segoe UI",Font.BOLD,22));
        card.add(title);

        int y=100;

        card.add(label("Account Type",50,y));

        saving=radio("Saving Account",250,y);
        current=radio("Current Account",250,y+30);
        fixed=radio("Fixed Deposit",250,y+60);
        recurring=radio("Recurring Deposit",250,y+90);

        ButtonGroup group=new ButtonGroup();
        group.add(saving);
        group.add(current);
        group.add(fixed);
        group.add(recurring);

        card.add(saving);
        card.add(current);
        card.add(fixed);
        card.add(recurring);

        y+=130;

        card.add(label("Services Required",50,y));

        atm=check("ATM Card",250,y);
        internet=check("Internet Banking",400,y);
        mobile=check("Mobile Banking",250,y+40);
        email=check("Email Alerts",400,y+40);
        cheque=check("Cheque Book",250,y+80);
        eStatement=check("E-Statement",400,y+80);

        card.add(atm);card.add(internet);
        card.add(mobile);card.add(email);
        card.add(cheque);card.add(eStatement);

        submit=btn("SUBMIT",220,470,new Color(15,32,39));
        cancel=btn("CANCEL",380,470,Color.GRAY);

        card.add(submit);
        card.add(cancel);

        submit.addActionListener(this);
        cancel.addActionListener(this);

        setSize(1000,700);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    JLabel label(String t,int x,int y){
        JLabel l=new JLabel(t);
        l.setBounds(x,y,200,25);
        l.setFont(new Font("Segoe UI",Font.BOLD,14));
        return l;
    }

    JRadioButton radio(String t,int x,int y){
        JRadioButton r=new JRadioButton(t);
        r.setBounds(x,y,200,30);
        r.setBackground(Color.WHITE);
        return r;
    }

    JCheckBox check(String t,int x,int y){
        JCheckBox c=new JCheckBox(t);
        c.setBounds(x,y,180,30);
        c.setBackground(Color.WHITE);
        return c;
    }

    JButton btn(String t,int x,int y,Color c){
        JButton b=new JButton(t);
        b.setBounds(x,y,140,45);
        b.setBackground(c);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI",Font.BOLD,14));
        return b;
    }

    public void actionPerformed(ActionEvent ae){

        if(ae.getSource()==submit){

            String accountType=null;

            if(saving.isSelected()) accountType="Saving";
            else if(current.isSelected()) accountType="Current";
            else if(fixed.isSelected()) accountType="Fixed";
            else if(recurring.isSelected()) accountType="Recurring";

            Random ran=new Random();
            String cardNumber="504093"+(1000000000L+Math.abs(ran.nextLong()%9000000000L));
            String pin=String.format("%04d",ran.nextInt(10000));

            String facility="";
            if(atm.isSelected()) facility+="ATM ";
            if(internet.isSelected()) facility+="Internet ";
            if(mobile.isSelected()) facility+="Mobile ";
            if(email.isSelected()) facility+="Email ";
            if(cheque.isSelected()) facility+="Cheque ";
            if(eStatement.isSelected()) facility+="E-Statement ";

            try{
                Conn c=new Conn();

                String query="insert into signupthree values('"
                        +formno+"','"+accountType+"','"+cardNumber+"','"+pin+"','"+facility+"')";

                c.s.executeUpdate(query);

                JOptionPane.showMessageDialog(null,
                        "Card Number: "+cardNumber+"\nPIN: "+pin);

                setVisible(false);
                new Login();

            }catch(Exception e){
                e.printStackTrace();
            }

        }else{
            setVisible(false);
            new Login();
        }
    }
}