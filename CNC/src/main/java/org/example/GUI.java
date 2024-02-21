package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class GUI implements MouseListener {
    private JPanel appPanel;
    private JPanel drawPanel;
    private JPanel btnPanel;
    private JButton segmentButton;
    private JButton arcButton;
    private JButton generareGCodeButton;
    private JButton foaieAlbaButton;
    private JLabel selecteazaCadraneleLabel;
    private JCheckBox a1CheckBox;
    private JCheckBox a2CheckBox;
    private JCheckBox a3CheckBox;
    private JCheckBox a4CheckBox;

    int x,y;
    int line = 0;
    HashMap<Integer, ArrayList<Integer>> linesMap = new HashMap<>();
    ArrayList<Integer> alX = new ArrayList<>();
    ArrayList<Integer> alY = new ArrayList<>();

    public GUI() {
        JFrame frame = new JFrame("App Frame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800,400);

        appPanel = new JPanel(new BorderLayout());
        frame.add(appPanel);

        drawPanel.setPreferredSize(new Dimension(400,400));
        drawPanel.setBackground(Color.WHITE);
        drawPanel.addMouseListener(this);
        appPanel.add(drawPanel, BorderLayout.WEST);

        btnPanel.setPreferredSize(new Dimension(400,400));
        appPanel.add(btnPanel, BorderLayout.EAST);

        frame.setVisible(true);

        segmentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] xArray = new int[alX.size()];
                int[] yArray = new int[alY.size()];

                for(int i = 0; i < alX.size(); i++)
                {
                    xArray[i] = alX.get(i);
                    yArray[i] = alY.get(i);
                }

                Graphics graphics = drawPanel.getGraphics();
                graphics.drawPolyline(xArray, yArray, alX.size());

                ArrayList<Integer> coordsList = new ArrayList<>();
                for(int i = 0; i < alX.size(); i++)
                {
                    coordsList.add(alX.get(i));
                    coordsList.add(alY.get(i));
                }
                linesMap.put(line++,coordsList);

                alX.clear();
                alY.clear();
            }
        });
        arcButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (alX.size() >= 2 && (a1CheckBox.isSelected() || a2CheckBox.isSelected() || a3CheckBox.isSelected() || a4CheckBox.isSelected())) {
                    int xPoint = alX.get(0);
                    int yPoint = alY.get(0);
                    int xCenter = alX.get(1);
                    int yCenter = alY.get(1);
                    int radius = (int) Math.sqrt((xCenter - xPoint) * (xCenter - xPoint) + (yCenter - yPoint) * (yCenter - yPoint));

                    Graphics graphics = drawPanel.getGraphics();

                    int[] coordsXList = new int[4 * radius];
                    int[] coordsYList = new int[4 * radius];
                    int lenght = 0;
                    int last;

                    // 1
                    for(int i = 0; i < radius; i++)
                    {
                        coordsXList[lenght] = xCenter + radius - i;
                        coordsYList[lenght++] = yCenter - (int)Math.sqrt(radius * radius - (xCenter - (xCenter + radius - i)) * (xCenter - (xCenter + radius - i)));
                    }
                    last = lenght - 1;

                    //2
                    for(int i = 0; i < radius; i++)
                    {
                        coordsXList[lenght] = xCenter - i;
                        coordsYList[lenght++] = coordsYList[last - i];
                    }

                    //3
                    for(int i = 0; i < radius; i++)
                    {
                        coordsXList[lenght] = xCenter - radius + i + 1;
                        coordsYList[lenght++] = yCenter + (int)Math.sqrt(radius * radius - (xCenter - (xCenter - radius + i)) * (xCenter - (xCenter - radius + i)));
                    }
                    last = lenght - 1;

                    //4
                    for(int i = 0; i < radius; i++)
                    {
                        coordsXList[lenght] = xCenter + i + 1;
                        coordsYList[lenght++] = coordsYList[last - i];
                    }

                    if(a1CheckBox.isSelected()) {
                        int[] cadran1X = new int[radius];
                        int[] cadran1Y = new int[radius];
                        for(int i = 0; i < radius; i++){
                            cadran1X[i] = coordsXList[i];
                            cadran1Y[i] = coordsYList[i];
                        }
                        graphics.drawPolyline(cadran1X, cadran1Y, cadran1X.length);

                        ArrayList<Integer> coordsList = new ArrayList<>();
                        for(int i = 0; i < cadran1X.length; i++)
                        {
                            coordsList.add(cadran1X[i]);
                            coordsList.add(cadran1Y[i]);
                        }
                        linesMap.put(line++,coordsList);
                    }

                    if(a2CheckBox.isSelected()) {
                        int[] cadran2X = new int[radius];
                        int[] cadran2Y = new int[radius];
                        for(int i = 0; i < radius; i++){
                            cadran2X[i] = coordsXList[radius + i];
                            cadran2Y[i] = coordsYList[radius + i];
                        }
                        graphics.drawPolyline(cadran2X, cadran2Y, cadran2X.length);

                        ArrayList<Integer> coordsList = new ArrayList<>();
                        for(int i = 0; i < cadran2X.length; i++)
                        {
                            coordsList.add(cadran2X[i]);
                            coordsList.add(cadran2Y[i]);
                        }
                        linesMap.put(line++,coordsList);
                    }

                    if(a3CheckBox.isSelected()) {
                        int[] cadran3X = new int[radius];
                        int[] cadran3Y = new int[radius];
                        for(int i = 0; i < radius; i++){
                            cadran3X[i] = coordsXList[2 * radius + i];
                            cadran3Y[i] = coordsYList[2 * radius + i];
                        }
                        graphics.drawPolyline(cadran3X, cadran3Y, cadran3X.length);

                        ArrayList<Integer> coordsList = new ArrayList<>();
                        for(int i = 0; i < cadran3X.length; i++)
                        {
                            coordsList.add(cadran3X[i]);
                            coordsList.add(cadran3Y[i]);
                        }
                        linesMap.put(line++,coordsList);
                    }

                    if(a4CheckBox.isSelected()) {
                        int[] cadran4X = new int[radius];
                        int[] cadran4Y = new int[radius];
                        for(int i = 0; i < radius; i++){
                            cadran4X[i] = coordsXList[3 * radius + i];
                            cadran4Y[i] = coordsYList[3 * radius + i];
                        }
                        graphics.drawPolyline(cadran4X, cadran4Y, cadran4X.length);

                        ArrayList<Integer> coordsList = new ArrayList<>();
                        for(int i = 0; i < cadran4X.length; i++)
                        {
                            coordsList.add(cadran4X[i]);
                            coordsList.add(cadran4Y[i]);
                        }
                        linesMap.put(line++,coordsList);
                    }

                    alX.clear();
                    alY.clear();
                }else{
                    JOptionPane.showMessageDialog(frame, "Trebuie marcate doua puncte si sa fie selectat minim un cadran!", "Argumente insuficiente pentru desenare arcului de cerc", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        generareGCodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileWriter f;
                PrintWriter p;
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss");
                LocalDateTime now = LocalDateTime.now();

                String nameFile = "G" + dtf.format(now) + ".txt";
                try{
                    f = new FileWriter(nameFile,true);
                    p = new PrintWriter(f);

                    p.write("G21 (metric)\n");
                    p.write("G90 (absolute mode)\n");
                    p.write("G92 X0.00 Y0.00 Z0.00\n\n");

                    p.write("M300 S30 (pen down)\n");
                    p.write("G4 P150 (wait 150ms)\n");
                    p.write("M300 S50 (pen up)\n");
                    p.write("G4 P150 (wait 150ms)\n");
                    p.write("M18 (disengage drives)\n");
                    p.write("M01 (Was registration test successful?)\n");
                    p.write("M17 (engage drives if YES, and continue)\n\n");

                    for(int i = 0; i < line; i++){
                        p.write("(Polyline consisting of segments.)\n");
                        p.write("G1 X" + (double)(linesMap.get(i).get(0))/10.0 + " Y" + (double)(400 - linesMap.get(i).get(1))/10.0 + " F3500.00\n");
                        p.write("M300 S30.00 (pen down)\n");
                        p.write("G4 P150 (wait 150ms)\n");
                        for(int j = 2; j < linesMap.get(i).size(); j += 2)
                        {
                            p.write("G1 X" + (double)(linesMap.get(i).get(j))/10.0 + " Y" + (double)(400 - linesMap.get(i).get(j+1))/10.0 + " F3500.00\n");
                        }
                        p.write("M300 S50.00 (pen up)\n");
                        p.write("G4 P150 (wait 150ms)\n\n");
                    }

                    p.write("\n\n(end of print job)\n");
                    p.write("M300 S50.00 (pen up)\n");
                    p.write("G4 P150 (wait 150ms)\n");
                    p.write("M300 S255 (turn off servo)\n");
                    p.write("G1 X0 Y0 F3500.00\n");
                    p.write("G1 Z0.00 F150.00 (go up to finished level)\n");
                    p.write("G1 X0.00 Y0.00 F3500.00 (go home)\n");
                    p.write("M18 (drives off)\n");

                    p.close();
                    f.close();
                }catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
        foaieAlbaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                alX.clear();
                alY.clear();
                linesMap.clear();
                line = 0;
                Graphics graphics = drawPanel.getGraphics();
                graphics.clearRect(0,0,400,400);
                graphics.setColor(Color.WHITE);
                graphics.fillRect(0,0,400,400);
            }
        });
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        x = e.getX();
        y = e.getY();

        alX.add(x);
        alY.add(y);

        Graphics graphics = drawPanel.getGraphics();
        graphics.fillRect(x-3,y-3,6,6);

    }


    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
