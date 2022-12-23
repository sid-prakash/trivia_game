package coms309.Message;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Message {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private String senderName;
	
	@Column 
	private String receiverName;
	
	@Lob
	private String content;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date sent = new Date();
	
	public Message(String senderName, String receiverName, String content) {
		this.senderName = senderName;
		this.receiverName = receiverName;
		this.content = content;
	}
	
	public Message() {}
	
	public Long getId() {
		return id;
	}
	
	public String getSenderName() {
		return senderName;
	}
	
	public String getReceiverName() {
		return receiverName;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public Date getSent() {
		return sent;
	}
}
