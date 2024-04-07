package com.imbidgod.api.controller;

import com.imbidgod.db.entity.OrderDetail;
import com.imbidgod.db.exception.DataException;
import com.imbidgod.db.exception.DuplicatedException;
import com.imbidgod.db.exception.EntityNotFoundException;
import com.imbidgod.db.service.IOrderDetailService;
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
@Api(tags = "OrderDetail Table 操作", value = "OrderDetail Table 操作 API 說明")
public class OrderDetailController {
    @Autowired(required=true)
    @Qualifier("orderDetailService")
    private IOrderDetailService service;


    @ApiOperation(value="以id取出單一物件(OrderDetail)")
    @ApiResponses(value={
            @ApiResponse(code=200, message="成功"),
            @ApiResponse(code=400, message="失敗"),
            @ApiResponse(code=404, message="無法以id取出單一物件(Order)")
    })
    @GetMapping("/orderDetail/{id}")
    public OrderDetail get(
                            @ApiParam(required=true, value="請傳入物件(OrderDetail)的id")
                            @PathVariable String id,
                            HttpServletResponse response) throws Exception {
        try{
            return getService().getEntityById( Long.parseLong( id ) );
        }catch(EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @ApiOperation(value="取出所有物件(OrderDetail)")
    @ApiResponses(value={
                            @ApiResponse(code=200, message="成功"),
                            @ApiResponse(code=400, message="失敗")
    })
    @GetMapping("/orderDetail")
    public Iterable<OrderDetail> getAll(HttpServletResponse response) throws Exception  {
        try{
            return getService().getAllEntities();
        }catch(Exception e){
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }


    @ApiOperation(value="新增單一物件(Order)")
    @ApiResponses(value={
            @ApiResponse(code=200, message="成功"),
            @ApiResponse(code=409, message="數據衝突"),
            @ApiResponse(code=400, message="失敗")
    })
    @PostMapping("/orderDetail")
    public OrderDetail create(
                                @ApiParam(required=true, value="請傳入物件(OrderDetail)的 JSON 格式")
                                @RequestBody(required=true) OrderDetail entity,
                                HttpServletResponse response) throws Exception {
        try {
            return getService().createEntity(entity);
        }catch(DataException e){
            //409
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }catch(DuplicatedException e){
            //409
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }catch(EntityNotFoundException e){
            //404
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(Exception e){
            e.printStackTrace();
            //400
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }


    @ApiOperation(value="更新物件(Order)")
    @ApiResponses(value={
            @ApiResponse(code=204, message="成功"),
            @ApiResponse(code=400, message="失敗"),
            @ApiResponse(code=409, message="數據衝突"),
            @ApiResponse(code=404, message="未找到物件(Order)id")
    })
    @PutMapping("orderDetail/{id}")
    public void update(
                        @ApiParam(required=true, value="請傳入物件(OrderDetail)的id")
                        @PathVariable String id,
                        @ApiParam(required=true, value="請傳入物件(OrderDetail)的 JSON 格式")
                        @RequestBody(required=true) OrderDetail entity,
                        HttpServletResponse response) {
        try{
            Long objectId = Long.parseLong(id);
            getService().updateEntity(objectId, entity);
        }catch(DataException e){
            //409
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }catch(DuplicatedException e){
            //409
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }catch(EntityNotFoundException e){
            //404
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(Exception e){
            e.printStackTrace();
            //400
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @ApiOperation(value="以id刪除物件(OrderDetail)")
    @ApiResponses(value={
            @ApiResponse(code=204, message="成功"),
            @ApiResponse(code=400, message="失敗"),
            @ApiResponse(code=409, message="不能下架"),
            @ApiResponse(code=404, message="未找到物件(ProductClass)id")
    })
    @DeleteMapping("orderDetail/{id}")
    public void delete(
            @ApiParam(required=true, value="請傳入物件(OrderDetail)的id")
            @PathVariable String id,
            HttpServletResponse response) throws Exception {
        try{
            Long objectId = Long.parseLong(id);
            getService().deleteEntity(objectId);
        }catch(DataException e){
            //409
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }catch(DuplicatedException e){
            //409
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }catch(EntityNotFoundException e){
            //404
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch(Exception e){
            e.printStackTrace();
            //400
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @ApiOperation(value="以orderId取出所有物件(OrderDetail)")
    @ApiResponses(value={
            @ApiResponse(code=200, message="成功"),
            @ApiResponse(code=400, message="失敗")
    })
    @GetMapping("/getOrderDetailsByOrderId/{orderId}")
    public Iterable<OrderDetail> getByOrderId(
            @ApiParam(required=true, value="請傳入物件(OrderDetail)的orderId")
            @PathVariable String orderId,
            HttpServletResponse response) throws Exception  {
        try{
            return getService().getEntitiesByOrderId(orderId);
        }catch(Exception e){
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }


}
