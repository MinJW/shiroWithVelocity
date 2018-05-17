package com.JWmes.project.system.dict.service.impl;

import java.util.List;

import com.JWmes.common.utils.StringUtils;
import com.JWmes.common.utils.security.ShiroUtils;
import com.JWmes.project.system.dict.entity.DictDataEntity;
import com.JWmes.project.system.dict.mapper.DictDataMapper;
import com.JWmes.project.system.dict.service.DictDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 字典 业务层处理
 * 
 * @author zhong
 */
@Service("dictDataService")
public class DictDataServiceImpl implements DictDataService
{
    @Autowired
    private DictDataMapper dictDataMapper;

    /**
     * 根据条件分页查询字典数据
     * 
     * @param dictData 字典数据信息
     * @return 字典数据集合信息
     */
    @Override
    public List<DictDataEntity> selectDictDataList(DictDataEntity dictData)
    {
        return dictDataMapper.selectDictDataList(dictData);
    }

    /**
     * 根据字典数据ID查询信息
     * 
     * @param dictCode 字典数据ID
     * @return 字典数据
     */
    @Override
    public DictDataEntity selectDictDataById(Long dictCode)
    {
        return dictDataMapper.selectDictDataById(dictCode);
    }

    /**
     * 通过字典ID删除字典数据信息
     * 
     * @param dictCode 字典数据ID
     * @return 结果
     */
    @Override
    public int deleteDictDataById(Long dictCode)
    {
        return dictDataMapper.deleteDictDataById(dictCode);
    }

    /**
     * 批量删除字典数据
     * 
     * @param ids 需要删除的数据
     * @return 结果
     */
    @Override
    public int batchDeleteDictData(Long[] ids)
    {
        return dictDataMapper.batchDeleteDictData(ids);
    }

    /**
     * 保存字典数据信息
     * 
     * @param dictData 字典数据信息
     * @return 结果
     */
    @Override
    public int saveDictData(DictDataEntity dictData)
    {
        Long dictCode = dictData.getDictCode();
        if (StringUtils.isNotNull(dictCode))
        {
            dictData.setUpdateBy(ShiroUtils.getLoginName());
            return dictDataMapper.updateDictData(dictData);
        }
        else
        {
            dictData.setCreateBy(ShiroUtils.getLoginName());
            return dictDataMapper.insertDictData(dictData);
        }
    }

}
