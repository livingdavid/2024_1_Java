import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;
import java.util.Locale;
import java.io.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;

// helper package
class ComponentAttacher {
    public static void attach(Container caller, JComponent abc, int x, int y, int width, int height) {
        caller.add(abc);
        abc.setBounds(x, y, width, height);
    }

    public static void attach(Container caller, JComponent abc, Rectangle r) {
        caller.add(abc);
        abc.setBounds(r);
    }
}

class CurrencyFormatter {
    public static String getCanDollarFormat(Double value){
        NumberFormat canFormat = NumberFormat.getCurrencyInstance(Locale.CANADA);
        return canFormat.format(value);
    }
}

interface Observable {
    void addObserver(Observer o);
    void removeObserver(Observer o);
    void notifyObserver();
}

interface Observer {
    void update();
}

// model.exceptions package
class NullAmountException extends Exception {
    public NullAmountException(String message) {
        super(message);
    }
}

// model package
class Balance {
    private ListOfRecords records;

    public Balance(ListOfRecords records) {
        this.records = records;
    }

    public String getIncome() {
        int income = 0;
        for (Record r : records.getRecords()) {
            if (r.getAmountInt() > 0) income += r.getAmountInt();
        }

        return CurrencyFormatter.getCanDollarFormat((double) income / 100);
    }

    public String getExpense() {
        int expense = 0;
        for (Record r : records.getRecords()) {
            if (r.getAmountInt() < 0) expense += r.getAmountInt();
        }

        return CurrencyFormatter.getCanDollarFormat((double) expense / 100);
    }

    public String getBalance() {
        int balance = 0;
        for (Record r : records.getRecords()) {
            balance += r.getAmountInt();
        }

        return CurrencyFormatter.getCanDollarFormat((double) balance / 100);
    }
}

class ChartData {
    private String name;
    private double value;

    public ChartData(String name, double value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }
}

class ListOfRecords {
    private List<Record> records;

    public ListOfRecords() {
        records = new ArrayList<>();
    }

    public List<Record> getRecords() {
        return records;
    }

    public void addRecord(Record record) {
        records.add(record);
    }

    public void clearRecords() {
        records.clear();
    }
}

class Record {
    private String date;
    private String amountForEdit;
    private int amountInt;
    private String description;
    private String category;
    private boolean isDeposit;
    private String amountModStr;
    private Calendar cal = Calendar.getInstance();

    public Record(String amountStr, String description, String category, boolean isDeposit) {
        this.amountModStr = amountModify(amountStr); // 15000
        this.amountInt = Integer.parseInt(amountModStr) / 100; // 150
        this.description = description;
        this.category = category;
        this.isDeposit = isDeposit;
        this.date = today();

        this.amountForEdit = String.valueOf((double) amountInt / 100);
        this.amountInt = (isDeposit) ? amountInt : amountInt * -1;
    }

    private String amountModify(String amountStr) {
        String modStr1 = amountStr.replace(".", "").replace(",", "");

        return modStr1.contains("$") ? modStr1.substring(1) : modStr1;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    private String getAmountModStr() {
        return amountModStr;
    }

    public int getAmountInt() {
        return amountInt;
    }

    public String getAmountForEdit() {
        return amountForEdit;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public boolean isDeposit() {
        return isDeposit;
    }

    private String today() {
        int year = cal.get(Calendar.YEAR) % 100;
        String month = String.format("%02d", cal.get(Calendar.MONTH) + 1);
        String date = String.format("%02d", cal.get(Calendar.DATE));
        return year + "/" + month + "/" + date;
    }

    public String saveString() {
        String saveString;
        String date = this.getDate();
        String amount = this.getAmountModStr();
        String category = this.getCategory();
        String description = this.getDescription();
        String isDeposit = "" + this.isDeposit();

        saveString = date + "," + amount + "," + description + "," + category + "," + isDeposit + ",\r\n";
        return saveString;
    }
}

// view package
class AddDialog extends Dialog {
    private AppManager appManager;
    private final String NUMBER_FORMAT_MSG = "Please input the integers";
    private final String ERROR_MSG = "Error! Something is missing.";
    private final String CATEGORY_MISSING_MSG = "Please select a category.";

    AddDialog(AppManager appManager) {
        super();
        this.appManager = appManager;
        initView();
    }

    private void initView() {
        setTitle("Add a record");
        leftBtn.setText("Add");

        amountText.setText("");
        descriptionText.setText("");
        incomeButtonGroup.clearSelection();
        expenditureButtonGroup.clearSelection();

        leftBtn.addActionListener(e -> {
            String amount = amountText.getText();
            String category = super.category;
            String description = descriptionText.getText();
            boolean isDeposit = super.isDeposit;

            try {
                appManager.addRecord(amount, category, description, isDeposit);
            } catch (NumberFormatException e1) {
                JOptionPane.showMessageDialog(this, NUMBER_FORMAT_MSG, "ERROR!", JOptionPane.ERROR_MESSAGE);
            } catch (NullAmountException e1) {
                JOptionPane.showMessageDialog(this, ERROR_MSG, "ERROR!", JOptionPane.ERROR_MESSAGE);
            } catch (NullPointerException e1) {
                JOptionPane.showMessageDialog(this, CATEGORY_MISSING_MSG, "ERROR!", JOptionPane.ERROR_MESSAGE);
            }
            initInputFields();
            this.dispose();
        });
        cancelBtn.addActionListener(e -> {
            initInputFields();
            this.dispose();
        });
    }
}

class ChartDialog extends JDialog {
    private final Rectangle BUTTON_RECT = new Rectangle(200, 420, 80, 30);
    private ChartDialog chartDialog;
    private AppManager appManager;

    ChartDialog(AppManager appManager) {
        this.appManager = appManager;
        chartDialog = this;
        setTitle("Expense Chart");
        initView();
    }

    private void initView() {
        this.setLayout(null);
        JButton closeBtn = new JButton("Close");
        ComponentAttacher.attach(chartDialog, closeBtn, BUTTON_RECT);

        closeBtn.addActionListener(e -> chartDialog.dispose());
    }

    void showDialog() {
        ChartPanel chartPanel = new ChartPanel();
        ComponentAttacher.attach(chartDialog, chartPanel, 0, 0, 500, 400);
        chartPanel.initAndShowGUI(appManager.getChartData());
    }

}

class ChartPanel extends JFXPanel{
    private ChartPanel chartPanel = this;
    private ArrayList<ChartData> chartData;

    void initAndShowGUI(ArrayList<ChartData> chartData) {
        this.chartData = chartData;
        Platform.runLater(() -> initFX(chartPanel));
    }

    private PieChart.Data[] convertChartData() {
        int size = chartData.size();
        String name;
        double value;
        PieChart.Data[] dataArray = new PieChart.Data[size];
        for (int i = 0; i < size; i++) {
            name = chartData.get(i).getName();
            value = chartData.get(i).getValue();
            dataArray[i] = new PieChart.Data(name, Math.abs(value));
        }
        return dataArray;
    }

    private void initFX(JFXPanel fxPanel) {
        // This method is invoked on the JavaFX thread
        Scene scene = new Scene(new Group());
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(convertChartData());
        final PieChart chart = new PieChart(pieChartData);
        ((Group) scene.getRoot()).getChildren().add(chart);

        fxPanel.setScene(scene);
        chart.setTitle("Expense by Categories");
    }
}

abstract class Dialog extends JDialog {
    final String[] EXPENDITURE_CATEGORY = {"Food", "Transport", "For fun", "Gift", "Clothes", "Other Expen."};
    final String[] INCOME_CATEGORY = {"Salary", "Pocket money", "Carried over", "Other Income"};

    JTextField amountText, descriptionText;
    JButton leftBtn, cancelBtn;
    String category;
    boolean isDeposit;
    JToggleButton[] incomeBtns = new JToggleButton[INCOME_CATEGORY.length];
    JToggleButton[] expenditureBtns = new JToggleButton[EXPENDITURE_CATEGORY.length];

    ButtonGroup incomeButtonGroup, expenditureButtonGroup;

    Dialog() {
        initView();
    }

    private void initView() {
        setLayout(null);

        JLabel infoLabel = new JLabel("Amount (C$)");
        JLabel descriptionLabel = new JLabel("Description");
        JLabel incomeCategoryLabel = new JLabel("Income Category");
        JLabel expenditureCategoryLabel = new JLabel("Expen. Category");
        ComponentAttacher.attach(this, infoLabel, 10, 10, 120, 25);
        ComponentAttacher.attach(this, descriptionLabel, 10, 40, 80, 25);
        ComponentAttacher.attach(this, incomeCategoryLabel, 10, 100, 120, 25);
        ComponentAttacher.attach(this, expenditureCategoryLabel, 10, 180, 120, 25);

        amountText = new JTextField();
        descriptionText = new JTextField();
        ComponentAttacher.attach(this, amountText, 120, 10, 250, 25);
        ComponentAttacher.attach(this, descriptionText, 120, 40, 250, 25);

        ActionListener listener = this::clearAnotherCategory;
        ComponentAttacher.attach(this, incomeButtonPanel(listener), 120, 100, 250, 60);
        ComponentAttacher.attach(this, expenditureButtonPanel(listener), 120, 180, 250, 90);

        leftBtn = new JButton();
        cancelBtn = new JButton("Cancel");
        ComponentAttacher.attach(this, leftBtn, 50, 300, 100, 40);
        ComponentAttacher.attach(this, cancelBtn, 200, 300, 100, 40);
    }

    private JPanel incomeButtonPanel(ActionListener listener) {
        JPanel incomeButtonPanel = new JPanel();
        incomeButtonGroup = new ButtonGroup();
        incomeButtonPanel.setLayout(new GridLayout(0,2));

        for (int i = 0; i < INCOME_CATEGORY.length; i++) {
            incomeBtns[i] = new JToggleButton();
            incomeBtns[i].setText(INCOME_CATEGORY[i]);
            incomeBtns[i].addActionListener(listener);
            incomeButtonGroup.add(incomeBtns[i]);
            incomeButtonPanel.add(incomeBtns[i]);
        }

        return incomeButtonPanel;
    }

    private JPanel expenditureButtonPanel(ActionListener listener) {
        JPanel expenditureButtonPanel = new JPanel();
        expenditureButtonGroup = new ButtonGroup();
        expenditureButtonPanel.setLayout(new GridLayout(0,2));

        for (int i = 0; i < EXPENDITURE_CATEGORY.length; i++) {
            expenditureBtns[i] = new JToggleButton(EXPENDITURE_CATEGORY[i]);
            expenditureBtns[i].addActionListener(listener);
            expenditureButtonGroup.add(expenditureBtns[i]);
            expenditureButtonPanel.add(expenditureBtns[i]);
        }

        return expenditureButtonPanel;
    }

    private void clearAnotherCategory(ActionEvent actionEvent) {
        category = actionEvent.getActionCommand();
        for (String income : INCOME_CATEGORY) {
            if (category.equals(income)) {
                expenditureButtonGroup.clearSelection();
                isDeposit = true;
            }
        }
        for (String expense : EXPENDITURE_CATEGORY) {
            if (category.equals(expense)) {
                incomeButtonGroup.clearSelection();
                isDeposit = false;
            }
        }
    }

    void initInputFields() {
        amountText.setText("");
        descriptionText.setText("");
        incomeButtonGroup.clearSelection();
        expenditureButtonGroup.clearSelection();
    }
}

class EditDialog extends Dialog {
    private AppManager appManager;
    private int editIndex;

    EditDialog(AppManager appManager) {
        super();
        this.appManager = appManager;
        initView();
    }

    private void initView() {
        setTitle("Edit a record");
        leftBtn.setText("Edit");
        initInputFields();

        leftBtn.addActionListener(e -> {
            String amount = amountText.getText();
            String category = super.category;
            String description = descriptionText.getText();
            boolean isDeposit = super.isDeposit;

            try {
                appManager.editRecord(editIndex, amount, category, description, isDeposit);
            } catch (NumberFormatException e1) {
                JOptionPane.showMessageDialog(this, "Please input the integers", "ERROR!", JOptionPane.ERROR_MESSAGE);
            } catch (NullAmountException e1) {
                JOptionPane.showMessageDialog(this, e1, "ERROR!", JOptionPane.ERROR_MESSAGE);
            } catch (NullPointerException e1) {
                JOptionPane.showMessageDialog(this, "Please select a category", "ERROR!", JOptionPane.ERROR_MESSAGE);
            }
            initInputFields();
            this.dispose();
        });

        cancelBtn.addActionListener(e -> {
            initInputFields();
            this.dispose();
        });
    }

    void initEditInputFields(int editIndex) {
        this.editIndex = editIndex;
        Record editingRecord = appManager.getRecord(editIndex);

        String category = editingRecord.getCategory();

        descriptionText.setText(editingRecord.getDescription());
        amountText.setText(editingRecord.getAmountForEdit());

        if (editingRecord.isDeposit()) {
            for (int i = 0; i < INCOME_CATEGORY.length; i++) {
                if (category.equals(incomeBtns[i].getText())) {
                    incomeBtns[i].doClick();
                }
            }
        } else {
            for (int i = 0; i < EXPENDITURE_CATEGORY.length; i++) {
                if (category.equals(expenditureBtns[i].getText())) {
                    expenditureBtns[i].doClick();
                }
            }
        }
    }
}

class MainFrame extends JFrame implements Observer {
    private final String[] TABLE_COLUMNS = {"Date", "Amount", "Description", "Category"};
    private final Rectangle DIALOG_RECT = new Rectangle(this.getX() + 50, this.getY() + 50, 400, 400);
    private final Rectangle CHART_DIALOG_RECT = new Rectangle(this.getX() + 50, this.getY() + 50, 500, 500);
    private final Dimension MAIN_FRAME_DIM = new Dimension(400, 600);
    private final int PANEL_WIDTH = (int) (MAIN_FRAME_DIM.width * 0.97);
    private final Rectangle BUTTON_REC = new Rectangle(80, 30);

    private final Rectangle RECORD_PANEL_RECT = new Rectangle(0, 0, PANEL_WIDTH, (int) (MAIN_FRAME_DIM.height * 0.65));
    private final Rectangle BALANCE_PANEL_RECT = new Rectangle(0, RECORD_PANEL_RECT.height + 5, PANEL_WIDTH, (int) (MAIN_FRAME_DIM.height * 0.15));
    private final Rectangle BUTTON_PANEL_RECT = new Rectangle(0, RECORD_PANEL_RECT.height + BALANCE_PANEL_RECT.height + 10, PANEL_WIDTH, (int) (MAIN_FRAME_DIM.height * 0.15));

    private AppManager appManager;
    private AddDialog addDialog;
    private EditDialog editDialog;
    private DefaultTableModel defaultTable;
    private JTable recordTable;
    private JLabel incomeMoney, expenseMoney, balanceMoney;

    public static void main(String[] args) {
        MainFrame piggyManager = new MainFrame();
        piggyManager.start();
    }

    private void start() {
        appManager = new AppManager();
        addDialog = new AddDialog(appManager);
        editDialog = new EditDialog(appManager);
        appManager.addObserver(this);
        initView();
    }

    private void initView() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Piggy Manager");
        setLocation(100, 100);
        setPreferredSize(MAIN_FRAME_DIM);
        setResizable(false);
        setLayout(null);

        ComponentAttacher.attach(this, buttonPanel(), BUTTON_PANEL_RECT);
        ComponentAttacher.attach(this, tablePanel(), RECORD_PANEL_RECT);
        ComponentAttacher.attach(this, balancePanel(), BALANCE_PANEL_RECT);
        initEditDialog();

        pack();
        setVisible(true);
    }

    private JPanel buttonPanel() {
        JPanel returnPanel = new JPanel(new FlowLayout());
        JButton addBtn = new JButton("Add");
        JButton editBtn = new JButton("Edit");
        JButton saveBtn = new JButton("Save");
        JButton loadBtn = new JButton("Load");
        JButton chartBtn = new JButton("Chart");
        ComponentAttacher.attach(returnPanel, addBtn, BUTTON_REC);
        ComponentAttacher.attach(returnPanel, editBtn, BUTTON_REC);
        ComponentAttacher.attach(returnPanel, saveBtn, BUTTON_REC);
        ComponentAttacher.attach(returnPanel, loadBtn, BUTTON_REC);
        ComponentAttacher.attach(returnPanel, chartBtn, BUTTON_REC);

        addBtn.addActionListener(e -> initAddDialog());
        editBtn.addActionListener(e -> initEditDialog());
        saveBtn.addActionListener(e -> appManager.saveRecords());
        loadBtn.addActionListener(e -> appManager.loadRecords());
        chartBtn.addActionListener(e -> initChartDialog());
        return returnPanel;
    }

    private JPanel tablePanel() {
        final String TABLE_TITLE = "Records Transactions";
        JPanel returnPanel = new JPanel(null);
        returnPanel.setBorder(new TitledBorder(new EtchedBorder(), TABLE_TITLE));

        defaultTable = new DefaultTableModel(TABLE_COLUMNS, 0);
        recordTable = new JTable(defaultTable);
        JScrollPane sp = new JScrollPane(recordTable);
        ComponentAttacher.attach(returnPanel, sp, 10, 20, (int) (RECORD_PANEL_RECT.width * 0.95), (int)(RECORD_PANEL_RECT.height * 0.92));

        return returnPanel;
    }

    private JPanel balancePanel() {
        final String BALANCE_TITLE = "Balance";
        JPanel returnPanel = new JPanel(null);

        returnPanel.setBorder(new TitledBorder(new EtchedBorder(), BALANCE_TITLE));
        JPanel incomeExpensePrintPanel = new JPanel(new GridLayout(2, 2));
        JPanel balancePrintPanel = new JPanel(new GridLayout());

        JLabel incomeLabel = new JLabel("Income");
        JLabel expenseLabel = new JLabel("Expense");
        JLabel balanceLabel = new JLabel("Balance");
        incomeMoney = new JLabel("$0.00");
        expenseMoney = new JLabel("$0.00");
        balanceMoney = new JLabel("$0.00");

        ComponentAttacher.attach(returnPanel, incomeExpensePrintPanel, 20, 20, (PANEL_WIDTH / 2) - 20, (int) (BALANCE_PANEL_RECT.height * 0.60));
        ComponentAttacher.attach(returnPanel, balancePrintPanel, (PANEL_WIDTH / 2) + 15, 20, (PANEL_WIDTH / 2) - 20, (int) (BALANCE_PANEL_RECT.height * 0.60));
        incomeExpensePrintPanel.add(incomeLabel);
        incomeExpensePrintPanel.add(incomeMoney);
        incomeExpensePrintPanel.add(expenseLabel);
        incomeExpensePrintPanel.add(expenseMoney);
        balancePrintPanel.add(balanceLabel);
        balancePrintPanel.add(balanceMoney);

        return returnPanel;
    }

    private void initAddDialog() {
        addDialog.setBounds(DIALOG_RECT);
        addDialog.setVisible(true);
    }

    private void initEditDialog() {
        int editIndex = recordTable.getSelectedRow();
        if (editIndex != -1) {
            editDialog.initEditInputFields(editIndex);
            editDialog.setBounds(DIALOG_RECT);
            editDialog.setVisible(true);
        }
    }

    private void initChartDialog() {
        ChartDialog chartDialog = new ChartDialog(appManager);
        chartDialog.setBounds(CHART_DIALOG_RECT);
        chartDialog.setVisible(true);
        chartDialog.showDialog();
    }

    @Override
    public void update() {
        printTable();
        printMoney();
    }

    private void printMoney() {
        incomeMoney.setText(appManager.getIncome());
        expenseMoney.setText(appManager.getExpense());
        balanceMoney.setText(appManager.getBalance());
    }

    private void printTable() {
        appManager.updateRecordTable(defaultTable);
    }
}

// viewModel package
class AppManager implements Observable {
    private final String[] EXPENDITURE_CATEGORY = {"Food", "Transport", "For fun", "Gift", "Clothes", "Other Expen."};

    private ListOfRecords records;
    private Balance balance;
    private List<Observer> observers;
    private NumberFormat canFormat = NumberFormat.getCurrencyInstance(Locale.CANADA);

    public AppManager() {
        this.records = new ListOfRecords();
        this.balance = new Balance(records);
        this.observers = new ArrayList<>();
    }

    public void addRecord(String amount, String category, String description, boolean isDeposit) throws NumberFormatException, NullAmountException, NullPointerException {
        double amountDouble = Double.parseDouble(amount) * 100;
        String amountStr = canFormat.format(amountDouble);
        if (amountStr == null) {
            throw new NullAmountException("Please input amount.");
        }

        records.addRecord(new Record(amountStr, description, category, isDeposit));
        notifyObserver();
    }

    public void editRecord(int editIndex, String amount, String category, String description, boolean isDeposit) throws NumberFormatException, NullAmountException, NullPointerException {
        if (amount == null) {
            throw new NullAmountException("Please input amount.");
        }
        double amountDouble = Double.parseDouble(amount) * 100;
        amount = CurrencyFormatter.getCanDollarFormat(amountDouble);

        Record editedRecord = new Record(amount, description, category, isDeposit);
        records.getRecords().set(editIndex, editedRecord);

        notifyObserver();
    }

    updateRecordTable(DefaultTableModel recordTableModel) {
      List<Record> recordArrayList = records.getRecords();
      if (recordTableModel != null) recordTableModel.getDataVector().removeAllElements();
      Object[] data = new Object[4];
      for (Record r : recordArrayList) {
        int amountForInt = r.getAmountInt();
        data[0] = r.getDate();
        data[1] = CurrencyFormatter.getCanDollarFormat((double) amountForInt/ 100);
        data[2] = r.getDescription();
        data[3] = r.getCategory();
  
        assert recordTableModel != null;
        recordTableModel.addRow(data);
      }
    }
  
    public void loadRecords() {
      records.clearRecords();
  
      JFileChooser jf = new JFileChooser();
      int response = jf.showOpenDialog(null);
      if (response == JFileChooser.APPROVE_OPTION) {
        File f = jf.getSelectedFile();
        Scanner s;
        try {
          s = new Scanner(new FileReader(f));
  
          String[] item;
          while (s.hasNextLine()) {
            item = s.nextLine().split(",");
            Record record = new Record(item[1], item[2], item[3], Boolean.parseBoolean(item[4]));
            record.setDate(item[0]);
            records.addRecord(record);
          }
        } catch (FileNotFoundException e) {
          JOptionPane.showMessageDialog(null, "File not found.", "ERROR!", JOptionPane.ERROR_MESSAGE);
        } catch (ArrayIndexOutOfBoundsException e) {
          JOptionPane.showMessageDialog(null, "You have selected a wrong file", "ERROR!", JOptionPane.ERROR_MESSAGE);
        }
      }
      notifyObserver();
    }
  
    public void saveRecords() {
      JFileChooser fileChooser = new JFileChooser();
      fileChooser.setDialogTitle("Select a file or make a new text file to save");
      int userSelection = fileChooser.showSaveDialog(null);
  
      FileWriter writer = null;
      BufferedWriter bWriter = null;
  
      if (userSelection == JFileChooser.APPROVE_OPTION) {
        try {
          writer = new FileWriter(fileChooser.getSelectedFile() + ".txt");
          bWriter = new BufferedWriter(writer);
          String str;
  
          for (Record r : records.getRecords()) {
            str = r.saveString();
            bWriter.append(str);
          }
          bWriter.flush();
        } catch (IOException e) {
          e.printStackTrace();
        } finally {
          try {
            if (bWriter != null) bWriter.close();
            if (writer != null) writer.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
  
    }
  
    public ArrayList<ChartData> getChartData() {
      ArrayList<ChartData> chartData = new ArrayList<>();
      for (String s : EXPENDITURE_CATEGORY) {
        double expense = 0;
        for (Record record : records.getRecords()) {
          if (record.getCategory().equals(s)) {
            expense += record.getAmountInt();
          }
        }
        chartData.add(new ChartData(s, expense));
      }
      return chartData;
    }
  
    @Override
    public void addObserver(Observer o) {
      if (!observers.contains(o)) observers.add(o);
    }
  
    @Override
    public void removeObserver(Observer o) {
      observers.remove(o);
    }
  
    @Override
    public void notifyObserver() {
      for (Observer o : observers) {
        o.update();
      }
    }
  
    public String getIncome() {
      return balance.getIncome();
    }
  
    public String getExpense() {
      return balance.getExpense();
    }
  
    public String getBalance() {
      return balance.getBalance();
    }
  
    public Record getRecord(int editIndex) {
      return records.getRecords().get(editIndex);
    }