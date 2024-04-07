package com.imbidgod.api.controller;

import com.imbidgod.db.entity.Product;
import com.imbidgod.db.exception.DataException;
import com.imbidgod.db.exception.DuplicatedException;
import com.imbidgod.db.exception.EntityNotFoundException;
import com.imbidgod.db.service.IProductService;
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
@Api(tags = "Product Table 操作", value = "Product Table 操作 API 說明")
public class ProductController {

    @Autowired(required=true)
    @Qualifier("productService")
    private IProductService service;

    @ApiOperation(value="以id取出單一物件(Product)")
    @ApiResponses(value={
            @ApiResponse(code=200, message="成功"),
            @ApiResponse(code=400, message="失敗"),
            @ApiResponse(code=404, message="無法以id取出單一物件(Product)")
    })
    @GetMapping("/product/{id}")
    public Product get(
                        @ApiParam(required=true, value="請傳入物件(Product)的id")
                        @PathVariable String id,
                        HttpServletResponse response) throws Exception {
        try{
            return getService().getEntityById(id);
        }catch(EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @ApiOperation(value="取出所有物件(Product)")
    @ApiResponses(value={
            @ApiResponse(code=200, message="成功"),
            @ApiResponse(code=400, message="失敗")
    })
    @GetMapping("/product")
    public Iterable<Product> getAll(HttpServletResponse response) throws Exception  {
        try{
            return getService().getAllEntities();
        }catch(Exception e){
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @ApiOperation(value="新增單一物件(Product)")
    @ApiResponses(value={
            @ApiResponse(code=200, message="成功"),
            @ApiResponse(code=409, message="數據衝突"),
            @ApiResponse(code=400, message="失敗")
    })
    @PostMapping("/product")
    public Product create(
                            @ApiParam(required=true, value="請傳入物件(Product)的 JSON 格式")
                            @RequestBody(required=true) Product entity,
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

    @ApiOperation(value="更新物件(Product)")
    @ApiResponses(value={
            @ApiResponse(code=204, message="成功"),
            @ApiResponse(code=400, message="失敗"),
            @ApiResponse(code=409, message="數據衝突"),
            @ApiResponse(code=404, message="未找到物件(Product)id")
    })
    @PutMapping("product/{id}")
    public void update(
                        @ApiParam(required=true, value="請傳入物件(Product)的id")
                        @PathVariable String id,
                        @ApiParam(required=true, value="請傳入物件(Product)的 JSON 格式")
                        @RequestBody(required=true) Product entity,
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

    @ApiOperation(value="下架id產品(Product)")
    @ApiResponses(value={
            @ApiResponse(code=204, message="成功"),
            @ApiResponse(code=400, message="失敗"),
            @ApiResponse(code=409, message="不能下架"),
            @ApiResponse(code=404, message="未找到物件(Product)id")
    })
    @DeleteMapping("product/{id}/discontinue")
    public void discontinue(
                            @ApiParam(required=true, value="請傳入物件(Product)的id")
                            @PathVariable String id,
                            HttpServletResponse response) throws Exception {
        try{
            Product product = new Product();
            product.setId(id);
            product.setIsActive(0);
            getService().updateEntity( id, product);
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

    @ApiOperation(value="上架id產品(Product)")
    @ApiResponses(value={
            @ApiResponse(code=204, message="成功"),
            @ApiResponse(code=400, message="失敗"),
            @ApiResponse(code=409, message="不能上架"),
            @ApiResponse(code=404, message="未找到物件(Product)id")
    })
    @PutMapping("product/{id}/launch")
    public void launch(
                        @ApiParam(required=true, value="請傳入物件(Product)的id")
                        @PathVariable String id,
                        HttpServletResponse response) throws Exception {
        try{
            Product product = new Product();
            product.setId(id);
            product.setIsActive(1);
            getService().updateEntity( id, product);
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
