package com.dao;

import com.entity.YuyueyishengEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import org.apache.ibatis.annotations.Param;
import com.entity.vo.YuyueyishengVO;
import com.entity.view.YuyueyishengView;


/**
 * 预约医生
 * 
 * @author 
 * @email 
 * @date 2023-03-12 23:08:30
 */
public interface YuyueyishengDao extends BaseMapper<YuyueyishengEntity> {
	
	List<YuyueyishengVO> selectListVO(@Param("ew") Wrapper<YuyueyishengEntity> wrapper);
	
	YuyueyishengVO selectVO(@Param("ew") Wrapper<YuyueyishengEntity> wrapper);
	
	List<YuyueyishengView> selectListView(@Param("ew") Wrapper<YuyueyishengEntity> wrapper);

	List<YuyueyishengView> selectListView(Pagination page,@Param("ew") Wrapper<YuyueyishengEntity> wrapper);
	
	YuyueyishengView selectView(@Param("ew") Wrapper<YuyueyishengEntity> wrapper);
	

}
