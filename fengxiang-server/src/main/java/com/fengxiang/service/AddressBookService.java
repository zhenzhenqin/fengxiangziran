package com.fengxiang.service;

import com.fengxiang.entity.AddressBook;

import java.util.List;

public interface AddressBookService {

    /**
     * 新增地址
     * @param addressBook
     */
    void add(AddressBook addressBook);

    /**
     * 查询地址
     * @param addressBook
     * @return
     */
    List<AddressBook> list(AddressBook addressBook);

    /**
     * 根据id查询地址详情
     * @param id
     * @return
     */
    AddressBook getAddressById(Long id);

    /**
     * 修改地址
     * @param addressBook
     */
    void update(AddressBook addressBook);

    /**
     * 设置默认地址
     * @param addressBook
     */
    void setDefault(AddressBook addressBook);

    /**
     * 查询默认地址
     * @param currentId
     * @return
     */
    AddressBook getDefault(Long currentId);

    /**
     * 根据id删除地址
     * @param id
     */
    void deleteById(Long id);
}
