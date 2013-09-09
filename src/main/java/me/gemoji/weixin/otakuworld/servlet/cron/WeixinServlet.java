package me.gemoji.weixin.otakuworld.servlet.cron;


import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.gemoji.weixin.otakuworld.bean.WeixinNewsArticle;
import me.gemoji.weixin.otakuworld.bean.WeixinNewsMessage;
import me.gemoji.weixin.otakuworld.bean.WeixinTextMessage;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

@SuppressWarnings("serial")
public class WeixinServlet extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		resp.setContentType("text/html; charset=UTF-8");
		resp.setCharacterEncoding("UTF-8");
		PrintWriter out = resp.getWriter();
		
		String signatrue = req.getParameter("signature");
		String timestamp = req.getParameter("timestamp");
		String nonce = req.getParameter("nonce");
		String echostr = req.getParameter("echostr");
		
		if(signatrue == null || timestamp == null || 
				nonce == null || echostr == null){
			out.print("微信君，貌似你打开的方式不对呦。。。");
			return;
		}
		
		String token = "otakuworld";
		List<String> list = Arrays.asList(new String[]{token, timestamp, nonce});
		Collections.sort(list);
		
		String raw = StringUtils.join(list, "");
		String encrypt = DigestUtils.sha1Hex(raw);
		
		if(encrypt.equals(signatrue)){
			out.print(echostr);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		resp.setContentType("text/xml; charset=UTF-8");
		resp.setCharacterEncoding("UTF-8");
		PrintWriter out = resp.getWriter();
		
		String input = IOUtils.toString(req.getInputStream(), "utf8");
		WeixinTextMessage msgIn = new WeixinTextMessage(input);
		
		String content = "小宅我是复读机星人，貌似你刚说的是：“" + msgIn.getContent() + "”吧？那是个什么东西？有节操么？";
		
		/*if("cos".equals(msgIn.getContent())){
			String img1 = "http://tiny.cc/3qqn1w";
			String img2 = "http://tiny.cc/xsqn1w";
			content = img1 + "\n" + img2;
			WeixinTextMessage msgOut = new WeixinTextMessage(msgIn.getFromUser(), msgIn.getToUser(), content);
			
			out.print(msgOut.toXmlStr());
		}*/
		
		if("cos".equals(msgIn.getContent())){
			WeixinNewsMessage news = new WeixinNewsMessage(msgIn.getFromUser(), msgIn.getToUser());
			String img = "http://tiny.cc/3qqn1w";
			String title = "Akali(英雄联盟) VS Sona(英雄联盟)";
			String description = "新鲜出炉的cos美图两张，各位看官赶紧投票吧~";
			WeixinNewsArticle article = new WeixinNewsArticle(title, description, img, "http://otakuworld.duapp.com/cos.html");
			news.addArticle(article);
			content = news.toXmlStr();
			
			out.print(news.toXmlStr());
		}else{
			WeixinTextMessage msgOut = new WeixinTextMessage(msgIn.getFromUser(), msgIn.getToUser(), content);
			out.print(msgOut.toXmlStr());
		}
	}
	
	public static void main(String[] args) throws IOException {
		String input = FileUtils.readFileToString(new File("C://test.txt"), "UTF-8");
		System.out.println(input);
		System.out.println(new WeixinTextMessage(input).toXmlStr());
	}
}
