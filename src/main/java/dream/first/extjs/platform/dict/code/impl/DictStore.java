/**
 * 
 */
package dream.first.extjs.platform.dict.code.impl;

/**
 * 字典类型与该类型对应的所有字典
 * 
 * @since 1.0.0
 */
public class DictStore {

	private String type;

	private String data;

	public DictStore(String type, String data) {
		this.type = type;
		this.data = data;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

}