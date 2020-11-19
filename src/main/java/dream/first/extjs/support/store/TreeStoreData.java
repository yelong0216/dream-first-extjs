/**
 * 
 */
package dream.first.extjs.support.store;

import java.util.List;

import dream.first.extjs.base.msg.DFETreeStoreData;

/**
 * extjs 中树数据的对象
 * 
 * @since 1.0.0
 */
public class TreeStoreData<T> extends DFETreeStoreData<T> {

	public TreeStoreData() {
		this(null);
	}

	public TreeStoreData(T data) {
		super(data);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TreeStoreData<T>> getChildren() {
		return (List<TreeStoreData<T>>) super.getChildren();
	}

}
