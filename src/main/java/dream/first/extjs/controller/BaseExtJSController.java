/**
 * 
 */
package dream.first.extjs.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.CrossOrigin;

import dream.first.core.controller.BaseCoreController;
import dream.first.core.model.service.DreamFirstModelService;
import dream.first.extjs.base.controller.DFBaseExtJSControllerable;
import dream.first.extjs.login.LoginValidate;

/**
 * 基础平台 controller
 * 
 * @since 2.0
 */
@CrossOrigin
@LoginValidate
public abstract class BaseExtJSController extends BaseCoreController implements DFBaseExtJSControllerable {

	// ==================================================static/final==================================================

	@Resource
	protected DreamFirstModelService modelService;

	// =====================Get/Set==================

	public DreamFirstModelService getModelService() {
		return modelService;
	}

}
