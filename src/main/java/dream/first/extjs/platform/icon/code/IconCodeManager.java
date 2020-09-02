/**
 * 
 */
package dream.first.extjs.platform.icon.code;

import dream.first.core.platform.icon.manage.IconManager;

/**
 * 图标代码管理器。生成图标ExtJS、CSS等代码
 * 
 * @since 2.0
 */
public interface IconCodeManager {

	/**
	 * 获取所有图标CSS样品代码
	 * 
	 * @return 所有图标CSS样式代码
	 * @throws IconCodeManageException 图标代码管理异常
	 */
	String getCSSCode() throws IconCodeManageException;

	/**
	 * 获取所有图标ExtJS脚本代码
	 * 
	 * @return 所有图标ExtJS脚本代码
	 * @throws IconCodeManageException 图标代码管理异常
	 */
	String getExtJSCode() throws IconCodeManageException;

	/**
	 * @return 图标管理器
	 */
	IconManager getIconManager();
	
}
