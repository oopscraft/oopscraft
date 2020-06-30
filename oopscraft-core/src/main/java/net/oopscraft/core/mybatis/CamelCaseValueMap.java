package net.oopscraft.core.mybatis;

import org.apache.commons.text.CaseUtils;

import net.oopscraft.core.ValueMap;

public class CamelCaseValueMap extends ValueMap {

	private static final long serialVersionUID = 7798178363368009309L;
	private static final char[] WHITE_SPACE = new char[]{' ','-','_'};
	
	public Object put(String name, Object value) {
		return super.put(CaseUtils.toCamelCase(name, false, WHITE_SPACE), value);
	}

}
