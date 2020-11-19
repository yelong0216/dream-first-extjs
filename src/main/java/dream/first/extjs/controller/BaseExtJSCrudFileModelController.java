/**
 * 
 */
package dream.first.extjs.controller;

import java.io.File;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.yelong.commons.io.FileUtilsE;
import org.yelong.commons.lang.Strings;
import org.yelong.core.annotation.Nullable;
import org.yelong.core.model.collector.ModelCollectors;
import org.yelong.core.model.sql.SqlModel;

import dream.first.core.model.file.BaseFileModelable;
import dream.first.core.model.file.BaseFileModels;
import dream.first.extjs.support.msg.JsonMsg;

/**
 * 基础的文件增删改查控制器
 * 
 * @since 2.0
 */
public abstract class BaseExtJSCrudFileModelController<M extends BaseFileModelable>
		extends BaseExtJSCrudModelController<M> {

	/**
	 * 默认得文件上传的参数名称
	 */
	public static final String DEFAULT_FILE_PARAMETER_NAME = "file";

	/**
	 * 下载文件
	 * 
	 * @param model SQL model
	 * @throws Exception 异常
	 */
	@ResponseBody
	@RequestMapping("download")
	public void download(@ModelAttribute M model) throws Exception {
		M fileModel = getDownloadFileModel(model);
		Objects.requireNonNull(fileModel, "不存在的记录！");
		String filePath = fileModel.getFilePath();
		Strings.requireNonBlank(filePath, "不存在的文件！");
		FileUtilsE.requireNonExist(filePath, fileModel.getFileName() + "文件不存在！");
		responseFile(FileUtilsE.getFile(filePath), fileModel.getFileName());
	}

	/**
	 * 获取下载的模型对象
	 * 
	 * @param model bind model
	 * @return 被下载的模型对象
	 * @throws Exception 异常
	 * @see #download(BaseFileModelable)
	 */
	@Nullable
	protected M getDownloadFileModel(M model) throws Exception {
		SqlModel<M> sqlModel = new SqlModel<M>(model);
		if (!modelService.getModelConfiguration().getSqlModelResolver().existCondition(sqlModel)) {
			return null;
		}
		M fileModel = modelService.findFirstBySqlModel(getModelClass(), sqlModel);
		return fileModel;
	}

	/**
	 * 文件是否存在
	 * 
	 * @param model SQL model
	 * @return JSON格式的响应结果
	 * @throws Exception 异常
	 */
	@ResponseBody
	@RequestMapping("existFile")
	public String existFile(@ModelAttribute M model) throws Exception {
		M fileModel = getExistFileModel(model);
		Objects.requireNonNull(fileModel, "不存在的记录！");
		String filePath = fileModel.getFilePath();
		boolean existFile;
		if (StringUtils.isBlank(filePath)) {
			existFile = false;
		} else {
			existFile = FileUtilsE.exists(filePath);
		}
		JsonMsg msg = new JsonMsg(true);
		msg.put("existFile", existFile);
		return toJson(msg);
	}

	/**
	 * 获取查询是否存在文件的模型
	 * 
	 * @param model bind model
	 * @return 查询是否存在的文件模型
	 * @throws Exception 异常
	 * @see #existFile(BaseFileModelable)
	 */
	@Nullable
	protected M getExistFileModel(M model) throws Exception {
		SqlModel<M> sqlModel = new SqlModel<M>(model);
		M fileModel = modelService.findFirstBySqlModel(getModelClass(), sqlModel);
		return fileModel;
	}

	/**
	 * 获取文件存储的根路径（不需要包含文件名称）。 该方法在{@link #beforeSave(BaseFileModelable)}
	 * 中将文件流输入文件前调用，且调用时其文件的名称、新名称、类型属性已经设置。如果重写
	 * {@link #beforeSave(BaseFileModelable)}这个方法可能不会被调用
	 * 
	 * @param fileModel bind model
	 * @return 文件存儲的根路径
	 */
	protected abstract String getFileRootPath(M fileModel);

	/**
	 * @return 文件的参数名称
	 */
	protected String getFileParameterName() {
		return DEFAULT_FILE_PARAMETER_NAME;
	}

	@Override
	public boolean validateModel(M model, JsonMsg msg) throws Exception {
		// 默认情况下文件是必须存在的。重写此方法可以覆盖
		MultipartFile file = getMultipartFile("file");
		Objects.requireNonNull(file, "请上传文件！");
		Strings.requireNonBlank(file.getOriginalFilename(), "请上传文件");
		return true;
	}

	@Override
	public void beforeSave(M model) throws Exception {
		MultipartFile file = getMultipartFile("file");
		if (null == file) {
			return;
		}
		if (StringUtils.isBlank(file.getOriginalFilename())) {
			return;
		}
		File newFile = null;
		BaseFileModels.setBaseProperty(model, file);
		newFile = FileUtilsE.createNewFile(getFileRootPath(model), model.getNewFileName());
		file.transferTo(newFile);
		model.setFilePath(newFile.getAbsolutePath());
	}

	@Override
	public boolean deleteModel(String deleteIds) throws Exception {
		List<M> fileModels = modelService
				.collect(ModelCollectors.findByOnlyPrimaryKeyContains(getModelClass(), deleteIds.split(",")));
		if (CollectionUtils.isEmpty(fileModels)) {
			return true;
		}
		// 连同文件一起删除
		modelService.doOperation(() -> {
			for (M fileModel : fileModels) {
				String filePath = fileModel.getFilePath();
				if (StringUtils.isNotBlank(filePath)) {
					FileUtilsE.deleteQuietly(FileUtilsE.getFile(filePath));
				}
				modelService.removeById(fileModel.getClass(), fileModel.getId());
			}
		});
		return true;
	}

}
