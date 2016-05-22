package cn.whutjxl.prediction.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ConverterUtils.DataSource;

public class PredUtils {

	public static List<Character> getFeaturesByExcel(File sourceFile) {
		List<Character> features = new ArrayList<Character>();
		Workbook workBook = null;
		double previous = 0.0;
		double current = 0.0;
		double sub = 0.0;
		try {
			String fileName = sourceFile.getName();
			if (fileName.endsWith(".xls")) {
				workBook = new HSSFWorkbook(new FileInputStream(sourceFile));
			} else if (fileName.endsWith(".xlsx")) {
				workBook = new XSSFWorkbook(new FileInputStream(sourceFile));
			}
			Sheet sheet = workBook.getSheetAt(0);
			Iterator<Row> it = sheet.rowIterator();
			if (it.hasNext())
				it.next();
			if (it.hasNext())
				previous = it.next().getCell(1).getNumericCellValue();
			while (it.hasNext()) {
				current = it.next().getCell(1).getNumericCellValue();
				sub = current - previous;
				previous = current;
				if (sub > 0) {
					features.add('U');
				} else if (sub < 0) {
					features.add('F');
				} else {
					features.add('-');
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (workBook != null) {
				try {
					workBook.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return features;
	}

	public static void createArff(List<Character> features, File destinationFile, int step) {
		FileWriter writer = null;
		StringBuffer sb = null;
		try {
			writer = new FileWriter(destinationFile);
			String fileName = destinationFile.getName().split("\\.")[0];
			writer.write("@relation " + fileName + "\n\n");
			for (int i = step - 1; i >= 1; i--) {
				writer.write("@attribute day" + i + " {U, -, F}\n");
			}
			writer.write("@attribute day0 {U, -, F}\n\n");
			writer.write("@data\n");
			int num = features.size();
			if (num >= step) {
				for (int j = step - 1; j < num; j++) {
					sb = new StringBuffer();
					for (int k = step - 1; k >= 1; k--) {
						sb.append(features.get(j - k) + ",");
					}
					sb.append(features.get(j) + "\n");
					writer.write(sb.toString());
					writer.flush();
				}
			} else {
				sb = new StringBuffer();
				for (int j = step - 1; j >= num; j--) {
					sb.append("?,");
				}
				/*
				 * for (int k = num - 1; k >= 1; k--) {
				 * sb.append(features.get(num - 1 - k) + ","); }
				 */
				for (int k = 0; k < num - 1; k++) {
					sb.append(features.get(k) + ",");
				}
				sb.append(features.get(num - 1) + "\n");
				writer.write(sb.toString());
				writer.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static Object trainModel(File sourceFile, String classifierName, boolean saveModel, String originalName) {
		String result = "";
		Double pctCorrect = 0.0;
		try {
			DataSource source = new DataSource(new FileInputStream(sourceFile));
			Instances train = source.getDataSet();
			train.setClassIndex(train.numAttributes() - 1);
			Classifier cls = (Classifier) Class.forName(classifierName).newInstance();
			cls.buildClassifier(train);
			Evaluation eval = new Evaluation(train);
			eval.crossValidateModel(cls, train, 10, new Random(1));
			String name = cls.getClass().getSimpleName();
			if ("J48".equals(name) || "LADTree".equals(name)) {
				result += cls.toString();
			}
			result += eval.toSummaryString();
			pctCorrect = Double.parseDouble(String.format("%.2f", eval.pctCorrect()));
			if (saveModel) {
				String modelName = originalName + "_" + name + "_" + (int) Math.floor(pctCorrect) + ".model";
				String modelPath = "src/main/resources/models/";
				SerializationHelper.write(modelPath + modelName, cls);
				return result;
			} else {
				return pctCorrect;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String[] getPredictionResult(File sourceFile, String modelName) {
		try {
			DataSource dataSource = new DataSource(new FileInputStream(sourceFile));
			Instances source = dataSource.getDataSet();
			source.setClassIndex(source.numAttributes() - 1);
			Instances prediction = new Instances(source);
			String modelPath = "src/main/resources/models/" + modelName + ".model";
			Classifier cls = (Classifier) SerializationHelper.read(modelPath);
			int numInst = source.numInstances();
			String[] results = new String[2];
			double result = cls.classifyInstance(source.instance(numInst - 1));
			prediction.instance(numInst - 1).setClassValue(result);
			double pctCorrect = cls.distributionForInstance(source.instance(numInst - 1))[(int) result];
			results[0] = prediction.instance(numInst - 1).stringValue(source.numAttributes() - 1);
			results[1] = String.format("%.2f", pctCorrect*100);
			return results;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
