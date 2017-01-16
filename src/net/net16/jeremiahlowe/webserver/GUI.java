package net.net16.jeremiahlowe.webserver;

import java.awt.BorderLayout;
import java.awt.TextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JToggleButton;
import javax.swing.JTabbedPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;

public class GUI extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	public TextArea serverOutput;
	private JTextField txtHTML;
	private JToggleButton tglbtnStartstopServer;
	private JSpinner portSpinner;
	private JSpinner maxConn;
	private TextArea customMimeTypes;
	private JCheckBox chckbxUseCustomPort;
	private Box horizontalBox;
	private JTextField txtFileEnding;
	private JTextField txtMimeType;
	private JButton btnAdd;
	private Box horizontalBox_1;
	private JCheckBox logToFile;
	private JTabbedPane tabbedPane;
	private JButton btnBrowse;
	private JCheckBox chckbxLogHttp;
	private JCheckBox chckbxLogDelegation;
	private Component horizontalGlue;
	private JCheckBox chckbxEnablePhp;
	private JFileChooser fcHTML;
	private boolean notAdded = true;
	private JButton btnClear;
	public GUI() {
		setAlwaysOnTop(true);
		setResizable(false);
		setTitle("Java webserver");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		JPanel panel = new JPanel();
		fcHTML = new JFileChooser();
		tabbedPane.addTab("Console", null, panel, null);
		panel.setLayout(new BorderLayout(0, 0));
		serverOutput = new TextArea();
		serverOutput.setEditable(false);
		panel.add(serverOutput);
		horizontalBox_1 = Box.createHorizontalBox();
		panel.add(horizontalBox_1, BorderLayout.SOUTH);
		tglbtnStartstopServer = new JToggleButton("Start/Stop");
		horizontalBox_1.add(tglbtnStartstopServer);
		btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				serverOutput.setText("");
			}
		});
		horizontalBox_1.add(btnClear);
		logToFile = new JCheckBox("Log output to file");
		horizontalBox_1.add(logToFile);
		chckbxLogDelegation = new JCheckBox("Log delegation");
		horizontalBox_1.add(chckbxLogDelegation);
		chckbxLogDelegation.setSelected(true);
		chckbxLogHttp = new JCheckBox("Log HTTP");
		horizontalBox_1.add(chckbxLogHttp);
		horizontalGlue = Box.createHorizontalGlue();
		horizontalBox_1.add(horizontalGlue);
		maxConn = new JSpinner();
		portSpinner = new JSpinner();
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Settings", null, panel_1, null);
		panel_1.setLayout(null);
		portSpinner.setModel(new SpinnerNumberModel(80, 0, 65535, 1));
		portSpinner.setBounds(166, 9, 78, 20);
		panel_1.add(portSpinner);
		chckbxUseCustomPort = new JCheckBox("Use custom port");
		chckbxUseCustomPort.setBounds(12, 8, 146, 23);
		panel_1.add(chckbxUseCustomPort);
		JLabel lblcCopyrightJeremiah = new JLabel(" \u00A9 Jeremiah Lowe  2016-2017");
		lblcCopyrightJeremiah.setBounds(0, 213, 429, 20);
		panel_1.add(lblcCopyrightJeremiah);
		maxConn.setModel(new SpinnerNumberModel(100, 0, 1000, 10));
		maxConn.setBounds(166, 41, 78, 20);
		panel_1.add(maxConn);
		JLabel lblMaximumThreadedConnections = new JLabel(" Maximum connections");
		lblMaximumThreadedConnections.setBounds(12, 44, 146, 14);
		panel_1.add(lblMaximumThreadedConnections);
		JLabel lblHtmlFile = new JLabel(" Default HTML file");
		lblHtmlFile.setBounds(12, 70, 146, 16);
		panel_1.add(lblHtmlFile);
		txtHTML = new JTextField();
		txtHTML.setText("index.html");
		txtHTML.setBounds(166, 68, 220, 20);
		panel_1.add(txtHTML);
		txtHTML.setColumns(10);
		btnBrowse = new JButton("...");
		btnBrowse.setBounds(398, 69, 19, 19);
		panel_1.add(btnBrowse);
		chckbxEnablePhp = new JCheckBox("Enable PHP");
		chckbxEnablePhp.setBounds(12, 94, 129, 23);
		panel_1.add(chckbxEnablePhp);
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Mime types", null, panel_2, null);
		panel_2.setLayout(new BorderLayout(0, 0));
		customMimeTypes = new TextArea();
		customMimeTypes.setEditable(false);
		panel_2.add(customMimeTypes, BorderLayout.CENTER);
		horizontalBox = Box.createHorizontalBox();
		panel_2.add(horizontalBox, BorderLayout.SOUTH);
		txtFileEnding = new JTextField();
		txtFileEnding.setText(".txt");
		horizontalBox.add(txtFileEnding);
		txtFileEnding.setColumns(10);
		txtMimeType = new JTextField();
		txtMimeType.setText("text/plain");
		txtMimeType.setColumns(10);
		horizontalBox.add(txtMimeType);
		btnAdd = new JButton("Add");
		horizontalBox.add(btnAdd);
	}
	public void addListeners(){
		if(notAdded){
			btnAdd.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					addMimeType(new MimeType(txtFileEnding.getText(), txtMimeType.getText()));
				}
			});
			fcHTML.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					String file = fcHTML.getSelectedFile().getPath();
					txtHTML.setText(file);
				}
			});
			maxConn.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					onGUIOptionChanged();
				}
			});
			portSpinner.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent arg0) {
					onGUIOptionChanged();
				}
			});
			chckbxUseCustomPort.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					portSpinner.setEnabled(chckbxUseCustomPort.isSelected());
					onGUIOptionChanged();
				}
			});
			txtHTML.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					onGUIOptionChanged();
				}
			});
			chckbxEnablePhp.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					onGUIOptionChanged();
					Startup.verifyPHP();
				}
			});
			btnBrowse.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					fcHTML.showOpenDialog(Instance.gui);
				}
			});
			chckbxLogDelegation.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					onGUIOptionChanged();
				}
			});
			tglbtnStartstopServer.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					onServerChange();
					onGUIOptionChanged();
				}
			});
			chckbxLogHttp.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					onGUIOptionChanged();
				}
			});
			notAdded = true;
		}
		else throw new RuntimeException("Listeners already added!");
	}
	public boolean getPHPEnabled(){
		return chckbxEnablePhp.isSelected();
	}
	public void setPHPEnabled(boolean enabled){
		chckbxEnablePhp.setSelected(enabled);
	}
	public void setLogToFile(boolean logToFile){
		this.logToFile.setSelected(logToFile);
	}
	public boolean shouldLogToFile(){
		return logToFile.isSelected();
	}
	public void log(String message){
		serverOutput.setText(serverOutput.getText() + message + "\n\r");
		serverOutput.setCaretPosition(serverOutput.getText().length());
	}
	public int getMaxConnections(){
		return (int) maxConn.getValue();
	}
	public String getHTML(){
		return txtHTML.getText();
	}
	public int getPort(){
		if(portSpinner.isEnabled()) return (int) portSpinner.getValue();
		else return 80;
	}
	public void onServerChange(){
		boolean startOrStop = tglbtnStartstopServer.isSelected();
		tabbedPane.setEnabledAt(1, !startOrStop);
		tabbedPane.setEnabledAt(2, !startOrStop);
		Startup.onServerActivate(startOrStop);
	}
	public void setGuiOptions(){
		if(Instance.config.getPort() != 80){
			portSpinner.setEnabled(true);
			chckbxUseCustomPort.setSelected(true);
		}
		else{
			portSpinner.setEnabled(false);
			chckbxUseCustomPort.setSelected(false);
		}
		txtHTML.setText(Instance.config.getHTMLFile());
		maxConn.setValue(Instance.config.getClients());
		portSpinner.setValue(Instance.config.getPort());
		setPHPEnabled(Instance.config.getPHPEnabled());
		setLogToFile(Instance.config.getLogToFile());
	}
	public void updateMimeTypes(){
		String show = "";
		for(MimeType m : MimeType.mimeTypes){
			show += m.fileName + " = " + m.mimeType + "\n\r";
		}
		customMimeTypes.setText(show);
	}
	public void addMimeType(MimeType in){
		Instance.mimeTypeLoader.addMimeType(in);
	}
	private void onGUIOptionChanged(){
		Instance.config.setClients(getMaxConnections());
		Instance.config.setHTML(getHTML());
		Instance.config.setPort(getPort());
		Instance.config.setLogToFile(shouldLogToFile());
		Instance.config.setLogDelegator(chckbxLogDelegation.isSelected());
		Instance.config.setLogHTTP(chckbxLogHttp.isSelected());
		Instance.config.setPHPEnabled(getPHPEnabled());
		Instance.config.save();
	}
}
