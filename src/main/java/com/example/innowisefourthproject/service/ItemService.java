package com.example.innowisefourthproject.service;

import com.example.innowisefourthproject.entity.Item;
import com.example.innowisefourthproject.exception.ServiceException;

import java.util.List;
import java.util.Optional;

public interface ItemService {
    boolean add(String name, String description, String price) throws ServiceException;

    boolean update(long id, String name, String description, String price) throws ServiceException;

    boolean delete(long id) throws ServiceException;

    List<Item> findAll() throws ServiceException;

    Optional<Item> findById(long id) throws ServiceException;
}
