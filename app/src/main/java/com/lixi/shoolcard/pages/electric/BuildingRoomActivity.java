package com.lixi.shoolcard.pages.electric;

import android.os.Bundle;
import android.widget.ListPopupWindow;

import com.google.gson.reflect.TypeToken;
import com.lixi.shoolcard.base.BaseActivity;
import com.lixi.shoolcard.constant.SchoolCardConstant;
import com.lixi.shoolcard.pages.adapter.InputAdapter;
import com.lixi.shoolcard.pages.adapter.RechardFilterAdaper;
import com.lixi.shoolcard.utils.DES3Util;
import com.lixi.shoolcard.utils.GsonUtil;
import com.lixi.shoolcard.network.MyCallbcak;
import com.lixi.shoolcard.network.NetWorkUitl;
import com.lixi.shoolcard.network.ShopApi;
import com.lixi.shoolcard.utils.TimeUtil;
import com.lixi.shoolcard.network.vo.ApiRequest;
import com.lixi.shoolcard.network.vo.ApiResult;
import com.lixi.shoolcard.network.vo.BuildingRequest;
import com.lixi.shoolcard.network.vo.BuildingVo;
import com.lixi.shoolcard.network.vo.FloorRequest;
import com.lixi.shoolcard.network.vo.FloorVo;
import com.lixi.shoolcard.network.vo.RoomRequest;
import com.lixi.shoolcard.network.vo.RoomVo;
import com.lixi.shoolcard.pages.model.BuildingRoomVo;
import com.lixi.shoolcard.pages.model.FloorRoomVo;
import com.tencent.mmkv.MMKV;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import retrofit2.Call;

public abstract class BuildingRoomActivity extends BaseActivity {
    //楼栋下拉框
    protected ListPopupWindow mOptions;
    //楼栋adapter
    protected InputAdapter mAdapter;
    //楼层下拉框
    protected ListPopupWindow  fOptions;
    //楼层adapter
    protected InputAdapter fAdapter;
    //房间adapter
    RechardFilterAdaper adapter;
    //缓存
    protected MMKV kv = MMKV.defaultMMKV();
    List<BuildingRoomVo> cacheData = new ArrayList<>();
    Map<Integer,List<RoomVo>> roomCache = new HashMap<>();
    protected List<String> buildings = new ArrayList<>();
    protected List<String> floors = new ArrayList<>();
    protected RoomVo selectedRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBuildingRoom();
    }

    protected void initBuildingRoom(){
        mOptions = new ListPopupWindow (this);
        mOptions.setAnimationStyle(-1);
        mAdapter = new InputAdapter(this);
        mOptions.setAdapter(mAdapter);
        fOptions = new ListPopupWindow (this);
        fOptions.setAnimationStyle(-1);
        fAdapter = new InputAdapter(this);
        fOptions.setAdapter(fAdapter);
        List<String> bus = getBu();
        if(bus.size() > 0) {
            mAdapter.setList(bus);
            mOptions.setSelection(0);
        }
    }

    protected List<String> getBu() {
        if(buildings.size() > 0){
            return buildings;
        }

        //cacheData先从缓存获取
        String cacheBuildingStr = kv.decodeString(SchoolCardConstant.CACHE_BUILDING);
        if(cacheBuildingStr != null){
            Type listType = new TypeToken<ArrayList<BuildingRoomVo>>(){}.getType();
            List<BuildingRoomVo> list = GsonUtil.getGson().fromJson(cacheBuildingStr,listType);
            if(list != null){
                if(TimeUtil.isToday(list.get(0).getCacheDate())){
                    cacheData = list;
                    //房间也从缓存加载一下
                    String cacheRoomStr = kv.decodeString(SchoolCardConstant.CACHE_ROOM);
                    if(cacheRoomStr != null){
                        Type mapType = new TypeToken<Map<Integer,List<RoomVo>>>(){}.getType();
                        Map<Integer,List<RoomVo>> rooms = GsonUtil.getGson().fromJson(cacheRoomStr,mapType);
                        if(rooms != null){
                            roomCache = rooms;
                        }
                    }
                    return getBuildName();
                }
            }
        }
        if(cacheData.size() == 0) {
            //缓存没有，从服务端加载
            showLoading();
            ShopApi shopApi = NetWorkUitl.getRetrofit().create(ShopApi.class);
            Call<ApiResult<List<BuildingVo>>> call = shopApi.getBuildingList(NetWorkUitl.initRequestBody(initBuildingRequest()));
            call.enqueue(new MyCallbcak<ApiResult<List<BuildingVo>>>() {
                @Override
                protected void setData(ApiResult<List<BuildingVo>> result) {
                    String decryptStr = DES3Util.desDecrypt(result.getData());
                    Type listType = new TypeToken<ArrayList<BuildingVo>>(){}.getType();
                    List<BuildingVo> buildingList = GsonUtil.getGson().fromJson(decryptStr,listType);
                    if(buildingList != null && buildingList.size() > 0) {
                        for (BuildingVo building : buildingList) {
                            BuildingRoomVo vo = new BuildingRoomVo();
                            vo.setSchoolId(building.getSchoolId());
                            vo.setBuildingId(building.getBuildingId());
                            vo.setBuildingName(building.getBuildingName());
                            if (cacheData.size() == 0) {
                                vo.setCacheDate(new Date());
                            }
                            cacheData.add(vo);
                        }
                        //缓存设置
                        String cacheStr = GsonUtil.getGson().toJson(cacheData);
                        kv.encode(SchoolCardConstant.CACHE_BUILDING, cacheStr);
                        mAdapter.setList(getBuildName());
                        mOptions.setSelection(0);
                        hideLoading();
                    }
                }

                @Override
                protected void onFinish() {
                    super.onFinish();
                    hideLoading();
                }
            });
//            //测试数据begin
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(new Date());
//            //calendar.add(Calendar.DAY_OF_MONTH, -1); //当前时间增加/减少若干天
//            Date date = calendar.getTime();
//            for(int i = 0; i < 20; i++){
//                BuildingRoomVo vo = new BuildingRoomVo();
//                vo.setSchoolId(1);
//                vo.setBuildingId(i+1);
//                vo.setBuildingName("楼栋"+(i+1));
//                if(cacheData.size() == 0) {
//                    vo.setCacheDate(date);
//                }
//                cacheData.add(vo);
//            }
//            String cacheStr = GsonUtil.getGson().toJson(cacheData);
//            kv.encode(SchoolCardConstant.CACHE_BUILDING,  cacheStr);
//            //测试数据end
        }
        return getBuildName();
    }

    private List<String> getBuildName(){
        if(cacheData != null && cacheData.size() > 0) {
            for(BuildingRoomVo buildVo:cacheData){
                buildings.add(buildVo.getBuildingName());
            }
        }
        return buildings;
    }

    protected void getFloors(int index) {
        BuildingRoomVo buildVo = cacheData.get(index);
        //缓存有就直接返回
        if(buildVo != null && buildVo.getFloorRoomList() != null){
            setFloorDtaToView(getFloorName(buildVo.getFloorRoomList()));
            return;
        }

        //加载房间
        //showLoading();
        ShopApi shopApi =  NetWorkUitl.getRetrofit().create(ShopApi.class);
        Call<ApiResult<List<FloorVo>>> call =  shopApi.getFloorList(NetWorkUitl.initRequestBody(initFloorRequest(buildVo.getBuildingId())));
        call.enqueue(new MyCallbcak<ApiResult<List<FloorVo>>>() {
            @Override
            protected void setData(ApiResult<List<FloorVo>> result) {
                if(result.getData() != null) {
                    String decryptStr = DES3Util.desDecrypt(result.getData());
                    Type listType = new TypeToken<ArrayList<FloorVo>>(){}.getType();
                    List<FloorVo> floors = GsonUtil.getGson().fromJson(decryptStr,listType);
                    if(floors != null && floors.size() > 0) {
                        List<FloorRoomVo> floorRoomVos = new ArrayList<>();
                        for (FloorVo floor : floors) {
                            //服务端数据不对 这边先过滤一下错误的
                            if(!buildVo.getBuildingId().equals(floor.getBuildingId())){
                                continue;
                            }
                            FloorRoomVo vo = new FloorRoomVo();
                            vo.setSchoolId(floor.getSchoolId());
                            vo.setBuildingId(floor.getBuildingId());
                            vo.setFloorId(floor.getFloorId());
                            vo.setFloorName(floor.getFloorName());
                            floorRoomVos.add(vo);
                        }
                        buildVo.setFloorRoomList(floorRoomVos);
                        //缓存重新设置一下
                        String cacheStr = GsonUtil.getGson().toJson(cacheData);
                        kv.encode(SchoolCardConstant.CACHE_BUILDING,  cacheStr);
                        //设置下拉框数据
                        setFloorDtaToView(getFloorName(floorRoomVos));
                    }
                }
            }

            @Override
            protected void onFinish() {
                super.onFinish();
                //hideLoading();
            }
        });


        //测试数据begin
//        List<RoomVo> roomVos = new ArrayList<>();
//        for(int i = 0; i < 100; i++){
//            RoomVo roomVo = new RoomVo();
//            roomVo.setRoomId(i+1);
//            roomVo.setRoomName("房间"+buildVo.getBuildingId()+""+(i+1));
//            roomVo.setBuildingId(buildVo.getBuildingId());
//            roomVo.setBuildingName(buildVo.getBuildingName());
//            roomVo.setAllAmp("1000");
//            roomVo.setUsedAmp(buildVo.getBuildingId()+""+(i+1));
//            roomVos.add(roomVo);
//        }
//        buildVo.setRoomList(roomVos);
        //测试数据end
    }

    private List<String> getFloorName(List<FloorRoomVo> list){
        floors = new ArrayList<>();
        if(list != null && list.size() > 0) {
            for (FloorRoomVo floorVo : list) {
                floors.add(floorVo.getFloorName());
            }
        }
        return floors;
    }

    protected void getRooms(BuildingRoomVo buildVo,int index) {
        //BuildingRoomVo buildVo = cacheData.get(index);
        if(buildVo==null||buildVo.getFloorRoomList()==null){
            return;
        }
        FloorRoomVo floorRoomVo = buildVo.getFloorRoomList().get(index);
        List<RoomVo> cacheRooms = roomCache.get(floorRoomVo.getFloorId());
        //缓存有就直接返回
        if(cacheRooms != null ){
            setRoomDataToView(cacheRooms);
            return;
        }


        //加载房间
//        showLoading();
        ShopApi shopApi =  NetWorkUitl.getRetrofit().create(ShopApi.class);
        Call<ApiResult<List<RoomVo>>> call =  shopApi.getRoomList(NetWorkUitl.initRequestBody(initRoomRequest(buildVo.getBuildingId(),floorRoomVo.getFloorId())));
        call.enqueue(new MyCallbcak<ApiResult<List<RoomVo>>>() {
            @Override
            protected void setData(ApiResult<List<RoomVo>> result) {
                if(result.getData() != null) {
                    String decryptStr = DES3Util.desDecrypt(result.getData());
                    Type listType = new TypeToken<ArrayList<RoomVo>>(){}.getType();
                    List<RoomVo> rooms = GsonUtil.getGson().fromJson(decryptStr,listType);
                    //过滤服务端错误的数据
                    List<RoomVo> filterRooms = rooms.stream().filter(
                            room -> floorRoomVo.getFloorId().equals(room.getFloorId())
                    ).collect(Collectors.toList());
                    //设置下拉框数据
                    setRoomDataToView(filterRooms);
                    roomCache.put(floorRoomVo.getFloorId(),filterRooms);
                    //缓存重新设置一下
                    String cacheStr = GsonUtil.getGson().toJson(roomCache);
                    kv.encode(SchoolCardConstant.CACHE_ROOM,  cacheStr);
//                    hideLoading();
                }
            }

            @Override
            protected void onFinish() {
                super.onFinish();
//                hideLoading();
            }
        });

        //测试数据begin
//        List<RoomVo> roomVos = new ArrayList<>();
//        for(int i = 0; i < 100; i++){
//            RoomVo roomVo = new RoomVo();
//            roomVo.setRoomId(i+1);
//            roomVo.setRoomName("房间"+buildVo.getBuildingId()+""+(i+1));
//            roomVo.setBuildingId(buildVo.getBuildingId());
//            roomVo.setBuildingName(buildVo.getBuildingName());
//            roomVo.setAllAmp("1000");
//            roomVo.setUsedAmp(buildVo.getBuildingId()+""+(i+1));
//            roomVos.add(roomVo);
//        }
//        buildVo.setRoomList(roomVos);
        //测试数据end
    }
    protected abstract void setFloorDtaToView(List<String> list);

    protected abstract void setRoomDataToView(List<RoomVo> list);

    protected void dismissOption(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mOptions != null) {
                    mOptions.dismiss();
                }
                if(fOptions != null) {
                    fOptions.dismiss();
                }
            }
        });
    }

    private ApiRequest<BuildingRequest> initBuildingRequest(){
        ApiRequest<BuildingRequest> request = new ApiRequest<>();
        BuildingRequest buildingRequest = new BuildingRequest();
        buildingRequest.setSchoolId(kv.decodeInt(SchoolCardConstant.SCHOOL_ID,-1));
        request.setRequestData(buildingRequest);
        System.out.println("请求参数：："+GsonUtil.getGson().toJson(request));
        return request;
    }

    private ApiRequest<FloorRequest> initFloorRequest(Integer buildingId){
        ApiRequest<FloorRequest> request = new ApiRequest<>();
        FloorRequest floorRequest = new FloorRequest();
        floorRequest.setSchoolId(kv.decodeInt(SchoolCardConstant.SCHOOL_ID,-1));
        floorRequest.setBuildingId(buildingId);
        request.setRequestData(floorRequest);
        System.out.println("请求参数：："+GsonUtil.getGson().toJson(request));
        return request;
    }

    protected ApiRequest<RoomRequest> initRoomRequest(Integer buildingId,Integer floorId){
        ApiRequest<RoomRequest> request = new ApiRequest<>();
        RoomRequest roomRequest = new RoomRequest();
        roomRequest.setSchoolId(kv.decodeInt(SchoolCardConstant.SCHOOL_ID,-1));
        roomRequest.setBuildingId(buildingId);
        roomRequest.setFloorId(floorId);
        if(selectedRoom != null) {
            roomRequest.setRoomId(selectedRoom.getRoomId());
        }
        request.setRequestData(roomRequest);
        System.out.println("请求参数：："+GsonUtil.getGson().toJson(request));
        return request;
    }

}
