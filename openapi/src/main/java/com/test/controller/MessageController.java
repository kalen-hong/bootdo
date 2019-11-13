package com.test.controller;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.openapi.common.controller.BaseController;
import com.openapi.common.utils.*;
import com.openapi.system.utils.MD5Util;
import com.openapi.system.vo.RequestVo;
import com.openapi.system.vo.ResponseVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.junit.Test;
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

import com.test.domain.MessageDO;
import com.test.service.MessageService;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.maxBy;

/**
 * 
 * 
 * @author liy
 * @email ${email}
 * @date 2019-10-24 15:01:07
 */
 
@Controller
@RequestMapping("/api/business")

public class MessageController extends BaseController {
    @Autowired
    private MessageService messageService;

    private String keyword = "逾期";
    private String keyword1 = "还款";
    private List<String> signlist = Arrays.asList("腾讯云", "有道云");//签名 后续需要加入到数据库配置

    private Map reMap = new HashMap();


    /*
	1 黑名单
    输入：号码
    输出： 1 命中 2 未命中
    逻辑：根据号码查询库中带有逾期关键字的最新短信，并且短信发送时间已经是当前时间7天之前
    */
    @ResponseBody
    @PostMapping("/queryBlackStatus")
    public ResponseVo queryBlackStatus(@RequestBody RequestVo<JSONObject> RequestVo) {

        ResponseVo rvo = new ResponseVo();
        if (ResponseVo.FAIL.equals(rvo.getCode())) {
            return rvo;
        }
        Map md5Phone = new HashMap();
        //明文号码集合
        List<String> iphoneNoList = (RequestVo.getData()).getJSONArray("iphoneList").toJavaList(String.class);
        for (String s : iphoneNoList) {
            md5Phone.put(s, MD5Util.md5(s));
        }
        Map map = new HashMap();
        long time = TimeUtil.getOffsetDateString(new Date(), -7) / 1000;
        map.put("iphoneList", new ArrayList<String>(md5Phone.values()));
        map.put("msgTime", time);
        List<MessageDO> re = messageService.list(map);

        List filterList = re.stream().filter((e) -> e.getMsgContent().contains(keyword)).map(MessageDO::getIphoneNo).collect(Collectors.toList());
        JSONArray ja = new JSONArray();
        for (String phone : iphoneNoList) {
            JSONObject jo = new JSONObject();
            if (filterList.contains(md5Phone.get(phone))) {
                jo.put("iPhone", phone);
                jo.put("status", "1");
            } else {
                jo.put("iPhone", phone);
                jo.put("status", "0");
            }
            ja.add(jo);
        }
        reMap.put("data", ja);

        rvo.setCode(ResponseVo.SUCCESS);
        rvo.setData(reMap);
        rvo.setMsg("success");
        return rvo;
    }


    /**
     * 2 逾期详情查询
     * 输入：号码
     * 输出：逾期平台，逾期金额，时间
     * 逻辑：根据号码查询库中带有逾期关键字的短信，如果有多条，根据签名分组取最新的一条。然后检索中短信中逾期的金额。最后按照平台+金额返回
     */
    @ResponseBody
    @PostMapping("/overTimeDetail")
    public ResponseVo overTimeDetail(@RequestBody RequestVo<JSONObject> RequestVo) {
        //ResponseVo rvo = super.initSign(accessToken,timestamp,sign);
        ResponseVo rvo = new ResponseVo();
        if (ResponseVo.FAIL.equals(rvo.getCode())) {
            return rvo;
        }

        Map md5Phone = new HashMap();
        //明文号码集合
        List<String> iphoneNoList = (RequestVo.getData()).getJSONArray("iphoneList").toJavaList(String.class);
        for (String s : iphoneNoList) {
            md5Phone.put(s, MD5Util.md5(s));
        }
        Map map = new HashMap();
        long time = TimeUtil.getOffsetDateString(new Date(), -7) / 1000;
        map.put("iphoneList", new ArrayList<String>(md5Phone.values()));
        List<MessageDO> re = messageService.list(map);

        List<MessageDO> filterList = re.stream().filter((e) -> e.getMsgContent().contains(keyword)).collect(Collectors.toList());
        //分组后的map
        Map<String, List<MessageDO>> singMap = new HashMap<>();
        for (String sign : signlist) {
            List<MessageDO> list = filterList.stream().filter((e) -> e.getMsgContent().contains(sign))
                    .collect(Collectors.toList());
            singMap.put(sign, list);
        }



        JSONObject jo = new JSONObject();
        Iterator it = singMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            MessageDO messageDO = ((MessageDO)pair.getValue());
            String sign = pair.getKey().toString();
            jo.put(messageDO.getIphoneNo(), mathUtils.maxString(messageDO.getMsgContent()));
        }

        reMap.put("data", jo);

        rvo.setCode(ResponseVo.SUCCESS);
        rvo.setData(reMap);
        rvo.setMsg("success");
        return rvo;
    }

    /**
     * 3 黑转白：
     * 输入：号码
     * 输出：1 是 2 否
     * 逻辑：根据号码查询库中 同一个签名下 至少有一次逾期和一次还款，并且还款日期大于逾期短信日期7天以上
     */
    @ResponseBody
    @PostMapping("/listIsWhite")
    public ResponseVo listIsWhite(@RequestBody RequestVo<JSONObject> RequestVo) {
        //ResponseVo rvo = super.initSign(accessToken,timestamp,sign);
        ResponseVo rvo = new ResponseVo();
        if (ResponseVo.FAIL.equals(rvo.getCode())) {
            return rvo;
        }
        Map<String, String> md5Phone = new HashMap();
        //明文号码集合
        List<String> iphoneNoList = (RequestVo.getData()).getJSONArray("iphoneList").toJavaList(String.class);
        for (String s : iphoneNoList) {
            md5Phone.put(MD5Util.md5(s), s);
        }
        Map map = new HashMap();
        map.put("iphoneList", new ArrayList<String>(md5Phone.keySet()));
        List<MessageDO> re = messageService.list(map);
        //分组后的map
        Map<String, List<MessageDO>> singMap = new HashMap<>();
        for (String sign : signlist) {
            List<MessageDO> list = re.stream().filter((e) -> e.getMsgContent().contains(sign))
                    .collect(Collectors.toList());
            singMap.put(sign, list);
        }
        Iterator it = singMap.entrySet().iterator();
        Map<String, Object> rjMap = new HashMap<String, Object>();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            List<MessageDO> list1 = (List<MessageDO>) pair.getValue();
            //逾期
            List<MessageDO> filterList = list1.stream().filter((e) -> e.getMsgContent().contains(keyword)).collect(Collectors.toList());
            //还款
            List<MessageDO> filterList1 = list1.stream().filter((e) -> e.getMsgContent().contains(keyword1)).collect(Collectors.toList());

            if (filterList.size() == 0 || filterList1.size() == 0) {
                continue;
            }
            for (MessageDO md : filterList) {
                for (MessageDO md1 : filterList1) {
                    if (md.getIphoneNo().equals(md.getIphoneNo()) && md1.getMsgTime() - md.getMsgTime() > 86400 * 7) {
                        rjMap.put(md5Phone.get(md.getIphoneNo()), true);
                    }
                }
            }
        }
        for (String p : new ArrayList<String>(md5Phone.values())) {
            if (!rjMap.containsKey(p)) {
                rjMap.put(p, false);
            }
        }

        reMap.put("data", rjMap);
        return new ResponseVo(ResponseVo.SUCCESS, "success", reMap);
    }


    /**
     * 号码状态查询，重复记录会替换调
     * 输入：号码
     * 输出：号码状态（根据AI机器平台返回的状态）
     * 逻辑：从AI机器人平台获取号码结果入库。然后根据号码查询出每个状态返回
     *
     * @param RequestVo
     * @return
     */
    @ResponseBody
    @PostMapping("/ListIPhoneStatus")
    public ResponseVo ListIPhoneStatus(@RequestBody RequestVo<JSONObject> RequestVo) {
        //ResponseVo rvo = super.initSign(accessToken,timestamp,sign);
        ResponseVo rvo = new ResponseVo();
        if (ResponseVo.FAIL.equals(rvo.getCode())) {
            return rvo;
        }

        Map md5Phone = new HashMap();
        //明文号码集合
        List<String> iphoneNoList = (RequestVo.getData()).getJSONArray("iphoneList").toJavaList(String.class);
        for (String s : iphoneNoList) {
            md5Phone.put(s, MD5Util.md5(s));
        }
        Map map = new HashMap();
        long time = TimeUtil.getOffsetDateString(new Date(), -7) / 1000;
        map.put("iphoneList", new ArrayList<String>(md5Phone.values()));
        List<MessageDO> re = messageService.list(map);

        Map<String, MessageDO> mapStream = re.stream().collect(Collectors.toMap(MessageDO::getIphoneNo, a -> a, (k1, k2) -> k1));
        JSONObject jo = new JSONObject();
        for (String phone : iphoneNoList) {
            if (mapStream.containsKey(md5Phone.get(phone))) {
                jo.put("iPhone", phone);
                jo.put("status", mapStream.get(md5Phone.get(phone)).getStatus());
            }
        }
        reMap.put("data", jo);
        return new ResponseVo(ResponseVo.SUCCESS, "success", reMap);
    }


    /**
     * 号码多投状态查询
     *
     * @param RequestVo 输入：号码
     *                  输出：1是 0 否
     *                  逻辑：同一个号码在不同签名借款或者还款次数超过2次
     * @return
     */
    @ResponseBody
    @PostMapping("/listIPhoneCastStatus")
    public ResponseVo listIPhoneCastStatus(@RequestBody RequestVo<JSONObject> RequestVo) {
        //ResponseVo rvo = super.initSign(accessToken,timestamp,sign);
        ResponseVo rvo = new ResponseVo();
        if (ResponseVo.FAIL.equals(rvo.getCode())) {
            return rvo;
        }

        Map md5Phone = new HashMap();
        //明文号码集合
        List<String> iphoneNoList = (RequestVo.getData()).getJSONArray("iphoneList").toJavaList(String.class);
        for (String s : iphoneNoList) {
            md5Phone.put(s, MD5Util.md5(s));
        }
        Map map = new HashMap();
        long time = TimeUtil.getOffsetDateString(new Date(), -7) / 1000;
        map.put("iphoneList", new ArrayList<String>(md5Phone.values()));
        List<MessageDO> re = messageService.list(map);
        //分组后的map
        Map<String, List<MessageDO>> singMap = new HashMap<>();
        for (String sign : signlist) {
            List<MessageDO> list = re.stream().filter((e) -> e.getMsgContent().contains(sign))
                    .collect(Collectors.toList());
            singMap.put(sign, list);
        }
        //所有借款号码set
        Set<String> mset1 = new HashSet<>();
        Set<String> repeatSet1 = new HashSet<>();
        //还款set
        Set<String> mset2 = new HashSet<>();
        Set<String> repeatSet2 = new HashSet<>();


        Iterator it = singMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            List<MessageDO> list1 = (List<MessageDO>) pair.getValue();
            //借款
            Set<String> phone1 = list1.stream().filter((e) -> e.getMsgContent().contains("借款")).map(MessageDO::getIphoneNo).collect(Collectors.toSet());
            Set<String> fset1 = new HashSet<>(mset1);
            fset1.retainAll(phone1); //在不同签名下的有2次借款的数据
            repeatSet1.addAll(fset1);
            mset1.addAll(phone1);//并集

            //还款
            Set<String> phone2 = list1.stream().filter((e) -> e.getMsgContent().contains("还款")).map(MessageDO::getIphoneNo).collect(Collectors.toSet());
            Set<String> fset2 = new HashSet<>(mset2);
            fset2.retainAll(phone2); //在不同签名下的有2次借款的数据
            repeatSet2.addAll(fset2);
            mset2.addAll(phone2);//并集

        }

        repeatSet1.addAll(repeatSet2);//重复的号码，也就是符合条件的 repeatSetq
        JSONObject jo = new JSONObject();
        for (String s : iphoneNoList) {
            if (repeatSet1.contains(s)) {
                jo.put(s, true);
            } else {
                jo.put(s, false);
            }
        }
        reMap.put("data", jo);
        return new ResponseVo(ResponseVo.SUCCESS, "success", reMap);

    }


    @Test
    public void testTask() throws IOException {
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(20, TimeUnit.SECONDS).build();
        Request request = new Request.Builder()
                .url("http://127.0.0.1:2899/perHourJob")
                .get()
                .build();
        Response response = client.newCall(request).execute();
        System.out.println(response.code());
    }

    @GetMapping("/test")
    public void test(){
        try {
            List<String> list = Lists.newArrayList();
            list.add("lanhai");
            lendingSchedulerExecutor.execute(list);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}