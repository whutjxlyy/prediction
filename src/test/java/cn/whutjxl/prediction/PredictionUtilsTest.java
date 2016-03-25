package cn.whutjxl.prediction;

import java.io.File;

import cn.whutjxl.prediction.utils.PredictionUtils;

public class PredictionUtilsTest {

	public static void main(String[] args) {
		File sourceFile=new File("C:/Users/whutjxl/Desktop/stock.xlsx");
		File destinationFile=new File("src/main/resources/arffs/stock.arff");
//		PredictionUtils.getArffByExcel(sourceFile, destinationFile, 7, 0);
		String s=(String) PredictionUtils.createModel(destinationFile, "weka.classifiers.trees.J48", null, true,"广电电气");
		System.out.println(s);
	}
}
