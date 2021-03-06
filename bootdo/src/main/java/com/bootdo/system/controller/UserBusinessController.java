package com.bootdo.system.controller;

import java.util.List;
import java.util.Map;

import com.bootdo.common.controller.BaseController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bootdo.system.domain.UserBusinessDO;
import com.bootdo.system.service.UserBusinessService;
import com.bootdo.common.utils.PageUtils;
import com.bootdo.common.utils.Query;
import com.bootdo.common.utils.R;

/**
 * 业务用户表
 * 
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2019-09-29 18:20:03
 */
 
@Controller
@RequestMapping("/system/userBusiness")
public class UserBusinessController extends BaseController {
	@Autowired
	private UserBusinessService userBusinessService;
	
	@GetMapping()
	@RequiresPermissions("system:userBusiness:userBusiness")
	String UserBusiness(){
	    return "system/userBusiness/userBusiness";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("system:userBusiness:userBusiness")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<UserBusinessDO> userBusinessList = userBusinessService.list(query);
		int total = userBusinessService.count(query);
		PageUtils pageUtils = new PageUtils(userBusinessList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("system:userBusiness:add")
	String add(){
	    return "system/userBusiness/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("system:userBusiness:edit")
	String edit(@PathVariable("id") Long id,Model model){
		UserBusinessDO userBusiness = userBusinessService.get(id);
		model.addAttribute("userBusiness", userBusiness);
	    return "system/userBusiness/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("system:userBusiness:add")
	public R save( UserBusinessDO userBusiness){
		if(userBusinessService.save(userBusiness)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("system:userBusiness:edit")
	public R update( UserBusinessDO userBusiness){
		userBusinessService.update(userBusiness);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("system:userBusiness:remove")
	public R remove( Long id){
		if(userBusinessService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("system:userBusiness:batchRemove")
	public R remove(@RequestParam("ids[]") Long[] ids){
		userBusinessService.batchRemove(ids);
		return R.ok();
	}
	
}
