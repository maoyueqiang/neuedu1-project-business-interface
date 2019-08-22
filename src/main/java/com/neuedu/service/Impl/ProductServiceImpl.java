package com.neuedu.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.neuedu.VO.ProductDetailVO;
import com.neuedu.VO.ProductListVO;
import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.CategoryMapper;
import com.neuedu.dao.ProductMapper;
import com.neuedu.pojo.Category;
import com.neuedu.pojo.Product;
import com.neuedu.service.ICategoryService;
import com.neuedu.service.IProductService;
import com.neuedu.utils.DateUtils;
import com.neuedu.utils.PropertiesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Set;

@Service

public class ProductServiceImpl implements IProductService{

    @Autowired
    ProductMapper productMapper;
    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    ICategoryService categoryService;

    /**
     * 新增或更新产品
     */
    @Override
    public ServerResponse addOrUpdate(Product product) {
        //非空校验
        if(product==null){
            return ServerResponse.createServerResponseByFail("参数不能为空");
        }

        //设置商品主图
        String subImages=product.getSubImages();
        if(subImages!=null&&!subImages.equals("")){
            String [] subImageArr = subImages.split(",");
            if(subImageArr.length>0){
                //设置主图
                product.setMainImage(subImageArr[0]);
            }
        }

        //判断添加还是更新
        if(product.getId()==null){
            //添加
            int result=productMapper.insert(product);
            if(result>0){
                return ServerResponse.createServerResponseBySuccess("添加成功");
            }
            else{
                return ServerResponse.createServerResponseByFail("添加失败");
            }
        }
        else{
            //更新
            int result = productMapper.updateByPrimaryKey(product);
            if(result>0){
                return ServerResponse.createServerResponseBySuccess("更新成功");
            }
            else{
                return ServerResponse.createServerResponseByFail("更新失败");
            }
        }

        //返回结果

    }

    /**
     * 产品上下架
     */
    @Override
    public ServerResponse set_sale_status(Product product) {
        //非空校验
        if(product.getId()==null){
            return ServerResponse.createServerResponseByFail("商品id不能为空");
        }
        if(product.getStatus()==null){
            return ServerResponse.createServerResponseByFail("商品状态码不能为空");
        }

        //更新产品状态
        int count = productMapper.updateByPrimaryKey(product);
        if(count>0){
            return ServerResponse.createServerResponseBySuccess("商品状态更新成功");
        }

        //返回结果
        return ServerResponse.createServerResponseByFail("商品状态更新失败");
    }

    /**
     * 查看商品详情
     */
    @Override
    public ServerResponse detail(Integer productId) {
        //非空校验
        if(productId==null){
            return ServerResponse.createServerResponseByFail("商品id不能为空");
        }

        //查询商品
        Product product = productMapper.selectByPrimaryKey(productId);
        if(product==null){
            return  ServerResponse.createServerResponseByFail("商品不存在");
        }

        //转换为vo
        ProductDetailVO productDetailVO = assembleProductDetailVO(product);

        //返回结果
        return ServerResponse.createServerResponseBySuccess(productDetailVO);
    }

    @Override
    public ServerResponse list(Integer pageNum, Integer pageSize) {
        Page page = PageHelper.startPage(pageNum,pageSize);//自动为sql语句加limit (pageNum-1)*pageSize,pageSize
        //查询
        List<Product> productList = productMapper.selectAll();
        //productList -> productListVOList
        List<ProductListVO> productListVOList = Lists.newArrayList();
        if(productList!=null&&productList.size()>0){
            for (Product product:productList) {
                ProductListVO productListVO = assembleProductListVO(product);
                productListVOList.add(productListVO);
            }
        }

        //step2:分页显示
        PageInfo pageInfo = new PageInfo(page);

        return ServerResponse.createServerResponseBySuccess(pageInfo);
    }

    @Override
    public ServerResponse search( Integer productId,String productName, Integer pageNum, Integer pageSize) {
        //step1:搜索商品

        //模糊查询
        if(productName!=null&&!productName.equals("")){
            productName="%"+productName+"%";
        }
        Page page = PageHelper.startPage(pageNum,pageSize);//自动为sql语句加limit
        //查询
        List<Product> productList = productMapper.findByIdOrName(productId,productName);
        //productList -> productListVOList
        List<ProductListVO> productListVOList = Lists.newArrayList();
        if(productList!=null&&productList.size()>0){
            for (Product product:productList) {
                ProductListVO productListVO = assembleProductListVO(product);
                productListVOList.add(productListVO);
            }
        }

        //step2:分页显示
        PageInfo pageInfo = new PageInfo(page);

        return ServerResponse.createServerResponseBySuccess(pageInfo);
    }

    @Override
    public ServerResponse detail_portal(Integer productId) {
        //非空校验
        if(productId==null){
            return ServerResponse.createServerResponseByFail("商品id不能为空");
        }

        //查询商品
        Product product = productMapper.selectByPrimaryKey(productId);
        if(product==null){
            return  ServerResponse.createServerResponseByFail("商品不存在");
        }

        //校验商品状态
        if(product.getStatus()!= Const.ProductStatusEnum.PRODUCT_ONLINE.getCode()){
            return ServerResponse.createServerResponseByFail("商品已下架或删除");
        }

        //转换为vo
        ProductDetailVO productDetailVO = assembleProductDetailVO(product);

        //返回结果
        return ServerResponse.createServerResponseBySuccess(productDetailVO);
    }

    @Override
    public ServerResponse list_portal(Integer categoryId, String keyword, Integer pageNum, Integer pageSize, String orderBy) {
        //参数校验 categoryId与keyword不能同时为空
        if(categoryId==null&&keyword==null){
            return ServerResponse.createServerResponseByFail("参数错误");
        }

        Set<Integer> integerSet=null;
        //categoryId
        if(categoryId!=null){
            Category category=categoryMapper.selectByPrimaryKey(categoryId);
            if(category==null&&(keyword==null||keyword.equals(""))){
                //没有商品
                Page page = PageHelper.startPage(pageNum,pageSize);
                List<ProductListVO> productListVOList = Lists.newArrayList();
                PageInfo pageInfo = new PageInfo(page);
                return ServerResponse.createServerResponseBySuccess(pageInfo);
            }

            //
            ServerResponse serverResponse = categoryService.get_deep_category(categoryId);
            integerSet = Sets.newHashSet();
            if(serverResponse.isSuccess()){
                integerSet = (Set<Integer>) serverResponse.getData();
            }

        }

        //keyword
        if(keyword!=null&&!keyword.equals("")){
            keyword="%"+keyword+"%";
        }
        Page page;
        if(orderBy.equals("")){
            page=PageHelper.startPage(pageNum,pageSize);
        }
        else{
            String[] orderByArr = orderBy.split("_");
            if(orderByArr.length>1){
                page=PageHelper.startPage(pageNum,pageSize,orderByArr[0]+" "+orderByArr[1]);
            }
            else{
                page=PageHelper.startPage(pageNum,pageSize);
            }
        }

        List<Product> productList = productMapper.searchProduct(integerSet,keyword,orderBy);
        List<ProductListVO> productListVOList = Lists.newArrayList();
        if(productList!=null&&productList.size()>0){
            for (Product p :productList) {
                ProductListVO productListVO = assembleProductListVO(p);
                productListVOList.add(productListVO);
            }
        }

        //分页
        PageInfo pageInfo = new PageInfo(page);
        pageInfo.setList(productListVOList);

        //返回
        return ServerResponse.createServerResponseBySuccess(pageInfo);
    }

    private ProductDetailVO assembleProductDetailVO(Product product){
        ProductDetailVO productDetailVO=new ProductDetailVO();
        productDetailVO.setId(product.getId());
        productDetailVO.setCategoryId(product.getCategoryId());

        int id=product.getCategoryId();
        Category category = categoryMapper.selectByPrimaryKey(id);
        if(category==null){
            productDetailVO.setParentCategoryId(0);
        }else
            productDetailVO.setParentCategoryId(category.getParentId());

        productDetailVO.setName(product.getName());
        productDetailVO.setSubtitle(product.getSubtitle());

        productDetailVO.setImageHost(PropertiesUtils.readByKey("imageHost"));

        productDetailVO.setMainImage(product.getMainImage());
        productDetailVO.setSubImages(product.getSubImages());
        productDetailVO.setPrice(product.getPrice());
        productDetailVO.setStock(product.getStock());
        productDetailVO.setStatus(product.getStatus());

        productDetailVO.setCreateTime(DateUtils.dateTOStr(product.getCreateTime()));
        productDetailVO.setUpdateTime(DateUtils.dateTOStr(product.getUpdateTime()));

        productDetailVO.setDetail(product.getDetail());



        return productDetailVO;
    }

    private ProductListVO assembleProductListVO(Product product){
        ProductListVO productListVO=new ProductListVO();
        productListVO.setId(product.getId());
        productListVO.setCategoryId(product.getCategoryId());
        productListVO.setName(product.getName());
        productListVO.setSubtitle(product.getSubtitle());
        productListVO.setMainImage(product.getMainImage());
        productListVO.setPrice(product.getPrice());
        productListVO.setStatus(product.getStatus());


        return productListVO;
    }
}
