package com.fengxiang.service.impl;

import com.fengxiang.context.BaseContext;
import com.fengxiang.entity.AddressBook;
import com.fengxiang.mapper.AddressBookMapper;
import com.fengxiang.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl implements AddressBookService {

    @Autowired
    private AddressBookMapper addressBookMapper;

    /**
     * 新增地址
     * @param addressBook
     */
    @Override
    public void add(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBook.setIsDefault(0); //设置为非默认地址
        addressBookMapper.insert(addressBook);
    }
}
