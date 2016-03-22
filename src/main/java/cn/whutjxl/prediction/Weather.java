package cn.whutjxl.prediction;

import java.io.BufferedWriter;
import java.io.FileWriter;

import java.util.Random;
import org.junit.Test;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class Weather {
	/**
	 * 10折交叉验证
	 */
	@Test
	public void fun1() {
		try {
			DataSource source = new DataSource("src/main/resources/weather.arff");
			Instances train = source.getDataSet();
			train.setClassIndex(train.numAttributes() - 1);
			J48 cls = new J48();
			String[] options1 = { "-C", "0.25" };
			cls.setOptions(options1);
			String[] options2 = { "-M", "2" };
			cls.setOptions(options2);
			cls.buildClassifier(train);
			Evaluation eval = new Evaluation(train);
			eval.crossValidateModel(cls, train, 10, new Random(1));
			System.out.println(cls.toString());
			System.out.println("===============");
			System.out.println(eval.toSummaryString());
			System.out.println(eval.toClassDetailsString());
			System.out.println(eval.toMatrixString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * train and test
	 */
	@Test
	public void fun2() {
		try {
			DataSource trainSource=new DataSource("src/main/resources/weather.arff");
			DataSource testSource=new DataSource("src/main/resources/weather2.arff");
			Instances train=trainSource.getDataSet();
			Instances test=testSource.getDataSet();
			train.setClassIndex(train.numAttributes()-1);
			test.setClassIndex(test.numAttributes()-1);
			J48 cls=new J48();
			String[] options1 = { "-C", "0.25" };
			cls.setOptions(options1);
			String[] options2 = { "-M", "2" };
			cls.setOptions(options2);
			cls.buildClassifier(train);
			Evaluation eval = new Evaluation(train);
			eval.evaluateModel(cls, test);
//			eval.crossValidateModel(cls, train, 10, new Random(1));
			System.out.println(cls.toString());
			System.out.println("===============");
			System.out.println(eval.toSummaryString());
			System.out.println(eval.toClassDetailsString());
			System.out.println(eval.toMatrixString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * prediction
	 */
	@Test
	public void fun3() {
		try {
			DataSource trainSource=new DataSource("src/main/resources/weather.arff");
			DataSource testSource=new DataSource("src/main/resources/weather2.arff");
			Instances train=trainSource.getDataSet();
			Instances test=testSource.getDataSet();
			train.setClassIndex(train.numAttributes()-1);
			test.setClassIndex(test.numAttributes()-1);
			J48 cls=new J48();
			String[] options1 = { "-C", "0.25" };
			cls.setOptions(options1);
			String[] options2 = { "-M", "2" };
			cls.setOptions(options2);
			cls.buildClassifier(train);
			Evaluation eval = new Evaluation(train);
//			eval.evaluateModel(cls, test);
			eval.crossValidateModel(cls, train, 10, new Random(1));
			System.out.println(cls.toString());
			System.out.println("===============");
			System.out.println(eval.toSummaryString());
			System.out.println(eval.toClassDetailsString());
			System.out.println(eval.toMatrixString());
			
			System.out.println("---------------------");
			System.out.println("---------------------");
			System.out.println("---------------------");
			System.out.println("---------------------");
			
			Instances prediction=new Instances(test);
			for(int i=0;i<test.numInstances();i++){
				double result = cls.classifyInstance(test.instance(i));
				
				System.out.println("<<<<<<<<<<<<");
				System.out.println(result);
				System.out.println(cls.distributionForInstance(test.instance(i))[(int) result]);
				System.out.println(">>>>>>>>>>>>");
				
				prediction.instance(i).setClassValue(result);
			}
			System.out.println(prediction);
			BufferedWriter writer=new BufferedWriter(new FileWriter("src/main/resources/weather3.arff"));
			writer.write(prediction.toString());
			writer.newLine();
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}