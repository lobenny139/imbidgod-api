package com.imbidgod.api.controller;

import com.imbidgod.db.entity.Order;
import com.imbidgod.db.exception.DataException;
import com.imbidgod.db.exception.DuplicatedException;
import com.imbidgod.db.exception.EntityNotFoundException;
import com.imbidgod.db.service.IOrderService;
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
@Api(tags = "Order Table 操作", value = "Order Table 操作 API 說明")
public class OrderController {
    @Autowired(required=true)
    @Qualifier("orderService")
    private IOrderService service;

    @ApiOperation(value="以id取出單一物件(Order)")
    @ApiResponses(value={
            @ApiResponse(code=200, message="成功"),
            @ApiResponse(code=400, message="失敗"),
            @ApiResponse(code=404, message="無法以id取出單一物件(Order)")
    })
    @GetMapping("/order/{id}")
    public Order get(
                        @ApiParam(required=true, value="請傳入物件(Order)的id")
                        @PathVariable String id,
                        HttpServletResponse response) throws Exception {
        try{
            return getService().getEntityById( id );
        }catch(EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @ApiOperation(value="取出所有物件(Order)")
    @ApiResponses(value={
            @ApiResponse(code=200, message="成功"),
            @ApiResponse(code=400, message="失敗")
    })
    @GetMapping("/order")
    public Iterable<Order> getAll(HttpServletResponse response) throws Exception  {
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
    @PostMapping("/order")
    public Order create(
                        @ApiParam(required=true, value="請傳入物件(Order)的 JSON 格式")
                        @RequestBody(required=true) Order entity,
                        HttpServletResponse response) throws Exception {
        try {
            return getService().createEntity(entity);
        }catch(DataException e){
            //409
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }catch(DuplicatedException e){
            //409
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }catch(EntityNotFoundException e) {
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
    @PutMapping("order/{id}")
    public void update(
                        @ApiParam(required=true, value="請傳入物件(Order)的id")
                        @PathVariable String id,
                        @ApiParam(required=true, value="請傳入物件(Order)的 JSON 格式")
                        @RequestBody(required=true) Order entity,
                        HttpServletResponse response) {
        try{
            getService().updateEntity(id, entity);
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



}
