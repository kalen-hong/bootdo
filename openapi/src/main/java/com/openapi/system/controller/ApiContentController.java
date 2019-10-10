package com.openapi.system.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.openapi.common.controller.BaseController;
import com.openapi.common.utils.PageUtils;
import com.openapi.common.utils.Query;
import com.openapi.common.utils.R;
import com.openapi.system.domain.ApiContentDO;
import com.openapi.system.service.ApiContentService;

/**
 * api业务内容表
 * 
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2019-09-29 15:51:59
 */

 
@Controller
@RequestMapping("/system/apiContent")
public class ApiContentController extends BaseController {

	private String prefix = "system/apiContent";
	@Autowired
	private ApiContentService apiContentService;

	@GetMapping()
	@RequiresPermissions("system:apiContent:apiContent")
	String ApiContent(){
	    return prefix+"/apiContent";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("system:apiContent:apiContent")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<ApiContentDO> apiContentList = apiContentService.list(query);
		int total = apiContentService.count(query);
		PageUtils pageUtils = new PageUtils(apiContentList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("system:apiContent:add")
	String add(){
	    return "system/apiContent/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("system:apiContent:edit")
	String edit(@PathVariable("id") Long id,Model model){
		ApiContentDO apiContent = apiContentService.get(id);
		model.addAttribute("apiContent", apiContent);
	    return "system/apiContent/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("system:apiContent:add")
	public R save(ApiContentDO apiContentDo) {
		apiContentDo.setGtmCreate(new Date());
		if (apiContentService.save(apiContentDo) > 0) {
			return R.ok();
		}
		return R.error();
	}

	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("system:apiContent:edit")
	public R update( ApiContentDO apiContent){
		apiContent.setGtmModified(new Date());
		apiContentService.update(apiContent);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("system:apiContent:remove")
	public R remove( Long id){
		if(apiContentService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("system:apiContent:batchRemove")
	public R remove(@RequestParam("ids[]") Long[] ids){
		apiContentService.batchRemove(ids);
		return R.ok();
	}



	@PostMapping("/exit")
	@ResponseBody
	boolean exit(@RequestParam Map<String, Object> params) {
		// 存在，不通过，false
		return !apiContentService.exit(params);
	}


}
