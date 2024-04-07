package com.imbidgod.api.controller;

import com.imbidgod.db.entity.ActivityDetail;
import com.imbidgod.db.exception.DataException;
import com.imbidgod.db.exception.DuplicatedException;
import com.imbidgod.db.exception.EntityNotFoundException;
import com.imbidgod.db.service.IActivityDetailService;
import io.swagger.annotations.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;

@Getter
@Setter
@RestController
@Api(tags = "ActivityDetail Table 操作", value = "ActivityDetail Table 操作 API 說明")
public class ActivityDetailController {
    @Autowired(required=true)
    @Qualifier("activityDetailService")
    private IActivityDetailService service;

    @ApiOperation(value="以id取出單一物件(ActivityDetail)")
    @ApiResponses(value={
            @ApiResponse(code=200, message="成功"),
            @ApiResponse(code=400, message="失敗"),
            @ApiResponse(code=404, message="無法以id取出單一物件(ActivityDetail)")
    })
    @GetMapping("/activityDetail/{id}")
    public ActivityDetail get(
            @ApiParam(required=true, value="請傳入物件(ActivityDetail)的id")
            @PathVariable String id,
            HttpServletResponse response) throws Exception {
        try{
            long objectId = Long.parseLong(id);
            return getService().getEntityById(objectId);
        }catch(EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @ApiOperation(value="新增單一物件(Activity)")
    @ApiResponses(value={
            @ApiResponse(code=200, message="成功"),
            @ApiResponse(code=409, message="數據衝突"),
            @ApiResponse(code=400, message="失敗")
    })
    @PostMapping("/activityDetail")
    public ActivityDetail create(
            @ApiParam(required=true, value="請傳入物件(Activity)的 JSON 格式")
            @RequestBody(required=true) ActivityDetail entity,
            HttpServletResponse response) throws Exception {
        try {
            if(entity.getJoinMemberId() == 0){
                throw new Exception("JoinMemberId can't not be null.");
            }
            return getService().createEntity(entity);
        }catch(EntityNotFoundException e){
            //404
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(DataException e){
            //409
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }catch(DuplicatedException e){
            //409
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }catch(Exception e){
            e.printStackTrace();
            //400
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @ApiOperation(value="以activityId取出所有物件(ActivityDetail)")
    @ApiResponses(value={
            @ApiResponse(code=200, message="成功"),
            @ApiResponse(code=400, message="失敗")
    })
    @GetMapping("/getActivityDetailsByActivityId/{activityId}")
    public Iterable<ActivityDetail> getByActivityId(
            @ApiParam(required=true, value="請傳入物件(ActivityDetail)的activityId")
            @PathVariable String activityId,
            HttpServletResponse response) throws Exception  {
        try{
            return getService().getEntitiesByActivityId(Long.parseLong(activityId));
        }catch(Exception e){
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @ApiOperation(value="以activityId取出得標資料(ActivityDetail)")
    @ApiResponses(value={
            @ApiResponse(code=200, message="成功"),
            @ApiResponse(code=400, message="失敗")
    })
    @GetMapping("/getHasGetBiddersByActivityId/{activityId}")
    public Iterable<ActivityDetail> getHasGetBiddersByActivityId(
            @ApiParam(required=true, value="請傳入物件(ActivityDetail)的activityId")
            @PathVariable String activityId,
            HttpServletResponse response) throws Exception  {
        try{
            return getService().getEntitiesByHasGetBidder(Long.parseLong(activityId));
        }catch(Exception e){
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }


}
