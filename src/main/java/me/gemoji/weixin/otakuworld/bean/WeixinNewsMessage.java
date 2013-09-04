package me.gemoji.weixin.otakuworld.bean;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class WeixinNewsMessage {

	private String toUser;
	private String fromUser;
	private String msgType;
	private List<WeixinNewsArticle> articles = new ArrayList<WeixinNewsArticle>();
	private String createTime;
	
	public WeixinNewsMessage(String toUser, String fromUser){
		this.toUser = toUser;
		this.fromUser = fromUser;
		this.msgType = "news";
		this.createTime = Long.toString(System.currentTimeMillis());
	}
	
	public void addArticle(WeixinNewsArticle article){
		articles.add(article);
	}
	
	public String toXmlStr(){
		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding("UTF-8");
		Element root = doc.addElement("xml");
		root.addElement("ToUserName").setText(toUser);
		root.addElement("FromUserName").setText(fromUser);
		root.addElement("MsgType").setText(msgType);
		root.addElement("CreateTime").setText(createTime);
		root.addElement("ArticleCount").setText(Integer.toString(articles.size()));
		Element articlesNode = root.addElement("Articles");
		for (WeixinNewsArticle article : articles) {
			addArticleElement(articlesNode, article);
		}
		return doc.asXML();
	}
	
	private void addArticleElement(Element articlesNode, WeixinNewsArticle article){
		Element item = articlesNode.addElement("item");
		item.addElement("Title").setText(article.getTitle());
		item.addElement("Description").setText(article.getDescription());
		item.addElement("PicUrl").setText(article.getPicUrl());
		item.addElement("Url").setText(article.getUrl());
	}
	
	public static void main(String[] args) {
		WeixinNewsMessage news = new WeixinNewsMessage("xyr.1987", "lc87624");
		String picUrl = "http://hdn.xnimg.cn/photos/hdn121/20130404/1410/original_Imr4_ec63000156bd113e.jpg";
		news.addArticle(new WeixinNewsArticle("Love", "I love you", picUrl, "http://www.gemoji.me"));
		news.addArticle(new WeixinNewsArticle("Love", "I love you", picUrl, "http://www.gemoji.me"));
		System.out.println(news.toXmlStr());
	}
}
