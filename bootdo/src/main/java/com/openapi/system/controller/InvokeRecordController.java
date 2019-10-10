package com.openapi.system.controller;

import java.util.List;
import java.util.Map;

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

import com.openapi.system.domain.InvokeRecordDO;
import com.openapi.system.service.InvokeRecordService;
import com.openapi.common.utils.PageUtils;
import com.openapi.common.utils.Query;
import com.openapi.common.utils.R;

/**
 * 
 * 
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2019-10-10 11:23:49
 */
 
@Controller
@RequestMapping("/system/invokeRecord")
public class InvokeRecordController {
	@Autowired
	private InvokeRecordService invokeRecordService;
	
	@GetMapping()
	@RequiresPermissions("system:invokeRecord:invokeRecord")
	String InvokeRecord(){
	    return "system/invokeRecord/invokeRecord";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("system:invokeRecord:invokeRecord")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<InvokeRecordDO> invokeRecordList = invokeRecordService.list(query);
		int total = invokeRecordService.count(query);
		PageUtils pageUtils = new PageUtils(invokeRecordList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("system:invokeRecord:add")
	String add(){
	    return "system/invokeRecord/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("system:invokeRecord:edit")
	String edit(@PathVariable("id") Long id,Model model){
		InvokeRecordDO invokeRecord = invokeRecordService.get(id);
		model.addAttribute("invokeRecord", invokeRecord);
	    return "system/invokeRecord/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("system:invokeRecord:add")
	public R save( InvokeRecordDO invokeRecord){
		if(invokeRecordService.save(invokeRecord)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("system:invokeRecord:edit")
	public R update( InvokeRecordDO invokeRecord){
		invokeRecordService.update(invokeRecord);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("system:invokeRecord:remove")
	public R remove( Long id){
		if(invokeRecordService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("system:invokeRecord:batchRemove")
	public R remove(@RequestParam("ids[]") Long[] ids){
		invokeRecordService.batchRemove(ids);
		return R.ok();
	}
	
}
