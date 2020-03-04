package com.rumolog.lambda.savetos3;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

public class SaveToS3RequestHandler implements RequestHandler<ByteArrayOutputStream, Object> {

	public ByteArrayOutputStream handleRequest(ByteArrayOutputStream request, Context context) {
		
		try {
			context.getLogger().log("Vai gravar o arquivo no S3!");
			
			String artifactId = "teste";
			String filename = artifactId + "/" + "excel_example.xlsx";
			this.uploadFile(filename, new ByteArrayInputStream(request.toByteArray()));
			
			context.getLogger().log("Salvou arquivo excel: " + filename);
			
		} catch (Exception e) {
			context.getLogger().log(e.getMessage());
			e.printStackTrace();
		}
		
		return null;
	}
	
	private void uploadFile(String filename, InputStream file) {
		try {
			AmazonS3 s3client = AmazonS3ClientBuilder.standard().withRegion(Regions.fromName("sa-east-1")).build();
			
			ObjectMetadata s3ObjectMetadata = new ObjectMetadata();
			s3ObjectMetadata.setContentLength(file.available());

			s3client.putObject(new PutObjectRequest("poc-artifacts/temp", filename, file, s3ObjectMetadata));
		} catch (Exception e) {
			String error = "ERROR - Problema ao fazer upload do arquivo: " + filename;
			throw new IllegalStateException(error, e);
		}
	}
	
	
}