package me.gemoji.weixin.otakuworld.servlet.cron;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.gemoji.weixin.otakuworld.crawler.Crawler;

import org.apache.commons.lang3.StringUtils;

@SuppressWarnings("serial")
public class CrawlerCronServlet extends HttpServlet{
	
	private static final String START_ACTION = "start";
	private static final String STOP_ACTION = "stop";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		resp.setContentType("text/html; charset=UTF-8");
		resp.setCharacterEncoding("UTF-8");
		PrintWriter out = resp.getWriter();
		
		Crawler crawler = Crawler.getInstance();
		
		String action = req.getParameter("action");
		if(StringUtils.equals(action, START_ACTION)){
			crawler.on();
			out.print("crawler start success!");
		}else if(StringUtils.equals(action, STOP_ACTION)){
			crawler.off();
			out.print("crawler stop success!");
		}else{
			out.print("crawling...");
		}
	}
	
}
