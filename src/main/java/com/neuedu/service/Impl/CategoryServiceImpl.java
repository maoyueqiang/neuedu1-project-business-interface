package com.neuedu.service.Impl;

import com.google.common.collect.Sets;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.CategoryMapper;
import com.neuedu.pojo.Category;
import com.neuedu.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Service
public class CategoryServiceImpl implements ICategoryService{

    @Autowired
    CategoryMapper categoryMapper;


    /**
     * 获取品类的子节点
     */
    @Override
    public ServerResponse get_category(Integer categoryId) {
        //非空校验
        if(categoryId==null){
            return ServerResponse.createServerResponseByFail("参数不能为空");
        }

        //根据categoeyId查询类别
        if(categoryId!=0){
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            if (category == null) {
                return ServerResponse.createServerResponseByFail("查询的类别不存在");
            }
        }

        //查询子类别
        List<Category> categoryList = categoryMapper.findChildCategory(categoryId);

        //返回结果
        return ServerResponse.createServerResponseBySuccess(categoryList);
    }


    /**
     * 获取当前分类id及递归子节点categoryId
     */
    @Override
    public ServerResponse get_deep_category(Integer categoryId) {
        //非空校验
        if(categoryId==null){
            return ServerResponse.createServerResponseByFail("参数不能为空");
        }

        //查询类别
        Set<Category> categorySet = Sets.newHashSet();
        categorySet=findAllChildCategory(categorySet,categoryId);

        //遍历删重
        Set<Integer> integerSet = Sets.newHashSet();
        Iterator<Category> categoryIterator = categorySet.iterator();
        while (categoryIterator.hasNext()){
            Category category = categoryIterator.next();
            integerSet.add(category.getId());
        }

        return ServerResponse.createServerResponseBySuccess(integerSet);
    }
    //递归查询
    private Set<Category> findAllChildCategory(Set<Category> categorySet,Integer categoryId){
        //首先判断自己是否存在
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if(category!=null){

            categorySet.add(category);
        }

        //查找平级子节点
        List<Category> categoryList = categoryMapper.findChildCategory(categoryId);
        if(categoryList!=null&&categoryList.size()>0){
            for(Category c:categoryList){
                findAllChildCategory(categorySet,c.getId());
            }
        }
        return categorySet;
    }
}
