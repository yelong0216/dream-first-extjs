/**
 * 
 */
package dream.first.extjs.platform.dict.code.impl;

/**
 * 字典值与显示文本的映射。
 * 
 * @since 1.0.0
 */
public class DictPair {

	/**
	 * 字典值
	 */
	private String dictValue;

	/**
	 * 字典显示的文本
	 */
	private String dictText;

	/**
	 * @param dictValue 字典值
	 * @param dictText  字典显示的文本
	 */
	public DictPair(String dictValue, String dictText) {
		this.dictValue = dictValue;
		this.dictText = dictText;
	}

	public String getDictValue() {
		return dictValue;
	}

	public void setDictValue(String dictValue) {
		this.dictValue = dictValue;
	}

	public String getDictText() {
		return dictText;
	}

	public void setDictText(String dictText) {
		this.dictText = dictText;
	}

}
