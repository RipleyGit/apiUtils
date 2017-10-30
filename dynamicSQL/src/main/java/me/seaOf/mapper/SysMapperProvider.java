package me.seaOf.mapper;

import static org.apache.ibatis.jdbc.SqlBuilder.BEGIN;
import static org.apache.ibatis.jdbc.SqlBuilder.DELETE_FROM;
import static org.apache.ibatis.jdbc.SqlBuilder.SQL;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.scripting.xmltags.ForEachSqlNode;
import org.apache.ibatis.scripting.xmltags.MixedSqlNode;
import org.apache.ibatis.scripting.xmltags.SqlNode;
import org.apache.ibatis.scripting.xmltags.StaticTextSqlNode;

import com.github.abel533.mapper.MapperProvider;
import com.github.abel533.mapperhelper.EntityHelper;
import com.github.abel533.mapperhelper.MapperHelper;

import javax.persistence.Table;

public class SysMapperProvider extends MapperProvider {

    public SysMapperProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    public SqlNode deleteByIDS(MappedStatement ms) {
        Class<?> entityClass = getSelectReturnType(ms);
        Set<EntityHelper.EntityColumn> entityColumns = EntityHelper.getPKColumns(entityClass);
        EntityHelper.EntityColumn column = null;
        for (EntityHelper.EntityColumn entityColumn : entityColumns) {
            column = entityColumn;
            break;
        }
        
        List<SqlNode> sqlNodes = new ArrayList<SqlNode>();
        // 开始拼sql
        BEGIN();
        // delete from table
        DELETE_FROM(tableName(entityClass));
        // 得到sql
        String sql = SQL();
        // 静态SQL部分
        sqlNodes.add(new StaticTextSqlNode(sql + " WHERE " + column.getColumn() + " IN "));
        // 构造foreach sql
        SqlNode foreach = new ForEachSqlNode(ms.getConfiguration(), new StaticTextSqlNode("#{"
                + column.getProperty() + "}"), "ids", "index", column.getProperty(), "(", ")", ",");
        sqlNodes.add(foreach);
        return new MixedSqlNode(sqlNodes);
    }

    //mybatis介绍sql工具

    /**
     * sqlNode :sql存储工具
     *
     * @param ms mybatis内置对象
     * @return
     * string sql = select count(*) from xxx
     *
     * 问题如何获取表名
     * 思路：
     *  如何获取当前执行的mapper的路径
     *  截取字符串
     *  通过反射可以获取class
     *  获取类发父级接口 SysMapper<Item>
     *  获取泛型参数
     *  获取@Table注解名
     *  获取name属性值
     *  拼接sql
     */
    public SqlNode findCount(MappedStatement ms){
        //获取当前mapper执行发方法全路径
        String methodPath = ms.getId();
        //截取接口的全路径
        String interFacePath = methodPath.substring(0, methodPath.lastIndexOf("."));

        try {
            //利用反射获取目标类
            Class<?> targetClass = Class.forName(interFacePath);
            //获取当前接口的上级
            Type[] parentTypes=targetClass.getGenericInterfaces();
            //获取父级type
            Type parentType = parentTypes[0];
            //判断当前上级接口是否一个泛型接口
            if(parentType instanceof ParameterizedType){
                //将type类型转化为泛型类型，调用对应的方法
                ParameterizedType parameterizedType = (ParameterizedType) parentType;
                //获取参数type类型数组
                Type[] argsTypes = parameterizedType.getActualTypeArguments();
                //获取参数的class类型
                Class<?> argsClass = (Class<?>) argsTypes[0];
                //判断当前class类型是否是@Table注解
                if(argsClass.isAnnotationPresent(Table.class)){
                    //获取table注解
                    Table table = argsClass.getAnnotation(Table.class);
                    //获取name属性值
                    String tableName = table.name();
                    //拼接sql语句
                    String  sql = "select count(*) from "+tableName;

                    //准备sqlNode对象
                    SqlNode sqlNode = new StaticTextSqlNode(sql);

                    return sqlNode;

                }
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}
