package net.net16.jeremiahlowe.webserver.ui.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import net.net16.jeremiahlowe.ansipro.AnsiColor;
import net.net16.jeremiahlowe.webserver.cfg.MimeType;
import net.net16.jeremiahlowe.webserver.utility.Instance;
import net.net16.jeremiahlowe.webserver.utility.Utility;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class GUI extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	public AnsiTextPane serverOutput;
	private JTextField txtHTML;
	private JToggleButton tglbtnStartstopServer;
	private JSpinner portSpinner;
	private JSpinner threadsSpinner;
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
	private Component horizontalGlue;
	private JCheckBox chckbxEnablePhp;
	private JFileChooser fcHTML;
	private boolean notAdded = true;
	private JButton btnClear;
	
	public GUI() {
		setAlwaysOnTop(true);
		setResizable(false);
		setTitle("Java webserver (c) Jeremiah Lowe 2016-2018");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
		serverOutput = new AnsiTextPane();
		serverOutput.setContentType("text/plain");
		serverOutput.setDefaultForeground(AnsiColor.Black);
		serverOutput.setDefaultBackground(AnsiColor.White);
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
		horizontalGlue = Box.createHorizontalGlue();
		horizontalBox_1.add(horizontalGlue);
		threadsSpinner = new JSpinner();
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
		threadsSpinner.setModel(new SpinnerNumberModel(2, 1, 128, 1));
		threadsSpinner.setBounds(166, 41, 78, 20);
		panel_1.add(threadsSpinner);
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
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension guiSize = new Dimension(550, 300);
		String os = Utility.getOS();
		guiSize.width = os.contains("windows") ? 500 : 600;
		guiSize.height = os.contains("windows") ? 300 : 400;
		setBounds(screenSize.width / 2 - guiSize.width / 2, screenSize.height / 2 - guiSize.height / 2, guiSize.width, guiSize.height);
	}
	public void addListeners(){
		if(notAdded){
			GUI guiRef = this;
			btnAdd.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					addMimeType(new MimeType(txtFileEnding.getText(), txtMimeType.getText()));
					updateMimeTypes();
				}
			});
			fcHTML.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					String file = fcHTML.getSelectedFile().getPath();
					txtHTML.setText(file);
				}
			});
			threadsSpinner.addChangeListener(new ChangeListener() {
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
					if(Instance.globalInstance.config.getPHPEnabled()){
						Utility.verifyPHP();
						JOptionPane.showMessageDialog(guiRef, "Verifying PHP, This may take a bit!");
					}
				}
			});
			btnBrowse.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					fcHTML.showOpenDialog(Instance.globalInstance.gui);
				}
			});
			tglbtnStartstopServer.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					onServerChange();
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
		//serverOutput.set
		//serverOutput.setText(serverOutput.getText() + message + System.lineSeparator());
		//serverOutput.setCaretPosition(serverOutput.getText().length());
		serverOutput.appendAnsi(message + System.lineSeparator());
	}
	public int getThreads(){
		return (int) threadsSpinner.getValue();
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
		Instance.globalInstance.delegator.setServerRunning(startOrStop);
	}
	public void setGuiOptions(){
		if(Instance.globalInstance.config.getPort() != 80){
			portSpinner.setEnabled(true);
			chckbxUseCustomPort.setSelected(true);
		}
		else{
			portSpinner.setEnabled(false);
			chckbxUseCustomPort.setSelected(false);
		}
		txtHTML.setText(Instance.globalInstance.config.getHTMLFile());
		threadsSpinner.setValue(Instance.globalInstance.config.getThreads());
		portSpinner.setValue(Instance.globalInstance.config.getPort());
		setPHPEnabled(Instance.globalInstance.config.getPHPEnabled());
		setLogToFile(Instance.globalInstance.config.getLogToFile());
	}
	public void updateMimeTypes(){
		String show = "";
		for(MimeType m : MimeType.mimeTypes){
			show += m.fileName + " = " + m.mimeType + "\n\r";
		}
		customMimeTypes.setText(show);
	}
	public void addMimeType(MimeType in){
		Instance.globalInstance.mimeTypeLoader.addMimeType(in);
	}
	private void onGUIOptionChanged(){
		Instance.globalInstance.config.setThreads(getThreads());
		Instance.globalInstance.config.setHTML(getHTML());
		Instance.globalInstance.config.setPort(getPort());
		Instance.globalInstance.config.setLogToFile(shouldLogToFile());
		Instance.globalInstance.config.setPHPEnabled(getPHPEnabled());
		Instance.globalInstance.config.save();
	}
}
