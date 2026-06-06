package com.example.innowisefourthproject.service.impl;

import com.example.innowisefourthproject.dao.ItemDao;
import com.example.innowisefourthproject.dao.impl.ItemDaoImpl;
import com.example.innowisefourthproject.entity.Item;
import com.example.innowisefourthproject.exception.DaoException;
import com.example.innowisefourthproject.exception.ServiceException;
import com.example.innowisefourthproject.service.ItemService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class ItemServiceImpl implements ItemService {

    private final Logger logger = LogManager.getLogger(ItemServiceImpl.class);
    private static final ItemServiceImpl instance = new ItemServiceImpl();
    private ItemDao itemDao = ItemDaoImpl.getInstance();

    private ItemServiceImpl() {
    }

    public static ItemServiceImpl getInstance() {
        return instance;
    }

    ItemServiceImpl(ItemDao itemDao) {
        this.itemDao = itemDao;
    }

    @Override
    public boolean add(String name, String description, String price) throws ServiceException {
        BigDecimal itemPrice = parsePrice(price);
        validateItemData(name, itemPrice);

        try {
            Item item = new Item(
                    name.trim(),
                    description == null ? "" : description.trim(),
                    itemPrice
            );
            return itemDao.insert(item);
        } catch (DaoException e) {
            logger.error("Error adding item");
            throw new ServiceException("Failed of creating item", e);
        }
    }

    @Override
    public boolean update(long id, String name, String description, String price) throws ServiceException {
        if (id <= 0) {
            throw new ServiceException("Incorrect item id");
        }
        BigDecimal itemPrice = parsePrice(price);
        validateItemData(name, itemPrice);
        try {
            Item item = new Item(
                    id,
                    name.trim(),
                    description == null ? "" : description.trim(),
                    itemPrice
            );

            return itemDao.update(item);
        } catch (DaoException e) {
            logger.error("Error of update item by id");
            throw new ServiceException("Could not update item", e);
        }
    }


    @Override
    public boolean delete(long id) throws ServiceException {
        if (id <= 0) {
            throw new ServiceException("Incorrect item id");
        }
        try {
            return itemDao.delete(id);
        } catch (DaoException e) {
            logger.error("Error of delete item by id");
            throw new ServiceException("Could not delete item by id", e);
        }
    }

    @Override
    public List<Item> findAll() throws ServiceException {
        try {
            return itemDao.findAll();
        } catch (DaoException e) {
            logger.error("Error of finding all items");
            throw new ServiceException("Failed to find all items");
        }
    }

    @Override
    public Optional<Item> findById(long id) throws ServiceException {
        try {
            return itemDao.findById(id);
        } catch (DaoException e) {
            logger.error("Error of findById item");
            throw new ServiceException("Failed to findById items", e);
        }
    }

    private void validateItemData(String name, BigDecimal price) throws ServiceException {
        if (name == null || name.isBlank()) {
            throw new ServiceException("Item name can not be empty");
        }
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new ServiceException("Item price can not be negative");
        }
    }

    private BigDecimal parsePrice(String price) throws ServiceException {
        if (price == null || price.isBlank()) {
            throw new ServiceException("Item price can not be empty");
        }
        try {
            return new BigDecimal(price);
        } catch (NumberFormatException e) {
            throw new ServiceException("Incorrect price format", e);
        }
    }
}
