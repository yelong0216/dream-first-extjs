package dream.first.extjs.platform.dict.code;

import dream.first.core.platform.dict.manage.DictManager;

/**
 * 字典代码管理器。获取字典的ExtJS脚本
 * 
 * @since 2.0
 */
public interface DictCodeManager {

	/**
	 * 获取所有字典的ExtJS脚本
	 * 
	 * @return 所有字典的ExtJS脚本
	 * @throws DictCodeManageException 字典ExtJS管理异常
	 */
	String getExtJSCode() throws DictCodeManageException;

	/**
	 * 获取指定字典类型的ExtJS脚本
	 * 
	 * @return 指定字典类型的ExtJS脚本
	 * @throws DictCodeManageException 字典ExtJS管理异常
	 */
	String getExtJSCode(String dictType) throws DictCodeManageException;

	/**
	 * 获取多个字典的ExtJS脚本
	 * 
	 * @return 多个字典的ExtJS脚本
	 * @throws DictCodeManageException 字典ExtJS管理异常
	 */
	String getExtJSCode(String[] dictTypes) throws DictCodeManageException;

	/**
	 * @return 字典管理器
	 */
	DictManager getDictManager();

}
