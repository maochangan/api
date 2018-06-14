package com.sshy.api.dao;

import com.sshy.api.bean.ArCharManagement;
import com.sshy.api.bean.ArThemeManagement;
import com.sshy.api.bean.CompanyUser;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface CompanyUserDao {


    //商户数据操作接口方法
    @Insert("insert into c_unionid(c_unionid) values(#{cUnionid})")
    Integer addUnionId(@Param("cUnionid") String cUnionid);

    @Select("select * from c_unionid where c_unionid = #{cUnionId , jdbcType=VARCHAR}")
    Map<String , Object> selectByCunId(String cUnionId);

    @Select("select * from c_unionid where id = #{id , jdbcType=INTEGER}")
    Map<String, Object> selectById(Integer id);

    @Delete("delete from c_unionid where id = #{id , jdbcType=INTEGER}")
    Integer deleteCunionId(Integer id);

    @Update("update from c_unionid set c_unionid = #{cUnionid , jdbcType=VARCHAR} where id = #{id , jdbcType=INTEGER}")
    Integer updateByParKey(@Param("id") Integer id, @Param("cUnionid") String cUnionid);

    Integer addCompanyUser(CompanyUser companyUser);



    //主题管理接口方法
    List<ArThemeManagement> selectAll();

    Integer updateArThemeSerByParKey(ArThemeManagement arThemeManagement);

    Integer addARTheme(ArThemeManagement arThemeManagement);

    @Select("SELECT * FROM ar_theme_management WHERE id = #{id , jdbcType=INTEGER}")
    ArThemeManagement getARThemeById(Integer id);

    @Update("UPDATE ar_theme_management SET delete_num = 0 WHERE id = #{id , jdbcType=INTEGER}")
    Integer deleteARThemeById(Integer id);


    //识别图管理接口方法
    @Insert("INSERT INTO ar_chart_management(ar_chart_image_id , ar_chart_model_url , c_user_id , ar_chart_create_time , delete_num)" +
            "VALUES(#{arChartImageId , jdbcType=INTEGER},#{arChartModelUrl , jdbcType=VARCHAR},#{cUserId , jdbcType=INTEGER },#{arChartCreateTime , jdbcType=TIMESTAMP} , #{deleteNum})")
    Integer addARChart(ArCharManagement arCharManagement);

    List<ArCharManagement> getAllARChart();

    Integer updateARChartSerByParKry(ArCharManagement arCharManagement);

    @Select("SELECT * FROM ar_chart_management WHERE ar_chart_image_id = #{arChartImageId , jdbcType=VARCHAR}")
    ArCharManagement getARTargetByImageId(String targetId);

    @Update("UPDATE ar_chart_management SET ar_chart_model_url = #{arChartModelUrl , jdbcType=VARCHAR} WHERE ar_chart_image_id = #{arChartImageId , jdbcType=VARCHAR}")
    Integer updateARTarget(@Param("arChartModelUrl") String arChartModelUrl ,@Param("arChartImageId") String arChartImageId);

    @Delete("DELETE FROM ar_chart_management WHERE ar_chart_image_id = #{arChartImageId , jdbcType=VARCHAR}")
    Integer removeTarget(String targetId);
}
