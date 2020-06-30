package net.oopscraft.application.menu;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import net.oopscraft.core.jpa.SystemEntity;
import net.oopscraft.application.security.SecurityPolicy;
import net.oopscraft.application.user.Authority;

@Entity
@Table(
	name = "APP_MENU_INFO",
	indexes = {
		@Index(columnList="UPER_MENU_ID,MENU_SEQ")
	}
)
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Menu extends SystemEntity {
	
	@Id
	@Column(name = "MENU_ID", length = 32)
	String id;

	@Column(name = "MENU_NAME", length = 1024)
	@NotNull
	String name;
	
	@Column(name = "UPER_MENU_ID", length = 32)
	String upperId;
	
	@Column(name = "MENU_SEQ")
	Integer sequence;
	
	@Column(name = "MENU_ICON", length = Integer.MAX_VALUE)
	@Lob
	String icon;

	@Column(name = "MENU_DESC", length = Integer.MAX_VALUE)
	@Lob
	String description;
	
	@Column(name = "LINK_URL", length = 1024)
	String linkUrl;

	public enum LinkTarget {
		SELF, BLANK
	}
	
	@Column(name = "LINK_TRGT")
	@Enumerated(EnumType.STRING)
	@JsonView(List.class)
	LinkTarget linkTarget;
	
	@Column(name = "DISP_PLCY")
	@Enumerated(EnumType.STRING)
	@JsonView(List.class)
	SecurityPolicy displayPolicy = SecurityPolicy.ANONYMOUS;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "APP_MENU_AUTH_DISP_MAP", 
		joinColumns = @JoinColumn(name = "MENU_ID"),
		foreignKey = @ForeignKey(name = "none"),
		inverseJoinColumns = @JoinColumn(name = "AUTH_ID"),
		inverseForeignKey = @ForeignKey(name = "none")
	)
	List<Authority> displayAuthorities = new ArrayList<Authority>();
	
	@JsonIgnore
	@OneToMany(
		fetch = FetchType.LAZY, 
		mappedBy = "id", 
		cascade = CascadeType.ALL, 
		orphanRemoval = true
	)
	@OrderBy("language")
	List<MenuI18n> i18ns = new ArrayList<MenuI18n>();
	
	public Menu() {}
	
	public Menu(String id) {
		this.id = id;
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

	public String getName() {
		return name;
	}
	
	/**
	 * getValue
	 * @param language
	 * @return
	 */
	public String getName(String language) {
		for(MenuI18n i18n : i18ns) {
			if(i18n.getLanguage().contentEquals(language)) {
				return i18n.getName();
			}
		}
		return this.getName();
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

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public LinkTarget getLinkTarget() {
		return linkTarget;
	}

	public void setLinkTarget(LinkTarget linkTarget) {
		this.linkTarget = linkTarget;
	}

	public SecurityPolicy getDisplayPolicy() {
		return displayPolicy;
	}

	public void setDisplayPolicy(SecurityPolicy displayPolicy) {
		this.displayPolicy = displayPolicy;
	}

	public List<Authority> getDisplayAuthorities() {
		return displayAuthorities;
	}

	public void setDisplayAuthorities(List<Authority> displayAuthorities) {
		this.displayAuthorities = displayAuthorities;
	}

	public List<MenuI18n> getI18ns() {
		return i18ns;
	}

	public void setI18ns(List<MenuI18n> i18ns) {
		this.i18ns = i18ns;
	}

}
