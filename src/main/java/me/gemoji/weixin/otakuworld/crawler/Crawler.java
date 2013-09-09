package me.gemoji.weixin.otakuworld.crawler;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler {
	
	private static final String USER_AGENT = 
			"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.62 Safari/537.36";
	private static final String PICTRUE_FOLDER_PATH = "/Users/gemoji/Pictures/cos/banciyuan.com/";
	
	private boolean onOff = false;
	
	private static final Crawler crawler = new Crawler();
	
	private Crawler(){}
	
	public static Crawler getInstance(){
		return crawler;
	}
	
	public boolean crawl(){
		if(!onOff) return false;
		return true;
	}
	
	public void off(){
		this.onOff = false;
	}
	
	public void on()
	{
		this.onOff = true;
	}

	public static void main(String[] args) {
		
		String url = "http://banciyuan.com/coser/detail/4491/15300";
		try {
			Document doc = Jsoup.connect(url).userAgent(USER_AGENT).get();
	 
			Elements images = doc.select("img.detail_std");
			for (Element image : images) {
	 
				String pictrueUrl = fixPictrueUrl(image.attr("src"));
				String pictureName = getPictrueName(pictrueUrl);
				System.out.println(pictrueUrl);
				System.out.println(pictureName);
				String filePath = PICTRUE_FOLDER_PATH + pictureName;
				FileUtils.copyURLToFile(new URL(pictrueUrl), new File(filePath), 1000, 5000);
	 
			}
	 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String fixPictrueUrl(String origin){
		int start = origin.indexOf("Public/Upload");
		return "http://banciyuan.com/" + origin.substring(start);
	}
	
	private static String getPictrueName(String pictrueUrl){
		int start = pictrueUrl.lastIndexOf("/") + 1;
		int end = pictrueUrl.indexOf(".690.jpg");
		return pictrueUrl.substring(start, end) + ".jpg";
	}
}
