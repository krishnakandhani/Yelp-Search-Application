package populate;


import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
//import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

public class hw3 {
	public static void main(String[] args) throws IOException {
		//Build the UI
		JFrame frame = new MainClass();
		frame.setTitle("Krishnakumar Kandhani");
		frame.setVisible(true);
		frame.setSize(1500, 850);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		frame.setLocationRelativeTo(null);
	}
}

@SuppressWarnings("serial")
//MainClass is being inherited from JFrame
class MainClass extends JFrame {
	
	JList<String> cataList;
	JList<String> subcataList;
	JList<String> attriList;
	JList<String> businessResultList;
	JList<String> userResultList;

	String conn_string;
	String query;

	JButton execBusBtn;
	JButton execUserBtn;

	JTable businessTable;
	JTable userTable;
	JTable busReviewJTable;
	JTable userreviewJTable;

	JSplitPane busResultPane;
	JSplitPane userresultPane;

	JPanel queryPanel;
	JPanel userPanel;
	JPanel valuePanel;

	DefaultTableModel busResultTable;
	DefaultTableModel userresultTable;
	DefaultTableModel reviewTable;
	DefaultTableModel userreviewTable;

	JComboBox <String> fromdayComboBox;
	JComboBox <String> ANDOR;
	JComboBox <String> cheats;
	JComboBox <String> toComboBox;
	JComboBox <String> totimeComboBox;
	JComboBox <String> numCheckinComboBox;
	JComboBox <String> startsComboBox;
	JComboBox <String> votesComboBox;
	JComboBox <String> reviewCountLabelComboBox;
	JComboBox <String> friendsCountLabelComboBox;
	JComboBox <String> andOrLabelComboBox;
	JComboBox <String> avgStarComboBox;
	JComboBox <String> votesUserComboBox;

	JTextField valueTextField;
	JTextField fromdateTextField;
	JTextField todateTextField;
	JTextField userValueTextField;
	JTextField votesValueTextField;
	JTextField countValueTextField;
	JTextField frndsCountValueTextField;
	JTextField memberSinceTextField;
	JTextField avgStarTextField;
	JTextField votesUserTextField;

	JSpinner.DateEditor memberSincede;
	JSpinner memberSincespinner;
	JSpinner.DateEditor fromdatede;
	JSpinner fromdatespinner;
	JSpinner.DateEditor todatede;
	JSpinner todatespinner;
	
	JTextArea queryField;

	DefaultListModel<String> addcataList = new DefaultListModel<String>();
	DefaultListModel<String> addsubcataList = new DefaultListModel<String>();
	DefaultListModel<String> addBidInList = new DefaultListModel<String>();
	DefaultListModel<String> addList = new DefaultListModel<String>();
	DefaultListModel<String> addattriList = new DefaultListModel<String>();
	DefaultListModel<String> addbusinessResultList = new DefaultListModel<String>();
	DefaultListModel<String> addUserResultList = new DefaultListModel<String>();

	
	ArrayList<String> selectedcataList = new ArrayList<String>();
	ArrayList<String> selectedsubcataList = new ArrayList<String>();
	ArrayList<String> selectedattriList = new ArrayList<String>();
	ArrayList<String> selectedbusinessResultList = new ArrayList<String>();

	static Connection con = null;

	PreparedStatement preparedStatement = null;

	HashMap<Integer,String> bIDMap;
	HashMap<Integer,String> userMap;

	String StagePopulateCatQuery = "";
	String StagePopulateSubCatQuery = "";
	String StagePopulateAttriQuery = "";
	String COLOR1 = "#964B00";
	String COLOR2 =  "#F5F5DC";
	
	
	

	public static void getDBConnection() 
	{
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		}
		catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}

		try {
			con = DriverManager.getConnection(
					"jdbc:oracle:thin:@localhost:1521:globalDB", "scott", "tiger");
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	MainClass() throws IOException {

		bIDMap = new HashMap<Integer,String>();
		userMap = new HashMap<Integer,String>();

		Border border = new LineBorder(Color.GRAY, 5);
		UIManager.put("SplitPaneDivider.border", border);
		cataList = new JList<String>();
		JLabel mainCataLabel = new JLabel("Category");
		Font f = mainCataLabel.getFont();
		mainCataLabel.setFont(new Font(f.getFontName(), Font.PLAIN, 20));
		JPanel mainCataTitle = new JPanel();
		mainCataTitle.setBackground(Color.decode(COLOR1));
		mainCataLabel.setForeground(Color.decode(COLOR2));
		mainCataTitle.add(mainCataLabel);
		JScrollPane mainCataContent = new JScrollPane();
		
		JSplitPane mainCataPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, mainCataTitle, mainCataContent);
		mainCataPane.setEnabled(false);
		mainCataContent.setViewportView(cataList);
		mainCataContent.getViewport().getView().setBackground(Color.decode(COLOR2));
		
		
		//Pane Generation for Sub Category List 
		subcataList = new JList<>();
		JLabel subCataLabel = new JLabel("Subcategory");
		subCataLabel.setFont(new Font(f.getFontName(), Font.PLAIN, 20));
		JPanel subCataTitle = new JPanel();
		subCataTitle.setBackground(Color.decode(COLOR1));
		subCataTitle.add(subCataLabel);
		subCataLabel.setForeground(Color.decode(COLOR2));
		JScrollPane subCataContents = new JScrollPane();
		JSplitPane subCataPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, subCataTitle, subCataContents);
		subCataPane.setEnabled(false);
		subCataContents.setViewportView(subcataList);
		subCataContents.getViewport().getView().setBackground(Color.decode(COLOR2));

		//Pane Generation for Attribute List 
		attriList = new JList<>();
		JLabel attributeLabel = new JLabel("Attribute");
		attributeLabel.setFont(new Font(f.getFontName(), Font.PLAIN, 20));
		attributeLabel.setForeground(Color.decode(COLOR2));
		JPanel attributeTitle = new JPanel();
		attributeTitle.setBackground(Color.decode(COLOR1));
		attributeTitle.add(attributeLabel);
		JScrollPane attributeContents = new JScrollPane();
		JSplitPane attributePane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, attributeTitle, attributeContents);
		attributePane.setEnabled(false);
		attributeContents.setViewportView(attriList);	
		attributeContents.getViewport().getView().setBackground(Color.decode(COLOR2));

		// Pane generation for Review
		JPanel reviewPanel = new JPanel();
		reviewPanel.setLayout(new BoxLayout(reviewPanel, BoxLayout.Y_AXIS));
		JLabel reviewLabel = new JLabel("Review");
		reviewLabel.setFont(new Font(f.getFontName(), Font.PLAIN, 20));
		reviewLabel.setForeground(Color.decode(COLOR2));
		JPanel reviewTitle = new JPanel();
		reviewTitle.setBackground(Color.decode(COLOR1));
		reviewTitle.add(reviewLabel);
		JScrollPane reviewContents = new JScrollPane();
		JSplitPane reviewPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, reviewTitle, reviewContents);
		reviewPane.setEnabled(false);
		reviewContents.setViewportView(reviewPanel);
		reviewContents.getViewport().getView().setBackground(Color.decode(COLOR2));

		Calendar cal = Calendar.getInstance();
		cal.set(1900, 04, 30);

		JLabel fromdateLabel = new JLabel("From Date");
		reviewPanel.add(fromdateLabel);		
		Date fromdatedate = cal.getTime();
		SpinnerDateModel fromdatesm = new SpinnerDateModel(fromdatedate, null, null, Calendar.HOUR_OF_DAY);
		fromdatespinner = new JSpinner(fromdatesm);
		fromdatede = new JSpinner.DateEditor(fromdatespinner, "yyyy-MM-dd");
		fromdatespinner.setEditor(fromdatede);
		reviewPanel.add(fromdatespinner);
				
		JLabel todateLabel = new JLabel("To Date");
		reviewPanel.add(todateLabel);
		Calendar cal1 = Calendar.getInstance();
		Date todatedate = cal1.getTime();
		SpinnerDateModel todatesm = new SpinnerDateModel(todatedate, null, null, Calendar.HOUR_OF_DAY);
		todatespinner = new JSpinner(todatesm);
		todatede = new JSpinner.DateEditor(todatespinner, "yyyy-MM-dd");
		todatespinner.setEditor(todatede);		
		reviewPanel.add(todatespinner);

		//Stars selection
		JLabel starsLabel = new JLabel("Stars:");
		reviewPanel.add(starsLabel);
		startsComboBox = new JComboBox<>();
		startsComboBox.addItem("=");
		startsComboBox.addItem(">");
		startsComboBox.addItem("<");
		reviewPanel.add(startsComboBox);
		startsComboBox.setSelectedItem(0);

		JLabel startValueLabel = new JLabel("Stars Value");
		reviewPanel.add(startValueLabel);
		userValueTextField = new JTextField();
		reviewPanel.add(userValueTextField);

		JLabel votesLabel = new JLabel("Votes:");
		reviewPanel.add(votesLabel);
		votesComboBox = new JComboBox<>();
		votesComboBox.addItem("=");
		votesComboBox.addItem(">");
		votesComboBox.addItem("<");
		reviewPanel.add(votesComboBox);

		JLabel votesValueLabel = new JLabel("Votes Value");
		reviewPanel.add(votesValueLabel);
		votesValueTextField = new JTextField();
		reviewPanel.add(votesValueTextField);
		
		ANDOR = new JComboBox<>();
		ANDOR.addItem("OR");
		ANDOR.addItem("AND");
		reviewPanel.add(new JLabel("Search AND, OR between attributes "));
		reviewPanel.add(ANDOR);
		ANDOR.setSelectedIndex(1);

		// Pane Generation for Business Results
		busResultTable = new DefaultTableModel();
		businessTable = new JTable();
		businessTable.setModel(busResultTable);
		busResultTable.addColumn("Business Name");
		busResultTable.addColumn("City");
		busResultTable.addColumn("State");
		busResultTable.addColumn("Stars");
		JLabel resultlabel = new JLabel("Business Results");
		resultlabel.setForeground(Color.decode(COLOR2));
		resultlabel.setFont(new Font(f.getFontName(), Font.PLAIN, 20));
		JPanel resultTitle = new JPanel();
		resultTitle.setBackground(Color.decode(COLOR1));
		resultTitle.add(resultlabel);
		JScrollPane result = new JScrollPane(businessTable);
		
		busResultPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, resultTitle, result);
		busResultPane.setEnabled(false);
		result.getViewport().getView().setBackground(Color.decode(COLOR2));

		//Pane generation for bottom pane
		//Pane Generation for user List 
		userPanel = new JPanel();
		userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
		JLabel userLabel = new JLabel("Users");
		userLabel.setForeground(Color.decode(COLOR2));
		userLabel.setFont(new Font(f.getFontName(), Font.PLAIN, 20));
		JPanel userTitle = new JPanel();
		userTitle.setBackground(Color.decode(COLOR1));
		userTitle.add(userLabel);
		JScrollPane userContents = new JScrollPane();
		JSplitPane userPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, userTitle, userContents);
		userPane.setEnabled(false);
		userContents.setViewportView(userPanel);
		

		JLabel memberSinceLabel = new JLabel("Member Since:");
		userPanel.add(memberSinceLabel);
		memberSinceTextField = new JTextField();
		userPanel.add(memberSinceTextField);
		userContents.getViewport().getView().setBackground(Color.decode(COLOR2));
		
		JLabel reviewCountLabel = new JLabel("Review Count:");
		userPanel.add(reviewCountLabel);
		reviewCountLabelComboBox = new JComboBox<>();
		reviewCountLabelComboBox.addItem("=");
		reviewCountLabelComboBox.addItem(">");
		reviewCountLabelComboBox.addItem("<");
		userPanel.add(reviewCountLabelComboBox);
    	countValueTextField = new JTextField();
		userPanel.add(countValueTextField);

		JLabel freindsCountLabel = new JLabel("Number of Friends:");
		userPanel.add(freindsCountLabel);
		friendsCountLabelComboBox = new JComboBox<>();
		friendsCountLabelComboBox.addItem("=");
		friendsCountLabelComboBox.addItem(">");
		friendsCountLabelComboBox.addItem("<");
		userPanel.add(friendsCountLabelComboBox);
		frndsCountValueTextField = new JTextField();
		userPanel.add(frndsCountValueTextField);

		JLabel avgStarsLabel = new JLabel("Average Stars:");
		userPanel.add(avgStarsLabel);
		avgStarComboBox = new JComboBox<>();
		avgStarComboBox.addItem("=");
		avgStarComboBox.addItem(">");
		avgStarComboBox.addItem("<");
		userPanel.add(avgStarComboBox);
		avgStarTextField = new JTextField();
		userPanel.add(avgStarTextField);

		JLabel votesUserLabel = new JLabel("Votes");
		userPanel.add(votesUserLabel);
		votesUserComboBox = new JComboBox<>();
		votesUserComboBox.addItem("=");
		votesUserComboBox.addItem(">");
		votesUserComboBox.addItem("<");
		userPanel.add(votesUserComboBox);
		votesUserTextField = new JTextField();
		userPanel.add(votesUserTextField);
		
		JLabel andOrLabel = new JLabel("Search AND, OR between attributes:");
		userPanel.add(andOrLabel);
		andOrLabelComboBox = new JComboBox<>();
		andOrLabelComboBox.addItem("AND");
		andOrLabelComboBox.addItem("OR");
		userPanel.add(andOrLabelComboBox);

		//Valued Pane
		valuePanel = new JPanel();
		valuePanel.setLayout(new BoxLayout(valuePanel, BoxLayout.Y_AXIS));
		JLabel valueLabel = new JLabel("Values");
		userLabel.setFont(new Font(f.getFontName(), Font.PLAIN, 20));
		JPanel valueTitle = new JPanel();
		valueTitle.setBackground(Color.decode(COLOR1));
		valueTitle.add(valueLabel);
		JScrollPane valueContents = new JScrollPane();
		JSplitPane valuePane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, valueTitle, valueContents);
		valuePane.setEnabled(false);
		valueContents.setViewportView(valuePanel);

	
		// Pane for Execute Query Box
		queryField = new JTextArea();
		JLabel queryLabel = new JLabel("Query");
		queryLabel.setFont(new Font(f.getFontName(), Font.PLAIN, 20));
		queryLabel.setForeground(Color.decode(COLOR2));
		JPanel queryTitle = new JPanel();
		queryTitle.setBackground(Color.decode(COLOR1));
		queryTitle.add(queryLabel);
		JScrollPane queryContents = new JScrollPane();
		JSplitPane queryPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, queryTitle, queryContents);
		queryPane.setEnabled(false);
		queryContents.setViewportView(queryField);
		queryContents.getViewport().getView().setBackground(Color.decode(COLOR2));

		//Pane for "execute query" button
		JPanel executePane = new JPanel();
		execBusBtn = new JButton("Execute Business Query");
		execBusBtn.setBackground(Color.decode(COLOR1));
		execBusBtn.setForeground(Color.decode(COLOR2));
		execBusBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				executeQuerryButtonActionPerformed(evt);
			}
		});
		executePane.add(execBusBtn);

		execUserBtn = new JButton("Execute User Query");
		execUserBtn.setBackground(Color.decode(COLOR1));
		execUserBtn.setForeground(Color.decode(COLOR2));
		execUserBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				userQuerryButtonActionPerformed(evt);
			}
		});
		executePane.add(execUserBtn);


		// Pane Generation for User Results
		userresultTable = new DefaultTableModel();
		userTable = new JTable();
		userTable.setModel(userresultTable);
		userresultTable.addColumn("User");
		userresultTable.addColumn("Yelping Since");
		userresultTable.addColumn("Average Stars");

		JLabel userresultlabel = new JLabel("User Results");
		userresultlabel.setFont(new Font(f.getFontName(), Font.PLAIN, 20));
		userresultlabel.setForeground(Color.decode(COLOR2));
		JPanel userresultTitle = new JPanel();
		userresultTitle.setBackground(Color.decode(COLOR1));
		userresultTitle.add(userresultlabel);
		JScrollPane userresult = new JScrollPane(userTable);
		userresultPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, userresultTitle, userresult);
		userresult.getViewport().getView().setBackground(Color.decode(COLOR2));
		userresultPane.setEnabled(false);

		JSplitPane firstUpperPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mainCataPane, subCataPane);
		firstUpperPane.setDividerLocation(200);
		firstUpperPane.setEnabled(false);

		
		JSplitPane secondUpperPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, firstUpperPane, attributePane);
		secondUpperPane.setDividerLocation(400);
		secondUpperPane.setEnabled(false);		

		JSplitPane thirdUpperPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, secondUpperPane, reviewPane);
		thirdUpperPane.setDividerLocation(600);
		thirdUpperPane.setEnabled(false);

		JSplitPane topPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, thirdUpperPane, busResultPane );
		topPane.setDividerLocation(870);
		topPane.setEnabled(false);

		JSplitPane firstLowerPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, queryPane, executePane);
		firstLowerPane.setDividerLocation(200);
		firstLowerPane.setEnabled(false);

		JSplitPane secondLowerPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, userPane, firstLowerPane);
		secondLowerPane.setDividerLocation(300);
		secondLowerPane.setEnabled(false);

		JSplitPane bottomPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,secondLowerPane, userresultPane);
		bottomPane.setDividerLocation(700);
		bottomPane.setEnabled(false);

		JSplitPane mainPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPane, bottomPane);
		mainPane.setDividerLocation(400);
		mainPane.setEnabled(false);
		
		conn_string = "INTERSECT";
	
		getContentPane().add(mainPane);

		//Establish Connection to database
		getDBConnection();

		unionIntersect();
		populateCataList();
		populatesubcataList();
		populateattriList();
		onPress();
		openReviewFrame();
		pop_user_result();
		openUserFrame();
	}

	private void unionIntersect() {
		ANDOR.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {

				cataList.clearSelection();
				addsubcataList.clear();
				addattriList.clear();
				addbusinessResultList.clear();
				JComboBox<String> combo = (JComboBox<String>) event.getSource();
				String selectedBook = (String) combo.getSelectedItem();

				if (selectedBook.equals("AND")) {
					conn_string = "INTERSECT";
				} else if (selectedBook.equals("OR")) {
					conn_string = "UNION";
				}
			}
		});
	}
	
	private void populateCataList() {
		
		userValueTextField.setText("");
		votesValueTextField.setText("");
		
		StagePopulateCatQuery = "";
		StagePopulateCatQuery = "SELECT DISTINCT C_NAME FROM B_MAIN_CATEGORY ORDER BY C_NAME\n";
		try {
			ResultSet resultset1 = null;
			preparedStatement=con.prepareStatement(StagePopulateCatQuery);
			resultset1 = preparedStatement.executeQuery(StagePopulateCatQuery);
			int i = 0;
			while(resultset1.next())
			{
				if(!addcataList.contains(resultset1.getString("C_NAME")))
				{
					addcataList.addElement(resultset1.getString("C_NAME"));
				}
			}
			preparedStatement.close();
			resultset1.close();
		} catch(Exception ex) {
			System.out.println(ex);
		}
		cataList.setModel(addcataList);
	}
	
	private void populatesubcataList() {
		MouseListener mainCataMouseListener = new MouseAdapter() {			
			public void mouseClicked(MouseEvent e) {
				userValueTextField.setText("");
				votesValueTextField.setText("");
				selectedcataList.clear();
				selectedattriList.clear();
				selectedsubcataList.clear();
				subcataList.removeAll();
				
				if (businessTable.getRowCount() > 0) {
					for (int i = businessTable.getRowCount() - 1; i > -1; i--) {
						busResultTable.removeRow(i);
					}
				}
				
				StagePopulateSubCatQuery = "";
				addsubcataList.clear();
				addattriList.clear();
				selectedcataList = (ArrayList<String>) cataList.getSelectedValuesList();
				
				StagePopulateSubCatQuery = "SELECT DISTINCT BSC.C_NAME from b_main_category bmc\r\n" + 
						"inner join b_sub_category bsc on bmc.bid=bsc.bid\r\n" + 
						"where BMC.C_NAME = '" + selectedcataList.get(0)+ "' ";
				
				if (conn_string == "INTERSECT") {
					for(int i=1;i<selectedcataList.size();i++) {
						StagePopulateSubCatQuery = StagePopulateSubCatQuery + " INTERSECT SELECT DISTINCT BSC.C_NAME from b_main_category bmc\r\n" + 
														"inner join b_sub_category bsc on bmc.bid=bsc.bid \r\n" + 
														"where BMC.C_NAME = '" + selectedcataList.get(i) + "'";
					}
				}
				else if (conn_string == "UNION") {
					for(int i=1;i<selectedcataList.size();i++) {
						StagePopulateSubCatQuery = StagePopulateSubCatQuery + " UNION SELECT DISTINCT BSC.C_NAME from b_main_category bmc\r\n" + 
														"inner join b_sub_category bsc on bmc.bid=bsc.bid \r\n" + 
														"where BMC.C_NAME = '" + selectedcataList.get(i) + "'";
					}
				} 	
				try {
				
					ResultSet resultset2 = null;
					preparedStatement=con.prepareStatement(StagePopulateSubCatQuery);
					resultset2 = preparedStatement.executeQuery(StagePopulateSubCatQuery);
					
					while(resultset2.next())
					{
						if(!addsubcataList.contains(resultset2.getString("C_NAME"))){
							addsubcataList.addElement(resultset2.getString("C_NAME"));
						}
					}
					preparedStatement.close();
					resultset2.close();
				}catch(Exception ex) {
						System.out.println(ex);
				}
				subcataList.setModel(addsubcataList);				
			}
		};
		cataList.addMouseListener(mainCataMouseListener);
	}


	private void populateattriList() {
		MouseListener subCataMouseListener = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				userValueTextField.setText("");
				votesValueTextField.setText("");
				selectedattriList.clear();

				StagePopulateAttriQuery= "";
				addattriList.clear();
				
				selectedsubcataList = (ArrayList<String>) subcataList.getSelectedValuesList();
				selectedcataList = (ArrayList<String>) cataList.getSelectedValuesList();
				
				StagePopulateAttriQuery = "select distinct ba.a_name from b_main_category bmc\r\n" + 
						"inner join b_sub_category bsc on bmc.bid=bsc.bid\r\n" +
						"inner join b_attributes ba on bmc.bid=ba.bid\r\n "
						+ "Where bmc.c_name = '" + selectedcataList.get(0) 
						+ "' and "
						+ "bsc.c_name = '" + selectedsubcataList.get(0) + "'\n";
				if (selectedsubcataList.size() > 0) {
				for(int i=0;i<selectedcataList.size();i++) {
					for(int j=1;j<selectedsubcataList.size();j++) {
						StagePopulateAttriQuery = StagePopulateAttriQuery + conn_string + " select distinct ba.a_name from b_main_category bmc\r\n"
									+ "inner join b_sub_category bsc on bmc.bid=bsc.bid\r\n"
									+ "inner join b_attributes ba on bmc.bid=ba.bid\r\n "
									+ "Where bmc.c_name = '" + selectedcataList.get(i) 
									+ "' and "
									+ "bsc.c_name = '" + selectedsubcataList.get(j) + "' \n";
					}
					break;
				}
				}
				for(int i=1;i<selectedcataList.size();i++) {
					for(int j=0;j<selectedsubcataList.size();j++) {
						StagePopulateAttriQuery = StagePopulateAttriQuery + conn_string + " select distinct ba.a_name from b_main_category bmc\r\n"
									+ "inner join b_sub_category bsc on bmc.bid=bsc.bid\r\n"
									+ "inner join b_attributes ba on bmc.bid=ba.bid\r\n "
									+ "Where bmc.c_name = '" + selectedcataList.get(i) 
									+ "' and "
									+ "bsc.c_name = '" + selectedsubcataList.get(j) + "' \n";
					}
				}
				
				try {
					ResultSet resultset2 = null;
					preparedStatement=con.prepareStatement(StagePopulateAttriQuery);
					resultset2 = preparedStatement.executeQuery(StagePopulateAttriQuery);
					
					while(resultset2.next())
					{
						if(!addattriList.contains(resultset2.getString("A_NAME"))){
							addattriList.addElement(resultset2.getString("A_NAME"));
						}
					}
					preparedStatement.close();
					resultset2.close();
				}catch(Exception ex) {
						System.out.println(ex);
				}
				attriList.setModel(addattriList);				
			}
		};
		subcataList.addMouseListener(subCataMouseListener);
	}
	
	private void onPress() {
		MouseListener attributeMouseListener = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				userValueTextField.setText("");
				votesValueTextField.setText("");
				selectedattriList = (ArrayList<String>) attriList.getSelectedValuesList();
			}
		};
		attriList.addMouseListener(attributeMouseListener);		
	}
		
	private void executeQuerryButtonActionPerformed(ActionEvent evt) {

		if (businessTable.getRowCount() > 0) {
			for (int i = businessTable.getRowCount() - 1; i > -1; i--) {
				busResultTable.removeRow(i);
			}
		}

		if(!selectedcataList.isEmpty()) {
		
			addbusinessResultList.clear();
			String FinalBusQuery = "";
			
			FinalBusQuery = "SELECT distinct BS.B_NAME, BS.CITY, BS.STATE, BS.STARS, BS.BID from business BS\r\n";
			
			if(!selectedcataList.isEmpty()) {
				FinalBusQuery += "inner join b_main_category bms on bs.bid=bms.bid\r\n";
				if(!selectedsubcataList.isEmpty()) {
					FinalBusQuery += "inner join b_sub_category bsc on bsc.bid=bms.bid\r\n";
					if(!selectedattriList.isEmpty()) 
						FinalBusQuery += "inner join b_attributes ba on ba.bid=bsc.bid\r\n";
				}
			}
			
			FinalBusQuery += " where \r\n";
			String ArdhiQuery = FinalBusQuery;
			
			if(!selectedcataList.isEmpty()) {
				for(int i=0; i<selectedcataList.size();i++) {	
					if(!selectedsubcataList.isEmpty()) {
						for(int j=0;j<selectedsubcataList.size();j++) {
							
							//if attribute is selected along with sub and cate
							if(!selectedattriList.isEmpty()) {
								for(int k=0;k<selectedattriList.size(); k++) {
									FinalBusQuery += " bms.c_name= '" +selectedcataList.get(i)+ "' and \r\n"+ 
											"bsc.c_name= '" +selectedsubcataList.get(j)+ "' and \r\n"+
											"ba.a_name= '" +selectedattriList.get(k)+ "' \r\n";
									if((i+1 < selectedcataList.size()) || 
											(j+1 < selectedsubcataList.size()) || 
											(k+1 < selectedattriList.size())) {
										FinalBusQuery += conn_string +" "+ ArdhiQuery;
									}
								}	
							}
							
							//if just sub and main is selected
							if(selectedattriList.isEmpty()) {
								FinalBusQuery += " bms.c_name= '" +selectedcataList.get(i)+ "' and \r\n"+ 
										"bsc.c_name= '" +selectedsubcataList.get(j)+ "' \r\n";
								if((i+1 < selectedcataList.size()) || 
										(j+1 < selectedsubcataList.size())) {
									FinalBusQuery += conn_string +" "+ ArdhiQuery;
								}
							}
						}
					}

					if(selectedsubcataList.isEmpty()) {
						FinalBusQuery += " bms.c_name= '" +selectedcataList.get(i)+ "' \r\n"; 
						if((i+1 < selectedcataList.size()))
							FinalBusQuery += conn_string +" "+ ArdhiQuery;
					}
				}
			}
					
			int antTracker=0;
			String FinalRevQuery="";
			
			if(!userValueTextField.getText().isEmpty() || !votesValueTextField.getText().isEmpty()||
					(!fromdatede.getTextField().getText().toString().equals("1900-05-30")) ||
					(!todatede.getTextField().getText().toString().equals("1900-05-30"))) 
					{
				int votesTracker=0;
				if (!votesValueTextField.getText().isEmpty()) {
					antTracker=1;
					FinalRevQuery = "select * from (" + FinalBusQuery + ") abc where abc.bid in (select r.bid from reviews r where r.FUNNY_VOTE + "
							+ "r.USEFUL_VOTE + r.COOL_VOTE " + votesComboBox.getSelectedItem() + votesValueTextField.getText() + ") \n";
					
					votesTracker=1;
				}
			
				int fromDateHaiFlag=0;	
				if (!fromdatede.getTextField().getText().toString().equals("1900-05-30")) {
					fromDateHaiFlag=1;
					antTracker=1;
					if(votesTracker==1) {
						
						FinalRevQuery = "select * from (" + FinalBusQuery + ") abc where abc.bid in (select r.bid from reviews r where r.FUNNY_VOTE + "
							+ "r.USEFUL_VOTE + r.COOL_VOTE " + votesComboBox.getSelectedItem() + votesValueTextField.getText() 
							+ " and r.R_DATE>to_date('"+ fromdatede.getTextField().getText() +"','yyyy-MM-dd')) \n";
				}
				else {
					
					antTracker=1;
				FinalRevQuery += "select * from (" + FinalBusQuery + ") abc "
						+ "where abc.bid in (select r.bid from reviews r "
						+ "where R.R_DATE > to_date('"+ fromdatede.getTextField().getText() +"','yyyy-MM-dd')) \n";
				}
					fromDateHaiFlag=1;
			}
			
			if(!todatede.getTextField().getText().toString().equals("2018-06-01")) {
				if(votesTracker==1 && fromDateHaiFlag==1) {
					antTracker=1;
					FinalRevQuery = "select * from (" + FinalBusQuery + ") abc where abc.bid in (select r.bid from reviews r where (r.FUNNY_VOTE "
							+ "+ r.USEFUL_VOTE + r.COOL_VOTE) " + votesComboBox.getSelectedItem() + votesValueTextField.getText()
							+ " and r.R_DATE>to_date('"+ fromdatede.getTextField().getText() +"','yyyy-MM-dd') \n"
							+ "and r.R_DATE<to_date('"+ todatede.getTextField().getText() +"','yyyy-MM-dd')) \n";
				}
				else if(votesTracker==1 && fromDateHaiFlag==0) {
					antTracker=1;
					FinalRevQuery = "select * from (" + FinalBusQuery + ") abc where abc.bid in (select r.bid from reviews r where (r.FUNNY_VOTE "
							+ "+ r.USEFUL_VOTE + r.COOL_VOTE) " + votesComboBox.getSelectedItem() + votesValueTextField.getText()
							+ " and r.R_DATE < to_date('"+ todatede.getTextField().getText() +"','yyyy-MM-dd')) \n ";
				}
				else if(votesTracker==0 && fromDateHaiFlag==1) {
					antTracker=1;
					FinalRevQuery = "select * from (" + FinalBusQuery + ") abc "
							+ "where abc.bid in (select r.bid from reviews r "
							+ "where R.R_DATE > to_date('"+ fromdatede.getTextField().getText() +"','yyyy-MM-dd') \n"
							+ "and r.R_DATE < to_date('"+ todatede.getTextField().getText() +"','yyyy-MM-dd')) \n";
				}
				else {
					antTracker=1;
					FinalRevQuery = "select * from (" + FinalBusQuery + ") abc "
							+ "where abc.bid in (select r.bid from reviews r "
							+ "where R.R_DATE < to_date('"+ todatede.getTextField().getText() +"','yyyy-MM-dd')) \n";
				}
				antTracker=1;
			}
			
			if (!userValueTextField.getText().isEmpty()) {
				float starsCount = Float.parseFloat((String) userValueTextField.getText());
				if(antTracker==1) {
					FinalRevQuery += " and abc.stars " +startsComboBox.getSelectedItem() + starsCount +" \n"  ;
				}
				else
					FinalRevQuery = "select * from (" + FinalBusQuery + ") abc where stars" + 
					 startsComboBox.getSelectedItem() + starsCount +" \n" ;
				antTracker=1;
			}
			
			}	
			if(FinalRevQuery != "") {
				FinalBusQuery = FinalRevQuery;
			}
			
			try {
				ResultSet resultset3 = null;
				preparedStatement=con.prepareStatement(FinalBusQuery);
				resultset3 = preparedStatement.executeQuery(FinalBusQuery);
				int i =0;
				String[] rowObj = new String[4];
				while(resultset3.next())
				{
					rowObj = new String[] {resultset3.getString("B_NAME"), resultset3.getString("CITY"), resultset3.getString("STATE"), resultset3.getString("STARS")};
					busResultTable.addRow(rowObj);
					bIDMap.put(i++, resultset3.getString("BID"));
				}
				queryField.setText(FinalBusQuery);
				preparedStatement.close();
				resultset3.close();
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}	
	
	private void openReviewFrame() { 
		businessTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1) {
					JFrame frame = new JFrame("List of reviews for selected business");

					JPanel panel = new JPanel();
					panel.setLayout(new FlowLayout());
					JTable target = (JTable)e.getSource();
					int row = target.getSelectedRow();
					String ardhiReviewFrameQuery = "";
					ardhiReviewFrameQuery = "SELECT R.R_DATE, R.STARS, R.R_TEXT, Y.USER_NAME, R.USEFUL_VOTE, R.FUNNY_VOTE, R.COOL_VOTE\r\n" + 
							"FROM REVIEWS R, BUSINESS B, YELP_USER Y\r\n" + 
							"WHERE R.USER_ID = Y.USER_ID AND B.BID = R.BID AND B.BID = '"+bIDMap.get(row)+"'"; 
									
					busReviewJTable = new JTable();
					busReviewJTable.setBounds(60,80,20,20);
				
					reviewTable = new DefaultTableModel();
					busReviewJTable.setModel(reviewTable);
					reviewTable.addColumn("User Name");
					reviewTable.addColumn("Review Date");
					reviewTable.addColumn("Review");
					reviewTable.addColumn("Stars");
					reviewTable.addColumn("Useful Vote");
					reviewTable.addColumn("Funny Vote");
					reviewTable.addColumn("Cool Vote");
					JScrollPane reviewResultPane = new JScrollPane(busReviewJTable);
					reviewResultPane.getViewport().getView().setBackground(Color.decode(COLOR2));
					panel.add(reviewResultPane);
					String[] rowObj = new String[7];
					try {
						ResultSet resultset3 = null;
						preparedStatement=con.prepareStatement(ardhiReviewFrameQuery);
						resultset3 = preparedStatement.executeQuery(ardhiReviewFrameQuery);

						while(resultset3.next())
						{
							rowObj = new String[] {resultset3.getString("USER_NAME"), resultset3.getString("R_DATE"), resultset3.getString("R_TEXT"), resultset3.getString("STARS"), resultset3.getString("USEFUL_VOTE"), resultset3.getString("FUNNY_VOTE"), resultset3.getString("COOL_VOTE")};				
							reviewTable.addRow(rowObj);
						}
						preparedStatement.close();
						resultset3.close();
					} catch(Exception ex) {
						System.out.println(ex);
					}
					frame.add(panel);
					frame.setSize(1000, 1000);
					frame.setLocationRelativeTo(null);	
					frame.setVisible(true);
				}
			}
		});

	}

	private void pop_user_result() {
		MouseListener userMouseListener = new MouseAdapter() 
		{
			public void mouseClicked(MouseEvent e) 
			{
				if (e.getClickCount() == 1) {
					if (businessTable.getRowCount() > 0) {
						for (int i = businessTable.getRowCount() - 1; i > -1; i--) {
							busResultTable.removeRow(i);
						}
					}
				}

				if((Integer.parseInt((String) countValueTextField.getText())) !=0) {
					queryField.setText(" ");
					if (businessTable.getRowCount() > 0) {
						for (int i = businessTable.getRowCount() - 1; i > -1; i--) {
							busResultTable.removeRow(i);
						}
					}

					String FinalSubQuery = "";
					int reviewCount = Integer.parseInt((String) countValueTextField.getText());

					FinalSubQuery += "SELECT DISTINCT YU.USER_NAME, YU.YELPING_SINCE, YU.AVERAGE_STARS \r\n" + 
							"FROM YELP_USER YU \r\n" + 
							"WHERE YU.REVIEW_COUNT " + reviewCountLabelComboBox.getSelectedItem() +
							" " + reviewCount;

					queryField.setText(FinalSubQuery);

					try {
						ResultSet resultset3 = null;
						preparedStatement=con.prepareStatement(FinalSubQuery);
						resultset3 = preparedStatement.executeQuery(FinalSubQuery);
						int i =0;
						String[] rowObj = new String[4];

						while(resultset3.next()){
							rowObj = new String[] {resultset3.getString("USER_NAME"), resultset3.getString("YELPING_SINCE"), resultset3.getString("AVERAGE_STARS")};
							busResultTable.addRow(rowObj);
							bIDMap.put(i++, resultset3.getString("BID"));
						}
						preparedStatement.close();
						resultset3.close();
					}
					catch(Exception ex) {
						ex.printStackTrace();
					}	
				}	
			}
		};
		userPanel.addMouseListener(userMouseListener);
	}

	private void userQuerryButtonActionPerformed(ActionEvent evt) {
		int antTracker=0;

		if (userresultTable.getRowCount() > 0) {
			for (int i = userresultTable.getRowCount() - 1; i > -1; i--) {
				userresultTable.removeRow(i);
			}
		}
		
		String UserQuery= "select distinct y.user_name, y.yelping_since, y.average_stars from yelp_user y"
				+ " where\r\n";

		if (!memberSinceTextField.getText().isEmpty()) {
			UserQuery += " y.yelping_since > to_date('"  + memberSinceTextField.getText() +"','dd-mm-yyyy') \n";
			antTracker=1;		
		}
		
		if(!countValueTextField.getText().isEmpty()) {
			int reviewCount = Integer.parseInt((String) countValueTextField.getText());
			if(antTracker == 1)
				UserQuery += " "+andOrLabelComboBox.getSelectedItem()+" ";
			UserQuery += " y.review_count "+ reviewCountLabelComboBox.getSelectedItem() +" "+ reviewCount;
			antTracker=1;
		}
		
		if(!frndsCountValueTextField.getText().isEmpty()) {
			if(antTracker == 1)
				UserQuery += " "+andOrLabelComboBox.getSelectedItem()+" ";
			UserQuery += " y.user_id in (SELECT F.USER_ID FROM  FRIENDS F WHERE F.USER_ID = Y.USER_ID \r\n" + 
					" group by f.user_id having count(F.user_id) " + friendsCountLabelComboBox.getSelectedItem() 
			 + frndsCountValueTextField.getText() +") ";
		}
		
		if(!avgStarTextField.getText().isEmpty()) {
			if(antTracker == 1)
				UserQuery += " "+andOrLabelComboBox.getSelectedItem()+" ";
			UserQuery += " Y.average_stars " + avgStarComboBox.getSelectedItem() +
					" " + avgStarTextField.getText();
			antTracker=1;
		}

		if(!votesUserTextField.getText().isEmpty()) {
			if(antTracker == 1)
				UserQuery += " "+andOrLabelComboBox.getSelectedItem()+" ";
			UserQuery += " (Y.FUNNY_VOTES + Y.USEFUL_VOTES + Y.COOL_VOTES) " + votesUserComboBox.getSelectedItem() +
					" " + votesUserTextField.getText();
		}
			queryField.setText(UserQuery);
			try {
				ResultSet resultset3 = null;
				preparedStatement=con.prepareStatement(UserQuery);
				resultset3 = preparedStatement.executeQuery(UserQuery);
				int i = 0;
				String[] rowObj = new String[3];

				while(resultset3.next()){
					rowObj = new String[] {resultset3.getString("USER_NAME"), resultset3.getString("YELPING_SINCE"), resultset3.getString("AVERAGE_STARS")};
					userresultTable.addRow(rowObj);
					userMap.put(i++, resultset3.getString("USER_NAME"));
				}
				preparedStatement.close();
				resultset3.close();
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}	
		}	
	

	private void openUserFrame() { 
		userTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1) {
					JFrame userframe = new JFrame("User JFrame");

					JPanel userpanel = new JPanel();
					userpanel.setLayout(new FlowLayout());
					JTable usertarget = (JTable)e.getSource();
					int row = usertarget.getSelectedRow();

					String userquerry = "SELECT R.R_DATE, R.STARS, R.R_TEXT, Y.USER_NAME, Y.USER_ID, R.USEFUL_VOTE, R.FUNNY_VOTE, R.COOL_VOTE\r\n" + 
							"FROM REVIEWS R, YELP_USER Y\r\n" + 
							"WHERE R.USER_ID = Y.USER_ID AND Y.USER_NAME LIKE '" + userMap.get(row)+"'";

					userreviewJTable = new JTable();
					userreviewJTable.setBounds(300,400,1000,1000);
					userreviewTable = new DefaultTableModel();
					userreviewJTable.setModel(userreviewTable);
					userreviewTable.addColumn("Review Date");
					userreviewTable.addColumn("Stars");
					userreviewTable.addColumn("Review");
					userreviewTable.addColumn("User Name");
					userreviewTable.addColumn("Useful Vote");
					userreviewTable.addColumn("Funny Vote");
					userreviewTable.addColumn("Cool Vote");

					JScrollPane userreviewResultPane = new JScrollPane(userreviewJTable);
					userreviewResultPane.getViewport().getView().setBackground(Color.decode(COLOR2));
					userpanel.add(userreviewResultPane);
					String[] rowObj = new String[7];

					try {
						ResultSet resultset3 = null;
						preparedStatement=con.prepareStatement(userquerry);
						resultset3 = preparedStatement.executeQuery(userquerry);

						while(resultset3.next())
						{
							rowObj = new String[] {resultset3.getString("R_DATE"), resultset3.getString("STARS"), resultset3.getString("R_TEXT"), resultset3.getString("USER_NAME"), resultset3.getString("USEFUL_VOTE"), resultset3.getString("FUNNY_VOTE"), resultset3.getString("COOL_VOTE")};
							userreviewTable.addRow(rowObj);
						}
						preparedStatement.close();
						resultset3.close();
					} catch(Exception ex) {
						System.out.println(ex);
					}
					userframe.add(userpanel);
					userframe.setSize(1800, 1200);
					userframe.setLocationRelativeTo(null);
					userframe.setVisible(true);
				}
			}
		});
	}
}
