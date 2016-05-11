package cn.whutjxl.prediction;

import java.io.File;
import cn.whutjxl.prediction.utils.PredictionUtils;


public class PredictionUtilsTest {

	public static void main(String[] args) {
		File sourceFile = new File("C:/Users/whutjxl/Desktop/stock.xlsx");
		File destinationFile = new File("src/main/resources/arffs/stock.arff");

		for (int step = 4; step < 40; step++) {
			PredictionUtils.getArffByExcel(sourceFile, destinationFile, step, 0, false);
			Double s = (Double) PredictionUtils.createModel(destinationFile, "weka.classifiers.trees.J48", null, false,
					"stock");
			System.out.println(s);
		}
	}
}
