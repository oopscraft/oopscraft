package net.oopscraft.application.board;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import net.oopscraft.application.core.jpa.SystemEntity;

@Entity
@Table(name = "APP_ATCL_RPLY_INFO")
@IdClass(ArticleReply.Pk.class)
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ArticleReply extends SystemEntity {
	
	/**
	 * ArticleReply.Pk
	 */
	public static class Pk implements Serializable {
		private static final long serialVersionUID = -2201892754104834161L;
		String articleId;
		String id;
		public Pk() {}
		public Pk(String articleId, String id) {
			this.articleId = articleId;
			this.id = id;
		}
		public String getArticleId() {
			return articleId;
		}
		public void setArticleId(String articleId) {
			this.articleId = articleId;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((articleId == null) ? 0 : articleId.hashCode());
			result = prime * result + ((id == null) ? 0 : id.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Pk other = (Pk) obj;
			if (articleId == null) {
				if (other.articleId != null)
					return false;
			} else if (!articleId.equals(other.articleId))
				return false;
			if (id == null) {
				if (other.id != null)
					return false;
			} else if (!id.equals(other.id))
				return false;
			return true;
		}
	}
	
	@Id
	@Column(name = "ATCL_ID", length = 32)
	String articleId;
	
	@Id
	@Column(name = "RPLY_ID", length = 32)
	String id;
	
	@Column(name = "UPER_RPLY_ID", length = 32)
	String upperId;

	@Column(name = "RPLY_DATE")
	Date date;
	
	@Column(name="RPLY_ATHR", length = 1024)
	String author;
	
	@Column(name="RPLY_PASS", length = 1024)
	String password;
	
	@Column(name = "RPLY_CNTS", length = Integer.MAX_VALUE)
	String contents;

	@Column(name = "USER_ID", length = 32)
	String userId;
	
	public ArticleReply() {}
	
	public ArticleReply(String articleId, String id) {
		this.articleId = articleId;
		this.id = id;
	}

	public String getArticleId() {
		return articleId;
	}

	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUpperId() {
		return upperId;
	}

	public void setUpperId(String upperId) {
		this.upperId = upperId;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
