package me.gemoji.weixin.otakuworld.servlet;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.*;
import me.chanjar.weixin.mp.bean.*;
import org.apache.commons.lang3.StringUtils;

public class WeixinServlet extends HttpServlet{

	private static final String WEIXIN_TOKEN = "otakuworld";
	private static final String WEIXIN_APP_ID = "wxaa1abd389bab6ff1";
	private static final String WEIXIN_APP_SECRET = "d4624c36b6795d1d99dcf0547af5443d";

	protected WxMpInMemoryConfigStorage wxMpConfigStorage;
	protected WxMpService wxMpService;
	protected WxMpMessageRouter wxMpMessageRouter;

	@Override public void init() throws ServletException {
		super.init();

		wxMpConfigStorage = new WxMpInMemoryConfigStorage();
		wxMpConfigStorage.setAppId(WEIXIN_APP_ID); // 设置微信公众号的appid
		wxMpConfigStorage.setSecret(WEIXIN_APP_SECRET); // 设置微信公众号的app corpSecret
		wxMpConfigStorage.setToken(WEIXIN_TOKEN); // 设置微信公众号的token
		//wxMpConfigStorage.setAesKey("..."); // 设置微信公众号的EncodingAESKey

		wxMpService = new WxMpServiceImpl();
		wxMpService.setWxMpConfigStorage(wxMpConfigStorage);

		WxMpMessageHandler cosHandler = new WxMpMessageHandler() {
			@Override
			public WxMpXmlOutMessage handle(WxMpXmlMessage wxMpXmlMessage, Map<String, Object> map,
											WxMpService wxMpService, WxSessionManager wxSessionManager) throws WxErrorException {

                WxMpXmlOutNewsMessage.Item article = new WxMpXmlOutNewsMessage.Item();
                article.setTitle("Akali(英雄联盟) VS Sona(英雄联盟)");
                article.setDescription("新鲜出炉的cos美图两张，各位看官赶紧投票吧~");
                article.setPicUrl("http://ww2.sinaimg.cn/large/dd7aac41jw1e7j3w325u5j20kk0d5gqo.jpg");
                article.setUrl("http://120.25.64.81/cos.html");


				return WxMpXmlOutMessage.NEWS()
                            .fromUser(wxMpXmlMessage.getToUserName())
                            .toUser(wxMpXmlMessage.getFromUserName())
                            .addArticle(article)
                            .build();
			}
		};

        WxMpMessageHandler defaultHandler = new WxMpMessageHandler() {
            @Override
            public WxMpXmlOutMessage handle(WxMpXmlMessage wxMpXmlMessage, Map<String, Object> map,
                                            WxMpService wxMpService, WxSessionManager wxSessionManager) throws WxErrorException {

                String content = "小宅我是复读机星人，貌似你刚说的是：“" + wxMpXmlMessage.getContent() + "”吧？那是个什么东西？有节操么？";

                return WxMpXmlOutMessage.TEXT()
                        .fromUser(wxMpXmlMessage.getToUserName())
                        .toUser(wxMpXmlMessage.getFromUserName())
                        .content(content)
                        .build();
            }
        };

		wxMpMessageRouter = new WxMpMessageRouter(wxMpService);

		wxMpMessageRouter
				.rule()
				.async(false)
				.content("cos") // 拦截内容为“cos”的消息
				.handler(cosHandler)
				.end();

        wxMpMessageRouter
                .rule()
                .handler(defaultHandler)
                .end();

	}


	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		resp.setContentType("text/html; charset=UTF-8");
		resp.setCharacterEncoding("UTF-8");
		PrintWriter out = resp.getWriter();

		String signature = req.getParameter("signature");
		String timestamp = req.getParameter("timestamp");
		String nonce = req.getParameter("nonce");

		if(!wxMpService.checkSignature(timestamp, nonce, signature)){
			out.print("微信君，貌似你打开的方式不对呦。。。");
			return;
		}

		String echostr = req.getParameter("echostr");
		if(StringUtils.isNotBlank(echostr)){
			out.print(echostr);
			return;
		}

		String encryptType = StringUtils.isBlank(req.getParameter("encrypt_type")) ?
				"raw" :
				req.getParameter("encrypt_type");

		if ("raw".equals(encryptType)) {
			// 明文传输的消息
			WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(req.getInputStream());
			WxMpXmlOutMessage outMessage = wxMpMessageRouter.route(inMessage);
			resp.getWriter().write(outMessage.toXml());
			return;
		}

		if ("aes".equals(encryptType)) {
			// 是aes加密的消息
			String msgSignature = req.getParameter("msg_signature");
			WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(req.getInputStream(), wxMpConfigStorage, timestamp, nonce, msgSignature);
			WxMpXmlOutMessage outMessage = wxMpMessageRouter.route(inMessage);
			resp.getWriter().write(outMessage.toEncryptedXml(wxMpConfigStorage));
			return;
		}

		resp.getWriter().println("不可识别的加密类型");
		return;

	}

}
