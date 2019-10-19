package com.test.controller;

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

import com.test.domain.TestUserDO;
import com.test.service.TestUserService;
import com.openapi.common.utils.PageUtils;
import com.openapi.common.utils.Query;
import com.openapi.common.utils.R;

/**
 * 
 * 
 * @author liy
 * @email ${email}
 * @date 2019-10-19 19:09:03
 */
 
@Controller
@RequestMapping("/test/testUser")
public class TestUserController {
	@Autowired
	private TestUserService testUserService;
	
	@GetMapping()
//	@RequiresPermissions("test:testUser:testUser")
	String TestUser(){
	    return "test/testUser/testUser";
	}
	
	@ResponseBody
	@GetMapping("/list")
//	@RequiresPermissions("test:testUser:testUser")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<TestUserDO> testUserList = testUserService.list(query);
		int total = testUserService.count(query);
		PageUtils pageUtils = new PageUtils(testUserList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
//	@RequiresPermissions("test:testUser:add")
	String add(){
	    return "test/testUser/add";
	}

	@GetMapping("/edit/{id}")
//	@RequiresPermissions("test:testUser:edit")
	String edit(@PathVariable("id") Integer id,Model model){
		TestUserDO testUser = testUserService.get(id);
		model.addAttribute("testUser", testUser);
	    return "test/testUser/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
//	@RequiresPermissions("test:testUser:add")
	public R save( TestUserDO testUser){
		if(testUserService.save(testUser)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
//	@RequiresPermissions("test:testUser:edit")
	public R update( TestUserDO testUser){
		testUserService.update(testUser);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
//	@RequiresPermissions("test:testUser:remove")
	public R remove( Integer id){
		if(testUserService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
//	@RequiresPermissions("test:testUser:batchRemove")
	public R remove(@RequestParam("ids[]") Integer[] ids){
		testUserService.batchRemove(ids);
		return R.ok();
	}
	
}
