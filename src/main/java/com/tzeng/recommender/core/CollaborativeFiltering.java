package com.tzeng.recommender.core;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import com.tzeng.recommender.core.simfunc.*;
import com.tzeng.recommender.util.*;

public class CollaborativeFiltering {
	private DataHolder dh;
	
	//constructor
	public CollaborativeFiltering(String pathname) throws FileNotFoundException{
		this.dh = new DataHolder(pathname);
	}// end constructor
	
	// private function for method StartToRecommend to compute the recommend score
	private double RecommendScore(int movieIndex, int userIndex, List<double[]> itemBased){
		double[] similarityList = new double[dh.getMovie().size()];
		for(int i = 0; i < dh.getMovie().size(); i++){
			similarityList[i] = SimilarityFunctions.CenteredCosineSimilarity(itemBased.get(movieIndex).clone(), itemBased.get(i).clone());
			System.out.println(similarityList[i]);
		}// end for
		//choose the similarity value which is larger than 0
		for (int i = 0; i < similarityList.length; i++){
			if (similarityList[i] <= 0)
				similarityList[i] = 0;
		}// end for
		similarityList[movieIndex] = 0;
		double score = 0;
		double sum = 0;
		for (int i = 0; i < similarityList.length; i++){
			score += similarityList[i]*itemBased.get(i)[userIndex];
			sum += similarityList[i];
		}// end for
		return score/sum;	
	}// end private method RecommendScore
	
	// The method used to recommend the next movie to the user.
	private Integer Recommend(String name, List<double[]> inputArray){
		System.out.print("\nGetting into Recommend function...\n");
		int user_index = dh.getUser().indexOf(name);
		List<Double> remainScore = new ArrayList<Double>();
		List<Integer> remainIndex = new ArrayList<Integer>();
		
		double maxScore = 0;
		int maxIndex = -1;
		
		// create item-based list		
		List<double[]> itemBased = new ArrayList<double[]>();
		double[] itemList = new double[dh.getUser().size()];
		
		for (int i = 0; i < dh.getMovie().size(); i++){
			itemList = new double[dh.getUser().size()];
			for (int j = 0; j < dh.getUser().size(); j++){
				itemList[j] = inputArray.get(j)[i];
			}// end nested for
			itemBased.add(itemList);
		}// end for
		
		/*
		//output the result
		for (int i = 0; i < dh.getMovie().size(); i++){
			for (int j = 0; j < dh.getUser().size(); j++){
				System.out.print(itemBased.get(i)[j] + " ");
			}// end nested for
			System.out.print("\n");
		}// end for	
		*/	
		
		//create remainIndex
		for (int i = 0; i < dh.getMovie().size(); i++){
			if (inputArray.get(user_index)[i] == 0) {
				remainIndex.add(i); 
			}
		}
		if (remainIndex.size() == 0){
			return -1;	
		}
		
		for (int i = 0; i < remainIndex.size(); i++){
			remainScore.add(RecommendScore(i, user_index, itemBased));
		}// end for
		
		//find the largest score
		for (int i = 0; i < remainIndex.size(); i++){
			if (remainScore.get(i) > maxScore) {
				maxIndex = remainIndex.get(i);
				maxScore = remainScore.get(i);
			}
		}// end for 
		//maxIndex = remainScore.indexOf(maxScore);
		
		return maxIndex;
	}// end method StartToRecommend
	
	//The method used to start the recommender system
	public void StartRecommenderSystem(String username){ 
		int movieIndex;
		int userIndex = dh.getUser().indexOf(username);
		String response = "Yes";
		System.out.print("You just start our Recommender System...");
		Scanner input = new Scanner(System.in);
		Scanner input2 = new Scanner(System.in);
		
		while(response.equals("Yes") || response.equals("yes") || response.equals("Y")){
			movieIndex = Recommend(username,dh.getInputArray());
			System.out.print("movieIndex = " + movieIndex + "\n");
			if (movieIndex == -1){
				System.out.print("\nYou've already seen all the movies in our list.\n");
				break;
			}else{
				System.out.print("We recommend you to see the movie named " + dh.getMovie().get(movieIndex) + " !\n");
			}
			System.out.print("Please input your preference (score from 1-5) of " + dh.getMovie().get(movieIndex) + " :");
			int movieScore = input.nextInt();
			// update inputArray
			dh.getInputArray().get(userIndex)[movieIndex] = movieScore;
		
			System.out.print("\n=== Update finished ===\n\n");
			System.out.print("Do you want us to recommend next movie to you ? (Yes/No) : ");
			response = input2.nextLine();
		}
		input.close();
		System.out.print("\n ===== Thank you for using our Recommender System! =====\n");	
	}
	
	//main function to test the method
	public static void main(String[] args) throws FileNotFoundException{
		CollaborativeFiltering cf = new CollaborativeFiltering(args[0]); 
		cf.StartRecommenderSystem("C");
	}// end main method
}// end class CollaborativeFiltering

