package cn.whutjxl.prediction;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.xmlbeans.impl.common.ReaderInputStream;
import org.junit.Test;

import cn.whutjxl.prediction.utils.PredUtils;
import cn.whutjxl.prediction.utils.PredictionUtils;
import weka.associations.Apriori;
import weka.associations.Associator;
import weka.associations.AssociatorEvaluation;
import weka.associations.ItemSet;
import weka.associations.PredictiveApriori;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.FastVector;
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

	/**
	 * 只有训练集 包含属性过滤
	 */
	@Test
	public void fun1() {
		/**
		 * C:/Users/whutjxl/Desktop/stock.xlsx
		 */
		String classifierName = "weka.classifiers.trees.J48";
		List<Character> features = PredUtils.getFeaturesByExcel(new File("C:/Users/whutjxl/Desktop/stock.xlsx"));
		System.out.println(features);
		List<Integer> indexs = new ArrayList<Integer>();
		Map<Integer, Double> map = new HashMap<Integer, Double>();

		for (int step = 4; step < 8; step++) {

			/**
			 * 不包含属性过滤
			 */
/*			for(int i=1;i<step;i++){
				indexs.add(i);
			}
			PredUtils.createArff(features, new File("src/main/resources/stock.arff"), step, indexs);
			Double result = (Double) PredUtils.trainModel(new File("src/main/resources/stock.arff"), classifierName,
					null, false, "stock");
			map.put(step, result);
			indexs.clear();*/

			for (int i = 1; i < step; i++) {
				indexs.add(i);
				PredUtils.createArff(features, new File("src/main/resources/stock.arff"), step, indexs);
				Double result = (Double) PredUtils.trainModel(new File("src/main/resources/stock.arff"), classifierName,
						null, false, "stock");
				map.put(i, result);
				indexs.clear();
			}
			for (Integer key : map.keySet()) {
				if (map.get(key) > 55)
					indexs.add(key);
			}
			System.out.println(map);
			PredUtils.createArff(features, new File("src/main/resources/stock.arff"), step, indexs);
			Double s = (Double) PredUtils.trainModel(new File("src/main/resources/stock.arff"), classifierName, null,
					false, "stock");
			System.out.println(s);
			System.out.println(indexs);
			map.clear();
		}
		
	/*	for (Integer integer : map.keySet()) {
			System.out.println(integer+"------------------>");
			System.out.println(map.get(integer));
		}*/
	}

	/**
	 * 训练集和测试集 不包含属性过滤
	 */
	@Test
	public void fun2() {
		String classifierName = "weka.classifiers.trees.J48";
		List<Character> features = PredUtils.getFeaturesByExcel(new File("C:/Users/whutjxl/Desktop/stock.xlsx"));
		System.out.println(features);
		List<Character> testList = new ArrayList<Character>();
		List<Integer> indexs = new ArrayList<Integer>();
		int num = features.size();
		for (int i = num * 9 / 10; i < num; i++) {
			testList.add(features.get(i));
		}
		for (int j = 0; j < testList.size(); j++) {
			features.remove(features.size() - 1);
		}
		testList.add('?');
		System.out.println(features);
		System.out.println(testList);
		for (int step = 4; step < 17; step++) {
			indexs.clear();
			for (int j = 1; j < step; j++)
				indexs.add(j);
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
				eval.evaluateModel(cls, train);
				// System.out.println(cls.toString());
				// System.out.println("===============");
				// System.out.println(eval.toSummaryString());
				// System.out.println(eval.toClassDetailsString());
				// System.out.println(eval.toMatrixString());
				System.out.println(step+"------>"+eval.pctCorrect());
			/*	int numIns=test.numInstances();
				int correct=0;
				for(int k=0;k<numIns;k++){
					if(cls.classifyInstance(test.instance(k))==test.instance(k).classValue()){
						correct++;
					}
				}
				System.out.println(step+"---->"+1.0*correct/numIns);*/
				/**
				 * 模型预测
				 */
				double result=cls.classifyInstance(test.instance(test.numInstances()-1));
				System.out.println(result);
				double[] pctcorr=cls.distributionForInstance(test.instance(test.numInstances()-1));
				System.out.println(Arrays.toString(pctcorr));
				System.out.println("============");
				
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	/**
	 * 训练集和测试集 包含属性过滤
	 */
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
			for (int j = 0; j < testList.size(); j++) {
				features.remove(features.size() - 1);
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

	/**
	 * 关联规则
	 */
	@Test
	public void fun4() {

		List<Character> features = PredUtils.getFeaturesByExcel(new File("C:/Users/whutjxl/Desktop/stock.xlsx"));
		List<Integer> list = new ArrayList<Integer>();
		int step = 10;
		for (int i = 1; i < step; i++)
			list.add(i);
		PredUtils.createArff(features, new File("src/main/resources/stock.arff"), step, list);
		DataSource source = null;
		try {
			source = new DataSource("src/main/resources/stock.arff");
			Instances train = source.getDataSet();
			train.setClassIndex(train.numAttributes() - 1);
			PredictiveApriori apriori = new PredictiveApriori();
			apriori.buildAssociations(train);
			System.out.println(apriori);
			FastVector[] rules = apriori.getAllTheRules();
			System.out.println("*************");
			for(int i=0;i<rules[0].size();i++){
				System.out.println(((ItemSet)rules[0].elementAt(i)).toString());
				System.out.println(((ItemSet)rules[1].elementAt(i)).toString());
				System.out.println(((Double)rules[2].elementAt(i)).doubleValue());
				System.out.println("------------------");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
