package db.training.easy.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import db.training.osb.util.ConfigResources;

public class SMTP {

	private DataOutputStream os = null;

	private BufferedReader is = null;

	private String sRt = "";

	private String smtpServer = ConfigResources.getInstance().getSmtpServer();

	private int serverPort = Integer.parseInt(ConfigResources.getInstance().getSmtpPort());

	private String fromAddress = ConfigResources.getInstance().getMailFromAdress();

	private final void writeRead(boolean isReadAnswer, String serverReplyCode, String message)
	    throws IOException, Exception {
		if (message != null && message.length() > 0) {
			sRt += message;
			os.writeBytes(message);
		}
		if (isReadAnswer) {
			String sRd = is.readLine() + "\n";
			sRt += sRd;
			if (serverReplyCode != null && serverReplyCode.length() > 0
			    && !sRd.startsWith(serverReplyCode))
				throw new Exception(sRt);
		}
	}

	public synchronized final String sendEmail(String toAddress, String fromAddress,
	    String subject, String messageText) throws IOException, Exception {
		return sendEmail(this.smtpServer, toAddress, null, fromAddress, subject, messageText);
	}

	public synchronized final String sendEmail(String smtpServer, String toAddress,
	    String ccAddress, String fromAddress, String subject, String messageText)
	    throws IOException, Exception {
		sRt = "";
		if (fromAddress != null)
			this.fromAddress = fromAddress;
		if (smtpServer == null
		    || smtpServer.length() <= 0
		    || this.fromAddress == null
		    || this.fromAddress.length() <= 0
		    || toAddress == null
		    || toAddress.length() <= 0
		    || ((subject == null || subject.length() <= 0) && (messageText == null || messageText
		        .length() <= 0)))
			throw new Exception("Invalid Parameters for sendEmail().");
		Socket so = new Socket(smtpServer, serverPort);
		os = new DataOutputStream(so.getOutputStream());
		is = new BufferedReader(new InputStreamReader(so.getInputStream()));
		so.setSoTimeout(10000);
		writeRead(true, "220", null);
		writeRead(true, "250", "HELO " + smtpServer + "\n");
		writeRead(true, "250", "RSET\n");
		writeRead(true, "250", "MAIL FROM: <" + this.fromAddress + ">\n");
		writeRead(true, "250", "RCPT TO: <" + toAddress + ">\n");
		if (ccAddress != null)
			writeRead(true, "250", "RCPT TO: <" + ccAddress + ">\n");
		writeRead(true, "354", "DATA\n");
		writeRead(false, null, "To:" + toAddress + "\n");
		if (ccAddress != null)
			writeRead(false, null, "Cc:" + ccAddress + "\n");
		writeRead(false, null, "From:" + this.fromAddress + "\n");
		writeRead(false, null, "Subject: " + subject + "\n");
		writeRead(false, null, "Mime-Version: 1.0\n");
		writeRead(false, null, "Content-Type: text/plain;charset=iso-8859-1\n");
		writeRead(false, null, "Content-Transfer-Encoding: quoted-printable\n\n");
		writeRead(false, null, messageText + "\n");
		writeRead(true, "250", ".\n");
		writeRead(true, "221", "QUIT\n");
		is.close();
		os.close();
		so.close();
		is = null;
		os = null;
		return sRt;
	}
}
