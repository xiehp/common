package com.tietuku.entity.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClients;

import com.tietuku.entity.token.Token;
import com.tietuku.entity.util.PathConfig;

public class PostImage {

	public static String doUpload(File file) {
		String token = PathConfig.getProperty("tie.tu.ku.token");
		return doUpload(file, token);
	}

	/**
	 * 提交图片给图库
	 * 
	 * @param image
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static String doUpload(File file, String token) {
		// 贴图库数据加密请求
		String url = PathConfig.getProperty("tie.tu.ku.post.api");

		HttpClient httpclient = HttpClients.createDefault();
		HttpPost httppost = new HttpPost(url);

		FileBody bin = new FileBody(file);
		MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create(); // 关键
		multipartEntityBuilder.addPart("file", bin);
		multipartEntityBuilder.addPart("Token", new StringBody(token, ContentType.APPLICATION_FORM_URLENCODED));

		try {
			httppost.setEntity(multipartEntityBuilder.build());
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();

			StringBuffer buffer = new StringBuffer();
			if (entity != null) {
				// start 读取整个页面内容
				InputStream is = entity.getContent();
				BufferedReader in = new BufferedReader(new InputStreamReader(is));

				String line = "";
				while ((line = in.readLine()) != null) {
					buffer.append(line);
				}
			}
			return buffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static void main(String[] args) throws ClientProtocolException, IOException {
		String token = Token.createToken(new Date().getTime() + 3600, 1340, "{\"height\":\"h\",\"width\":\"w\",\"s_url\":\"url\"}");
		token = PathConfig.getProperty("tie.tu.ku.token");
		String result = PostImage.doUpload(new File("D:/work/temp/bbb2/300013.jpg"), token);
		System.out.println(result);
	}

}
