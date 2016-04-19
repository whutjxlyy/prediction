package cn.whutjxl.prediction;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import cn.whutjxl.prediction.utils.PredUtils;
import cn.whutjxl.prediction.utils.PredictionUtils;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

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

	@Test
	public void fun1() {
		/**
		 * C:/Users/whutjxl/Desktop/stock.xlsx
		 */
		String classifierName = "weka.classifiers.trees.J48";
		List<Character> features = PredUtils.getFeaturesByExcel(new File("C:/Users/whutjxl/Desktop/stock.xlsx"));
		System.out.println(features);
		List<Integer> indexs = new ArrayList<Integer>();

		for (int step = 4; step < 14; step++) {

			Map<Integer, Double> map = new HashMap<Integer, Double>();
			for (int i = 1; i < step; i++) {
				indexs.add(i);
				PredUtils.createArff(features, new File("src/main/resources/stock.arff"), step, indexs);
				Double result = (Double) PredUtils.trainModel(new File("src/main/resources/stock.arff"), classifierName,
						null, false, "stock");
				map.put(i, result);
				indexs.clear();
			}
			for (Integer key : map.keySet()) {
				if (map.get(key) > 50)
					indexs.add(key);
			}
			System.out.println(map);
			PredUtils.createArff(features, new File("src/main/resources/stock.arff"), step, indexs);
			Double s = (Double) PredUtils.trainModel(new File("src/main/resources/stock.arff"), classifierName, null,
					false, "stock");
			System.out.println(s);
			System.out.println(indexs);
		}
	}

	@Test
	public void fun2() {
		String classifierName = "weka.classifiers.trees.J48";
		for (int step = 8; step < 9; step++) {
			PredictionUtils.getArffByExcel(new File("C:/Users/whutjxl/Desktop/stock.xlsx"),
					new File("src/main/resources/stock.arff"), step, 0, false);
			PredictionUtils.getArffByExcel(new File("C:/Users/whutjxl/Desktop/test.xlsx"),
					new File("src/main/resources/test.arff"), step, 0, false);
			try {
				DataSource trainSource = new DataSource("src/main/resources/stock.arff");
				DataSource testSource = new DataSource("src/main/resources/test.arff");
				Instances train = trainSource.getDataSet();
				Instances test = testSource.getDataSet();
				train.setClassIndex(train.numAttributes() - 1);
				test.setClassIndex(test.numAttributes() - 1);
				Classifier cls = (Classifier) Class.forName(classifierName).newInstance();
				cls.buildClassifier(train);
				Evaluation eval = new Evaluation(train);
				eval.evaluateModel(cls, test);
				// System.out.println(cls.toString());
				// System.out.println("===============");
				// System.out.println(eval.toSummaryString());
				// System.out.println(eval.toClassDetailsString());
				// System.out.println(eval.toMatrixString());
				System.out.println(eval.pctCorrect());
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	@Test
	public void fun3() {
		String classifierName = "weka.classifiers.trees.J48";
		List<Character> features = PredUtils.getFeaturesByExcel(new File("C:/Users/whutjxl/Desktop/stock.xlsx"));
		List<Character> testList = new ArrayList<Character>();
		for (int step = 4; step < 14; step++) {
			int num = features.size();
			for (int i = num * 9 / 10; i < num; i++) {
				testList.add(features.get(i));
			}
			List<Integer> indexs = new ArrayList<Integer>();
			Map<Integer, Double> map = new HashMap<Integer, Double>();
			indexs.clear();
			for (int i = 1; i < step; i++) {
				indexs.add(i);
				PredUtils.createArff(features, new File("src/main/resources/stock.arff"), step, indexs);
				PredUtils.createArff(testList, new File("src/main/resources/test.arff"), step, indexs);
				try {
					DataSource trainSource = new DataSource("src/main/resources/stock.arff");
					DataSource testSource = new DataSource("src/main/resources/test.arff");
					Instances train = trainSource.getDataSet();
					Instances test = testSource.getDataSet();
					train.setClassIndex(train.numAttributes() - 1);
					test.setClassIndex(test.numAttributes() - 1);
					Classifier cls = (Classifier) Class.forName(classifierName).newInstance();
					cls.buildClassifier(train);
					Evaluation eval = new Evaluation(train);
					eval.evaluateModel(cls, test);
					map.put(i, eval.pctCorrect());
				} catch (Exception e) {
					e.printStackTrace();
				}
				indexs.clear();
			}
			System.out.println(map);
			for (Integer key : map.keySet()) {
				if (map.get(key) > 55)
					indexs.add(key);
			}
			System.out.println(indexs);
			PredUtils.createArff(features, new File("src/main/resources/stock.arff"), step, indexs);
			PredUtils.createArff(testList, new File("src/main/resources/test.arff"), step, indexs);
			try {
				DataSource trainSource = new DataSource("src/main/resources/stock.arff");
				DataSource testSource = new DataSource("src/main/resources/test.arff");
				Instances train = trainSource.getDataSet();
				Instances test = testSource.getDataSet();
				train.setClassIndex(train.numAttributes() - 1);
				test.setClassIndex(test.numAttributes() - 1);
				Classifier cls = (Classifier) Class.forName(classifierName).newInstance();
				cls.buildClassifier(train);
				Evaluation eval = new Evaluation(train);
				eval.evaluateModel(cls, test);
				System.out.println(eval.pctCorrect());
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

}
