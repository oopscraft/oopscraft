package net.oopscraft.application.user;

import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import net.oopscraft.application.core.jpa.SystemEntity;

@Entity
@Table(
	name = "APP_ROLE_INFO",
	indexes = {
		@Index(columnList="SYS_INST_DATE"),
		@Index(columnList="ROLE_NAME,SYS_INST_DATE")
	}
)
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Role extends SystemEntity {

	@Id
	@Column(name = "ROLE_ID", length = 32)
	@NotNull
	String id;

	@Column(name = "ROLE_NAME", length = 1024)
	@NotNull
	String name;
	
	@Column(name = "ROLE_ICON", length = Integer.MAX_VALUE)
	@Lob
	String icon;

	@Column(name = "ROLE_DESC", length = Integer.MAX_VALUE)
	@Lob
	String description;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "APP_ROLE_AUTH_MAP", 
		joinColumns = @JoinColumn(name = "ROLE_ID"),
		foreignKey = @ForeignKey(name = "none"),
		inverseJoinColumns = @JoinColumn(name = "AUTH_ID"),
		inverseForeignKey = @ForeignKey(name = "none")
	)
	List<Authority> authorities;
	
	@Transient
	String holder = null;
	
	public Role() {}
	
	public Role(String id) {
		this.id = id;
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

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Authority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(List<Authority> authorities) {
		this.authorities = authorities;
	}

	public String getHolder() {
		return holder;
	}

	public void setHolder(String holder) {
		this.holder = holder;
	}

}
