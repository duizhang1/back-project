package com.zhf.webfont.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhf.common.exception.Asserts;
import com.zhf.common.util.TransactionUtil;
import com.zhf.webfont.bo.StoreListWithIsStoreParam;
import com.zhf.webfont.mapper.ArticleStoreListRelationMapper;
import com.zhf.webfont.po.ArticleStoreListRelation;
import com.zhf.webfont.po.StoreList;
import com.zhf.webfont.po.User;
import com.zhf.webfont.service.StoreListService;
import com.zhf.webfont.mapper.StoreListMapper;
import com.zhf.webfont.util.JwtTokenUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
* @author 10276
* @description 针对表【wf_store_list】的数据库操作Service实现
* @createDate 2023-01-31 01:59:17
*/
@Service
public class StoreListServiceImpl extends ServiceImpl<StoreListMapper, StoreList>
    implements StoreListService{

    @Resource
    private JwtTokenUtil jwtTokenUtil;
    @Resource
    private StoreListMapper storeListMapper;
    @Resource
    private ArticleStoreListRelationMapper articleStoreListRelationMapper;

    @Override
    public List<StoreListWithIsStoreParam> getStoreListWithIsStore(String articleId) {
        User currentUser = jwtTokenUtil.getCurrentUserFromHeader();
        return storeListMapper.getStoreListWithIsStore(currentUser.getUuid(),articleId);
    }

    @Override
    public void createStoreList(StoreList storeList) {
        checkStoreListParam(storeList);
        User currentUser = jwtTokenUtil.getCurrentUserFromHeader();

        storeList.setUserId(currentUser.getUuid());
        storeList.setCreateTime(new Date());
        storeList.setUpdateTime(new Date());
        storeList.setArticleNum(0);

        int insert = storeListMapper.insert(storeList);
        Asserts.failIsTrue(insert < 1,"添加失败");
    }

    @Override
    public void updateArticleStore(Map<String,Object> param) {
        String articleId = (String) param.get("articleId");
        List<String> list = (List<String>) param.get("updateList");
        Set<String> set = new HashSet<>(list);

        User currentUser = jwtTokenUtil.getCurrentUserFromHeader();
        List<StoreList> storeLists = getUserStoreList(currentUser.getUuid());
        List<String> storeIdLists = new ArrayList<>();
        for (StoreList storeList : storeLists) {
            storeIdLists.add(storeList.getUuid());
        }

        QueryWrapper<ArticleStoreListRelation> wrapper = new QueryWrapper<>();
        wrapper.in("store_list_id",storeIdLists)
                .eq("article_id",articleId);
        List<ArticleStoreListRelation> articleStoreListRelations = articleStoreListRelationMapper.selectList(wrapper);
        Set<String> articleStoreListRelationSet = new HashSet<>();
        for (ArticleStoreListRelation articleStoreListRelation : articleStoreListRelations) {
            articleStoreListRelationSet.add(articleStoreListRelation.getStoreListId());
        }

        List<String> insertList = new ArrayList<>();
        List<String> deleteList = new ArrayList<>();
        for (ArticleStoreListRelation articleStoreListRelation : articleStoreListRelations) {
            if (!set.contains(articleStoreListRelation.getStoreListId())){
                deleteList.add(articleStoreListRelation.getStoreListId());
            }
        }
        for (String s : list) {
            if (!articleStoreListRelationSet.contains(s)){
                insertList.add(s);
            }
        }

        TransactionUtil.transaction(()->{
            if (deleteList.size() > 0){
                QueryWrapper<ArticleStoreListRelation> wrapper1 = new QueryWrapper<>();
                wrapper1.in("store_list_id",deleteList)
                        .eq("article_id",articleId);
                int delete = articleStoreListRelationMapper.delete(wrapper1);
                Asserts.failIsTrue(delete < deleteList.size(),"收藏失败");
            }
            int incr = 0;
            for (String storeListId : insertList) {
                ArticleStoreListRelation articleStoreListRelation = new ArticleStoreListRelation();
                articleStoreListRelation.setArticleId(articleId);
                articleStoreListRelation.setUpdateTime(new Date());
                articleStoreListRelation.setCreateTime(new Date());
                articleStoreListRelation.setStoreListId(storeListId);
                incr += articleStoreListRelationMapper.insert(articleStoreListRelation);
            }
            Asserts.failIsTrue(incr < insertList.size(),"收藏失败");
        });
    }

    private List<StoreList> getUserStoreList(String userId) {
        QueryWrapper<StoreList> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId);
        return storeListMapper.selectList(wrapper);
    }

    private void checkStoreListParam(StoreList storeList) {
        Asserts.failIsTrue(StrUtil.isEmpty(storeList.getName()),"收藏夹名不为空");
        Asserts.failIsTrue(StrUtil.isEmpty(storeList.getState()),"状态不为空");

        QueryWrapper<StoreList> wrapper = new QueryWrapper<>();
        wrapper.eq("name",storeList.getName());
        StoreList storeList1 = storeListMapper.selectOne(wrapper);
        Asserts.failIsTrue(storeList1 != null,"该收藏夹名已存在");
    }
}




