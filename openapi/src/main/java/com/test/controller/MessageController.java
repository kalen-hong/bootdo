package com.test.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.openapi.common.controller.BaseController;
import com.openapi.common.utils.DateUtils;
import com.openapi.common.utils.MathUtils;
import com.openapi.common.utils.TimeUtil;
import com.openapi.system.utils.MD5Util;
import com.openapi.system.vo.RequestVo;
import com.openapi.system.vo.ResponseVo;
import com.test.domain.MessageDO;
import com.test.service.MessageService;

/**
 * 
 * 
 * @author liy
 * @date 2019-10-24 15:01:07
 */

@Controller
@RequestMapping("/api/business")
public class MessageController extends BaseController {
	@Autowired
	private MessageService messageService;

	private static final String EXCEED_KEYWORD = "逾期";
	private static final String REPAYMENT_KEYWORD = "还款";
	protected Logger log = LoggerFactory.getLogger(MessageController.class);

	/*
	 * 1 黑名单 输入：号码 输出： 1 命中 2 未命中 逻辑：根据号码查询库中带有逾期关键字的最新短信，并且短信发送时间已经是当前时间7天之前
	 */
	@ResponseBody
	@PostMapping("/queryBlackStatus")
	public ResponseVo<List<Map<String, String>>> queryBlackStatus(RequestVo<String> requestVo) {
		try {
			@SuppressWarnings("unchecked")
			ResponseVo<List<Map<String, String>>> rvo = super.initSign(requestVo);
			if (rvo.getCode().equals(ResponseVo.FAIL)) {
				return rvo;
			}
			log.info("查询黑名单入参："+JSON.toJSONString(requestVo.getData()));
			Map<String, String> md5Phone = new HashMap<String, String>();
			// 明文号码集合
			JSONObject object = JSON.parseObject(requestVo.getData());
			List<String> iphoneNoList = object.getJSONArray("iPhone").toJavaList(String.class);
			for (String s : iphoneNoList) {
				md5Phone.put(MD5Util.md5(s), s);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			long time = TimeUtil.getOffsetDateString(new Date(), -7) / 1000;
			map.put("iphoneList", new ArrayList<String>(md5Phone.keySet()));
			map.put("msgTime", time);
			List<MessageDO> re = messageService.list(map);
			List<String> exceedMessgaeList = re.stream().filter((e) -> e.getMsgContent().contains(EXCEED_KEYWORD))
					.map(MessageDO::getIphoneNo).collect(Collectors.toList());
			List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
			for (String phone : iphoneNoList) {
				Map<String, String> singlePhoneBlackStatus = new HashMap<String, String>();
				if (exceedMessgaeList.contains(md5Phone.get(phone))) {
					singlePhoneBlackStatus.put("status", "1");
				} else {
					singlePhoneBlackStatus.put("status", "0");
				}
				singlePhoneBlackStatus.put("iPhone", phone);
				dataList.add(singlePhoneBlackStatus);
			}
			return new ResponseVo<List<Map<String, String>>>(ResponseVo.SUCCESS, "success", dataList);
		} catch (Exception e) {
			log.error("查询黑名单失败：" + e.getMessage(), e);
			return new ResponseVo<List<Map<String, String>>>(ResponseVo.FAIL, "fail", null);
		}
	}

	/**
	 * 2 逾期详情查询 输入：号码 输出：逾期平台，逾期金额，时间
	 * 逻辑：根据号码查询库中带有逾期关键字的短信，如果有多条，根据平台分组取最新的一条。然后检索中短信中逾期的金额。最后按照平台+金额返回
	 */
	@ResponseBody
	@PostMapping("/overTimeDetail")
	public ResponseVo<List<Map<String, String>>> overTimeDetail(RequestVo<String> requestVo) {
		@SuppressWarnings("unchecked")
		ResponseVo<List<Map<String, String>>> rvo = super.initSign(requestVo);
		if (rvo.getCode().equals(ResponseVo.FAIL)) {
			return rvo;
		}
		log.info("逾期详情入参："+JSON.toJSONString(requestVo.getData()));
		Map<String, String> md5Phone = new HashMap<String, String>();
		// 明文号码集合
		JSONObject object = JSON.parseObject(requestVo.getData());
		List<String> iphoneNoList = object.getJSONArray("iPhone").toJavaList(String.class);
		for (String s : iphoneNoList) {
			md5Phone.put(MD5Util.md5(s), s);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("iphoneList", new ArrayList<String>(md5Phone.keySet()));
		List<MessageDO> re = messageService.list(map);
		List<MessageDO> exceedMessgaeList = re.stream().filter((e) -> e.getMsgContent().contains(EXCEED_KEYWORD))
				.collect(Collectors.toList());
		if (CollectionUtils.isEmpty(exceedMessgaeList)) {
			return new ResponseVo<List<Map<String, String>>>(ResponseVo.FAIL, "fail", null);
		}
		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
		Map<String, List<MessageDO>> iphoneGroupMap = exceedMessgaeList.stream()
				.collect(Collectors.groupingBy(MessageDO::getIphoneNo));
		for (String phone : iphoneGroupMap.keySet()) {
			Map<String, MessageDO> singlePhoneExceedMap = statisticsLastExceedMessage(exceedMessgaeList);
			for (Map.Entry<String, MessageDO> entry : singlePhoneExceedMap.entrySet()) {
				Map<String, String> temp = new HashMap<String, String>();
				temp.put("iphone", md5Phone.get(phone));
				temp.put("platName", entry.getKey());
				String exceedAmount = MathUtils.getExceedAmount(entry.getValue().getMsgContent());
				temp.put("amount", exceedAmount);
				dataList.add(temp);
			}
		}
		return new ResponseVo<List<Map<String, String>>>(ResponseVo.SUCCESS, "success", dataList);
	}

	/**
	 * 统计单手机号各个平台最新的逾期短信
	 * 
	 * @return
	 */
	private Map<String, MessageDO> statisticsLastExceedMessage(List<MessageDO> exceedMessgaeList) {
		Map<String, MessageDO> exceedMap = new HashMap<String, MessageDO>();
		exceedMessgaeList.forEach(message -> {
			String platform = getPlatformName(message.getMsgContent());
			if (exceedMap.containsKey(platform)) {
				MessageDO existMessageDO = exceedMap.get(platform);
				// 是否是最新的逾期短信
				if (existMessageDO.getCreateDate().before(message.getCreateDate())) {
					exceedMap.put(platform, message);
				}
			} else {
				exceedMap.put(platform, message);
			}
		});
		return exceedMap;
	}

	private String getPlatformName(String messageContent) {
		int leftIndex = messageContent.indexOf("[");
		int rightIndex = messageContent.indexOf("]");
		return messageContent.substring(leftIndex + 1, rightIndex);
	}

	/**
	 * 3 黑转白： 输入：号码 输出：1 是 2 否 逻辑：根据号码查询库中 同一个平台下
	 * 至少有一次逾期和一次还款(同时存在)，并且还款日期大于逾期短信发送日期7天以上
	 * 
	 * @throws ParseException
	 */
	@ResponseBody
	@PostMapping("/listIsWhite")
	public ResponseVo<List<Map<String, String>>> listIsWhite(RequestVo<String> requestVo) throws ParseException {
		@SuppressWarnings("unchecked")
		ResponseVo<List<Map<String, String>>> rvo = super.initSign(requestVo);
		if (rvo.getCode().equals(ResponseVo.FAIL)) {
			return rvo;
		}
		log.info("黑转白入参："+JSON.toJSONString(requestVo.getData()));
		Map<String, String> md5Phone = new HashMap<String, String>();
		// 明文号码集合
		JSONObject object = JSON.parseObject(requestVo.getData());
		List<String> iphoneNoList = object.getJSONArray("iPhone").toJavaList(String.class);
		for (String s : iphoneNoList) {
			md5Phone.put(MD5Util.md5(s), s);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("iphoneList", new ArrayList<String>(md5Phone.keySet()));
		List<MessageDO> messageList = messageService.list(map);
		if (CollectionUtils.isEmpty(messageList)) {
			return new ResponseVo<List<Map<String, String>>>(ResponseVo.FAIL, "fail", null);
		}
		Map<String, List<MessageDO>> iphoneGroupMap = messageList.stream()
				.collect(Collectors.groupingBy(MessageDO::getIphoneNo));
		Map<String, String> meetConditionMap = new HashMap<String, String>();
		for (String phone : iphoneGroupMap.keySet()) {
			List<MessageDO> singlePhoneMessageList = iphoneGroupMap.get(phone);
			// 逾期
			List<MessageDO> exceedList = singlePhoneMessageList.stream()
					.filter((e) -> e.getMsgContent().contains(EXCEED_KEYWORD)).collect(Collectors.toList());
			// 还款
			List<MessageDO> repaymentList = singlePhoneMessageList.stream()
					.filter((e) -> e.getMsgContent().contains(REPAYMENT_KEYWORD)).collect(Collectors.toList());

			if (exceedList.size() == 0 || repaymentList.size() == 0) {
				continue;
			}
			if (exceedList.size() > 1) {
				exceedList.sort(new Comparator<MessageDO>() {

					@Override
					public int compare(MessageDO o1, MessageDO o2) {
						try {
							Date date1 = DateUtils.parse(DateUtils.DATE_TIME_PATTERN, o1.getMsgTime());
							Date date2 = DateUtils.parse(DateUtils.DATE_TIME_PATTERN, o2.getMsgTime());
							return date1.after(date2) ? 1 : 0;
						} catch (ParseException e) {
							return 0;
						}

					}
				});
			}
			String lastExceedMsgTimeStr = exceedList.get(0).getMsgTime();
			for (MessageDO msg : repaymentList) {
				Date lastExceedMsgDate = DateUtils.parse(DateUtils.DATE_TIME_PATTERN, lastExceedMsgTimeStr);
				Date currMsgTime = DateUtils.parse(DateUtils.DATE_TIME_PATTERN, msg.getMsgTime());
				Calendar cal = Calendar.getInstance();
				cal.setTime(currMsgTime);
				cal.add(Calendar.DAY_OF_MONTH, -7);
				if (lastExceedMsgDate.before(cal.getTime())) {
					meetConditionMap.put(md5Phone.get(msg.getIphoneNo()), "1");
				}
			}
		}
		for (String phone : iphoneNoList) {
			if (!meetConditionMap.containsKey(phone)) {
				meetConditionMap.put(phone, "0");
			}
		}
		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
		for (String phone : meetConditionMap.keySet()) {
			Map<String, String> temp = new HashMap<String, String>();
			temp.put("iPhone", phone);
			temp.put("isWhite", meetConditionMap.get(phone));
			dataList.add(temp);
		}
		return new ResponseVo<List<Map<String, String>>>(ResponseVo.SUCCESS, "success", dataList);
	}

	/**
	 * 号码状态查询，重复记录会替换调 输入：号码 输出：号码状态（根据AI机器平台返回的状态）
	 * 逻辑：从AI机器人平台获取号码结果入库。然后根据号码查询出每个状态返回
	 *
	 * @param RequestVo
	 * @return
	 */
	@ResponseBody
	@PostMapping("/ListIPhoneStatus")
	public ResponseVo<List<Map<String, String>>> ListIPhoneStatus(RequestVo<JSONObject> RequestVo) {
//		// ResponseVo rvo = super.initSign(accessToken,timestamp,sign);
//		ResponseVo rvo = new ResponseVo();
//		if (ResponseVo.FAIL.equals(rvo.getCode())) {
//			return rvo;
//		}
//
//		Map md5Phone = new HashMap();
//		// 明文号码集合
//		List<String> iphoneNoList = (RequestVo.getData()).getJSONArray("iphoneList").toJavaList(String.class);
//		for (String s : iphoneNoList) {
//			md5Phone.put(s, MD5Util.md5(s));
//		}
//		Map map = new HashMap();
//		long time = TimeUtil.getOffsetDateString(new Date(), -7) / 1000;
//		map.put("iphoneList", new ArrayList<String>(md5Phone.values()));
//		List<MessageDO> re = messageService.list(map);
//
//		Map<String, MessageDO> mapStream = re.stream()
//				.collect(Collectors.toMap(MessageDO::getIphoneNo, a -> a, (k1, k2) -> k1));
//		JSONObject jo = new JSONObject();
//		for (String phone : iphoneNoList) {
//			if (mapStream.containsKey(md5Phone.get(phone))) {
//				jo.put("iPhone", phone);
//				jo.put("status", mapStream.get(md5Phone.get(phone)).getStatus());
//			}
//		}
//		reMap.put("data", jo);
		return new ResponseVo<List<Map<String, String>>>(ResponseVo.SUCCESS, "success", null);
	}

	/**
	 * 号码多投状态查询
	 *
	 * @param RequestVo 输入：号码 输出：1是 0 否 逻辑：同一个号码在不同平台(借款或者还款)次数超过2次 count(借款) > 2 or
	 *                  count逾期() > 2
	 * 
	 * @return
	 */
	@ResponseBody
	@PostMapping("/listIPhoneCastStatus")
	public ResponseVo<List<Map<String, String>>> listIPhoneCastStatus(RequestVo<String> requestVo) {
		@SuppressWarnings("unchecked")
		ResponseVo<List<Map<String, String>>> rvo = super.initSign(requestVo);
		if (rvo.getCode().equals(ResponseVo.FAIL)) {
			return rvo;
		}
		log.info("号码多投状态入参："+JSON.toJSONString(requestVo.getData()));
		Map<String, String> md5PhoneMap = new HashMap<String, String>();
		// 明文号码集合
		JSONObject object = JSON.parseObject(requestVo.getData());
		List<String> iphoneNoList = object.getJSONArray("iPhone").toJavaList(String.class);
		for (String s : iphoneNoList) {
			md5PhoneMap.put(MD5Util.md5(s), s);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("iphoneList", new ArrayList<String>(md5PhoneMap.keySet()));
		List<MessageDO> messageList = messageService.list(map);
		if (CollectionUtils.isEmpty(messageList)) {
			return new ResponseVo<List<Map<String, String>>>(ResponseVo.FAIL, "fail", null);
		}
		Map<String, List<MessageDO>> iphoneGroupMap = messageList.stream().filter(messag -> {
			return messag.getMsgContent() != null;
		}).collect(Collectors.groupingBy(MessageDO::getIphoneNo));
		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
		for (String md5Phone : iphoneGroupMap.keySet()) {
			List<MessageDO> singlePhoneMessageList = iphoneGroupMap.get(md5Phone);
			long exceedNum = singlePhoneMessageList.stream()
					.filter(message -> message.getMsgContent().contains(EXCEED_KEYWORD)).count();
			Map<String, String> tempMap = new HashMap<String, String>();
			tempMap.put("iPhone", md5PhoneMap.get(md5Phone));
			tempMap.put("status", "0");
			if (exceedNum > 2) {
				tempMap.put("status", "1");
				dataList.add(tempMap);
				continue;
			}
			long repaymentNum = singlePhoneMessageList.stream()
					.filter(message -> message.getMsgContent().contains(REPAYMENT_KEYWORD)).count();
			if (repaymentNum > 2) {
				tempMap.put("status", "1");
			}
			dataList.add(tempMap);
		}
		return new ResponseVo<List<Map<String, String>>>(ResponseVo.SUCCESS, "success", dataList);
	}
}