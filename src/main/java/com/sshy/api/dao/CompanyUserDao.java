package com.sshy.api.dao;

import com.sshy.api.bean.*;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface CompanyUserDao {


    //商户数据操作接口方法
    @Insert("insert into c_unionid(c_unionid) values(#{cUnionid})")
    Integer addUnionId(@Param("cUnionid") String cUnionid);

    @Select("select * from c_unionid where c_unionid = #{cUnionId , jdbcType=VARCHAR}")
    Map<String, Object> selectByCunId(String cUnionId);

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

    ArThemeManagement getARThemeById(Integer id);

    @Update("UPDATE ar_theme_management SET delete_num = 1 WHERE id = #{id , jdbcType=INTEGER}")
    Integer deleteARThemeById(Integer id);


    //识别图管理接口方法
    @Insert("INSERT INTO ar_chart_management(ar_chart_image_id , ar_model_id , c_user_id , ar_chart_create_time , delete_num , out_url , ar_theme_id , ar_chart_image_url)" +
            "VALUES(#{arChartImageId , jdbcType=INTEGER},#{arModelId , jdbcType=INTEGER},#{cUserId , jdbcType=INTEGER },#{arChartCreateTime , jdbcType=TIMESTAMP} , #{deleteNum , jdbcType=INTEGER} , #{outUrl , jdbcType=VARCHAR} , #{arThemeId , jdbcType=INTEGER} , #{arChartImageUrl , jdbcType=VARCHAR})")
    Integer addARChart(ArCharManagement arCharManagement);

    List<ArCharManagement> getAllARChart();

    Integer updateARChartSerByParKry(ArCharManagement arCharManagement);

    @Select("SELECT * FROM ar_chart_management WHERE ar_chart_image_id = #{arChartImageId , jdbcType=VARCHAR}")
    ArCharManagement getARTargetByImageId(String targetId);


    Integer updateARTarget(ArCharManagement arCharManagement);

    @Delete("DELETE FROM ar_chart_management WHERE ar_chart_image_id = #{arChartImageId , jdbcType=VARCHAR}")
    Integer removeTarget(String targetId);


    //展示内容管理
    Integer addARModel(ArModelManagement arModelManagement);

    List<ArModelManagement> getARModleList();

    ArModelManagement getARModel(Integer id);

    @Update("UPDATE ar_model_management SET delete_num=0 WHERE id = #{id , jdbcType=INTEGER}")
    Integer removeARModel(Integer id);

    Integer updateArModelSerByParKey(ArModelManagement arModelManagement);

    @Select("SELECT count(*) FROM ar_chart_management WHERE ar_model_id = #{id , jdbcType=INTEGER}")
    Integer getCountImageNum(Integer id);


    /*素材管理*/
    Integer addFile(FileTable fileTable);

    List<FileTable> getFileList(@Param("fileType") String fileType);

    @Delete("DELETE FROM file_table WHERE id = #{id , jdbcType=INTEGER}")
    Integer deleteFile(Integer id);

    FileTable selectFileById(Integer id);
}
