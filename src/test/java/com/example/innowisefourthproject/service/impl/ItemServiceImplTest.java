package com.example.innowisefourthproject.service.impl;

import com.example.innowisefourthproject.dao.ItemDao;
import com.example.innowisefourthproject.entity.Item;
import com.example.innowisefourthproject.exception.DaoException;
import com.example.innowisefourthproject.exception.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    private ItemDao itemDao;

    private ItemServiceImpl itemService;

    @BeforeEach
    void setUp() {
        itemService = new ItemServiceImpl(itemDao);
    }

    @Test
    void addShouldReturnTrueWhenItemDataIsValid() throws DaoException, ServiceException {
        when(itemDao.insert(any(Item.class))).thenReturn(true);

        boolean result = itemService.add("Laptop", "Good laptop", "1200.00");

        assertTrue(result);

        ArgumentCaptor<Item> itemCaptor = ArgumentCaptor.forClass(Item.class);
        verify(itemDao).insert(itemCaptor.capture());

        Item savedItem = itemCaptor.getValue();

        assertEquals("Laptop", savedItem.getName());
        assertEquals("Good laptop", savedItem.getDescription());
        assertEquals(new BigDecimal("1200.00"), savedItem.getPrice());
    }

    @Test
    void addShouldReturnFalseWhenDaoReturnsFalse() throws DaoException, ServiceException {
        when(itemDao.insert(any(Item.class))).thenReturn(false);

        boolean result = itemService.add("Laptop", "Good laptop", "1200.00");

        assertFalse(result);
        verify(itemDao).insert(any(Item.class));
    }

    @Test
    void addShouldThrowServiceExceptionWhenNameIsEmpty() {
        ServiceException exception = assertThrows(
                ServiceException.class,
                () -> itemService.add("", "Description", "100.00")
        );

        assertEquals("Item name can not be empty", exception.getMessage());
        verifyNoInteractions(itemDao);
    }

    @Test
    void addShouldThrowServiceExceptionWhenNameIsNull() {
        ServiceException exception = assertThrows(
                ServiceException.class,
                () -> itemService.add(null, "Description", "100.00")
        );

        assertEquals("Item name can not be empty", exception.getMessage());
        verifyNoInteractions(itemDao);
    }

    @Test
    void addShouldThrowServiceExceptionWhenPriceIsEmpty() {
        ServiceException exception = assertThrows(
                ServiceException.class,
                () -> itemService.add("Laptop", "Description", "")
        );

        assertEquals("Item price can not be empty", exception.getMessage());
        verifyNoInteractions(itemDao);
    }

    @Test
    void addShouldThrowServiceExceptionWhenPriceIsNull() {
        ServiceException exception = assertThrows(
                ServiceException.class,
                () -> itemService.add("Laptop", "Description", null)
        );

        assertEquals("Item price can not be empty", exception.getMessage());
        verifyNoInteractions(itemDao);
    }

    @Test
    void addShouldThrowServiceExceptionWhenPriceIsIncorrect() {
        ServiceException exception = assertThrows(
                ServiceException.class,
                () -> itemService.add("Laptop", "Description", "abc")
        );

        assertEquals("Incorrect price format", exception.getMessage());
        verifyNoInteractions(itemDao);
    }

    @Test
    void addShouldThrowServiceExceptionWhenPriceIsNegative() {
        ServiceException exception = assertThrows(
                ServiceException.class,
                () -> itemService.add("Laptop", "Description", "-10")
        );

        assertEquals("Item price can not be negative", exception.getMessage());
        verifyNoInteractions(itemDao);
    }

    @Test
    void addShouldThrowServiceExceptionWhenDaoThrowsException() throws DaoException {
        when(itemDao.insert(any(Item.class)))
                .thenThrow(new DaoException(new RuntimeException("DB error")));

        ServiceException exception = assertThrows(
                ServiceException.class,
                () -> itemService.add("Laptop", "Description", "100.00")
        );

        assertEquals("Failed of creating item", exception.getMessage());
        verify(itemDao).insert(any(Item.class));
    }

    @Test
    void updateShouldReturnTrueWhenItemDataIsValid() throws DaoException, ServiceException {
        when(itemDao.update(any(Item.class))).thenReturn(true);

        boolean result = itemService.update(1L, "Phone", "New phone", "500.50");

        assertTrue(result);

        ArgumentCaptor<Item> itemCaptor = ArgumentCaptor.forClass(Item.class);
        verify(itemDao).update(itemCaptor.capture());

        Item updatedItem = itemCaptor.getValue();

        assertEquals(1L, updatedItem.getId());
        assertEquals("Phone", updatedItem.getName());
        assertEquals("New phone", updatedItem.getDescription());
        assertEquals(new BigDecimal("500.50"), updatedItem.getPrice());
    }

    @Test
    void updateShouldReturnFalseWhenDaoReturnsFalse() throws DaoException, ServiceException {
        when(itemDao.update(any(Item.class))).thenReturn(false);

        boolean result = itemService.update(1L, "Phone", "New phone", "500.50");

        assertFalse(result);
        verify(itemDao).update(any(Item.class));
    }

    @Test
    void updateShouldThrowServiceExceptionWhenIdIsIncorrect() {
        ServiceException exception = assertThrows(
                ServiceException.class,
                () -> itemService.update(0L, "Phone", "Description", "500")
        );

        assertEquals("Incorrect item id", exception.getMessage());
        verifyNoInteractions(itemDao);
    }

    @Test
    void updateShouldThrowServiceExceptionWhenNameIsEmpty() {
        ServiceException exception = assertThrows(
                ServiceException.class,
                () -> itemService.update(1L, "", "Description", "500")
        );

        assertEquals("Item name can not be empty", exception.getMessage());
        verifyNoInteractions(itemDao);
    }

    @Test
    void updateShouldThrowServiceExceptionWhenPriceIsIncorrect() {
        ServiceException exception = assertThrows(
                ServiceException.class,
                () -> itemService.update(1L, "Phone", "Description", "abc")
        );

        assertEquals("Incorrect price format", exception.getMessage());
        verifyNoInteractions(itemDao);
    }

    @Test
    void updateShouldThrowServiceExceptionWhenDaoThrowsException() throws DaoException {
        when(itemDao.update(any(Item.class)))
                .thenThrow(new DaoException(new RuntimeException("DB error")));

        ServiceException exception = assertThrows(
                ServiceException.class,
                () -> itemService.update(1L, "Phone", "Description", "500")
        );

        assertEquals("Could not update item", exception.getMessage());
        verify(itemDao).update(any(Item.class));
    }

    @Test
    void deleteShouldReturnTrueWhenIdIsValid() throws DaoException, ServiceException {
        when(itemDao.delete(1L)).thenReturn(true);

        boolean result = itemService.delete(1L);

        assertTrue(result);
        verify(itemDao).delete(1L);
    }

    @Test
    void deleteShouldReturnFalseWhenDaoReturnsFalse() throws DaoException, ServiceException {
        when(itemDao.delete(1L)).thenReturn(false);

        boolean result = itemService.delete(1L);

        assertFalse(result);
        verify(itemDao).delete(1L);
    }

    @Test
    void deleteShouldThrowServiceExceptionWhenIdIsIncorrect() {
        ServiceException exception = assertThrows(
                ServiceException.class,
                () -> itemService.delete(0L)
        );

        assertEquals("Incorrect item id", exception.getMessage());
        verifyNoInteractions(itemDao);
    }

    @Test
    void deleteShouldThrowServiceExceptionWhenDaoThrowsException() throws DaoException {
        when(itemDao.delete(1L))
                .thenThrow(new DaoException(new RuntimeException("DB error")));

        ServiceException exception = assertThrows(
                ServiceException.class,
                () -> itemService.delete(1L)
        );

        assertEquals("Could not delete item by id", exception.getMessage());
        verify(itemDao).delete(1L);
    }

    @Test
    void findAllShouldReturnItems() throws DaoException, ServiceException {
        List<Item> expectedItems = List.of(
                new Item(1L, "Laptop", "Good laptop", new BigDecimal("1200.00")),
                new Item(2L, "Mouse", "Wireless mouse", new BigDecimal("35.00"))
        );

        when(itemDao.findAll()).thenReturn(expectedItems);

        List<Item> actualItems = itemService.findAll();

        assertEquals(expectedItems, actualItems);
        verify(itemDao).findAll();
    }

    @Test
    void findAllShouldThrowServiceExceptionWhenDaoThrowsException() throws DaoException {
        when(itemDao.findAll())
                .thenThrow(new DaoException(new RuntimeException("DB error")));

        ServiceException exception = assertThrows(
                ServiceException.class,
                () -> itemService.findAll()
        );

        assertEquals("Failed to find all items", exception.getMessage());
        verify(itemDao).findAll();
    }

    @Test
    void findByIdShouldReturnItemWhenIdIsValid() throws DaoException, ServiceException {
        Item item = new Item(1L, "Laptop", "Good laptop", new BigDecimal("1200.00"));

        when(itemDao.findById(1L)).thenReturn(Optional.of(item));

        Optional<Item> result = itemService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(item, result.get());
        verify(itemDao).findById(1L);
    }

    @Test
    void findByIdShouldReturnEmptyWhenItemNotFound() throws DaoException, ServiceException {
        when(itemDao.findById(1L)).thenReturn(Optional.empty());

        Optional<Item> result = itemService.findById(1L);

        assertTrue(result.isEmpty());
        verify(itemDao).findById(1L);
    }

    @Test
    void findByIdShouldReturnEmptyWhenIdIsIncorrect() throws DaoException, ServiceException {
        when(itemDao.findById(0L)).thenReturn(Optional.empty());

        Optional<Item> result = itemService.findById(0L);

        assertTrue(result.isEmpty());
        verify(itemDao).findById(0L);
    }

    @Test
    void findByIdShouldThrowServiceExceptionWhenDaoThrowsException() throws DaoException {
        when(itemDao.findById(1L))
                .thenThrow(new DaoException(new RuntimeException("DB error")));

        ServiceException exception = assertThrows(
                ServiceException.class,
                () -> itemService.findById(1L)
        );

        assertEquals("Failed to findById items", exception.getMessage());
        verify(itemDao).findById(1L);
    }
}