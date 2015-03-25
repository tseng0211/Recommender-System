package com.tzeng.recommender.core.simfunc;

public class SimilarityFunctions {
	
	// method for Centered Cosine Similarity
	public static double CenteredCosineSimilarity(double[] A, double[] B){
			
		double sumA = 0;
		double sumB = 0;
		double average_A, average_B;
		double square_sumA = 0;
		double square_sumB = 0;				
		
		for (int i = 0; i < A.length; i++){
			sumA = sumA + A[i];
			sumB = sumB + B[i];
		} // end for
		average_A = sumA / A.length;
		average_B = sumB / B.length;

		// normalize 
		for (int i = 0; i < A.length; i++){
			A[i] -= average_A;
			B[i] -= average_B;
		}// end for
	
		// dot product of A and B
		double dot = 0;
		for (int i = 0; i < A.length; i++){
			dot = dot + A[i]*B[i];
		}// end for
			
		for (int i = 0; i < A.length; i++){
			square_sumA += A[i]*A[i];
			square_sumB += B[i]*B[i];
		}// end for
			
		return Math.sqrt((dot*dot)/(square_sumA*square_sumB));	
	 }// end method CenteredCosineSimilaritysineSimilarity

}// end class SimilarityFunctions
