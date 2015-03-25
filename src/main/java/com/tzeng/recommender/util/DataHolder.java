package com.tzeng.recommender.util;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

public class DataHolder {
	private List<String> movie;
	private List<String> user;
	private List<double[]> inputArray;
	
	//constructor
	public DataHolder(String pathname) throws FileNotFoundException{
		this.inputArray = LoadData(pathname);
	}
	
	// This method is used to read the input file.
	private List<double[]> LoadData(String filePath) throws FileNotFoundException {	
		String line;
		int colNumber;
		user = new ArrayList<String>();
		List<double[]> inputArray = new ArrayList<double[]>();
		
		BufferedReader br = new BufferedReader(new FileReader(filePath));
			
		try{		
			String[] movieLine = br.readLine().split(",");
			for (int i = 0; i < movieLine.length; i++){
				movie.add(movieLine[i]);
			}
			colNumber = movie.size();
			
			double[] rowList = new double[colNumber];

			while((line = br.readLine()) != null){
				String[] splittedLine = line.split(",");
				user.add(splittedLine[0]);
				splittedLine = (String[]) ArrayUtils.remove(splittedLine, 0);
				for (int i = 0; i < splittedLine.length; i++){
					if (splittedLine[i].equals("-1"))
						splittedLine[i] = "0";
				}
				rowList = new double[colNumber];
				for (int i = 0; i < colNumber; i++){
					rowList[i] = (double) Integer.parseInt(splittedLine[i]);
				}// end for
				inputArray.add(rowList);
			}// end while
			br.close();
			} catch (Exception e) {
			System.err.print(e);
		}// end try
		
		//output the result
		for (int i = 0; i < user.size(); i++){
			for (int j = 0; j < movie.size(); j++){
				System.out.print(inputArray.get(i)[j] + " ");
			}// end nested for
			System.out.print("\n");
		}// end for
		
		return inputArray;
	}// end method LoadFile
	
	// method used to export the data table as a csv file.
	public void ExportToCSV() throws FileNotFoundException{
			
		File RecommenderCSV = new File("/Users/tzeng/Documents/Java/Recommender.csv");
		PrintWriter outfile = new PrintWriter(RecommenderCSV);
		List<String> movieCP = movie;
		movieCP.add(0, "User/Movie");
		for (int i = 0; i < movieCP.size(); i++){
			outfile.append(movieCP.get(i));
		}// end for
		outfile.append("\n");
		for (int i = 0; i < user.size(); i++){
			outfile.write(inputArray.get(i).toString());
		}// end for
		outfile.close();	
	}// end method ExportToCSV

	// method used to extract row data. (user-based)
	public double[] userBasedList(String username){
		int userIndex = user.indexOf(username);
		return inputArray.get(userIndex);
	}// end method getUserBasedList
	
	// method used to extract column data. (item-based)
	public double[] itemBasedList(String movieName){
		double[] itemBasedList = new double[user.size()];
		int movieIndex = movie.indexOf(movieName);
		for (int i = 0; i < user.size(); i++){
			itemBasedList[i] = inputArray.get(i)[movieIndex];
		}// end for
		return itemBasedList;
	}// end method itemBasedList
	
	//print the result of the table
	public void printInputArray(){
		System.out.print("=== The current state of your input array ===");
		for (int i = 0; i < user.size(); i++){
			for (int j = 0; j < movie.size(); j++){
				System.out.print(inputArray.get(i)[j] + " ");
			}// nested for
			System.out.print("\n");
		}// end for
	}//end method printInputArray
	
	//get function
	public List<String> getMovie(){
		return movie;
	}
	public List<String> getUser(){
		return user;
	}
	public List<double[]> getInputArray(){
		return inputArray;
	}
}// end class DataHolder 

	
	