package ntut.csie.ezScrum.issue.mail.service.core;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class GmailSender {
	private final String host_ = "smtp.gmail.com";
	private final int port_ = 465;
	private String username_;
	private String password_;
	private Properties props_ = new Properties();
	private Session session_;
	
	public GmailSender(String username, String password) {
		username_ = username;
		password_ = password;
		System.setProperty("mail.mime.charset", "big5");
		props_.put("mail.smtp.host", host_);
		props_.put("mail.smtp.socketFactory.port", port_);
		props_.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props_.put("mail.smtp.auth", "true");
		props_.put("mail.smtp.port", port_);
		props_.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		
		session_ = Session.getInstance(props_, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username_, password_);
			}
		});
	}
	public String send(String address, String ccAddresses, String subject, String text, byte[] data) {
		Message message = new MimeMessage(session_);
		MimeMultipart multipart = new MimeMultipart();
		MimeBodyPart messageBody = new MimeBodyPart();
		try {
			message.setFrom(new InternetAddress(username_));
			return "寄送email結束.";
		} catch (MessagingException e) {
			String errorMessage = e.getMessage().toString();
			if (errorMessage.contains("https://accounts.google.com/signin/continue?")) {
				return "請開啟    安全性較低的應用程式存取權限  或使用設定google兩段式登入";
			} else if (errorMessage.contains("Username and Password not accepted")) {
				return "帳號密碼不正確";
			} else if (errorMessage.contains("Invalid Addresses")) {
				return "送信位址格式不正確";
			} else if (errorMessage.contains("Could not connect to SMTP host: smtp.gmail.com")) {
				return "無法連線到SMTP host，請檢察防火牆或Proxy設定";
			} else if (errorMessage.contains("Unknown SMTP host: smtp.gmail.com")) {
				return "Unknown SMTP host: smtp.gmail.com，請檢察網路連線";
			} else {
				return e.getMessage().toString();
			}
		}
	}
	
}
