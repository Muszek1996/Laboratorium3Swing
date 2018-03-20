import javax.swing.*;
import org.mariuszgromada.math.mxparser.Expression;
import java.awt.*;
import java.awt.event.*;
import java.text.MessageFormat;

public class calculator extends JFrame implements ActionListener{
    private JPanel mainPanel;
    private JTextArea historyTextArea;
    private JTextField formulaInput;
    private JList functionsList;
    private JButton evalButton ;
    private JScrollPane scrollContainerPane;
    private JPanel functionsPanel;
    private JPanel historyPanel;
    private DefaultListModel<DoublePair> listModel;
    private JMenuBar menuBar = new JMenuBar();
    private JMenu mainMenu = new JMenu("Options");
    private JMenuItem reset=new JMenuItem("Reset"),exit= new JMenuItem("Exit");
    private String lastResult = "";
    private String lastFormula = "";




    public calculator() {
        super("Java calculator app");
        evalButton.addActionListener(this);
        menuBar.add(mainMenu);
        mainMenu.add(reset);
        mainMenu.add(exit);
        setJMenuBar(menuBar);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.add(createHistoryPanel(), constraints(0,0,1,1,GridBagConstraints.BOTH,new Insets(5,5,5,5)));
        mainPanel.add(createFormulaInput(), constraints(0,1,1,0,GridBagConstraints.BOTH,new Insets(5,5,5,5)));
        mainPanel.add(createFunctionsPanel(), constraints(1,0,0,1,GridBagConstraints.BOTH,new Insets(5,5,5,5)));

        mainPanel.add(evalButton, constraints(1,1,0,0,GridBagConstraints.BOTH,new Insets(5,5,5,5)));
        add(mainPanel);
        setPreferredSize(new Dimension(700,500));
        setLocation(1920/2-350, 1080/2-250);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                historyTextArea.setText("");
                formulaInput.setText("");
                lastFormula = "";
            }
        });


        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                dispose();
            }
        });
    }



    private JPanel createHistoryPanel() {
        historyPanel.setLayout(new GridBagLayout());
        historyTextArea.setEditable(false);
        historyTextArea.setLineWrap(true);
        historyPanel.add(new JScrollPane(historyTextArea), constraints(0,0,1,1,GridBagConstraints.BOTH, new Insets(0,0,0,0)));
        return historyPanel;
    }



    private JTextField createFormulaInput() {
        formulaInput.addActionListener(this);
        formulaInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    formulaInput.setText(lastFormula);
                }
            }
        });


        return formulaInput;
    }

    private JPanel createFunctionsPanel() {
        functionsPanel.setLayout(new GridBagLayout());
        functionsPanel.add(createListOfFunctions(), constraints(0,0,0,1,GridBagConstraints.BOTH,new Insets(0,0,0,0)));

        return functionsPanel;
    }

    private JList<String> createListOfFunctions() {
        listModel = new DefaultListModel<>();
        listModel.addElement(new DoublePair("Square root", "sqrt()"));
        listModel.addElement(new DoublePair("Fibonacci number", "Fib()"));
        listModel.addElement(new DoublePair("Absolute value", "abs()"));
        listModel.addElement(new DoublePair("Binary Logarithm", "log2()"));
        listModel.addElement(new DoublePair("Exponential", "exp()"));
        listModel.addElement(new DoublePair("!", "!"));
        listModel.addElement(new DoublePair("^", "^"));
        listModel.addElement(new DoublePair("%", "#"));
        listModel.addElement(new DoublePair("Omega Constant", "[Om]*3"));
        listModel.addElement(new DoublePair("Sierpiński Constant", "2*[Ks]"));
        listModel.addElement(new DoublePair("Euler-Mascheroni constant", "2*[gam]"));
        listModel.addElement(new DoublePair("Last result", lastResult));

        functionsList = new JList(listModel);

        functionsList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String stringToInsert = (listModel.getElementAt(functionsList.getSelectedIndex())).getValue();
                    StringBuilder formulaInputText = new StringBuilder(formulaInput.getText());
                    formulaInput.setText((formulaInputText.insert(formulaInput.getCaretPosition(), stringToInsert)).toString());
                    if(formulaInput.getText().contains(")")) {
                        formulaInput.setCaretPosition(formulaInput.getText().indexOf(')'));
                    }else if(formulaInput.getText().contains("!")) {
                        formulaInput.setCaretPosition(formulaInput.getText().indexOf('!'));
                    }

                    formulaInput.requestFocus();
                }
            }
        });

        return functionsList;
    }







    private void handleInputFromUser() {
        lastFormula = formulaInput.getText();
        formulaInput.setText("");
        Expression expression = new Expression(lastFormula);
        try {
            if (expression.checkSyntax()) {
                Double result = expression.calculate();
                lastResult = result.toString();
                if (Double.isNaN(result)) {
                    throw new Exception("Not a number");
                }
                listModel.set(listModel.size()-1, new DoublePair("Last Result", lastResult));
                String newResult = MessageFormat.format("{0} \n \t\t= {1}\n==========\n", lastFormula, result);
                historyTextArea.append(newResult);
            } else {
                String errorMessage = expression.getErrorMessage();
                throw new Exception(errorMessage);
            }
        } catch (Exception exc) {
            JOptionPane.showMessageDialog(null, exc.getMessage(), "Wrong formula!", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == formulaInput || source == evalButton) {
            handleInputFromUser();
        }
    }

    private final GridBagConstraints constraints(int x,int y,int wx,int wy, int gridBagConstraints, Insets insets){
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.weightx = wx;
        constraints.weighty = wy;
        constraints.fill = gridBagConstraints;
        constraints.insets = insets;
        return constraints;
    }


}
