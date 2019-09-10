package com.neuedu.controller.portal;

import com.neuedu.common.ServerResponse;
import com.neuedu.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product/")
@CrossOrigin
public class ProductController {

    @Autowired
    IProductService productService;

    /**
     * 前台-商品详情
     */
    @RequestMapping(value = "detail.do")
    public ServerResponse detail(Integer productId){
        return productService.detail_portal(productId);
    }

    /**
     * 前台-产品搜索，动态排序
     */
    @RequestMapping(value = "list.do")
    public ServerResponse list(@RequestParam(required = false) Integer categoryId,
                               @RequestParam(required = false) String keyword,
                               @RequestParam(required = false,defaultValue = "1") Integer pageNum,
                               @RequestParam(required = false,defaultValue = "10") Integer pageSize,
                               @RequestParam(required = false,defaultValue = "") String orderBy){
        if(categoryId==null||categoryId.equals("")){
            categoryId=null;
        }
        if(keyword==null||keyword.equals("")){
            keyword=null;
        }

        return productService.list_portal(categoryId,keyword,pageNum,pageSize,orderBy);
    }

    /**
     * 前台-产品搜索 轮播产品
     */
    @RequestMapping(value = "bannerlist.do")
    public ServerResponse bannerlist(){
        return productService.bannerlist();
    }

    /**
     * 前台-产品搜索 热卖产品
     */
    @RequestMapping(value = "hotlist.do")
    public ServerResponse hotlist(){
        return productService.hotlist();
    }

    /**
     * 前台-产品搜索 新品
     */
    @RequestMapping(value = "newlist.do")
    public ServerResponse newlist(){
        return productService.newlist();
    }

}
