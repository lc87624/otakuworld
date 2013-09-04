package me.gemoji.weixin.otakuworld.bean;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class WeixinTextMessage {

	private String toUser;
	private String fromUser;
	private String msgType;
	private String content;
	private String createTime;
	
	public WeixinTextMessage(String xml){
		try {
			Document doc = DocumentHelper.parseText(xml);
			this.toUser = doc.valueOf("//ToUserName");
			this.fromUser = doc.valueOf("//FromUserName");
			this.msgType = doc.valueOf("//MsgType");
			this.content = doc.valueOf("//Content");
			this.createTime = doc.valueOf("//CreateTime");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public WeixinTextMessage(String toUser, String fromUser, String content){
		this.toUser = toUser;
		this.fromUser = fromUser;
		this.msgType = "text";
		this.content = content;
		this.createTime = Long.toString(System.currentTimeMillis());
	}
	
	public String toXmlStr(){
		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding("UTF-8");
		Element root = doc.addElement("xml");
		root.addElement("ToUserName").setText(toUser);
		root.addElement("FromUserName").setText(fromUser);
		root.addElement("MsgType").setText(msgType);
		root.addElement("Content").setText(content);
		root.addElement("CreateTime").setText(createTime);
		return doc.asXML();
	}

	public String getToUser() {
		return toUser;
	}

	public void setToUser(String toUser) {
		this.toUser = toUser;
	}

	public String getFromUser() {
		return fromUser;
	}

	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
}
