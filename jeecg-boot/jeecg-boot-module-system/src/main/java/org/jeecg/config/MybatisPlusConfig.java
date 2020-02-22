package org.jeecg.config;

import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantHandler;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantSqlParser;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 单数据源配置（jeecg.datasource.open = false时生效）
 * @Author zhoujf
 *
 */
@Configuration
@MapperScan(value={"org.jeecg.modules.**.mapper*"})
public class MybatisPlusConfig {

    /**
         *  分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();

//        // 创建SQL解析器集合
//        List<ISqlParser> sqlParserList = new ArrayList<>();
//
//        // 创建租户SQL解析器
//        TenantSqlParser tenantSqlParser = new TenantSqlParser();
//
//        // 设置租户处理器
//        tenantSqlParser.setTenantHandler(new TenantHandler() {
//            @Override
//            public Expression getTenantId() {
//                // 设置当前租户ID，实际情况你可以从cookie、或者缓存中拿都行
//                return new StringValue("gci");
//            }
//
//            @Override
//            public String getTenantIdColumn() {
//                // 对应数据库租户ID的列名
//                return "tenant_id";
//            }
//
//            @Override
//            public boolean doTableFilter(String tableName) {
//                // 是否需要需要过滤某一张表
//                List<String> tableNameList = Arrays.asList("sys_user");
//                if (tableNameList.contains(tableName)){
//                    return true;
//                }
//                return false;
//            }
//        });
//
//        sqlParserList.add(tenantSqlParser);
//        paginationInterceptor.setSqlParserList(sqlParserList);

        paginationInterceptor.setLimit(-1);
        return paginationInterceptor;


//        // 设置sql的limit为无限制，默认是500
//        return new PaginationInterceptor().setLimit(-1);
    }
    
//    /**
//     * mybatis-plus SQL执行效率插件【生产环境可以关闭】
//     */
//    @Bean
//    public PerformanceInterceptor performanceInterceptor() {
//        return new PerformanceInterceptor();
//    }
    
   
}
