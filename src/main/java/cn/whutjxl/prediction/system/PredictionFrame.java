package cn.whutjxl.prediction.system;

import java.awt.BorderLayout;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import cn.whutjxl.prediction.utils.PredUtils;

public class PredictionFrame extends JFrame {

	private static final int DEFAULT_WITH = 1000;
	private static final int DEFAULT_HEIGHT = 600;

	// trainField
	private JFrame thisJFrame;
	private JPanel panel1;
	private JPanel panel2;
	private JTabbedPane tabbedPane;
	private JPanel trainPanel;
	private JScrollPane resultScrollPane;
	private JTextArea resultTextArea;
	private JTextField trainFileField;
	private JButton trainFileSelectButton;
	private JButton trainButton;
	private JComboBox<String> classifierBox;
	private Map<String, String> classifierMap;

	// predictionField
	private JPanel predictionPanel;
	private JTextField predictionFileField;
	private JButton predictionSelectFileButton;
	private JComboBox<String> modelBox;
	private JButton predictionButton;
	private JPanel resultShowPanel;
	private JScrollPane showScrollPane;
	private JLabel resultLabel;
	private JPanel totalPanel;

	public PredictionFrame(String title) {
		// classifier
		classifierMap = new HashMap<String, String>();
		classifierMap.put("J48", "weka.classifiers.trees.J48");
		classifierMap.put("LADTree", "weka.classifiers.trees.LADTree");
		classifierMap.put("NBTree", "weka.classifiers.trees.NBTree");
		classifierMap.put("LibSVM", "weka.classifiers.functions.LibSVM");
		classifierMap.put("RBFNetwork", "weka.classifiers.functions.RBFNetwork");
		classifierMap.put("AODE", "weka.classifiers.bayes.AODE");
		classifierMap.put("HNB", "weka.classifiers.bayes.HNB");
		classifierMap.put("NaiveBayes", "weka.classifiers.bayes.NaiveBayes");
		classifierMap.put("WAODE", "weka.classifiers.bayes.WAODE");

		thisJFrame = this;
		setTitle(title);
		setSize(DEFAULT_WITH, DEFAULT_HEIGHT);
		setResizable(false);

		panel1 = new JPanel();
		panel2 = new JPanel();
		panel1.setLayout(new BorderLayout());
		panel2.setLayout(new BorderLayout());

		// train panel
		trainPanel = new JPanel();
		trainPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
		trainPanel.add(new JLabel("请选择股票历史数据:"));
		trainFileField = new JTextField(20);
		trainPanel.add(trainFileField);
		trainFileSelectButton = new JButton("...");
		trainFileSelectButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				/**
				 * JFileChooser
				 */
				/*
				 * JFileChooser jfc=new JFileChooser();
				 * jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				 * jfc.setMultiSelectionEnabled(false);
				 * if(jfc.showOpenDialog(thisJFrame)==JFileChooser.
				 * APPROVE_OPTION){ File f=jfc.getSelectedFile();
				 * trainFileField.setText(f.getAbsolutePath()); }
				 */

				/**
				 * FileDialog
				 */
				FileDialog fileDialog = new FileDialog(thisJFrame, "请选择历史数据", FileDialog.LOAD);
				fileDialog.setMultipleMode(false);
				fileDialog.setVisible(true);
				String fileName = fileDialog.getFile();
				if (fileName != null && !"".equals(fileName)) {
					trainFileField.setText(fileDialog.getDirectory() + fileName);
				}
			}
		});
		trainPanel.add(trainFileSelectButton);
		trainPanel.add(new JLabel("请选择分类器:"));
		classifierBox = new JComboBox<String>();
		for (String s : classifierMap.keySet()) {
			classifierBox.addItem(s);
		}
		trainPanel.add(classifierBox);
		trainButton = new JButton("开始训练");
		trainButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				String sourceFileName = trainFileField.getText().replace('\\', '/');
				String classifierName = classifierMap.get(classifierBox.getSelectedItem());
				if (sourceFileName == null || "".equals(sourceFileName)) {
					JOptionPane.showMessageDialog(thisJFrame, "未选择股票历史数据文件!", "警告", JOptionPane.WARNING_MESSAGE);
				} else if (!(sourceFileName.endsWith(".xls") || sourceFileName.endsWith(".xlsx"))) {
					JOptionPane.showMessageDialog(thisJFrame, "请选择Excel格式的文件!", "警告", JOptionPane.WARNING_MESSAGE);
				} else {
					File sourceFile = new File(sourceFileName);
					if (!sourceFile.exists()) {
						JOptionPane.showMessageDialog(thisJFrame, "文件路径有误!", "警告", JOptionPane.WARNING_MESSAGE);
					} else {
						resultTextArea.setText("");
						resultTextArea.paintImmediately(resultTextArea.getBounds());
						resultTextArea.setText("模型正在训练中,请稍候...\n\n\n\n");
						resultTextArea.paintImmediately(resultTextArea.getBounds());
						File destinationFile = new File("src/main/resources/arffs/stock.arff");
						List<Character> features = PredUtils.getFeaturesByExcel(sourceFile);
						for (int step = 7; step <= 10; step++) {
							PredUtils.createArff(features, destinationFile, step);
							String resultText = (String) PredUtils.trainModel(destinationFile, classifierName, true,
									sourceFile.getName().split("\\.")[0] + "_" + step);
							resultTextArea.append("\n\nstep=" + step + "\n\n");
							resultTextArea.append(resultText);
							resultTextArea.append("\n===============================================\n");
							resultTextArea.paintImmediately(resultTextArea.getBounds());
						}
						resultTextArea.append("\n模型训练完成\n\n\n\n\n");
						resultTextArea.paintImmediately(resultTextArea.getBounds());
						modelBox.removeAll();
						File folder = new File("src/main/resources/models/");
						File[] fileArr = folder.listFiles();
						for (int i = 0; i < fileArr.length; i++)
							modelBox.addItem(fileArr[i].getName().split("\\.")[0]);
					}
				}
			}
		});
		trainPanel.add(trainButton);
		panel1.add(trainPanel, BorderLayout.NORTH);

		// resultScrollPane
		resultTextArea = new JTextArea();
		resultScrollPane = new JScrollPane(resultTextArea);
		resultScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		resultScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		panel1.add(resultScrollPane, BorderLayout.CENTER);

		// predictionPanel
		predictionPanel = new JPanel();
		predictionPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
		predictionPanel.add(new JLabel("请选择待预测股票数据:"));
		predictionFileField = new JTextField(20);
		predictionPanel.add(predictionFileField);
		predictionSelectFileButton = new JButton("...");
		predictionSelectFileButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				FileDialog fileDialog = new FileDialog(thisJFrame, "请选择历史数据", FileDialog.LOAD);
				fileDialog.setMultipleMode(false);
				fileDialog.setVisible(true);
				String fileName = fileDialog.getFile();
				if (fileName != null && !"".equals(fileName)) {
					predictionFileField.setText(fileDialog.getDirectory() + fileName);
				}
			}
		});
		predictionPanel.add(predictionSelectFileButton);
		predictionPanel.add(new JLabel("请选择模型:"));
		modelBox = new JComboBox<String>();
		File folder = new File("src/main/resources/models/");
		File[] fileArr = folder.listFiles();
		for (int i = 0; i < fileArr.length; i++)
			modelBox.addItem(fileArr[i].getName().split("\\.")[0]);
		predictionPanel.add(modelBox);
		predictionButton = new JButton("开始预测");
		predictionButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				String sourceFileName = predictionFileField.getText().replace('\\', '/');
				String modelName = (String) modelBox.getSelectedItem();
				if (sourceFileName == null || "".equals(sourceFileName)) {
					JOptionPane.showMessageDialog(thisJFrame, "未选择股票历史数据文件!", "警告", JOptionPane.WARNING_MESSAGE);
				} else if (!(sourceFileName.endsWith(".xls") || sourceFileName.endsWith(".xlsx"))) {
					JOptionPane.showMessageDialog(thisJFrame, "请选择Excel格式的文件!", "警告", JOptionPane.WARNING_MESSAGE);
				} else if (modelName == null || "".equals(modelName)) {
					JOptionPane.showMessageDialog(thisJFrame, "当前没有任何模型,请先创建模型！", "警告", JOptionPane.WARNING_MESSAGE);
				} else {
					File sourceFile = new File(sourceFileName);
					if (!sourceFile.exists()) {
						JOptionPane.showMessageDialog(thisJFrame, "文件路径有误!", "警告", JOptionPane.WARNING_MESSAGE);
					} else {
						resultLabel.setText("正在预测中,请稍候...");
						resultLabel.paintImmediately(resultLabel.getBounds());
						File destinationFile = new File("src/main/resources/arffs/prediction.arff");
						int step = Integer.parseInt(modelName.split("_")[1]);
						List<Character> features = PredUtils.getFeaturesByExcel(sourceFile);
						features.add('?');
						PredUtils.createArff(features, destinationFile, step);
						String[] results = PredUtils.getPredictionResult(destinationFile, modelName);
						features.remove(features.size() - 1);
						int num = features.size();
						resultShowPanel.removeAll();

						int max = 10;
						if (num <= max) {
							for (int i = 0; i < num; i++) {
								addJLabel(resultShowPanel, features.get(i), 0);
							}
						} else {
							for (int i = 10; i >= 1; i--) {
								addJLabel(resultShowPanel, features.get(num - i), 0);
							}
						}
						addJLabel(resultShowPanel, results[0].charAt(0), 1);
						resultLabel.setText("预测完成!        预测精度为:" + results[1] + "%");
						resultLabel.paintImmediately(resultLabel.getBounds());
					}
				}
			}
		});
		predictionPanel.add(predictionButton);
		panel2.add(predictionPanel, BorderLayout.NORTH);

		// showPrediction
		totalPanel = new JPanel();
		totalPanel.setLayout(null);
		resultLabel = new JLabel();
		resultLabel.setBounds(220, 110, 560, 20);
		totalPanel.add(resultLabel);
		resultShowPanel = new JPanel();
		resultShowPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		showScrollPane = new JScrollPane(resultShowPanel);
		showScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		showScrollPane.setBounds(200, 140, 600, 128);
		totalPanel.add(showScrollPane);
		panel2.add(totalPanel, BorderLayout.CENTER);

		tabbedPane = new JTabbedPane();
		tabbedPane.add("training", panel1);
		tabbedPane.add("predicting", panel2);
		add(tabbedPane);
	}

	public static void addJLabel(JPanel jpanel, Character s, int state) {
		Icon icon = null;
		if (state == 0) {
			if (s.equals('U')) {
				icon = new ImageIcon("src/main/resources/pictures/ug.jpg");
			} else if (s.equals('F')) {
				icon = new ImageIcon("src/main/resources/pictures/fg.jpg");
			} else if (s.equals('-')) {
				icon = new ImageIcon("src/main/resources/pictures/sg.jpg");
			}
		} else if (state == 1) {
			if (s.equals('U')) {
				icon = new ImageIcon("src/main/resources/pictures/uy.jpg");
			} else if (s.equals('F')) {
				icon = new ImageIcon("src/main/resources/pictures/fy.jpg");
			} else if (s.equals('-')) {
				icon = new ImageIcon("src/main/resources/pictures/sy.jpg");
			}
		}
		jpanel.add(new JLabel(icon));
	}
}
