package net.oopscraft.application.board;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import net.oopscraft.core.jpa.SystemEntity;

@Entity
@Table(name = "APP_ATCL_FILE_INFO")
@IdClass(ArticleFile.Pk.class)
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ArticleFile extends SystemEntity {

	/**
	 * BoardArticleFile.Pk
	 */
	public static class Pk implements Serializable {
		private static final long serialVersionUID = 3127781407229494383L;
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
	@Column(name = "FILE_ID", length = 32)
	String id;
	
	@Column(name = "FILE_NAME", length = 1024)
	String name;
	
	@Column(name = "FILE_TYPE", length = 255)
	String type;
	
	@Column(name = "FILE_SIZE")
	Long size;
	
	public ArticleFile() { }
	
	public ArticleFile(String articleId, String id) {
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

}
