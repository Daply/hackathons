package com.pizzacutter.visualizer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.*;

import com.pizzacutter.Element;
import com.pizzacutter.PizzaCutter;

/**
 * 
 * @author Pleshchankova Daria
 *
 */
public class PizzaVisualizationWindow extends JFrame {
    
    private PizzaCutter pizzaCutter = null;
    
    private JSplitPane splitPaneV;
    private JSplitPane splitPaneH;
    private JPanel filesPanel;
    private JPanel runPanel;
    private PizzaGrid pizzaPanel;
    private String fileName = "";
    
    public PizzaVisualizationWindow(){
        setTitle( "Pizza Cut Application" );
        setBackground( Color.gray );
    
        JPanel topPanel = new JPanel();
        topPanel.setLayout( new BorderLayout() );
        getContentPane().add( topPanel );
    
        // Create the panels
        createFilesPanel();
        createRunPanel();
        createPizzaPanel();
    
        // Create a splitter pane
        splitPaneV = new JSplitPane( JSplitPane.VERTICAL_SPLIT );
        topPanel.add( splitPaneV, BorderLayout.CENTER );
    
        splitPaneH = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT );
        splitPaneH.setLeftComponent( filesPanel );
        splitPaneH.setRightComponent( runPanel );
    
        splitPaneV.setLeftComponent( splitPaneH );
        splitPaneV.setRightComponent( pizzaPanel );
    }
    
    public void createFilesPanel(){
        filesPanel = new JPanel();
        filesPanel.setLayout( new GridLayout(1,4,4,4));
    
        filesPanel.add( new JLabel( "Choose file:" ), BorderLayout.NORTH );
        
        // create files choose buttons
        JButton fileExampleButton = new JButton("example");
        fileExampleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fileName = "src//com//pizzacutter//data//a_example.in";
            }          
         });
        fileExampleButton.setPreferredSize(new Dimension(90, 30));
        JButton fileSmallButton = new JButton("small");
        fileSmallButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fileName = "src//com//pizzacutter//data//b_small.in";
            }          
         });
        fileSmallButton.setPreferredSize(new Dimension(80, 30));
        JButton fileMediumButton = new JButton("medium");
        fileMediumButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fileName = "src//com//pizzacutter//data//c_medium.in";
            }          
         });
        fileMediumButton.setPreferredSize(new Dimension(80, 30));
        JButton fileBigButton = new JButton("big");
        fileBigButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fileName = "src//com//pizzacutter//data//d_big.in";
            }          
         });
        fileBigButton.setPreferredSize(new Dimension(80, 30));
        
        // Add buttons
        filesPanel.add( fileExampleButton, BorderLayout.CENTER );
        filesPanel.add( fileSmallButton, BorderLayout.CENTER );
        filesPanel.add( fileMediumButton, BorderLayout.CENTER );
        filesPanel.add( fileBigButton, BorderLayout.CENTER );
    }
    
    public void createRunPanel(){
        runPanel = new JPanel();
        runPanel.setLayout( new FlowLayout() );
    
        runPanel.add( new JLabel( "Cut pizza:" ), BorderLayout.NORTH );
        
        
        JButton runButton = new JButton("run");
        runButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!fileName.isEmpty() && pizzaCutter != null)
                    try {
                        pizzaCutter.run(fileName);
                        pizzaCutter.printResult();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
            }          
         });
        runButton.setPreferredSize(new Dimension(30, 30));
        
        runPanel.add( runButton );
    }
    
    public void createPizzaPanel(){
        pizzaPanel = new PizzaGrid();
        pizzaPanel.setLayout( new BorderLayout() );
        pizzaPanel.setPreferredSize( new Dimension( 400, 400 ) );
        pizzaPanel.setMinimumSize( new Dimension( 100, 100 ) );
    
        pizzaPanel.add( new JLabel( "pizza grid" ), BorderLayout.NORTH );
    }
    
    public void setPizzaCutter(PizzaCutter pizzaCutter){
        this.pizzaCutter = pizzaCutter;
    }
    
    public void setPizza(Element[][] pizza){
        this.pizzaPanel.setPizza(pizza);
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void redraw() {
        this.pizzaPanel.redraw();
    }
    
    public static class PizzaGrid extends JPanel {
        
        static int basicWidth = 400;
        static int basicLength = 400;
    
        private int startX = 30;
        private int startY = 30;
        
        private int width = basicWidth;
        private int length = basicLength;
        
        private int squareSize = 10;
    
        private Element[][] pizza;
        private Point[][] pizzaGrid;
        private List<Color> colors = null;
    
        public PizzaGrid() {
            this.startX = this.getX() + 30;
            this.startY = this.getY() + 30;
        }
    
        public void setStartX(int startX) {
            this.startX = startX;
        }
        
        public void setStartY(int startY) {
            this.startY = startY;
        }
        
        public void setPizza(Element[][] pizza) {
            if (pizza.length > 0) {
                this.pizza = pizza;
                this.pizzaGrid = new Point[pizza.length][pizza[0].length];
                setSize(pizza.length, pizza[0].length);
                Point point = null;
                for (int i = 0; i < pizza.length; i++) {
                    for (int j = 0; j < pizza[0].length; j++) {
                        point = new Point(i*squareSize+startX, j*squareSize+startY);
                        this.pizzaGrid[i][j] = point;
                    }
                }
            }
        }
        
        public void setSize(int width, int length) {
            if (width*this.squareSize < 100 ||
                    length*this.squareSize < 100) {
                this.squareSize = 20;
            }
            if (width*this.squareSize > 1000 ||
                    length*this.squareSize > 1000) {
                this.squareSize = 5;
            }
            if (width*this.squareSize > 2000 ||
                    length*this.squareSize > 2000) {
                this.squareSize = 1;
            }
            if (width*this.squareSize > 5000 ||
                    length*this.squareSize > 5000) {
                this.squareSize = 1;
            }
            this.width = width*this.squareSize;
            this.length = length*this.squareSize;
            this.colors = new ArrayList<Color>();
            setColors(width, length);
        }
        
        public void setColors(int width, int length) {
            Random r = new Random(this.colors.size()*3);
            for (int i = 0; i < width*length; i++) {
                float h = r.nextFloat();
                float s = r.nextFloat();
                float b = r.nextFloat();
                this.colors.add(Color.getHSBColor(h, s, b));
            }
        }
        
        public Color getSliceColor(int i, int j) {
            int colorNumber = this.pizza[i][j].getSliceMark();
            if (colorNumber == -1)
                return Color.WHITE;
            return this.colors.get(colorNumber);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            if (this.pizzaGrid != null) {
                for (int i = 0; i < this.pizzaGrid.length; i++) {
                    for (int j = 0; j < this.pizzaGrid[0].length; j++) {
                        g.setColor(getSliceColor(i, j));
                        g.fillRect(pizzaGrid[i][j].y, pizzaGrid[i][j].x, squareSize, squareSize);
                    }
                }
                
                g.setColor(Color.BLACK);
                g.drawRect(startX, startY, length, width);
    
                // draw line dividors
                for (int i = startY; i < startY + width; i += squareSize) {
                    g.drawLine(startX, i+squareSize, startY + length, i+squareSize);
                }
                
                for (int i = startX; i < startX + length; i += squareSize) {
                    g.drawLine(i+squareSize, startY, i+squareSize, startY + width);
                }
            }
    
        }
    
        public void redraw() {
            repaint();
        }
    
    }
    
    public static void main( String args[] ){
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception evt) {}
        // Create an instance of the test application
        PizzaCutter pizzaCutter = new PizzaCutter();
        PizzaVisualizationWindow mainFrame = new PizzaVisualizationWindow();
        //pizzaCutter.setVisualizer(mainFrame);
        mainFrame.setPizzaCutter(pizzaCutter);
        mainFrame.setSize(800, 800);
        //mainFrame.pack();
        mainFrame.setVisible( true );
    }
}

