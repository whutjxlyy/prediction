package cn.whutjxl.prediction.system;

import java.awt.BorderLayout;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import cn.whutjxl.prediction.utils.PredictionUtils;

public class PredictionFrame extends JFrame {

	private static final int DEFAULT_WITH=600;
	private static final int DEFAULT_HEIGHT=500;
	
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
	
	public PredictionFrame(String title) {
		//  classifier
		classifierMap=new HashMap<String, String>();
		classifierMap.put("J48", "weka.classifiers.trees.J48");
		classifierMap.put("LADTree", "weka.classifiers.trees.LADTree");
		classifierMap.put("NBTree", "weka.classifiers.trees.NBTree");
		classifierMap.put("LibSVM", "weka.classifiers.functions.LibSVM");
		classifierMap.put("RBFNetwork", "weka.classifiers.functions.RBFNetwork");
		classifierMap.put("AODE", "weka.classifiers.bayes.AODE");
		classifierMap.put("HNB", "weka.classifiers.bayes.HNB");
		classifierMap.put("NaiveBayes", "weka.classifiers.bayes.NaiveBayes");
		classifierMap.put("WAODE", "weka.classifiers.bayes.WAODE");
		
		thisJFrame=this;
		setTitle(title);
		setSize(DEFAULT_WITH, DEFAULT_HEIGHT);
//		setResizable(false);
		
		panel1=new JPanel();
		panel2=new JPanel();
		panel1.setLayout(new BorderLayout());
		panel2.setLayout(new BorderLayout());
		
		//train panel
		trainPanel=new JPanel();
		trainPanel.setLayout(new FlowLayout(FlowLayout.CENTER,20,10));
		trainPanel.add(new JLabel("请选择股票历史数据:"));
		trainFileField=new JTextField(20);
		trainPanel.add(trainFileField);
		trainFileSelectButton=new JButton("...");
		trainFileSelectButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				/**
				 * JFileChooser
				 */
			/*	JFileChooser jfc=new JFileChooser();
				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				jfc.setMultiSelectionEnabled(false);
				if(jfc.showOpenDialog(thisJFrame)==JFileChooser.APPROVE_OPTION){
					File f=jfc.getSelectedFile();
					trainFileField.setText(f.getAbsolutePath());
				}*/
				
				/**
				 * FileDialog
				 */
				FileDialog fileDialog=new FileDialog(thisJFrame, "请选择历史数据", FileDialog.LOAD);
				fileDialog.setMultipleMode(false);
				fileDialog.setVisible(true);
				String fileName=fileDialog.getFile();
				if(fileName!=null&&!"".equals(fileName)){
					trainFileField.setText(fileDialog.getDirectory()+fileName);
				}
			}
		});
		trainPanel.add(trainFileSelectButton);
		trainPanel.add(new JLabel("请选择分类器:"));
		classifierBox=new JComboBox<String>();
		for (String s : classifierMap.keySet()) {
			classifierBox.addItem(s);
		}
		trainPanel.add(classifierBox);
		trainButton=new JButton("开始训练");
		trainButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				String sourceFileName=trainFileField.getText().replace('\\', '/');
				String classifierName=classifierMap.get(classifierBox.getSelectedItem());
				if(sourceFileName==null||"".equals(sourceFileName)){
					JOptionPane.showMessageDialog(thisJFrame, "未选择股票历史数据文件!", "警告", JOptionPane.WARNING_MESSAGE);
				}else if(!(sourceFileName.endsWith(".xls")||sourceFileName.endsWith(".xlsx"))){
					JOptionPane.showMessageDialog(thisJFrame, "请选择Excel格式的文件!", "警告", JOptionPane.WARNING_MESSAGE);
				}else{
					File sourceFile=new File(sourceFileName);
					if(!sourceFile.exists()){
						JOptionPane.showMessageDialog(thisJFrame, "文件路径有误!", "警告", JOptionPane.WARNING_MESSAGE);
					}else{
						int index=0;
						double max=0;
						resultTextArea.setText("模型正在训练中，请稍候...\n\n\n\n");
						resultTextArea.paintImmediately(resultTextArea.getBounds());
						File destinationFile=new File("src/main/resources/arffs/stock.arff");
						for(int i=4;i<40;i++){
							PredictionUtils.getArffByExcel(sourceFile, destinationFile, i, 0);
							double result=(Double) PredictionUtils.createModel(destinationFile, classifierName, null, false, "");
							if(result>max){
								index=i;
								max=result;
							}
						}
						PredictionUtils.getArffByExcel(sourceFile, destinationFile, index, 0);
						String resultText=(String) PredictionUtils.createModel(destinationFile, classifierName, null, true, sourceFile.getName().split("\\.")[0]);
						resultTextArea.append(resultText);
						resultTextArea.paintImmediately(resultTextArea.getBounds());
						resultTextArea.append("\n模型训练完成\n\n\n\n\n");
						resultTextArea.paintImmediately(resultTextArea.getBounds());
					}
				}
			}
		});
		trainPanel.add(trainButton);
		panel1.add(trainPanel, BorderLayout.NORTH);
		
		//resultScrollPane
		resultTextArea=new JTextArea();
		resultScrollPane=new JScrollPane(resultTextArea);
		resultScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		resultScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		panel1.add(resultScrollPane, BorderLayout.CENTER);
		
		tabbedPane=new JTabbedPane();
		tabbedPane.add("training", panel1);
		tabbedPane.add("predicting", panel2);
		add(tabbedPane);
	}
}
