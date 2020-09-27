package com.liuqi.springbootsecurityredisnginx.core;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * 类说明 <br>
 * <p>
 * 构造说明 :
 * <pre>
 *
 * </pre>
 *
 * @author : alexliu
 * @version v1.0 , Create at 3:24 下午 2020/9/18
 */
public class BaseDao<Entity, PK extends Serializable> extends SqlSessionDaoSupport implements Serializable{

    private static final long serialVersionUID = 3674013335380598212L;

    private static final String PKG_SEP = ".";

    private static final String _SELECT_BY_PK = ".selectByPK";

    private static final String _SELECT_BY_SELECTIVE = ".selectSelective";

    private static final String _SELECT_LIST_BY_SELECTIVE = ".selectListSelective";

    private static final String _SELECT_ALL = ".selectAll";

    @Resource
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        super.setSqlSessionFactory(sqlSessionFactory);
    }

    private String getNameSpace() {

        String daoPkg = this.getClass().getPackage().getName();
        String daoName = this.getClass().getSimpleName();

        return daoPkg + PKG_SEP + daoName;
    }

    /**
     * 根据主键 ID 查询唯一数据
     * @param id {@link PK} 主键
     * @return {@link Entity}
     */
    public Entity selectByPK(PK id) {
        return getSqlSession().selectOne(this.getNameSpace() + _SELECT_BY_PK, id);
    }

    /**
     * 根据传入的对象，有条件的查询唯一的数据
     * @param entity {@link Entity} 反射对象
     * @return {@link Entity}
     */
    public Entity selectSelective(Entity entity) {
        return getSqlSession().selectOne(this.getNameSpace() + _SELECT_BY_SELECTIVE, entity);
    }

    /**
     * 根据传入的对象，有条件的查询数据集合
     * @param entity {@link Entity} 反射对象
     * @return {@link List &lt;Entity&gt; }
     */
    public List<Entity> selectListSelective(Entity entity) {
        return getSqlSession().selectList(this.getNameSpace() + _SELECT_LIST_BY_SELECTIVE, entity);
    }

    /**
     * 查询所有数据
     * @return {@link List &lt;Entity&gt; }
     */
    public List<Entity> selectAll() {
        return getSqlSession().selectList(this.getNameSpace() + _SELECT_ALL);
    }
}
