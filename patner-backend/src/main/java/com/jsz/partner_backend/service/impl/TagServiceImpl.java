package com.jsz.partner_backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsz.partner_backend.model.domain.Tag;
import com.jsz.partner_backend.service.TagService;
import com.jsz.partner_backend.mapper.TagMapper;
import org.springframework.stereotype.Service;

/**
* @author 12099
* @description 针对表【tag】的数据库操作Service实现
* @createDate 2024-11-26 20:28:49
*/
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
    implements TagService{

}




