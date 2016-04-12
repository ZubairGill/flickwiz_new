package org.xululabs.flickwiz.service.coreclassess;

import java.io.IOException;
import java.util.Base64;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.xululabs.flickwiz.service.Loader;


public class MatDecoderAndEncoder {

	
	
	public static  String encode(Mat mat)
	{
		Loader.init();
		int row=mat.rows();
		int col=mat.cols();
		int channel=mat.channels();
		MatOfByte matofBytes=new MatOfByte(mat.reshape(1,(row*col*channel)));
		byte[] arrayOfBytes=matofBytes.toArray();
		String encodedMat=Base64.getEncoder().encodeToString(arrayOfBytes);
		return encodedMat;
	}
	
	public static Mat decode(String input,final int row,final int col,final int channel) throws IOException
	{
		//System.err.println("char lenght: " + input.length());
		//System.out.println("Row value crash "+row+ " "+ col+ " "+ channel);
		byte[] arrayOfBytes=Base64.getDecoder().decode(input);
		MatOfByte matofBytes=new MatOfByte(arrayOfBytes);
		final int mul = row*col*channel;
		//System.out.println("mul: " + mul);
		Mat outMat=new Mat(mul,1,CvType.CV_8UC1);
		//System.err.println("outRows "+outMat.rows());
		matofBytes.convertTo(outMat,CvType.CV_8UC1);
		try{
		//	System.err.println("re-shape: row: " + row + ", outMatBefore: " + outMat.rows());
		outMat=outMat.reshape(1,row);
		}catch(Exception e)
		{
			System.err.println(input);
			System.err.println("Row value crash "+row+ " "+ col+ " "+ channel);
			System.out.println("Row value out "+outMat.rows());
			System.out.println("col value out "+outMat.rows());
			System.out.println("channel value out "+outMat.rows());
		}
	//	System.out.println("Row value crash "+row+ " "+ col+ " "+ channel);
	//	System.out.println(outMat.rows());
		return outMat;
	}
	
	
	
}
