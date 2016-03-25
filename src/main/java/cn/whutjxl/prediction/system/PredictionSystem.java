package cn.whutjxl.prediction.system;

import java.awt.EventQueue;

import javax.swing.JFrame;

public class PredictionSystem {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			
			public void run() {
				PredictionFrame frame=new PredictionFrame("股票短期趋势预测系统");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
			}
		});
	}
}
