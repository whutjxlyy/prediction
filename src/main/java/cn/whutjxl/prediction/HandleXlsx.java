package cn.whutjxl.prediction;

import java.io.FileWriter;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class HandleXlsx {
	
	public static void main(String[] args) throws Exception {
		char[] features=new char[300];
		int num=0;
		double prev=0;
		double now=0;
		double diff=0;
		XSSFWorkbook workbook=new XSSFWorkbook("src/main/resources/stock.xlsx");
		Sheet sheet=workbook.getSheetAt(0);
		Iterator<Row> it=sheet.rowIterator();
		if(it.hasNext())
			it.next();
		if(it.hasNext())
			prev=it.next().getCell(4).getNumericCellValue();
		while(it.hasNext()){
			now=it.next().getCell(4).getNumericCellValue();
			diff=now-prev;
			prev=now;
			if(diff>0)
				features[num++]='U';
			else if(diff<0)
				features[num++]='F';
			else 
				features[num++]='-';
		}
		FileWriter writer=new FileWriter("src/main/resources/stock5.arff");
		writer.write("@relation stock\n\n");
		writer.write("@attribute four {U, -, F}\n");
		writer.write("@attribute three {U, -, F}\n");
		writer.write("@attribute two {U, -, F}\n");
		writer.write("@attribute one {U, -, F}\n");
		writer.write("@attribute today {U, -, F}\n");
		writer.write("@attribute tomorrow {U, -, F}\n\n");
		writer.write("@data\n");
		for(int i=5;i<num;i++)
			writer.write(features[i-5]+","+features[i-4]+","+features[i-3]+","+features[i-2]+","+features[i-1]+","+features[i]+"\n");
		writer.flush();
		writer.close();
	}
}
