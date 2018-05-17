package com.JWmes.project.system.dict.mapper;

import java.util.List;

import com.JWmes.project.system.dict.entity.DictTypeEntity;
import org.apache.ibatis.annotations.Mapper;


/**
 * 字典表 数据层
 * 
 * @author zhong
 */
@Mapper
public interface DictTypeMapper
{
    /**
     * 根据条件分页查询字典类型
     * 
     * @param dictType 字典类型信息
     * @return 字典类型集合信息
     */
    public List<DictTypeEntity> selectDictTypeList(DictTypeEntity dictType);

    /**
     * 根据字典类型ID查询信息
     * 
     * @param dictId 字典类型ID
     * @return 字典类型
     */
    public DictTypeEntity selectDictTypeById(Long dictId);
    
    /**
     * 通过字典ID删除字典信息
     * 
     * @param dictId 字典ID
     * @return 结果
     */
    public int deleteDictTypeById(Long dictId);

    /**
     * 批量删除字典类型
     * 
     * @param ids 需要删除的数据
     * @return 结果
     */
    public int batchDeleteDictType(Long[] ids);

    /**
     * 批量删除字典数据
     * 
     * @param ids 需要删除的数据
     * @return 结果
     */
    public int batchDeleteDictData(Long[] ids);

    /**
     * 新增字典类型信息
     * 
     * @param dictType 字典类型信息
     * @return 结果
     */
    public int insertDictType(DictTypeEntity dictType);

    /**
     * 修改字典类型信息
     * 
     * @param dictType 字典类型信息
     * @return 结果
     */
    public int updateDictType(DictTypeEntity dictType);

    /**
     * 校验字典类型称是否唯一
     * 
     * @param dictType 字典类型
     * @return 结果
     */
    public DictTypeEntity checkDictTypeUnique(String dictType);
}
