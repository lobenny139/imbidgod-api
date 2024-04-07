package com.imbidgod.api.controller;

import com.imbidgod.db.entity.ProductClass;
import com.imbidgod.db.exception.DataException;
import com.imbidgod.db.exception.DuplicatedException;
import com.imbidgod.db.exception.EntityNotFoundException;
import com.imbidgod.db.service.IProductClassService;
import io.swagger.annotations.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;

@ApiIgnore
@Getter
@Setter
@RestController
@Api(tags = "ProductClass Table 操作", value = "ProductClass Table 操作 API 說明")
public class ProductClassController {

    @Autowired(required=true)
    @Qualifier("productClassService")
    private IProductClassService service;

    @ApiOperation(value="以id取出單一物件(ProductClass)")
    @ApiResponses(value={
            @ApiResponse(code=200, message="成功"),
            @ApiResponse(code=400, message="失敗"),
            @ApiResponse(code=404, message="無法以id取出單一物件(ProductClass)")
    })
    @GetMapping("/productClass/{id}")
    public ProductClass get(
                            @ApiParam(required=true, value="請傳入物件(ProductClass)的id")
                            @PathVariable String id,
                            HttpServletResponse response) throws Exception {
        try{
            return getService().getEntityById( Long.parseLong(id) );
        }catch(EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @ApiOperation(value="取出所有物件(ProductClass)")
    @ApiResponses(value={
                            @ApiResponse(code=200, message="成功"),
                            @ApiResponse(code=400, message="失敗")
    })
    @GetMapping("/productClass")
    public Iterable<ProductClass> getAll(HttpServletResponse response) throws Exception  {
        try{
            return getService().getAllEntities();
        }catch(Exception e){
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @ApiOperation(value="新增單一物件(ProductClass)")
    @ApiResponses(value={
            @ApiResponse(code=200, message="成功"),
            @ApiResponse(code=409, message="數據衝突"),
            @ApiResponse(code=400, message="失敗")
    })
    @PostMapping("/productClass")
    public ProductClass create(
                                @ApiParam(required=true, value="請傳入物件(ProductClass)的 JSON 格式")
                                @RequestBody(required=true) ProductClass entity,
                                HttpServletResponse response) throws Exception {
        try {
            return getService().createEntity(entity);
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

    @ApiOperation(value="更新物件(ProductClass)")
    @ApiResponses(value={
            @ApiResponse(code=204, message="成功"),
            @ApiResponse(code=400, message="失敗"),
            @ApiResponse(code=409, message="數據衝突"),
            @ApiResponse(code=404, message="未找到物件(ProductClass)id")
    })
    @PutMapping("productClass/{id}")
    public void update(
                        @ApiParam(required=true, value="請傳入物件(ProductClass)的id")
                        @PathVariable String id,
                        @ApiParam(required=true, value="請傳入物件(ProductClass)的 JSON 格式")
                        @RequestBody(required=true) ProductClass entity,
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

    @ApiOperation(value="下架id產品分類(ProductClass)")
    @ApiResponses(value={
            @ApiResponse(code=204, message="成功"),
            @ApiResponse(code=400, message="失敗"),
            @ApiResponse(code=409, message="不能下架"),
            @ApiResponse(code=404, message="未找到物件(ProductClass)id")
    })
    @DeleteMapping("productClass/{id}/discontinue")
    public void discontinue(
                            @ApiParam(required=true, value="請傳入物件(ProductClass)的id")
                            @PathVariable String id,
                            HttpServletResponse response) throws Exception {
        try{
            long productClassId = Long.parseLong( id );
            ProductClass productClass = new ProductClass();
            productClass.setId(productClassId );
            productClass.setIsActive(0) ;
            getService().updateEntity( productClassId, productClass);
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

    @ApiOperation(value="上架id產品分類(ProductClass)")
    @ApiResponses(value={
            @ApiResponse(code=204, message="成功"),
            @ApiResponse(code=400, message="失敗"),
            @ApiResponse(code=409, message="不能上架"),
            @ApiResponse(code=404, message="未找到物件(ProductClass)id")
    })
    @PutMapping("productClass/{id}/launch")
    public void launch(
                        @ApiParam(required=true, value="請傳入物件(ProductClass)的id")
                        @PathVariable String id,
                        HttpServletResponse response) throws Exception {
        try{
            long Id = Long.parseLong( id );
            ProductClass productClass = new ProductClass();
            productClass.setId(Id );
            productClass.setIsActive(1) ;
            getService().updateEntity( Id, productClass);
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
