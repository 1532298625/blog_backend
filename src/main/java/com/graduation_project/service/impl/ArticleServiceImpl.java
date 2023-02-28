package com.graduation_project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.graduation_project.dao.entity.*;
import com.graduation_project.dao.mapper.ArticleMapper;
import com.graduation_project.dao.mapper.TimeViewCountMapper;
import com.graduation_project.dao.repository.ArticleTagsAndCategoryRepository;
import com.graduation_project.dao.repository.CategoryRepository;
import com.graduation_project.dao.repository.TagsRepository;
import com.graduation_project.service.ArticleService;
import com.graduation_project.service.SysUserService;
import com.graduation_project.service.TimeViewCountService;
import com.graduation_project.service.UserBehaviorService;
import com.graduation_project.utils.BeanCopyUtils;
import com.graduation_project.vo.ArticleVo;
import com.graduation_project.vo.ResponseResult;
import com.graduation_project.vo.UserVo;
import com.graduation_project.vo.params.ArticleParams;
import com.graduation_project.vo.params.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * (Article)表服务实现类
 *
 * @author makejava
 * @since 2022-05-24 13:34:27
 */
@Service("articleService")
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private ArticleTagsAndCategoryRepository articleTagsAndCategory;
    @Autowired
    private TagsRepository tagsRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private SysUserService userService;
    @Autowired
    private ThreadService threadService;
    @Autowired
    private UserBehaviorService userBehaviorService;
    @Autowired
    private ArticleMapper articleMapper;

    /**
     * 新增
     * @param articleParams
     * @return
     */
    @Override
    public ResponseResult insertArticle(ArticleParams articleParams) {
        //获取当前用户 id
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //把前端数据copy到表里
        Article article = BeanCopyUtils.copyBean(articleParams, Article.class);
        article.setAuthorId(loginUser.getUser().getId());
        baseMapper.insert(article);

        //再插入 分类和标签 数据 到 mongodb
        ArticleTagsAndCategory tagsAndCategory = new ArticleTagsAndCategory();
        tagsAndCategory.setArticleId(article.getId());
        tagsAndCategory.setTagsId(articleParams.getTagsId());
        tagsAndCategory.setCategoryId(articleParams.getCategoryId());
        articleTagsAndCategory.save(tagsAndCategory);
        return ResponseResult.success("文章发布成功");
    }

    /**
     * 编辑
     * @param articleParams
     * @return
     */
    @Override
    public ResponseResult updateArticle(ArticleParams articleParams) {
        //获取当前用户 id
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long authorId = loginUser.getUser().getId();
        //看看这个文章是不是我的
//        if (articleParams.getUser().getId() != authorId.longValue()) {
//            //如果不是 不可以编辑
//            return ResponseResult.fail("权限不足");
//        }
        //TODO 如果是 管理员 也可以编辑
        Article article = BeanCopyUtils.copyBean(articleParams, Article.class);
        //把前端数据 copy 到 表里
        baseMapper.updateById(article);

        //再插入 分类和标签 数据 到 mongodb
        List<String> categoryId = articleParams.getCategoryId();
        List<String> tagsId = articleParams.getTagsId();
        if (!CollectionUtils.isEmpty(categoryId) || !CollectionUtils.isEmpty(tagsId)) {
            ArticleTagsAndCategory tagsAndCategory = articleTagsAndCategory.findByArticleId(articleParams.getId());
            if (!CollectionUtils.isEmpty(categoryId)) {
                tagsAndCategory.setCategoryId(categoryId);
            }
            if (!CollectionUtils.isEmpty(tagsId)) {
                tagsAndCategory.setTagsId(tagsId);
            }
            articleTagsAndCategory.save(tagsAndCategory);
        }

        return ResponseResult.success("编辑成功");
    }

    @Autowired
    private TimeViewCountService timeViewCountService;
    /**
     * 根据 id 查找
     * @param articleId
     * @return
     */
    @Override
    public ResponseResult findArticleById(String articleId) {
        //文章信息
        LambdaQueryWrapper<UserBehavior> queryWrapper = new LambdaQueryWrapper<>();
        LambdaUpdateWrapper<UserBehavior> updateWrapper = new LambdaUpdateWrapper<>();
        SecurityContext context = SecurityContextHolder.getContext();
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userid = loginUser.getUser().getId();
        queryWrapper.eq(UserBehavior::getUserid,userid);
        queryWrapper.eq(UserBehavior::getArticleid,articleId);
        UserBehavior ub = userBehaviorService.getOne(queryWrapper);
        if(ub!=null){
            updateWrapper.eq(UserBehavior::getUserid,userid);
            updateWrapper.eq(UserBehavior::getArticleid,articleId);
            updateWrapper.set(UserBehavior::getCount,ub.getCount()+0.5);
            userBehaviorService.update(updateWrapper);
        }else{
            UserBehavior ub2 = new UserBehavior(userid,articleId,0.5);
            userBehaviorService.save(ub2);
        }
        QueryWrapper<TimeViewcount> wrapper = new QueryWrapper<>();
        SimpleDateFormat formatter= new SimpleDateFormat("M月dd日");
        Date date = new Date(System.currentTimeMillis());
        String time = formatter.format(date);
        wrapper.eq("time",time);
        TimeViewcount timeViewcount = timeViewCountService.getOne(wrapper);
        UpdateWrapper<TimeViewcount> viewcountUpdateWrapper = new UpdateWrapper<>();
        if(timeViewcount!=null){
            viewcountUpdateWrapper.eq("time",time);
            viewcountUpdateWrapper.set("count",timeViewcount.getCount()+0.5);
            timeViewCountService.update(viewcountUpdateWrapper);
        }else{
            TimeViewcount viewcount = new TimeViewcount(time, 0.5);
            timeViewCountService.save(viewcount);
        }
        ArticleVo articleVo = setTagsAndUser(articleId);
        Article article = BeanCopyUtils.copyBean(articleVo, Article.class);
        threadService.updateViewCount(baseMapper,article);
        return ResponseResult.success("文章查询成功",articleVo);
    }

    private ArticleVo setTagsAndUser(String articleId) {
        Article article = baseMapper.selectById(articleId);


        //作者信息
        User byId = userService.getById(article.getAuthorId());
        UserVo userVo = BeanCopyUtils.copyBean(byId, UserVo.class);

        //标签和分类信息
        ArticleTagsAndCategory categoryAndTags = articleTagsAndCategory.findByArticleId(articleId);

        if (Objects.isNull(categoryAndTags)) {}
        List<String> tagsId = categoryAndTags.getTagsId();
        Iterable<Tags> tags = tagsRepository.findAllById(tagsId);

        List<String> categoryId = categoryAndTags.getCategoryId();
        Iterable<Category> allCategory = categoryRepository.findAllById(categoryId);

        //设置标签 和 分类
        article.setTagsList(Lists.newArrayList(tags));
        article.setCategoryList(Lists.newArrayList(allCategory));
        //设置用户信息
        ArticleVo articleVo = BeanCopyUtils.copyBean(article, ArticleVo.class);
        articleVo.setUser(userVo);
        return articleVo;
    }

    /**
     * 删除帖子
     */
    @Override
    public ResponseResult deleteArticle(String articleId) {
        // 先看看 这个文章 是不是 这个用户的
        Article article = baseMapper.selectById(articleId);
        Long authorId = article.getAuthorId();
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // TODO 如果是管理员 也可以删除
        if (loginUser.getUser().getId() != authorId.longValue()) {
            return ResponseResult.fail("删除失败");
        }
        baseMapper.deleteById(articleId);
        //还要把对应的标签信息删除
        articleTagsAndCategory.deleteByArticleId(articleId);
        return ResponseResult.success("删除成功");
    }
    @Autowired
    private CategoryService categoryService;

    @Override
    public ResponseResult listArticle(PageParams params) {

        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        if (params.getCategoryId().length()!=0){
            List<ArticleTagsAndCategory> articles = categoryService.getArticleByCategoryId2(params);
            for (ArticleTagsAndCategory article : articles) {
                queryWrapper.eq(Article::getId,article.getArticleId()).or();
            }
        }else if(params.getTagId().length()!=0){
            List<ArticleTagsAndCategory> articles = categoryService.getArticleByTagId(params);
            for (ArticleTagsAndCategory article : articles) {
                queryWrapper.eq(Article::getId,article.getArticleId()).or();
            }
        }else{
            if (StringUtils.hasText(params.getKeyword())) {
                queryWrapper.like(Article::getTitle,params.getKeyword())
                        .or()
                        .like(Article::getSummary,params.getKeyword())
                        .or().like(Article::getContentHtml,params.getKeyword());
            }
        }
        //排序规则
        queryWrapper.orderByDesc(Article::getWeight,Article::getCreateDate,Article::getUpdateTime);
        //分页数据
        IPage<Article> page = new Page<>(params.getPage(),params.getPageSize());
        List<Article> records = baseMapper.selectPage(page, queryWrapper).getRecords();
        //填充 作者 标签 信息
        List<ArticleVo> articleVoList = records.stream().map(article -> setTagsAndUser(article.getId())).collect(Collectors.toList());
        Map<String,Object> res = new HashMap<String,Object>();
        res.put("articleData",articleVoList);
        int num = articleMapper.getArticleCount();
        res.put("num",num);
        return ResponseResult.success("文章列表",res);
    }

    @Override
    public ResponseResult hotArticle() {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getWeight,Article::getViewCounts,Article::getCommentCounts);
        queryWrapper.last("limit 5");
        List<Article> articles = baseMapper.selectList(queryWrapper);
        //填充 作者 标签 信息
        List<ArticleVo> articleVoList = articles.stream().map(article -> setTagsAndUser(article.getId())).collect(Collectors.toList());
        return ResponseResult.success(articleVoList);
    }

    @Override
    public ResponseResult recentArticle() {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getCreateDate,Article::getViewCounts,Article::getCommentCounts);
        queryWrapper.last("limit 5");
        List<Article> articles = baseMapper.selectList(queryWrapper);
        //填充 作者 标签 信息
        List<ArticleVo> articleVoList = articles.stream().map(article -> setTagsAndUser(article.getId())).collect(Collectors.toList());
        return ResponseResult.success(articleVoList);
    }
}

