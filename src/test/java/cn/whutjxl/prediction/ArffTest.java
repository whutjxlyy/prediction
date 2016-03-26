package cn.whutjxl.prediction;

import java.io.File;
import java.util.Arrays;

import cn.whutjxl.prediction.utils.PredictionUtils;

public class ArffTest {

	public static void main(String[] args) {
//		File sourceFile=new File("C:/Users/whutjxl/Desktop/aaa.xlsx");
//		File destination1=new File("src/main/resources/stock.arff");
		File destination2=new File("src/main/resources/prediction.arff");
//		PredictionUtils.getArffByExcel(sourceFile, destination1, 12, 0,false);
//		PredictionUtils.getArffByExcel(sourceFile, destination2, 33, 0,true);
		String[] result=PredictionUtils.getPredictionResult(destination2, "stock_33_NaiveBayes");
		System.out.println(Arrays.toString(result));
	}
}
